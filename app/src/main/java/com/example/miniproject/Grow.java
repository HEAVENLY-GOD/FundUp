package com.example.miniproject;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Grow extends Fragment {

    private DatabaseReference databaseReference;

    public Grow() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grow, container, false);

        // Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Projects");

        // Fetch and display all projects
        fetchAllProjects(view);

        return view;
    }

    private void fetchAllProjects(View view) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    LinearLayout cardContainer = view.findViewById(R.id.cardContainer);
                    cardContainer.removeAllViews();

                    for (DataSnapshot projectSnapshot : snapshot.getChildren()) {
                        String startupName = projectSnapshot.child("StartupName").getValue(String.class);
                        String category = projectSnapshot.child("Category").getValue(String.class);
                        addCardView(cardContainer, startupName, category);
                    }
                } else {
                    Toast.makeText(getContext(), "No data found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("GrowFragment", "Failed to fetch data", error.toException());
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCardView(LinearLayout cardContainer, String startupName, String category) {
        // Create CardView
        CardView cardView = new CardView(getContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(20, 20, 20, 20);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(50);  // Make card corners more rounded
        cardView.setCardBackgroundColor(Color.parseColor("#332400")); // Dark brown like the image
        cardView.setAlpha(0.9f);
        cardView.setCardElevation(10);

        // Create main LinearLayout inside CardView
        LinearLayout cardLayout = new LinearLayout(getContext());
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setPadding(30, 30, 30, 30);
        cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // Title Section
        LinearLayout titleLayout = new LinearLayout(getContext());
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // Title Label
        TextView titleLabel = new TextView(getContext());
        titleLabel.setText("Title:");
        titleLabel.setTextColor(Color.WHITE);
        titleLabel.setTextSize(18);
        titleLabel.setTypeface(null, Typeface.BOLD);
        titleLabel.setPadding(0, 0, 20, 0);

        // Title Value
        TextView titleText = new TextView(getContext());
        titleText.setText(startupName);
        titleText.setTextColor(Color.parseColor("#27FFE7"));
        titleText.setTextSize(18);
        titleText.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);

        titleLayout.addView(titleLabel);
        titleLayout.addView(titleText);

        // Category Section
        LinearLayout categoryLayout = new LinearLayout(getContext());
        categoryLayout.setOrientation(LinearLayout.HORIZONTAL);
        categoryLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // Category Label
        TextView categoryLabel = new TextView(getContext());
        categoryLabel.setText("Category:");
        categoryLabel.setTextColor(Color.WHITE);
        categoryLabel.setTextSize(18);
        categoryLabel.setTypeface(null, Typeface.BOLD);
        categoryLabel.setPadding(0, 10, 20, 0);

        // Category Value
        TextView categoryText = new TextView(getContext());
        categoryText.setText(category);
        categoryText.setTextColor(Color.parseColor("#27FFE7"));
        categoryText.setTextSize(18);
        categoryText.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);

        categoryLayout.addView(categoryLabel);
        categoryLayout.addView(categoryText);

        // Button Layout
        LinearLayout buttonLayout = new LinearLayout(getContext());
        buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        buttonLayout.setPadding(0, 20, 0, 0);
        buttonLayout.setGravity(android.view.Gravity.END);

        // View Button
        Button viewButton = new Button(getContext());
        viewButton.setText("View");
        viewButton.setBackgroundResource(R.drawable.roundg); // Apply rounded corners
        viewButton.setTextColor(Color.BLACK);
        viewButton.setTextSize(16);
        viewButton.setPadding(40, 10, 40, 10);

        buttonLayout.addView(viewButton);

        // Add Views to Card Layout
        cardLayout.addView(titleLayout);
        cardLayout.addView(categoryLayout);
        cardLayout.addView(buttonLayout);

        // Add everything to CardView
        cardView.addView(cardLayout);
        cardContainer.addView(cardView);
    }

}