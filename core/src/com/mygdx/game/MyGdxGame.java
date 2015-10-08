package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.battlewindow.BattleInfoHUD;
import com.mygdx.game.battlewindow.SpriteAnimation;


public class MyGdxGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private BitmapFont font;
    private TextureAtlas battleAtlas;
    private Image background;
    public SpriteAnimation[] animations = new SpriteAnimation[2];
    private TextureAtlas[] atlases = new TextureAtlas[2];
    private Rectangle[] rects = new Rectangle[2];
    private BattleInfoHUD[] HUDs = new BattleInfoHUD[2];

    private final static byte me = 0, opp = 1;

    private float width;
    private float height;
    private float scaledX;
    private float scaledY;
    private float delta;
    private float elapsedTime = 0;

    private Bridge bridge;
    public MyGdxGame(Bridge bridge) {
        this.bridge = bridge;
    }

    public void pauseSprites() {
        animations[me].paused = !animations[me].paused;
        animations[opp].paused = !animations[opp].paused;
    }

    @Override
    public void create() {
        setupCalculations();
        setupGUI();
        loadAnimations();
        //this.bridge.setGame(this);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        background.draw(batch, 1f);

        animations[me].draw(elapsedTime, batch);
        animations[opp].draw(elapsedTime, batch);

        HUDs[me].draw(batch);
        HUDs[opp].draw(batch);

        batch.end();

        elapsedTime += Gdx.graphics.getDeltaTime();

        if (elapsedTime > 20 && elapsedTime < 30) {
            animations[me].visible = false;
            animations[opp].visible = false;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    private void loadAnimations() {
        {
            atlases[me] = new TextureAtlas(Gdx.files.internal("528.atlas"));
            Array<TextureRegion> regions = new Array<TextureRegion>();
            for (int i = 1; i < atlases[me].getRegions().size; i++) {
                regions.add(atlases[me].findRegion(StringFormat(i)));
            }
            animations[me] = new SpriteAnimation(0.1f, regions, Animation.PlayMode.LOOP);
            animations[me].fitInRectangle(rects[me], true);
        }

        {
            atlases[opp] = new TextureAtlas(Gdx.files.internal("246.atlas"));
            Array<TextureRegion> regions = new Array<TextureRegion>();
            for (int i = 1; i < atlases[opp].getRegions().size; i++) {
                regions.add(atlases[opp].findRegion(StringFormat(i)));
            }
            animations[opp] = new SpriteAnimation(0.1f, regions, Animation.PlayMode.LOOP);
            animations[opp].fitInRectangle(rects[opp], false);
        }
    }

    private void setupCalculations() {
        batch = new SpriteBatch();

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        scaledX = width / 400f;
        scaledY = height / 240f;

        rects[me] = new Rectangle(5 * scaledX, 5 * scaledY, scaledX * 155, scaledY * 90);
        rects[opp] = new Rectangle(width - (scaledX * 135) - 5 * scaledX, height - (scaledY * 135) - 5 * scaledY, scaledX * 135, scaledY * 130);
    }

    private void setupGUI() {
        battleAtlas = new TextureAtlas("battle3.txt");

        font = new BitmapFont(Gdx.files.internal("battle.fnt"), battleAtlas.findRegion("font"));
        font.getData().setScale(scaledX * 1.5f);

        background = new Image(new Texture(Gdx.files.internal("bakcground.png")));
        background.setWidth(width);
        background.setHeight(height);

        HUDs[me] = new BattleInfoHUD(battleAtlas, true, scaledX, font);
        //HUDs[me].additionalSetup(213);
        //HUDs[me].updateName("Swoobat", (byte) 2);
        //HUDs[opp] = new BattleInfoHUD(battleAtlas, false, scaledX, font);
        //HUDs[opp].updateName("Larvitar", (byte) 1);
    }

    private String StringFormat(Integer i) {
        // Because String.format won't transfer to html version
        String string = i + "";
        while (string.length() < 3) {
            string = "0" + string;
        }
        return string;
    }

    private native void alertPlz() /*-{
            $wnd.myFunction();
    }-*/;
}
