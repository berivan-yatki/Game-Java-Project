package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The PauseScreen class is responsible for displaying the pause menu in the Bomber Quest game.
 * <p>
 * This screen provides options to resume the game, load a new map, or exit the game. It uses
 * LibGDX's {@link Screen} interface to manage user input and rendering while the game is paused.
 */
public class PauseScreen implements Screen {

    /** The main game instance used for screen transitions and resource management. */
    private final BomberQuestGame game;

    /** The stage that organizes and displays UI components for the pause menu. */
    private final Stage stage;

    /**
     * Constructor for PauseScreen.
     * <p>
     * Sets up the camera, viewport, stage, and UI components such as buttons for resuming the game,
     * loading a new map, or exiting the game.
     *
     * @param game       The main game instance for accessing resources and transitioning screens.
     * @param gameScreen The current game screen, to return to when resuming the game.
     */
    public PauseScreen(BomberQuestGame game, GameScreen gameScreen) {
        this.game = game;
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f;
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.add(new Label("GAME PAUSED", game.getSkin(), "title")).padBottom(80).row();

        /**
         * Adds a "Continue" button to the pause menu.
         * <p>
         * When clicked, this button resumes the game and switches back to the active game screen.
         */
        TextButton continueButton = new TextButton("Continue", game.getSkin());
        table.add(continueButton).width(200).padBottom(20).row();
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(gameScreen); // Return to the game screen
                gameScreen.resumeGame(); // Resume the game
            }
        });

        /**
         * Adds a "Load New Map" button to the pause menu.
         * <p>
         * When clicked, this button opens a file chooser to allow the user to select and load a new map.
         */
        TextButton loadMapButton = new TextButton("Load New Map", game.getSkin());
        table.add(loadMapButton).width(300).padBottom(20).row();
        loadMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.openFileChooserAndLoadMap();
            }
        });

        /**
         * Adds an "Exit Game" button to the pause menu.
         * <p>
         * When clicked, this button exits the game by pausing, disposing of resources, and calling {@code Gdx.app.exit()}.
         */
        TextButton exitButton = new TextButton("Exit Game", game.getSkin());
        table.add(exitButton).width(300).row();
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.pause();
                //game.dispose();
                Gdx.app.exit();
            }
        });
    }

    /**
     * Renders the pause menu.
     * <p>
     * Clears the screen with a teal background, updates the stage, and draws UI components such as buttons.
     * Mutes the game audio while the pause menu is active.
     * </p>
     *
     * @param deltaTime The time elapsed since the last frame, used for updating stage elements.
     */
    @Override
    public void render(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.250f);
        ScreenUtils.clear(Color.TAN);
        stage.act(frameTime);
        stage.draw();
        game.muteGame();
    }

    /**
     * Resizes the stage when the screen size changes.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    /** Disposes of the stage and its resources when the pause screen is destroyed. */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Called when the pause screen is shown.
     * <p>
     * Sets the input processor to the stage so it can receive user input.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}