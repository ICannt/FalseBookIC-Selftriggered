package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.block.Block;
import org.bukkit.event.block.SignChangeEvent;

public class MC0270 extends SelftriggeredBaseIC {

    private Block myBlock = null;
    private boolean result;

    public MC0270() {
        setTypeID(11);
        this.ICName = "POWER SENSOR";
        this.ICNumber = "[MC0270]";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("Output: High if the block is powered", "", "");
        this.chipState.setLines("Y offset, with 0 being the IC block. Leave blank to default to the block below.", "");
        this.ICDescription = "The MC0270 checks for a powered block relative to the block behind the IC sign. By default it checks the block directly underneath but this can be changed.";
    }

    public void checkCreation(SignChangeEvent event) {
        event.setLine(3, "");

        if (event.getLine(2).length() < 1) {
            event.setLine(2, "-1");
        }

        String yOffset = event.getLine(2);
        try {
            if (yOffset.length() > 0) {
                Integer.parseInt(yOffset);
            }
        } catch (NumberFormatException e) {
            SignUtils.cancelSignCreation(event, "The third line must be a number or blank.");
            return;
        }
    }

    public boolean onLoad(String[] lines) {
        try {
            this.myBlock = getICBlock(this.signBlock).getBlock().getRelative(0, Integer.valueOf(lines[2]).intValue(), 0);
            return true;
        } catch (Exception e) {
            this.myBlock = null;
        }
        return false;
    }

    public void Execute() {
        this.result = ((this.myBlock.isBlockPowered()) || (this.myBlock.isBlockIndirectlyPowered()));
        if (this.result != this.oldStatus) {
            this.oldStatus = this.result;
            switchLever(Lever.BACK, this.signBlock, this.result);
        }
    }
}