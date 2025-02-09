package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class Name extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_name);

        // Initialize EditText fields for First Name, Last Name
        firstNameEditText = findViewById(R.id.editText);  // ID for first name EditText
        lastNameEditText = findViewById(R.id.Last);  // ID for last name EditText

        // Handle button click to validate and move to next activity
        Button nameb = findViewById(R.id.nameb);
        nameb.setOnClickListener(v -> {
            if (validateFirstName() && validateLastName()) {
                Intent intent = new Intent(Name.this, pass.class);
                startActivity(intent);
            } else {
                // Show a message to the user if fields are empty
                Toast.makeText(Name.this, "Please complete all fields.", Toast.LENGTH_SHORT).show();
            }
        });

        // Edge-to-edge setup for window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Method to validate the first name
    private boolean validateFirstName() {
        String firstName = firstNameEditText.getText().toString().trim();
        if (firstName.isEmpty()) {
            firstNameEditText.setError("First name is required");
            return false;
        }
        return true;
    }

    // Method to validate the last name
    private boolean validateLastName() {
        String lastName = lastNameEditText.getText().toString().trim();
        if (lastName.isEmpty()) {
            lastNameEditText.setError("Last name is required");
            return false;
        }
        return true;
    }
}
