package com.example.salude.features.main_screen.fragments.connection;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.salude.R;
import com.example.salude.features.auth_firebase.login.view.LoginAuthFirebaseActivity;
import com.example.salude.features.main_screen.view.MainScreenActivity;
import com.example.salude.features.splash_screen.view.SplashScreenActivity;

public class ConnectionLostFragment extends Fragment {

    private LottieAnimationView animationView;
    private Button btnRetry;
    private Button btnContinueOffline;
    private boolean isCheckingConnection = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.connection_lost, container, false);

        animationView = view.findViewById(R.id.connectionLostAnimation);
        btnRetry = view.findViewById(R.id.btnRetry);
        btnContinueOffline = view.findViewById(R.id.btnContinueOffline);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnRetry.setOnClickListener(v -> {
            if (isCheckingConnection) return;
            isCheckingConnection = true;

            // Show checking connection state
            Toast.makeText(getContext(), "Checking connection...", Toast.LENGTH_SHORT).show();

            // Check connection status
            if (isNetworkAvailable()) {
                // Connection restored - show animation
                ((MainScreenActivity) requireActivity()).showConnectionRestoredAnimation();
            } else {
                // Still no connection
                Toast.makeText(getContext(), "Still offline. Please check your connection.", Toast.LENGTH_SHORT).show();
                isCheckingConnection = false;
            }
        });

        btnContinueOffline.setOnClickListener(v -> {
            // Return to home screen with limited functionality
            ((MainScreenActivity) requireActivity()).continueOffline();
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
