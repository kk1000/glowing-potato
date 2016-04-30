package fi.tamk.tiko.orion.sleeprunner.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import fi.tamk.tiko.orion.sleeprunner.SleepRunner;
import fi.tamk.tiko.orion.sleeprunner.data.Constants;
import fi.tamk.tiko.orion.sleeprunner.graphics.PauseDialog;
import fi.tamk.tiko.orion.sleeprunner.graphics.UIText;
import fi.tamk.tiko.orion.sleeprunner.screens.GameScreen;

/**
 * Class for pause stage, don't let the name fool you.
 * It is used to create question and game over dialog as well as pause dialog.
 */
public class PauseStage extends Stage {

    private GameScreen gameScreen;
    private SleepRunner game;

    private PauseDialog questionDialog;
    private PauseDialog gameOverDialog;
    private PauseDialog currentDialog;
    private PauseDialog pauseDialog;

    private TextButton continueTextButton;
    private TextButton mainMenuTextButton;
    private TextButton newGameTextButton;
    private TextButton falseTextButton;
    private TextButton trueTextButton;

    private String rightQuestionAnswer;

    private Skin skin;

    /**
     * Constructor for the pause stage.
     *
     * @param g             Reference to SleepRunner class.
     * @param worldCamera   Game's UI camera.
     * @param batch         The spritebatch.
     */
    public PauseStage(SleepRunner g, OrthographicCamera worldCamera,Batch batch ){
        super(new ScalingViewport(Scaling.stretch, Constants.APP_WIDTH, Constants.APP_HEIGHT, worldCamera), batch);
        this.game = g;
        this.skin = game.getSkin();
        this.gameScreen = game.getGameScreen();
        setButtons();
        setDialogs();
    }

    private void setDialogs( ) {
        questionDialog = new PauseDialog( game, "", game.translate.get( "random_fact1" ), trueTextButton, falseTextButton );
        gameOverDialog = new PauseDialog( game, game.translate.get( "game_over" ), "", newGameTextButton, mainMenuTextButton );
        pauseDialog = new PauseDialog( game, game.translate.get( "game_paused" ), "", continueTextButton, mainMenuTextButton );
    }

    /**
     * Sets all button attributes ready to use.
     */
    private void setButtons( ) {

        // Continue button.
        continueTextButton = new TextButton(game.translate.get("continue"), skin, "default");
        continueTextButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Continue button changes the game state back to the previous one.
                currentDialog.remove();
                gameScreen.setGameState(gameScreen.getPreviousGameState());
                return true;
            }
        });

        // New game button.
        newGameTextButton = new TextButton(game.translate.get("play_again"), skin, "default");
        newGameTextButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // New game button restarts the game and starts new one.
                currentDialog.remove();
                gameScreen.restartGame();
                return true;
            }
        });

        // Main menu button.
        mainMenuTextButton = new TextButton(game.translate.get("main_menu"), skin, "default");
        mainMenuTextButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Main menu button changes game screen back to the main menu.
                currentDialog.remove();
                game.switchToMainMenuMusic();
                game.setMainMenuScreen();
                return true;
            }
        });

        // False button.
        falseTextButton = new TextButton(game.translate.get("false_text"), skin, "default");
        falseTextButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // False button is one of the answers for dialog.
                if ( rightQuestionAnswer != null ) {
                    if (rightQuestionAnswer.equalsIgnoreCase("FALSE")) {
                        answeredQuestionRight();
                    } else {
                        answeredQuestionWrong();
                    }
                }
                // Return to RUNNING state.
                currentDialog.remove();
                gameScreen.setGameState( Constants.GAME_RUNNING );
                return true;
            }
        });

        // True button.
        trueTextButton = new TextButton(game.translate.get("true_text"), skin, "default");
        trueTextButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // True button is one of the answers for dialog.
                if ( rightQuestionAnswer != null ) {
                    if (rightQuestionAnswer.equalsIgnoreCase("TRUE")) {
                        answeredQuestionRight();
                    } else {
                        answeredQuestionWrong();
                    }
                } else {
                    Gdx.app.log( "PauseStage", "Question not found answer, ignored." );
                }
                // Return to the RUNNING state.
                currentDialog.remove();
                gameScreen.setGameState( Constants.GAME_RUNNING );
                return true;
            }
        });

    }

    /**
     * Player answered question dialog's question right.
     */
    public void answeredQuestionRight( )  {
        Gdx.app.log("PauseStage", "That is a correct answer!");
        UIText uiText = gameScreen.getUiStage().getUiText();
        uiText.setScore(uiText.getScore() + 50);
    }

    /**
     * Player answered question dialog's question wrong.
     */
    public void answeredQuestionWrong( )  {
        Gdx.app.log("PauseStage", "That is a wrong answer!");
        UIText uiText = gameScreen.getUiStage().getUiText();
        uiText.setScore( uiText.getScore() + 50 );
    }

    /**
     * Shows one of the dialog by given type.
     *
     * @param dialogType Dialog's type, "PAUSE", "GAMEOVER" or "QUESTION"
     */
    public void showDialog( String dialogType ) {
        if ( dialogType.equals( "PAUSE" ) ) {
            addActor(pauseDialog);
            currentDialog = pauseDialog;
        } else if ( dialogType.equals("GAMEOVER")) {
            addActor(gameOverDialog);
            currentDialog = gameOverDialog;
        } else if ( dialogType.equals( "QUESTION" ) ) {
            // Random new question to the question dialog.
            String[] questionData = game.translate.get( "random_fact" + MathUtils.random( 1, Constants.QUESTION_AMOUNT ) ).split( ";" );
            if ( questionData.length == 2 ) { rightQuestionAnswer = questionData[1]; }
            questionDialog.setText(questionData[0]);
            addActor(questionDialog);
            currentDialog = questionDialog;
        }
    }

}
