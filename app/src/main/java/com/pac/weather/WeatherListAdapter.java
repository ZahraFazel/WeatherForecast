package com.pac.weather;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherListAdapter extends ArrayAdapter<String>
{

    private Activity context;
    private String[] day;
    private String[] summary;
    private int[] icon;
    private String[] windSpeed;
    private String[] humidity;
    private String[] pressure;
    private String[] high;
    private String[] low;
    private boolean isDay;

    public WeatherListAdapter(Activity context, String[] day, String[] summary, int[] icon,
                              String[] windSpeed, String[] humidity, String[] pressure,
                              String[] high, String[] low, boolean isDay)
    {
        super(context, R.layout.weather_list, day);
        this.context = context;
        this.day = day;
        this.summary = summary;
        this.icon = icon;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.pressure = pressure;
        this.high = high;
        this.low = low;
        this.isDay = isDay;
    }

    public View getView(int position,View view,ViewGroup parent)
    {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.weather_list, null,true);
        TextView dayText = rowView.findViewById(R.id.date);
        ImageView iconImage = rowView.findViewById(R.id.icon);
        TextView summaryText = rowView.findViewById(R.id.summary);
        TextView windSpeedText = rowView.findViewById(R.id.wind);
        TextView humidityText = rowView.findViewById(R.id.humidity);
        TextView pressureText = rowView.findViewById(R.id.pressure);
        TextView highText = rowView.findViewById(R.id.high);
        TextView lowText = rowView.findViewById(R.id.low);

        dayText.setText(day[position]);
        iconImage.setImageResource(icon[position]);
        summaryText.setText(summary[position]);
        windSpeedText.setText(windSpeed[position]);
        humidityText.setText(humidity[position]);
        pressureText.setText(pressure[position]);
        highText.setText(high[position]);
        lowText.setText(low[position]);

        if(isDay)
        {
            dayText.setTextColor(Color.BLACK);
            summaryText.setTextColor(Color.BLACK);
            windSpeedText.setTextColor(Color.BLACK);
            humidityText.setTextColor(Color.BLACK);
            pressureText.setTextColor(Color.BLACK);
            highText.setTextColor(Color.BLACK);
            lowText.setTextColor(Color.BLACK);
        }
        else
        {
            dayText.setTextColor(Color.WHITE);
            summaryText.setTextColor(Color.WHITE);
            windSpeedText.setTextColor(Color.WHITE);
            humidityText.setTextColor(Color.WHITE);
            pressureText.setTextColor(Color.WHITE);
            highText.setTextColor(Color.WHITE);
            lowText.setTextColor(Color.WHITE);
        }
        return rowView;
    }

}