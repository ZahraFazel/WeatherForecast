package com.pac.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements NotificationCenter.Observer{
    private NotificationCenter notificationCenter = NotificationCenter.getInstance();
    private Controller controller = Controller.getInstance(notificationCenter);

    ArrayList<City> cities = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationCenter.register(this);

        setContentView(R.layout.activity_main);


        final EditText searchBox = findViewById(R.id.search_box);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchBox.getText().length() != 0){
                    controller.dispatchQueue.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            makeRequest(searchBox.getText().toString());
                        }
                    });
                }
            }
        });
        final TextView tempTextView = findViewById(R.id.temp_text_view);
        tempTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageToDisplayActivity(tempTextView.getText().toString());
            }
        });
    }

    private void makeRequest(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.base_url) + query + getString(R.string.request_format) + getString(R.string.token);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("error", error.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    private void parseJson(String response){
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray cityArray = jsonObj.getJSONArray("features");
            cities.clear();
            for (int i = 0; i < cityArray.length(); i++) {
                JSONObject place = cityArray.getJSONObject(i);
                City city = new City(place.getString("place_name"),place.getString("center"));
                cities.add(city);
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
                TextView textView = findViewById(R.id.temp_text_view);
                textView.setText(cities.get(0).getName());
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        notificationCenter.unRegister(this);
    }

    public void sendMessageToDisplayActivity(String cityName) {
        String center = "";

        for (City city : cities)
            if(city.getName().equals(cityName))
                center = city.getCenter();

        Intent intent = new Intent(this, DisplayWeahterActivity.class);
        String key = getString(R.string.msg_inflater_key);
        intent.putExtra(key, center);
        startActivity(intent);
    }

}
