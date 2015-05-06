package com.example.kurtiscc.gasmileage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kurtiscc on 1/29/2015.
 */
public class HistoryRowAdapter extends ArrayAdapter<Gas> {
    private Context context;
    private int resource;
    private ArrayList<Gas> Gas = new ArrayList<Gas>();

    public HistoryRowAdapter(Context context, int resource, ArrayList<Gas> Gas) {
        super(context, resource, Gas);
        this.context = context;
        this.resource = resource;
        this.Gas = Gas;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GasDataHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder = new GasDataHolder();
            holder.date = (TextView) row.findViewById(R.id.list_date);
            holder.odometer = (TextView) row.findViewById(R.id.list_odometer);
            holder.gallons = (TextView) row.findViewById(R.id.list_gallons);
            holder.price = (TextView) row.findViewById(R.id.list_price);
            holder.location = (TextView) row.findViewById(R.id.list_location);
            holder.lat = (TextView) row.findViewById(R.id.list_lat);
            holder.lng = (TextView) row.findViewById(R.id.list_lng);


            row.setTag(holder);
        }
        else
        {
            holder = (GasDataHolder)row.getTag();
        }

        Gas gas = Gas.get(position);
        holder.date.setText(gas.getDate());
        holder.odometer.setText("Odometer: " + String.valueOf(gas.getOdometer()));
        holder.gallons.setText("Total Gallons: " + String.valueOf(gas.getGallons()));
        holder.price.setText("Total Cost: $" + String.valueOf(gas.getPrice()));
        holder.location.setText("Location: " + gas.getLocation());
        holder.lat.setText("Latitude: " + String.valueOf(gas.getLat()));
        holder.lng.setText("Longitude: " + String.valueOf(gas.getLng()));

        return row;
    }
    static class GasDataHolder{
        TextView date, location, odometer, price, gallons, lat, lng;
    }
}


