package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.map.GameMap;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {
    /**
     * The size of a grid cell in pixels.
     * This allows us to think of coordinates in terms of square grid tiles
     * (e.g. x=1, y=1 is the bottom left corner of the map)
     * rather than absolute pixel coordinates.
     */
    public static final int TILE_SIZE_PX = 16;

    /**
     * The scale of the game.
     * This is used to make everything in the game look bigger or smaller.
     */
    public static final int SCALE = 4;
    private final BomberQuestGame game;
    private final SpriteBatch spriteBatch;
    private final GameMap map;
    private final Hud hud;
    private final OrthographicCamera mapCamera;
    private Viewport viewport;
    private float elapsedTime = 0f;
    private float time = 60.0f;
    private boolean isPaused = false;
    private int enemyNo = 0;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(BomberQuestGame game) {
        this.game = game;
        this.spriteBatch = game.getSpriteBatch();
        this.map = game.getMap();
        this.hud = new Hud(spriteBatch, game.getSkin().getFont("font"));
        this.mapCamera = new OrthographicCamera();
        this.enemyNo = map.getEnemyNo();
        System.out.println("Enemy no: " + enemyNo);
        float viewportWidth = map.getWidth() * TILE_SIZE_PX * SCALE;
        float viewportHeight = map.getHeight() * TILE_SIZE_PX * SCALE;
        this.mapCamera.setToOrtho(false, viewportWidth, viewportHeight);
        viewport = new ExtendViewport(viewportWidth, viewportHeight, mapCamera);
        updateCamera();
    }

    /**
     * Updates and renders the game every frame.
     * <p>
     * Processes input, updates game logic, and renders elements like the map and HUD.
     * Handles pausing the game when the ESC key is pressed and transitions to the pause menu.
     * Also manages the game timer and triggers the game-over screen when time runs out.
     * </p>
     *
     * @param deltaTime Time since the last frame, used for consistent updates.
     */

    @Override
    public void render(float deltaTime) {
        if (isPaused) {
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = true;
            game.goToPause();
            return;
        }

        game.muteGame();

        ScreenUtils.clear(Color.WHITE);

        float frameTime = Math.min(deltaTime, 0.250f);

        map.tick(frameTime);

        updateCamera();

        renderMap();

        elapsedTime += deltaTime;
        if (elapsedTime >= 1.0f) {
            time -= 1.0f;
            elapsedTime = 0;
        }

        int timeToAdd = 0;

        if (this.enemyNo > map.getEnemyNo()) {
            timeToAdd = (this.enemyNo - map.getEnemyNo()) * 15;
            System.out.println("Time to add: " + timeToAdd);
            time += timeToAdd;
            this.enemyNo = map.getEnemyNo();
        }
        
        if (time == 0) {
            game.goToOutOfTime();
        }

        hud.render(map.getPlayer().getBlastRadius(),
                map.getPlayer().getConcurrentBombs(),
                map.getPlayer().getBombNo(),
                map.getEnemyNo(),
                map.getIsExitVisible(),
                map.getPlayer().getIsShielded(),
                map.getPlayer().getSpeed(),
                time);
    }

    /**
     * Resumes the game from the paused state.
     * <p>
     * This method resets the {@code isPaused} flag, stops pause and menu music,
     * and starts the game music to continue gameplay.
     */
    public void resumeGame() {
        isPaused = false;
        MusicTrack.PAUSE_SCREEN.stop();
        MusicTrack.MENU_SCREEN.stop();
        MusicTrack.GAME_SCREEN.play();
    }

    /**
     * Updates the camera position to follow the player's current location.
     * <p>
     * Adjusts the camera to center on the player and updates the SpriteBatch's projection matrix.
     * </p>
     */
    private void updateCamera() {
        mapCamera.setToOrtho(false);
        game.getSpriteBatch().setProjectionMatrix(mapCamera.combined);
        mapCamera.position.x = (map.getPlayer().getX() + 0.5f) * TILE_SIZE_PX * SCALE;
        mapCamera.position.y = (map.getPlayer().getY() + 0.5f)* TILE_SIZE_PX * SCALE;
        mapCamera.update();
    }

    /**
     * Renders the game map and all drawable objects.
     * <p>
     * Sets the SpriteBatch to use the camera's view and draws each drawable object
     * on the map.
     * </p>
     */
    private void renderMap() {
        spriteBatch.setProjectionMatrix(mapCamera.combined);
        spriteBatch.begin();

        for (Drawable drawable : map.getAllDrawables()) {
            draw(spriteBatch, drawable);
        }

        spriteBatch.end();
    }

    /**
     * Draws a drawable object on the screen.
     * <p>
     * Scales the object's texture based on the game scale and tile size,
     * and renders it using the specified {@link SpriteBatch}.
     * Must be called between {@code spriteBatch.begin()} and {@code spriteBatch.end()}.
     * </p>
     *
     * @param spriteBatch The SpriteBatch used for rendering.
     * @param drawable    The drawable object to render.
     */
    private static void draw(SpriteBatch spriteBatch, Drawable drawable) {
        TextureRegion texture = drawable.getCurrentAppearance();
        float x = drawable.getX() * TILE_SIZE_PX * SCALE;
        float y = drawable.getY() * TILE_SIZE_PX * SCALE;
        float width = texture.getRegionWidth() * SCALE;
        float height = texture.getRegionHeight() * SCALE;
        spriteBatch.draw(texture, x, y, width, height);
    }

    /**
     * Adjusts the camera and viewport to match the new window size.
     *
     * @param width  The new width of the window.
     * @param height The new height of the window.
     */
    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}