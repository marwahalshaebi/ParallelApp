package com.example.parallel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfile extends AppCompatActivity {
    private FirebaseUser user;
    private ImageView mBack;
    private String userID;
    private DatabaseReference reference;
    private Button update;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    //To be used to give user the ability to create a Profile image
    private de.hdodenhof.circleimageview.CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        profileImage= findViewById(R.id.editImage);
        //mBack as named to take the user back to the UserProfile activity
        mBack = findViewById(R.id.editback);

        //Update will update the users name and licenses number once clicked
        update= findViewById(R.id.updateProfile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        //Getting instance of the user
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Getting a reference to the user database
        reference = FirebaseDatabase.getInstance().getReference("User");

        //get the user id of the current user to be used to later
        userID = user.getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        // Geting values provided by the user in the edit text
        final EditText editName =findViewById(R.id.editName);
        final EditText editLicense =findViewById(R.id.editLicense);

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });


        //Once the update button is clicked the following will be excuted
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Here I am looking the users databases for the user that is logged in currently
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //Once the user is found I get a snapshot of the user as user class
                        User userProfile = snapshot.getValue(User.class);

                        // if the user wants to change both values this is executed other wise it will check whichever one the user wants to change
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

                        //Once all this is done it will take the user back to the UserProfile activity
                        startActivity(new Intent(getApplicationContext(),UserProfile.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditProfile.this,"Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //Takes you back to the UseProfile activity
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UserProfile.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);


            }
        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        // uplaod image to firebase storage
        final StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}