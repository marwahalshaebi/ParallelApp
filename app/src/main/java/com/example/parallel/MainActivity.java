package com.example.parallel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView nameText;
    private String emailExtra;
    private ImageView mProfile;
    public static final String CHANNEL_ID = "Parallel App";
    private static final String CHANNEL_NAME = "Main Activity";
    private static final String CHANNEL_DESC = "Parallel App Notifications";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProfile = findViewById(R.id.Profile);

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UserProfile.class));
            }
        });

    }

    public void getStarted(View view) {
        startActivity(new Intent(getApplicationContext(),Map.class));
    }
}