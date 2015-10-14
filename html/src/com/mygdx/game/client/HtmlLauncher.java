package com.mygdx.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.mygdx.game.Bridge;
import com.mygdx.game.battlewindow.ContinuousGameFrame;
import com.mygdx.game.battlewindow.Event;
import com.mygdx.game.battlewindow.Events;
import com.mygdx.game.battlewindow.TaskService;

public class HtmlLauncher extends GwtApplication {

    public GwtApplicationConfiguration getConfig () {
        GwtApplicationConfiguration config = new GwtApplicationConfiguration(600, 360);
        config.preferFlash = false;
        //Assets.init(getPreloaderBaseURL());
        return config;
    }

    @Override
    public ApplicationListener getApplicationListener () {
        return new ContinuousGameFrame(new HtmlBridge());
    }


    public static class HtmlBridge implements Bridge {
        byte me, opp;
        boolean isBattle;

        @Override
        public TextureAtlas getAtlas(String path) {
            return Assets.getAtlas(path);
        }

        @Override
        public Texture getTexture(String path) {
            return Assets.getTexture(path);
        }

        @Override
        public BitmapFont getFont(String path) {
            return Assets.getFont(path);
        }

        private ContinuousGameFrame game;

        @Override
        public TaskService setGame(ContinuousGameFrame game) {
            this.game = game;
            try {
                setCallBacks();
            } catch (Exception e) {
                Window.alert("can't find battle");
            }
            return new TaskService2();
        }

        @Override
        public void log(String text) {
            Logger.println(text);
        }

        @Override
        public void pause() {
            pausebattle();
        }

        @Override
        public void unpause() {
            unpausebattle();
        }

        private native void pausebattle() /*-{
            $wnd.battle.pause();
        }-*/;

        private native void unpausebattle() /*-{
            $wnd.battle.unpause();
        }-*/;

        @Override
        public void alert(String message) {
            //Window.alert("Alert from: " + side);
            if (message.equals("true")) {
                //game.HUDs[0].updatePoke(JavaScriptPokemon.fromJS(getPoke(0, 1)));
                Window.alert(getPoke(this.me, 0));
            } else if (message.equals("false")){
                //game.HUDs[1].updatePoke(JavaScriptPokemon.fromJS(getPoke(1, 1)));
                Window.alert(getPoke(this.opp, 0));
                //addEvent(new Events.LogEvent(getBattle()));
            } else {
                //Window.alert(message);
            }
        }

        @Override
        public void finished() {
            //Logger.println("finished");
            int randomNum = Random.nextInt(37);
            HtmlEvents.DelayedEvent event = new HtmlEvents.DelayedEvent(new Events.BackgroundChange(randomNum), this, 2);
            addEvent(event);
            me = (byte) getMe();
            opp = (byte) getOpp();
            isBattle = getIsBattle();
            //Logger.println("Is real? " + isBattle);
            //Logger.println("My Pokemon " + getPoke(this.me, 0));
            //Logger.println("Opp Pokemon " + getPoke(this.opp, 0));

            JavaScriptPokemon me = JavaScriptPokemon.fromJS(getPoke(this.me, 0));
            if (isBattle) {
                game.HUDs[0].updatePokeNonSpectating(me);
            } else {
                game.HUDs[0].updatePoke(me);
            }
            if (me.num() > 0) {
                HtmlEvents.DelayedEvent eventme = new HtmlEvents.DelayedEvent(new Events.SpriteChange(me, true), this, 1);
                addEvent(eventme);
            }

            JavaScriptPokemon opp = JavaScriptPokemon.fromJS(getPoke(this.opp, 0));
            game.HUDs[1].updatePoke(opp);
            if (opp.num() > 0) {
                HtmlEvents.DelayedEvent eventopp = new HtmlEvents.DelayedEvent(new Events.SpriteChange(opp, false), this, 1);
                addEvent(eventopp);
            }

            Timer t = new Timer() {
                @Override
                public void run() {
                    unpausebattle();
                }
            };

            t.schedule(2000);
        }

        @Override
        public void addEvent(Event event) {
            game.service.offer(event);
        }

        /*
        public void newEvent(int args) {
            String s = "";
            if (args == 1) {
                s = "Send Out";
            }
            if (args == 2) {
                s = "Send Back";
            }
            //Event event = new Events.LogEvent(s);
            //addEvent(event);
        }
        */

        public void dealWithSendOut(int player) {
            //Logger.println("sendout");
            JavaScriptPokemon poke = JavaScriptPokemon.fromJS(getPoke(player, 0));
            boolean side = player == me;
            Event event;
            if (side) {
                if (isBattle) {
                    event = new Events.HUDChangeBattling(poke);
                } else {
                    event = new Events.HUDChange(poke, true);
                }
            } else {
                event = new Events.HUDChange(poke, false);
            }
            addEvent(event);
            HtmlEvents.DelayedEvent event1 = new HtmlEvents.DelayedEvent(new Events.SpriteChange(poke, side), this, 1);
            addEvent(event1);
        }

        public void dealWithSendBack(int player) {
            //Logger.println("sendback");
            pausebattle();
            //Event event = new Events.SendBack((byte) player);
            //addEvent(event);
            Timer t = new Timer() {
                @Override
                public void run() {
                    unpausebattle();
                }
            };
            t.schedule(500);
        }

        public void dealWithStatusChange(int player, int status) {
            //Logger.println("statuschange");
            Event event = new Events.StatusChange(player == me, status);
            addEvent(event);
        }

        public void dealWithHpChange(int player, int change) {
            //Logger.println("hpchange");
            pausebattle();
            /*
            int duration = change;
            if (duration < 0) duration = -duration;
            if (duration > 100) duration = 100;*/
            boolean side = player == me;
            if (side && isBattle) {
                dealWithHpChangeBattling();
            } else {
                Event event = new HtmlEvents.AnimatedHPEvent((byte) change, side, /*duration * 30*/1000, this);
                addEvent(event);
            }
        }

        private void dealWithHpChangeBattling() {
            JavaScriptPokemon my = JavaScriptPokemon.fromJS(getPoke(me, 0));
            Logger.println("new percent " + my.percent() + "  " + my.life());
            Event event = new Events.SetHPBattlingAnimated(my.percent(), my.life(), 2f);
            addEvent(event);
            new Timer() {
                @Override
                public void run() {
                    unpausebattle();
                }
            }.schedule(1000);
        }

        public void dealWithKo(int player) {
            //Logger.println("KO");
            pausebattle();
            Event event = new Events.KO(player == me);
            addEvent(event);
            Timer t = new Timer() {
                @Override
                public void run() {
                    unpausebattle();
                }
            };
            t.schedule(1000);
        }

        private native void setCallBacks() /*-{
        var that = this;
        $wnd.battle.on("sendout", function(player) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithSendOut(I)(player);
        });
        $wnd.battle.on("sendback", function(player) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithSendBack(I)(player);
        });
        $wnd.battle.on("ko", function(player) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithKo(I)(player);
        });
        $wnd.battle.on("statuschange", function(player, status) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithStatusChange(II)(player, status);
        });
        $wnd.battle.on("hpchange", function(player, change) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithHpChange(II)(player, change);
        });
        }-*/;

        private static native int battleId() /*-{
        return $wnd.battleId;
        }-*/;

        private static native String getPoke(int side, int slot) /*-{
        var poke = $wnd.battle.teams[side][slot];
        return JSON.stringify(poke);
        }-*/;

        private static native String getBattle() /*-{
            var battle = $wnd.battle;
            return JSON.stringify(battle);
        }-*/;

        private static native String getTeam(int side) /*-{
            var team = $wnd.battle.teams[side];
            return JSON.stringify(team);
        }-*/;

        private static native int getMe() /*-{
            var me = $wnd.battle.myself;
            return me;
        }-*/;

        private static native int getOpp() /*-{
            var opp = $wnd.battle.opponent;
            return opp;
        }-*/;

        private static native boolean getIsBattle() /*-{
            var isBattle = $wnd.battle.isBattle();
            return isBattle;
        }-*/;
    }


    @Override
    public Preloader.PreloaderCallback getPreloaderCallback() {
        return new Preloader.PreloaderCallback() {
            @Override
            public void update(Preloader.PreloaderState state) {
                // like update(stateTime) but update(stat.getProgess())
            }

            @Override
            public void error(String file) {
                System.out.println("error: " + file);
            }
        };
    }



    /*
    public LoadingListener getLoadingListener() {
        return new LoadingListener() {
            @Override
            public void beforeSetup() {
                // Do something!
            }

            @Override
            public void afterSetup() {
                // Do something!
            }
        };
    }
    */


    @Override
    public String getPreloaderBaseURL() {
        return GWT.getHostPageBaseURL() + "public/battle/";
    }
}
