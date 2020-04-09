package com.pac.weather;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherListAdapter extends ArrayAdapter<String>
{

    private Activity context;
    private String[] mainTitle;
    private String[] subtitle;
    private Integer[] imgID;

    public WeatherListAdapter(Activity context, String[] mainTitle,String[] subtitle, Integer[] imgID)
    {
        super(context, R.layout.weather_list, mainTitle);
        this.context=context;
        this.mainTitle =mainTitle;
        this.subtitle=subtitle;
        this.imgID =imgID;
    }

    public View getView(int position,View view,ViewGroup parent)
    {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.weather_list, null,true);
        TextView titleText = rowView.findViewById(R.id.title);
        ImageView imageView = rowView.findViewById(R.id.icon);
        TextView subtitleText = rowView.findViewById(R.id.subtitle);
        titleText.setText(mainTitle[position]);
        imageView.setImageResource(imgID[position]);
        subtitleText.setText(subtitle[position]);
        return rowView;
    }

    public void setContext(Activity context)
    {
        this.context = context;
    }

    public void setMainTitle(String[] mainTitle)
    {
        this.mainTitle = mainTitle;
    }

    public void setSubtitle(String[] subtitle)
    {
        this.subtitle = subtitle;
    }

    public void setImgID(Integer[] imgID)
    {
        this.imgID = imgID;
    }
}