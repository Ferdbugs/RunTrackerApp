package com.example.runtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

public class Statistics extends AppCompatActivity {

    //Class to Show Runner's Statistics

    Run run;
    TextView BestRun,BestDistance,BestDistanceWeek,BestSpeedWeek,BestDistanceMonth,BestSpeedMonth,TotalDistance,AverageSpeed;       //Variable Declaration
    float bestSpeed,bestDistance,bestSpeedWeek,bestDistanceWeek,bestSpeedMonth,bestDistanceMonth,totalDistance,averageSpeed;
    long week,month,monthTime,oneDay,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {        //OnCreate Initialize all the variables for the Statistics
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        run = new Run();
        week = System.currentTimeMillis()-(7*24*3600*1000);
        oneDay=24*3600*1000;
        monthTime =30*oneDay;
        month = System.currentTimeMillis()-monthTime;
        BestRun = findViewById(R.id.fastestRun);
        BestDistance = findViewById(R.id.longestDistance);
        BestDistanceWeek = findViewById(R.id.longWeek);
        BestSpeedWeek = findViewById(R.id.FastWeek);
        BestDistanceMonth = findViewById(R.id.longMonth);
        BestSpeedMonth = findViewById(R.id.FastMonth);
        TotalDistance = findViewById(R.id.totalDistance);
        AverageSpeed = findViewById(R.id.avgSpeed);
        displayData();
    }

    public void displayData(){
        DBHandler dbhandler = new DBHandler(getApplicationContext());  //Uses DBHandler for setting the data to the Run model and adds the run model to the listview
        Cursor cursor = dbhandler.showList();
            while(cursor.moveToNext()){
                totalDistance = totalDistance + cursor.getFloat(1);
                time = time + cursor.getLong(2);
                if(bestDistance<cursor.getFloat(1)) {
                    bestDistance = (cursor.getFloat(1));
                }
                if(bestSpeed<cursor.getFloat(3)) {
                    bestSpeed = (cursor.getFloat(3));
                }
                if(week<cursor.getLong(9)){
                    if(bestDistanceWeek<cursor.getFloat(1)){
                        bestDistanceWeek = (cursor.getFloat(1));
                    }
                    if(bestSpeedWeek<cursor.getFloat(3)){
                        bestSpeedWeek = (cursor.getFloat(3));
                    }
                }
                if(month<cursor.getLong(9)){
                    if(bestDistanceMonth<cursor.getFloat(1)){
                        bestDistanceMonth = (cursor.getFloat(1));
                    }
                    if(bestSpeedMonth<cursor.getFloat(3)){
                        bestSpeedMonth = (cursor.getFloat(3));
                    }
                }
            }
        time=time/1000;
        averageSpeed = totalDistance/time;
        BestDistance.setText(String.format(Locale.ENGLISH,"%.02f m",bestDistance));
        BestRun.setText(String.format(Locale.ENGLISH,"%.02f m/s",bestSpeed));
        BestDistanceWeek.setText(String.format(Locale.ENGLISH,"%.02f m",bestDistanceWeek));
        BestSpeedWeek.setText(String.format(Locale.ENGLISH,"%.02f m/s",bestSpeedWeek));
        BestDistanceMonth.setText(String.format(Locale.ENGLISH,"%.02f m",bestDistanceMonth));
        BestSpeedMonth.setText(String.format(Locale.ENGLISH,"%.02f m/s",bestSpeedMonth));
        TotalDistance.setText(String.format(Locale.ENGLISH,"%.02f m",totalDistance));
        AverageSpeed.setText(String.format(Locale.ENGLISH,"%.02f m/s",averageSpeed));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent MainActivity = new Intent(Statistics.this, com.example.runtracker.MainActivity.class);
        startActivity(MainActivity);
    }
}

