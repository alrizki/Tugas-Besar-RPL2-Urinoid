package com.example.adlif.urinoid;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Registrasi extends AppCompatActivity {

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputDisplay;
    private TextInputLayout textInputPassword;

    Button btnsignup;

    @BindView(R.id.signinLink) TextView _signinLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        ButterKnife.bind(this);


        textInputEmail = findViewById(R.id.text_input_email);
        textInputUsername = findViewById(R.id.text_input_username);
        textInputDisplay = findViewById(R.id.text_input_display);
        textInputPassword = findViewById(R.id.text_input_password);
        btnsignup = findViewById(R.id.btnSignup);

        _signinLink.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnsignup.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail() | !validateUsername() | !validateDisplay() | !validatePassword()) {
                    return;
                }
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean validateEmail(){
        String emailInput =  textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Email masih kosong");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validateUsername(){
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()){
            textInputUsername.setError("Username masih kosong");
            return false;
        } else if (usernameInput.length() > 15){
            textInputUsername.setError("Username terlalu panjang");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validateDisplay(){
        String displayInput = textInputDisplay.getEditText().getText().toString().trim();

        if (displayInput.isEmpty()){
            textInputDisplay.setError("Nama masih kosong");
            return false;
        } else if (displayInput.length() > 15){
            textInputDisplay.setError("Nama terlalu panjang");
            return false;
        } else {
            textInputDisplay.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String passwordInput =  textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Password masih kosong");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }
}
