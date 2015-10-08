package com.mygdx.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.mygdx.game.Bridge;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.battlewindow.ContinuousGameFrame;

public class HtmlLauncher extends GwtApplication {

        public GwtApplicationConfiguration getConfig () {
                GwtApplicationConfiguration config = new GwtApplicationConfiguration(1056, 450);
                config.preferFlash = false;
                return config;
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new ContinuousGameFrame(new HtmlBridge());
        }

        private static class HtmlBridge implements Bridge {
                private ContinuousGameFrame game;
                @Override
                public void setGame(ContinuousGameFrame game) {
                        this.game = game;
                        exportPause2();
                        //game.animations[1].region = game.animations[1].getKeyFrame(0);
                        //game.animations[1].paused = true;
                }

                @Override
                public void pause2() {
                        //game.pauseSprites();
                }

                public native void exportPause2() /*-{
                        var that = this;
                        $wnd.pause2 = $entry(function() {
                                that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::pause2()();
                                $wnd.myFunction();
                        });
                }-*/;
        }

        @Override
        public Preloader.PreloaderCallback getPreloaderCallback() {
                return super.getPreloaderCallback();
        }
}