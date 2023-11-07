package com.example.duckjumpgame;

import android.os.Handler;
import android.widget.ImageView;

public class CreateHazard extends AnimateAndDetectCollision {
    private Handler collisionHandler = new Handler();
    private DuckPlayer duckPlayer;
    private SoundManager soundEffect;
    private GameManager theGame;

    /**
     * In the constructor startFallAnimation is called right away because we want the animations to
     * begin when the user opens the game, the collision handler is also called so that collision
     * will start to be detected.
     *
     * @param platform The Imageview of the platform that is being animated.
     * @param screenWidth Maximum width the platform can respawn at.
     * @param screenHeight Bottom of the screen, endpoint of the animation.
     * @param duckPlayer The duckObject that manages the duck.
     * @param duration Amount of time the platform falling animation will take.
     * @param respawnDelay Amount of time between animations of the platform falling.
     * @param theGame This is the game manager that is running the game, we need access to it here
     * so that once collision is corrected.
     */

    public CreateHazard(ImageView platform, int screenWidth, int screenHeight,
                        DuckPlayer duckPlayer, int duration, int respawnDelay, GameManager theGame){
        super(platform, screenWidth, screenHeight, duckPlayer, duration, respawnDelay);
        this.duckPlayer = duckPlayer;
        this.soundEffect = new SoundManager(platform.getContext());
        collisionHandler.postDelayed(collisionChecker, 100);
        this.theGame = theGame;
        startFallAnimation(); // Initial hazard animation before the delayed ones
        // Schedule next animations and respawns in a loop
    }

    /**
     * This runnable is checking for collision repeatedly until the game ends.
     * It uses the checkCollision method for collision, if collision is detected
     * the endGame function in game manager will be called ending the game.
     */
    Runnable collisionChecker = new Runnable(){
        public void run(){
            // Check for collision and if duck is too high
            if (checkCollision() && duckPlayer.getDuckY() > 150){
                // If yes run jump and play sound effect
                theGame.endGame();

            }
            // Continue the collision check if game hasn't ended
            if(!stopRunnable){
                collisionHandler.postDelayed(this, 100);
            }
        }
    };
}
