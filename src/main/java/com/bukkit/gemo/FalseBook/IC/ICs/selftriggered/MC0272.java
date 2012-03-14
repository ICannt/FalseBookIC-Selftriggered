package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

public class MC0272 extends SelftriggeredBaseIC {

    private boolean result;
    private int detectionRange = -1;
    private Vector offsetVector = null;
    private String detectedString = null;
    private String detectionMode = null;

    public MC0272() {
        setTypeID(13);
        setICName("P-DETECTION");
        setICNumber("[MC0272]");
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("Output", "", "");
        this.chipState.setLines("Radius[=OffsetX:OffsetY:OffsetZ] (i.e: 0=0:2:0 will check 2 Blocks above the IC-Block)", "p:<part of a playername> OR g:<groupname> OR -g:<groupname>");
        this.ICDescription = "The MC0272 outputs high if a specified player is detected in the given distance around the ic.";
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

            String[] split = lines[3].split(":");
            if (split.length < 2) {
                return false;
            }
            this.detectionMode = split[0];
            this.detectedString = split[1];

            return (this.detectionMode.equalsIgnoreCase("p")) || (this.detectionMode.equalsIgnoreCase("g")) || (this.detectionMode.equalsIgnoreCase("-g"));
        } catch (Exception e) {
            this.detectionRange = -1;
            this.detectionMode = null;
            this.detectedString = null;
        }
        return false;
    }

    public void Execute() {
        this.result = false;
        Location blockLoc = getICBlock(this.signBlock, this.offsetVector);
        if (this.detectionMode.equalsIgnoreCase("p")) {
            Player[] playerList = Bukkit.getServer().getOnlinePlayers();
            for (Player player : playerList) {
                if ((player.isDead()) || (!player.isOnline())) {
                    continue;
                }
                if (player.getName().toLowerCase().indexOf(this.detectedString.toLowerCase()) <= -1) {
                    continue;
                }
                if (BlockUtils.isInRange(player.getLocation(), blockLoc, this.detectionRange)) {
                    this.result = true;
                    break;
                }
            }
            playerList = (Player[]) null;
        } else if (this.detectionMode.equalsIgnoreCase("g")) {
            Player[] playerList = Bukkit.getServer().getOnlinePlayers();
            for (Player player : playerList) {
                if ((player.isDead()) || (!player.isOnline())) {
                    continue;
                }
                if (!UtilPermissions.isPlayerInGroup(player, this.detectedString, this.signBlock.getWorld().getName())) {
                    continue;
                }
                if (BlockUtils.isInRange(player.getLocation(), blockLoc, this.detectionRange)) {
                    this.result = true;
                    break;
                }
            }
            playerList = (Player[]) null;
        } else if (this.detectionMode.equalsIgnoreCase("-g")) {
            Player[] playerList = Bukkit.getServer().getOnlinePlayers();
            for (Player player : playerList) {
                if ((player.isDead()) || (!player.isOnline())) {
                    continue;
                }
                if (!UtilPermissions.isPlayerInGroupWithoutInhetirance(player, this.detectedString, this.signBlock.getWorld().getName())) {
                    continue;
                }
                if (BlockUtils.isInRange(player.getLocation(), blockLoc, this.detectionRange)) {
                    this.result = true;
                    break;
                }
            }
            playerList = (Player[]) null;
        }
        blockLoc = null;

        if (this.result != this.oldStatus) {
            this.oldStatus = this.result;
            switchLever(Lever.BACK, this.signBlock, this.result);
        }
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
        if (event.getLine(3).length() < 0) {
            SignUtils.cancelSignCreation(event, "Please enter a Playername in Line 4");
            return;
        }
        String[] split = event.getLine(3).split(":");
        if (split.length < 2) {
            SignUtils.cancelSignCreation(event, "Wrong syntax in Line 4. Use p:<playername> or g:<groupname> OR -g:<groupname>");
            return;
        }
        if ((!split[0].equalsIgnoreCase("p")) && (!split[0].equalsIgnoreCase("g")) && (!split[0].equalsIgnoreCase("-g"))) {
            SignUtils.cancelSignCreation(event, "Wrong syntax in Line 4. Use p:<playername> or g:<groupname> or -g:<groupname>");
            return;
        }
    }
}