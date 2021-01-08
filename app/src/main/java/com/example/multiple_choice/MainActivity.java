package com.example.multiple_choice;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import java.util.HashMap;

import Defines.Auth;
import Interfaces.FragmentCommunicate;
import Helpers.Helper;
import MainFragments.IndexTitleBarFragment;
import MainFragments.QuestionIndexFragment;
import MainFragments.UserIndexFragment;

public class MainActivity extends Activity implements FragmentCommunicate {

    FragmentManager fragmentManager;
    IndexTitleBarFragment indexTitleBarFragment;
    QuestionIndexFragment questionIndexFragment;
    UserIndexFragment userIndexFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAuth();
        onInit();
        callFragments();
    }

    private void checkAuth() {
        Auth auth = Auth.getInstance();
        if (auth.getUser()== null) {
            SharedPreferences sharedPreferences = getSharedParams();
            if (sharedPreferences != null) {
                String loggedUsername = sharedPreferences.getString("loggedUsername", "");
                if (!loggedUsername.equals("")) {
                    // logged
                    auth.setUserByUsername(loggedUsername);
                } else {
                    // not logged
                    navigateToLogin();
                }
            } else navigateToLogin();
        }
    }

    private void navigateToLogin() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }

    private void onInit() {
        fragmentManager = getFragmentManager();
        Helper.initFontAwesome(getApplicationContext(), getWindow().getDecorView());
    }

    private void callFragments() {
        // title bar
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        indexTitleBarFragment = new IndexTitleBarFragment();
        fragmentTransaction.add(R.id.indexTitleBarFrame, indexTitleBarFragment, "titleBar");
        fragmentTransaction.commit();
        if (getController().equals("questions")) callQuestionFragments();
        else if (getController().equals("users")) callUserFragments();
    }

    private void callQuestionFragments() {
        // index
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        questionIndexFragment = new QuestionIndexFragment();
        fragmentTransaction.replace(R.id.indexFrame, questionIndexFragment, "question-index");
        fragmentTransaction.commit();
    }

    private void callUserFragments() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        userIndexFragment = new UserIndexFragment();
        fragmentTransaction.replace(R.id.indexFrame, userIndexFragment, "user-index");
        fragmentTransaction.commit();
    }

    @Override
    public void communicate(HashMap<String, String> data, String fromFragment) {
        switch (fromFragment) {
            case "index-title-bar":
                indexTitleBarFragmentCommunicate(data);
                break;
            case "question-index":
                questionIndexCommunicate(data);
                break;
        }
    }

    @Override
    public void communicate(String event, String fromFragment) {

    }

    private void indexTitleBarFragmentCommunicate(HashMap<String, String> params) {
        if (getController().equals("questions")) {
            if (getIsChangingController()){
                callQuestionFragments();
                setIsChangingController(false);
            }
            QuestionIndexFragment indexFragment = (QuestionIndexFragment) fragmentManager.findFragmentByTag("question-index");

            // if new question level # current => on change
            if (getIsChangingQuestionLevel()) {
                indexFragment.onChangeData(params);
                setIsChangingQuestionLevel(false);
            }
        }else if (getController().equals("users")){
            if (getIsChangingController()){
                callUserFragments();
                setIsChangingController(false);
            }
        }
    }

    private void questionIndexCommunicate(HashMap<String, String> data) {
        Intent i = new Intent(MainActivity.this, FormActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("params", data);
        i.putExtra("package", b);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else super.onBackPressed();
    }

    @Override
    protected void doSomethingOnNetworkChange() {
        if (internetConnectionAvailable()){
            callFragments();
        }
    }

}