package com.example.duckjumpgame;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import java.util.Random;

public class PlatformManager {
    private int screenWidth;
    private Random randomInt = new Random();
    private int screenHeight;
    private Handler collisionHandler = new Handler();
    private ImageView platform;
    private DuckPlayer duckPlayer;
    private int duration;
    private int respawnDelay;
    private Handler platformHandler = new Handler();

    public PlatformManager(ImageView platform, int screenWidth, int screenHeight, DuckPlayer duckPlayer, int duration, int respawnDelay){
        this.platform = platform;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.duckPlayer = duckPlayer;
        this.duration = duration;
        this.respawnDelay = respawnDelay;
    }


    public void startFallAnimation(){
        float originalY = platform.getY();

        ObjectAnimator fallAnimator = ObjectAnimator.ofFloat(platform, "translationY", originalY, screenHeight);
        fallAnimator.setInterpolator(new AccelerateInterpolator());//makes the platform accelerate rather than constant speed
        fallAnimator.setDuration(duration);
        fallAnimator.start();
    }

    public void respawn(){ //TODO: still respawning off screen
        int randomX = randomInt.nextInt(screenWidth + platform.getWidth()) - platform.getWidth(); //random x coordiante - width so doesnt spawn off screen to left
        platform.setX(randomX);                                             //and + width so doesnt spawn off screen to the right

        //Set y coordinate above the screen, above the screen is negitive
        platform.setY(-100);

    }


    //this is starting the handelers that handel collision and the platform animation
    //called in game manager
    public void managePlatform(){
        startFallAnimation();//initial platform animation before the delayed ones
        // Schedule next animations and respawns in a loop
        platformHandler.postDelayed(platformRunnable, respawnDelay);
        collisionHandler.postDelayed(collisionChecker, 100);
    }
    Runnable platformRunnable = new Runnable() {
        public void run() {
            //Trigger the next fall animation after respawn
            respawn();
            startFallAnimation();
            //Repeat the process
            platformHandler.postDelayed(this, respawnDelay);
        }
    };

    //runnable is running every 100 mills checking for collision
    Runnable collisionChecker = new Runnable(){
        public void run(){
            //Check for collision
            if (checkCollision()){
                //If yes run jump
                duckPlayer.jump();
            }
            //Continue the collision check
            collisionHandler.postDelayed(this, 100);
        }
    };


    //if the duck is on the cloud return true to indicate collision
    public boolean checkCollision(){
        int duckTopY = duckPlayer.getDuckY();
        int duckBottomY = duckPlayer.getDuckY() + duckPlayer.getDuckHeight();
        int platformTopY = (int)platform.getY();
        int platformBottomY = (int)platform.getY() + platform.getHeight();
        int duckLeft = duckPlayer.getDuckX();
        int duckRight = duckLeft + duckPlayer.getDuckWidth();
        int platformLeft = (int) platform.getX();
        int platformRight = platformLeft + platform.getWidth();
        //if the ducks bottom is below the clouds top and the ducks x is below the patforms x and

        return duckBottomY >= platformTopY && duckBottomY <= platformBottomY &&
                (duckLeft >= platformLeft && duckLeft <= platformRight ||
                        duckRight <= platformRight && duckRight >= platformRight);

    }
}
