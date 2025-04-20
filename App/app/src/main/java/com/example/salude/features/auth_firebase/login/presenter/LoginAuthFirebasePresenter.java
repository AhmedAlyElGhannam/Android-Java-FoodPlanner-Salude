package com.example.salude.features.auth_firebase.login.presenter;

import com.example.salude.contracts.LoginContract;

public class LoginAuthFirebasePresenter implements LoginContract.Presenter, LoginContract.OnLoginFinishedListener {
    LoginContract.View view;
    LoginContract.Model model;

    public LoginAuthFirebasePresenter(LoginContract.View _view, LoginContract.Model _model) {
        view = _view;
        model = _model;
    }

    @Override
    public void callLoginModelAction(String email, String password) {

    }

    @Override
    public void callLoginWithGoogleModelAction() {

    }

    @Override
    public void OnLoginSuccess() {

    }

    @Override
    public void OnLoginFailure(String err) {

    }
}
