package com.example.duckjumpgame;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

/**
 * This class extends AnimateImageView because it is for Imageviews that need to be animated with
 * the additional functionality of being able to detect collison. This class is used for all
 * Imageviews that need collision detection with the duck, this includes the platforms, hazards, and
 * the platform wiht a coin.
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
     * In the constructor the collision handler is called so that collision
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

        settings.checkMute(soundEffect);
    }

    /**
     * This runnable is checking for collision repeatedly until the game ends.
     * It uses the checkCollision method for collision, if collision is detected
     * the function will find out what needs to happen by comparing to the typeOfObject String.
     * If the collision was for a platform the duck will jump and quack, if the collision was for
     * a hazard the game will end, and if the collision was for the platform with a coin and the
     * coin hadnt been collected quack, jump, increase coins collected, and hide the coin.
     */
    Runnable collisionChecker = new Runnable(){
        public void run(){
            int maxHeight = 150; // We wont let the duck jump if it is higher than this

            if(typeOfObject.equals("withCoin")) { // If there is a coin
                theCoin.setX(imageToAnimate.getX());//setting the coin and platform to the same x
                //so that even though they respawn randomly they will be put together
            }
            // Check for collision and if duck is too high
            if (checkCollision() && duckPlayer.getDuckY() > maxHeight){

                if(typeOfObject.equals("platform")) {
                    soundEffect.playSound(R.raw.quack);
                    duckPlayer.jump();
                }

                else if(typeOfObject.equals("hazard")) {
                    boolean gameOutcome = false;
                    theGame.endGame(gameOutcome);
                }

                else if(typeOfObject.equals("withCoin")) {
                    soundEffect.playSound(R.raw.quack);
                    duckPlayer.jump();
                    if (theCoin.getVisibility() == View.VISIBLE) {// if the coin is isnt collected
                        theCoin.setVisibility(View.INVISIBLE); // yet, hide it and increase coins
                        int newCoinAmount = duckPlayer.getCoinsCollected() + 1; // collected
                        duckPlayer.setCoinsCollected(newCoinAmount);
                    }
                }

            }
            // Continue the collision check if game hasn't ended. Uses the same stopping boolean
            // As the animation in AnimateImageView
            if(!stopAnimation){
                collisionHandler.postDelayed(this, 50);
            }
        }
    };

    /**
     * This method is used to detect collision by comparing coordinates of the
     * duck to coordinates of platforms. It is called constantly in the
     * collisionChecker runnable
     *
     * @return return true if the duck is on a platform
     */
    public boolean checkCollision() {
        int duckTopY = duckPlayer.getDuckY();
        int duckBottomY = duckPlayer.getDuckY() + duckPlayer.getDuckHeight();
        int platformTopY = (int) imageToAnimate.getY();
        int platformBottomY = (int) imageToAnimate.getY() + imageToAnimate.getHeight();
        int duckLeft = duckPlayer.getDuckX();
        int duckRight = duckLeft + duckPlayer.getDuckWidth();
        int duckHalf = duckPlayer.getDuckHeight() / 2;
        int platformLeft = (int) imageToAnimate.getX();
        int platformRight = platformLeft + imageToAnimate.getWidth();

        CollisionChecker collisionChecker = new CollisionChecker(
                duckTopY, duckBottomY, platformTopY, platformBottomY,
                duckLeft, duckRight, duckHalf, platformLeft, platformRight
        );

        return collisionChecker.checkCollision();
    }

}
