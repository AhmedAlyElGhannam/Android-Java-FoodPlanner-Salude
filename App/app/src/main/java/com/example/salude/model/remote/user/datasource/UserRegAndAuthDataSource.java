package com.example.salude.model.remote.user.datasource;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.salude.contracts.LoginContract;
import com.example.salude.contracts.RegistrationContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserRegAndAuthDataSource {
    private static UserRegAndAuthDataSource repo;
    FirebaseAuth mAuth;
    private UserRegAndAuthDataSource() {
        mAuth = FirebaseAuth.getInstance();
    }
    public static synchronized UserRegAndAuthDataSource getInstance() {
        if (repo == null) {
            repo = new UserRegAndAuthDataSource();
        }

        return repo;
    }

    public FirebaseUser getCurrentUser() {
        FirebaseUser user = null;
        if (mAuth != null) {
            // get user id
            user = mAuth.getCurrentUser();
        }
        return user;
    }

    public void registerUser(
            String name,
            String email,
            String password,
            RegistrationContract.OnRegistrationFinishedListener listener) {
        // this calls firebase operation to create a user with the given email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                // this part adds an onCompleteListener to the operation which will say if operation was a success or not
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // success => set updates to Firebase user profile with name and photo
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(getCurrentUser().getPhotoUrl())
                                    .build();
                            // if user exists => set the above updates
                            if (getCurrentUser() != null) {
                                getCurrentUser().updateProfile(profileUpdates)
                                        // once updates are done successfully => inform presenter
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                   @Override
                                                                   public void onComplete(@NonNull Task<Void> task) {
                                                                       if (task.isSuccessful()) {
                                                                           listener.OnRegistrationSuccess();
                                                                       }
                                                                       else {
                                                                           listener.OnRegistrationFailure("Failed to register!");
                                                                           Log.i("TAG", "onComplete: registerUser - updateProfile - addOnCompleteListener ERROR");
                                                                       }
                                                                   }
                                                               });
                            }
                        }
                        else {
                            listener.OnRegistrationFailure("Failed to register!");
                            Log.i("TAG", "onComplete: registerUser - createUserWithEmailAndPassword - addOnCompleteListener ERROR");
                        }
                    }
                });
    }

    public void userAccountLogin(String email, String password, LoginContract.OnLoginFinishedListener listener) {
        // ask to sign in using the given email and password
        mAuth.signInWithEmailAndPassword(email, password)
                // this part adds an onCompleteListener to the operation which will say if operation was a success or not
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listener.OnLoginSuccess();
                        }
                        else {
                            listener.OnLoginFailure("Failed to login!");
                            Log.i("TAG", "onComplete: userAccountLogin - signInWithEmailAndPassword - addOnCompleteListener ERROR");
                        }
                    }
                });
    }

    public void userGoogleLogin(String idToken, LoginContract.OnLoginFinishedListener listener) {

        // create a credentials object to allow login using google
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        // use the credentials to sign in
        mAuth.signInWithCredential(credential)
                // this part adds an onCompleteListener to the operation which will say if operation was a success or not
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // set res to true
                            listener.OnLoginSuccess();
                        } else {
                            Log.i("TAG", "onComplete: userGoogleLogin - signInWithCredential - addOnCompleteListener ERROR");
                            listener.OnLoginFailure("ERROR! while logging in using Google");
                        }
                    }
                });
    }
}
