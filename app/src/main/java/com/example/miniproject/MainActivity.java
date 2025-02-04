package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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