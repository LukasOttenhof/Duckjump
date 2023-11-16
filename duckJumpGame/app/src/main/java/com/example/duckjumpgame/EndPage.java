package com.example.duckjumpgame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndPage extends AppCompatActivity{
    private TextView finalScoreTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_page);
        finalScoreTextView = findViewById(R.id.finalScoreTextView);

        // Retrieve the final score from the intent
        int finalScore = getIntent().getIntExtra("finalScore", 0);

        // Update the TextView with the final score
        finalScoreTextView.setText("You win! Your score: " + finalScore);
    }

    public void enterMainMenu(View view){
        // Go back to main menu when the button is clicked
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void startGame(View view){
        // Restart game when the button is clicked
        Intent intent = new Intent(this, GameManager.class);
        startActivity(intent);
    }

}