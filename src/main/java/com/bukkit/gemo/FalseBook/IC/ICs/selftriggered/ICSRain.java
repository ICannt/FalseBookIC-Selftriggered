package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import org.bukkit.event.block.SignChangeEvent;

public class ICSRain extends SelftriggeredBaseIC {

    private boolean result;

    public ICSRain() {
        this.ICName = "IS IT RAINY";
        this.ICNumber = "ics.rain";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("Output: High if it is raining, but not thundering", "", "");
        this.ICDescription = "The MC0281 outputs high if it is raining, but not thundering";
    }

    public void checkCreation(SignChangeEvent event) {
        event.setLine(2, "");
        event.setLine(3, "");
    }

    public void Execute() {
        this.result = ((this.signBlock.getWorld().hasStorm()) && (!this.signBlock.getWorld().isThundering()));
        if (this.result != this.oldStatus) {
            this.oldStatus = this.result;
            switchLever(Lever.BACK, this.signBlock, this.result);
        }
    }
}