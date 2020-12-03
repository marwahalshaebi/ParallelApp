package com.example.parallel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class User {

    private String fullName, email, licenseNumber;

    public User() {
    }

    public User(String fullName) {
        this.fullName = fullName;
    }

    public User(String fullName, String email, String licenseNumber) {
        this.fullName = fullName;
        this.email = email;
        this.licenseNumber = licenseNumber;

    }


    public String getLicenseNumber() {
        return licenseNumber;
    }


    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
    public String getEmail() {
        return email;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public  void sendNotification(final String title, final String body){


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FETCHING TOKEN", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://parallel3004.firebaseapp.com/api/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        Api api = retrofit.create(Api.class);

                        retrofit2.Call<ResponseBody> call = api.sendNotification(token, title, body);

                        call.enqueue(new Callback<ResponseBody>() { @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            System.out.println("========SUCCESS=========");
                        }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                System.out.println("========FAILURE=========");
                            }
                        });


                    }
                });


    }




}
