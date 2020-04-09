package com.pac.weather;

import java.io.Serializable;
import java.util.ArrayList;

public class DataBase implements Serializable {
    private ArrayList<Weather> dailyForecast;
    private String timezone;
    private long time;

    public DataBase(ArrayList<Weather> dailyForecast, String timezone, long time) {
        this.dailyForecast = dailyForecast;
        this.timezone = timezone;
        this.time = time;
    }

    public ArrayList<Weather> getDailyForecast() {
        return dailyForecast;
    }

    public String getTimezone() {
        return timezone;
    }

    public long getTime() {
        return time;
    }
}
