package com.example.miniproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private FirebaseAuth mAuth; // FirebaseAuth instance
    private SharedPreferences sharedPreferences; // SharedPreferences to store the login state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        // Set up email and password EditText
        emailEditText = findViewById(R.id.logem);  // ID for the email EditText
        passwordEditText = findViewById(R.id.logpa);  // ID for the password EditText
        rememberMeCheckBox = findViewById(R.id.check);  // ID for the Remember Me checkbox

        // Check if the user is already logged in and rememberMe is true
        if (sharedPreferences.getBoolean("rememberMe", false)) {
            // User is remembered and logged in, navigate directly to Technical activity
            navigateToTechnical();
        }

        // Make sure the "Remember Me" checkbox is unchecked when the user is not logged in
        rememberMeCheckBox.setChecked(false);

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

                        // Check if Remember me is checked
                        if (rememberMeCheckBox.isChecked()) {
                            // Save the "Remember me" state in SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("rememberMe", true);
                            editor.putString("userEmail", email); // Optionally save the user's email
                            editor.apply();
                        }

                        // Navigate to Technical activity and clear the back stack
                        navigateToTechnical();
                    } else {
                        // If the login fails
                        Toast.makeText(MainActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to navigate to Technical activity
    private void navigateToTechnical() {
        Intent intent = new Intent(MainActivity.this, Tecnical.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear back stack
        startActivity(intent);
        finish(); // Finish the current activity (MainActivity)
    }
}
