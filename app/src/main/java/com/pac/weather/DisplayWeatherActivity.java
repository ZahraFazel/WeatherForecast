package com.pac.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DisplayWeatherActivity extends AppCompatActivity implements NotificationCenter.Observer {

    private NotificationCenter notificationCenter = NotificationCenter.getInstance();
    private Controller controller = Controller.getInstance(notificationCenter);

    private String center;

    public String getCenter()
    {
        return center;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        notificationCenter.register(this);

        setContentView(R.layout.activity_weather);


        Intent intent = getIntent();
        String msgKey = getString(R.string.msg_inflater_key);
        center = intent.getStringExtra(msgKey).replace("[", "").replace("]", "");
        controller.newDailyForecast();
        controller.dispatchQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                String url = getString(R.string.DarkSky_baseUrl) + center;
                controller.makeRequest(DisplayWeatherActivity.this, url, "skyDark");
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
                String[] day = new String[controller.getDailyForecast().size()];
                String[] summary = new String[controller.getDailyForecast().size()];
                int[] icon = new int[controller.getDailyForecast().size()];
                String[] windSpeed = new String[controller.getDailyForecast().size()];;
                String[] humidity = new String[controller.getDailyForecast().size()];;
                String[] pressure = new String[controller.getDailyForecast().size()];
                String[] high = new String[controller.getDailyForecast().size()];
                String[] low = new String[controller.getDailyForecast().size()];
                int i = 0;
                for( Weather weather : controller.getDailyForecast() )
                {
                    Date date = new Date((controller.getTime() + i * 86400) * 1000);
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(controller.getTimezone()));
                    cal.setTime(date);
                    day[i] = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) + ", " + cal.get(Calendar.MONTH)
                        + " " + cal.get(Calendar.DAY_OF_MONTH);
                    summary[i] = weather.getSummery();
                    icon[i] = getResources().getIdentifier(weather.getIcon().replace('-','_')
                                                            ,"drawable", getPackageName());
                    windSpeed[i] = weather.getWindSpeed();
                    humidity[i] = weather.getHumidity();
                    pressure[i] = weather.getPressure();
                    high[i] = weather.getTemperatureHigh();
                    low[i] = weather.getTemperatureLow();
                    i++;
                }

                RelativeLayout relativeLayout = findViewById(R.id.relLayout);
                boolean isDay;
                if( controller.getTime() > controller.getDailyForecast().get(0).getSunrise() &&
                        controller.getTime() < controller.getDailyForecast().get(0).getSunset() )
                {
                    relativeLayout.setBackgroundResource(R.drawable.day);
                    isDay = true;
                }
                else
                {
                    relativeLayout.setBackgroundResource(R.drawable.night);
                    isDay = false;
                }
                WeatherListAdapter adapter = new WeatherListAdapter(DisplayWeatherActivity.this,
                        day, summary, icon, windSpeed, humidity, pressure, high, low, isDay);
                ListView list = findViewById(R.id.weatherList);
                list.setAdapter(adapter);

            }
        });
    }

    protected void onDestroy()
    {
        super.onDestroy();
        notificationCenter.unRegister(this);
    }
}
