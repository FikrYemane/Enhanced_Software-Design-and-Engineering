/**
 * Goals.java
 *
 * Version: 1.3
 * Author: Fikr Yemane
 * Date: 08/13/2024
 *
 * Description:
 * This class handles the goal-setting functionality within the fitness tracking application. It allows users to set
 * weight goals, stores them in a database, and displays them dynamically in the UI. The class interacts with shared
 * preferences for user sessions and with a database helper class for data storage and retrieval.
 *
 * Key Features:
 * - Goal Management: Enables users to set and store weight goals along with their target dates.
 * - Dynamic UI Updates: Dynamically displays the user's goals in the interface based on data retrieved from the database.
 * - Database Interaction: Efficiently stores and retrieves goal data using the DBHelper class.
 * - User Session Management: Uses shared preferences to maintain user session data across activities.
 *
 * Time Complexity Considerations:
 * - Goal Insertion: O(1) - Inserting a new goal into the database is a constant-time operation.
 * - UI Update: O(n) - Updating the UI with user goals involves iterating through the list of goals, where n is the number of goals.
 *
 * Efficiency and Optimization:
 * - The class is designed to efficiently handle goal setting and display, ensuring a smooth user experience.
 * - Proper error handling and validation ensure that only valid data is processed, reducing the risk of application errors.
 * - The dynamic creation of UI components based on the number of goals allows for flexible and scalable UI updates.
 */


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
    /**
     * Updates the Goal View
     *
     * This method is responsible for updating the user interface with the user's
     * goals fetched from the database. It clears any existing views in the
     * `goal_container`, retrieves the current user's goals, and dynamically creates
     * `TextView` elements to display each goal's value and associated date.
     *
     * Time Complexity: O(n)
     * - The method involves iterating over the list of goals retrieved from the database,
     *   where `n` is the number of goals. The complexity is linear relative to the
     *   number of goals.
     *
     * Efficiency:
     * - The method efficiently updates the UI by removing existing views before
     *   adding new ones, ensuring no redundant views are displayed.
     * - TextViews are created dynamically based on the number of goals, which allows
     *   for flexibility in displaying varying numbers of goals without pre-defining
     *   UI components.
     *
     * @return void
     */

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
