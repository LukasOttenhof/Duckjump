package com.example.duckjumpgame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WinPage extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_page);

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