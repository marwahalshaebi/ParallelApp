package com.example.parallel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        User user = new User();
        user.sendNotification("Your Time is almost up", "View your parking spot");
    }
}
