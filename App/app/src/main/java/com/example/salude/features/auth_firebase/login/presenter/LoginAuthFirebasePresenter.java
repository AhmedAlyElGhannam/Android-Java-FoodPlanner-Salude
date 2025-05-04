package com.example.salude.features.auth_firebase.login.presenter;

import android.content.Intent;

import com.example.salude.contracts.LoginContract;
import com.example.salude.features.auth_firebase.login.view.LoginAuthFirebaseActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginAuthFirebasePresenter implements LoginContract.Presenter, LoginContract.OnLoginFinishedListener {
    LoginContract.View view;
    LoginContract.Model repo;

    public LoginAuthFirebasePresenter(LoginContract.View _view, LoginContract.Model _repo) {
        view = _view;
        repo = _repo;
    }

    @Override
    public void callLoginModelAction(String email, String password) {
        if (repo != null) {
            repo.userAccountLogin(email, password, this);
        }
    }

    @Override
    public void callLoginWithGoogleModelAction(GoogleSignInClient googleSignInClient) {
        if (repo != null && view != null) {
            view.showProgress();
            Intent signInIntent = googleSignInClient.getSignInIntent();
            ((LoginAuthFirebaseActivity) view).startGoogleSignIn(signInIntent);
        }
    }

    public void handleGoogleSignInResult(Intent data) {
        try {
            if (data == null) {
                OnLoginFailure("No sign-in data received");
                return;
            }

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);

            if (account != null && account.getIdToken() != null) {
                repo.userGoogleLogin(account.getIdToken(), this);
            } else {
                OnLoginFailure("Invalid Google account credentials");
            }
        } catch (ApiException e) {
            OnLoginFailure("Google sign-in error: " + e.getStatusCode());
        }
    }

    @Override
    public void OnLoginSuccess() {
        if (view != null) {
            view.onSuccessUIAction();
        }
    }

    @Override
    public void OnLoginFailure(String err) {
        if (view != null) {
            view.onErrorUIAction("");
        }
    }
}
