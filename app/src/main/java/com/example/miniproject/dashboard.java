package com.example.miniproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class dashboard extends AppCompatActivity {

    // Declare ImageViews
    private ImageView a1, a2, a3, a4, a5, a6, a7, a8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Enable edge-to-edge window

        setContentView(R.layout.activity_dashboard);

        a1 = findViewById(R.id.b1);
        a2 = findViewById(R.id.b2);
        a3 = findViewById(R.id.b3);
        a4 = findViewById(R.id.b4);
        a5 = findViewById(R.id.b5);
        a6 = findViewById(R.id.b6);
        a7 = findViewById(R.id.b7);
        a8 = findViewById(R.id.b8);

        // Set onClickListeners for each image button
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(dashboard.this, "You clicked Technical!", Toast.LENGTH_SHORT).show();
            }
        });

        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(dashboard.this, "You clicked Social!", Toast.LENGTH_SHORT).show();
            }
        });

        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(dashboard.this, "You clicked Ecommerce!", Toast.LENGTH_SHORT).show();
            }
        });

        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(dashboard.this, "You clicked Healthtech!", Toast.LENGTH_SHORT).show();
            }
        });

        a5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(dashboard.this, "You clicked Biotech!", Toast.LENGTH_SHORT).show();
            }
        });

        a6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(dashboard.this, "You clicked Fintech!", Toast.LENGTH_SHORT).show();
            }
        });

        a7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(dashboard.this, "You clicked Saas!", Toast.LENGTH_SHORT).show();
            }
        });

        a8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(dashboard.this, "You clicked Education!", Toast.LENGTH_SHORT).show();
            }
        });

        // Apply window insets handling to ensure proper padding with system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
