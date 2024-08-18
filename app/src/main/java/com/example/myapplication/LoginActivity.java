/**
 * LoginActivity.java
 *
 * Version: 1.3
 * Author: Fikr Yemane
 * Date: 08/13/2024
 *
 * Description:
 * This class handles user authentication for the fitness tracking application. It allows users to log in by
 * entering their credentials (username and password), validates these credentials against stored data in the
 * database, and manages user session through shared preferences. Upon successful login, it redirects users
 * to the main menu activity.
 *
 * Key Features:
 * - User Authentication: Validates user credentials against the database.
 * - User Session Management: Stores user session data in shared preferences for persistent login state.
 * - Navigation: Redirects to the main menu activity upon successful login.
 *
 * Time Complexity Considerations:
 * - Input Validation: O(1) - Checking if the input fields are empty is a constant-time operation.
 * - Credential Validation: O(1) - Validating credentials with the database typically involves a constant-time query.
 * - Shared Preferences Operations: O(1) - Storing and retrieving data from shared preferences are constant-time operations.
 *
 * Efficiency and Optimization:
 * - The activity efficiently handles user input and provides immediate feedback, ensuring a smooth login process.
 * - It uses shared preferences to manage session data efficiently, reducing the need for repetitive login actions.
 * - Proper error handling is implemented to manage invalid credentials and provide user feedback.
 */

package com.example.myapplication;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ComponentActivity {
    EditText username, password;
    Button btnlogin;
    DBHelper DB;
    public String exportUserName;
    private String user;
    private String pass;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        btnlogin = findViewById(R.id.btnsignin1);
        DB = new DBHelper(this);

        // Get shared preferences to store user session data
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // Set click listener for the login button
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();

                // Check if user input is empty and provide feedback
                if (user.equals("") || pass.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Validate user credentials with the database
                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                    if (checkuserpass) {
                        // Credentials are correct; proceed to the main menu
                        Toast.makeText(LoginActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        Intent menuIntent = new Intent(LoginActivity.this, Menu.class);
                        menuIntent.putExtra("username", user);
                        menuIntent.putExtra("password", pass);
                        startActivity(menuIntent);

                        // Save user session data in shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(USERNAME, user);
                        editor.putString(PASSWORD, pass);
                        editor.apply();

                    } else {
                        // Invalid credentials; show error message
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
        public String getUser() {
            return username.getText().toString();
        }
        public String getPass(){
            return pass;

        }
}
