package com.example.duckjumpgame;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class CreatePlatformWithCoin extends AnimateAndDetectCollision {
    private Handler collisionHandler = new Handler();
    private DuckPlayer duckPlayer;
    private SoundManager soundEffect;
    private ImageView theCoin;
    private boolean coinIsCollected = false;
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
        this.soundEffect = new SoundManager(platform.getContext());
        collisionHandler.postDelayed(collisionChecker, 100);
        this.theCoin = theCoin;

    }

    /**
     * This runnable is checking for collision repeatedly until the game ends.
     * It uses the checkCollision method for collision, if collision is detected
     * we set the coin to be invisable, update number of coins collected and make the coin
     * invisable.
     */
    Runnable collisionChecker = new Runnable(){
        public void run(){
            // Check for collision and if duck is too high
            if (checkCollision() && duckPlayer.getDuckY() > 150){
                // If yes run jump and play sound effect
                soundEffect.playSound(R.raw.quack);
                duckPlayer.jump();
                if(!coinIsCollected){
                    theCoin.setVisibility(View.INVISIBLE);
                    int newCoinAmount = duckPlayer.getCoinsCollected() + 1;
                    duckPlayer.setCoinsCollected(newCoinAmount);
                    coinIsCollected = true;
                }
            }
            // Continue the collision check if game hasn't ended
            if(!stopRunnable){
                collisionHandler.postDelayed(this, 100);
            }
        }
    };
}
