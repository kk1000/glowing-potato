package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * This class is used to create ImageButtons more easily.
 */
public class GraphicButton extends ImageButton {
    public GraphicButton(Drawable imageUp, Drawable imageDown, Drawable imageChecked) {
        super(imageUp, imageDown, imageChecked);
    }
}
