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
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RunHistoryActivity extends AppCompatActivity {

    ListView runList;                                       //Declare variables for RunHistoryActivity
    TextView no_data;
    CustomAdapter adapter;

    ArrayList<Run> listItem = new ArrayList<>();

    //Activity to show the List of Runs by the user

    // Custom adapter for displaying Run Objects in the listview

    private class CustomAdapter extends ArrayAdapter<Run> {
        public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Run> objects) {
            super(context, resource, objects);
        }

        private class holdView {
            TextView display_distance, display_duration;            //Initialize textViews
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            holdView holder;

            if (convertView == null) {
                holder = new holdView();
                convertView = getLayoutInflater().inflate(R.layout.simple_list_item, null);
                holder.display_distance = convertView.findViewById(R.id.displayDistance);
                holder.display_duration = convertView.findViewById(R.id.displayDuration);

                convertView.setTag(holder);
            } else {
                holder = (holdView) convertView.getTag();
            }

            Run run = getItem(position);
            holder.display_duration.setText(run.getDuration());
            holder.display_distance.setText(run.getDistance());

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
        DBHandler dbhandler = new DBHandler(getApplicationContext(), DBHandler.TABLE_RUNNER, null ,2);  //Uses DBHandler for setting the data to the Run model and adds the run model to the listview
        Cursor cursor = dbhandler.showList();

        if(cursor.getCount() ==0){
            no_data.setVisibility(View.VISIBLE);
        } else {
            no_data.setVisibility(View.GONE);
            listItem.clear();
            while(cursor.moveToNext()){
                Run run = new Run();
                run.setID(cursor.getInt(0));
                run.setDistance(cursor.getString(1));
                run.setDuration(cursor.getString(2));
                run.setSpeed(cursor.getString(3));
                run.setType(cursor.getString(4));
                run.setInitialLocationLAT(5);
                run.setInitialLocationLONG(6);
                run.setInitialLocationLAT(7);
                run.setInitialLocationLONG(8);

                listItem.add(run);
            }
            adapter = new CustomAdapter(RunHistoryActivity.this, R.layout.simple_list_item, listItem);
            runList.setAdapter(adapter);
        }
    }




}
