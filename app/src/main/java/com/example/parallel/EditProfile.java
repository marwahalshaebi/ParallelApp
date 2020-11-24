package com.example.parallel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class EditProfile extends AppCompatActivity {
    private FirebaseUser user;
    private ImageView mBack;
    private String userID;
    private DatabaseReference reference;
    private Button update;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mBack = findViewById(R.id.editback);
        update= findViewById(R.id.updateProfile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        userID = user.getUid();
        final EditText editName =findViewById(R.id.editName);
        final EditText editLicense =findViewById(R.id.editLicense);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);
                        if(userProfile != null && !editName.getText().toString().matches("") && !editLicense.getText().toString().matches("")) {
                            String fullName = editName.getText().toString();
                            String license = editLicense.getText().toString();

                            reference.child(userID).child("fullName").setValue(fullName);
                            reference.child(userID).child("licenseNumber").setValue(license);

                        }else if (userProfile != null && !editName.getText().toString().matches("")){
                            String fullName = editName.getText().toString();
                            reference.child(userID).child("fullName").setValue(fullName);


                        }else if(userProfile != null && !editLicense.getText().toString().matches("")){
                            String license = editLicense.getText().toString();
                            reference.child(userID).child("licenseNumber").setValue(license);
                        }
                        startActivity(new Intent(getApplicationContext(),UserProfile.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditProfile.this,"Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UserProfile.class));
            }
        });

    }

}