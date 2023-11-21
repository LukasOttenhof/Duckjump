package com.example.duckjumpgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{
    private SoundManager soundEffect;
    private SoundManager buttonSoundEffect;
    private Settings settings;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = new Settings(this);
        
        settings.loadMuteStatus();

        soundEffect = new SoundManager(this);
        buttonSoundEffect = new SoundManager(this);
        Button muteButton = findViewById(R.id.muteButton);

        // Handles the startup of sound and whether or not user last muted.
        if(settings.getIsMuted()){
            soundEffect.isMuted = true;
            buttonSoundEffect.isMuted = true;
            muteButton.setBackgroundResource(R.drawable.mutebutton);

        } else{
            soundEffect.isMuted = false;
            soundEffect.loopSound(R.raw.main_theme_2);
            buttonSoundEffect.isMuted = false;
            muteButton.setBackgroundResource(R.drawable.volume);
        }
    }
    // This method will be called when the play button is clicked

    /**
     * Handles the new intent for the play button and
     * switches to play screen
     *
     * @param view - Current View
     */
    public void startGame(View view){
        buttonSoundEffect.playSound(R.raw.button_sound);
        // Start game when the button is clicked
        Intent intent = new Intent(this, GameManager.class);
        startActivity(intent);
        soundEffect.stopSound();
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
        buttonSoundEffect.playSound(R.raw.button_sound);
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
        buttonSoundEffect.playSound(R.raw.button_sound);
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

    /**
     * Responsible for muting sounds on button press for main menu.
     * Works by setting a property in SoundManager and stopping or starting sound.
     * Also changes the icon to display whether or not the sound is muted.
     * @param myView - Current View
     */
    public void muteSound(View myView){
        Button muteButton = findViewById(R.id.muteButton);

        if(settings.getIsMuted()){
            // If muted when button is pressed, unmute
            soundEffect.isMuted = false;
            soundEffect.loopSound(R.raw.main_theme_2);

            buttonSoundEffect.isMuted = false;
            muteButton.setBackgroundResource(R.drawable.volume);

            settings.setIsMuted(false);
            settings.saveMuteStatus();
        } else{
            // Mute sounds
            soundEffect.isMuted = true;
            soundEffect.stopSound();

            buttonSoundEffect.isMuted = true;

            muteButton.setBackgroundResource(R.drawable.mutebutton);
            settings.setIsMuted(true);
            settings.saveMuteStatus();
        }
    }

}