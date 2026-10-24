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
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.bomberquest.BomberQuestGame;

/**
 * The MenuScreen class is responsible for displaying the main menu of the Bomber Quest game.
 * <p>
 * It sets up the UI components for the menu, including buttons to load a map, start the game, or exit the application.
 * The class implements the LibGDX {@link Screen} interface, enabling it to manage user input and rendering for the menu.
 */
public class MenuScreen implements Screen {

    /** The stage used to organize and display UI elements on the menu screen. */
    private final Stage stage;

    /** Reference to the main game instance for transitioning screens and accessing resources. */
    private final BomberQuestGame game;

    /**
     * Constructor for MenuScreen.
     * <p>
     * Initializes the camera, viewport, stage, and UI elements, including buttons to load maps
     * and exit the game. Configures the layout and event listeners for user interaction.
     *
     * @param game The main game instance for accessing resources and transitioning screens.
     */

    public MenuScreen(BomberQuestGame game) {
        this.game = game;
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(new Label("WELCOME TO BOMBERMAN!", game.getSkin(), "title")).padBottom(80).row();

        /** Adding the button to load the map. */
        TextButton loadMapButton = new TextButton("Load Map", game.getSkin());
        table.add(loadMapButton).width(300).padBottom(20).row();

        loadMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.openFileChooserAndLoadMap();
            }
        });

        /** Adding the exit button. */
        TextButton exitButton = new TextButton("Exit Game", game.getSkin());
        table.add(exitButton).width(300).row();
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //game.pause();
                //game.dispose();
                Gdx.app.exit();
            }
        });
    }

    /**
     * Renders the menu screen.
     * <p>
     * Clears the screen, updates the stage, and draws the UI elements such as buttons and labels.
     * Also mutes the game audio while the menu is active.
     * </p>
     *
     * @param deltaTime The time elapsed since the last frame, used for updating animations and stage elements.
     */
    @Override
    public void render(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.250f); // Cap frame time to 250ms to prevent spiral of death        ScreenUtils.clear(Color.BLACK);
        ScreenUtils.clear(Color.ROYAL);
        stage.act(frameTime); // Update the stage
        stage.draw(); // Draw the stage
        this.game.muteGame();
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

    /** Disposes of the stage and its resources when the menu screen is destroyed. */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Called when the menu screen is shown.
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
