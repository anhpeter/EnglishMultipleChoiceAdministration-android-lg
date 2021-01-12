package com.example.multiple_choice;

import androidx.annotation.RequiresApi;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;

import Interfaces.FragmentCommunicate;
import Helpers.Helper;
import MainFragments.AddQuestionDialogFragment;
import MainFragments.QuestionFormFragment;
import MainFragments.QuestionFormTitleBarFragment;
import MainFragments.SecDialogFragment;

public class FormActivity extends Activity implements FragmentCommunicate {

    FragmentManager fragmentManager;
    HashMap<String, String> myParams;
    QuestionFormFragment questionFormFragment;
    QuestionFormTitleBarFragment questionFormTitleBarFragment;

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
        questionFormTitleBarFragment = new QuestionFormTitleBarFragment();
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
        } else if (fromFragment.equals(QuestionFormFragment.fragmentName)) {
            if (event.equals("on-edit-item-callback")) {
                questionFormTitleBarFragment.onEditItemCallback();
            }
        } else if (fromFragment.equals(SecDialogFragment.fragmentName)) {
            if (event.equals("dismiss")) {
                questionFormFragment.onSecDialogDismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!questionFormFragment.isSaving()) {
            // normal
            if (questionFormFragment.isFormChanged()) {
                // form changed
                showQuitDialog();
            } else {
                // form not changed
                onQuit();
            }
        } else {
            // saving
            Toast.makeText(this, "Please wait for saving!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showQuitDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Are you sure?");
        alertDialog.setMessage("Quit without saving?");
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onQuit();
            }
        });
        alertDialog.show();
    }

    private void onQuit() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Helper.hideKeyboard(this);
    }
}