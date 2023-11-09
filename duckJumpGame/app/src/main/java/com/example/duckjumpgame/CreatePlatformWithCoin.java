package com.example.duckjumpgame;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class CreatePlatformWithCoin extends AnimateAndDetectCollision {
    private Handler collisionHandler = new Handler();
    private DuckPlayer duckPlayer;
    private SoundManager soundEffect;
    private ImageView theCoin;
    private ImageView platform;

    /**
     * In the constructor the collision handler is called so that collision
     * will start to be detected.
     *
     * @param platform The Imageview of the platform that is being animated.
     * @param screenWidth Maximum width the platform can respawn at.
     * @param screenHeight Bottom of the screen, endpoint of the animation.
     * @param duckPlayer The duckObject that manages the duck.
     * @param duration Amount of time the platform falling animation will take.
     * @param respawnDelay Amount of time between animations of the platform falling.
     */

    public CreatePlatformWithCoin(ImageView platform, int screenWidth, int screenHeight,
                        DuckPlayer duckPlayer, int duration, int respawnDelay, ImageView theCoin){

        super(platform, screenWidth, screenHeight, duckPlayer, duration, respawnDelay);
        this.duckPlayer = duckPlayer;
        this.platform = platform;
        this.soundEffect = new SoundManager(platform.getContext());
        collisionHandler.postDelayed(collisionChecker, 100);
        this.theCoin = theCoin;

    }

    /**
     * This runnable is checking for collision repeatedly until the game ends.
     * It uses the checkCollision method for collision, if collision is detected
     * we set the coin to be invisible, update number of coins collected and it so the coin cant
     * be collected again until after the coin has respawned.
     */
    Runnable collisionChecker = new Runnable(){
        public void run(){
            int maxHeight = 150; // We wont let the duck jump if it is higher than this
            theCoin.setX(platform.getX() );//setting the coin and platform to the same x
            //so that even though they respawn randomly they will be put together

            // Check for collision and if duck is too high
            if (checkCollision() && duckPlayer.getDuckY() > maxHeight){
                // If yes run jump and play sound effect
                soundEffect.playSound(R.raw.quack);
                duckPlayer.jump();
                // If coin is visible it hasn't been collected yet.
                if(theCoin.getVisibility() == View.VISIBLE){
                    theCoin.setVisibility(View.INVISIBLE);
                    int newCoinAmount = duckPlayer.getCoinsCollected() + 1;
                    duckPlayer.setCoinsCollected(newCoinAmount);
                }
            }
            // Continue the collision check if game hasn't ended
            if(!stopRunnable){
                collisionHandler.postDelayed(this, 50);
            }
        }
    };
}
