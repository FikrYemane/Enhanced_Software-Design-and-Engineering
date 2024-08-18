/**
 * WeightHistory.java
 *
 * Version: 1.3
 * Author: Fikr Yemane
 * Date: 08/13/2024
 *
 * Description:
 * This class displays the history of weight entries for the user within the fitness tracking application.
 * It retrieves weight data from the database and dynamically updates the user interface to show all weight
 * entries along with the current date. It also includes a back button to navigate to the main menu.
 *
 * Key Features:
 * - Data Retrieval: Fetches weight entries from the database for the current user.
 * - Dynamic UI Update: Creates and displays `TextView` elements for each weight entry.
 * - Navigation: Provides a back button to return to the main menu activity.
 *
 * Time Complexity Considerations:
 * - Data Retrieval: O(n) - Retrieving weight data involves iterating over the list of weights, where `n` is the number of entries.
 * - UI Update: O(n) - Dynamically adding `TextView` elements for each weight entry involves iterating over the list of weights.
 *
 * Efficiency and Optimization:
 * - The method efficiently clears and updates the UI by removing existing views before adding new ones.
 * - Weight entries are displayed with the current date, ensuring relevant and up-to-date information.
 * - Uses linear time operations for data handling and UI updates, ensuring smooth performance for moderate-sized datasets.
 */


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
        // Clear all existing views from the weightContainer to prepare for new data
        weightContainer.removeAllViews();

        // Retrieve shared preferences to get stored username and password
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(USERNAME, null);
        String password = sharedPreferences.getString(PASSWORD, null);

        // Fetch the list of weights associated with the username from the database
        ArrayList<Double> importedList = db.getWeights(username);

        // Get the current date to be displayed alongside each weight
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        // Iterate through the list of weights and dynamically create TextViews to display them
        for (int i = 0; i < importedList.size(); i++) {
            // Create a string for the weight and date information
            String weightText = "Weight " + (i + 1) + ": " + importedList.get(i);
            String dateText = "Date: " + dateFormat.format(today);

            // Initialize a new TextView for displaying weight information
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

            // Add the newly created TextView to the weightContainer
            weightContainer.addView(textView);
        }
    }

}
