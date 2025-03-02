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
import androidx.fragment.app.FragmentTransaction;

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
    private Button profButton;  // New Button for navigating to 'personal' Fragment
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
        // Get reference to the "Profile" button
        profButton = rootView.findViewById(R.id.prof);  // Assuming ID for the button is 'prof'

        // Get references to the EditTexts for username and joined date
        usname = rootView.findViewById(R.id.usname);
        joined = rootView.findViewById(R.id.Joined);

        // Fetch and display the student's first name, last name, and join date
        fetchUserDetails();

        // Set the click listener for the Goku button (Logout button)
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

        // Set the click listener for the "Profile" button (to go to personal fragment)
        profButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Profile button clicked");

                // Check if the fragment is attached to an Activity
                if (getActivity() != null) {
                    // Create a new instance of the personal fragment
                    personal personalFragment = new personal();

                    // Start a FragmentTransaction to navigate to the personal fragment
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, personalFragment);  // Replace with the container where the fragment is to be displayed
                    transaction.addToBackStack(null);  // Optionally add to back stack to allow navigation back
                    transaction.commit();
                }
            }
        });

        return rootView;
    }

    private void fetchUserDetails() {
        // Ensure the user is authenticated
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.e(TAG, "User is not authenticated.");
            return;
        }

        String userId = user.getUid();  // Get current user's UID
        Log.d(TAG, "Current User ID: " + userId);

        // Reference to 'Users' node for displayName
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Fetch displayName
                    String displayName = snapshot.child("displayName").getValue(String.class);
                    if (displayName != null) {
                        usname.setText(displayName);  // Set displayName in EditText
                        Log.d(TAG, "Display Name: " + displayName);
                    } else {
                        usname.setText("No display name set");
                        Log.e(TAG, "Display Name not found.");
                    }
                } else {
                    usname.setText("User data not available");
                    Log.e(TAG, "User data does not exist in Users for UID: " + userId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read user data: " + databaseError.getMessage());
                usname.setText("Error loading user data");
            }
        });

        // Reference to 'students' node for joinDate
        DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference("students");
        studentsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String joinDate = snapshot.child("joinDate").getValue(String.class);
                        if (joinDate != null) {
                            joined.setText(joinDate);
                            Log.d(TAG, "Join Date: " + joinDate);
                        } else {
                            joined.setText("Join date not available");
                            Log.e(TAG, "Join Date not found.");
                        }
                        break;  // Since we're only expecting one match, exit loop after first result
                    }
                } else {
                    joined.setText("Join date not available");
                    Log.e(TAG, "User data does not exist in students for UID: " + userId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read join date: " + databaseError.getMessage());
                joined.setText("Error loading join date");
            }
        });
    }
}