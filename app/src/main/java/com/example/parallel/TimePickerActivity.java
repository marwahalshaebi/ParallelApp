package com.example.parallel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;

public class TimePickerActivity extends AppCompatActivity {
//public class TimePicker extends FrameLayout {

    private TimePicker time_Picker;
    private Button button_Show_Time;
    private LocalTime currentHours = null;
    private LocalTime currentMins = null;
    private int totalMinutes = 0;
    private float parkingRate = (float) 0.05;
    private float costs = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        showTime();
    }

    public void showTime(){
        time_Picker = (TimePicker)findViewById(R.id.simpleTimePicker);
        button_Show_Time = (Button)findViewById(R.id.button);
        time_Picker.setIs24HourView(true);


        button_Show_Time.setOnClickListener(
                new View.OnClickListener(){
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v){

                        int h = time_Picker.getCurrentHour() -  currentHours.getHour();
                        int m = time_Picker.getCurrentMinute() - currentMins.getMinute();

                        costs = (h*60 + m) * parkingRate;

                        Toast.makeText(getBaseContext(), time_Picker.getCurrentHour() + " : " + time_Picker.getCurrentMinute() + " -- " + costs ,
                                Toast.LENGTH_LONG).show();

                        startActivity(new Intent(getApplicationContext(), MainPay.class).putExtra("costs", costs));

                        //startActivity((new Intent(getApplicationContext(), MainActivity.class)
                                //.putExtra("email", email.replaceAll("[.]","-"))));

                        //Toast.makeText(getBaseContext(), h + " : " + m + "PARKING TIME LIMIT",
                                //Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}