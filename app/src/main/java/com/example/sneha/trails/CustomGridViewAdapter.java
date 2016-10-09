package com.example.sneha.trails;

/**
 * Created by lucascassiano on 10/8/16.
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class CustomGridViewAdapter extends ArrayAdapter<TrackView> {
    Context context;
    int layoutResourceId;
    ArrayList<TrackView> data = new ArrayList<TrackView>();

    public CustomGridViewAdapter(Context context, int layoutResourceId,
                                 ArrayList<TrackView> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.path_distance);
            //Set Image here
            holder.imageItem = (RoundedImageView) row.findViewById(R.id.item_image);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        TrackView item = data.get(position);
        holder.txtTitle.setText(item.getDistance()+"m");
        holder.imageItem.setImageBitmap(item.getImage());

        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), SendDataActivity.class);
                Log.i("Button Position", "Track "+  position + " Selected");
                SavedPaths.getInstance().setSelectedPath(position);
                getContext().startActivity(intent);
                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
            }
        });
            return row;

    }

    static class RecordHolder {
        TextView txtTitle;
        RoundedImageView imageItem;
    }
}