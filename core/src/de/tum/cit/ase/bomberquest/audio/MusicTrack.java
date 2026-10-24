package de.tum.cit.ase.bomberquest.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * This enum is used to manage the music tracks in the game.
 * Currently, only one track is used, but this could be extended to include multiple tracks.
 * Using an enum for this purpose is a good practice, as it allows for easy management of the music tracks
 * and prevents the same track from being loaded into memory multiple times.
 * See the assets/audio folder for the actual music files.
 * Feel free to add your own music tracks and use them in the game!
 */
public enum MusicTrack {

    /** Background music for the menu screen. */
    MENU_SCREEN("menu_screen.mp3", 0.2f),

    /** Background music for the game screen. */
    GAME_SCREEN("game.mp3", 0.2f),

    /** Background music for the pause screen. */
    PAUSE_SCREEN("pause_screen2.mp3", 0.2f);

    /** The music file owned by this variant. */
    private final Music music;

    /**
     * Constructs a MusicTrack instance.
     *
     * @param fileName The name of the audio file associated with the track.
     * @param volume   The initial volume for the track.
     */
    MusicTrack(String fileName, float volume) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal("audio/" + fileName));
        this.music.setLooping(true);
        this.music.setVolume(volume);
    }

    /** Plays the music track. Does not stop other tracks automatically. */
    public void play() {
        this.music.play();
    }

    /** Stops the music track if it is currently playing. */
    public void stop() {
        this.music.stop();
    }

    /** Disposes of the music track, freeing up resources. */
    public void dispose() {
        this.music.dispose();
    }

    /** Mutes the music track by setting its volume to zero. */
    public void mute() {
        this.music.setVolume(0);
    }

    /** Unmutes the music track by restoring its default volume. */
    public void unmute() {
        this.music.setVolume(0.2f);
    }
}
