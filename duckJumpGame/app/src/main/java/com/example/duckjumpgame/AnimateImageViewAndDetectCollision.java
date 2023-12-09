package com.example.duckjumpgame;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

/**
 * This class extends AnimateImageView because it is for Imageviews that need to be animated with
 * the additional functionality of being able to detect collision. This class is used for all
 * Imageviews that need collision detection with the duck, this includes the platforms, hazards, and
 * the platform with a coin.
 */
public class AnimateImageViewAndDetectCollision extends AnimateImageView {
    private Handler collisionHandler = new Handler();
    private DuckPlayer duckPlayer;
    private SoundManager soundEffect;
    private Settings settings;
    private ImageView theCoin;
    private GameManager theGame;
    private String typeOfObject;

    /**
     * In the constructor, the collision handler is called so that collision
     * will start to be detected.
     *
     * @param screenWidth Maximum width the platform can respawn at.
     * @param screenHeight Bottom of the screen, endpoint of the animation.
     * @param duckPlayer The duckObject that manages the duck.
     * @param duration Amount of time the platform falling animation will take.
     */

    public AnimateImageViewAndDetectCollision(ImageView imageToAnimate, int screenWidth, int screenHeight,
                                              DuckPlayer duckPlayer, int duration, ImageView theCoin
            , GameManager theGame, String typeOfObject){

        super(imageToAnimate, screenWidth, screenHeight, duration);
        this.duckPlayer = duckPlayer;
        this.typeOfObject = typeOfObject;
        this.soundEffect = new SoundManager(imageToAnimate.getContext());
        this.settings = new Settings(imageToAnimate.getContext());
        this.theGame = theGame;
        settings.loadMuteStatus();
        collisionHandler.postDelayed(collisionChecker, 100);
        this.theCoin = theCoin;

        settings.checkMute(soundEffect); // Used for checking if collision sounds should be muted
    }

    /**
     * This runnable is checking for collision repeatedly until the game ends.
     * It uses the checkCollision method for collision, if collision is detected
     * the function will find out what function should be called as result of the collision by
     * comparing to the typeOfObject String.
     */
    Runnable collisionChecker = new Runnable(){
        public void run(){
            int maxHeight = 150; // We wont let the duck jump if it is higher than this

            if(typeOfObject.equals("withCoin")) { // If there is a coin
                theCoin.setX(imageToAnimate.getX()); // Setting the coin and platform to the same x
                // so that even though they respawn randomly they will be put together
            }
            // Check for collision and if duck is too high
            if (checkCollision() && duckPlayer.getDuckY() > maxHeight){

                if(typeOfObject.equals("platform")) {
                    platformCollision();
                }

                else if(typeOfObject.equals("hazard")) {
                    hazardCollision();
                }

                else if(typeOfObject.equals("withCoin")) {
                    platformWithCoinCollision();
                }

            }
            // Continue the collision check if game hasn't ended. Uses the same stopping boolean
            // As the animation in AnimateImageView
            if(!stopAnimation && !isGamePaused){
                collisionHandler.postDelayed(this, 50);
            }
        }
    };

    /**
     * This method is called when there is collision and the object that there was collision with
     * was a platform. This method makes the duck jump, and makes the quack sound.
     */
    private void platformCollision(){
        soundEffect.playSound(R.raw.quack);
        duckPlayer.jump();
    }

    /**
     * This method is called when there is collision and the object there is collision with is
     * a hazard. This method ends the game.
     */
    private void hazardCollision(){
        boolean gameOutcome = false;
        theGame.endGame(gameOutcome);
    }

    /**
     * This method is called when there is collision and the object there is collision with is a
     * platform that has a coin. This method calls platformCollision to make the duck act as it
     * should when coming in contact with the platform, but if the coin hasn't been collected yet
     * it will hide the coin and raise the number of coins collected
     */
    private void platformWithCoinCollision(){
        platformCollision();
        // if the coin is isn't collected yet, hide it and increase coins
        if (theCoin.getVisibility() == View.VISIBLE) {
            theCoin.setVisibility(View.INVISIBLE);
            int newCoinAmount = duckPlayer.getCoinsCollected() + 1; // Collected
            duckPlayer.setCoinsCollected(newCoinAmount);
        }
    }

    /**
     * This method is used to detect collision by using the collision checker class. This method
     * is called constantly in the collisionChecker runnable
     *
     * @return return true if the duck is on a platform
     */
    public boolean checkCollision() {
        // Getting all of the coordinates to do comparisons between duck and ImageView this class
        // is animating
        int duckTopY = duckPlayer.getDuckY();
        int duckBottomY = duckPlayer.getDuckY() + duckPlayer.getDuckHeight();
        int objectTopY = (int) imageToAnimate.getY();
        int objectBottomY = (int) imageToAnimate.getY() + imageToAnimate.getHeight();
        int duckLeft = duckPlayer.getDuckX();
        int duckRight = duckLeft + duckPlayer.getDuckWidth();
        int duckHalf = duckPlayer.getDuckHeight() / 2;
        int objectLeft = (int) imageToAnimate.getX();
        int objectRight = objectLeft + imageToAnimate.getWidth();
        if(typeOfObject.equals("hazard")) { // Making the coordinates a tighter fit for hazard
            objectRight -= 50;
            objectLeft += 80;
        }

        // Using collision check class to detect collision
        CollisionChecker collisionChecker = new CollisionChecker(
                duckTopY, duckBottomY, objectTopY, objectBottomY,
                duckLeft, duckRight, duckHalf, objectLeft, objectRight
        );

        return collisionChecker.checkCollision();
    }
    @Override
    /**
     * This method is called in game manager to resume the ImageView being animated.
     * It works by setting isGamePaused to false and using the .resume() function from the
     * ObjectAnimator library. This method overrides the one in AnimateImageView because in this
     * class the resume function needs the extra functionality of resuming the collision detection
     * runnable.
     */
    public void resumeAnimation() {
        isGamePaused = false;
        if (fallAnimator != null && fallAnimator.isPaused()) {
            fallAnimator.resume();
        }
        collisionHandler.postDelayed(collisionChecker, 100);
    }

}
