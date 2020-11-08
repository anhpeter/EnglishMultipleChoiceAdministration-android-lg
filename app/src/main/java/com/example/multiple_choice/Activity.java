package com.example.multiple_choice;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public abstract class Activity extends AppCompatActivity {

    private String defaultController = "questions";
    private String defaultQuestionLevel = "easy";
    private boolean isChangingController = false;
    private boolean isChangingQuestionLevel = false;

    // SETTER AND GETTER
    public String getController() {
        return getSharedParams().getString("controller", defaultController);
    }

    public String getQuestionLevel() {
        return getSharedParams().getString("questionLevel", defaultQuestionLevel);
    }

    public void setController(String value){
        sharedParamsPutString("controller", value);
    }

    public void setQuestionLevel(String value){
        sharedParamsPutString("questionLevel", value);
    }

    // SHARED PREFERENCES
    public SharedPreferences getSharedParams() {
        SharedPreferences sharedPreferences = getSharedPreferences("global-package", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public void sharedParamsPutInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedParams().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void sharedParamsPutString(String key, String value) {
        SharedPreferences.Editor editor = getSharedParams().edit();
        editor.putString(key, value);
        editor.apply();
    }

    // IS CHANGING...
    public void setIsChangingController(boolean value){
        isChangingController = value;
    }

    public boolean getIsChangingController(){
        return isChangingController;
    }

    public void setIsChangingQuestionLevel(boolean value){
        isChangingQuestionLevel = value;
    }

    public boolean getIsChangingQuestionLevel(){
        return isChangingQuestionLevel;
    }
}
