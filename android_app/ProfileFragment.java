package com.example.wheatyieldpredictionproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Buttons
        Button myInfoButton = view.findViewById(R.id.myinfo);
        Button changePasswordButton = view.findViewById(R.id.change_password);
        Button notificationsButton = view.findViewById(R.id.notifications);
        Button logoutButton = view.findViewById(R.id.logout);
        Button deleteAccountButton = view.findViewById(R.id.delete_account);

        // Check if button exists in the layout
        if (myInfoButton == null) {
            Log.e("ProfileFragment", "myinfo button not found in layout");
            return view;
        }

        // Open MyInfoActivity
        myInfoButton.setOnClickListener(v -> {
            Log.d("ProfileFragment", "MyInfo button clicked");
            Intent intent = new Intent(requireActivity(), MyInfoActivity.class);
            startActivity(intent);
        });

        // Change Password
        changePasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        // Notifications
        notificationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), NotificationSettingsActivity.class);
            startActivity(intent);
        });

        // Logout
        logoutButton.setOnClickListener(v -> {
            clearUserSession();
            Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            navigateToLogin();
        });

        // Delete Account
        deleteAccountButton.setOnClickListener(v -> deleteAccount());

        return view;
    }

    private void deleteAccount() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(getActivity(), "Account deleted successfully", Toast.LENGTH_LONG).show();
        navigateToLogin();
    }

    private void clearUserSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_email");
        editor.remove("user_name");
        editor.apply();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
