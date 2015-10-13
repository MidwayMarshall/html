package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Bridge;
import com.mygdx.game.battlewindow.ContinuousGameFrame;
import com.mygdx.game.battlewindow.Event;
import com.mygdx.game.battlewindow.Events;
import com.mygdx.game.battlewindow.TaskService;

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
			Random random = new Random();
			int randomNum = random.nextInt(37);
			game.service.offer(new Events.BackgroundChange(randomNum));
		}

		@Override
		public TaskService setGame(ContinuousGameFrame game) {
			this.game = game;
			TaskService service = new DesktopService();
			Random random = new Random();
			int randomNum = random.nextInt(37);
			service.offer(new Events.BackgroundChange(randomNum));
			Poke me = new Poke((byte) (random.nextInt(95) + 5), "Test", (short) random.nextInt(600), (byte) random.nextInt(3), (byte) (random.nextInt(80) + 20), random.nextInt(7));
			Poke opp = new Poke((byte) (random.nextInt(100) + 5), "Test", (short) random.nextInt(600), (byte) random.nextInt(3), (byte) (random.nextInt(80) + 20), random.nextInt(7));
			service.offer(new Events.SpriteChange(me, true));
			service.offer(new Events.SpriteChange(opp, false));
			service.offer(new Events.HUDChange(me, true));
			service.offer(new Events.HUDChange(opp, false));
			return service;
		}

		@Override
		public void alert(String message) {
			log(message);
		}

		@Override
		public TextureAtlas getAtlas(String path) {
			return new TextureAtlas(Gdx.files.internal(path));
		}

		@Override
		public Texture getTexture(String path) {
			return new Texture(Gdx.files.internal(path));
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
			System.out.println(text);
		}
	}
}
