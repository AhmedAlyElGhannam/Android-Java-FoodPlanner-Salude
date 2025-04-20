package com.example.salude.contracts;

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
        void callLoginModelAction(String email, String password);
        void callLoginWithGoogleModelAction();

    }

    interface Model {
        // caller: presenter, callee: model
        void userAccountLogin(String email, String password, RegistrationContract.OnRegistrationFinishedListener listener); // call presenter stuff
        void userGoogleLogin(RegistrationContract.OnRegistrationFinishedListener listener);
    }

    interface OnLoginFinishedListener {
        // caller: model, callee: presenter
        public void OnLoginSuccess();
        // caller: model, callee: presenter
        public void OnLoginFailure(String err);
    }
}
