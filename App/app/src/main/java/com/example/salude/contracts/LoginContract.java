package com.example.salude.contracts;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;

public interface LoginContract {
    interface View {
        // methods to show and hide progress bar
        void showProgress();
        void hideProgress();
        // caller: presenter, callee: view
        void onSuccessUIAction();
        // caller: presenter, callee: view
        void onErrorUIAction(String msg);
    }

    interface Presenter {
        // caller: view, callee: presenter
        public void initiateUserAccountLogin(String email, String password); // call presenter stuff
        public void initiateGoogleLogin(String userIdToken);
    }

    interface Model {
        public FirebaseUser getCurrentUser();
        public void userAccountLogin(String email, String password, LoginContract.OnLoginFinishedListener listener);
        public void userGoogleLogin(String idToken, LoginContract.OnLoginFinishedListener listener);

    }

    interface OnLoginFinishedListener {
        // caller: model, callee: presenter
        public void OnLoginSuccess();
        // caller: model, callee: presenter
        public void OnLoginFailure(String err);
    }
}
