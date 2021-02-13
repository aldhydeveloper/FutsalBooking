package com.ns.quenfutsalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class SplashScreenActivity extends AppCompatActivity {

    Button btn_login, btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        btn_login = findViewById(R.id.btn_login1);
        btn_signup = findViewById(R.id.btn_sign1);

        btn_login.setOnClickListener(v -> {
            Intent SendToLogin = new Intent(this, LoginActivity.class);
            SendToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            SendToLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(SendToLogin);
        });
        btn_signup.setOnClickListener(v -> {
            Intent SendToSignUp = new Intent(this, SignUpActivity.class);
            SendToSignUp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            SendToSignUp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(SendToSignUp);
        });


    }
}