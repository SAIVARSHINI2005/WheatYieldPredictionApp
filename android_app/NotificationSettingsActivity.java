package com.example.wheatyieldpredictionproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationSettingsActivity extends AppCompatActivity {

    private Switch switchNotifications;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "NotificationPrefs";
    private static final String NOTIF_KEY = "notifications_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        switchNotifications = findViewById(R.id.switchNotifications);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load saved notification preference
        boolean isNotificationsEnabled = sharedPreferences.getBoolean(NOTIF_KEY, true);
        switchNotifications.setChecked(isNotificationsEnabled);

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(NOTIF_KEY, isChecked);
            editor.apply();
            Toast.makeText(NotificationSettingsActivity.this, isChecked ? "Notifications Enabled" : "Notifications Disabled", Toast.LENGTH_SHORT).show();
        });
    }
}
