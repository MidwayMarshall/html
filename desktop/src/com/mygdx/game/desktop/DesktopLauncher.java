package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Bridge;
import com.mygdx.game.battlewindow.ContinuousGameFrame;
import com.mygdx.game.battlewindow.Event;
import com.mygdx.game.battlewindow.Events;

import java.util.Random;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 600;
		config.height = 360;
		config.resizable = false;
		new LwjglApplication(new ContinuousGameFrame(new DesktopBridge()), config);
	}

	private static class DesktopBridge implements Bridge {
		public ContinuousGameFrame game;

		@Override
		public void pauseSwitch() {

		}

		@Override
		public void setGame(ContinuousGameFrame game) {
			this.game = game;
			//game.animations[1].region = game.animations[1].getKeyFrame(0);
			//game.animations[1].paused = true;
		}

		Random random = new Random();

		@Override
		public void finished() {

		}

		@Override
		public void alert(boolean side) {
			//Event back = new Events.BackgroundChange(random.nextInt(1));
			//game.service.offer(back);
			Event log = new Events.LogEvent("THIS IS A REALLY LONG STRING YOU SHOULD WORK NICELY IF IT WRAPS PROPERLY");
			game.service.offer(log);
			System.out.println("ALERT!" + side);
		}
	}
}
