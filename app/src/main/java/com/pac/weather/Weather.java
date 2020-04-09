package com.pac.weather;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Weather implements Serializable
{
    private String summery;
    private String humidity;
    private String pressure;
    private String windSpeed;
    private String icon;
    private int sunrise;
    private int sunset;
    private String  temperatureHigh;
    private String temperatureLow;

    public Weather(String summary, String humidity, String pressure, String windSpeed, String icon,
                   String sunrise, String sunset, String temperatureHigh, String temperatureLow)
    {
        DecimalFormat df = new DecimalFormat("0.00");
        this.summery = summary;
        this.icon = icon;
        this.sunrise = Integer.parseInt(sunrise);
        this.sunset = Integer.parseInt(sunset);
        this.temperatureHigh = df.format(Double.parseDouble(temperatureHigh)) + " °C";
        this.temperatureLow = df.format(Double.parseDouble(temperatureLow)) + " °C";
        this.humidity = df.format(Double.parseDouble(humidity)) + " %";
        this.pressure = df.format(Double.parseDouble(pressure)) + " hPa";
        this.windSpeed = Double.parseDouble(windSpeed) + " mPs";
    }

    public String getSummery()
    {
        return summery;
    }

    public String getHumidity()
    {
        return humidity;
    }

    public String getPressure()
    {
        return pressure;
    }

    public String getWindSpeed()
    {
        return windSpeed;
    }

    public String getIcon()
    {
        return icon;
    }

    public int getSunrise()
    {
        return sunrise;
    }

    public int getSunset()
    {
        return sunset;
    }

    public String getTemperatureHigh()
    {
        return temperatureHigh;
    }

    public String getTemperatureLow()
    {
        return temperatureLow;
    }

}
