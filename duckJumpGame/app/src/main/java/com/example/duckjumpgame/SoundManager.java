package com.example.duckjumpgame;
import android.content.Context;
import android.media.MediaPlayer;

/**
 * The SoundManager class is designed to be instantiated in any class you need, and play a sound
 * effect from the "raw" resource folder. You can do this by creating a soundManager and passing
 * the current context as a param. Next call the play(int soundEffect) function where soundEffect is
 * the resource ID of the sound effect you want to play. For instance, quack.mp3 would be
 * playSound(R.raw.quack). The resetting and releasing of the MediaPlayer is done when the sound
 * stops.
 */

public class SoundManager {
    MediaPlayer mediaPlayer;
    Context currentContext;

    public SoundManager(Context currentContext){
        this.currentContext = currentContext; // Set current context since we need it as a param
    }


    //----------------------------------Play Sound Effect-----------------------------------------//

    /**
     * Plays a sound effect.
     * Handles all of the play, stop, and resetting/releasing of mediaPlayer
     *
     * @param soundEffect - The resource ID for the sound effect. ex. "R.raw.quack"
     *                      this way we only need the one function to play any sound effect
     */
    public void playSound(int soundEffect){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(currentContext, soundEffect);
        }
        // On completion, stop sound effect and reset mediaPlayer
        mediaPlayer.setOnCompletionListener(mediaPlayer -> stopSound());
        mediaPlayer.start();
    }

    /**
     * Handles the stopping of the quack sound effect and releases the mediaPlayer.
     * Is called by the quack function and OnCompletionListener.
     */
    protected void stopSound(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Similar to playSound however it is looping and does not end on completion,
     * instance will have to call stopSound() to stop.
     *
     * @param soundEffect - The resource ID for the sound effect. ex. "R.raw.quack"
     *                      this way we only need the one function to play any sound effect
     */
    public void loopSound(int soundEffect){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(currentContext, soundEffect);
        }
        // On completion, stop sound effect and reset mediaPlayer
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

}
