package com.example.wheatyieldpredictionproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPassword, newPassword, confirmPassword;
    private Button changePasswordButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Use the same SharedPreferences file as LoginActivity
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        changePasswordButton.setOnClickListener(view -> updatePassword());
    }

    private void updatePassword() {
        String storedPassword = sharedPreferences.getString("USER_PASSWORD", ""); // Retrieve stored password
        String oldPass = oldPassword.getText().toString().trim();
        String newPass = newPassword.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(oldPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!oldPass.equals(storedPassword)) {
            Toast.makeText(this, "Incorrect old password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "New passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the updated password in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_PASSWORD", newPass);
        editor.apply();

        Toast.makeText(ChangePasswordActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
