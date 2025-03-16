package com.example.miniproject;

import android.content.Context;
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
        databaseReference = FirebaseDatabase.getInstance().getReference("Projects");
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
                        String projectId = projectSnapshot.getKey();
                        String startupName = projectSnapshot.child("StartupName").getValue(String.class);
                        String category = projectSnapshot.child("Category").getValue(String.class);
                        addCardView(cardContainer, projectId, startupName, category);
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

    private void addCardView(LinearLayout cardContainer, String projectId, String startupName, String category) {
        Context context = cardContainer.getContext();

        CardView cardView = new CardView(context);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(20, 20, 20, 20);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(50);
        cardView.setCardBackgroundColor(Color.parseColor("#332400"));
        cardView.setAlpha(0.9f);
        cardView.setCardElevation(10);

        LinearLayout cardLayout = new LinearLayout(context);
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setPadding(30, 30, 30, 30);
        cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        TextView titleText = new TextView(context);
        titleText.setText("Title: " + startupName);
        titleText.setTextColor(Color.parseColor("#FFFFFFFF"));
        titleText.setTextSize(18);
        titleText.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);

        TextView categoryText = new TextView(context);
        categoryText.setText("Category: " + category);
        categoryText.setTextColor(Color.parseColor("#FFFFFFFF"));
        categoryText.setTextSize(18);
        categoryText.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);

        LinearLayout buttonContainer = new LinearLayout(context);
        buttonContainer.setGravity(android.view.Gravity.END);
        buttonContainer.setPadding(0, 20, 0, 0);

        Button viewButton = new Button(context);
        viewButton.setText("View");
        viewButton.setBackgroundResource(R.drawable.roundg);
        viewButton.setTextColor(Color.BLACK);
        viewButton.setTextSize(16);
        viewButton.setPadding(40, 10, 40, 10);

        viewButton.setOnClickListener(v -> {
            DatabaseReference domeRef = FirebaseDatabase.getInstance()
                    .getReference("Projects")
                    .child(projectId)
                    .child("dome");

            domeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long currentDome = 0;
                    if (snapshot.exists()) {
                        currentDome = snapshot.getValue(Long.class);
                    }
                    domeRef.setValue(currentDome + 1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("GrowFragment", "Failed to update dome count", error.toException());
                }
            });

            detail detailFragment = detail.newInstance(projectId);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        buttonContainer.addView(viewButton);
        cardLayout.addView(titleText);
        cardLayout.addView(categoryText);
        cardLayout.addView(buttonContainer);

        cardView.addView(cardLayout);
        cardContainer.addView(cardView);
    }
}
