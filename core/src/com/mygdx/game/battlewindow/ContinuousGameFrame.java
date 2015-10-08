package com.mygdx.game.battlewindow;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Bridge;
import com.mygdx.game.TaskService2;

public class ContinuousGameFrame extends ApplicationAdapter implements InputProcessor {
    private static final String TAG = "ContinuousGameFrame";
    public TaskService service;

    public SpriteBatch batch;
    private BitmapFont font;
    private TextureAtlas battleAtlas;
    private Image background;
    public SpriteAnimation[] sprites = new SpriteAnimation[2];
    public TextureAtlas[] spriteAtlas = new TextureAtlas[2];
    public BattleInfoHUD[] HUDs = new BattleInfoHUD[2];

    private final byte me = 0;
    private final byte opp = 1;

    private final static int STATUS_INIT = 0;
    private final static int STATUS_RUNNING = 1;
    private final static int STATUS_PAUSED = 2;
    private int STATUS_CURRENT = STATUS_INIT;

    private float elapsedTime = 0f;
    private float width;
    private float height;
    private float scaledX;
    private float scaledY;
    private float delta;

    private Bridge bridge;

    public ContinuousGameFrame(Bridge bridge) {
        this.bridge = bridge;

    }

    long beginTime;

    @Override
    public void create() {
        if (bridge != null) {
            bridge.setGame(this);

            /*
            callBack.callForward(this);
            activity = callBack.hook();
            */
            service = new TaskService2();

            try {
                call();
                //activity.callForward(this, service);

                calculateGUI();

                setBackground(0);

                HUDs[me] = new BattleInfoHUD(battleAtlas, true, scaledX, font);
                HUDs[opp] = new BattleInfoHUD(battleAtlas, false, scaledX, font);

                Gdx.input.setInputProcessor(this);

            } catch (NullPointerException e) {
                e.printStackTrace();
                this.dispose();
            }
        } else {
            calculateGUI();
            setBackground(0);
            HUDs[me] = new BattleInfoHUD(battleAtlas, true, scaledX, font);
            HUDs[opp] = new BattleInfoHUD(battleAtlas, false, scaledX, font);
            updateSprite(true, "3", false);
            updateSprite(false, "5", false);
        }
    }

    protected void call() {
        // Handle in overridden extension
    }

    private final static int FRAME_TARGET = 1000 / 32;
    private int sleepTime;

    Event event;

    @Override
    public void render() {
        delta = Gdx.graphics.getDeltaTime();
        elapsedTime += delta;

        beginTime = System.currentTimeMillis();

        switch (STATUS_CURRENT) {
            case STATUS_INIT: {
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                event = service.take();
                if (event != null) {
                    //Log.e(TAG, "Event Received");
                    event.run(this);
                }
                if (background != null) {
                    batch.begin();
                    background.draw(batch, 1f);
                    batch.end();
                }
                break;
            }
            case STATUS_RUNNING: {
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                event = service.take();
                if (event != null) {
                    //Log.e(TAG, "Event Received");
                    event.run(this);
                }
                act();
                batch.begin();
                draw();
                batch.end();
                break;
            }
            case STATUS_PAUSED: {
                break;
            }
        }


        long timeDiff = System.currentTimeMillis() - beginTime;
        sleepTime = (int) (FRAME_TARGET - timeDiff);
        if (sleepTime > 0 && Gdx.graphics.getFramesPerSecond() > 25) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {

            }
        }
    }

    private void calculateGUI() {
        batch = new SpriteBatch();

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        scaledX = width / 400f;
        scaledY = height / 240;

        Rectangle.tmp.set(5 * scaledX, 5 * scaledY, scaledX * 155, scaledY * 175);
        Rectangle.tmp2.set(width - (scaledX * 135) - 5 * scaledX, height - (scaledY * 135) - 5 * scaledY, scaledX * 135, scaledY * 130);

        battleAtlas = new TextureAtlas(Gdx.files.internal("battle3.txt"));

        font = new BitmapFont(Gdx.files.internal("battle.fnt"), battleAtlas.findRegion("font"));
        font.getData().setScale(scaledX * 1.5f);
    }

    private void draw() {
        background.draw(batch, 1f);

        drawPokemon(me);
        drawPokemon(opp);

        HUDs[me].draw(batch);
        HUDs[opp].draw(batch);

        font.draw(batch, "" + Gdx.graphics.getFramesPerSecond(), 100, 100);
    }

    private void act() {
        HUDs[me].act(delta);
        HUDs[opp].act(delta);
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void dispose() {
        super.dispose();
        try {
            battleAtlas.dispose();
            font.dispose();
            batch.dispose();
            spriteAtlas[me].dispose();
            spriteAtlas[opp].dispose();
            ((TextureRegionDrawable) background.getDrawable()).getRegion().getTexture().dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkForFemaleAsset(boolean side, String path) {
        return Gdx.files.internal("data/sheets/" + (!side ? "front/atlas/" : "back/atlas/") + path + "f" + ".zz").exists();
    }

    public void updateSprite(boolean side, String path, boolean female) {
        if (!path.equals("0")) {
            if (side) {
                if (female && checkForFemaleAsset(true, path)) path = path + "f";
                //spriteAtlas[me] = GdxGZipAssetLoader.loadTextureAtlas(path, true);
                Array<TextureRegion> regions = new Array<TextureRegion>();
                if (spriteAtlas[me].findRegion("001") == null) {
                    for (int i = 1; i < spriteAtlas[me].getRegions().size; i++) {
                        regions.add(spriteAtlas[me].findRegion("" + i));
                    }
                } else {
                    for (int i = 1; i < spriteAtlas[me].getRegions().size; i++) {
                        regions.add(spriteAtlas[me].findRegion(StringFormat(i)));
                    }
                }
                sprites[me] = new SpriteAnimation(0.1f, regions, Animation.PlayMode.LOOP);
                sprites[me].fitInRectangle(Rectangle.tmp, true);
            } else {
                if (female && checkForFemaleAsset(false, path)) path = path + "f";
                //spriteAtlas[opp] = GdxGZipAssetLoader.loadTextureAtlas(path, false);
                Array<TextureRegion> regions = new Array<TextureRegion>();
                if (spriteAtlas[opp].findRegion("001") == null) {
                    for (int i = 1; i < spriteAtlas[opp].getRegions().size; i++) {
                        regions.add(spriteAtlas[opp].findRegion("" + i));
                    }
                } else {
                    for (int i = 1; i < spriteAtlas[opp].getRegions().size; i++) {
                        regions.add(spriteAtlas[opp].findRegion(StringFormat(i)));
                    }
                }
                sprites[opp] = new SpriteAnimation(0.1f, regions, Animation.PlayMode.LOOP);
                sprites[opp].fitInRectangle(Rectangle.tmp2, false);
            }
            if (STATUS_CURRENT == STATUS_INIT) {
                if (sprites[me] != null && sprites[opp] != null) {
                    if (sprites[me].getKeyFrames().length > 0 && sprites[opp].getKeyFrames().length > 0) {
                        STATUS_CURRENT = STATUS_RUNNING;
                    }
                }
            }
        }
    }

    public void drawPokemon(byte player) {
        sprites[player].draw(elapsedTime, batch);
    }

    public void setBackground(int id) {
        background = new Image(new Texture(Gdx.files.internal("bakcground.png")));
        background.setWidth(width);
        background.setHeight(height);
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Handle in overridden extension
        /*
        Log.e(TAG, "Rect 1:" + Rectangle.tmp.contains(screenX, screenY));
        if (Rectangle.tmp.contains(screenX, screenY)) {
            activity.DialogLooper(BattleActivity.BattleDialog.MyDynamicInfo.ordinal());
        }
        Log.e(TAG, "Rect 2:" + Rectangle.tmp2.contains(screenX, screenY));
        if (Rectangle.tmp2.contains(screenX, screenY)) {
            activity.DialogLooper(BattleActivity.BattleDialog.OppDynamicInfo.ordinal());
        }
        */
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    private String StringFormat(Integer i) {
        // Because String.format won't transfer to html version
        String string = i + "";
        while (string.length() < 3) {
            string = "0" + string;
        }
        return string;
    }
}
