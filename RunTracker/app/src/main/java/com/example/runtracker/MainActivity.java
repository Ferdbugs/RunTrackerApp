package com.example.runtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    Button Map, Progress, Stats;                                   //Main Activity buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {        //Initialize HomeScreen of the application
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map = findViewById(R.id.Map);
        Progress = findViewById(R.id.Progress);
        Stats = findViewById(R.id.Stats);

        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MapIntent = new Intent(MainActivity.this, MapsActivity.class);       //Redirects to MapsActivity
                startActivity(MapIntent);
                finish();
            }
        });

        Progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HistoryIntent = new Intent(MainActivity.this, RunHistoryActivity.class);     //Redirects to List of Runs
                startActivity(HistoryIntent);
                finish();
            }
        });

        Stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HistoryIntent = new Intent(MainActivity.this, Statistics.class);     //Redirects to List of Runs
                startActivity(HistoryIntent);
                finish();
            }
        });
    }
}
