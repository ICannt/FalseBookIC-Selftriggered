package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MC0264 extends SelftriggeredBaseIC {

    private boolean result;
    private int detectionRange = -1;
    private Vector offsetVector = null;
    private Location blockLoc = null;
    private ArrayList<FBItemType> itemList;

    public MC0264() {
        this.ICName = "ITEM SENSOR";
        this.ICNumber = "ics.itemsensor";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("Output", "", "");
        this.chipState.setLines("Radius[=OffsetX:OffsetY:OffsetZ] (i.e: 0=0:2:0 will check 2 Blocks above the IC-Block)", "item to detect");
        this.ICDescription = "The MC0264 outputs high if the specified item is detected in the given distance around the ic.";
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
        if (SignUtils.parseLineToItemListWithSize(event.getLine(3), "-", false, 1, 9999) == null) {
            SignUtils.cancelSignCreation(event, "Please enter at least one item in Line 4.");
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

            this.itemList = SignUtils.parseLineToItemListWithSize(lines[3], "-", false, 1, 9999);

            return this.itemList != null;
        } catch (Exception e) {
            this.detectionRange = -1;
        }
        return false;
    }

    public void Execute() {
        this.result = false;
        this.blockLoc = getICBlock(this.signBlock, this.offsetVector);

        HashMap<String, Integer> foundList = new HashMap<String, Integer>();
        ArrayList<Item> bukkitItemList = (ArrayList<Item>) this.signBlock.getWorld().getEntitiesByClass(Item.class);
        ItemStack itemStack = null;
        for (Item item : bukkitItemList) {
            itemStack = item.getItemStack();
            for (int i = 0; i < this.itemList.size(); i++) {
                if ((itemStack.getTypeId() != this.itemList.get(i).getItemID()) || ((!this.itemList.get(i).usesWildcart()) && (itemStack.getDurability() != this.itemList.get(i).getItemData()))
                        || (!BlockUtils.isInRange(item.getLocation(), this.blockLoc, this.detectionRange))) {
                    continue;
                }
                int oldAmount = 0;
                if (foundList.containsKey(this.itemList.get(i).getString())) {
                    oldAmount = foundList.get(this.itemList.get(i).getString()).intValue();
                }
                oldAmount += itemStack.getAmount();
                foundList.put(this.itemList.get(i).getString(), Integer.valueOf(oldAmount));
                if ((this.itemList.get(i).getAmount() == -1) || (oldAmount >= this.itemList.get(i).getAmount())) {
                    this.result = true;
                    break;
                }
            }

        }

        bukkitItemList.clear();
        bukkitItemList = null;
        this.blockLoc = null;

        if (this.result != this.oldStatus) {
            this.oldStatus = this.result;
            switchLever(Lever.BACK, this.signBlock, this.result);
        }
    }
}