package com.pac.weather;

import android.content.Context;
import android.util.Log;
import android.view.View;

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
    private static NotificationCenter notificationCenter;
    private static Controller controller;
    public DispatchQueue dispatchQueue = new DispatchQueue("Controller");

    private Controller() {
    }

    public static Controller getInstance(NotificationCenter notificationCenter) {
        if (controller == null) {
            controller = new Controller();
            Controller.notificationCenter = notificationCenter;
        }
        return controller;
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
            DisplayWeahterActivity activity = (DisplayWeahterActivity) root;
            JSONObject jsonObj = new JSONObject(response).getJSONObject("daily");
            JSONArray jsonArr = jsonObj.getJSONArray("data");

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject forecast = jsonArr.getJSONObject(i);
                Weather weather = new Weather(forecast.getString("summary"),
                        forecast.getString("summary"),
                        forecast.getString("humidity"),
                        forecast.getString("pressure"),
                        forecast.getString("windSpeed"));
                activity.getDailyForecast().add(weather);
                if (i > 10)
                    break;
            }
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
                City city = new City(place.getString("place_name"), place.getString("center"));
                activity.getCities().add(city);
            }
            activity.update();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
