package com.example.multiple_choice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Defines.Auth;

public class LoginActivity extends Activity {

    EditText edtUsername, edtPassword;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();
        onBtnSubmitClicked();
    }

    private void onBtnSubmitClicked() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                if (isFormValid(username, password)) {
                    Auth auth = Auth.getInstance();
                    auth.loginByUsernameAndPassword(username, password);
                    if (auth.isLogged()) {
                        SharedPreferences sharedPreferences = getSharedParams();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loggedUsername", username);
                        editor.apply();
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "User is not exist!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please type username and password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isFormValid(String username, String password) {
        return (!username.equals("") && !password.equals(""));
    }

    private void mapping() {
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }
}