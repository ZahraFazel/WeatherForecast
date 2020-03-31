package com.pac.weather;

public class City {
    private String name;
    private String center;

    public City(String name, String center) {
        this.name = name;
        this.center = center;
    }

    public String getName() {
        return name;
    }

    public String getCenter() {
        return center;
    }
}
