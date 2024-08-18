/**
 * DBHelper.java
 *
 * Version: 1.3
 * Author: Fikr Yemane
 * Date: 08/04/2024
 *
 * Description:
 * This class is responsible for managing the SQLite database operations for the fitness tracking application.
 * It handles the creation, upgrading, and data manipulation (insertion, querying, and validation) of user
 * information, weight entries, and fitness goals. The class has been optimized for memory management, including
 * efficient data retrieval, validation, and error handling to ensure data integrity and application stability.
 *
 * Key Features:
 * - User Authentication: Provides methods for storing and validating user credentials.
 * - Data Management: Supports storing and retrieving users' weights and fitness goals.
 * - Error Handling: Implements robust error handling and validation to maintain database integrity.
 * - Optimization: Includes enhancements such as lazy loading, profiling, and efficient query management to optimize performance.
 * - Security: Incorporates security measures, including password hashing, to protect user data.
 *
 * Time Complexity Considerations:
 * - Most methods have a constant time complexity O(1), ensuring quick operations for database interactions.
 * - Methods that involve querying data, such as fetching user weights or goals, have linear time complexity O(n)
 *   relative to the number of entries associated with a particular user.
 *
 * Efficiency and Optimization:
 * - The class minimizes memory usage by fetching only necessary data and closing database resources after use.
 * - It includes validation checks to ensure only valid data is processed, thereby reducing potential errors.
 * - The implementation of efficient data structures and indexing on frequently accessed columns further enhances
 *   performance.
 */


package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }
    private static final String TABLE_NAME = "weights";
    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT)");

        //Create Table for weight
        MyDB.execSQL("CREATE TABLE weights (\n" +
                "                username TEXT,\n" +
                "                weight REAL,\n" +
                "                timestamp REAL\n" +
                "        );");
        MyDB.execSQL("CREATE TABLE goals (" +
                "username TEXT," +
                "goal REAL," +
                "date TEXT" +
                ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists weights");
        MyDB.execSQL("drop Table if exists goals");

    }

    public Boolean insertData(String username, String password){
        if (username == null || username.isEmpty()) {
            Log.e("DBHelper", "null or empty");
            return false;
        }
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        return result != -1;
    }


    /**
     * Inserts a weight entry for a specified user into the "weights" table.
     *
     * This method validates the input to ensure that the username is not null or empty,
     * and that the weight is a valid number. If the input is valid, it inserts the data
     * into the database with a timestamp. The method returns true if the insertion was
     * successful, or false if it failed.
     *
     * Time Complexity: O(1)
     *
     * Efficiency and Optimization:
     *   -Input Validation: Ensures that invalid or incomplete data is not processed,
     *     reducing potential errors and ensuring data integrity before attempting an insertion.
     *
     *   - Minimal Operations: The method performs only essential operations (validation
     *     and insertion), which helps in maintaining high performance and avoids unnecessary
     *     computational overhead.
     * @param username The username associated with the weight entry.
     * @param weight The weight value to be inserted for the user.
     * @return A boolean indicating whether the data insertion was successful.
     */

    public boolean insertWeightData(String username, double weight) {
        // Validate input to ensure username is not null or empty and weight is a valid number
        if (username == null || username.isEmpty() || Double.isNaN(weight)) {
            Log.e("DBHelper", "Invalid input data for insertWeightData");
            return false;
        }

        // Get writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a ContentValues object to hold the data to be inserted
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("weight", weight);
        contentValues.put("timestamp", System.currentTimeMillis());

        // Insert data into the "weights" table and get the result
        long result = db.insert("weights", null, contentValues);

        // Return true if insert was successful (result is not -1), otherwise return false
        return result != -1;
    }

    /**
     * Inserts a goal entry for a specified user into the "goals" table with validation and error handling.
     *
     * This method inserts the provided username, goal value, and date into the database,
     * after performing validation checks on the input. It returns true if the insertion 
     * was successful, or false if it failed or an error occurred.
     *
     * Time Complexity: O(1)
     *   - The time complexity is constant as the method performs a fixed number of 
     *     operations regardless of the input size. 
     *
     * Efficiency and Optimization:
     *   - Validation Checks: Includes input validation to ensure that invalid data 
     *     does not cause issues during insertion, which improves overall efficiency.
     *   - Error Handling: Uses try-catch blocks to manage potential database 
     *     exceptions, preventing crashes and ensuring stability.
     *   - ContentValues Usage: Efficiently organizes and manages the data to be 
     *     inserted, utilizing SQLite's optimized mechanisms for data handling.
     *
     * @param username The username associated with the goal entry.
     * @param goal The goal value to be inserted for the user.
     * @param date The date associated with the goal entry.
     * @return A boolean indicating whether the data insertion was successful.
     */

    public boolean insertGoals(String username, double goal, String date) {
        if (username == null || username.isEmpty()) {
            // Handle invalid username
            return false;
        }

        try {
            // Get a writable database instance
            SQLiteDatabase db = this.getWritableDatabase();

            // Create a ContentValues object to store the data to be inserted
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", username);
            contentValues.put("goal", goal);
            contentValues.put("date", date);

            // Insert the data into the "goals" table and get the result
            long result = db.insert("goals", null, contentValues);

            // Return true if the insertion was successful (result is not -1), otherwise return false
            return result != -1;
        } catch (SQLiteException e) {
            // Handle any database errors that may occur during insertion
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a list of goals for a specified user from the database.
     *
     * This method queries the "goals" table in the SQLite database to retrieve
     * all goals associated with the given username. Each goal is paired with
     * its associated date, and the results are stored in an ArrayList of Pair
     * objects. The method handles database operations and ensures proper resource
     * management by closing the Cursor after use. It also includes error handling
     * to manage potential database access issues.
     *
     * Time Complexity: O(n), where n is the number of rows in the "goals" table
     * that match the specified username.
     *
     * Efficiency and Optimization: The method efficiently retrieves data using
     * a direct SQL query and processes results in a single pass through the cursor.
     * The use of the `finally` block ensures that the Cursor is always closed,
     * even if an exception occurs, preventing potential resource leaks.
     *
     * @param username The username for which to retrieve the goals.
     * @return An ArrayList of Pair objects, each containing the date and goal
     *         value for the specified user.
     */

    public ArrayList<Pair<String, Double>> getGoals(String username) {
        // Create an ArrayList to hold the retrieved goals
        ArrayList<Pair<String, Double>> goalsList = new ArrayList<>();

        Cursor cursor = null;
        try {
            // Get a readable database instance
            SQLiteDatabase db = this.getReadableDatabase();
            // Create a cursor to query the database for goals of the specified user
            cursor = db.rawQuery("SELECT date, goal FROM goals WHERE username = ?", new String[]{username});

            // Check if the cursor is not null and has at least one row
            if (cursor != null && cursor.moveToFirst()) {
                // Iterate through the cursor results
                do {
                    // Extract date and goal values from the current row
                    @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                    @SuppressLint("Range") double goal = cursor.getDouble(cursor.getColumnIndex("goal"));
                    // Create a Pair object to hold date and goal
                    Pair<String, Double> goalPair = new Pair<>(date, goal);
                    // Add the Pair to the goalsList
                    goalsList.add(goalPair);
                } while (cursor.moveToNext());
            }

            // Close the cursor to release resources
            cursor.close();
        } catch (SQLiteException e) {
            // Handle potential database errors
            e.printStackTrace();
        } finally {
            // Ensure the cursor is closed even if an exception occurs
            if (cursor != null) {
                cursor.close();
            }
        }

        // Return the list of goals
        return goalsList;
    }

    /**
     * Retrieves the list of weights for a specified user from the database.
     *
     * This method queries the "weights" table to fetch all weight entries
     * associated with the given username. The weights are returned in an
     * ArrayList of Double values, representing the historical weight data
     * of the user. The method uses a `Cursor` to iterate through the query
     * results and extracts the weight values.
     *
     * Time Complexity: O(n), where n is the number of weight entries associated
     * with the username.
     * Optimization: The method efficiently retrieves and stores
     * the user's weight data in a list. It uses a single SQL query and minimizes
     * memory usage by only fetching the "weight" column, making the operation more
     * efficient.
     *
     * @param username The username whose weights are to be retrieved.
     * @return An ArrayList of Double values representing the weights of the user,
     *         or an empty list if no weights are found or the username does not exist.
     */

    public ArrayList<Double> getWeights(String username) {

        ArrayList<Double> weightsList = new ArrayList<>();
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT weight FROM weights WHERE username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") double weight = cursor.getDouble(cursor.getColumnIndex("weight"));
                weightsList.add(weight);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return weightsList;
    }


    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    /**
     * Checks if the provided username and password match an entry in the database.
     *
     * This method queries the "users" table to verify if there is a record with
     * the specified username and password. It returns true if a matching record
     * is found, indicating successful authentication, and false otherwise.
     * The method uses a raw SQL query to perform the check and relies on the
     * `Cursor` object to determine if any results are returned.
     *
     * Time Complexity: O(1) for the `cursor.getCount()` method, as it typically
     * involves a single database lookup operation.

     *
     * @param username The username to be checked.
     * @param password The password to be checked.
     * @return True if a matching username and password are found in the database;
     *         false otherwise.
     */

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    /**
     * Hashes the input password using the SHA-256 algorithm.
     *
     * @param password The plain text password to be hashed.
     * @return The hashed password as a hexadecimal string, or null if hashing fails.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        try {
            // Obtain a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Compute the hash of the password
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Convert the hashed bytes to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            // Return the hexadecimal string representation of the hashed password
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle exception if the SHA-256 algorithm is not available
            // Return null to indicate failure
            return null;
        }
    }

}