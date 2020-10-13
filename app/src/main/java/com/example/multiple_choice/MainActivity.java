package com.example.multiple_choice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Defines.FragmentCommunicate;
import Defines.User;
import Helpers.Helper;
import MainFragments.IndexTitleBarFragment;
import MainFragments.QuestionIndexFragment;

public class MainActivity extends AppCompatActivity implements FragmentCommunicate {

    FragmentManager fragmentManager;

    String controller;
    String questionLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapping();
        onInit();

        callFragments();
    }

    private void onInit(){
        controller = "questions";
         questionLevel = "easy";
        fragmentManager = getFragmentManager();
        Helper.initFontAwesome(getApplicationContext(), getWindow().getDecorView());
    }

    private void callFragments(){
        if (controller == "questions") callQuestionFragments();
        else if (controller == "users") callUserFragments();
    }

    private void callQuestionFragments(){
        // title bar
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        IndexTitleBarFragment indexTitleBarFragment = new IndexTitleBarFragment();
        Bundle titleBarBundle = new Bundle();
        titleBarBundle.putString("controller", controller);
        titleBarBundle.putString("questionLevel", questionLevel);
        indexTitleBarFragment.setArguments(titleBarBundle);
        fragmentTransaction.add(R.id.indexTitleBarFrame, indexTitleBarFragment, "titleBar");

        // index
        fragmentTransaction.add(R.id.indexFrame, new QuestionIndexFragment(), "index");
        fragmentTransaction.commit();

    }

    private void callUserFragments(){
        Toast.makeText(this, "User Fragments Called", Toast.LENGTH_SHORT).show();
    }

    private void mapping(){
    }

    @Override
    public void communicate(HashMap<String, String> data) {
        controller = data.get("controller");
        if (controller == "questions"){
            questionLevel = data.get("questionLevel");
            QuestionIndexFragment indexFragment = (QuestionIndexFragment) fragmentManager.findFragmentByTag("index");
            indexFragment.onChangeData(data);
        }
        Toast.makeText(this, controller +" - "+questionLevel, Toast.LENGTH_SHORT).show();
    }
}