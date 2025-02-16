package com.example.miniproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Welcome extends AppCompatActivity {

    private EditText emailEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();  // Initialize FirebaseAuth

        // Handle first TextView (JoinStartUp)
        TextView startupTextView = findViewById(R.id.startup);
        styleJoinStartUpText(startupTextView);

        // Handle second TextView (StartUp with S and u in white)
        TextView superTextView = findViewById(R.id.Super);
        styleSuperText(superTextView);

        ImageView exitImage = findViewById(R.id.exit);
        exitImage.setOnClickListener(v -> finish()); // Close the activity

        // Initialize the EditText for email input
        emailEditText = findViewById(R.id.editText);

        // Set focus change listener for the EditText (trigger validation when focus is lost)
        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateEmail();  // Trigger email validation when EditText loses focus
        });

        // Add text change listener to clear the error while typing
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5) validateEmail(); // Trigger validation if the text length is greater than 5
            }
        });

        // Button click handler
        Button cbutton = findViewById(R.id.cbutton);
        cbutton.setOnClickListener(v -> {
            if (validateEmail()) {
                // Proceed with valid email authentication
                String email = emailEditText.getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(email, "defaultPassword") // Temporarily using a default password
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(Welcome.this, Name.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Welcome.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // Show a message to the user that the email is invalid
                Toast.makeText(Welcome.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            }
        });

        // Edge-to-edge setup for window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Validation function
    private boolean validateEmail() {
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email format");
            return false;
        }
        return true;
    }

    private void styleJoinStartUpText(TextView textView) {
        String fullText = "JoinStartUp";
        SpannableString spannableString = new SpannableString(fullText);

        // Black for "Join"
        spannableString.setSpan(
                new ForegroundColorSpan(Color.BLACK),
                0,
                4,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // White for "StartUp"
        spannableString.setSpan(
                new ForegroundColorSpan(Color.WHITE),
                4,
                fullText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        textView.setText(spannableString);
    }

    private void styleSuperText(TextView textView) {
        String superText = "StartUp";
        SpannableString spannable = new SpannableString(superText);

        // White for 'S' (first character)
        spannable.setSpan(
                new ForegroundColorSpan(Color.WHITE),
                0,
                1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // White for 'U' (6th character)
        spannable.setSpan(
                new ForegroundColorSpan(Color.WHITE),
                5,
                6,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        textView.setText(spannable);
    }
}
