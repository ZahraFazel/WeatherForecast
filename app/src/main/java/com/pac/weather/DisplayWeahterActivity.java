package com.pac.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayWeahterActivity extends AppCompatActivity implements NotificationCenter.Observer {

    private NotificationCenter notificationCenter = NotificationCenter.getInstance();
    private Controller controller = Controller.getInstance(notificationCenter);

    private String center;
    private ArrayList<Weather> dailyForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationCenter.register(this);

        setContentView(R.layout.activity_display_weahter);
        dailyForecast = new ArrayList<>();

        Intent intent = getIntent();
        String msgKey = getString(R.string.msg_inflater_key);
        center = intent.getStringExtra(msgKey).replace("[", "").replace("]", "");
        controller.dispatchQueue.postRunnable(new Runnable() {
            @Override
            public void run() {
                makeRequest();
            }
        });
    }


    private void makeRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.DarkSky_baseUrl) + center;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("errorDark", error.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    private void parseJson(String response) {
        try {
            JSONObject jsonObj = new JSONObject(response).getJSONObject("daily");
            JSONArray jsonArr = jsonObj.getJSONArray("data");

            for (int i = 0; i < 10; i++) {
                JSONObject forecast = jsonArr.getJSONObject(i);
                Weather weather = new Weather(forecast.getString("summary"),
                        forecast.getString("summary"),
                        forecast.getString("humidity"),
                        forecast.getString("pressure"),
                        forecast.getString("windSpeed"));
                dailyForecast.add(weather);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        update();

    }

    @Override
    public void update() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //TODO: create recycler view for list representation
                TextView textView = findViewById(R.id.textView);
                textView.setText(dailyForecast.get(0).getSummery());
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        notificationCenter.unRegister(this);
    }
}
