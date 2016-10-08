package com.example.sneha.trails;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;

public class TracksScreen extends AppCompatActivity {
    //http://www.androidhub4you.com/2013/07/custom-grid-view-example-in-android.html
    GridView gridView;
    ArrayList<TrackView> gridArray = new ArrayList<TrackView>();
    CustomGridViewAdapter customGridAdapter;
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Adding Tracks
        //Add Grids
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.background_city);

        gridArray.add(new TrackView(icon,100,10));
        gridArray.add(new TrackView(icon,120,10));
        gridArray.add(new TrackView(icon,100,10));
        gridArray.add(new TrackView(icon,100,10));
        gridArray.add(new TrackView(icon,100,10));
        gridArray.add(new TrackView(icon,100,10));
        gridArray.add(new TrackView(icon,100,10));
        gridArray.add(new TrackView(icon,100,10));
        gridArray.add(new TrackView(icon,100,10));
        gridArray.add(new TrackView(icon,120,10));
        gridArray.add(new TrackView(icon,100,10));
        gridArray.add(new TrackView(icon,100,10));
        gridArray.add(new TrackView(icon,100,10));
        gridArray.add(new TrackView(icon,100,10));
        gridArray.add(new TrackView(icon,100,10));
        gridArray.add(new TrackView(icon,100,10));

        gridView = (GridView) findViewById(R.id.gridView1);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
        gridView.setAdapter(customGridAdapter);
    }

}
