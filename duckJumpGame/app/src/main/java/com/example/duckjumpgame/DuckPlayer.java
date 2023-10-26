package com.example.duckjumpgame;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;


public class DuckPlayer{

    private ImageView theDuck;

    private int screenHeight;


    public DuckPlayer(ImageView theDuck, int screenHeight){
        this.theDuck = theDuck;
        this.screenHeight = screenHeight;
    }


    public void jump(){
        int originalY = (int)theDuck.getY();
        int jumpDuration = 500;
        int fallDuration = 5000; //Adjust this value for the fall duration

        //Calculate the ending positions for the jump animation, 0 is at the top, 300 is the hight of the jump from where the duck was
        int jumpPeak = originalY - 300;

        //Create a ValueAnimator for jump and fall animation
        ValueAnimator jumpAndFallAnimator = ValueAnimator.ofFloat(originalY, jumpPeak, screenHeight);
        jumpAndFallAnimator.setInterpolator(new LinearInterpolator()); // Constant speed
        jumpAndFallAnimator.setDuration(jumpDuration + fallDuration);

        //Set up an update listener to handle the animation values, i found this online, i did not
        //know about update listeners before
        jumpAndFallAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                theDuck.setY(animatedValue);
            }
        });

        //Start the jump and fall animation
        jumpAndFallAnimator.start();
    }

    //getters are used for detecting collision
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

    //following are used to update duck location during on touch

    public ViewGroup.MarginLayoutParams getDuckLayoutParams(){
        return (ViewGroup.MarginLayoutParams) theDuck.getLayoutParams();
    }
    public void setDuckLayoutParams(ViewGroup.MarginLayoutParams params){
        theDuck.setLayoutParams(params);
    }

    //animator found at
    //https://stackoverflow.com/questions/11633221/android-properties-that-can-be-animated-with-objectanimator
    //https://developer.android.com/develop/ui/views/animations/prop-animation#views
    public void startBounceAnimation(){
        int originalY = (int)theDuck.getY();
        int duration = 5000;
        int initialJumpHeight = 600;
        ObjectAnimator bounceAnimator = ObjectAnimator.ofFloat(theDuck, "translationY", originalY, originalY - initialJumpHeight, screenHeight);
        bounceAnimator.setInterpolator(new AccelerateInterpolator());
        bounceAnimator.setDuration(duration);

        bounceAnimator.start();
    }

}
