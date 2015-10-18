package com.mygdx.game.battlewindow;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;
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


    public static class SetHPBattlingAnimated extends Event {
        byte percent;
        short HP;
        float duration;
        int spot;

        public SetHPBattlingAnimated(int spot, byte percent, short HP, float duration) {
            this.spot = spot;
            this.percent = percent;
            this.HP = HP;
            this.duration = duration;
        }

        public int duration() {
            return (int) (duration * 1000);
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            Frame.getHUD(spot).setHPBattling(percent, HP, duration);
            //          Log.e("Event", "SetHPBattlingAnimated " +  log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class HUDChangeBattling extends InstantEvent {
        JSONPoke poke;
        int spot;

        public HUDChangeBattling(JSONPoke poke, int spot) {
            this.poke = poke;
            this.spot = spot;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            Frame.getHUD(spot).updatePokeNonSpectating(poke);
            if (poke.status() == 31) {
                //Frame.sprites[0].paused = true;
            }
            //Log.e("Event", log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class HUDChange extends InstantEvent {
        JSONPoke poke;
        int spot;


        public HUDChange(JSONPoke poke, int spot) {
            this.poke = poke;
            this.spot = spot;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            Frame.getHUD(spot).updatePoke(poke);
            //if (poke.status() == 31) {
            //    Frame.sprites[(side ? 0 : 1)].paused = true;
            //}
            //Log.e("Event", log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class SpriteChange extends InstantEvent {
        JSONPoke poke;
        int spot;
        String path;

        public SpriteChange(JSONPoke poke, int spot, boolean back) {
            this.poke = poke;
            this.spot = spot;
            this.path = (back ? "back/" : "front/") + Short.toString(poke.num());
            if (poke.forme() > 0) {
                this.path = this.path + "_" + Byte.toString(poke.forme());
            }
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
//            Log.e("Event", "SpriteChange " + log + " to " + Thread.currentThread().getName() + " took: " + time);
            Frame.updateSprite(spot, path, false);
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class StatusChange extends InstantEvent {
        int spot;
        int status;

        public StatusChange(int spot, int status) {
            this.spot = spot;
            this.status = status;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            //Log.e("Event", log + " to " + Thread.currentThread().getName() + " took: " + time);
            Frame.getHUD(spot).updateStatus(status);
            if (status == 31) {
                //Frame.sprites[(side ? 0 : 1)].paused = true;
            }
        }
    }

    public static class BackgroundChange extends InstantEvent {
        int id;

        public BackgroundChange(int id) {
            this.id = id;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            Frame.setBackground(id);
        }

        public int getId() {
            return id;
        }
    }

    public static class KO extends Event {
        int spot;

        public KO(int spot) {
            this.spot = spot;
        }

        public int duration() {
            return 800;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            //Frame.sprites[(side ? 0 : 1)].paused = true;
            //Frame.sprites[(side ? 0 : 1)].startFade();

            FadeToAction fade = new FadeToAction();
            fade.setDuration(0.8f);
            fade.setAlpha(0f);

            VisibleAction visible  = new VisibleAction();
            visible.setVisible(false);

            Frame.getSprite(spot).pause();
            Frame.getSprite(spot).addAction(Actions.sequence(fade, visible));
            Frame.getHUD(spot).updateStatus(31); // 31 = Koed
        }
    }

    public static class SendOut extends Event {
        int spot;

        public SendOut(int spot) {
            this.spot = spot;
        }

        public int duration() {
            return 600;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            ScaleToAction action = new ScaleToAction();
            action.setScale(Frame.getSprite(spot).originalScale);
            action.setInterpolation(Interpolation.pow2Out);
            action.setDuration(0.4f);

            FadeToAction fade = new FadeToAction();
            fade.setDuration(.2f);
            fade.setAlpha(1f);

            VisibleAction visible  = new VisibleAction();
            visible.setVisible(true);

            PauseAction unpause = new PauseAction();
            unpause.setPaused(false);

            Frame.getSprite(spot).addAction(Actions.sequence(visible, fade, action, unpause));
        }
    }

    public static class SendBack extends Event {
        int spot;

        public SendBack(int spot) {
            this.spot = spot;
        }

        public int duration() {
            return 600;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            //Frame.sprites[(side ? 0 : 1)].paused = true;
            //Frame.sprites[(side ? 0 : 1)].startScale();
            ScaleToAction action = new ScaleToAction();
            action.setScale(0.5f * Frame.getSprite(spot).originalScale);
            action.setInterpolation(Interpolation.pow2In);
            action.setDuration(.4f);

            FadeToAction fade = new FadeToAction();
            fade.setDuration(.2f);
            fade.setAlpha(0f);

            VisibleAction visible  = new VisibleAction();
            visible.setVisible(false);

            Frame.getSprite(spot).pause();
            Frame.getSprite(spot).addAction(Actions.sequence(action,fade,visible));
        }
    }
 }
