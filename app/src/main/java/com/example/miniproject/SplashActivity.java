package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.InputStream;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();  // Correct Handler initialization
    private Runnable runnable;  // Declare the runnable here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Handle system UI inset (for edge-to-edge display)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Access raw resource (e.g., youtube.mp4)

        // Initialize the runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                // Start MainActivity after the splash screen
                Intent intent = new Intent(SplashActivity.this, MainActivity.class); // Fix the Intent creation
                startActivity(intent);
                finish(); // Finish the splash activity to prevent returning back to it
            }
        };

        // Schedule the runnable to run after 3 seconds
        handler.postDelayed(runnable, 3000); // Delay of 3 seconds (adjust as needed)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);  // Remove the runnable callback if the activity is destroyed
    }
}
