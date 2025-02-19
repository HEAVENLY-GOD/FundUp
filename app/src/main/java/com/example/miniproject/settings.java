package com.example.miniproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class settings extends Fragment {

    private Button buttongoku;
    private SharedPreferences sharedPreferences;
    private static final String TAG = "SettingsFragment";  // For logging

    public settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("AppPrefs", getActivity().MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        // Get reference to the Goku button (Logout button)
        buttongoku = rootView.findViewById(R.id.goku);

        // Set the click listener for the Goku button
        buttongoku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Logout button clicked");

                // Check if the fragment is attached to an Activity
                if (getActivity() != null) {
                    try {
                        // Log out the user using FirebaseAuth
                        FirebaseAuth.getInstance().signOut();

                        // Clear the "Remember Me" state in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("rememberMe", false);  // Set rememberMe to false
                        editor.remove("userEmail");  // Optionally remove the saved email
                        editor.apply();

                        // Log the successful logout
                        Log.d(TAG, "User logged out");

                        // Navigate to the MainActivity (login or home activity)
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

                        // Finish the current activity to prevent going back to the settings screen
                        getActivity().finish();
                    } catch (Exception e) {
                        Log.e(TAG, "Error during logout", e);
                    }
                } else {
                    Log.e(TAG, "Fragment is not attached to an Activity");
                }
            }
        });

        return rootView;
    }
}
