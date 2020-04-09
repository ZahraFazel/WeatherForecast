package com.pac.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
        try {
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
        }catch (Exception e){
            createToast(getString(R.string.offline_mode));
            controller.dispatchQueue.postRunnable(new Runnable() {
                @Override
                public void run() {
                    controller.readDataFromFile();
                }
            });
        }


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
                Date date = new Date(controller.getTime() * 1000);
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(controller.getTimezone()));
                cal.setTime(date);
                int dayWeek = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                for( Weather weather : controller.getDailyForecast() )
                {
                    day[i] = getWeekDayName(dayWeek) + ", " +
                            getMonthName(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.DAY_OF_MONTH);
                    summary[i] = weather.getSummery();
                    icon[i] = getResources().getIdentifier(weather.getIcon().replace('-','_')
                                                            ,"drawable", getPackageName());
                    windSpeed[i] = weather.getWindSpeed();
                    humidity[i] = weather.getHumidity();
                    pressure[i] = weather.getPressure();
                    high[i] = weather.getTemperatureHigh();
                    low[i] = weather.getTemperatureLow();
                    i++;
                    cal.add(Calendar.DATE, 1);
                    dayWeek = (dayWeek + 1) % 7;
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

    private static String getMonthName(int month)
    {
        switch (month)
        {
            case 0: return "January";
            case 1: return "February";
            case 2: return "March";
            case 3: return "April";
            case 4: return "May";
            case 5: return "June";
            case 6: return "July";
            case 7: return "August";
            case 8: return "September";
            case 9: return "October";
            case 10: return "November";
            case 11: return "December";
            default:return "";
        }
    }

    private static String getWeekDayName(int weekDay)
    {
        switch (weekDay)
        {
            case 0: return "Mon";
            case 1: return "Tue";
            case 2: return "Wed";
            case 3: return "Thu";
            case 4: return "Fri";
            case 5: return "Sat";
            case 6: return "Sun";
            default:return "";
        }
    }

    public void createToast(String msg){
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, msg, duration);
        toast.show();
    }
}
