package com.example.duckjumpgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
 * openHowToPlay() - Swaps the main screen to view the How to play text by swapping UI elements
 * backToMain() - Called when in credits screen or How to play screen and the return to main menu
 * button is clicked
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
     * This method displays the main text which is a big white text box and sets its text to
     * be the credits.
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
        Button howToPlayButton = findViewById(R.id.howToPlayButton);
        creditButton.setOnClickListener(this::backToMain);

        creditButton.setText("Back To Menu");
        mainMenu.setVisibility(View.INVISIBLE);
        creditsText.setVisibility(View.VISIBLE);
        creditsText.setText("Created by Lukas, Edan, and Cole. \n\nImage Credits:" +
                " \nhttps://www.flaticon.com/free-icons icons created by Freepik - Flaticon");
        playButton.setVisibility(View.INVISIBLE);
        howToPlayButton.setVisibility(View.INVISIBLE);

    }
    /**
     * Handles the "How To Play" button on the main menu
     * and swaps main menu widgets with the corresponding credits widgets.
     *
     * This method displays the main text which is a big white text box and sets its text to
     * be instructions on how to play.
     *
     * @param myView - Current View
     */
    public void openHowToPlay(View myView){
        buttonSoundEffect.playSound(R.raw.button_sound);
        ImageView mainMenu = findViewById(R.id.mainMenu);
        TextView instructionsText = findViewById(R.id.mainText);
        Button playButton = findViewById(R.id.playButton);
        Button creditButton = findViewById(R.id.creditButton);
        Button howToPlayButton = findViewById(R.id.howToPlayButton);
        howToPlayButton.setOnClickListener(this::backToMain);

        howToPlayButton.setText("Back To Menu");
        mainMenu.setVisibility(View.INVISIBLE);
        instructionsText.setVisibility(View.VISIBLE);
        instructionsText.setText("Welcome to Duck Jump!\n While playing swipe or tap anywhere to move\n " +
                "Collect as many coins as you can \n Don't let your duck fall, collide with platforms to jump higher \n Dodge the bird\n" +
                "Good Luck and Merry Christmas!");

        playButton.setVisibility(View.INVISIBLE);
        creditButton.setVisibility(View.INVISIBLE);
    }

    /**
     * This method is called after pressing back to menu. This restores the main menu back
     * to its original state.
     *
     * This method is used to switch the main menu back to how it is before pressing credits or
     * how to play. It works by setting the credits button and how to play buttons onclick function
     * to be what they are by default rather than this method. It also makes all buttons visable
     * again.
     *
     * @param myView - Current View
     */
    public void backToMain(View myView){
        buttonSoundEffect.playSound(R.raw.button_sound);
        ImageView mainMenu = findViewById(R.id.mainMenu);
        TextView creditsText = findViewById(R.id.mainText);
        Button playButton = findViewById(R.id.playButton);
        Button creditButton = findViewById(R.id.creditButton);
        Button howToPlayButton = findViewById(R.id.howToPlayButton);
        creditButton.setOnClickListener(this::openCredits);
        howToPlayButton.setOnClickListener(this::openHowToPlay);

        creditButton.setText("Credits");
        howToPlayButton.setText("How To Play");
        mainMenu.setVisibility(View.VISIBLE);
        creditsText.setVisibility(View.INVISIBLE);
        playButton.setVisibility(View.VISIBLE);
        howToPlayButton.setVisibility(View.VISIBLE);
        creditButton.setVisibility(View.VISIBLE);
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