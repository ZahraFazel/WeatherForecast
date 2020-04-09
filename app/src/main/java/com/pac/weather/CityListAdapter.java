package com.pac.weather;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CityListAdapter extends ArrayAdapter<String>
{
    private Activity context;
    private String[] mainTitle;
    private String[] subtitle;

    public CityListAdapter(Activity context, String[] mainTitle, String[] subtitle)
    {
        super(context, R.layout.city_list, mainTitle);
        this.context = context;
        this.mainTitle = mainTitle;
        this.subtitle = subtitle;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.city_list, null,true);
        TextView titleText = rowView.findViewById(R.id.title);
        TextView subtitleText = rowView.findViewById(R.id.subtitle);
        titleText.setText(mainTitle[position]);
        subtitleText.setText(subtitle[position]);
        return rowView;
    }
}