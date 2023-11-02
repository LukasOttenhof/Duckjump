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
    private Boolean stopRunnable = false;
    private ImageView platform;
    private DuckPlayer duckPlayer;
    private int duration;
    private int respawnDelay;
    private Handler platformHandler = new Handler();
    private Random randomInt = new Random();
    private SoundManager soundEffect;

    private GameManager theGame;
    public PlatformManager(ImageView platform, int screenWidth, int screenHeight, DuckPlayer duckPlayer, int duration, int respawnDelay, GameManager theGame){
        this.platform = platform;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.duckPlayer = duckPlayer;
        this.duration = duration;
        this.respawnDelay = respawnDelay;
        this.soundEffect = new SoundManager(platform.getContext());
        this.theGame = theGame;
        startFallAnimation(); // Initial platform animation before the delayed ones
        // Schedule next animations and respawns in a loop
        platformHandler.postDelayed(platformRunnable, respawnDelay);
        collisionHandler.postDelayed(collisionChecker, 100);
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
    public void respawn(){ //TODO: still respawning off screen
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
     * This runnable is checking for collission repeatedly until the game ends.
     * It uses the checkCollision method for collision, if collision is detected
     * the duck will jump and a the quack sound effect is played. It stops when
     * stopRunnable is set to true which happens when the game ends. The runnable
     * is set to a 100 milisecond delay so that when the duck goes up through a platform
     * the delay isnt so short that the duck jumps or quaks a second time
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
     * Used to end the runables. This
     * method is called in the endGame method in game manager.
     */
    public void endRunnables(){
        stopRunnable = true;

    }

}
