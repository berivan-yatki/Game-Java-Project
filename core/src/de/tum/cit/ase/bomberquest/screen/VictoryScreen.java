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
 * The VictoryScreen class is responsible for displaying the victory screen in the Bomber Quest game.
 * <p>
 * This screen is shown when the player wins the game, providing an option to return to the main menu.
 * It uses LibGDX's {@link Screen} interface to handle rendering and user input.
 */
public class VictoryScreen implements Screen {

    /** The main game instance used for transitioning screens and accessing resources. */
    private final BomberQuestGame game;

    /** The stage that organizes and displays UI components for the victory screen. */
    private final Stage stage;

    /**
     * Constructor for VictoryScreen.
     * <p>
     * Sets up the camera, viewport, stage, and UI components, including a button to return to the main menu.
     *
     * @param game The main game instance for accessing resources and transitioning screens.
     */
    public VictoryScreen(BomberQuestGame game) {
        this.game = game;
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f;
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.add(new Label("YOU WON!", game.getSkin(), "title")).padBottom(80).row();

        /**
         * Adds a "Go To Main Menu" button to the victory screen.
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
     * Renders the victory screen.
     * <p>
     * Clears the screen with a pink background, updates the stage, and draws UI components like labels and buttons.
     * Mutes the game audio while the victory screen is active.
     * </p>
     *
     * @param deltaTime The time elapsed since the last frame, used for updating animations and stage elements.
     */
    @Override
    public void render(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.250f); // Cap frame time to 250ms to prevent spiral of death        ScreenUtils.clear(Color.BLACK);
        ScreenUtils.clear(Color.PINK);
        stage.act(frameTime); // Update the stage
        stage.draw(); // Draw the stage
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

    /** Disposes of the stage and its resources when the victory screen is destroyed. */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Called when the victory screen is shown.
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