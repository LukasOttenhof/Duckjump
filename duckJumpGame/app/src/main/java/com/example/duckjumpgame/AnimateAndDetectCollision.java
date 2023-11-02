package com.example.duckjumpgame;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import java.util.Random;

/**
 * This class is used to create objects that are animated and need to be able to detect collision.
 * The classes that will extend from it will be the CreateHazard class and CreatePlatform
 * (possibly a platform with a coin class), both of these classes create objects that have a
 * vertical falling animation and collision detection, but have different things that happen once
 * collision is detected.
 */
public class AnimateAndDetectCollision {
    private int screenWidth;
    private int screenHeight;
    private Boolean stopRunnable = false;
    private ImageView platform;
    private DuckPlayer duckPlayer;
    private int duration;
    private int respawnDelay;
    private Handler platformHandler = new Handler();
    private Random randomInt = new Random();

    public AnimateAndDetectCollision(ImageView platform, int screenWidth, int screenHeight, DuckPlayer duckPlayer, int duration, int respawnDelay, GameManager theGame){
        this.platform = platform;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.duckPlayer = duckPlayer;
        this.duration = duration;
        this.respawnDelay = respawnDelay;
        startFallAnimation(); // Initial platform animation before the delayed ones
        // Schedule next animations and respawns in a loop
        platformHandler.postDelayed(platformRunnable, respawnDelay);

    }

    /**
     * Used to animate the platform ImageView. It starts the animation from the
     * platforms y value and makes it move to below the screen height.
     */
    public void startFallAnimation(){
        int originalY = (int)platform.getY();
        ObjectAnimator fallAnimator = ObjectAnimator.ofFloat(platform, "translationY", originalY, screenHeight+400);
        fallAnimator.setInterpolator(new AccelerateInterpolator()); //Makes the platform accelerate rather than have a constant speed
        fallAnimator.setDuration(duration);
        fallAnimator.start();
    }

    /**
     * This method is used to reset the location of the platform after the animation.
     * It works by moving the platform to a coordinate above the screen and then moving
     * the platform to a random x coordinate within the screen width.
     */
    public void respawn(){
        int randomX = randomInt.nextInt(screenWidth - platform.getWidth()); // Random x coordinate minus width so doesn't spawn off screen to left
        platform.setX(randomX);                                             // And plus width so doesn't spawn off screen to the right

        // Set y coordinate above the screen, above the screen is negative
        platform.setY(-100);

    }

    /**
     * This runnable is used to animate the platforms in a cycle, first reseting the
     * platform since startFallAnimation was called in constructor, then calling
     * startFallAnimation again. The handeler runs every respawnDelay milliseconds
     * because that is the amount of time needed to run the startFallAnimation method.
     * It will stop running when stopRunnable is true, stopRunnable is set to true when
     * the game ends.
     */
    Runnable platformRunnable = new Runnable() {
        public void run() {
            // Trigger the next fall animation after respawn
            respawn();
            startFallAnimation();
            // Repeat the process if game hasn't ended
            if(!stopRunnable) {
                platformHandler.postDelayed(this, respawnDelay);
            }
        }
    };



    /**
     * This method is used to detect collision by comparing coordinates of the
     * duck to coordinates of platforms. It is called continusly in the
     * collisionChecker runnable
     *
     * @return return true if the duck is on a platform
     */
    public boolean checkCollision(){
        int duckTopY = duckPlayer.getDuckY();
        int duckBottomY = duckPlayer.getDuckY() + duckPlayer.getDuckHeight();
        int platformTopY = (int)platform.getY();
        int platformBottomY = (int)platform.getY() + platform.getHeight();
        int duckLeft = duckPlayer.getDuckX();
        int duckRight = duckLeft + duckPlayer.getDuckWidth();
        int platformLeft = (int) platform.getX();
        int platformRight = platformLeft + platform.getWidth();

        // If the top or bottom or middle of the duck is between the top and bottom of the platform,
        // and one of the sides of the duck is within the platforms side's,
        // return true to indicate collision
        return (duckBottomY >= platformTopY && duckBottomY <= platformBottomY ||
                duckTopY <= platformBottomY && duckTopY >= platformTopY ||
                duckBottomY + (duckPlayer.getDuckHeight()/2) >= platformTopY && duckBottomY + (duckPlayer.getDuckHeight()/2) <= platformBottomY ) &&
                (duckLeft >= platformLeft && duckLeft <= platformRight ||
                        duckRight <= platformRight && duckRight >= platformRight);

    }
    /**
     * Used to end the runnables. This
     * method is called in the endGame method in game manager.
     */
    public void endRunnables(){
        stopRunnable = true;

    }

}
