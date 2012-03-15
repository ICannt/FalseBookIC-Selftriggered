package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.ExternalICPackage;
import com.bukkit.gemo.FalseBook.IC.ICs.ICUpgrade;
import com.bukkit.gemo.FalseBook.IC.ICs.ICUpgraderMC;

public class SelftriggeredPackage extends ExternalICPackage {

    public SelftriggeredPackage() {
        setAPI_VERSION("1.1");

        setShowImportMessages(false);
        addIC(ICSRandom.class);
        addIC(ICSDay.class);
        addIC(ICSSetTime.class);
        addIC(ICSWaterSensor.class);
        addIC(ICSLavaSensor.class);
        addIC(ICSLightSensor.class);
        addIC(ICSBlockSensor.class);
        addIC(ICSItemSensor.class);
        addIC(ICSVanisher.class);
        addIC(ICSPowerSensor.class);
        addIC(ICSDetector.class);
        addIC(ICSPDetector.class);
        addIC(ICSSun.class);
        addIC(ICSRain.class);
        addIC(ICSStorm.class);
        addIC(ICSSetWeather.class);
        addIC(ICSClock.class);
        addIC(ICSRandomThree.class);
        
        ICUpgrade.addUpgrader("[MC0020]", new ICUpgraderMC("ics.random"));
        ICUpgrade.addUpgrader("[MC0230]", new ICUpgraderMC("ics.day"));
        ICUpgrade.addUpgrader("[MC0232]", new ICUpgraderMC("ics.settime"));
        ICUpgrade.addUpgrader("[MC0260]", new ICUpgraderMC("ics.watersensor"));
        ICUpgrade.addUpgrader("[MC0261]", new ICUpgraderMC("ics.lavasensor"));
        ICUpgrade.addUpgrader("[MC0262]", new ICUpgraderMC("ics.lightsensor"));
        ICUpgrade.addUpgrader("[MC0263]", new ICUpgraderMC("ics.blocksensor"));
        ICUpgrade.addUpgrader("[MC0264]", new ICUpgraderMC("ics.itemsensor"));
        ICUpgrade.addUpgrader("[MC0265]", new ICUpgraderMC("ics.vanisher"));
        ICUpgrade.addUpgrader("[MC0270]", new ICUpgraderMC("ics.powersensor"));
        ICUpgrade.addUpgrader("[MC0271]", new ICUpgraderMC("ics.detector"));
        ICUpgrade.addUpgrader("[MC0272]", new ICUpgraderMC("ics.pdetector"));
        ICUpgrade.addUpgrader("[MC0280]", new ICUpgraderMC("ics.sun"));
        ICUpgrade.addUpgrader("[MC0281]", new ICUpgraderMC("ics.rain"));
        ICUpgrade.addUpgrader("[MC0282]", new ICUpgraderMC("ics.storm"));
        ICUpgrade.addUpgrader("[MC0285]", new ICUpgraderMC("ics.setweather"));
        ICUpgrade.addUpgrader("[MC0420]", new ICUpgraderMC("ics.clock"));
        ICUpgrade.addUpgrader("[MC9999]", new ICUpgraderMC("ics.randomthree"));
    }
}