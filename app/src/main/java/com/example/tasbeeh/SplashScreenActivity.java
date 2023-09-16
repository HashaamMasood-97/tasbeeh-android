package com.example.tasbeeh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_DURATION = 1500; // 4 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Delay for 4 seconds and then transition to the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an intent to launch the main activity
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish(); // Close the splash screen activity
            }
        }, SPLASH_DISPLAY_DURATION);
    }
}
