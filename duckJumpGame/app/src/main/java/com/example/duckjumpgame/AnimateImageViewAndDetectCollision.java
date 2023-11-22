package com.example.duckjumpgame;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class AnimateImageViewAndDetectCollision extends AnimateImageView {
    private Handler collisionHandler = new Handler();
    private DuckPlayer duckPlayer;
    private SoundManager soundEffect;
    private Settings settings;
    private ImageView theCoin;
    private ImageView platform;
    private GameManager theGame;
    private String typeOfObject;

    /**
     * In the constructor the collision handler is called so that collision
     * will start to be detected.
     *
     * @param platform The Imageview of the platform that is being animated.
     * @param screenWidth Maximum width the platform can respawn at.
     * @param screenHeight Bottom of the screen, endpoint of the animation.
     * @param duckPlayer The duckObject that manages the duck.
     * @param duration Amount of time the platform falling animation will take.
     * @param respawnDelay Amount of time between animations of the platform falling.
     */

    public AnimateImageViewAndDetectCollision(ImageView platform, int screenWidth, int screenHeight,
                                              DuckPlayer duckPlayer, int duration, ImageView theCoin
            , GameManager theGame, String typeOfObject){

        super(platform, screenWidth, screenHeight, duckPlayer, duration);
        this.duckPlayer = duckPlayer;
        this.typeOfObject = typeOfObject;
        this.platform = platform;
        this.soundEffect = new SoundManager(platform.getContext());
        this.settings = new Settings(platform.getContext());
        this.theGame = theGame;
        settings.loadMuteStatus();
        collisionHandler.postDelayed(collisionChecker, 100);
        this.theCoin = theCoin;

        settings.checkMute(soundEffect);
    }

    /**
     * This runnable is checking for collision repeatedly until the game ends.
     * It uses the checkCollision method for collision, if collision is detected
     * jump will be called, and a quack sound will play. If the coin != null there is
     * a coin so update number of coins collected and it so the coin cant
     * be collected again until after the coin has respawned. If theCoin == platform this is a
     * signal that the imageview being animated is a hazard, if collision is detected end game.
     */
    Runnable collisionChecker = new Runnable(){
        public void run(){
            int maxHeight = 150; // We wont let the duck jump if it is higher than this
            if(theCoin != null) { // If there is a coin
                theCoin.setX(platform.getX());//setting the coin and platform to the same x
                //so that even though they respawn randomly they will be put together
            }
            // Check for collision and if duck is too high
            if (checkCollision() && duckPlayer.getDuckY() > maxHeight){
                // If yes run jump and play sound effect
                if(typeOfObject.equals("platform")) {
                    soundEffect.playSound(R.raw.quack);
                    duckPlayer.jump();
                }
                // If the platform == the coin there was a collision so end game
                else if(typeOfObject.equals("hazard")) {
                    boolean gameOutcome = false;
                    theGame.endGame(gameOutcome);
                }
                // If there is a coin and it is visible it hasn't been collected yet.
                else if(typeOfObject.equals("withCoin")) {
                    soundEffect.playSound(R.raw.quack);
                    duckPlayer.jump();
                    if (theCoin.getVisibility() == View.VISIBLE) {
                        theCoin.setVisibility(View.INVISIBLE);
                        int newCoinAmount = duckPlayer.getCoinsCollected() + 1;
                        duckPlayer.setCoinsCollected(newCoinAmount);
                    }
                }

            }
            // Continue the collision check if game hasn't ended
            if(!stopRunnable){
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
    public boolean checkCollision(){
        int duckTopY = duckPlayer.getDuckY();
        int duckBottomY = duckPlayer.getDuckY() + duckPlayer.getDuckHeight();
        int platformTopY = (int)imageToAnimate.getY();
        int platformBottomY = (int)imageToAnimate.getY() + imageToAnimate.getHeight();
        int duckLeft = duckPlayer.getDuckX();
        int duckRight = duckLeft + duckPlayer.getDuckWidth();
        int platformLeft = (int) imageToAnimate.getX();
        int platformRight = platformLeft + imageToAnimate.getWidth();

        // First create booleans that eveluate to true if there is vertical overlap
        boolean topCollision = duckTopY <= platformBottomY && duckTopY >= platformTopY;
        boolean bottomCollision = duckBottomY >= platformTopY && duckBottomY <= platformBottomY;
        boolean middleCollision = (duckBottomY + duckPlayer.getDuckHeight()/2) >= platformTopY && (duckBottomY +
                (duckPlayer.getDuckHeight()/2) <= platformBottomY);

        // Next create booleans that evaluate to true if there is horizontal overlap
        boolean leftCollision = duckLeft >= platformLeft && duckLeft <= platformRight;
        boolean rightCollision = duckRight >= platformLeft && duckRight <= platformRight;

        // Return true if there is vertical overlap and horizontal overlap to indicate collision
        return (topCollision || bottomCollision || middleCollision) && (leftCollision || rightCollision);

    }

}
