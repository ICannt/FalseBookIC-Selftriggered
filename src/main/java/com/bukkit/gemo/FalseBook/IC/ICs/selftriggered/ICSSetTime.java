package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.block.SignChangeEvent;

public class ICSSetTime extends SelftriggeredBaseIC {

    private int newTime;
    private World myWorld;

    public ICSSetTime() {
        this.ICName = "SET-TIME";
        this.ICNumber = "ics.settime";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("", "", "");
        this.chipState.setLines("worldtime in ticks", "");
        this.ICDescription = "The MC0232 sets the time to the specified time every X serverticks.";
    }

    public void checkCreation(SignChangeEvent event) {
        if (!Parser.isInteger(event.getLine(1))) {
            SignUtils.cancelSignCreation(event, ChatColor.RED + "Line 3 must be a number.");
            return;
        }

        int value = Parser.getInteger(event.getLine(1), 13000);
        if ((value < 0) || (value > 23999)) {
            value = 0;
        }
        event.setLine(1, String.valueOf(value));
        event.setLine(2, "");
    }

    public boolean onLoad(String[] lines) {
        if (!Parser.isInteger(lines[1])) {
            return false;
        }
        this.newTime = Parser.getInteger(lines[1], 13000);
        this.myWorld = this.signBlock.getWorld();
        return true;
    }

    public void Execute() {
        this.myWorld.setTime(this.newTime);
    }
}