package com.example.sneha.trails;

/**
 * Created by lucascassiano on 10/8/16.
 */

import android.graphics.Bitmap;

/**
 *
 * @author manish.s
 *
 */

public class TrackView {
    Bitmap highlight;
    int distance;
    int timesUsed;
    int id;
    public TrackView(int id, Bitmap image, int distance, int timesUsed) {
        super();
        this.id = id;
        this.highlight = image;
        this.distance = distance;
        this.timesUsed = timesUsed;
    }

    public Bitmap getImage(){
        return highlight;
    }

    public Bitmap getHighlightImage(){
        return  highlight;
    }

    public void setHighlightImage(Bitmap image){
        this.highlight = image;
    }

    public void setDistance(int distance){
        this.distance = distance;
    }

    public void setTimesUsed(int timesUsed){
        this.timesUsed = timesUsed;
    }

    public int getDistance(){
        return  distance;
    }

    public int getTimesUsed(){
        return  timesUsed;
    }

    public int getId(){return id;}

    public void setId(int id){
        this.id = id;
    }

}

