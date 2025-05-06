package com.example.salude.features.main_screen.fragments.profile.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.salude.contracts.HomeScreenContract;
import com.example.salude.contracts.ProfileScreenContract;
import com.google.firebase.auth.FirebaseUser;

public class ProfileScreenPresenter implements ProfileScreenContract.Presenter {

    private final ProfileScreenContract.View view;
    private final ProfileScreenContract.Model repo;

    public ProfileScreenPresenter(ProfileScreenContract.View _view, ProfileScreenContract.Model _repo) {
        view = _view;
        repo = _repo;
    }

    @Override
    public String getUserName() {
        FirebaseUser user = repo.getCurrentUser();
        String name;
        if (user != null) {
            name = user.getDisplayName();
        }
        else {
            name = "Guest";
        }
        return name;
    }

    @Override
    public void userSignOut() {
        repo.userSignOut();
    }

    @Override
    public void showUserProfilePhoto() {
        if (repo.getCurrentUser().getPhotoUrl() != null) {
            view.showUserProfilePhoto(repo.getCurrentUser().getPhotoUrl().toString());
        } else {
            view.showUserProfilePhoto(null);
        }
    }
}
