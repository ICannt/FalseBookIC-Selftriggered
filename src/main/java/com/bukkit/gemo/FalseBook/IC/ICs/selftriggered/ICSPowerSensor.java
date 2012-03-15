package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.block.Block;
import org.bukkit.event.block.SignChangeEvent;

public class ICSPowerSensor extends SelftriggeredBaseIC {

    private Block myBlock = null;
    private boolean result;

    public ICSPowerSensor() {
        this.ICName = "POWER SENSOR";
        this.ICNumber = "ics.powersensor";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("Output: High if the block is powered", "", "");
        this.chipState.setLines("Y offset, with 0 being the IC block. Leave blank to default to the block below.", "");
        this.ICDescription = "The MC0270 checks for a powered block relative to the block behind the IC sign. By default it checks the block directly underneath but this can be changed.";
    }

    public void checkCreation(SignChangeEvent event) {
        event.setLine(2, "");

        if (event.getLine(1).length() < 1) {
            event.setLine(1, "-1");
        }

        String yOffset = event.getLine(1);
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
            this.myBlock = getICBlock(this.signBlock).getBlock().getRelative(0, Integer.valueOf(lines[1]).intValue(), 0);
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