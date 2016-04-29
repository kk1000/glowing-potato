package fi.tamk.tiko.orion.sleeprunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;

/**
 * Dialog for pause screen.
 */
public class PauseDialog extends Dialog {

    private SleepRunner sleepRunner;
    private Skin skin;

    private TextButton buttonOne;
    private TextButton buttonTwo;

    private String header;
    private String text;

    private Label headerLabel;
    private Label textLabel;

    /**
     * Constructor for PauseDialog.
     *
     * @param sleepRunner SleepRunner reference.
     * @param header      Dialog's header, can be empty.
     * @param text        Dialog's text, can be empty.
     * @param buttonOne   First button of the dialog.
     * @param buttonTwo   Second button of the dialog.
     */
    public PauseDialog(SleepRunner sleepRunner, String header, String text,
                       TextButton buttonOne, TextButton buttonTwo ) {
        super( "", sleepRunner.getSkin(), "dialog" );
        this.sleepRunner = sleepRunner;
        this.skin = sleepRunner.getSkin();
        this.header = header;
        this.text = text;
        this.buttonOne = buttonOne;
        this.buttonTwo = buttonTwo;

        // Texts.

        Table textTable = new Table();

        headerLabel = new Label( header, skin, "header" );
        headerLabel.setAlignment( Align.center );
        textLabel = new Label( text, skin, "default" );
        textLabel.setAlignment( Align.center );

        // Add texts to the text table.
        textTable.add( headerLabel ).center().pad( 10f );
        textTable.add( textLabel ).center().pad( 10f );

        // Add texts to the content table.
        getContentTable().add( textTable ).center().pad(20f);

        // Buttons.

        Table buttonTable = new Table();

        // Add buttons to the button table.
        buttonTable.add( buttonOne ).width( 150f ).height( 75f ).pad( 10f );
        buttonTable.add(buttonTwo).width(150f).height(75f).pad( 10f );

        // Add button table to the dialog.
        getButtonTable().add( buttonTable ).center().pad(20f);

        // Dialog settings.

        setHeight(getPrefHeight());
        setWidth(getPrefWidth());
        setModal(true);
        setMovable(false);
        setResizable(false);
    }

    /**
     * Sets header.
     *
     * @param header Header of the dialog.
     */
    public void setHeader( String header ) {
        headerLabel.setText( header );
    }

    /**
     * Sets text.
     *
     * @param text Text of the dialog.
     */
    public void setText( String text ) {
        textLabel.setText( text );
    }

}
