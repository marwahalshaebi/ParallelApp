package com.example.parallel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.security.auth.callback.CallbackHandler;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class User {

    private String fullName, email, licenseNumber;
    public String token;

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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private void setToken(String token) {this.token = token;};

    private  void sendNotification(String title, String body){

        this.token = FirebaseMessaging.getInstance().getToken().getResult();
        Log.d("TOKEN", token);
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    public void onSuccess(TResult result){
//
//                    }
//                });
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (task.isSuccessful()) {
//                            String token = task.getResult().getToken();
//                            setToken(token);
//                            Log.d("TOKEN", "Refreshed token: " + token);
//                        } else {
//                            Log.d("TOKEN", "FAILED TO GET TOKEN");
//                        }
//                    }
//                });
//
//        FirebaseInstallations.getToken()
//                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                            if (task.isSuccessful()) {
//                                String token = task.getResult().getToken();
//                                setToken(token);
//                                Log.d("TOKEN", "Refreshed token: " + token);
//                            } else {
//                                Log.d("TOKEN", "FAILED TO GET TOKEN");
//                            }
//                        }
//                    });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parallel3004.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = api.sendNotification(this.token, title, body);

//        call.enqueue(new CallbackHandler<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) throws IOException {
//                //Toast.makeText(SendNotificationActivity.this, response.body().string(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });

    }

}
