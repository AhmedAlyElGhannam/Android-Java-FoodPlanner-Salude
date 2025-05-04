package com.example.salude.features.main_screen.fragments.connection;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.salude.R;
import com.example.salude.features.auth_firebase.login.view.LoginAuthFirebaseActivity;
import com.example.salude.features.splash_screen.view.SplashScreenActivity;
import com.example.salude.utils.clicklistener.OnConnectionRestoredListener;

public class ConnectionRestoredFragment extends Fragment {
    LottieAnimationView animationView;
    private final OnConnectionRestoredListener connection_listener;

    public ConnectionRestoredFragment(OnConnectionRestoredListener listener) {
        connection_listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.connection_restored, container, false);

        animationView = view.findViewById(R.id.connectionRestoredAnimation);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // handler for action after animation ends
        animationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // call action
                connection_listener.onConnectionRestored();
            }
        });
    }

}
