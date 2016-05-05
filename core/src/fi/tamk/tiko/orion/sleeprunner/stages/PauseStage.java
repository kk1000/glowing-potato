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
import fi.tamk.tiko.orion.sleeprunner.data.Preference;
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
    private TextButton gameOverMainMenuTextButton;
    private TextButton pauseMainMenuTextButton;
    private TextButton newGameTextButton;
    private TextButton falseTextButton;
    private TextButton trueTextButton;

    private String rightQuestionAnswer;
    private Preference preference;
    private Skin skin;

    /**
     * Constructor for the pause stage.
     *
     * @param gameScreen    GameScreen reference.
     * @param worldCamera   Game's UI camera.
     * @param batch         The spritebatch.
     */
    public PauseStage(GameScreen gameScreen, OrthographicCamera worldCamera,Batch batch ){
        super(new ScalingViewport(Scaling.stretch, Constants.APP_WIDTH, Constants.APP_HEIGHT, worldCamera), batch);
        this.gameScreen = gameScreen;
        this.game = gameScreen.getGame();
        this.skin = game.resources.skin;
        this.preference = new Preference();
        setButtons();
        setDialogs();
    }

    private void setDialogs( ) {
        questionDialog = new PauseDialog( game, game.translate.get("question"), game.translate.get( "random_fact1" ), trueTextButton, falseTextButton );
        gameOverDialog = new PauseDialog( game, game.translate.get("game_over"), "", newGameTextButton, gameOverMainMenuTextButton );
        pauseDialog = new PauseDialog( game, game.translate.get( "game_paused" ), "", continueTextButton, pauseMainMenuTextButton );
    }

    /**
     * Sets all button attributes ready to use.
     */
    private void setButtons( ) {

        // Continue button.
        continueTextButton = new TextButton(game.translate.get("continue"), skin, "default");
        continueTextButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Continue button changes the game state back to the previous one.
                currentDialog.remove();
                gameScreen.setGameState(gameScreen.getPreviousGameState());
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

        // Main menu buttons.
        gameOverMainMenuTextButton = new TextButton(game.translate.get("main_menu"), skin, "default");
        gameOverMainMenuTextButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Main menu button changes game screen back to the main menu.
                currentDialog.remove();
                game.switchToMainMenuMusic();
                game.setScreen( game.getMainMenuScreen() );
                return true;
            }
        });

        pauseMainMenuTextButton = new TextButton(game.translate.get("main_menu"), skin, "default");
        pauseMainMenuTextButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Main menu button changes game screen back to the main menu.
                currentDialog.remove();
                game.switchToMainMenuMusic();
                game.setScreen( game.getMainMenuScreen() );
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
        game.resources.answerRightSound.play( preference.getSoundVolume() );
        uiText.setScore(uiText.getScore() + 50);
    }

    /**
     * Player answered question dialog's question wrong.
     */
    public void answeredQuestionWrong( )  {
        Gdx.app.log("PauseStage", "That is a wrong answer!");
        UIText uiText = gameScreen.getUiStage().getUiText();
        game.resources.answerWrongSound.play(preference.getSoundVolume());
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
            currentDialog.setText( game.translate.format( "gainedScores", gameScreen.getUiStage().getUiText().getScore() ) );
        } else if ( dialogType.equals( "QUESTION" ) ) {
            // Random new question to the question dialog.
            String[] questionData = game.translate.get( "random_fact" + MathUtils.random( 1, Constants.QUESTION_AMOUNT ) ).split( ";" );
            if ( questionData.length == 2 ) { rightQuestionAnswer = questionData[1]; }
            questionDialog.setText(questionData[0]);
            addActor(questionDialog);
            currentDialog = questionDialog;
        }
    }

    public void refreshTexts(){
        continueTextButton.setText(game.translate.get("continue"));
        newGameTextButton.setText(game.translate.get("play_again"));
        gameOverMainMenuTextButton.setText(game.translate.get("main_menu"));
        pauseMainMenuTextButton.setText(game.translate.get("main_menu"));
        falseTextButton.setText(game.translate.get("false_text"));
        trueTextButton.setText(game.translate.get("true_text"));
        gameOverDialog.setHeader(game.translate.get("game_over"));
        questionDialog.setHeader(game.translate.get("question"));
        pauseDialog.setHeader(game.translate.get( "game_paused" ));
    }
}
