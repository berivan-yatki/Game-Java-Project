package de.tum.cit.ase.bomberquest.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * The SoundEffect enum represents various sound effects used in the Bomber Quest game.
 * <p>
 * Each sound effect is associated with an audio file, which is loaded and managed through the enum.
 * This allows for centralized handling of game sound effects, ensuring proper playback, stopping, and disposal.
 */
public enum SoundEffect {

    /** The sound effect for bomb explosions. */
    BOMB_EXPLOSION("bomb_explosion.wav", 0.5f),

    /** The sound effect for dropping a bomb. */
    DROP_BOMB("drop_bomb.mp3", 0.5f),

    /** The sound effect for victory. */
    VICTORY("player_wins.wav", 0.5f),

    /** The sound effect for collecting a power-up. */
    POWER_UP("power_up.ogg", 0.5f),

    /** The sound effect for game over. */
    GAME_OVER("player_dies.ogg", 0.5f);

    /** The sound object associated with this effect. */
    private final Sound sound;

    /** The volume of the sound effect. */
    private float volume;

    /**
     * Constructs a SoundEffect instance.
     *
     * @param fileName The name of the audio file associated with the sound effect.
     * @param volume   The initial volume for the sound effect.
     */
    SoundEffect(String fileName, float volume) {
        this.sound = Gdx.audio.newSound(Gdx.files.internal("audio/" + fileName));
        this.volume = volume;
    }

    /** Plays the sound effect at the current volume. */
    public void play() {
        this.sound.play(volume);
    }

    /**
     * Stops the sound effect if it is currently playing.
     * <p>
     * This method has no usage currently but can be useful if looping sounds are added in the future.
     */
    public void stop() {
        this.sound.stop();
    }

    /** Disposes of the sound effect, freeing up resources. */
    public void dispose() {
        this.sound.dispose();
    }

    /** Mutes the sound effect by setting its volume to zero. */
    public void mute() {
        this.volume = 0;
    }

    /** Unmutes the sound effect by restoring its default volume. */
    public void unmute() {
        this.volume = 0.5f;
    }
}
