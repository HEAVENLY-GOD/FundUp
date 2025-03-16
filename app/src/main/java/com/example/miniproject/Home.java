package com.example.miniproject;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends Fragment {
    private EditText card, found1, found2, dome, notifier;
    private DatabaseReference databaseReference;
    private String userId;

    public Home() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        card = view.findViewById(R.id.card);
        found1 = view.findViewById(R.id.found1);
        found2 = view.findViewById(R.id.found2);
        dome = view.findViewById(R.id.dome);
        notifier = view.findViewById(R.id.notifier);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Projects").child(userId);
            fetchProjectDetails();
        }

        Button postButton = view.findViewById(R.id.post);
        Button myProjectButton = view.findViewById(R.id.myproject);

        postButton.setOnClickListener(v -> openProjDetails(false));
        myProjectButton.setOnClickListener(v -> openProjDetails(true));

        return view;
    }

    private void fetchProjectDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.e("HomeFragment", "No project data found");
                    return;
                }

                card.setText(snapshot.child("StartupName").exists() ? snapshot.child("StartupName").getValue(String.class) : "No Startup Name Found");
                found1.setText(snapshot.child("Founder1").exists() ? snapshot.child("Founder1").getValue(String.class) : "N/A");
                found2.setText(snapshot.child("Founder2").exists() ? snapshot.child("Founder2").getValue(String.class) : "N/A");
                dome.setText(snapshot.child("dome").exists() ? String.valueOf(snapshot.child("dome").getValue(Integer.class)) : "0");

                // Fetch Notifier Count and Display
                if (snapshot.child("notifier").exists()) {
                    long count = snapshot.child("notifier").getValue(Long.class);
                    notifier.setText(String.valueOf(count));
                } else {
                    notifier.setText("0"); // Default value if no count found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeFragment", "Failed to fetch project details", error.toException());
            }
        });
    }

    private void openProjDetails(boolean fetchData) {
        Projdetails projdetailsFragment = new Projdetails();
        Bundle bundle = new Bundle();
        bundle.putBoolean("fetchData", fetchData);
        projdetailsFragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, projdetailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
