package com.example.duckjumpgame;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * The Settings class is designed to be instantiated in any class you need, usually any class that
 * contains a SoundManager object, and in the future, any class that requires data to be locally
 * stored, and recalled on launch. This works by utilizing the SharedPreferences utility in android
 * studio to save and load the appropriate data needed to update things like settings and save
 * progress within a particular class.
 */
public class Settings {
    private static final String PREFERENCES = "prefsFile";
    private static final String IS_MUTED_KEY = "isMuted";
    private boolean isMuted;
    Context currentContext;

    public Settings(Context currentContext){
        this.currentContext = currentContext;
        this.loadMuteStatus();
    }

    /**
     * This checks the current mute status from the shared preferences and
     * applies it to the SoundManager object that has been passed. This way
     * we prevent having to do the check within each class that has a
     * SoundManager object.
     * @param soundEffect - SoundManager object you want to change
     */
    public void checkMute(SoundManager soundEffect){
        if(this.isMuted) {
            soundEffect.isMuted = true;
        }
    }

    /**
     * This saves the current mute status whenever the button is pressed on
     * the main menu. By using SharedPreferences, we can save this information
     * locally to the phone, and recall it whenever a new intent is made, or when
     * the app is reset.
     */
    public void saveMuteStatus(){
        SharedPreferences preferences = currentContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_MUTED_KEY, isMuted);
        editor.apply();
    }

    /**
     * This loads the current mute status whenever it is called. We use this
     * function when creating a new settings object to ensure that if the sound
     * is supposed to be muted, we update the appropriate soundEffect objects
     * by using the checkMute(SoundManager) method within this class.
     */
    public void loadMuteStatus(){
        SharedPreferences soundSetting = currentContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        isMuted = soundSetting.getBoolean(IS_MUTED_KEY, false);
    }

    public boolean getIsMuted(){
        return this.isMuted;
    }
    public void setIsMuted(boolean isMuted){
        this.isMuted = isMuted;
    }
}
