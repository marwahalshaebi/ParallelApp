package com.example.parallel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfile extends AppCompatActivity {

    FirebaseUser user;
    ImageView mBack;
    String userID;
    DatabaseReference reference;
    Button log, edit;

    //To be used later for user Profile image
    de.hdodenhof.circleimageview.CircleImageView profileImage;

    private static final int GALLERY_INTENT_CODE = 1023 ;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        fAuth = FirebaseAuth.getInstance();

        //mBack as named to take the user back to the UserProfile activity
        mBack = findViewById(R.id.back);


        //Getting instance of the user
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Getting a reference to the user database
        reference = FirebaseDatabase.getInstance().getReference("User");

        //get the user id of the current user to be used to later
        userID = user.getUid();

        //Logs the user out of the activity
        log = findViewById(R.id.logoutProfile);

        // Takes the user to the EditProfile activity
        edit = findViewById(R.id.editProfile);


        //To be used later for user Profile image
        profileImage= findViewById(R.id.profileImage);


        // Getting references to the following textviews
        final TextView profileName =findViewById(R.id.profileName);
        final TextView profileEmail =  findViewById(R.id.profileEmail);
        final TextView profileLicense =findViewById(R.id.profileLicense);

        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               @Override
               public void onSuccess(Uri uri) {
                   Picasso.get().load(uri).into(profileImage);
               }
        });

        //Here I am looking the users databases for the user that is logged in currently
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Once the user is found I get a snapshot of the user as user class
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    // Here I am filling up the textviews with the users info (Name,email and License)
                    String fullName = userProfile.getFullName();
                    String email = userProfile.getEmail();
                    String license = userProfile.getLicenseNumber();

                    profileEmail.setText(email);
                    profileLicense.setText(license);
                    profileName.setText(fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this,"Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        //Takes you back to the Main activity
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        //Takes you to the EditProfile activity
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),EditProfile.class));
            }
        });

        //logs the User out of the app
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });
    }


}