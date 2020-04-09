package com.pac.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayWeahterActivity extends AppCompatActivity implements NotificationCenter.Observer {

    private NotificationCenter notificationCenter = NotificationCenter.getInstance();
    private Controller controller = Controller.getInstance(notificationCenter);

    private String center;
    private ArrayList<Weather> dailyForecast;

    public String getCenter()
    {
        return center;
    }

    public ArrayList<Weather> getDailyForecast()
    {
        return dailyForecast;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        notificationCenter.register(this);

        setContentView(R.layout.weather_list);
        dailyForecast = new ArrayList<>();

        Intent intent = getIntent();
        String msgKey = getString(R.string.msg_inflater_key);
        center = intent.getStringExtra(msgKey).replace("[", "").replace("]", "");
        controller.dispatchQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                String url = getString(R.string.DarkSky_baseUrl) + center;
                controller.makeRequest(DisplayWeahterActivity.this, url, "skyDark");
            }
        });

    }


    @Override
    public void update()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                //TODO: create recycler view for list representation
//                TextView textView = findViewById(R.id.textView);
//                textView.setText(dailyForecast.get(0).getSummery());
                System.out.println(dailyForecast.get(0).getSummery());
                System.out.println(dailyForecast.get(0).getWindSpeed());
                System.out.println(dailyForecast.get(0).getHumidity());
                System.out.println(dailyForecast.get(0).getPressure());
                System.out.println(dailyForecast.get(0).getTemperature());;
            }
        });
    }

    protected void onDestroy()
    {
        super.onDestroy();
        notificationCenter.unRegister(this);
    }
}
