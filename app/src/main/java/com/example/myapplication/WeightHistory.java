package com.example.myapplication;

import androidx.activity.ComponentActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.activity.compose.ComponentActivityKt;
import androidx.annotation.Nullable;
import androidx.compose.runtime.CompositionContext;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WeightHistory extends ComponentActivity {
    LinearLayout weightContainer;
    Button back_button;
    DBHelper db = new DBHelper(this);
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        setContentView(R.layout.weight_history);
        weightContainer = findViewById(R.id.weight_container);
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeightHistory.this, Menu.class);
                intent.putExtra("username", intent.getStringExtra("username"));
                intent.putExtra("password", intent.getStringExtra("password"));
                startActivity(intent);
            }
        });

      //  System.out.println(db.getUsername());


        updateWeightHistory();
    }


    private void updateWeightHistory() {
        // Clear existing views from weightContainer

        weightContainer.removeAllViews();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(USERNAME, null);
        String password = sharedPreferences.getString(PASSWORD, null);
        ArrayList<Double> importedList = db.getWeights(username);
        // Get today's date
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        // Loop through the list of imported weights and add TextViews dynamically
        for (int i = 0; i < importedList.size(); i++) {
            String weightText = "Weight " + (i + 1) + ": " + importedList.get(i);
            String dateText = "Date: " + dateFormat.format(today);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(weightText + " " + dateText);
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setPadding(0, 50, 0, 0);
            textView.setTypeface(null, Typeface.BOLD);
            weightContainer.addView(textView);
        }


    }
}
