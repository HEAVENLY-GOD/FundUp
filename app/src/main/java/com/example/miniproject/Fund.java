package com.example.miniproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fund extends Fragment {

    private LinearLayout cardContainer;
    private DatabaseReference databaseReference;
    private String projectId = "HYON1qS87xfaU6F8R7xwi15LfaE3"; // Update with actual project ID

    public Fund() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fund, container, false);
        cardContainer = view.findViewById(R.id.cardContainer);

        // Fetch and display accepted users
        fetchAcceptedUsers();

        return view;
    }

    private void fetchAcceptedUsers() {
        if (projectId == null) return;

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Projects")
                .child(projectId).child("acceptedUsers");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cardContainer.removeAllViews(); // Clear previous views

                if (snapshot.exists()) {
                    boolean found = false;
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userId = userSnapshot.getKey(); // Fetch user UID
                        String displayName = userSnapshot.getValue(String.class);

                        if (displayName != null && !userId.equals(currentUserId)) { // Exclude the current user
                            addUserCard(displayName);
                            found = true;
                        }
                    }

                    if (!found) {
                        Toast.makeText(getContext(), "No accepted users found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No accepted users found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load accepted users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUserCard(String displayName) {
        // Create CardView
        CardView cardView = new CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(20, 20, 20, 20);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(50);
        cardView.setCardElevation(8f);
        cardView.setCardBackgroundColor(getResources().getColor(android.R.color.black));
        cardView.setAlpha(0.8f);

        // Create Inner LinearLayout (Vertical)
        LinearLayout linearLayout = new LinearLayout(requireContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(30, 30, 30, 30);

        // Create a Horizontal LinearLayout for User Label and Name
        LinearLayout userInfoLayout = new LinearLayout(requireContext());
        userInfoLayout.setOrientation(LinearLayout.HORIZONTAL);
        userInfoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // User Label
        TextView userLabel = new TextView(requireContext());
        userLabel.setText("User: ");
        userLabel.setTextSize(18);
        userLabel.setTextColor(getResources().getColor(android.R.color.white));

        // User Name TextView
        TextView userName = new TextView(requireContext());
        userName.setText(displayName);
        userName.setTextSize(18);
        userName.setTextColor(getResources().getColor(android.R.color.holo_blue_light));

        // Add Label and Name to Horizontal Layout
        userInfoLayout.addView(userLabel);
        userInfoLayout.addView(userName);

        // Create a Horizontal LinearLayout for Buttons
        LinearLayout buttonLayout = new LinearLayout(requireContext());
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Accept Button
        Button acceptButton = new Button(requireContext());
        acceptButton.setText("Accept");
        acceptButton.setTextColor(getResources().getColor(android.R.color.black));
        acceptButton.setBackgroundResource(R.drawable.roundg);
        LinearLayout.LayoutParams acceptParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
        );
        acceptParams.setMargins(10, 25, 10, 0);
        acceptButton.setLayoutParams(acceptParams);

        // Reject Button
        Button rejectButton = new Button(requireContext());
        rejectButton.setText("Reject");
        rejectButton.setTextColor(getResources().getColor(android.R.color.black));
        rejectButton.setBackgroundResource(R.drawable.roundg);
        LinearLayout.LayoutParams rejectParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
        );
        rejectParams.setMargins(10, 25, 10, 0);
        rejectButton.setLayoutParams(rejectParams);

        // Add Buttons to Button Layout
        buttonLayout.addView(acceptButton);
        buttonLayout.addView(rejectButton);

        // Add Views to Main LinearLayout
        linearLayout.addView(userInfoLayout);
        linearLayout.addView(buttonLayout);

        // Add LinearLayout to CardView
        cardView.addView(linearLayout);

        // Add CardView to container
        cardContainer.addView(cardView);
    }
}
