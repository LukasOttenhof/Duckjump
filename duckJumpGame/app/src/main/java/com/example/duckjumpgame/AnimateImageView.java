package com.example.duckjumpgame;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
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
public class AnimateImageView {
    private int screenWidth;
    private int screenHeight;
    protected Boolean stopRunnable = false; // Used to stop Runnables when game ends
    public ImageView imageToAnimate; // Made public for testing
    private boolean isGamePaused = false;
    private ObjectAnimator fallAnimator;
    private DuckPlayer duckPlayer;
    private int duration;
    private int respawnDelay;
    private Random randomInt = new Random();

    /**
     * In the constructor the variables will be set to the values of the parameters and the
     * platformHandler will be called starting the animation loop.
     *
     * @param imageToAnimate The Imageview of the platform that is being animated.
     * @param screenWidth Maximum width the platform can respawn at.
     * @param screenHeight Bottom of the screen, endpoint of the animation.
     * @param duckPlayer The duckObject that manages the duck.
     * @param duration Amount of time the platform falling animation will take.
     */
    public AnimateImageView(ImageView imageToAnimate, int screenWidth, int screenHeight,
                            DuckPlayer duckPlayer, int duration){
        this.imageToAnimate = imageToAnimate;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.duckPlayer = duckPlayer;
        this.duration = duration;
        this.respawnDelay = respawnDelay;
       // platformHandler.postDelayed(platformRunnable, respawnDelay);
        startFallAnimation(); // Initial animation before the delayed ones
        // Schedule next animations and respawns in a loop
    }

    /**
     * Used to animate the platform ImageView. It starts the animation from the
     * platforms y value and makes it move to below the screen height.
     */
    public void startFallAnimation(){
        int originalY = (int)imageToAnimate.getY();
        fallAnimator = ObjectAnimator.ofFloat(imageToAnimate, "translationY", originalY, screenHeight+400);
        fallAnimator.setInterpolator(new AccelerateInterpolator()); //Makes the platform accelerate rather than have a constant speed
        fallAnimator.setDuration(duration);
        fallAnimator.start();


        fallAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Animation started
            }


            @Override
            public void onAnimationEnd(Animator animation) {
                if (!stopRunnable && !isGamePaused && screenWidth != 0) {
                    respawn();
                    imageToAnimate.setVisibility(View.VISIBLE);
                    startFallAnimation(); // Start a new animation loop
                }
            }


            @Override
            public void onAnimationCancel(Animator animation) {
                // Animation canceled
            }


            @Override
            public void onAnimationRepeat(Animator animation) {
                // Animation repeated
            }
        });


    }

    /**
     * This method is used to reset the location of the platform after the animation.
     * It works by moving the platform to a coordinate above the screen and then moving
     * the platform to a random x coordinate within the screen width.
     */
    public void respawn(){
        int randomX = randomInt.nextInt(screenWidth - imageToAnimate.getWidth()); // Random x coordinate minus width so doesn't spawn off screen to the right
        imageToAnimate.setX(randomX);

        // Set y coordinate above the screen, above the screen is negative
        imageToAnimate.setY(-100);

    }

    /**
     * Used to end the runnables by setting the stopRunnable variable to true, this will make
     * the repeat condition in the Runnables false so they will stop running.
     * This method is called in the endGame method in game manager.
     */
    public void endRunnables(){
        stopRunnable = true;

    }

    //========================================
    public void pauseAnimation() {
        isGamePaused = true;
        if (fallAnimator != null && fallAnimator.isRunning()) {
            int ypos = (int)imageToAnimate.getY();
            fallAnimator.pause();
            imageToAnimate.setY(ypos);
        }
        // Additional logic for pausing any ongoing animations or transitions
    }


    //Resumes the animation
    public void resumeAnimation() {
        isGamePaused = false;
        if (fallAnimator != null && fallAnimator.isPaused()) {
            fallAnimator.resume();
        }

    }


}
