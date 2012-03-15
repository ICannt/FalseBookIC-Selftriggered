package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import org.bukkit.event.block.SignChangeEvent;

public class ICSSun extends SelftriggeredBaseIC {

    private boolean result;

    public ICSSun() {
        this.ICName = "IS IT SUNNY";
        this.ICNumber = "ics.sun";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("Output: High if it is sunny", "", "");
        this.ICDescription = "The MC0280 outputs high if it is sunny.";
    }

    public void checkCreation(SignChangeEvent event) {
        event.setLine(2, "");
        event.setLine(3, "");
    }

    public void Execute() {
        this.result = ((!this.signBlock.getWorld().hasStorm()) && (!this.signBlock.getWorld().isThundering()));
        if (this.result != this.oldStatus) {
            this.oldStatus = this.result;
            switchLever(Lever.BACK, this.signBlock, this.result);
        }
    }
}