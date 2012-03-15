package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.block.SignChangeEvent;

public class ICSSetWeather extends SelftriggeredBaseIC {

    private World myWorld;
    private int mode = -1;
    private boolean isRain = false;
    private boolean isThunder = false;

    public ICSSetWeather() {
        this.ICName = "SET WEATHER";
        this.ICNumber = "ics.setweather";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("", "", "");
        this.chipState.setLines("SUN or RAIN or STORM", "");
        this.ICDescription = "The MC0285 sets the weather to the specified weather, whenever the weather changes.";
    }

    public void checkCreation(SignChangeEvent event) {
        event.setLine(2, "");
        String line = event.getLine(1);
        if ((!line.equalsIgnoreCase("sun")) && (!line.equalsIgnoreCase("rain")) && (!line.equalsIgnoreCase("storm"))) {
            SignUtils.cancelSignCreation(event, ChatColor.RED + "Line 3 must be sun, rain or storm");
            return;
        }
        event.setLine(1, line.toUpperCase());
    }

    public boolean onLoad(String[] lines) {
        this.myWorld = this.signBlock.getWorld();
        String line = lines[1];
        if ((!line.equalsIgnoreCase("sun")) && (!line.equalsIgnoreCase("rain")) && (!line.equalsIgnoreCase("storm"))) {
            this.mode = -1;
            return false;
        }
        if (line.equalsIgnoreCase("sun")) {
            this.mode = 0;
        } else if (line.equalsIgnoreCase("rain")) {
            this.mode = 1;
        } else if (line.equalsIgnoreCase("storm")) {
            this.mode = 2;
        }
        return true;
    }

    public void Execute() {
        this.isRain = this.myWorld.hasStorm();
        this.isThunder = this.myWorld.isThundering();
        if (this.mode == 0) {
            if ((this.isRain) || (this.isThunder)) {
                this.signBlock.getWorld().setStorm(false);
                this.signBlock.getWorld().setThundering(false);
            }
        } else if (this.mode == 1) {
            if ((!this.isRain) || (this.isThunder)) {
                this.signBlock.getWorld().setStorm(true);
                this.signBlock.getWorld().setThundering(false);
            }
        } else if (this.mode == 2) {
            if ((!this.isRain) || (!this.isThunder)) {
                this.signBlock.getWorld().setStorm(true);
                this.signBlock.getWorld().setThundering(true);
            }
        }
    }
}