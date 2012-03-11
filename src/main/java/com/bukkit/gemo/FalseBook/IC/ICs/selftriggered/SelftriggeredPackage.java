package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.ExternalICPackage;

public class SelftriggeredPackage extends ExternalICPackage
{
  public SelftriggeredPackage()
  {
    setAPI_VERSION("1.1");

    setShowImportMessages(false);
    addIC(MC0020.class);
    addIC(MC0230.class);
    addIC(MC0232.class);
    addIC(MC0260.class);
    addIC(MC0261.class);
    addIC(MC0262.class);
    addIC(MC0263.class);
    addIC(MC0264.class);
    addIC(MC0265.class);
    addIC(MC0270.class);
    addIC(MC0271.class);
    addIC(MC0272.class);
    addIC(MC0280.class);
    addIC(MC0281.class);
    addIC(MC0282.class);
    addIC(MC0285.class);
    addIC(MC0420.class);
    addIC(MC9999.class);
  }
}