package com.example.duckjumpgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class GameManager extends AppCompatActivity{
    private DuckPlayer duckPlayer;
    private Handler winHandler = new Handler();

    private boolean isGamePaused = false;
    public boolean stopWinHandler = false;
    private TextView scoreDisplay;
    private TextView coinDisplay;
    private int finalScore;
    private int screenWidth;
    private int screenHeight;
    CreatePlatform initialPlatform1;
    CreatePlatform initialPlatform2;
    CreatePlatform initialPlatform3;
    CreatePlatform hiddenPlatform1;
    CreatePlatform hiddenPlatform2;
    CreatePlatform hiddenPlatform3;
    CreatePlatform hiddenPlatform4;
    CreatePlatform hiddenPlatform5;
    CreatePlatformWithCoin coinPlatform;
    AnimateAndDetectCollision animateCoin;
    CreateHazard hazardObject;
    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_manager);

        // Get the player icon
        ImageView theDuck = findViewById(R.id.theDuck);
        ConstraintLayout background = findViewById(R.id.background);

        scoreDisplay = findViewById(R.id.scoreNum);
        coinDisplay = findViewById(R.id.coinNum);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        // Set up the DuckPlayer instance
        duckPlayer = new DuckPlayer(theDuck, screenHeight, screenWidth);
        Button pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    togglePause(v);
                }
                return true;
            }
        });
        // Start the platform animation
        managePlatforms();
        // Start check for win
        winHandler.postDelayed(winChecker, 100);

        // Start the platform with coin and hazard
        manageCoinAndHazard();

        // Setting up a touch listener for the background, when touch is detected, send this info
        // to the backend, calling the onTouchEvent in duckPlayer
        background.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                return duckPlayer.onTouchEvent(event);
            }
        });
    }

    public void togglePause(View view) {
        if (isGamePaused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    private void pauseGame() {
        isGamePaused = true;

        // Stop any ongoing animations or game logic here
        // For example, stop the platform animations
        duckPlayer.pauseAnimation();
        initialPlatform1.pauseAnimation();
        initialPlatform2.pauseAnimation();
        initialPlatform3.pauseAnimation();
        hiddenPlatform1.pauseAnimation();
        hiddenPlatform2.pauseAnimation();
        hiddenPlatform3.pauseAnimation();
        hiddenPlatform4.pauseAnimation();
        hiddenPlatform5.pauseAnimation();
        coinPlatform.pauseAnimation();
        animateCoin.pauseAnimation();
        hazardObject.pauseAnimation();
    }

    private void resumeGame() {
        isGamePaused = false;
        // Resume any animations or game logic here
        duckPlayer.resumeAnimation();
        initialPlatform1.resumeAnimation();
        initialPlatform2.resumeAnimation();
        initialPlatform3.resumeAnimation();
        hiddenPlatform1.resumeAnimation();
        hiddenPlatform2.resumeAnimation();
        hiddenPlatform3.resumeAnimation();
        hiddenPlatform4.resumeAnimation();
        hiddenPlatform5.resumeAnimation();
        coinPlatform.resumeAnimation();
        animateCoin.resumeAnimation();
        hazardObject.resumeAnimation();
    }


    public boolean onTouchEvent(MotionEvent event) {
        if (!isGamePaused) {
            // Subtract to center duck on pointer
            int newX = (int) event.getRawX() - duckPlayer.getDuckWidth() / 2;
            // Getting duck params so we can change them
            ViewGroup.MarginLayoutParams params = duckPlayer.getDuckLayoutParams();
            // Adding the change
            // as long as the new location will be within the screen make the change
            if (newX >= 0 && newX + duckPlayer.getDuckWidth() <= screenWidth) {
                params.leftMargin = newX;
                duckPlayer.setDuckLayoutParams(params);
            }
        }
        return true;
    }


    /**
     * Manages the platforms that are created by this function.
     *
     * Each platform is created with bounds for screen width and height
     * to prevent from spawning outside of play area, as well as a duration, and a respawn delay
     *
     * Initial platforms are spawned at the start of the game and are in the
     * same location each time while hidden platforms respawn after falling off
     * bottom of the screen
     */
    public void managePlatforms(){
        ImageView platform1 = findViewById(R.id.platform1);
        ImageView platform2 = findViewById(R.id.platform2);
        ImageView platform3 = findViewById(R.id.platform3);

        ImageView TopPlatform1 = findViewById(R.id.platformTop1);
        ImageView TopPlatform3 = findViewById(R.id.platformTop3);
        ImageView TopPlatform4 = findViewById(R.id.platformTop4);
        ImageView TopPlatform5 = findViewById(R.id.platformTop5);
        ImageView TopPlatform6 = findViewById(R.id.platformTop6);

        // These platforms are the ones that start on the screen. Dont want them to respawn on screen so make delay huge

        initialPlatform1 = new CreatePlatform(platform1, screenWidth, screenHeight, duckPlayer, 4000, 100000);
        initialPlatform2 = new CreatePlatform(platform2, screenWidth, screenHeight, duckPlayer, 3000, 100000);
        initialPlatform3 = new CreatePlatform(platform3, screenWidth, screenHeight, duckPlayer, 2000, 100000);

        // The rest of the platforms, they will respawn consistalnty throughout the game.
        hiddenPlatform1 = new CreatePlatform(TopPlatform1, screenWidth, screenHeight, duckPlayer, 6000, 6000);
        hiddenPlatform2 = new CreatePlatform(TopPlatform3, screenWidth, screenHeight, duckPlayer, 5500, 5500);
        hiddenPlatform3 = new CreatePlatform(TopPlatform4, screenWidth, screenHeight, duckPlayer, 7000, 7000);
        hiddenPlatform4 = new CreatePlatform(TopPlatform5, screenWidth, screenHeight, duckPlayer, 10000, 10000);
        hiddenPlatform5 = new CreatePlatform(TopPlatform6, screenWidth, screenHeight, duckPlayer, 6000, 6000);
    }

    private void manageCoinAndHazard(){
        // Create a hazard by getting the image we want to be a hazard and using it to make a
        // hazard object.
        ImageView hazardImage = findViewById(R.id.hazard);
        // For some reason if you make it its own AnimateAndDetectCollision it crashes
        hazardObject = new CreateHazard(hazardImage, screenWidth, screenHeight, duckPlayer, 4000, 6000, this);

        // Creating the platform with a coin
        ImageView TopPlatform2 = findViewById(R.id.platformTop2);
        ImageView theCoin = findViewById(R.id.coin);

        //MAKE RESPAWN LONGER IN ACTUAL GAME, IT IS FAST NOW FOR TESTING
        coinPlatform = new CreatePlatformWithCoin(TopPlatform2, screenWidth, screenHeight, duckPlayer, 5500, 5500, theCoin);
        animateCoin = new AnimateAndDetectCollision(theCoin, screenWidth, screenHeight, duckPlayer, 5500, 5500);
    }

    /**
     * Open the winPage when the game is over and end the runnables.
     * If the runnables arent ended the quacking noise will continue
     * while in the EndPage
     */
    public void endGame(){
        stopWinHandler = true;
        initialPlatform1.endRunnables();
        initialPlatform2.endRunnables();
        initialPlatform3.endRunnables();
        hiddenPlatform1.endRunnables();
        hiddenPlatform2.endRunnables();
        hiddenPlatform3.endRunnables();
        hiddenPlatform4.endRunnables();
        hiddenPlatform5.endRunnables();
        coinPlatform.endRunnables();
        animateCoin.endRunnables();
        hazardObject.endRunnables();

        finalScore = calculateAndDisplayScore();
        Intent intent = new Intent(this, EndPage.class);
        intent.putExtra("finalScore", finalScore);
        startActivity(intent);
    }


    /**
     * Runnable is running every 100 milliseconds checking for game end condition
     * which is when the DuckPlayer is below the screen bounds. It also updates the score
     * that the user has by setting the score display to the calculated score.
     *
     * Learned how to use runnable and handlers from examples online
     */
    Runnable winChecker = new Runnable() {
        public void run() {
            // Check if the game is not paused
            if (!isGamePaused) {
                // Check for if duck is too low
                if (duckPlayer.getDuckY() >= screenHeight) {
                    endGame();
                    return;
                }
                calculateAndDisplayScore();
            }
            // If the game hasn't ended and is not paused, continue
            if (!stopWinHandler) {
                winHandler.postDelayed(this, 100); // execute again in 100 millis
            }
        }
    };


    /**
     * This method calculates and displays the score by first caluclating the score, this is done
     * by multiplying the platforms touched by the coins collected.
     * It then updates the displayed score by setting the TextView that displays the score to be
     * the score that was calculated. It is called when the duck makes collision since score is
     * based off of the score being updated.
     */
    public int calculateAndDisplayScore(){
        int score;
        score = duckPlayer.getCoinsCollected() * (duckPlayer.getPlatformsTouched() + duckPlayer.getScoreDistance());
        scoreDisplay.setText(String.valueOf(score));
        coinDisplay.setText(String.valueOf(duckPlayer.getCoinsCollected()-1));
        return score;
    }
}
