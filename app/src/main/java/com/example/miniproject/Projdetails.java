package com.example.miniproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
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

public class Projdetails extends Fragment {

    private EditText startupName, founders1, founders2, problemStatement, solutionValue, targetMarket, investmentAsk, contactEmail, contactPhone;
    private Spinner categorySpinner, businessSpinner;
    private Button postButton;
    private DatabaseReference databaseReference;
    private String userId;
    private boolean fetchData = false;
    private LottieAnimationView lottieAnimationView;

    public Projdetails() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projdetails, container, false);

        if (getArguments() != null) {
            fetchData = getArguments().getBoolean("fetchData", false);
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Projects").child(userId);
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return view;
        }

        startupName = view.findViewById(R.id.startupName);
        founders1 = view.findViewById(R.id.founder1);
        founders2 = view.findViewById(R.id.founder2);
        problemStatement = view.findViewById(R.id.problemStatement);
        solutionValue = view.findViewById(R.id.solution);
        targetMarket = view.findViewById(R.id.targetMarket);
        investmentAsk = view.findViewById(R.id.investmentAsk);
        contactEmail = view.findViewById(R.id.contactEmail);
        contactPhone = view.findViewById(R.id.contactPhone);
        categorySpinner = view.findViewById(R.id.cat);
        businessSpinner = view.findViewById(R.id.business);
        postButton = view.findViewById(R.id.signup);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);

        setupSpinners();

        if (fetchData) {
            fetchProjectDetails();
        }

        postButton.setOnClickListener(v -> saveOrUpdateData());

        return view;
    }

    private void setupSpinners() {
        String[] categories = {"Technical", "Social", "Ecommerce", "Fintech", "HealthTech", "SaaS", "Biotech", "Education"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryAdapter);

        String[] businessModels = {"Business to Business (B2B)", "Business to Consumer (B2C)", "Subscription-based Model", "On-demand Model"};
        ArrayAdapter<String> businessAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, businessModels);
        businessSpinner.setAdapter(businessAdapter);
    }

    private void fetchProjectDetails() {
        if (userId == null) return;

        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    startupName.setText(snapshot.child("StartupName").getValue(String.class));
                    founders1.setText(snapshot.child("Founder1").getValue(String.class));
                    founders2.setText(snapshot.child("Founder2").getValue(String.class));
                    problemStatement.setText(snapshot.child("ProblemStatement").getValue(String.class));
                    solutionValue.setText(snapshot.child("SolutionValue").getValue(String.class));
                    targetMarket.setText(snapshot.child("TargetMarket").getValue(String.class));
                    investmentAsk.setText(snapshot.child("InvestmentAsk").getValue(String.class));
                    contactEmail.setText(snapshot.child("ContactEmail").getValue(String.class));
                    contactPhone.setText(snapshot.child("ContactPhone").getValue(String.class));
                    setSpinnerSelection(categorySpinner, snapshot.child("Category").getValue(String.class));
                    setSpinnerSelection(businessSpinner, snapshot.child("BusinessModel").getValue(String.class));
                } else {
                    Toast.makeText(getContext(), "No project data found", Toast.LENGTH_SHORT).show();
                }
                lottieAnimationView.postDelayed(() -> {
                    lottieAnimationView.pauseAnimation();
                    lottieAnimationView.setVisibility(View.GONE);
                }, 2000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                lottieAnimationView.setVisibility(View.GONE);
            }
        });
    }

    private void saveOrUpdateData() {
        if (userId == null) return;

        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();

        Map<String, String> projectData = new HashMap<>();
        projectData.put("StartupName", startupName.getText().toString().trim());
        projectData.put("Founder1", founders1.getText().toString().trim());
        projectData.put("Founder2", founders2.getText().toString().trim());
        projectData.put("Category", categorySpinner.getSelectedItem().toString());
        projectData.put("BusinessModel", businessSpinner.getSelectedItem().toString());
        projectData.put("ProblemStatement", problemStatement.getText().toString().trim());
        projectData.put("SolutionValue", solutionValue.getText().toString().trim());
        projectData.put("TargetMarket", targetMarket.getText().toString().trim());
        projectData.put("InvestmentAsk", investmentAsk.getText().toString().trim());
        projectData.put("ContactEmail", contactEmail.getText().toString().trim());
        projectData.put("ContactPhone", contactPhone.getText().toString().trim());

        databaseReference.setValue(projectData).addOnCompleteListener(task -> {
            lottieAnimationView.pauseAnimation();
            lottieAnimationView.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Project details saved/updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to save!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        if (value == null) return;
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        int position = adapter.getPosition(value);
        if (position >= 0) {
            spinner.setSelection(position);
        }
    }
}
