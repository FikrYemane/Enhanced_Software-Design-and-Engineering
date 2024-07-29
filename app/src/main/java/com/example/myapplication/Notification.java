package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;


import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.tv.interactive.TvInteractiveAppService;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
;



public class Notification extends ComponentActivity {
    Switch dailyReminderSwitch;
    Switch weightGoalSwitch;
    Switch halfWayReminderSwitch;
    Switch congratsSwitch;
    Switch updateDailyWeightSwitch;
    private static final int NOTIFICATION_ID = 1;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    private static final int SMS_PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        dailyReminderSwitch = findViewById(R.id.daily_reminder);
        weightGoalSwitch = findViewById(R.id.weight_goal);
        halfWayReminderSwitch = findViewById(R.id.half_way_reminder);
        congratsSwitch = findViewById(R.id.congrats);
        updateDailyWeightSwitch = findViewById(R.id.update_daily_weight);


        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // Permission granted
                showNotification("Hello");
            } else {
                // Permission denied
                Toast.makeText(this, "Permission required for daily reminders", Toast.LENGTH_SHORT).show();
            }
        });
        dailyReminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    showNotification("Second");
                } else {
                    // Permission not granted, request it
                    requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
                }
            } else {
                // Switch turned off
                Toast.makeText(this, "Daily Reminder OFF", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showNotification(String message) {
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), chanelID);
        builder.setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Notification Title")
                .setContentText("We will send daily reminders") // Changed the message
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        notificationManager.notify(1, builder.build());
            // ... Use NotificationManager to actually display the notification
        }
}
