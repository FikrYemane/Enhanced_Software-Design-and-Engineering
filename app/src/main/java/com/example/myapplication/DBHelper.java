package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    public boolean insertWeightData(String username, double weight) {
        if (username == null || username.isEmpty() || Double.isNaN(weight)) {
            Log.e("DBHelper", "Invalid input data for insertWeightData");
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("weight", weight);
        contentValues.put("timestamp", System.currentTimeMillis()); // Add the timestamp

        long result = db.insert("weights", null, contentValues);
        return result != -1;
    }
    public boolean insertGoals(String username, double goal ,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("goal", goal);
        contentValues.put("date",  date);
        long result = db.insert("goals", null, contentValues);
        return  result != -1;
    }

    public ArrayList<Pair<String, Double>> getGoals(String username) {
        ArrayList<Pair<String, Double>> goalsList = new ArrayList<>();
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT date, goal FROM goals WHERE username = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") double goal = cursor.getDouble(cursor.getColumnIndex("goal"));
                Pair<String, Double> goalPair = new Pair<>(date, goal);
                goalsList.add(goalPair);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return goalsList;
    }

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

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {

            return null;
        }

    }
}