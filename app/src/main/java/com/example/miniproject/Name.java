package com.example.miniproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.example.miniproject.model.User; // Import the custom User class

public class Name extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText;
    private FirebaseAuth mAuth;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_name);

        mAuth = FirebaseAuth.getInstance();
        email = getIntent().getStringExtra("email");  // Get email passed from Welcome activity

        firstNameEditText = findViewById(R.id.editText);
        lastNameEditText = findViewById(R.id.Last);

        TextView sivaTextView = findViewById(R.id.siva);
        stylet1Text(sivaTextView);

        TextView kumarTextView = findViewById(R.id.kumar);
        stylet2Text(kumarTextView);

        Button nameb = findViewById(R.id.nameb);
        nameb.setOnClickListener(v -> {
            if (validateFirstName() && validateLastName()) {
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                saveUserNameToFirebase(firstName, lastName);
            } else {
                Toast.makeText(Name.this, "Please complete all fields.", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean validateFirstName() {
        String firstName = firstNameEditText.getText().toString().trim();
        if (firstName.isEmpty()) {
            firstNameEditText.setError("First name is required");
            return false;
        }
        return true;
    }

    private boolean validateLastName() {
        String lastName = lastNameEditText.getText().toString().trim();
        if (lastName.isEmpty()) {
            lastNameEditText.setError("Last name is required");
            return false;
        }
        return true;
    }

    private void saveUserNameToFirebase(String firstName, String lastName) {
        String userId = mAuth.getCurrentUser().getUid();

        // Create a User object using the values
        User user = new User(firstName, lastName, email);  // Use the custom User class

        FirebaseDatabase.getInstance().getReference("Users")
                .child(userId)  // Use the current user's ID as the key
                .setValue(user)  // Store the user data
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Name.this, "Name saved successfully!", Toast.LENGTH_SHORT).show();
                        // Proceed to the next activity (if needed)
                        Intent intent = new Intent(Name.this, pass.class); // Replace with your target activity
                        startActivity(intent);
                    } else {
                        Toast.makeText(Name.this, "Failed to save name: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void stylet1Text(TextView textView) {
        String superText = "StartUp";
        SpannableString spannable = new SpannableString(superText);
        spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
    }

    private void stylet2Text(TextView textView) {
        String fullText = "JoinStartUp";
        SpannableString spannableString = new SpannableString(fullText);

        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 4, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
    }
}
