package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.block.Block;
import org.bukkit.event.block.SignChangeEvent;

public class ICSLightSensor extends SelftriggeredBaseIC {

    private Block myBlock = null;
    private boolean result;
    private int minLight = 15;

    public ICSLightSensor() {
        this.ICName = "LIGHT SENSOR";
        this.ICNumber = "ics.lightsensor";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("Output: High if the threshold is reached", "", "");
        this.chipState.setLines("Minimum light level, 0 to 15.", "");
        this.ICDescription = "The MC0262 checks to see if the light level of the block above the block behind the IC sign is greater than or equal to a configurable threshold.";
    }

    public void checkCreation(SignChangeEvent event) {
        event.setLine(2, "");

        if (event.getLine(1).length() < 1) {
            event.setLine(1, "15");
        }

        String minLight = event.getLine(1);
        try {
            if (minLight.length() > 0) {
                Integer.parseInt(minLight);
            }
        } catch (NumberFormatException e) {
            SignUtils.cancelSignCreation(event, "The third line must indicate the minimum light level.");
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
        this.result = (this.myBlock.getLightLevel() >= this.minLight);
        if (this.result != this.oldStatus) {
            this.oldStatus = this.result;
            switchLever(Lever.BACK, this.signBlock, this.result);
        }
    }
}