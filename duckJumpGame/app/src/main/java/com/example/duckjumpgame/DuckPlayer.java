package com.example.duckjumpgame;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;


public class DuckPlayer{

    private ImageView theDuck;

    private int screenHeight;


    public DuckPlayer(ImageView theDuck, int screenHeight){
        this.theDuck = theDuck;
        this.screenHeight = screenHeight;
    }

    /**
     * Handles the jumping animation of the DuckPlayer after the initial jump is done
     */
    public void jump(){
        int originalY = (int)theDuck.getY();
        int jumpDuration = 2000;


        // Calculate the ending positions for the jump animation, 0 is at the top, 300 is the hight of the jump from where the duck was
        int jumpPeak = originalY - 150;

        // Create a ValueAnimator for jump and fall animation
        ValueAnimator jumpAnimator = ValueAnimator.ofFloat(originalY, jumpPeak);
        jumpAnimator.setInterpolator(new DecelerateInterpolator()); // Start the duck speed fast and slow at top
        jumpAnimator.setDuration(jumpDuration/2);
        ValueAnimator fallAnimator = ValueAnimator.ofFloat(jumpPeak, screenHeight);
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

    // Getters are used for detecting collision
    public int getDuckX(){
        return (int)theDuck.getX();
    }

    public int getDuckY(){
        return (int)theDuck.getY();
    }

    public int getDuckWidth(){
        return theDuck.getWidth();
    }

    public int getDuckHeight(){
        return theDuck.getHeight();
    }

    // Following are used to update duck location during on touch

    public ViewGroup.MarginLayoutParams getDuckLayoutParams(){
        return (ViewGroup.MarginLayoutParams) theDuck.getLayoutParams();
    }
    public void setDuckLayoutParams(ViewGroup.MarginLayoutParams params){
        theDuck.setLayoutParams(params);
    }



    /**
<<<<<<< HEAD
     * Handles the first bounce animation of the DuckPlayer, it is slower and higher
     * than the rest of the jumps to make it easy to come into contact with at least
     * one platfrom.
=======
     * Handles the initial bounce animation of the DuckPlayer on collision. After the
     * initial bounce, the jump is handled by the jump() function.
>>>>>>> 50ad18524fca40f1cdbf9e16d2f8f5e74df504be
     *
     * Animator found at
     * https://stackoverflow.com/questions/11633221/android-properties-that-can-be-animated-with-objectanimator
     * https://developer.android.com/develop/ui/views/animations/prop-animation#views
     */
    public void startBounceAnimation(){
        int originalY = (int)theDuck.getY();
        int duration = 4000;//slow and high to make the first platform easy to touch
        int initialJumpHeight = 800;
        ObjectAnimator bounceAnimator = ObjectAnimator.ofFloat(theDuck, "translationY", originalY, originalY - initialJumpHeight, screenHeight);
        bounceAnimator.setInterpolator(new LinearInterpolator());
        bounceAnimator.setDuration(duration);

        bounceAnimator.start();
    }


}
