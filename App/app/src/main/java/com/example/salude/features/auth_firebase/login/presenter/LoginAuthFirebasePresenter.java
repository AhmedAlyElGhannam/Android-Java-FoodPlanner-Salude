package com.example.salude.features.auth_firebase.login.presenter;

import com.example.salude.contracts.LoginContract;
import com.example.salude.model.remote.firebase.FirebaseRepository;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class LoginAuthFirebasePresenter implements LoginContract.Presenter, LoginContract.OnLoginFinishedListener {
    LoginContract.View view;
    FirebaseRepository repo;
    private GoogleSignInClient mGoogleSignInClient;


    public LoginAuthFirebasePresenter(LoginContract.View _view, FirebaseRepository _repo, GoogleSignInClient client) {
        view = _view;
        repo = _repo;
        mGoogleSignInClient = client;
    }

    public void initiateUserAccountLogin(String email, String password) {
        if (repo != null) {
            repo.userAccountLogin(email, password, this);
        }
    }

    public void initiateGoogleLogin(String userIdToken) {
        repo.userGoogleLogin(userIdToken, this);
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
