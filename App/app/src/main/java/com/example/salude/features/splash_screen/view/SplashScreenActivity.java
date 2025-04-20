package com.example.salude.features.splash_screen.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.salude.MainActivity;
import com.example.salude.R;
import com.example.salude.features.auth_firebase.login.view.LoginAuthFirebaseActivity;
import com.google.firebase.FirebaseApp;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // initialize firebase
        FirebaseApp.initializeApp(this);

        // creating an object of lottie animation
        LottieAnimationView animationView = findViewById(R.id.splashAnimation);

        // handler for action after animation ends
        animationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // start main activity (change its name later)
                Intent intent = new Intent(SplashScreenActivity.this, LoginAuthFirebaseActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
