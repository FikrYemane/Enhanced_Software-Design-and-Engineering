
 /**
 * AddWeight.java
 *
 * Version: 1.3
 * Author: Fikr Yemane
 * Date: 08/13/2024
 *
 * Description:
 * This class handles the addition of weight data in the fitness tracking application. It manages user input for weight,
 * interacts with the database to store weight entries, and provides feedback to the user. The class also facilitates
 * navigation back to the main menu.
 *
 * Key Features:
 * - Input Validation: Ensures the user inputs a valid weight before attempting to store it.
 * - Database Interaction: Inserts the weight into the database and retrieves all weight entries for the user.
 * - User Feedback: Provides real-time feedback on the success or failure of the weight entry process.
 * - Navigation: Allows users to return to the main menu after adding weight data.
 *
 * Time Complexity Considerations:
 * - Input Validation: O(1) - Checking if the input is empty is a constant-time operation.
 * - Parsing the Weight: O(1) - Parsing a string to a double is a constant-time operation.
 * - Database Insertion: O(1) - Insertion is typically a constant-time operation
 * - Weight Retrieval: O(n) - Retrieving all weights for a user involves iterating over the entries, where n is the number of entries.
 *
 * Efficiency and Optimization:
 * - The class is optimized for user experience by providing quick feedback and efficiently managing database interactions.
 * - Proper error handling ensures the robustness of the application's functionality, even in edge cases.
 */

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
        // Set click listener for the add weight button to insert weight data
        /**
         * Sets up the Add Weight Button Listener
         *
         * This method sets up an `OnClickListener` for the `add_weight_button`. When the button is
         * clicked, the method validates the user's weight input, inserts the weight into the
         * database, and provides feedback based on the success of the insertion. It also retrieves
         * and logs the user's weight data after the insertion attempt.
         *
         * Time Complexity:
         * - Input validation: O(1) - Checking if the input is empty is a constant-time operation.
         * - Parsing the weight: O(1) - Parsing a string to a double is a constant-time operation.
         * - Inserting into the database: O(1)the insertion will be a constant-time operation.
         * - Retrieving weights: O(n) - Retrieving all weights associated with the username involves
         *   iterating over the list of stored weights, where `n` is the number of entries.
         *
         * Efficiency:
         * - The method efficiently handles user input and provides immediate feedback, ensuring a
         *   smooth user experience.
         * - Proper error handling is implemented to account for empty inputs and insertion failures,
         *   maintaining robustness in the application's functionality.
         *
         * @param None
         * @return void
         */

        add_weight_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weightText = entered_weight.getText().toString().trim();

                // Validate that the weight input is not empty
                if (weightText.isEmpty()) {
                    Toast.makeText(AddWeight.this, "Please enter a weight", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Parse the entered weight and attempt to insert it into the database
                double enteredWeight = Double.parseDouble(weightText);
                boolean isInserted = DB.insertWeightData(username, enteredWeight);

                // Provide feedback to the user based on the result of the insertion
                if (isInserted) {
                    Toast.makeText(AddWeight.this, "Weight Added", Toast.LENGTH_SHORT).show();
                    ArrayList<Double> enteredWeights = DB.getWeights(username);
                    System.out.println(enteredWeights);
                } else {
                    Toast.makeText(AddWeight.this, "Failed to add weight", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

}
