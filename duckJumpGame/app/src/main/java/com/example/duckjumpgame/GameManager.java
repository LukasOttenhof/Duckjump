package com.example.duckjumpgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class GameManager extends AppCompatActivity{
    private DuckPlayer duckPlayer;
    private SoundManager soundEffect;
    private boolean isPaused = false;
    private Settings settings;
    private Handler winHandler = new Handler();
    public boolean stopRunnables = false;
    private TextView scoreDisplay;
    private TextView timeDisplay;
    private TextView coinDisplay;
    private static final String PREFERENCES = "prefsFile";
    private static final String IS_MUTED_KEY = "isMuted";
    private boolean isMuted;
    private int finalScore;
    private boolean wasGameWon;
    private int timePlayed = 0;
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
    CreatePlatform coinPlatform;
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

        soundEffect = new SoundManager(this);
        settings = new Settings(this);

        settings.loadMuteStatus();

        settings.checkMute(soundEffect);

        // Get the player icon
        ImageView theDuck = findViewById(R.id.theDuck);
        ConstraintLayout background = findViewById(R.id.background);


        scoreDisplay = findViewById(R.id.scoreNum);
        timeDisplay = findViewById(R.id.timeNum);
        coinDisplay = findViewById(R.id.coinNum);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        // Set up the DuckPlayer instance
        duckPlayer = new DuckPlayer(theDuck, screenHeight, screenWidth);

        // Start the platform animation
        managePlatforms();
        // Start check for win
        winHandler.postDelayed(winChecker, 100);
        winHandler.postDelayed((timeUpdater),100);

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


    /**
     * Manages the platforms that are created by this function.
     *
     * Each platform is created with bounds for screen width and height
     * to prevent from spawning outside of play area, as well as a duration, and a respawn delay,
     * the coin parameter is set to null because these platforms don't have coins.
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

        initialPlatform1 = new CreatePlatform(platform1, screenWidth, screenHeight, duckPlayer, 4000, 100000, null);
        initialPlatform2 = new CreatePlatform(platform2, screenWidth, screenHeight, duckPlayer, 3000, 100000, null);
        initialPlatform3 = new CreatePlatform(platform3, screenWidth, screenHeight, duckPlayer, 2000, 100000, null);

        // The rest of the platforms, they will respawn consistalnty throughout the game.
        hiddenPlatform1 = new CreatePlatform(TopPlatform1, screenWidth, screenHeight, duckPlayer, 6000, 6000, null);
        hiddenPlatform2 = new CreatePlatform(TopPlatform3, screenWidth, screenHeight, duckPlayer, 5500, 5500, null);
        hiddenPlatform3 = new CreatePlatform(TopPlatform4, screenWidth, screenHeight, duckPlayer, 7000, 7000, null);
        hiddenPlatform4 = new CreatePlatform(TopPlatform5, screenWidth, screenHeight, duckPlayer, 10000, 10000, null);
        hiddenPlatform5 = new CreatePlatform(TopPlatform6, screenWidth, screenHeight, duckPlayer, 6000, 6000, null);
    }

    /**
     * This class initialized the hazard object and coin so that they
     * will become animated and begin detecting collision.
     */
    private void manageCoinAndHazard(){
        // Create a hazard by getting the image we want to be a hazard and using it to make a
        // hazard object.
        ImageView hazardImage = findViewById(R.id.hazard);
        // For some reason if you make it its own AnimateAndDetectCollision it crashes
        hazardObject = new CreateHazard(hazardImage, screenWidth, screenHeight, duckPlayer, 4000, 6000, this);

        // Creating the platform with a coin
        ImageView TopPlatform2 = findViewById(R.id.platformTop2);
        ImageView theCoin = findViewById(R.id.coin);


        coinPlatform = new CreatePlatform(TopPlatform2, screenWidth, screenHeight, duckPlayer, 5500, 5500, theCoin);
        animateCoin = new AnimateAndDetectCollision(theCoin, screenWidth, screenHeight, duckPlayer, 5500, 5500);
    }

    /**
     * Open the winPage when the game is over and end the runnables.
     * If the runnables arent ended the quacking noise will continue
     * while in the EndPage
     *
     * @return return if the game was won
     */
    public boolean endGame(boolean outCome){
        soundEffect.playSound(R.raw.damage_sound);
        stopRunnables = true;
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
        wasGameWon = outCome;
        finalScore = calculateAndDisplayScore();
        Intent intent = new Intent(this, EndPage.class);
        intent.putExtra("finalScore", finalScore);
        intent.putExtra("wasGameWon", wasGameWon);
        startActivity(intent);

        return wasGameWon;
    }


    /**
     * Runnable is running every 100 milliseconds checking for game end condition
     * which is when the DuckPlayer is below the screen bounds. It also updates the score
     * and coins that the user has by calling calculateAndDisplayScore().
     *
     * Learned how to use runnable and handlers from examples online
     */
    Runnable winChecker = new Runnable(){
        public void run(){
            // Check for if duck is too low
            if(timePlayed >= 180){
                boolean winOutCome = true;
                endGame(winOutCome);
            }
            else if (duckPlayer.getDuckY() >= screenHeight){
                boolean winOutCome = false;
                endGame(winOutCome);
                return;
            }
            calculateAndDisplayScore();
            // If the game hasn't ended continue
            if(!stopRunnables && !isPaused){
                winHandler.postDelayed(this, 100); //execute again in 100 millis
            }
        }
    };

    Runnable timeUpdater = new Runnable(){

        public void run() {
            // Your loop logic goes here
            timePlayed +=1;
            calculateAndDisplayTime();

            // Schedule the Runnable to run again after 1 second
            // Unless game has ended
            if(!stopRunnables && !isPaused) {
                winHandler.postDelayed(this, 1000);
            }
        }
    };


    /**
     * This method calculates and displays the score by first caluclating the score, this is done
     * by multiplying the platforms touched by the coins collected.
     * It then updates the displayed score by setting the TextView that displays the score to be
     * the score that was calculated. It also displays the number of coins collected
     * by using getCoinsCollected. It is called when the duck makes collision since score is
     * based off of the score being updated.
     */
    public int calculateAndDisplayScore(){
        int score;
        score = duckPlayer.getCoinsCollected() * (duckPlayer.getPlatformsTouched() + duckPlayer.getScoreDistance());
        scoreDisplay.setText(String.valueOf(score));
        coinDisplay.setText(String.valueOf(duckPlayer.getCoinsCollected() - 1));
        return score;
    }

    public void pauseGame(View myView){
        isPaused = true;
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
        Button pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(this::resumeGame);
        pauseButton.setText("Resume");
    }
    public void resumeGame(View myView){
        isPaused = false;

        winHandler.postDelayed((timeUpdater),100);
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
        Button pauseButton = findViewById(R.id.pauseButton);
        winHandler.postDelayed(winChecker, 100);
        winHandler.postDelayed((timeUpdater),100);
        pauseButton.setOnClickListener(this::pauseGame);
        pauseButton.setText("Pause");
    }

    public void calculateAndDisplayTime(){
        timeDisplay.setText(String.valueOf(timePlayed));
    }
}

