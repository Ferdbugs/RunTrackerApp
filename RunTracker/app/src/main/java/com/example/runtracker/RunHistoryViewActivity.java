package com.example.runtracker;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class RunHistoryViewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public int RunID;
    private LatLng latLngInit,latLngFinal;
    public Run run;
    Polyline polyline;

    TextView displayDuration, displayDistance,Speed,Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_history_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.displayMap);
        mapFragment.getMapAsync(this);

        RunID = getIntent().getIntExtra("RunID", 0);

        displayDistance = findViewById(R.id.display_distance);
        displayDuration = findViewById(R.id.display_duration);
        Speed = findViewById(R.id.Speed);
        Type = findViewById(R.id.Type);

        displayRunData();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(latLngInit).title("START"));
        mMap.addMarker(new MarkerOptions().position(latLngFinal).title("FINISH"));
        polyline = googleMap.addPolyline(new PolylineOptions().clickable(true)
        .add(
                new LatLng(latLngInit.latitude,latLngInit.longitude),
                new LatLng(latLngFinal.latitude,latLngFinal.longitude)
        )
        .color(Color.RED));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngInit));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngFinal,16.2f));
    }

    public void displayRunData(){
        DBHandler dbHandler = new DBHandler(this, DBHandler.TABLE_RUNNER, null, 1);
        run = dbHandler.findRun(RunID);
        displayDistance.setText(run.getDistance());
        displayDuration.setText(run.getDuration());
        String distance = run.getDistance();
        String duration = run.getDuration();
        String[] splitDistance = distance.split(" ");
        String[] splitTime = duration.split(":");
        Float Time = (Float.parseFloat(splitTime[0])+Float.parseFloat(splitTime[1])/60)/60;
        Float DistanceFloat = Float.parseFloat(splitDistance[0]);
        Float runningSpeed = DistanceFloat/Time;
        String runningSpeedFinal = String.format("%.02f",runningSpeed);
        String SpeedText = (runningSpeedFinal) + " Kmph";
        Speed.setText(SpeedText);
        if(runningSpeed>10){
            Type.setText("Running");
        }
        else{
            Type.setText("Walking");
        }
        latLngInit = new LatLng(run.getInitialLocationLAT(),run.getInitialLocationLONG());
        latLngFinal = new LatLng(run.getFinalLocationLAT(),run.getFinalLocationLONG());
    }
}
