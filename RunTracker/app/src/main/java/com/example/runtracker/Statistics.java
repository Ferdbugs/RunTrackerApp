package com.example.runtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class Statistics extends AppCompatActivity {


    Run run;
    TextView BestRun,BestDistance;
    float bestSpeed,bestDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        run = new Run();
        BestRun = findViewById(R.id.BestRun);
        BestDistance = findViewById(R.id.LongestDistance);
        displayData();
    }

    public void displayData(){
        DBHandler dbhandler = new DBHandler(getApplicationContext(), DBHandler.TABLE_RUNNER, null ,2);  //Uses DBHandler for setting the data to the Run model and adds the run model to the listview
        Cursor cursor = dbhandler.showList();
            while(cursor.moveToNext()){
                if(bestDistance<cursor.getFloat(1)) {
                    bestDistance = (cursor.getFloat(1));
                }
                if(bestSpeed<cursor.getFloat(3)) {
                    bestSpeed = (cursor.getFloat(3));
                }
            }
        BestDistance.setText(String.format(Locale.ENGLISH,"%.02f m",bestDistance));
        BestRun.setText(String.format(Locale.ENGLISH,"%.02f m/s",bestSpeed));
        }
    }

