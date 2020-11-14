package com.example.parallel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView nameText;
    private String emailExtra;
    public static final String CHANNEL_ID = "Parallel App";
    private static final String CHANNEL_NAME = "Main Activity";
    private static final String CHANNEL_DESC = "Simplified Coding Notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        this.emailExtra = getIntent().getStringExtra("email");
//        this.nameText = findViewById(R.id.nameTextView);
//        //this.nameText.setText("Hi "+emailExtra);
//
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference().child("User").child(this.emailExtra);
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child("fullName").getValue() != null){
//                    nameText.setText("Hi "+ snapshot.child("fullName").getValue().toString());
//                }
//
//            }-+
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }

}
 // this is the welcome page
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
    public void getStarted(View view) {
        startActivity(new Intent(getApplicationContext(),Map.class));
    }
}