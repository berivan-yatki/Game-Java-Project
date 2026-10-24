package de.tum.cit.ase.bomberquest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.audio.SoundEffect;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.screen.*;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;

/**
 * The BomberQuestGame class represents the core of the Bomber Quest game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class BomberQuestGame extends Game {

    /**
     * Sprite Batch for rendering game elements.
     * This eats a lot of memory, so we only want one of these.
     */
    private SpriteBatch spriteBatch;

    /** The game's UI skin. This is used to style the game's UI elements. */
    private Skin skin;

    /**
     * The file chooser for loading map files from the user's computer.
     * This will give you access to a {@link com.badlogic.gdx.files.FileHandle} object,
     * which you can use to read the contents of the map file as a String, and then parse it into a {@link GameMap}.
     */
    private final NativeFileChooser fileChooser;

    /**
     * The map. This is where all the game objects are stored.
     * This is owned by {@link BomberQuestGame} and not by {@link GameScreen}
     * because the map should not be destroyed if we temporarily switch to another screen.
     */
    private GameMap map;

    /** Counter to track the number of times the "M" key has been pressed for muting/unmuting. */
    private int keyMPressCount;

    /**
     * Constructor for BomberQuestGame.
     *
     * @param fileChooser The file chooser for loading map files, typically used in desktop environments.
     */
    public BomberQuestGame(NativeFileChooser fileChooser) {
        this.fileChooser = fileChooser;
        this.keyMPressCount = 0;
    }

    /**
     * Called when the game is created.
     * <p>
     * Initializes the SpriteBatch, skin, and game map. Also transitions to the main menu screen.
     * </p>
     */
    @Override
    public void create() {
        this.spriteBatch = new SpriteBatch(); // Create SpriteBatch for rendering
        this.skin = new Skin(Gdx.files.internal("skin/craftacular/craftacular-ui.json"));
        this.map = new GameMap(this);
        goToMenu();
    }

    /**
     * Switches to the menu screen.
     * <p>
     * Stops all other music tracks and plays the menu screen music.
     * </p>
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this));
        MusicTrack.PAUSE_SCREEN.stop();
        MusicTrack.GAME_SCREEN.stop();
        MusicTrack.MENU_SCREEN.play();
    }

    /**
     * Switches to the game screen.
     * <p>
     * Stops other music tracks and starts the game screen music.
     * </p>
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this));
        MusicTrack.PAUSE_SCREEN.stop();
        MusicTrack.MENU_SCREEN.stop();
        MusicTrack.GAME_SCREEN.play();
    }

    /**
     * Switches to the pause screen.
     * <p>
     * Pauses the current game screen, stops other music tracks, and plays the pause menu music.
     * </p>
     */
    public void goToPause() {
        if (this.getScreen() instanceof GameScreen) {
            GameScreen currentGameScreen = (GameScreen) this.getScreen();
            currentGameScreen.pause();
            this.setScreen(new PauseScreen(this, currentGameScreen));
        }

        MusicTrack.MENU_SCREEN.stop();
        MusicTrack.GAME_SCREEN.stop();
        MusicTrack.PAUSE_SCREEN.play();
    }

    /**
     * Switches to the game-over screen.
     * <p>
     * Stops all music tracks and plays the game-over sound effect.
     * </p>
     */
    public void goToGameOver() {
        this.setScreen(new GameOverScreen(this));
        MusicTrack.PAUSE_SCREEN.stop();
        MusicTrack.MENU_SCREEN.stop();
        MusicTrack.GAME_SCREEN.stop();
        SoundEffect.GAME_OVER.play();
    }

    /**
     * Switches to the victory screen.
     * <p>
     * Stops all music tracks and plays the victory sound effect.
     * </p>
     */
    public void goToVictory() {
        this.setScreen(new VictoryScreen(this));
        MusicTrack.PAUSE_SCREEN.stop();
        MusicTrack.MENU_SCREEN.stop();
        MusicTrack.GAME_SCREEN.stop();
        SoundEffect.VICTORY.play();
    }

    /**
     * Switches to the out-of-time screen.
     * <p>
     * Stops all music tracks and plays the game-over sound effect.
     * </p>
     */
    public void goToOutOfTime() {
        this.setScreen(new OutOfTimeScreen(this));
        MusicTrack.PAUSE_SCREEN.stop();
        MusicTrack.MENU_SCREEN.stop();
        MusicTrack.GAME_SCREEN.stop();
        SoundEffect.GAME_OVER.play();
    }

    /** Returns the skin for UI elements. */
    public Skin getSkin() {
        return skin;
    }

    /** Returns the main SpriteBatch for rendering. */
    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    /** Returns the current map, if there is one. */
    public GameMap getMap() {
        return map;
    }

    /**
     * Switches to the given screen and disposes of the previous screen.
     * @param screen the new screen
     */
    @Override
    public void setScreen(Screen screen) {
        Screen previousScreen = super.screen;
        super.setScreen(screen);
        if (previousScreen != null) {
            previousScreen.dispose();
        }
    }

    /** Cleans up resources when the game is disposed. */
    @Override
    public void dispose() {
        getScreen().hide();
        getScreen().dispose();
        spriteBatch.dispose();
        skin.dispose();
        for (MusicTrack musicTrack : MusicTrack.values()) {
            musicTrack.dispose();
        }
        for (SoundEffect soundTrack : SoundEffect.values()) {
            soundTrack.dispose();
        }
    }

    /** Adding the getter for fileChooser. */
    public NativeFileChooser getFileChooser() {
        return fileChooser;
    }

    /**
     * Toggles the mute state of the game audio when the "M" key is pressed.
     */
    public void muteGame() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            if (keyMPressCount % 2 == 0) {
                MusicTrack.PAUSE_SCREEN.mute();
                MusicTrack.GAME_SCREEN.mute();
                MusicTrack.MENU_SCREEN.mute();
                SoundEffect.BOMB_EXPLOSION.mute();
                SoundEffect.POWER_UP.mute();
                SoundEffect.VICTORY.mute();
                SoundEffect.GAME_OVER.mute();
                SoundEffect.DROP_BOMB.mute();
            } else {
                MusicTrack.PAUSE_SCREEN.unmute();
                MusicTrack.GAME_SCREEN.unmute();
                MusicTrack.MENU_SCREEN.unmute();
                SoundEffect.BOMB_EXPLOSION.unmute();
                SoundEffect.POWER_UP.unmute();
                SoundEffect.VICTORY.unmute();
                SoundEffect.GAME_OVER.unmute();
                SoundEffect.DROP_BOMB.unmute();
            }
            keyMPressCount++;
            if (keyMPressCount == Integer.MAX_VALUE) {
                keyMPressCount = 1;
            }
            return;
        }
    }

    /**
     * Opens a native file chooser for selecting a map file, loads the selected map into the game,
     * and transitions to the game screen. Disposes the current map if one is already loaded.
     */
    public void openFileChooserAndLoadMap() {
        NativeFileChooserConfiguration conf = new NativeFileChooserConfiguration();
        conf.directory = Gdx.files.internal("maps");

        fileChooser.chooseFile(conf, new NativeFileChooserCallback() {
            @Override
            public void onFileChosen(com.badlogic.gdx.files.FileHandle file) {
                Gdx.app.log("FileChooser", "File chosen: " + file.path());

                if (map != null) {
                    map.dispose();
                }

                map = new GameMap(BomberQuestGame.this);
                map.loadMap(file.path());

                goToGame();
            }

            @Override
            public void onCancellation() {
                Gdx.app.log("FileChooser", "File selection cancelled");
            }

            @Override
            public void onError(Exception exception) {
                Gdx.app.error("FileChooser", "Error selecting file", exception);
            }
        });
    }
}
