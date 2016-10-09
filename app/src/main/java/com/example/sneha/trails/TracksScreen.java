package com.example.sneha.trails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class TracksScreen extends AppCompatActivity {
    //http://www.androidhub4you.com/2013/07/custom-grid-view-example-in-android.html

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a new Class", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                AddNewTrack();
            }
        });


    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        Log.i("resume", "resumed");
        super.onResume();

        GridView gridView;
        ArrayList<TrackView> gridArray = new ArrayList<TrackView>();
        CustomGridViewAdapter customGridAdapter;
        //Refresh your stuff here
        //Adding Tracks
        //Add Grids
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.background_city);

        //Sneha, if you need to add stuff, add here:
        //gridArray.add(new TrackView(Bitmap icon, int pathDistance, int time of times it was followed by someone)
        if(!SavedPaths.getInstance().getPaths().isEmpty()){
            for(List<LocationData> l : SavedPaths.getInstance().getPaths()){
                int id = 0; //<-- Change it Sneha :)
                gridArray.add(new TrackView(id,icon,100,10));
            }
        }

        gridView = (GridView) findViewById(R.id.gridView1);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
        gridView.setAdapter(customGridAdapter);
    }

    private void AddNewTrack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
