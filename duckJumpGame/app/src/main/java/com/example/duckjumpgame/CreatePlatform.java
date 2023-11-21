package com.example.duckjumpgame;

import android.os.Handler;
import android.widget.ImageView;

public class CreatePlatform extends AnimateAndDetectCollision {
    private Handler collisionHandler = new Handler();
    private DuckPlayer duckPlayer;
    private SoundManager soundEffect;
    private Settings settings;

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
     */

    public CreatePlatform(ImageView platform, int screenWidth, int screenHeight, DuckPlayer duckPlayer, int duration, int respawnDelay){
        super(platform, screenWidth, screenHeight, duckPlayer, duration, respawnDelay);
        this.duckPlayer = duckPlayer;
        this.soundEffect = new SoundManager(platform.getContext());
        this.settings = new Settings(platform.getContext());
        collisionHandler.postDelayed(collisionChecker, 100);

        settings.checkMute(soundEffect);

        startFallAnimation(); // Initial platform animation before the delayed ones
        // Schedule next animations and respawns in a loop
    }

    /**
     * This runnable is checking for collision repeatedly until the game ends.
     * It uses the checkCollision method for collision, if collision is detected
     * the duck will jump and a the quack sound effect is played. It stops when
     * stopRunnable is set to true which happens when the game ends. The runnable
     * is set to a 100 millisecond delay so that when the duck goes up through a platform
     * the delay isn't so short that the duck jumps or quacks a second time.
     */
    Runnable collisionChecker = new Runnable(){
        public void run(){
            // Check for collision and if duck is too high
            if (checkCollision() && duckPlayer.getDuckY() > 150){
                // If yes run jump and play sound effect
                soundEffect.playSound(R.raw.quack);
                duckPlayer.jump();

            }
            // Continue the collision check if game hasn't ended
            if(!stopRunnable){
                collisionHandler.postDelayed(this, 25);
            }
        }
    };
}
