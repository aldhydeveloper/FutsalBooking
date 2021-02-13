package com.ns.quenfutsalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoadingScreen extends AppCompatActivity {

    Handler handler;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        handler = new Handler();
        handler.postDelayed(() -> {

            if (fUser != null) {
                Intent SendToDashboard = new Intent(this, MainActivity.class);
                SendToDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SendToDashboard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(SendToDashboard);
                finish();
            } else {
                Intent dsp = new Intent(this, SplashScreenActivity.class);
                startActivity(dsp);
                finish();
            }
        }, 4000);
    }
}