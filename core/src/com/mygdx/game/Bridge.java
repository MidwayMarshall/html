package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.battlewindow.ContinuousGameFrame;
import com.mygdx.game.battlewindow.Event;
import com.mygdx.game.battlewindow.TaskService;

public interface Bridge {
    public void pause();
    public void unpause();
    public void finished();
    public TaskService setGame(ContinuousGameFrame game);
    public void alert(String message);
    public TextureAtlas getAtlas(String path);
    public Texture getTexture(String path);
    public BitmapFont getFont(String path);
    public void addEvent(Event event);
    /* Event will be played as soon as the current event finishes */
    public void addImmediateEvent(Event event);
    public void log(String text);
    public boolean isDebug();
}
