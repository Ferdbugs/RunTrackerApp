package com.example.runtracker;

import androidx.fragment.app.FragmentActivity;

import android.content.ComponentName;

import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.example.runtracker.RunService.State;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,RunService.ServiceListener {

    private GoogleMap mMap;                                                 //Declare Variables for the activity
    private Polyline polyline;

    public Location FirstLocation;
    public Location StartRun;
    public float Distance;

    private TextView distanceRan;
    private Button Start;
    private Button Pause;
    private Button Reset;
    private Button Resume;

    TextView Timer;

    private RunService runService;
    boolean isBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {                   //Initialize variables onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment!=null){mapFragment.getMapAsync(this);}

        distanceRan = findViewById(R.id.Distance);
        Timer = findViewById(R.id.Timer);
        Start = findViewById(R.id.Start);
        Pause = findViewById(R.id.Stop);
        Pause.setVisibility(View.INVISIBLE);
        Reset = findViewById(R.id.Reset);
        Reset.setVisibility(View.INVISIBLE);
        Resume = findViewById(R.id.Resume);
        Resume.setVisibility(View.INVISIBLE);

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                               //OnClickListener for Start Button
                runService.StartTracking();
                UpdateUI();
            }
        });

        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                               //OnClickListener for Pause button
                runService.PauseTracking();
                UpdateUI();
            }
        });

        Resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runService.ResumeTracking();
                UpdateUI();
            }
        });

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                //End run and save details to the database
                runService.saveRun();
                Toast.makeText(MapsActivity.this, "Run added", Toast.LENGTH_SHORT).show();             //Make toast to show add run
                Distance = 0;
                String DistanceReset = "  KM";
                distanceRan.setText(DistanceReset);
                StartRun = null;
                runService.changeRunState(false);
                Timer.setText(R.string.ZeroClock);
                finish();
                Intent intent = new Intent(MapsActivity.this, RunHistoryActivity.class);
                startActivity(intent);
            }
        });


    }

    private ServiceConnection myConnection = new ServiceConnection() {                  //Create new service
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RunService.MyLocalBinder binder = (RunService.MyLocalBinder) service;
            runService = binder.getService();
            isBound = true;
            runService.addListener(MapsActivity.this);
            runService.startLocationUpdates();
            if(runService.CurrentState!=State.Stopped){
                polyline.setPoints(runService.points);
                UpdateDistance(runService.Distance);
                UpdateTime(runService.MillisecondTime+runService.TimeBuff);
            }
            UpdateUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }

    };



    @Override
    public void onMapReady(GoogleMap googleMap) {                                       //OnMapReady initialize the map with co-ordinates and set polylines on user travelled paths
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(6);
        polyline = mMap.addPolyline(polylineOptions);

        Intent intent = new Intent(this, RunService.class);                //Start Service
        if(bindService(intent, myConnection,0)){
            startService(intent);
        }
    }

    private void UpdateUI(){
        if(runService.CurrentState==State.Stopped){
            Start.setEnabled(true);
            Pause.setVisibility(View.VISIBLE);
            Resume.setVisibility(View.INVISIBLE);
            Reset.setVisibility(View.INVISIBLE);
        }
        if(runService.CurrentState==State.Running){
            Start.setEnabled(false);
            Pause.setVisibility(View.VISIBLE);
            Resume.setVisibility(View.INVISIBLE);
            Reset.setVisibility(View.VISIBLE);
        }
        if(runService.CurrentState==State.Paused){
            Start.setEnabled(false);
            Pause.setVisibility(View.INVISIBLE);
            Resume.setVisibility(View.VISIBLE);
            Reset.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {                                                         //Starts location updates onResume
        super.onResume();
    }


    @Override
    public void UpdateSpeed(float Speed) {

    }

    @Override
    public void UpdateDistance(float distance) {
        distanceRan.setText(String.format(Locale.ENGLISH,"%.2f m",distance));
    }

    @Override
    public void UpdateTime(long UpdateTime) {
        int Seconds = (int) (UpdateTime / 1000);

        int Minutes = Seconds / 60;

        Seconds = Seconds % 60;

        String Time = "" + String.format(Locale.ENGLISH,"%02d:%02d", Minutes,Seconds);

        Timer.setText(Time);
    }

    @Override
    public void UpdateLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(FirstLocation==null){
            FirstLocation = location;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18.2f));
        }
        if(runService.CurrentState== State.Running) {
            List<LatLng> points = polyline.getPoints();
            points.add(latLng);
            polyline.setPoints(points);
        }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18.2f));
    }
}
