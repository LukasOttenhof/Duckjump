package com.example.duckjumpgame;

import android.os.Handler;
import android.widget.ImageView;

public class CreatePlatform extends AnimateAndDetectCollision {
    private Handler collisionHandler = new Handler();
    private Boolean stopRunnable = false;

    private DuckPlayer duckPlayer;

    private SoundManager soundEffect;

    private GameManager theGame;

    public CreatePlatform(ImageView platform, int screenWidth, int screenHeight, DuckPlayer duckPlayer, int duration, int respawnDelay, GameManager theGame) {
        super(platform, screenWidth, screenHeight, duckPlayer, duration, respawnDelay, theGame);
        this.duckPlayer = duckPlayer;
        this.soundEffect = new SoundManager(platform.getContext());
        this.theGame = theGame;
        collisionHandler.postDelayed(collisionChecker, 100);
    }

    /**
     * This runnable is checking for collision repeatedly until the game ends.
     * It uses the checkCollision method for collision, if collision is detected
     * the duck will jump and a the quack sound effect is played. It stops when
     * stopRunnable is set to true which happens when the game ends. The runnable
     * is set to a 100 millisecond delay so that when the duck goes up through a platform
     * the delay isnt so short that the duck jumps or quacks a second time
     */
    Runnable collisionChecker = new Runnable(){
        public void run(){
            // Check for collision and if duck is too high
            if (checkCollision() && duckPlayer.getDuckY() > 150){
                // If yes run jump and play sound effect
                soundEffect.playSound(R.raw.quack);
                duckPlayer.jump();
                theGame.calculateAndDisplayScore();
            }
            // Continue the collision check if game hasn't ended
            if(!stopRunnable){
                collisionHandler.postDelayed(this, 100);
            }
        }
    };
}
