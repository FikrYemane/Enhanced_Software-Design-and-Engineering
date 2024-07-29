package com.example.myapplication;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.compose.ComponentActivityKt;
import androidx.annotation.Nullable;
import androidx.compose.runtime.CompositionContext;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;

import androidx.activity.ComponentActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Goals extends ComponentActivity {
    LinearLayout goal_container;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    Button save_goal;
    EditText weight_goal;
    EditText goal_date;
    public static void printVariableType(Object variable) {
        if (variable == null) {
            System.out.println("null");
        } else {
            // Get the class of the object and its simple name
            Class<?> clazz = variable.getClass();
            String typeName = clazz.getSimpleName();

            // Special handling for arrays
            if (clazz.isArray()) {
                typeName = clazz.getComponentType().getSimpleName() + "[]";
            }

            System.out.println("Variable type: " + typeName);
        }
    }
    DBHelper DB = new DBHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        save_goal = findViewById(R.id.save_goal);
        weight_goal = findViewById(R.id.weight_goal);
        goal_date = findViewById(R.id.goal_date);
        goal_container = findViewById(R.id.goal_container);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(USERNAME, null);
    //    String password = sharedPreferences.getString(PASSWORD, null);
        ArrayList<Pair<String, Double>> goalsList = DB.getGoals(username); // Replace getUsername() with the actual method to get the username

        save_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weightText = weight_goal.getText().toString().trim();
                String goalDate = goal_date.getText().toString().trim();

                if(weightText.isEmpty() || goalDate.isEmpty()){
                    Toast.makeText(Goals.this, "Please Enter Weight or Date" ,Toast.LENGTH_SHORT).show();
                    return;
                }
                double weight_goal = Double.parseDouble(weightText);
                boolean isInserted = DB.insertGoals(username, weight_goal, goalDate);

                if(isInserted){
                    Toast.makeText(Goals.this, "Goal Added", Toast.LENGTH_SHORT).show();
                    updateGoal();
                }else{
                    Toast.makeText(Goals.this, "Goal Not Added", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    private void updateGoal(){
        goal_container.removeAllViews();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(USERNAME, null);
        ArrayList<Pair<String, Double>> goalsList = DB.getGoals(username);
        for(Pair<String, Double> goal: goalsList){
            String goalDate = goal.first;
            Double goalValue = goal.second;

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setText("Goal: " + goalValue + ", Date: " + goalDate);
            textView.setTextSize(16);
            textView.setTextColor(Color.BLACK);
            goal_container.addView(textView);

        }

    }
}
