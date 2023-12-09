package com.example.duckjumpgame;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * This class contains all methods related to the duck. This class can make the duck jump,
 * pause, resume, and keeps track of the number of coins that have been collected. This class
 * also contains the onTouchEvent that is called to move the duck when a user touches the screen.
 */
public class DuckPlayer {
    private ImageView theDuck;
    private int screenHeight;
    private boolean isGamePaused = false;
    private int screenWidth;
    private int coinsCollected = 1; // 1 By default so score wont be set back to 0 when multiplying
    private int platformsTouched = 0;
    public ValueAnimator jumpAnimator;
    public ValueAnimator fallAnimator;
    private boolean isJumpAfterPause = false;
    private JumpScorer jumpScorer;
    public ObjectAnimator bounceAnimator;

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
        jumpScorer = new JumpScorer();
        startBounceAnimation(); // Begin animation
    }

    /**
     * This method is used for making the duck jump when coming into contact with a platform,
     * it is called once collision is detected. It works by creating an animation that starts from
     * where the duck was at the point of collision, up to the jump height, and then brings the
     * duck back down to the screenHeight.
     */
    public void jump() {
        int originalY = (int) theDuck.getY();
        int jumpDuration = 2000; // Speed of the animation

        //  Do not update the score if the jump is being called on resume
        if(!isJumpAfterPause){
            platformsTouched += 1;
            jumpScorer.updateScore();
        }
        isJumpAfterPause = false;

        // Calculate the ending positions for the jump animation, 0 is at the top, 300 is the hight of the jump from where the duck was
        int jumpPeak = originalY - 150;

        // Create a ValueAnimator for jump and fall animation
        jumpAnimator = ValueAnimator.ofFloat(originalY, jumpPeak);
        jumpAnimator.setInterpolator(new DecelerateInterpolator()); // Start the duck speed fast and slow at top
        jumpAnimator.setDuration(jumpDuration / 2);
        fallAnimator = ValueAnimator.ofFloat(jumpPeak, screenHeight);
        fallAnimator.setInterpolator(new AccelerateInterpolator()); // Start slow and speed up as fall progresses
        fallAnimator.setDuration(jumpDuration - 500);


        // Set up an update listener to handle the animation values, found how to set up listener
        // Online

        jumpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(isGamePaused){ // .pause is like an update do we need this so
                                // that when thte game pauses the pause is not
                                //over ridden
                }
                else{
                    float animatedValue = (float) animation.getAnimatedValue();
                    theDuck.setY(animatedValue);
                }
            }
        });

        fallAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(isGamePaused) {// .pause is like an update do we need this so
                    // that when thte game pauses the pause is not
                    //over ridden

                }
                else{
                    float animatedValue = (float) animation.getAnimatedValue();
                    theDuck.setY(animatedValue);                }
            }
        });




        // Start the jump and fall animation
        // animator set found at https://stackoverflow.com/questions/64744445/animatorset-stopping-when-playing-sequentially
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(jumpAnimator, fallAnimator);

        animatorSet.start();

    }





    // Getter used for jumping score
    public int getScoreDistance() {
        return jumpScorer.getScoreDistance();
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
     * Handles the initial bounce animation of the DuckPlayer. After the
     * initial bounce, the jump is handled by the jump() function. Initial bounce is higher
     * and slower because the customer wants it to be easy to get to the first platform.
     *
     * Animator found at
     * https://stackoverflow.com/questions/11633221/android-properties-that-can-be-animated-with-objectanimator
     * https://developer.android.com/develop/ui/views/animations/prop-animation#views
     */

    public void startBounceAnimation() {
        int originalY = (int) theDuck.getY();
        int initialJumpHeight = 800;
        int duration = 4000;
        // Create animator
        bounceAnimator = ObjectAnimator.ofFloat(theDuck, "translationY", originalY, originalY - initialJumpHeight, screenHeight);
        bounceAnimator.setInterpolator(new LinearInterpolator());
        bounceAnimator.setDuration(duration);

        bounceAnimator.start();

        jumpScorer.updateScore();

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
        // Subtract to center duck on pointer
        int newX = (int) event.getRawX() - getDuckWidth() / 2;
        // Getting duck params so we can change them
        ViewGroup.MarginLayoutParams params = getDuckLayoutParams();
        // as long as the new location will be within the screen make the change
        if (newX >= 0 && newX + getDuckWidth() <= screenWidth && !isGamePaused) {
            params.leftMargin = newX;
            setDuckLayoutParams(params);
        }

        return true;
    }

    /**
     * This method is used to make the duck pause, it is called in game manager when the game is
     * paused, it works by checking each of the animators, when finding the one that is running
     * use the .pause() function from the ObjectAnimator/ValueAnimator Library to pause the
     * ducks animation.
     */
    public void pauseAnimation() {
        isGamePaused = true;
        if (jumpAnimator != null && jumpAnimator.isRunning()) {
            jumpAnimator.pause();
        }
        if (fallAnimator != null && fallAnimator.isRunning()) {
            fallAnimator.pause();
        }
        if (bounceAnimator != null && bounceAnimator.isRunning()) {
            bounceAnimator.pause();
        }
    }

    /**
     * This method is used to make the duck resume after pressing resume. Rather than using
     * .resume() we use jump so that if the user was falling when they had to jump up for water or
     * something similar when they come back they arent going to die.
     */
    public void resumeAnimation() {
        isGamePaused = false;
        isJumpAfterPause = true;
        jump();
    }
}