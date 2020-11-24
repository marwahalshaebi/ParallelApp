package com.example.parallel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {


    private EditText emailEdit;
    private Button resetBtn;
    private ProgressBar progressBar;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEdit = findViewById(R.id.email);
        resetBtn =  findViewById(R.id.resetBtn);
        progressBar = findViewById(R.id.progressBar2);

        auth = FirebaseAuth.getInstance();

        //Calling reset function onc reset button is clicked will do this in the rest of my code
        //to increase readability
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
    }

    private void reset(){

        //get email provided by the user
        String email = emailEdit.getText().toString().trim();

        //Checks if the user provided valid email
        if(email.isEmpty()){
            emailEdit.setError("Email is required");
            emailEdit.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdit.setError("Please Provide Valid Email");
            emailEdit.requestFocus();
        }

        progressBar.setVisibility(View.VISIBLE);

        //this calls a function sends a reset password to the user email
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    //Asking the user to check their email to reset the password and then taking them back to
                    //the login page
                    Toast.makeText(ForgotPassword.this, "Check your email to reset password", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),Login.class));

                }else{
                    Toast.makeText(ForgotPassword.this,"Try again",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}