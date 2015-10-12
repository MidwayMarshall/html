package com.mygdx.game;

public class BattlePoke  {
    public short currentHP = 0;
    public short totalHP = 0;

    public String rnick, nick = "", pokeName = "";
    int fullStatus = 0;
    public boolean shiny = false;
    public byte gender = 0;
    public byte lifePercent = 0;
    public byte level = 0;
    public byte lastKnownPercent = 0;
    public boolean sub = false;
    public Integer[][] stats = new Integer[2][6];

    public final int status() {
        if ((fullStatus & (1 << 31)) != 0)
            return 31;
        // intlog2(fullStatus & 0x3F)
        int x = fullStatus & 0x3F;
        int i;
        for (i = 0; x > 1; i++) {
            x/=2;
        }
        return i;
    }
}
