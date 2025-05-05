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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salude.R;
import com.example.salude.contracts.LoginContract;
import com.example.salude.features.auth_firebase.login.presenter.LoginAuthFirebasePresenter;
import com.example.salude.features.auth_firebase.register.view.RegisterAuthFirebaseActivity;
import com.example.salude.features.main_screen.view.MainScreenActivity;
import com.example.salude.model.remote.user.datasource.UserRegAndAuthDataSource;
import com.example.salude.model.repository.SaludRepository;
import com.example.salude.utils.guest.GuestMode;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAuthFirebaseActivity extends AppCompatActivity implements LoginContract.View {
    TextInputEditText editTextMail;
    TextInputEditText editTextPassword;
    Button googleBtn;
    Button loginBtn;
    Button guestBtn;
    TextView registerTxt;
    TextView forgotPassTxt;
    ProgressBar progressBar;
    LoginAuthFirebasePresenter presenter;
    FirebaseAuth mAuth;

    // defining a request code for sign in operation (used with startActivityWithResult)
    private static final int RC_SIGN_IN = 9001;
    // object of google's sign in client
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflate login screen xml layout
        setContentView(R.layout.login_screen);

        // get an instance of firebase authenticator
        mAuth = FirebaseAuth.getInstance();

        // sign in with google shenanigans
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // found in google-services.json
                .requestEmail()
                .build();

        // get google client instance
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // create an object of login presenter (reference to view + repo instance + reference to google signin client)
        presenter = new LoginAuthFirebasePresenter(this, SaludRepository.getInstance(this), mGoogleSignInClient);


        // get references to UI elements by id
        editTextMail = findViewById(R.id.inputEmail);
        editTextPassword = findViewById(R.id.inputPassword);
        googleBtn = findViewById(R.id.buttonGoogle);
        loginBtn = findViewById(R.id.buttonLogin);
        guestBtn = findViewById(R.id.buttonGuest);
        registerTxt = findViewById(R.id.registerTxt);
        forgotPassTxt = findViewById(R.id.forgotPassTxt);
        progressBar = findViewById(R.id.progressBar2);


        // I WILL NOT HANDLE YOU!
        forgotPassTxt.setVisibility(View.INVISIBLE);

        // loginBtn click handler
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get email and check it is not empty
                String email = String.valueOf(editTextMail.getText());
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginAuthFirebaseActivity.this, "Please enter your email.", Toast.LENGTH_LONG).show();
                    return;
                }
                // get password and check it is not empty
                String password = String.valueOf(editTextPassword.getText());
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginAuthFirebaseActivity.this, "Please enter a password.", Toast.LENGTH_LONG).show();
                    return;
                }

                // call presenter to handle login operation
                presenter.initiateUserAccountLogin(email, password);
            }
        });

        // googleBtn click listener
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        // guest mode click listener
        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuestMode.setGuestModeState(true);
                Intent intent = new Intent(LoginAuthFirebaseActivity.this, MainScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // registerTxt click listener
        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to registration activity
                Intent intent = new Intent(LoginAuthFirebaseActivity.this, RegisterAuthFirebaseActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // for started activity with code RC_SIGN_IN aka google sign in
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //
                GoogleSignInAccount account = task.getResult(ApiException.class);
                presenter.initiateGoogleLogin(account.getIdToken());
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }
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
        hideProgress();
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onErrorUIAction(String msg) {
        Toast.makeText(this, "Failed to login. Try again later.", Toast.LENGTH_SHORT).show();
    }
}
