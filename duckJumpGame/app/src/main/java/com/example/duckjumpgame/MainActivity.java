package com.example.duckjumpgame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    // This method will be called when the play button is clicked

    /**
     * Handles the new intent for the play button and
     * switches to play screen
     *
     * @param view - Current View
     */
    public void startGame(View view){
        // Start game when the button is clicked
        Intent intent = new Intent(this, GameManager.class);
        startActivity(intent);
    }


    /**
     * Handles the "Credits" button on the main menu
     * and swaps main menu widgets with the corresponding credits widgets.
     *
     * This is done to prevent memory leak and performance issues associated with creating
     * or removing new intents.
     *
     * @param myView - Current View
     */
    public void openCredits(View myView){
        TextView mainText = findViewById(R.id.mainText);
        Button playButton = findViewById(R.id.playButton);
        Button creditButton = findViewById(R.id.creditButton);
        mainText.setText("Created by: Cole, Edan, and Lukas");
        mainText.setTextSize(30);
        creditButton.setOnClickListener(this::backToMain);

        creditButton.setText("Back To Menu");
        playButton.setVisibility(View.INVISIBLE);
        playButton.setEnabled(false);
    }


    /**
     * Handles the "Main Menu" button on the credits screen
     * and swaps credits widgets with the corresponding main menu widgets.
     *
     * This is done to prevent memory leak and performance issues associated with creating
     * or removing new intents.
     *
     * @param myView - Current View
     */
    public void backToMain(View myView){
        TextView mainText = findViewById(R.id.mainText);
        Button playButton = findViewById(R.id.playButton);
        Button creditButton = findViewById(R.id.creditButton);
        mainText.setText("Duck Jump");
        mainText.setTextSize(50);
        creditButton.setOnClickListener(this::openCredits);

        creditButton.setText("Credits");
        playButton.setVisibility(View.VISIBLE);
        playButton.setEnabled(true);
    }

}