package com.example.miniproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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

public class pass extends AppCompatActivity {

    private EditText passwordEditText, confirmPasswordEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pass);

        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth

        // Initialize the password and confirm password EditTexts
        passwordEditText = findViewById(R.id.ceditText); // ID for password EditText
        confirmPasswordEditText = findViewById(R.id.CLast); // ID for confirm password EditText

        // Setup TextViews with styled text
        TextView t1TextView = findViewById(R.id.t1);
        stylet1Text(t1TextView);

        // Handle second TextView (StartUp with S and u in white)
        TextView t2TextView = findViewById(R.id.t2);
        stylet2Text(t2TextView);

        // Handle button click to validate and move to the next activity
        Button scon = findViewById(R.id.scon);
        scon.setOnClickListener(v -> {
            if (validatePassword() && validateConfirmPassword()) {
                String password = passwordEditText.getText().toString().trim();
                mAuth.getCurrentUser().updatePassword(password) // Update password in Firebase
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Password updated successfully
                                Intent intent = new Intent(pass.this, slide.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(pass.this, "Password update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // Show a message to the user if validation fails
                Toast.makeText(pass.this, "Please make sure both passwords match and are not empty.", Toast.LENGTH_SHORT).show();
            }
        });

        // Edge-to-edge setup for window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Method to validate the password
    private boolean validatePassword() {
        String password = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return false;
        }
        return true;
    }

    // Method to validate the confirm password
    private boolean validateConfirmPassword() {
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.setError("Confirm password is required");
            return false;
        }

        if (!confirmPassword.equals(password)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    // Method to style the "StartUp" text with different colors for S and U
    private void stylet1Text(TextView textView) {
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

    // Method to style the "JoinStartUp" text with "Join" in black and "StartUp" in white
    private void stylet2Text(TextView textView) {
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
}
