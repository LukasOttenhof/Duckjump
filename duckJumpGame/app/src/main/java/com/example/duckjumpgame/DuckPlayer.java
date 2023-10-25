package com.example.duckjumpgame;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;


public class DuckPlayer{
    private int lastX;
    private ImageView theDuck;
    private int screenWidth;
    private int screenHeight;


    public DuckPlayer(ImageView theDuck, int screenWidth, int screenHeight){
        this.theDuck = theDuck;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        theDuck.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                return onTouchEvent(event);
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event){
        int x = (int)event.getRawX() - theDuck.getWidth() / 2;
        moveDuckPlayer(x);
        lastX = x;
        return true;
    }

    //gpt
    private void moveDuckPlayer(int x){
        int deltaX = x - lastX;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) theDuck.getLayoutParams();
        int newLeftMargin = (int) (params.leftMargin + deltaX);
        if (newLeftMargin >= 0 && newLeftMargin + theDuck.getWidth() <= screenWidth) {
            params.leftMargin = newLeftMargin;
            theDuck.setLayoutParams(params);

            ViewGroup.MarginLayoutParams duckLayout = (ViewGroup.MarginLayoutParams) theDuck.getLayoutParams();
            duckLayout.leftMargin = newLeftMargin;
            theDuck.setLayoutParams(duckLayout);
        }
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

        //Set up an update listener to handle the animation values, i found this online, i couldnt
        //figure out how to get jump working properly on my own
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
