package com.example.salude.features.auth_firebase.login.presenter;

import com.example.salude.contracts.LoginContract;
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
        if (repo != null) {
            // needs more logic
//            repo.userGoogleLogin("", this);
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
