package com.example.wheatyieldpredictionproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmailInput, loginPasswordInput;
    Button loginButton;
    TextView signupText;
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmailInput = findViewById(R.id.login_email);
        loginPasswordInput = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupText = findViewById(R.id.signup_text);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        loginButton.setOnClickListener(v -> {
            String inputEmail = loginEmailInput.getText().toString().trim();
            String inputPassword = loginPasswordInput.getText().toString().trim();

            if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(inputEmail, inputPassword);
        });

        signupText.setOnClickListener(v -> {
            // Navigate to SignupActivity
            Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(signupIntent);
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Successfully logged in
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            // Save user email to SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("EMAIL", user.getEmail());
                            editor.apply();

                            // Navigate to HomeActivity
                            Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(homeIntent);
                            finish();
                        }
                    } else {
                        // Authentication failed
                        Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                        Log.e("LoginError", "Error: " + task.getException().getMessage());
                    }
                });
    }
}
