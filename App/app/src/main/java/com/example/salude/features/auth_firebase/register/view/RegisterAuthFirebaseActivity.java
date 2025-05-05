package com.example.salude.features.auth_firebase.register.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salude.R;
import com.example.salude.contracts.RegistrationContract;
import com.example.salude.features.auth_firebase.register.presenter.RegisterAuthFirebasePresenter;
import com.example.salude.features.main_screen.view.MainScreenActivity;
import com.example.salude.model.repository.SaludRepository;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterAuthFirebaseActivity extends AppCompatActivity implements RegistrationContract.View {
    TextInputEditText editTextName;
    TextInputEditText editTextMail;
    TextInputEditText editTextPassword;
    TextInputEditText editTextConfirmPassword;
    Button registerBtn;
    ProgressBar progressBar;
    RegisterAuthFirebasePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflate registration screen xml layout
        setContentView(R.layout.registeration_screen);

        // create an object of register presenter
        presenter = new RegisterAuthFirebasePresenter(this, SaludRepository.getInstance(this));

        // get references to UI elements by id
        registerBtn = findViewById(R.id.registerBtn);
        editTextMail = findViewById(R.id.inputEmail);
        editTextPassword = findViewById(R.id.inputPassword);
        editTextConfirmPassword = findViewById(R.id.inputConfirmPassword);
        progressBar = findViewById(R.id.progressBar);
        editTextName = findViewById(R.id.inputUsername);

        // registerBtn click handler
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(editTextName.getText());
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(RegisterAuthFirebaseActivity.this, "Please enter your username.", Toast.LENGTH_LONG).show();
                    return;
                }

                String email = String.valueOf(editTextMail.getText());
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterAuthFirebaseActivity.this, "Please enter your email.", Toast.LENGTH_LONG).show();
                    return;
                }

                String password = String.valueOf(editTextPassword.getText());
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterAuthFirebaseActivity.this, "Please enter a password.", Toast.LENGTH_LONG).show();
                    return;
                }

                String confirmedPassword = String.valueOf(editTextConfirmPassword.getText());
                if (TextUtils.isEmpty(confirmedPassword)) {
                    Toast.makeText(RegisterAuthFirebaseActivity.this, "Please enter re-enter your password.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (password.equals(confirmedPassword)) {
                    presenter.callRegisterModelAction(name, email, password);
                }
                else {
                    Toast.makeText(RegisterAuthFirebaseActivity.this, "Re-entered password does not match the original. Try again.", Toast.LENGTH_LONG).show();
                }

                return;
            }
        });
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
        // return to login page
        Intent intent = new Intent(RegisterAuthFirebaseActivity.this, MainScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onErrorUIAction(String msg) {
        Toast.makeText(this, "Failed to register. Try again later.", Toast.LENGTH_SHORT).show();
    }
}
