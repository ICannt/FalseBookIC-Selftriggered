package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.SignUtils;
import java.util.ArrayList;
import org.bukkit.block.Block;
import org.bukkit.event.block.SignChangeEvent;

public class ICSBlockSensor extends SelftriggeredBaseIC {

    private Block myBlock = null;
    private boolean result;
    private FBItemType item = null;

    public ICSBlockSensor() {
        this.ICName = "BLOCK SENSOR";
        this.ICNumber = "ics.blocksensor";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("Output: High if the block is present", "", "");
        this.chipState.setLines("Y offset, with 0 being the IC block. Leave blank to default to the block below.", "BlockID[:SubID]");
        this.ICDescription = "The MC0263 checks for the presence of a specified block relative to the block behind the IC sign. By default it checks the block directly underneath but this can be changed.";
    }

    public void checkCreation(SignChangeEvent event) {
        if (event.getLine(1).length() < 1) {
            event.setLine(1, "-1");
        }

        String yOffset = event.getLine(1);
        try {
            if (yOffset.length() > 0) {
                Integer.parseInt(yOffset);
            }
        } catch (NumberFormatException e) {
            SignUtils.cancelSignCreation(event, "The third line must be a number or be blank.");
            return;
        }

        if (event.getLine(1) == null) {
            SignUtils.cancelSignCreation(event, "Item not found");
            return;
        }

        String[] split = event.getLine(2).split(":");
        try {
            if (!BlockUtils.isValidBlock(Integer.valueOf(split[0]).intValue())) {
                SignUtils.cancelSignCreation(event, "Block not found");
                return;
            }
        } catch (Exception e) {
            if (!BlockUtils.isValidBlock(BlockUtils.getItemIDFromName(event.getLine(2)))) {
                SignUtils.cancelSignCreation(event, "Block not found");
                return;
            }
        }
    }

    public boolean onLoad(String[] lines) {
        try {
            this.myBlock = getICBlock(this.signBlock).getBlock().getRelative(0, Integer.valueOf(lines[1]).intValue(), 0);
            ArrayList<FBItemType> thisItems = SignUtils.parseLineToItemListWithSize(lines[2], "-", false, 1, 1);
            if (thisItems != null) {
                this.item = thisItems.get(0);
                thisItems.clear();
                thisItems = null;
                return true;
            }
            return false;
        } catch (Exception e) {
            this.myBlock = null;
        }
        return false;
    }

    public void Execute() {
        this.result = ((this.myBlock.getTypeId() == this.item.getItemID()) && ((this.myBlock.getData() == this.item.getItemData()) || (this.item.usesWildcart())));
        if (this.result != this.oldStatus) {
            this.oldStatus = this.result;
            switchLever(Lever.BACK, this.signBlock, this.result);
        }
    }
}