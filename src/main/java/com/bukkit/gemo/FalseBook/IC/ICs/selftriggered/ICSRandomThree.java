package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.ICUtils;
import java.util.Random;
import org.bukkit.event.block.SignChangeEvent;

public class ICSRandomThree extends SelftriggeredBaseIC {

    Random rGen = new Random();
    boolean[] oldStatus = new boolean[3];

    public ICSRandomThree() {
        this.ICName = "3-BIT RANDOM";
        this.ICNumber = "ics.randomthree";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("Random bit 1", "Random bit 2", "Random bit 3");
        this.ICDescription = "The MC9999 generates three random bits every X serverticks.";
    }

    public boolean onLoad(String[] lines) {
        this.oldStatus[0] = ICUtils.isLeverActive(this.signBlock);
        this.oldStatus[1] = ICUtils.isLeftLeverActive(this.signBlock);
        this.oldStatus[2] = ICUtils.isRightLeverActive(this.signBlock);
        return true;
    }

    public void checkCreation(SignChangeEvent event) {
        event.setLine(1, "");
        event.setLine(2, "");
    }

    public void Execute() {
        boolean newStatus = this.rGen.nextBoolean();
        if (newStatus != this.oldStatus[0]) {
            this.oldStatus[0] = newStatus;
            switchLever(Lever.BACK, this.signBlock, newStatus);
        }
        newStatus = this.rGen.nextBoolean();
        if (newStatus != this.oldStatus[1]) {
            this.oldStatus[1] = newStatus;
            switchLever(Lever.LEFT, this.signBlock, newStatus);
        }
        newStatus = this.rGen.nextBoolean();
        if (newStatus != this.oldStatus[2]) {
            this.oldStatus[2] = newStatus;
            switchLever(Lever.RIGHT, this.signBlock, newStatus);
        }
    }
}