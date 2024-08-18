/**
 * HomeActivity.java
 *
 * Version: 1.3
 * Author: Fikr Yemane
 * Date: 08/13/2024
 *
 * Description:
 * This class serves as the home or main menu activity for the fitness tracking application. It provides users
 * with navigation options to access different features of the app, including adding new weight entries and
 * viewing weight history. The activity sets up button click listeners that initiate transitions to the
 * AddWeight and WeightHistory activities.
 *
 * Key Features:
 * - Navigation: Provides buttons for navigating to the AddWeight and WeightHistory activities.
 * - User Interaction: Handles user input through button clicks to transition between different app functionalities.
 *
 * Time Complexity Considerations:
 * - Button Click Handling: O(1) - Setting up click listeners and handling button clicks are constant-time operations.
 *
 * Efficiency and Optimization:
 * - The activity efficiently manages user interactions and transitions between different views with minimal overhead.
 * - The use of `Intent` objects for activity transitions ensures a clear and efficient navigation flow.
 * - Better looking UI for user to navigate through the options
 */


package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.Nullable;

public class HomeActivity extends ComponentActivity {
    Button add_weight;
    Button weight_history;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        add_weight = findViewById(R.id.add_weight);
        weight_history = findViewById(R.id.weight_history);


        add_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddWeight.class );
                startActivity(intent);
            }
        });
        weight_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, WeightHistory.class);
                startActivity(intent);
            }
        });




    }
}
