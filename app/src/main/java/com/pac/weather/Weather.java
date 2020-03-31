package com.pac.weather;

public class Weather {
    private String summery;
    private String temperature;
    private String humidity;
    private String pressure;
    private String windSpeed;

    public Weather(String summary, String temperature, String humidity, String pressure, String windSpeed) {
        this.summery = summary;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
    }

    public String getSummery() {
        return summery;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getWindSpeed() {
        return windSpeed;
    }
}
