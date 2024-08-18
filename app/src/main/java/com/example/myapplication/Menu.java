// Menu.java
/*
 * Menu.java
 *
 * Version: 1.0
 * Author: Fikr Yemane
 * Date: 04/15/2024
 *
 * This class represents the main menu of the fitness tracking application.
 * It provides navigation to various features including adding weight, viewing weight history, setting goals, and managing notifications.
 * The class interacts with shared preferences for storing user session data and passes user information between activities.
 */

package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.ComponentActivity;
import androidx.activity.compose.ComponentActivityKt;
import androidx.compose.runtime.CompositionContext;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.Nullable;


public final class Menu extends ComponentActivity {

    Button add_weight;
    Button weight_history;
    Button goals;
    Button notification;
    EditText userName;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    @SuppressLint("MissingInflatedId")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        notification = findViewById(R.id.notification);
        add_weight = findViewById(R.id.add_weight);
        weight_history = findViewById(R.id.weight_history);
        goals = findViewById(R.id.future_goals);
        userName = findViewById(R.id.username1);
        LoginActivity login = new LoginActivity();
        Intent intent = getIntent();
        final String intentUserName = intent.getStringExtra("username");

//        System.out.println(intentUserName);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String name = sharedPreferences.getString(USERNAME, null);
        String password = sharedPreferences.getString(PASSWORD, null);

        add_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, AddWeight.class );
                intent.putExtra("username",intentUserName);
                startActivity(intent);
            }
        });
        weight_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, WeightHistory.class);
                startActivity(intent);
            }
        });
        goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Goals.class);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Notification.class);
                startActivity(intent);
            }
        });

    }
}
