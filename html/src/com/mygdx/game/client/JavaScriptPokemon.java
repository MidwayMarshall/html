package com.mygdx.game.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.mygdx.game.JSONPoke;

public class JavaScriptPokemon extends JavaScriptObject implements JSONPoke {

    protected JavaScriptPokemon() {}

    @Override
    public final boolean shiny() {
        return getShiny();
    }

    @Override
    public final byte level() {
        return getLevel();
    }

    @Override
    public final String name() {
        return getName();
    }

    @Override
    public final short num() {
        return getNum();
    }

    @Override
    public final byte percent() {
        return getPercent();
    }

    @Override
    public final byte gender() {
        return getGender();
    }

    @Override
    public final int status() {
        return getStatus();
    }

    private native byte getGender() /*-{
        return this.gender;
    }-*/;

    private native byte getPercent() /*-{
        return this.percent;
    }-*/;
    private native byte getLevel() /*-{
        return this.level;
    }-*/;
    private native String getName() /*-{
        return this.name;
    }-*/;

    private native short getNum() /*-{
        return this.num;
    }-*/;

    private native boolean getShiny() /*-{
        return this.shiny;
    }-*/;

    private native int getStatus() /*-{
        return this.status;
    }-*/;

    public static native JavaScriptPokemon fromJS(String json) /*-{
        return eval('(' + json + ')');
    }-*/;

    public static native String pokemonName(int num) /*-{
        return $wnd.pokeinfo.name(num);
    }-*/;
}
