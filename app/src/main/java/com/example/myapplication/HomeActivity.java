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
