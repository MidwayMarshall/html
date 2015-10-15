package com.mygdx.game.client;


import com.google.gwt.user.client.Timer;
import com.mygdx.game.Bridge;
import com.mygdx.game.battlewindow.ContinuousGameFrame;
import com.mygdx.game.battlewindow.Event;
import com.mygdx.game.battlewindow.Events;

public class HtmlEvents {
    public static class DelayedEvent implements Event, DownloaderListener {
        /* If an asset doesn't exist the client need to download from the server and when finished fire the event that requires the resource */
        private Event event;
        private Bridge bridge;
        // to handle Paired downloads
        private int queueSize = 0;
        private int type;

        public DelayedEvent(Event event, Bridge bridge, int type) {
            this.event = event;
            this.bridge = bridge;
            this.type = type;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            switch (type) {
                case 0: {
                    break;
                }
                case 1: {
                    // Pokemon Sprite and Atlas;
                    queueSize = 2;
                    Events.SpriteChange sendevent = (Events.SpriteChange) this.event;
                    //Logger.println("Starting QueueEvent for atlas " + sendevent.getPath());
                    MyAssetChecker.checkText(sendevent.getPath() + ".atlas", this);
                    //Logger.println("Starting QueueEvent for image " + sendevent.getPath());
                    MyAssetChecker.checkImage(sendevent.getPath() + ".png", this);
                    break;
                }
                case 2: {
                    // Background Image
                    queueSize = 1;
                    Events.BackgroundChange backevent = (Events.BackgroundChange) this.event;
                    //Logger.println("Starting QueueEvent for background " + backevent.getId());
                    MyAssetChecker.checkImage("background/" + Integer.toString(backevent.getId()) + ".png", this);
                    break;
                }
                default: {
                    Logger.println("Unhandled Queue Event");
                }
            }
        }

        @Override
        public synchronized void finished() {
            queueSize--;
            //Logger.println("Queue remaining " + queueSize + " type: " + type);
            if (queueSize == 0) {
                bridge.addEvent(event);
            }
        }

        @Override
        public void failure(String url) {
            if (url.contains(".png")) {
                switch (type) {
                    case 1: {
                        // Could we revert the forme?
                        Events.SpriteChange sendevent = (Events.SpriteChange) this.event;
                        String path = sendevent.getPath();
                        if (path.contains("_")) {
                            // It is a forme
                            sendevent.setPath(path.substring(0, path.indexOf("_")));
                            // Try again without forme;
                            MyAssetChecker.checkText(sendevent.getPath() + ".atlas", this);
                            MyAssetChecker.checkImage(sendevent.getPath() + ".png", this);
                        } else {
                            Logger.println("404 Error " + path);
                        }
                        break;
                    }
                    default: {
                        Logger.println("404 Error");
                        break;
                    }
                }
            }
        }
    }

    public static class AnimatedHPEvent implements Event {
        private Bridge bridge;
        private byte change;
        private boolean side;
        private float duration;

        public AnimatedHPEvent(byte change, boolean side, float duration, Bridge bridge) {
            this.change = change;
            this.side = side;
            this.duration = duration;
            this.bridge = bridge;
        }

        @Override
        public void run(ContinuousGameFrame Frame) {
            //bridge.pause();
            Frame.HUDs[(side ? 0 : 1)].setChangeHP(change, duration/1000f);
            new Timer() {
                @Override
                public void run() {
                    finish();
                }
            }.schedule((int) (duration + 120));
        }

        private void finish() {
            bridge.unpause();
        }
    }
}
