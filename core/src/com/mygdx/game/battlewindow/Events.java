package com.mygdx.game.battlewindow;


import com.mygdx.game.BattlePoke;
import com.mygdx.game.ShallowBattlePoke;

public class Events {
    private Events() {
    }

    public static class SetHPAnimated implements Event {
        byte HP;
        boolean side;
        float duration;

        public SetHPAnimated(byte HP, boolean side, float duration) {
            this.HP = HP;
            this.side = side;
            this.duration = duration;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            Frame.HUDs[(side ? 0 : 1)].setHP(HP, duration);
            //       Log.e("Event", "SetHPAnimated " + log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class SetHP implements Event {
        byte HP;
        boolean side;


        public SetHP(byte HP, boolean side) {
            this.HP = HP;
            this.side = side;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            Frame.HUDs[(side ? 0 : 1)].setHPNonAnimated(HP);

            //           Log.e("Event", "SetHP " + log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class SetHPBattling implements Event {
        byte percent;
        short HP;

        public SetHPBattling(byte percent, short HP) {
            this.percent = percent;
            this.HP = HP;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            Frame.HUDs[0].updateRealHealth(percent, HP);
            //        Log.e("Event", "SetHPBattling " + log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class SetHPBattlingAnimated implements Event {
        byte percent;
        short HP;
        float duration;

        public SetHPBattlingAnimated(byte percent, short HP, float duration) {
            this.percent = percent;
            this.HP = HP;
            this.duration = duration;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            Frame.HUDs[0].setHP(percent, HP, duration);
            //          Log.e("Event", "SetHPBattlingAnimated " +  log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class HUDChangeBattling implements Event {
        BattlePoke poke;

        public HUDChangeBattling(BattlePoke poke) {
            this.poke = poke;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            Frame.HUDs[0].updatePokeNonSpectating(poke);
            if (poke.status() == 31) {
                Frame.sprites[0].paused = true;
            }
            //Log.e("Event", log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class HUDChange implements Event {
        ShallowBattlePoke poke;
        boolean side;


        public HUDChange(ShallowBattlePoke poke, boolean side) {
            this.poke = poke;
            this.side = side;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            Frame.HUDs[(side ? 0 : 1)].updatePoke(poke);
            if (poke.status() == 31) {
                Frame.sprites[(side ? 0 : 1)].paused = true;
            }
            //Log.e("Event", log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class SpriteChange implements Event {
        boolean side;
        String path;
        boolean female;

        public SpriteChange(boolean side, String path, boolean female) {
            this.side = side;
            this.path = path;
            this.female = female;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
//            Log.e("Event", "SpriteChange " + log + " to " + Thread.currentThread().getName() + " took: " + time);
            Frame.updateSprite(side, path, female);
        }
    }

    public static class StatusChange implements Event {
        boolean side;
        int status;

        public StatusChange(boolean side, int status) {
            this.side = side;
            this.status = status;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            //Log.e("Event", log + " to " + Thread.currentThread().getName() + " took: " + time);
            Frame.HUDs[(side ? 1 : 0)].updateStatus(status);
            if (status == 31) {
                Frame.sprites[(side ? 1 : 0)].paused = true;
            }
        }
    }

    public static class BackgroundChange implements Event {
        int id;

        public BackgroundChange(int id) {
            this.id = id;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            Frame.setBackground(id);
        }
    }

    /*
    public static class StatChange implements Event {
        byte player;
        byte type;

        public StatChange(byte player, byte type) {
            this.player = player;
            this.type = type;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            Frame.addStatEffect(player, type);
        }
    }
    */
}
