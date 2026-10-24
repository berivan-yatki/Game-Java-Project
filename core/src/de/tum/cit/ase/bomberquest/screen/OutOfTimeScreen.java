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
 * The OutOfTimeScreen class is shown when the player runs out of time.
 * <p>
 * Displays a message informing the player and provides a button to return to the main menu.
 * The screen mutes the game audio while active.
 * </p>
 */
public class OutOfTimeScreen implements Screen {

    /** The main game instance used for transitioning screens and accessing resources. */
    private final BomberQuestGame game;

    /** The stage that organizes and displays UI components for the out-of-time screen. */
    private final Stage stage;

    /**
     * Constructor for OutOfTimeScreen.
     * <p>
     * Sets up the camera, viewport, stage, and UI components, including a button to return to the main menu.
     *
     * @param game The main game instance for accessing resources and transitioning screens.
     */
    public OutOfTimeScreen(BomberQuestGame game) {
        this.game = game;
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f;
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.add(new Label("YOU ARE OUT OF TIME!\nGAME OVER!", game.getSkin(), "title")).padBottom(80).row();

        /**
         * Adds a "Go To Main Menu" button to the out-of-time screen.
         * <p>
         * When clicked, this button transitions the game back to the main menu screen.
         */
        TextButton goToMainMenuButton = new TextButton("Go To Main Menu", game.getSkin());
        table.add(goToMainMenuButton).width(300).row();
        goToMainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToMenu();
            }
        });
    }

    /**
     * Renders the out-of-time screen.
     * <p>
     * Clears the screen with a solid background color, updates the stage, and draws all UI components.
     * Additionally, mutes the game audio when the game-over screen is active.
     *
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.250f);
        ScreenUtils.clear(Color.SKY);
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
        stage.getViewport().update(width, height, true);
    }

    /** Disposes of the stage and its resources when the game-over screen is destroyed. */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Called when the out-of-time screen is shown.
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