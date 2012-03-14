package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.entity.Slime;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

public class MC0271 extends SelftriggeredBaseIC {

    private boolean result;
    private Vector offsetVector = null;
    private int detectionRange = -1;
    private int detectionMode = -1;
    List<String> Types = new ArrayList<String>();

    public MC0271() {
        this.ICName = "DETECTION";
        this.ICNumber = "[MC0271]";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.Types.add("PLAYER");
        this.Types.add("MOBHOSTILE");
        this.Types.add("MOBPEACEFUL");
        this.Types.add("ANYMOB");
        this.Types.add("ANY");
        this.Types.add("CART");
        this.Types.add("STORAGECART");
        this.Types.add("POWEREDCART");
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("Output", "", "");
        this.chipState.setLines("Radius[=OffsetX:OffsetY:OffsetZ] (i.e: 0=0:2:0 will check 2 Blocks above the IC-Block)", "detection type");
        this.ICDescription = "The MC0271 outputs high if the specified type is detected in the given distance around the ic.<br /><br /><b>Detection types:</b><ul><li>PLAYER</li><li>MOBHOSTILE</li><li>MOBPEACEFUL</li><li>ANYMOB</li><li>ANY</li><li>CART</li><li>STORAGECART</li><li>POWEREDCART</li></ul><br /><br />";
    }

    public void checkCreation(SignChangeEvent event) {
        if (!Parser.isIntegerWithOffset(event.getLine(2))) {
            SignUtils.cancelSignCreation(event, "Line 3 must be a number or a number with a vector.");
            return;
        }

        Integer radius = Parser.getIntegerFromOffsetLine(event.getLine(2), 0);
        Vector vector = Parser.getVectorFromOffsetLine(event.getLine(2));
        if (radius < 0) {
            radius = 0;
        }
        if ((vector.getBlockX() != 0) || (vector.getBlockY() != 0) || (vector.getBlockZ() != 0)) {
            event.setLine(2, radius + "=" + vector.getBlockX() + ":" + vector.getBlockY() + ":" + vector.getBlockZ());
        } else {
            event.setLine(2, radius.toString());
        }
        if (event.getLine(3).length() > 0) {
            boolean f = false;
            for (int i = 0; i < this.Types.size(); i++) {
                if (this.Types.get(i).equalsIgnoreCase(event.getLine(3))) {
                    f = true;
                    event.setLine(3, this.Types.get(i));
                }
            }
            if (!f) {
                SignUtils.cancelSignCreation(event, "Type not found.");
                return;
            }
        } else {
            SignUtils.cancelSignCreation(event, "Please enter a Type in Line 4.");
            return;
        }
    }

    public boolean onLoad(String[] lines) {
        try {
            if (!Parser.isIntegerWithOffset(lines[2])) {
                return false;
            }
            this.detectionRange = Parser.getIntegerFromOffsetLine(lines[2], 0);
            if (this.detectionRange < 0) {
                this.detectionRange = 0;
            }
            this.offsetVector = Parser.getVectorFromOffsetLine(lines[2]);

            for (int i = 0; i < this.Types.size(); i++) {
                if (this.Types.get(i).equalsIgnoreCase(lines[3])) {
                    this.detectionMode = i;
                }
            }
            return this.detectionMode != -1;
        } catch (Exception e) {
            this.detectionRange = -1;
            this.detectionMode = -1;
        }
        return false;
    }

    public void Execute() {
        this.result = false;
        Location blockLoc = getICBlock(this.signBlock, this.offsetVector);
        int nowTyp = this.detectionMode;
        List<LivingEntity> liste = this.signBlock.getWorld().getLivingEntities();
        switch (nowTyp) {
            case 0:
                Player[] playerList = Bukkit.getServer().getOnlinePlayers();
                for (Player player : playerList) {
                    if ((player.isDead()) || (!player.isOnline())) {
                        continue;
                    }
                    if (BlockUtils.isInRange(player.getLocation(), blockLoc, this.detectionRange)) {
                        this.result = true;
                        break;
                    }
                }
                playerList = (Player[]) null;
                break;
            case 1:
                for (Entity ent : liste) {
                    if ((!(ent instanceof Monster)) && (!(ent instanceof Ghast)) && (!(ent instanceof Giant)) && (!(ent instanceof Slime))) {
                        continue;
                    }
                    if (ent.isDead()) {
                        continue;
                    }
                    if (BlockUtils.isInRange(ent.getLocation(), blockLoc, this.detectionRange)) {
                        this.result = true;
                        break;
                    }
                }
                liste.clear();
                liste = null;
                break;
            case 2:
                for (Entity ent : liste) {
                    if ((!(ent instanceof Animals)) && (!(ent instanceof Villager)) && (!(ent instanceof IronGolem)) && (!(ent instanceof Ocelot))) {
                        continue;
                    }
                    if (ent.isDead()) {
                        continue;
                    }
                    if (BlockUtils.isInRange(ent.getLocation(), blockLoc, this.detectionRange)) {
                        this.result = true;
                        break;
                    }
                }
                liste.clear();
                liste = null;
                break;
            case 3:
                for (Entity ent : liste) {
                    if ((!(ent instanceof Animals)) && (!(ent instanceof Monster)) && (!(ent instanceof Ghast)) && (!(ent instanceof Giant)) && (!(ent instanceof Slime)) && (!(ent instanceof Villager)) && (!(ent instanceof IronGolem)) && (!(ent instanceof Ocelot))) {
                        continue;
                    }
                    if (ent.isDead()) {
                        continue;
                    }
                    if (BlockUtils.isInRange(ent.getLocation(), blockLoc, this.detectionRange)) {
                        this.result = true;
                        break;
                    }
                }
                liste.clear();
                liste = null;
                break;
            case 4:
                for (Entity ent : liste) {
                    if ((!(ent instanceof Player)) && (!(ent instanceof Animals)) && (!(ent instanceof Monster)) && (!(ent instanceof Ghast)) && (!(ent instanceof Giant)) && (!(ent instanceof Slime)) && (!(ent instanceof Minecart)) && (!(ent instanceof StorageMinecart)) && (!(ent instanceof PoweredMinecart)) && (!(ent instanceof Villager))) {
                        continue;
                    }
                    if (ent.isDead()) {
                        continue;
                    }
                    if (BlockUtils.isInRange(ent.getLocation(), blockLoc, this.detectionRange)) {
                        this.result = true;
                        break;
                    }
                }
                liste.clear();
                liste = null;
                break;
            case 5:
                for (Entity ent : liste) {
                    if (!(ent instanceof Minecart)) {
                        continue;
                    }
                    if (ent.isDead()) {
                        continue;
                    }
                    if (BlockUtils.isInRange(ent.getLocation(), blockLoc, this.detectionRange)) {
                        this.result = true;
                        break;
                    }
                }
                liste.clear();
                liste = null;
                break;
            case 6:
                for (Entity ent : liste) {
                    if (!(ent instanceof StorageMinecart)) {
                        continue;
                    }
                    if (ent.isDead()) {
                        continue;
                    }
                    if (BlockUtils.isInRange(ent.getLocation(), blockLoc, this.detectionRange)) {
                        this.result = true;
                        break;
                    }
                }
                liste.clear();
                liste = null;
                break;
            case 7:
                for (Entity ent : liste) {
                    if (!(ent instanceof PoweredMinecart)) {
                        continue;
                    }
                    if (ent.isDead()) {
                        continue;
                    }
                    if (BlockUtils.isInRange(ent.getLocation(), blockLoc, this.detectionRange)) {
                        this.result = true;
                        break;
                    }
                }
                liste.clear();
                liste = null;
        }

        blockLoc = null;

        if (this.result != this.oldStatus) {
            this.oldStatus = this.result;
            switchLever(Lever.BACK, this.signBlock, this.result);
        }
    }
}