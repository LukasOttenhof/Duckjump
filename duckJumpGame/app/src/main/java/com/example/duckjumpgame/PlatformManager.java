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
    private Handler handler;

    public PlatformManager(ImageView platform, int screenWidth, int screenHeight, DuckPlayer duckPlayer, long duration, long respawnDelay){
        this.platform = platform;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.duckPlayer = duckPlayer;
        this.duration = duration;
        this.respawnDelay = respawnDelay;

        this.handler = new Handler();
        collisionHandler.postDelayed(collisionChecker, 100);
    }



    public float getCloudTopY(){
        return platform.getY();
    }

    public float getCloudBottomY(){
        return platform.getY() + platform.getHeight();
    }

    public float getCloudX(){
        return platform.getX();
    }

    public int getCloudWidth(){
        return platform.getWidth();
    }

    public void startFallAnimation(){
        float originalY = platform.getY();

        ObjectAnimator fallAnimator = ObjectAnimator.ofFloat(platform, "translationY", originalY, screenHeight);
        fallAnimator.setInterpolator(new AccelerateInterpolator());
        fallAnimator.setDuration(duration);
        fallAnimator.start();
    }

    public void respawn(){
        float randomX = (float) Math.random() * screenWidth - 93;
        platform.setX(randomX);

        //Set Y-coordinate above the screen, above the screen is negitive
        platform.setY(-2 * platform.getHeight());

    }

    public void managePlatform(){
        //first fall animation
        startFallAnimation();

        //Schedule next animations and respawns in a loop
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                //Trigger the next fall animation after respawn
                startFallAnimation();
                respawn();
                //Repeat the process
                managePlatform();
            }
        }, respawnDelay);
    }

    //runnable is running every 100 mills checking for collision
    private Runnable collisionChecker = new Runnable(){
        @Override
        public void run(){
            //Check for collision
            if (checkCollision()){
                //If yes run jump
                duckPlayer.jump();
            }
            //Continue the collision check
            collisionHandler.postDelayed(this, 100); // Adjust the delay time as needed
        }
    };


    //if the duck is on the cloud return true to indicate collision
    public boolean checkCollision(){
        float duckBottomY = duckPlayer.getDuckY() + duckPlayer.getDuckHeight();
        float cloudTopY = getCloudTopY();
        float cloudBottomY = getCloudBottomY();

        return duckBottomY >= cloudTopY && duckPlayer.getDuckX() >= getCloudX()
                && duckPlayer.getDuckX() + duckPlayer.getDuckWidth() <= getCloudX() + getCloudWidth()
                && duckBottomY <= cloudBottomY;
    }
}
