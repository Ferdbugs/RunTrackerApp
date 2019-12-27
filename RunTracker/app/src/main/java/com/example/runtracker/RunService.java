package com.example.runtracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.NotificationCompat;


public class RunService extends Service {

    public static final String CHANNEL_ID = "RunChannel";

    private final IBinder RunBinder = new MyLocalBinder();
    NotificationCompat.Builder builder;
    NotificationManager manager;
    public float Distance;
    public long StartTime;
    public float bestSpeed;
    public float bestDistance;
    public long longestRun;

    public Location NewLocation;
    public Location StartLocation;
    public Location PreviousLocation;
    public List<LatLng> points = new ArrayList<>();

    Handler timerHandler = new Handler();
    long MillisecondTime, TimeBuff, UpdateTime = 0L ;
    public State CurrentState;
    public float speed;
    long time;

    public enum State{
        Running, Paused, Stopped;
    }

    FusedLocationProviderClient fusedLocationProviderClient;


    private ServiceListener listener;

    public void onCreate(){
        super.onCreate();
        createNotificationChannel();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);        //Initialize fusedLocationProvider for getting location updates on the Map
    }

    public void startLocationUpdates() {                                               //Call createLocationRequest
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {     //Location callback for updating distance
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    PreviousLocation = NewLocation;
                    NewLocation = location;
                    if(CurrentState == State.Running){
                        Distance = Distance + PreviousLocation.distanceTo(NewLocation);
                        listener.UpdateDistance(Distance);
                        points.add(new LatLng(location.getLatitude(),location.getLongitude()));

                    }
                    listener.UpdateLocation(location);
                }
            }
        };
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5);
        locationRequest.setFastestInterval(5);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //String content = mapsActivity.getState();

        final Intent notificationIntent = new Intent(this, MapsActivity.class);             //OnServiceStart
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("New Activity")
                .setSmallIcon(R.drawable.ic_run)
                .setContentIntent(pendingIntent);

        startForeground(1, builder.build());

        return START_NOT_STICKY;
    } // end of method onStartCommand()


    public void StopTime(){
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void StartTracking(){
        Distance = 0;
        StartLocation = NewLocation;
        PreviousLocation = NewLocation;
        StartTime = SystemClock.uptimeMillis();
        timerHandler.post(timerRunnable);
        CurrentState = State.Running;
    }

    public void PauseTracking(){
        CurrentState = State.Paused;
        timerHandler.removeCallbacks(timerRunnable);
        TimeBuff += MillisecondTime;
        MillisecondTime = 0;
    }

    public void ResumeTracking(){
        StartTime = SystemClock.uptimeMillis();
        timerHandler.post(timerRunnable);
        CurrentState = State.Running;
    }

    public void addListener(ServiceListener listener){
        this.listener = listener;
    }

    public Runnable timerRunnable = new Runnable() {                                      //Calculate duration of run

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            long UpdateTime = TimeBuff + MillisecondTime;

            listener.UpdateTime(UpdateTime);

            timerHandler.postDelayed(this, 500);
        }

    };


    public interface ServiceListener{
        void UpdateSpeed(float Speed);
        void UpdateDistance(float distance);
        void UpdateTime(long Time);
        void UpdateLocation(Location location);
    }




    private void createNotificationChannel(){                               //Create Notification
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "RunTracker Notification",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }

    public void changeRunState(boolean run){                                    //Change Run State for Notification
        if(run){
            manager = getSystemService(NotificationManager.class);
            manager.notify(1, builder.setContentText("Running").build());
        } else {
            manager.notify(1, builder.setContentText("Walking").build());
        }
    }

    public void saveRun(){
        time = (TimeBuff+MillisecondTime)/1000;
        speed = Distance/time;
        DBHandler dbHandler = new DBHandler(this, null, null, 1);                       //Initialize DBHandler
        Run run = new Run(Distance, TimeBuff + MillisecondTime,speed,StartLocation,NewLocation) ;
        dbHandler.addRun(run);                                                                                       //Call DBHandler add run method to add it to database

    }

    //Binding methods
    @Override
    public IBinder onBind(Intent intent) {
        return RunBinder;
    }

    public class MyLocalBinder extends Binder {
        RunService getService() {
            return RunService.this;
        }
    }

    //method to stop the service
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public float getRunDistance() {
        return Distance;
    }

    public void setRunDistance(Float distance) {
        Distance = distance;
    }

    public void setStartTime(long StartTime){
        this.StartTime = StartTime;
    }

    public long getStartTime() {
        return StartTime;
    }
}
