package com.example.duckjumpgame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndPage extends AppCompatActivity{
    private TextView finalScoreTextView;
    private SoundManager buttonSoundEffect;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_page);
        finalScoreTextView = findViewById(R.id.finalScoreTextView);

        buttonSoundEffect = new SoundManager(this);

        // Retrieve the final score from the intent
        int finalScore = getIntent().getIntExtra("finalScore", 0);
        boolean wasGameWon = getIntent().getBooleanExtra("wasGameWon",false);

        if (wasGameWon) {
            finalScoreTextView.setText(" You Win!        Your Score: " + finalScore);
        } else {
            finalScoreTextView.setText(" You Lose.        Your Score: " + finalScore);
        }

    }


    public void enterMainMenu(View view){
        buttonSoundEffect.playSound(R.raw.button_sound);
        // Go back to main menu when the button is clicked
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void startGame(View view){
        buttonSoundEffect.playSound(R.raw.button_sound);
        // Restart game when the button is clicked
        Intent intent = new Intent(this, GameManager.class);
        startActivity(intent);
    }

}