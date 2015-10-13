package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
		public void pause() {

		}

		@Override
		public void unpause() {

		}

		@Override
		public void finished() {

		}

		@Override
		public void setGame(ContinuousGameFrame game) {
			this.game = game;
		}

		@Override
		public void alert(String message) {

		}

		@Override
		public TextureAtlas getAtlas(String path) {
			return null;
		}

		@Override
		public Texture getTexture(String path) {
			return null;
		}

		@Override
		public BitmapFont getFont(String path) {
			return null;
		}

		@Override
		public void addEvent(Event event) {

		}

		@Override
		public void log(String text) {

		}
	}
}
