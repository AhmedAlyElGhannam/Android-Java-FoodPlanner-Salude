package com.example.salude.contracts;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public interface LoginContract {
    interface View {
        // methods to show and hide progress bar
        void showProgress();
        void hideProgress();
        // caller: presenter, callee: view
        void onSuccessUIAction();
        // caller: presenter, callee: view
        void onErrorUIAction(String msg);
        void handleGoogleSignInResult(String idToken); // New method
    }

    interface Presenter {
        // caller: view, callee: presenter
        void callLoginModelAction(String email, String password);
        void callLoginWithGoogleModelAction(GoogleSignInClient googleSignInClient);
        public void detachView();
        public void handleGoogleSignInResult(String idToken);
    }

    interface Model {
        // caller: presenter, callee: model
        void userAccountLogin(String email, String password, LoginContract.OnLoginFinishedListener listener); // call presenter stuff
        void userGoogleLogin(String idToken, LoginContract.OnLoginFinishedListener listener);
    }

    interface OnLoginFinishedListener {
        // caller: model, callee: presenter
        public void OnLoginSuccess();
        // caller: model, callee: presenter
        public void OnLoginFailure(String err);
    }
}
