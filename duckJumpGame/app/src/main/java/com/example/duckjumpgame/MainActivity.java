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
    public void startGame(View view){
        // Start game when the button is clicked
        Intent intent = new Intent(this, GameManager.class);
        startActivity(intent);
    }

    //used when pressing open credits button
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
    //this is used to go back after opening credits
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