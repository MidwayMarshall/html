package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Bridge;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.battlewindow.ContinuousGameFrame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1056;
		config.height = 450;
		config.resizable = false;
		new LwjglApplication(new ContinuousGameFrame(new DesktopBridge()), config);
	}

	private static class DesktopBridge implements Bridge {
		public ContinuousGameFrame game;

		@Override
		public void pause2() {

		}

		@Override
		public void setGame(ContinuousGameFrame game) {
			this.game = game;
			//game.animations[1].region = game.animations[1].getKeyFrame(0);
			//game.animations[1].paused = true;
		}
	}
}
