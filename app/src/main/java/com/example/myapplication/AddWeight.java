package com.example.myapplication;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.compose.ComponentActivityKt;
import androidx.compose.runtime.CompositionContext;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;

public class AddWeight extends ComponentActivity{


    Button add_weight_button;
    EditText entered_weight;
    Button back_button;
    EditText weight_date;
    DBHelper DB;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";


    public static List <Double> weights = new ArrayList<>();
    List <Integer> dates = new ArrayList<>();
    LoginActivity getUserName = new LoginActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_weight);
        Intent intent = getIntent();
        add_weight_button = findViewById(R.id.add_weight_button);
        entered_weight = findViewById(R.id.entered_weight);
        back_button = findViewById(R.id.back_button);
        weight_date = findViewById(R.id.editTextDate);
        DB = new DBHelper(this);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(USERNAME, null);
        String password = sharedPreferences.getString(PASSWORD, null);

//
//        if (intent != null) {
//            username = intent.getStringExtra("username");
//        }
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddWeight.this, Menu.class);

                startActivity(intent);
            }
        });
        add_weight_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weightText = entered_weight.getText().toString().trim();

                if (weightText.isEmpty()) {
                    Toast.makeText(AddWeight.this, "Please enter a weight", Toast.LENGTH_SHORT).show();
                    return;
                }
                double enteredWeight = Double.parseDouble(weightText);
                boolean isInserted = DB.insertWeightData(username, enteredWeight);
                if (isInserted) {
                    Toast.makeText(AddWeight.this, "Weight Added", Toast.LENGTH_SHORT).show();
                    ArrayList<Double> entetedWeight = DB.getWeights(username);
                    System.out.println(entetedWeight);
                }
                 else {
                        Toast.makeText(AddWeight.this, "Failed to add weight", Toast.LENGTH_SHORT).show();
                    }

            }
        });




    }

}
