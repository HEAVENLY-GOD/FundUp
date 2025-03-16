package com.example.miniproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class detail extends Fragment {

    private EditText topic, founder1, founder2, category, problemStatement, solutionValue, targetMarket, investmentAsk, businessModel, contactEmail, contactPhone;
    private Button notifierButton;
    private DatabaseReference databaseReference;
    private String projectId;
    private FirebaseUser currentUser;

    public static detail newInstance(String projectId) {
        detail fragment = new detail();
        Bundle args = new Bundle();
        args.putString("ProjectId", projectId);
        fragment.setArguments(args);
        return fragment;
    }

    public detail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Initialize UI elements
        topic = view.findViewById(R.id.topic);
        founder1 = view.findViewById(R.id.t0);
        founder2 = view.findViewById(R.id.t4);
        category = view.findViewById(R.id.t5);
        problemStatement = view.findViewById(R.id.t6);
        solutionValue = view.findViewById(R.id.t7);
        targetMarket = view.findViewById(R.id.t8);
        investmentAsk = view.findViewById(R.id.t9);
        businessModel = view.findViewById(R.id.t10);
        contactEmail = view.findViewById(R.id.t11);
        contactPhone = view.findViewById(R.id.t12);
        notifierButton = view.findViewById(R.id.rejectButton);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (getArguments() != null) {
            projectId = getArguments().getString("ProjectId");
            databaseReference = FirebaseDatabase.getInstance().getReference("Projects").child(projectId);

            fetchProjectDetails();
            checkOwnerAndDisableButton();
        } else {
            Toast.makeText(getContext(), "Error: No project data provided", Toast.LENGTH_SHORT).show();
            return view;
        }

        notifierButton.setOnClickListener(v -> increaseNotifierCount());

        return view;
    }

    private void fetchProjectDetails() {
        if (projectId == null) return;

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(getContext(), "No project data found", Toast.LENGTH_SHORT).show();
                    return;
                }

                topic.setText(snapshot.child("StartupName").getValue(String.class));
                founder1.setText(snapshot.child("Founder1").getValue(String.class));
                founder2.setText(snapshot.child("Founder2").getValue(String.class));
                category.setText(snapshot.child("Category").getValue(String.class));
                problemStatement.setText(snapshot.child("ProblemStatement").getValue(String.class));
                solutionValue.setText(snapshot.child("SolutionValue").getValue(String.class));
                targetMarket.setText(snapshot.child("TargetMarket").getValue(String.class));
                investmentAsk.setText(snapshot.child("InvestmentAsk").getValue(String.class));
                businessModel.setText(snapshot.child("BusinessModel").getValue(String.class));
                contactEmail.setText(snapshot.child("ContactEmail").getValue(String.class));
                contactPhone.setText(snapshot.child("ContactPhone").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkOwnerAndDisableButton() {
        if (currentUser != null && currentUser.getUid().equals(projectId)) {
            notifierButton.setEnabled(false);
            Toast.makeText(getContext(), "Owners cannot press this button!", Toast.LENGTH_SHORT).show();
        }
    }

    private void increaseNotifierCount() {
        if (projectId == null || currentUser == null) return;

        String userId = currentUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("displayName");
        DatabaseReference notifierRef = databaseReference.child("notifier");
        DatabaseReference notifierUsersRef = databaseReference.child("notifierUsers").child(userId);
        DatabaseReference acceptedUsersRef = databaseReference.child("acceptedUsers");

        acceptedUsersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(getContext(), "You have already accepted this project!", Toast.LENGTH_SHORT).show();
                } else {
                    notifierRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long currentCount = snapshot.exists() ? snapshot.getValue(Long.class) != null ? snapshot.getValue(Long.class) : 0 : 0;
                            long updatedCount = currentCount + 1;

                            notifierRef.setValue(updatedCount).addOnSuccessListener(aVoid -> {
                                notifierUsersRef.setValue(true);
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String displayName = snapshot.getValue(String.class);
                                            acceptedUsersRef.child(userId).setValue(displayName);
                                            Toast.makeText(getContext(), "Accepted by: " + displayName, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getContext(), "Error fetching display name", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }).addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Failed to update notifier", Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Error fetching notifier count", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error checking accepted users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
