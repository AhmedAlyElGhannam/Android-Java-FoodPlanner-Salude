package com.example.salude.features.auth_firebase.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salude.MainActivity;
import com.example.salude.R;
import com.example.salude.contracts.LoginContract;
import com.example.salude.features.auth_firebase.login.presenter.LoginAuthFirebasePresenter;
import com.example.salude.features.auth_firebase.register.view.RegisterAuthFirebaseActivity;
import com.example.salude.model.authentication.login.LoginAuthRepository;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflate login screen xml layout
        setContentView(R.layout.login_screen);

        // create an instance of firebase
        mAuth = FirebaseAuth.getInstance();

        // create an object of login presenter
        presenter = new LoginAuthFirebasePresenter(this, LoginAuthRepository.getInstance());

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
                presenter.callLoginWithGoogleModelAction();
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
            Intent intent = new Intent(LoginAuthFirebaseActivity.this, MainActivity.class);
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
        // proceed to app home page
        Intent intent = new Intent(LoginAuthFirebaseActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onErrorUIAction(String msg) {
        Toast.makeText(this, "Failed to login. Try again later.", Toast.LENGTH_SHORT).show();
    }
}
