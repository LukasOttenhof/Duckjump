package com.example.duckjumpgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

public class gameManager extends AppCompatActivity{
    private DuckPlayer duckPlayer;
    private Handler winHandler = new Handler();
    int screenWidth;
    int screenHeight;
    public int score = 0; //need way to implement score


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_manager);

        //Get the player icon
        ImageView theDuck = findViewById(R.id.theDuck);


        //Getting screen width so there is a max on how far left and right the player icon can move
        this.screenWidth = getResources().getDisplayMetrics().widthPixels;
        this.screenHeight = getResources().getDisplayMetrics().heightPixels;

        //Set up the DuckPlayer instance
        duckPlayer = new DuckPlayer(theDuck, screenWidth, screenHeight);

        //Start the bounce animation for the duck player
        duckPlayer.startBounceAnimation();

        //Start the platform animation
        managePlatforms();
        //start check for win
        winHandler.postDelayed(winChecker, 100);

    }


    public void managePlatforms(){
        ImageView platform1 = findViewById(R.id.platform1);
        ImageView platform2 = findViewById(R.id.platform2);
        ImageView platform3 = findViewById(R.id.platform3);

        ImageView TopPlatform1 = findViewById(R.id.platformTop1);
        ImageView TopPlatform2 = findViewById(R.id.platformTop2);
        ImageView TopPlatform3 = findViewById(R.id.platformTop3);

        PlatformManager InitialPlatform1 = new PlatformManager(platform1, screenWidth, screenHeight, duckPlayer, 4000, 40000);
        PlatformManager InitialPlatform2 = new PlatformManager(platform2, screenWidth, screenHeight, duckPlayer, 3000, 30000);
        PlatformManager InitialPlatform3 = new PlatformManager(platform3, screenWidth, screenHeight, duckPlayer, 2000, 20000);

        PlatformManager platform4 = new PlatformManager(TopPlatform1, screenWidth, screenHeight, duckPlayer, 6000, 6000);
        PlatformManager platform5 = new PlatformManager(TopPlatform2, screenWidth, screenHeight, duckPlayer, 5000, 5000);
        PlatformManager platform6 = new PlatformManager(TopPlatform3, screenWidth, screenHeight, duckPlayer, 4000, 4000);

        //starting the animation and collison check
        InitialPlatform1.managePlatform();
        InitialPlatform2.managePlatform();
        InitialPlatform3.managePlatform();
        platform4.managePlatform();
        platform5.managePlatform();
        platform6.managePlatform();
    }

    public void endGame(){
        //Open the winPage when game ends
        Intent intent = new Intent(this, winPage.class);
        startActivity(intent);
    }



    //runnable is running every 100 milliseconds checking for game end condition
    //learned how to use runnable and handalers from examples online
    Runnable winChecker = new Runnable(){
        public void run(){
            //Check for if duck is too low
            if (duckPlayer.getDuckY() >= screenHeight){
                endGame();
                return;
            }
            winHandler.postDelayed(this, 100); //execute again in 100 millis
        }
    };

}
