package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ExternalICPackage;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ICSVanisher extends SelftriggeredBaseIC {

    private boolean result;
    private int detectionRange = -1;
    private Vector offsetVector = null;
    private Location blockLoc = null;
    private ArrayList<FBItemType> itemList;

    public ICSVanisher() {
        this.ICName = "ITEM VANISHER";
        this.ICNumber = "ics.vanisher";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("Output", "", "");
        this.chipState.setLines("Radius[=OffsetX:OffsetY:OffsetZ] (i.e: 0=0:2:0 will check 2 Blocks above the IC-Block)", "items to remove");
        this.ICDescription = "The MC0265 outputs high if at least one specified item is removed in the given distance around the ic.";
    }

    public void checkCreation(SignChangeEvent event) {
        if (!Parser.isIntegerWithOffset(event.getLine(1))) {
            SignUtils.cancelSignCreation(event, "Line 3 must be a number or a number with a vector.");
            return;
        }

        Integer radius = Parser.getIntegerFromOffsetLine(event.getLine(1), 0);
        Vector vector = Parser.getVectorFromOffsetLine(event.getLine(1));
        if (radius < 0) {
            radius = 0;
        }
        if ((vector.getBlockX() != 0) || (vector.getBlockY() != 0) || (vector.getBlockZ() != 0)) {
            event.setLine(1, radius + "=" + vector.getBlockX() + ":" + vector.getBlockY() + ":" + vector.getBlockZ());
        } else {
            event.setLine(1, radius.toString());
        }
        if (SignUtils.parseLineToItemListWithSize(event.getLine(2), "-", false, 1, 9999) == null) {
            SignUtils.cancelSignCreation(event, "Please enter at least one item in Line 4.");
            return;
        }
    }

    public boolean onLoad(String[] lines) {
        try {
            if (!Parser.isIntegerWithOffset(lines[1])) {
                return false;
            }
            this.detectionRange = Parser.getIntegerFromOffsetLine(lines[1], 0);
            if (this.detectionRange < 0) {
                this.detectionRange = 0;
            }
            this.offsetVector = Parser.getVectorFromOffsetLine(lines[1]);

            this.itemList = SignUtils.parseLineToItemListWithSize(lines[2], "-", false, 1, 9999);

            return this.itemList != null;
        } catch (Exception e) {
            this.detectionRange = -1;
        }
        return false;
    }

    public void Execute() {
        this.result = false;
        this.blockLoc = getICBlock(this.signBlock, this.offsetVector);

        List<Entity> liste = this.signBlock.getWorld().getEntities();
        ItemStack stack = null;
        Entity ent = null;
        for (int counter = liste.size() - 1; counter >= 0; counter--) {
            ent = liste.get(counter);
            if (!(ent instanceof Item)) {
                continue;
            }
            stack = ((Item) ent).getItemStack();
            for (int i = 0; i < this.itemList.size(); i++) {
                if ((stack.getTypeId() != this.itemList.get(i).getItemID()) || ((!this.itemList.get(i).usesWildcart()) && (stack.getDurability() != this.itemList.get(i).getItemData()))
                        || (!BlockUtils.isInRange(ent.getLocation(), this.blockLoc, this.detectionRange))) {
                    continue;
                }
                ent.remove();
                this.result = true;
            }

        }

        liste.clear();
        liste = null;

        this.blockLoc = null;

        if (this.result != this.oldStatus) {
            this.oldStatus = this.result;
            switchLever(Lever.BACK, this.signBlock, this.result);
        }
    }
}