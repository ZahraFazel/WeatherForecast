package com.pac.weather;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Controller {
    private final String filePath = "res/data.txt";
    private static NotificationCenter notificationCenter;
    private static Controller controller;
    public DispatchQueue dispatchQueue = new DispatchQueue("Controller");
    private ArrayList<Weather> dailyForecast;
    private String timezone;
    private long time;

    private Controller() {
    }

    public static Controller getInstance(NotificationCenter notificationCenter) {
        if(controller == null) {
            controller = new Controller();
            Controller.notificationCenter = notificationCenter;
        }
        return controller;
    }

    private void writeDataToFile(){
        try {

            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
            objectOutputStream.writeObject(dailyForecast);
            objectOutputStream.close();
            file.close();
            System.err.println("Database was succesfully written to the file");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    ArrayList<Weather> readDataFromFile(){
        try {

            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileIn);
            ArrayList<Weather> obj = (ArrayList<Weather>)objectInputStream.readObject();
            objectInputStream.close();
            fileIn.close();
            System.err.println("Database has been read from the file");
            return obj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void makeRequest(final Context root, String url, final String parserKind) {
        RequestQueue queue = Volley.newRequestQueue(root);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (parserKind.equals("MapBox"))
                            parseMapBoxJson(response, root);
                        else parseSkyDarkJson(response, root);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("errorDark", error.getMessage());
            }
        });

        queue.add(stringRequest);
    }


    private void parseSkyDarkJson(String response, Context root) {
        try {
            DisplayWeatherActivity activity = (DisplayWeatherActivity) root;
            JSONObject jsonObjDaily = new JSONObject(response).getJSONObject("daily");
            JSONArray jsonArrDaily = jsonObjDaily.getJSONArray("data");
            time = Long.parseLong((new JSONObject(response)).getJSONObject("currently").getString("time"));
            timezone = (new JSONObject(response)).getString("timezone");

            for (int i = 0; i < jsonArrDaily.length(); i++)
            {
                JSONObject forecast = jsonArrDaily.getJSONObject(i);
                String summary = forecast.getString("summary");
                String humidity = forecast.getString("humidity");
                String pressure = forecast.getString("pressure");
                String windSpeed = forecast.getString("windSpeed");
                String icon = forecast.getString("icon");
                String sunrise = forecast.getString("sunriseTime");
                String sunset = forecast.getString("sunsetTime");
                String temperatureHigh = forecast.getString("temperatureHigh");
                String temperatureLow = forecast.getString("temperatureLow");

                Weather weather = new Weather(summary, humidity, pressure, windSpeed, icon, sunrise,
                        sunset, temperatureHigh, temperatureLow);
                dailyForecast.add(weather);
                if (i > 10)
                    break;
            }
            writeDataToFile();
            activity.update();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseMapBoxJson(String response, Context root) {
        try {
            MainActivity activity = (MainActivity) root;
            JSONObject jsonObj = new JSONObject(response);
            JSONArray cityArray = jsonObj.getJSONArray("features");
            activity.getCities().clear();
            for (int i = 0; i < cityArray.length(); i++) {
                JSONObject place = cityArray.getJSONObject(i);
                City city = new City(place.getString("text"), place.getString("place_name"),
                        place.getString("center"));
                activity.getCities().add(city);
            }
            activity.update();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void newDailyForecast(){
        dailyForecast = new ArrayList<>();
    }

    ArrayList<Weather> getDailyForecast()
    {
        return dailyForecast;
    }

    public long getTime()
    {
        return time;
    }

    public String getTimezone()
    {
        return timezone;
    }
}
