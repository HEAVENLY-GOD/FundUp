package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth; // FirebaseAuth instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Set up email and password EditText
        emailEditText = findViewById(R.id.logem);  // ID for the email EditText
        passwordEditText = findViewById(R.id.logpa);  // ID for the password EditText

        // Check if there's an Intent coming from pass.java (with email and password)
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

        // If we received the email and password from pass.java, set them in the EditText fields
        if (email != null && password != null) {
            emailEditText.setText(email);
            passwordEditText.setText(password);
        }

        // Set up login button
        MaterialButton logButton = findViewById(R.id.log);
        logButton.setOnClickListener(view -> {
            // Capture email and password from EditText inside the onClickListener
            final String emailToLogin = emailEditText.getText().toString().trim();
            final String passwordToLogin = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(emailToLogin) || TextUtils.isEmpty(passwordToLogin)) {
                Toast.makeText(MainActivity.this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
            } else {
                // Call login method
                loginUser(emailToLogin, passwordToLogin);
            }
        });

        // Underline for signup_text
        TextView signupTextView = findViewById(R.id.signup);  // Ensure the ID matches your XML
        String signupText = "Signup";
        SpannableString signupContent = new SpannableString(signupText);
        signupContent.setSpan(new UnderlineSpan(), 0, signupText.length(), 0);
        signupTextView.setText(signupContent);

        // Click listener for signup
        signupTextView.setOnClickListener(v -> {
            // Navigate to signup activity
            Intent intent1 = new Intent(MainActivity.this, Welcome.class);
            startActivity(intent1);
        });
    }

    // Method to log in the user using Firebase
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login successful
                        Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        // Navigate to dashboard activity
                        Intent intent = new Intent(MainActivity.this, dashboard.class);
                        startActivity(intent);
                    } else {
                        // If the login fails
                        Toast.makeText(MainActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
