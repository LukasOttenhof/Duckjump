package com.example.duckjumpgame;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;


public class DuckPlayer {
    private ImageView theDuck;
    private int screenHeight;
    private boolean isGamePaused = false;
    private int screenWidth;
    private int coinsCollected = 1; // 1 By default so score wont be set back to 0
    private int platformsTouched = 0;
    private int scoreDistance;
    private ValueAnimator jumpAnimator;
    private ValueAnimator fallAnimator;
    private ObjectAnimator bounceAnimator;

    /**
     * @param theDuck      ImageView of the duck, it is whats being animated in this class.
     * @param screenHeight This is the bottom of the screen, its what the animation will end at
     *                     because the duck is trying to fall off of the screen.
     * @param screenWidth  This is used so that when moving the duck the limit of how far right the
     *                     duck can go without falling off the screen is known.
     */
    public DuckPlayer(ImageView theDuck, int screenHeight, int screenWidth) {
        this.theDuck = theDuck;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        startBounceAnimation();
    }

    /**
     * This method is used for making the duck jump when coming into contact with a platform,
     * it is called once collision is detected. It works by creating an animation that starts from
     * where the duck was at the point of collision, up to the jump height, and then brings the
     * duck back down to the screenHeight.
     */
    public void jump() {
        int originalY = (int) theDuck.getY();
        int jumpDuration = 2000;
        jumpScore();
        platformsTouched += 1;
        // Calculate the ending positions for the jump animation, 0 is at the top, 300 is the hight of the jump from where the duck was
        int jumpPeak = originalY - 150;

        // Create a ValueAnimator for jump and fall animation
        jumpAnimator = ValueAnimator.ofFloat(originalY, jumpPeak);
        jumpAnimator.setInterpolator(new DecelerateInterpolator()); // Start the duck speed fast and slow at top
        jumpAnimator.setDuration(jumpDuration / 2);
        fallAnimator = ValueAnimator.ofFloat(jumpPeak, screenHeight);
        fallAnimator.setInterpolator(new AccelerateInterpolator()); // Start slow and speed up as fall progresses
        fallAnimator.setDuration(jumpDuration - 500);

        // Set up an update listener to handle the animation values, i found this online, i did not
        // know about update listeners before
        jumpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                theDuck.setY(animatedValue);
            }
        });
        fallAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                theDuck.setY(animatedValue);
            }
        });



        // Start the jump and fall animation
        // animator set found at https://stackoverflow.com/questions/64744445/animatorset-stopping-when-playing-sequentially
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(jumpAnimator, fallAnimator);

        animatorSet.start();

    }


    //Changes Score when jumping
    public void jumpScore() {

        for (int scoreCount = 10; scoreCount > 0; scoreCount -= 1) {

            scoreDistance += 1;

        }


    }

    //getter for jumping score
    public int getScoreDistance() {
        return scoreDistance;
    }

    // Getters are used for detecting collision
    public int getDuckX() {
        return (int) theDuck.getX();
    }

    public int getDuckY() {
        return (int) theDuck.getY();
    }

    public int getDuckWidth() {
        return theDuck.getWidth();
    }

    public int getDuckHeight() {
        return theDuck.getHeight();
    }


    // Following are used to update duck location during on touch

    public ViewGroup.MarginLayoutParams getDuckLayoutParams() {
        return (ViewGroup.MarginLayoutParams) theDuck.getLayoutParams();
    }

    public void setDuckLayoutParams(ViewGroup.MarginLayoutParams params) {
        theDuck.setLayoutParams(params);
    }

    public int getPlatformsTouched() {
        return platformsTouched;
    }

    public int getCoinsCollected() {
        return coinsCollected;
    }

    public void setCoinsCollected(int newNumberOfCoins) {
        coinsCollected = newNumberOfCoins;
    }

    /**
     * Handles the initial bounce animation of the DuckPlayer on collision. After the
     * initial bounce, the jump is handled by the jump() function. Initial bounce is higher
     * and slower because the customer wants it to be easy to get to the first platform.
     * <p>
     * Animator found at
     * https://stackoverflow.com/questions/11633221/android-properties-that-can-be-animated-with-objectanimator
     * https://developer.android.com/develop/ui/views/animations/prop-animation#views
     */

    public void startBounceAnimation() {
        int originalY = (int) theDuck.getY();
        int initialJumpHeight = 800;
        int duration = 4000;
        bounceAnimator = ObjectAnimator.ofFloat(theDuck, "translationY", originalY, originalY - initialJumpHeight, screenHeight);
        bounceAnimator.setInterpolator(new LinearInterpolator());
        bounceAnimator.setDuration(duration);

        bounceAnimator.start();

        jumpScore();
    }

    /**
     * This method is used to update the position of the duck, it is used when GameManager detects
     * the player touching the background. It gets the x value where the player touched, if it is
     * within the screen width, it will set the duck to this new x position.
     *
     * @param event
     * @return true to the touch event to indicate that the event has finished
     */
    public boolean onTouchEvent(MotionEvent event) {
        // Check if the game is not paused

        // Subtract to center duck on pointer
        int newX = (int) event.getRawX() - getDuckWidth() / 2;
        // Getting duck params so we can change them
        ViewGroup.MarginLayoutParams params = getDuckLayoutParams();
        // Adding the change
        // as long as the new location will be within the screen make the change
        if (newX >= 0 && newX + getDuckWidth() <= screenWidth && !isGamePaused) {
            params.leftMargin = newX;
            setDuckLayoutParams(params);
        }

        return true;
    }


    public void pauseAnimation() {
        isGamePaused = true;
        if (jumpAnimator != null && jumpAnimator.isRunning()) {
            jumpAnimator.pause();
        }
        else if (fallAnimator != null && fallAnimator.isRunning()) {
            fallAnimator.pause();
        }
        else if (bounceAnimator != null && bounceAnimator.isRunning()) {
            bounceAnimator.pause();
        }
    }
    public void resumeAnimation() {
        isGamePaused = false;
        if (jumpAnimator != null && jumpAnimator.isPaused()) {
            jumpAnimator.resume();
        }
        if (fallAnimator != null && fallAnimator.isPaused()) {
            fallAnimator.resume();
        }
        if (bounceAnimator != null && bounceAnimator.isPaused()) {
            bounceAnimator.resume();
        }
    }
}