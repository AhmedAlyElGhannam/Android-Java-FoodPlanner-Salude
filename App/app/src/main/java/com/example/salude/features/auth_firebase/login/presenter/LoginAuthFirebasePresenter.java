package com.example.salude.features.auth_firebase.login.presenter;

import com.example.salude.contracts.LoginContract;
import com.example.salude.features.auth_firebase.login.view.LoginAuthFirebaseActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

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
        if (view != null && repo != null) {
            view.showProgress();
            // Launch Google Sign-In from the View (Activity)
            if (view instanceof LoginAuthFirebaseActivity) {
                ((LoginAuthFirebaseActivity) view).launchGoogleSignIn(googleSignInClient);
            }
        }
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void handleGoogleSignInResult(String idToken) {
        if (repo != null && view != null) {
            view.showProgress();
            repo.userGoogleLogin(idToken, this);
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
