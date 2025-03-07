package com.example.miniproject;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Home extends Fragment {

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find buttons
        Button postButton = view.findViewById(R.id.post);
        Button myProjectButton = view.findViewById(R.id.myproject);

        // Post button opens Projdetails fragment (for creating/updating)
        postButton.setOnClickListener(v -> {
            Log.d("HomeFragment", "Post button clicked!");
            openProjDetails(false);  // false -> Means user is posting a new project
        });

        // MyProject button opens Projdetails fragment (to fetch existing data)
        myProjectButton.setOnClickListener(v -> {
            Log.d("HomeFragment", "My Projects button clicked!");
            openProjDetails(true);  // true -> Means fetch data from Firebase
        });

        return view;
    }

    private void openProjDetails(boolean fetchData) {
        Projdetails projdetailsFragment = new Projdetails();

        // Pass a flag to indicate data fetching
        Bundle bundle = new Bundle();
        bundle.putBoolean("fetchData", fetchData);
        projdetailsFragment.setArguments(bundle);

        // Replace current fragment with Projdetails
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, projdetailsFragment);
        transaction.addToBackStack(null); // Allow back navigation
        transaction.commit();
    }
}
