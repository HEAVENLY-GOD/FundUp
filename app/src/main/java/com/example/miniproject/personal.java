package com.example.miniproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class personal extends Fragment {

    private EditText fullName, dob, gender, email, address, displayName, accountCreation;
    private Spinner typeSpinner;
    private Button saveButton;
    private DatabaseReference userRef;
    private FirebaseAuth auth;
    private LottieAnimationView loadingAnimation;
    private String[] options = {"Entrepreneur", "Investor", "Advisor"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        }

        // Initialize UI elements
        fullName = view.findViewById(R.id.full_name);
        dob = view.findViewById(R.id.dob);
        gender = view.findViewById(R.id.gender);
        email = view.findViewById(R.id.email);
        address = view.findViewById(R.id.address);
        displayName = view.findViewById(R.id.display_name);
        accountCreation = view.findViewById(R.id.account_creation);
        typeSpinner = view.findViewById(R.id.typeSpinner);
        saveButton = view.findViewById(R.id.savebutton);
        loadingAnimation = view.findViewById(R.id.ldr);

        // Set up Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Load user data
        loadUserData();

        // Save data on button click
        saveButton.setOnClickListener(v -> saveUserData());

        // Back button functionality
        ImageView backArrow = view.findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void loadUserData() {
        if (userRef != null) {
            // Show Lottie Animation
            loadingAnimation.setVisibility(View.VISIBLE);
            loadingAnimation.playAnimation();

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        fullName.setText(snapshot.child("fullName").getValue(String.class));
                        dob.setText(snapshot.child("dob").getValue(String.class));
                        gender.setText(snapshot.child("gender").getValue(String.class));
                        email.setText(snapshot.child("email").getValue(String.class));
                        address.setText(snapshot.child("address").getValue(String.class));
                        displayName.setText(snapshot.child("displayName").getValue(String.class));

                        // Fetch stored value for Spinner
                        String userType = snapshot.child("userType").getValue(String.class);
                        if (userType != null) {
                            int spinnerPosition = ((ArrayAdapter<String>) typeSpinner.getAdapter()).getPosition(userType);
                            typeSpinner.setSelection(spinnerPosition);
                        }
                    }

                    // Fetch join date from "students" node
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        String userId = user.getUid();
                        DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference("students");

                        studentsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String joinDate = snapshot.child("joinDate").getValue(String.class);
                                        if (joinDate != null) {
                                            accountCreation.setText(joinDate);
                                        } else {
                                            accountCreation.setText("Join date not available");
                                        }
                                        break; // Exit after first match
                                    }
                                } else {
                                    accountCreation.setText("Join date not available");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                accountCreation.setText("Error loading join date");
                            }
                        });
                    }

                    // Hide animation smoothly after a short delay
                    loadingAnimation.postDelayed(() -> {
                        loadingAnimation.pauseAnimation();
                        loadingAnimation.setVisibility(View.GONE);
                    }, 2000);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                    loadingAnimation.setVisibility(View.GONE);
                }
            });
        }
    }

    private void saveUserData() {
        String fullNameText = fullName.getText().toString().trim();
        String dobText = dob.getText().toString().trim();
        String genderText = gender.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String addressText = address.getText().toString().trim();
        String displayNameText = displayName.getText().toString().trim();
        String userType = typeSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(fullNameText) || TextUtils.isEmpty(dobText) ||
                TextUtils.isEmpty(genderText) || TextUtils.isEmpty(emailText) ||
                TextUtils.isEmpty(addressText) || TextUtils.isEmpty(displayNameText)) {
            Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingAnimation.setVisibility(View.VISIBLE);
        loadingAnimation.playAnimation();

        if (userRef != null) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("fullName", fullNameText);
            userData.put("dob", dobText);
            userData.put("gender", genderText);
            userData.put("email", emailText);
            userData.put("address", addressText);
            userData.put("displayName", displayNameText);
            userData.put("userType", userType);

            userRef.setValue(userData).addOnCompleteListener(task -> {
                loadingAnimation.pauseAnimation();
                loadingAnimation.setVisibility(View.GONE);

                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to save data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}