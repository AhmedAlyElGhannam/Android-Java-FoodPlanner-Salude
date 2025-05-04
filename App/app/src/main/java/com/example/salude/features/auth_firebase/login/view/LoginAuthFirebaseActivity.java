package com.example.salude.features.auth_firebase.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salude.R;
import com.example.salude.contracts.LoginContract;
import com.example.salude.features.auth_firebase.login.presenter.LoginAuthFirebasePresenter;
import com.example.salude.features.auth_firebase.register.view.RegisterAuthFirebaseActivity;
import com.example.salude.features.main_screen.view.MainScreenActivity;
import com.example.salude.model.local.dao.MealDAO;
import com.example.salude.model.local.dao.RoomLocalDB;
import com.example.salude.model.pojo.Meal;
import com.example.salude.model.remote.firebase.login.LoginAuthRepository;
import com.example.salude.model.remote.firebase.service.FirebaseDataSyncService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class LoginAuthFirebaseActivity extends AppCompatActivity implements LoginContract.View {
    TextInputEditText editTextMail;
    TextInputEditText editTextPassword;
    Button googleBtn;
    Button loginBtn;
    TextView registerTxt;
    TextView forgotPassTxt;
    ProgressBar progressBar;
    LoginAuthFirebasePresenter presenter;
    FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflate login screen xml layout
        setContentView(R.layout.login_screen);

        // create an instance of firebase
        mAuth = FirebaseAuth.getInstance();

        // create an object of login presenter
        presenter = new LoginAuthFirebasePresenter(this, LoginAuthRepository.getInstance());

        // sign in with google shenanigans
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id_google))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(LoginAuthFirebaseActivity.this, options);

        // get references to UI elements by id
        editTextMail = findViewById(R.id.inputEmail);
        editTextPassword = findViewById(R.id.inputPassword);
        googleBtn = findViewById(R.id.buttonGoogle);
        loginBtn = findViewById(R.id.buttonLogin);
        registerTxt = findViewById(R.id.registerTxt);
        forgotPassTxt = findViewById(R.id.forgotPassTxt);
        progressBar = findViewById(R.id.progressBar2);

        // loginBtn click handler
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(editTextMail.getText());
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginAuthFirebaseActivity.this, "Please enter your email.", Toast.LENGTH_LONG).show();
                    return;
                }

                String password = String.valueOf(editTextPassword.getText());
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginAuthFirebaseActivity.this, "Please enter a password.", Toast.LENGTH_LONG).show();
                    return;
                }

                presenter.callLoginModelAction(email, password);
            }
        });

        // googleBtn click handler
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                presenter.callLoginWithGoogleModelAction(googleSignInClient);
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });

        // registerTxt click handler
        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to registration activity
                Intent intent = new Intent(LoginAuthFirebaseActivity.this, RegisterAuthFirebaseActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /*
            forgotPassTxt click handler
            TODO
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser = mAuth.getCurrentUser();
        // if user is already logged in --> go to home page
        if (currUser != null) {
            Intent intent = new Intent(LoginAuthFirebaseActivity.this, MainScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessUIAction() {
        // sync with user data from firebase then switch to main screen
        loadUserDataFromFirebase();

        // proceed to main screen

    }

    @Override
    public void onErrorUIAction(String msg) {
        Toast.makeText(this, "Failed to login. Try again later.", Toast.LENGTH_SHORT).show();
    }



    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    String idToken = signInAccount.getIdToken();
                    // Pass the token to presenter for handling
                    presenter.handleGoogleSignInResult(idToken);
                } catch (ApiException e) {
                    Log.e("GOOGLE_SIGN_IN", "Sign-in failed. Code: " + e.getStatusCode(), e);
                    onErrorUIAction("Google sign-in failed");
                }
            }
        }
    });

    private void loadUserDataFromFirebase() {
        showProgress();

        FirebaseDataSyncService.getInstance().loadUserData()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        updateLocalDatabase(dataSnapshot);
                    } else {
                        proceedToMainScreen();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseLoad", "Failed to load data", e);
                    proceedToMainScreen();
                });
    }

    private void updateLocalDatabase(DataSnapshot dataSnapshot) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                MealDAO.FavouriteMealDAO favDao = RoomLocalDB.getInstance(this).getFavouriteMealDAO();
                MealDAO.PlannedMealDAO plannedDao = RoomLocalDB.getInstance(this).getPlannedMealDAO();

                // Handle favorites
                DataSnapshot favSnapshot = dataSnapshot.child("favorites");
                if (favSnapshot.exists()) {
                    for (DataSnapshot mealSnapshot : favSnapshot.getChildren()) {
                        Meal meal = mealSnapshot.getValue(Meal.class);
                        if (meal != null) {
                            meal.setIsFavouriteMeal(true);
                            favDao.insertFavouriteMeal(meal);

                        }
                    }
                }

                // Handle planned meals
                DataSnapshot plannedSnapshot = dataSnapshot.child("planned");
                if (plannedSnapshot.exists()) {
                    for (DataSnapshot mealSnapshot : plannedSnapshot.getChildren()) {
                        Meal meal = mealSnapshot.getValue(Meal.class);
                        if (meal != null && meal.getPlannedMealDate() != null) {
                            plannedDao.insertPlannedMeal(meal);
                        }
                    }
                }

                runOnUiThread(this::proceedToMainScreen);
            } catch (Exception e) {
                Log.e("LocalDBUpdate", "Error updating local DB", e);
                runOnUiThread(this::proceedToMainScreen);
            }
        });
    }

    private void proceedToMainScreen() {
        hideProgress();
        startActivity(new Intent(this, MainScreenActivity.class));
        finish();
    }

    public void launchGoogleSignIn(GoogleSignInClient googleSignInClient) {
        Intent intent = googleSignInClient.getSignInIntent();
        activityResultLauncher.launch(intent);
    }

    @Override
    public void handleGoogleSignInResult(String idToken) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
