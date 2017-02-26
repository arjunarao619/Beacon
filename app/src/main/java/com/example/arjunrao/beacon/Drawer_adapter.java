package com.example.arjunrao.beacon;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Arjun Rao on 2/21/2017.
 */

public class Drawer_adapter extends ArrayAdapter<Navigation_Drawer> {

    Context context;
    int layoutResourceId;
    Navigation_Drawer data[] = null;

    public Drawer_adapter(Context context, int layoutResourceId, Navigation_Drawer[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new WeatherHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }

        Navigation_Drawer navigation_drawer = data[position];
        holder.txtTitle.setText(navigation_drawer.title);
        holder.imgIcon.setImageResource(navigation_drawer.icon);

        return row;
    }

    static class WeatherHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}