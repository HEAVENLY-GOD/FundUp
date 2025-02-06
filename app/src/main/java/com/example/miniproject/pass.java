package com.example.miniproject;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class pass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pass);

        TextView t1TextView = findViewById(R.id.t1);
        stylet1Text(t1TextView);

        // Handle second TextView (StartUp with S and u in white)
        TextView t2TextView = findViewById(R.id.t2);
        stylet2Text(t2TextView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

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

        // Rest will use XML's default black color
        textView.setText(spannable);
    }
}
