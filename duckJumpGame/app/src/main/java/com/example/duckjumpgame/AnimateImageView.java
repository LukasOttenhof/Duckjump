package com.example.duckjumpgame;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import java.util.Random;

/**
 * This class is used to create objects that are animated vertically using y coordinates.
 * This class is extended by AnimateImageViewAndDectectCollision for ImageViews that need to
 * detect collision.
 */
public class AnimateImageView {
    private int screenWidth;
    private int screenHeight;
    protected Boolean stopAnimation = false; // Used to stop Runnables when game ends
    public ImageView imageToAnimate; // Made public for testing
    private boolean isGamePaused = false;
    private ObjectAnimator fallAnimator;
    private int duration;
    private Random randomInt = new Random();

    /**
     * In the constructor the variables will be set to the values of the parameters and the
     * platformHandler will be called starting the animation loop.
     *
     * @param imageToAnimate The Imageview of the platform that is being animated.
     * @param screenWidth Maximum width the platform can respawn at.
     * @param screenHeight Bottom of the screen, endpoint of the animation.
     * @param duration Amount of time the platform falling animation will take.
     */
    public AnimateImageView(ImageView imageToAnimate, int screenWidth, int screenHeight,
                             int duration){
        this.imageToAnimate = imageToAnimate;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        this.duration = duration;

        startFallAnimation(); // Start the animation
    }

    /**
     * Used to animate the platform ImageView. It starts the animation from the
     * platforms y value and makes it move to below the screen height. It uses the private
     * ObjectAnimator fallAnimator.
     */
    public void startFallAnimation(){
        int originalY = (int)imageToAnimate.getY(); // Getting starting point for the animation

        // Setting up the animator
        fallAnimator = ObjectAnimator.ofFloat(imageToAnimate, "translationY", originalY, screenHeight+400);
        fallAnimator.setInterpolator(new AccelerateInterpolator()); //Makes the platform accelerate rather than have a constant speed
        fallAnimator.setDuration(duration);
        fallAnimator.start();


        // Setting up animators for the animator
        fallAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Animation started
            }

            // When that animation ends reset the imageview and call the animator again
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!stopAnimation && !isGamePaused && screenWidth != 0) {
                    respawn();
                    imageToAnimate.setVisibility(View.VISIBLE);
                    fallAnimator.start();
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
     * Used to end the vertical animation when the game ends. It works by setting the stopAmination
     * variable to true so that the animation will not repeat once the animation ends.
     */
    public void endRunnables(){
        stopAnimation = true;

    }

    /**
     * This method is called in game manager to pause the ImageView being animated.
     * It works by setting isGamePaused to true and using the .pause() function from the
     * ObjectAnimator library.
     */
    public void pauseAnimation() {
        isGamePaused = true;
        if (fallAnimator != null && fallAnimator.isRunning()) {
            fallAnimator.pause();
        }

    }


    /**
     * This method is called in game manager to resume the ImageView being animated.
     * It works by setting isGamePaused to false and using the .resume() function from the
     * ObjectAnimator library.
     */
    public void resumeAnimation() {
        isGamePaused = false;
        if (fallAnimator != null && fallAnimator.isPaused()) {
            fallAnimator.resume();
        }

    }

}
