package com.example.miniproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.miniproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class settings extends Fragment {

    private Button buttongoku;
    private SharedPreferences sharedPreferences;
    private EditText usname;  // EditText to display first and last name
    private EditText joined;  // EditText to display the join date
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

        // Get references to the EditTexts for username and joined date
        usname = rootView.findViewById(R.id.usname);
        joined = rootView.findViewById(R.id.Joined);

        // Fetch and display the student's first name, last name, and join date
        fetchUserDetails();

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

    private void fetchUserDetails() {
        // Ensure that the user is authenticated
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.e(TAG, "User is not authenticated.");
            return;
        }

        String userId = user.getUid();  // Get current user's UID
        Log.d(TAG, "Current User ID: " + userId);  // Log user ID for debugging

        // Get reference to the 'students' node
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("students");

        // Search for the current user using their userId in the students node
        usersRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Log the entire data snapshot for debugging
                    Log.d(TAG, "DataSnapshot exists: " + dataSnapshot.getValue());

                    // Assuming the data is a single user record, get the first child
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            // Combine first name and last name to display
                            String fullName = user.getFirstName() + " " + user.getLastName();
                            usname.setText(fullName);  // Display full name in the EditText
                            Log.d(TAG, "User found: " + fullName);  // Log the fetched name

                            // Set the joined date in the 'Joined' EditText
                            String joinDate = user.getJoinDate();  // Assuming joinDate is a string
                            joined.setText(joinDate);  // Display join date in the EditText
                            Log.d(TAG, "Join date: " + joinDate);  // Log the fetched join date
                        } else {
                            usname.setText("User data is null");
                            joined.setText("Join date not available");
                            Log.e(TAG, "User data is null for the UID: " + userId);
                        }
                    }
                } else {
                    Log.e(TAG, "User data does not exist in Firebase for UID: " + userId);
                    usname.setText("User data not available");
                    joined.setText("Join date not available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read user data: " + databaseError.getMessage());
                usname.setText("Error loading user data");
                joined.setText("Error loading join date");
            }
        });
    }
}
