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
import android.widget.ImageView;
import android.widget.TextView;

/**
 * MainActivity.java class is designed to control the main menu activity within the game. This
 * includes the UI elements within the main menu, such as the buttons, images, transitions, and
 * settings (mute button for the time being). This also handles the main menu theme music using the
 * SoundManager object. Another important element is the Settings object, this ensures that when
 * you toggle mute on or off, regardless of whether or not you close the game, the choice will
 * remain selected throughout DuckJump. The four notable methods within this class are:
 *
 * startGame() - Starts the game screen by switching to the GameManager class and intent
 * openCredits() - Swaps the main screen to view the credits text by swapping UI elements
 * backToMain() - Called when in credits screen and the return to main menu button is clicked
 * muteSound() - Normally we would have the settings handle the audio, however, since you can toggle
 * mute on this screen if you need, we needed to have an additional method to handle this particular
 * activity's sound
 */

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
        ImageView mainMenu = findViewById(R.id.mainMenu);
        TextView creditsText = findViewById(R.id.mainText);
        Button playButton = findViewById(R.id.playButton);
        Button creditButton = findViewById(R.id.creditButton);
        creditButton.setOnClickListener(this::backToMain);

        creditButton.setText("Back To Menu");
        mainMenu.setVisibility(View.INVISIBLE);
        creditsText.setVisibility(View.VISIBLE);
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
        ImageView mainMenu = findViewById(R.id.mainMenu);
        TextView creditsText = findViewById(R.id.mainText);
        Button playButton = findViewById(R.id.playButton);
        Button creditButton = findViewById(R.id.creditButton);
        creditButton.setOnClickListener(this::openCredits);

        creditButton.setText("Credits");
        mainMenu.setVisibility(View.VISIBLE);
        creditsText.setVisibility(View.INVISIBLE);
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