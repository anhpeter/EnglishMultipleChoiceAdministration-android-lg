package com.example.multiple_choice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    public boolean internetConnectionAvailable() {
        int timeOut = 3000;
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress!=null && !inetAddress.equals("");
    }

    private BroadcastReceiver networkStateReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            doSomethingOnNetworkChange();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    protected void doSomethingOnNetworkChange(){};
}
