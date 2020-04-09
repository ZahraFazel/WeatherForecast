package com.pac.weather;

public class City {
    private String name;
    private String center;
    private String place;

    public City(String name, String place, String center)
    {
        this.name = name;
        this.center = center;
        this.place = place;
    }

    public String getName()
    {
        return name;
    }

    public String getCenter()
    {
        return center;
    }

    public String getPlace()
    {
        return place;
    }
}
