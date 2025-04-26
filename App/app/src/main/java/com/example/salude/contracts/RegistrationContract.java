package com.example.salude.contracts;

public interface RegistrationContract {
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
        void callRegisterModelAction(String name, String email, String password);
    }

    interface Model {
        // caller: presenter, callee: model
        void registerUser(String name, String email, String password, OnRegistrationFinishedListener listener); // call presenter stuff
    }

    interface OnRegistrationFinishedListener {
        // caller: model, callee: presenter
        public void OnRegistrationSuccess();
        // caller: model, callee: presenter
        public void OnRegistrationFailure(String err);
    }
}
