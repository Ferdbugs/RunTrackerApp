package com.example.runtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RunHistoryActivity extends AppCompatActivity {

    ListView runList;                                       //Declare variables for RunHistoryActivity
    TextView no_data;
    CustomAdapter adapter;

    ArrayList<Run> listItem = new ArrayList<>();

    //Activity to show the List of Runs by the user

    // Custom adapter for displaying Run Objects in the listView

    private class CustomAdapter extends ArrayAdapter<Run> {
        public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Run> objects) {
            super(context, resource, objects);
        }

        private class holdView {
            TextView display_distance, display_duration,display_speed, getDisplay_date;            //Initialize textViews
            ImageView delete;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            holdView holder;

            if (convertView == null) {
                holder = new holdView();
                convertView = getLayoutInflater().inflate(R.layout.simple_list_item, null);
                holder.display_distance = convertView.findViewById(R.id.displayDistance);
                holder.display_duration = convertView.findViewById(R.id.displayDuration);
                holder.display_speed = convertView.findViewById(R.id.Speed);
                holder.getDisplay_date = convertView.findViewById(R.id.Date);
                holder.delete = convertView.findViewById(R.id.delete);

                convertView.setTag(holder);
            } else {
                holder = (holdView) convertView.getTag();
            }

            final Run run = getItem(position);
            if(run!=null) {
                holder.display_distance.setText(String.format(Locale.ENGLISH, "%.02f meters",run.getDistance()));
                holder.display_duration.setText(String.format(Locale.ENGLISH, "%d sec",run.getDuration()/1000) );
                holder.display_speed.setText(String.format(Locale.ENGLISH, "%.02f m/s", run.getSpeed()));
                holder.getDisplay_date.setText(run.getDate());
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DBHandler handler = new DBHandler(getApplicationContext());
                        handler.deleteRun(run.getID());
                        listItem.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {                        //OnCreate for the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_history);

        runList = findViewById(R.id.runList);
        no_data = findViewById(R.id.no_data);

        displayData();

        runList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {          //OnClickListener for List Items
                Run selected = (Run)runList.getItemAtPosition(position);
                Intent intent = new Intent(RunHistoryActivity.this, RunHistoryViewActivity.class);
                intent.putExtra("RunID", selected.getID());
                startActivity(intent);
            }
        });

    }


    public void displayData(){
        DBHandler dbhandler = new DBHandler(getApplicationContext());  //Uses DBHandler for setting the data to the Run model and adds the run model to the listview
        Cursor cursor = dbhandler.showList();

        if(cursor.getCount() ==0){
            no_data.setVisibility(View.VISIBLE);
        } else {
            no_data.setVisibility(View.GONE);
            listItem.clear();
            while(cursor.moveToNext()){
                Run run = new Run();
                run.setID(cursor.getInt(0));
                run.setDistance(cursor.getFloat(1));
                run.setDuration(cursor.getLong(2));
                run.setSpeed(cursor.getFloat(3));
                run.setType(cursor.getString(4));
                run.setDate(cursor.getLong(9));

                listItem.add(run);
            }
            adapter = new CustomAdapter(RunHistoryActivity.this, R.layout.simple_list_item, listItem);
            runList.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent MainActivity = new Intent(this, com.example.runtracker.MainActivity.class);
        startActivity(MainActivity);
    }
}
