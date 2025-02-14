package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton logButton = findViewById(R.id.log);
        logButton.setOnClickListener(view -> {
            // Create an Intent to navigate to the next activity
            Intent intent = new Intent(MainActivity.this, dashboard.class);
            startActivity(intent);
        });

        // Underline for signup_text
        TextView signupTextView = findViewById(R.id.signup); // Make sure ID matches XML
        String signupText = "Signup";
        SpannableString signupContent = new SpannableString(signupText);
        signupContent.setSpan(new UnderlineSpan(), 0, signupText.length(), 0);
        signupTextView.setText(signupContent);

        // Click listener
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Welcome.class);
                startActivity(intent);
            }
        });
    }
}