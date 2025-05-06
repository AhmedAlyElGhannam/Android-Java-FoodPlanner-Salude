package com.example.salude.contracts;

import com.google.firebase.auth.FirebaseUser;

public interface ProfileScreenContract {
    public interface View {
        public void showUserProfilePhoto(String uri);
    }
    public interface Presenter {
        public String getUserName();
        public void userSignOut();
        public void showUserProfilePhoto();
    }
    public interface Model {
        public FirebaseUser getCurrentUser();
        public void userSignOut();
    }
}
