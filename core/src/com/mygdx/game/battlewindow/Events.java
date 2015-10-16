package com.mygdx.game.battlewindow;

import com.mygdx.game.JSONPoke;

public class Events {
    private Events() {
    }

    /*
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
            Frame.HUDs[(side ? 0 : 1)].setChangeHPBattling(HP, duration);
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
    */


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
            Frame.HUDs[0].setHPBattling(percent, HP, duration);
            //          Log.e("Event", "SetHPBattlingAnimated " +  log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class HUDChangeBattling implements Event {
        JSONPoke poke;

        public HUDChangeBattling(JSONPoke poke) {
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
        JSONPoke poke;
        boolean side;


        public HUDChange(JSONPoke poke, boolean side) {
            this.poke = poke;
            this.side = side;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            Frame.HUDs[(side ? 0 : 1)].updatePoke(poke);
            //if (poke.status() == 31) {
            //    Frame.sprites[(side ? 0 : 1)].paused = true;
            //}
            //Log.e("Event", log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class SpriteChange implements Event {
        JSONPoke poke;
        boolean side;
        String path;

        public SpriteChange(JSONPoke poke, boolean side) {
            this.poke = poke;
            this.side = side;
            this.path = (side ? "back/" : "front/") + Short.toString(poke.num());
            if (poke.forme() > 0) {
                this.path = this.path + "_" + Byte.toString(poke.forme());
            }
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
//            Log.e("Event", "SpriteChange " + log + " to " + Thread.currentThread().getName() + " took: " + time);
            Frame.updateSprite(side, path, false);
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
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
            Frame.HUDs[(side ? 0 : 1)].updateStatus(status);
            if (status == 31) {
                Frame.sprites[(side ? 0 : 1)].paused = true;
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

        public int getId() {
            return id;
        }
    }

    /*
    public static class SendOut implements Event {
        JSONPoke poke;
        boolean side;
        String path;

        public SendOut(JSONPoke poke, boolean side) {
            this.poke = poke;
            this.side = side;
            this.path = (side ? "back/" : "front/") + Short.toString(poke.num());
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
//            Log.e("Event", "SpriteChange " + log + " to " + Thread.currentThread().getName() + " took: " + time);
            Frame.updateSprite(side, path, false);
        }

        public String getPath() {
            return path;
        }
    }
    */

    /*
    public static class SendBack implements Event {
        byte player;

        public SendBack(byte player) {
            this.player = player;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            Frame.sprites[player].visible = false;
        }
    }
    */

    public static class KO implements Event {
        boolean side;

        public KO(boolean side) {
            this.side = side;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            Frame.sprites[(side ? 0 : 1)].paused = true;
            Frame.sprites[(side ? 0 : 1)].startFade();
            Frame.HUDs[(side ? 0 : 1)].updateStatus(31); // 31 = Koed
        }
    }

    public static class Scale implements Event {
        boolean side;

        public Scale(boolean side) {
            this.side = side;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            Frame.sprites[(side ? 0 : 1)].paused = true;
            Frame.sprites[(side ? 0 : 1)].startScale();
        }
    }
 }
