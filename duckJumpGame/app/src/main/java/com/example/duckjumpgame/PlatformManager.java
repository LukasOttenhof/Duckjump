package com.example.duckjumpgame;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import java.util.Random;

public class PlatformManager {
    private int screenWidth;

    private int screenHeight;
    private Handler collisionHandler = new Handler();
    private ImageView platform;
    private DuckPlayer duckPlayer;
    private int duration;
    private int respawnDelay;
    private Handler platformHandler = new Handler();
    private Random randomInt = new Random();
    private SoundManager soundEffect;

    public PlatformManager(ImageView platform, int screenWidth, int screenHeight, DuckPlayer duckPlayer, int duration, int respawnDelay){
        this.platform = platform;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.duckPlayer = duckPlayer;
        this.duration = duration;
        this.respawnDelay = respawnDelay;
        this.soundEffect = new SoundManager(platform.getContext());

        startFallAnimation(); // Initial platform animation before the delayed ones
        // Schedule next animations and respawns in a loop
        platformHandler.postDelayed(platformRunnable, respawnDelay);
        collisionHandler.postDelayed(collisionChecker, 100);
    }


    public void startFallAnimation(){
        float originalY = platform.getY();
        ObjectAnimator fallAnimator = ObjectAnimator.ofFloat(platform, "translationY", originalY, screenHeight+500);
        fallAnimator.setInterpolator(new AccelerateInterpolator()); //Makes the platform accelerate rather than have a constant speed
        fallAnimator.setDuration(duration);
        fallAnimator.start();
    }

    public void respawn(){ //TODO: still respawning off screen
        int randomX = randomInt.nextInt(screenWidth - platform.getWidth()); // Random x coordinate minus width so doesn't spawn off screen to left
        platform.setX(randomX);                                             // And plus width so doesn't spawn off screen to the right

        // Set y coordinate above the screen, above the screen is negative
        platform.setY(-100);

    }

    Runnable platformRunnable = new Runnable() {
        public void run() {
            // Trigger the next fall animation after respawn
            respawn();
            startFallAnimation();
            //Repeat the process
            platformHandler.postDelayed(this, respawnDelay);
        }
    };

    // Runnable is running every 100 mills checking for collision
    Runnable collisionChecker = new Runnable(){
        public void run(){
            // Check for collision and if duck is too high
            if (checkCollision() && duckPlayer.getDuckY() > 150){
                // If yes run jump and play sound effect
                soundEffect.playSound(R.raw.quack);
                duckPlayer.jump();
            }
            // Continue the collision check
            collisionHandler.postDelayed(this, 100);
        }
    };


    // If the duck is on the cloud, return true to indicate collision
    public boolean checkCollision(){
        int duckTopY = duckPlayer.getDuckY();
        int duckBottomY = duckPlayer.getDuckY() + duckPlayer.getDuckHeight();
        int platformTopY = (int)platform.getY();
        int platformBottomY = (int)platform.getY() + platform.getHeight();
        int duckLeft = duckPlayer.getDuckX();
        int duckRight = duckLeft + duckPlayer.getDuckWidth();
        int platformLeft = (int) platform.getX();
        int platformRight = platformLeft + platform.getWidth();

        // If the top or bottom of the duck is between the top and bottom of the platform,
        // and one of the sides of the duck is within the platforms side's,
        // return true to indicate collision
        return (duckBottomY >= platformTopY && duckBottomY <= platformBottomY ||
                duckTopY <= platformBottomY && duckTopY >= platformTopY) &&
                (duckLeft >= platformLeft && duckLeft <= platformRight ||
                        duckRight <= platformRight && duckRight >= platformRight);

    }
}
