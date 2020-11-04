package com.example.multiple_choice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Defines.Auth;
import Defines.FragmentCommunicate;
import Defines.User;
import Helpers.Helper;
import MainFragments.IndexTitleBarFragment;
import MainFragments.QuestionFormFragment;
import MainFragments.QuestionIndexFragment;

public class MainActivity extends AppCompatActivity implements FragmentCommunicate {

    FragmentManager fragmentManager;
    String controller;
    String questionLevel;
    IndexTitleBarFragment indexTitleBarFragment;
    QuestionIndexFragment questionIndexFragment;
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            SharedPreferences sharedPreferences = getSharedPreferences("global-package", Context.MODE_PRIVATE);
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
        controller = "questions";
        questionLevel = "easy";
        fragmentManager = getFragmentManager();
        Helper.initFontAwesome(getApplicationContext(), getWindow().getDecorView());
    }

    private void callFragments() {
        if (controller.equals("questions")) callQuestionFragments();
        else if (controller.equals("users")) callUserFragments();
    }

    private void callQuestionFragments() {
        Bundle b = new Bundle();
        HashMap<String, String> params = new HashMap<>();
        params.put("controller", controller);
        params.put("questionLevel", questionLevel);
        b.putSerializable("params", params);

        // title bar
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        indexTitleBarFragment = new IndexTitleBarFragment();
        indexTitleBarFragment.setArguments(b);
        fragmentTransaction.add(R.id.indexTitleBarFrame, indexTitleBarFragment, "titleBar");

        // index
        questionIndexFragment = new QuestionIndexFragment();
        questionIndexFragment.setArguments(b);
        fragmentTransaction.add(R.id.indexFrame, questionIndexFragment, "index");
        fragmentTransaction.commit();

    }

    private void callUserFragments() {
    }

    @Override
    public void communicate(HashMap<String, String> data, String fromFragment) {
        switch (fromFragment) {
            case "question-index-title-bar":
                questionIndexTitleBarFragmentCommunicate(data);
                break;
            case "question-index":
                questionIndexCommunicate(data);
                break;
        }
    }

    private void questionIndexTitleBarFragmentCommunicate(HashMap<String, String> data) {
        controller = data.get("controller");
        if (controller == "questions") {
            String newQuestionLevel = data.get("questionLevel");
            QuestionIndexFragment indexFragment = (QuestionIndexFragment) fragmentManager.findFragmentByTag("index");

            // if new question level # current => on change
            if (!questionLevel.equals(newQuestionLevel)) {
                indexFragment.onChangeData(data);
                questionLevel = newQuestionLevel;
            }
        }
    }

    private void questionIndexCommunicate(HashMap<String, String> data) {
        Intent i = new Intent(MainActivity.this, FormActivity.class);
        Bundle b = new Bundle();
        data.put("controller", controller);
        b.putSerializable("params", data);
        i.putExtra("package", b);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        questionIndexFragment.onListAll();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else super.onBackPressed();
    }
}