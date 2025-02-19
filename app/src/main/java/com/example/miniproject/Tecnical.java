package com.example.miniproject;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Tecnical extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnical);

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);

        // Set listener for navigation item selection
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();

                // Switch between fragments depending on item selected
                if (id == R.id.home) {
                    selectedFragment = new Home();
                } else if (id == R.id.msg) {
                    selectedFragment = new Grow();
                } else if (id == R.id.profile) {
                    selectedFragment = new Fund();
                } else if (id == R.id.logout) {
                    selectedFragment = new settings();
                }

                // Add custom animations for fragment transitions
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                // Return true to let the BottomNavigationView handle the state change
                return true;
            }
        });

        // Set default fragment when the activity starts
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.home);  // Set default fragment selection
        }
    }
}
