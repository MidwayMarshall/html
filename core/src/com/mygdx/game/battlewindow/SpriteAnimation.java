package com.mygdx.game.battlewindow;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class SpriteAnimation extends Animation {

    public SpriteAnimation(float frameDuration, Array<? extends TextureRegion> keyFrames) {
        super(frameDuration, keyFrames);
    }

    public SpriteAnimation(float frameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playMode) {
        super(frameDuration, keyFrames, playMode);
    }

    public SpriteAnimation(float frameDuration, TextureRegion... keyFrames) {
        super(frameDuration, keyFrames);
    }

    private float scale = 1f;

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void dispose() {
        getKeyFrame(0f).getTexture().dispose();
    }
    // Run bottomLeft or topRight before using this

    private float offX;
    private float offY;
    public boolean paused;
    public boolean visible = true;

    TextureRegion region;

    public void draw(float time, Batch batch) {
        if (!paused) {
            region = getKeyFrame(time);
        }
        offX = ((TextureAtlas.AtlasRegion) region).offsetX * scale;
        offY = ((TextureAtlas.AtlasRegion) region).offsetY * scale;
        batch.draw(region, x + offX, y + offY, region.getRegionWidth() * scale, region.getRegionHeight() * scale);
        //Gdx.gl.glDisable(GL20.GL_BLEND);
        //GLES20.glDisable(GLES20.GL_BLEND);
    }

    // 400/240
    private float x = 0f;
    private float y = 0f;

    public void fitInRectangle(Rectangle rect, boolean side) {
        float width = getKeyFrame(0f).getRegionWidth();
        float height = getKeyFrame(0f).getRegionHeight();
        if (width > height) {
            float scale = rect.width / width;
            if (side) {
                if (scale > 5.5f) scale = 5.5f;
            } else {
                if (scale > 4.0f) scale = 4.0f;
            }
            setScale(scale);
        } else {
            float scale = rect.height / height;
            if (side) {
                if (scale > 5.5f) scale = 5.5f;
            } else {
                if (scale > 4.0f) scale = 4.0f;
            }
            setScale(scale);
        }
        width = width * scale;
        height = height * scale;

        float difference = rect.width - width;
        x = rect.x + (difference / 2f);

        difference = rect.height - height;
        y = rect.y + (difference / 2f);
    }
}
