/*
 * UserLogin.java
 *
 * Version: 1.0
 * Author: Fikr Yemane
 * Date: 04/15/2024
 *
 * This class manages the user registration and login process for the fitness tracking application.
 * It includes functionality for creating a new account by validating user input, checking for existing users,
 * and inserting new user data into the database. The class also handles navigation to the sign-in activity.
 */

package com.example.myapplication;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.compose.ComponentActivityKt;
import androidx.compose.runtime.CompositionContext;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.widget.Toast;

import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.Nullable;


public final class UserLogin extends ComponentActivity {

    EditText username, password, repassword;
    Button signup, signin;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        signup = findViewById(R.id.btnsignup);
        signin = findViewById(R.id.btnsignin);
        DB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if (user.equals("") || pass.equals("") || repass.equals("")) {
                    Toast.makeText(UserLogin.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.equals(repass)) {
                        boolean checkuser = DB.checkusername(user);
                        if (!checkuser) {
                            boolean insert = DB.insertData(user, pass);
                            if (insert) {
                                Toast.makeText(UserLogin.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UserLogin.this, HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(UserLogin.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UserLogin.this, "User already exists! Please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UserLogin.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLogin.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

        /*signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();


                if (user.equals("") || pass.equals("")) {
                    Toast.makeText(UserLogin.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                    Log.d("UserLogin", "Check user pass result: " + checkuserpass);
                    if (checkuserpass) {
                        Toast.makeText(UserLogin.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(UserLogin.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/

    }


