package com.example.wheatyieldpredictionproject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private ActionBarDrawerToggle toggle;
    private TextView welcomeText;
    private static final int PROFILE_UPDATE_REQUEST = 1001;
// Welcome text in the navigation header

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize components with correct IDs
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set Toolbar
        setSupportActionBar(toolbar);

        // Drawer Toggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Access Navigation Header View
        View headerView = navigationView.getHeaderView(0);
        welcomeText = headerView.findViewById(R.id.welcome_text);

        // Get username from LoginActivity
        Intent intent = getIntent();
        String userName = intent.getStringExtra("USERNAME");
        String email=intent.getStringExtra("EMAIL");

        // Set dynamic welcome message in Navigation Header
        if (userName != null && !userName.isEmpty()) {
            welcomeText.setText("Welcome " + userName+"email"+email);
        } else {
            welcomeText.setText("Welcome User"); // Default fallback
        }

        // Handle Navigation Drawer Clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.nav_farm_connect) {
                    selectedFragment = new FarmConnectFragment();
                } else if (item.getItemId() == R.id.nav_cropinfo) {
                    selectedFragment = new CropInfoFragment();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Handle Bottom Navigation Clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Log.d("BottomNav", "Clicked: " + item.getTitle()); // Debugging Log

            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.bottom_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.bottom_reports) {
                selectedFragment = new ReportsFragment();
            } else if (item.getItemId() == R.id.bottom_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }

            return false;
        });



        // Load Default Fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    // Method to load fragments
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private BroadcastReceiver navHeaderUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String imageUri = intent.getStringExtra("PROFILE_IMAGE_URI");
            if (imageUri != null) {
                ImageView navProfileImage = findViewById(R.id.nav_profile_image);
                navProfileImage.setImageURI(Uri.parse(imageUri));
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(navHeaderUpdateReceiver, new IntentFilter("UPDATE_NAV_HEADER"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(navHeaderUpdateReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_UPDATE_REQUEST && resultCode == RESULT_OK && data != null) {
            String updatedImageUri = data.getStringExtra("UPDATED_IMAGE");
            if (updatedImageUri != null) {
                ImageView navProfileImage = findViewById(R.id.nav_profile_image);
                navProfileImage.setImageURI(Uri.parse(updatedImageUri));
            }
        }
    }

}
