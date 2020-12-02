package com.example.parallel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AlarmManagerCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_test);
        Button button = findViewById(R.id.button);
        final TextView text = findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("Hello There");
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(v.getContext(), AlertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(v.getContext(), 1, intent, 0);
                long timeAtButtonClick = System.currentTimeMillis();
                long time10 = 10 *1000;
                alarmManager.set(AlarmManager.RTC_WAKEUP,timeAtButtonClick+time10,pendingIntent);
            }
            });
    }
}