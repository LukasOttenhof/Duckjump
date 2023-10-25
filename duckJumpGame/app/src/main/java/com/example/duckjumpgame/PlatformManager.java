package com.example.duckjumpgame;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;


public class PlatformManager {
    private int screenWidth;
    private int screenHeight;
    private Handler collisionHandler = new Handler();
    private ImageView platform;
    private DuckPlayer duckPlayer;
    private long duration;
    private long respawnDelay;
    private Handler platformHandler = new Handler();

    public PlatformManager(ImageView platform, int screenWidth, int screenHeight, DuckPlayer duckPlayer, long duration, long respawnDelay){
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

    public void respawn(){
        float randomX = (float) Math.random() * screenWidth - 93;
        platform.setX(randomX);

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
    private Runnable platformRunnable = new Runnable() {
        public void run() {
            //Trigger the next fall animation after respawn
            respawn();
            startFallAnimation();
            //Repeat the process
            platformHandler.postDelayed(this, respawnDelay);
        }
    };

    //runnable is running every 100 mills checking for collision
    private Runnable collisionChecker = new Runnable(){
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
        float duckBottomY = duckPlayer.getDuckY() + duckPlayer.getDuckHeight();
        float cloudTopY = platform.getY();
        float cloudBottomY = platform.getY() + platform.getHeight();

        return duckBottomY >= cloudTopY && duckPlayer.getDuckX() >= platform.getX()
                && duckPlayer.getDuckX() + duckPlayer.getDuckWidth() <= platform.getX() + platform.getWidth()
                && duckBottomY <= cloudBottomY;
    }
}
