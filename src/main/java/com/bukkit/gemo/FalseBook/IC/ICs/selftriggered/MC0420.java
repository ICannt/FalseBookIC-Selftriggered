package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.ICUtils;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.event.block.SignChangeEvent;

public class MC0420 extends SelftriggeredBaseIC
{
  private int pulserate = 1;
  private boolean isOn = true;
  private boolean canTurnOff = false;
  private int nowCount = 0;

  public MC0420() {
    setTypeID(4);
    this.ICName = "CLOCK";
    this.ICNumber = "[MC0420]";
    setICGroup(ICGroup.SELFTRIGGERED);
    this.chipState = new BaseChip(false, false, false, "Activated (only if Line 4 is \"TRUE\")", "", "");
    this.chipState.setOutputs("Output", "", "");
    this.chipState.setLines("The number of serverticks to wait before toggling state", "TRUE or FALSE or empty (=FALSE)");
    this.ICDescription = "MC0420 toggles its output state every X serverticks (if it is turned on, default: on).";
  }

  public void checkCreation(SignChangeEvent event)
  {
    if (event.getLine(2).length() < 1) {
      event.setLine(2, "5");
    }

    String yOffset = event.getLine(2);
    try {
      if (yOffset.length() > 0) {
        int yy = Integer.parseInt(yOffset);
        if (yy < 1)
          yy = 1;
        if (yy > 15) {
          yy = 15;
        }
        this.pulserate = yy;
        event.setLine(2, ((Integer)this.pulserate).toString());
      }
    } catch (NumberFormatException e) {
      SignUtils.cancelSignCreation(event, "The third line must be a number or be blank.");
      return;
    }

    if (event.getLine(3).length() > 0)
      try {
        Boolean.parseBoolean(event.getLine(3));
      } catch (Exception e) {
        SignUtils.cancelSignCreation(event, "The last line must be false, true or nothing.");
        return;
      }
  }

  public boolean onLoad(String[] lines)
  {
    try
    {
      this.pulserate = Integer.valueOf(lines[2]).intValue();
      if (lines[3].length() > 0)
        this.canTurnOff = Boolean.valueOf(lines[3]).booleanValue();
    } catch (Exception e) {
      this.canTurnOff = true;
      this.pulserate = 5;
    }
    return true;
  }

  public void Execute()
  {
    if (this.canTurnOff) {
      this.isOn = ICUtils.isInputHigh(this.signBlock, 1);
    }
    if (!this.isOn) {
      return;
    }

    this.nowCount += 1;
    if (this.nowCount >= this.pulserate) {
      this.nowCount = 0;
      this.oldStatus = (!this.oldStatus);
      switchLever(Lever.BACK, this.signBlock, this.oldStatus);
    }
  }
}