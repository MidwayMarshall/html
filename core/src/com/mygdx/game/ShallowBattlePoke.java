package com.mygdx.game;

public class ShallowBattlePoke {
    public String rnick = "";
    public byte level = 0;
    public int fullStatus = 0;
    public byte gender = 0;
    public byte lifePercent = 0;

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
