package com.example.multiple_choice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.rpc.Help;

import java.util.ArrayList;
import java.util.HashMap;

import Defines.FragmentCommunicate;
import Defines.ICallback;
import Defines.Question;
import Helpers.Helper;
import MainFragments.AddQuestionDialogFragment;
import MainFragments.IndexTitleBarFragment;
import MainFragments.QuestionFormFragment;
import MainFragments.QuestionFormTitleBarFragment;
import MainFragments.QuestionIndexFragment;

public class FormActivity extends Activity implements FragmentCommunicate {

    FragmentManager fragmentManager;
    HashMap<String, String> myParams;
    QuestionFormFragment questionFormFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        myParams = new HashMap<>();
        fragmentManager = getFragmentManager();
        Bundle b = getIntent().getBundleExtra("package");
        if (b != null) {
            // form params
            HashMap<String, String> params = (HashMap<String, String>) b.getSerializable("params");
            myParams = new HashMap<>();
            myParams.putAll(params);
            callFragments();
        } else solveInputError();

    }

    private void solveInputError() {
        finish();
    }

    private void callFragments() {
        if (getController().equals("questions")) callQuestionFragments();
        else if (getController().equals("users")) callUserFragments();
    }

    private void callQuestionFragments() {
        Bundle b = new Bundle();
        b.putSerializable("params", myParams);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // title bar
        QuestionFormTitleBarFragment questionFormTitleBarFragment = new QuestionFormTitleBarFragment();
        questionFormTitleBarFragment.setArguments(b);
        fragmentTransaction.add(R.id.formTitleBarFrame, questionFormTitleBarFragment, "titleBar");

        // form
        questionFormFragment = new QuestionFormFragment();
        questionFormFragment.setArguments(b);
        fragmentTransaction.add(R.id.formFrame, questionFormFragment, "form");
        fragmentTransaction.commit();
    }

    private void callUserFragments() {
    }

    @Override
    public void communicate(HashMap<String, String> data, String fromFragment) {
        if (fromFragment.equals("question-form-title-bar")) {
            String delete = (data.get("delete") != null) ? data.get("delete") : "";
            if (delete.equals("yes")) questionFormFragment.onDelete();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void communicate(String event, String fromFragment) {
        if (fromFragment.equals(AddQuestionDialogFragment.fragmentName)) {
            if (event.equals("dismiss")) {
                questionFormFragment.onAddQuestionDialogDismiss();
            } else if (event.equals("on-picture-type-click")) {
                questionFormFragment.onPickQuestionPicture();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Helper.hideKeyboard(this);
    }
}