package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import fi.tamk.tiko.orion.sleeprunner.data.Constants;

/**
 * Class for pause menu
 */
public class PauseMenu extends Actor {

    private GlyphLayout glyphLayout;
    private Texture texture;
    private BitmapFont font;
    private String text;
    private float textX;
    private float textY;

    /**
     * Constructor for the pause menu.
     *
     * @param x     X-position.
     * @param y     Y-position.
     * @param font  The game's general font.
     * @param text  Pause menu's text.
     */
    public PauseMenu(float x, float y, BitmapFont font, String text){
        this.texture = new Texture(Gdx.files.internal(Constants.PAUSEMENU_IMAGE_PATH));
        this.setX(x);
        this.setY(y);
        this.font = font;
        this.text = text;
        this.glyphLayout = new GlyphLayout(this.font, this.text);
        this.textX = ( x + this.texture.getWidth()/2 ) - this.glyphLayout.width/2;
        this.textY = ( y + this.texture.getHeight() ) - this.glyphLayout.height;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor());
        batch.draw(texture, getX(), getY());
        font.draw( batch, text, textX, textY );
    }

    @Override
    public void act(float delta){
        super.act(delta);
    }

}
