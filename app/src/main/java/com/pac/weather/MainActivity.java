package com.pac.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NotificationCenter.Observer
{
    private NotificationCenter notificationCenter = NotificationCenter.getInstance();
    private Controller controller = Controller.getInstance(notificationCenter);

    ArrayList<City> cities = new ArrayList<>();

    public ArrayList<City> getCities()
    {
        return cities;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        notificationCenter.register(this);

        setContentView(R.layout.activity_main);


        final EditText searchBox = findViewById(R.id.search_box);
        searchBox.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (searchBox.getText().length() != 0)
                {
                    controller.dispatchQueue.postRunnable(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            String url = getString(R.string.base_url) +
                                    searchBox.getText().toString() + getString(R.string.request_format) +
                                    getString(R.string.MapBox_token);
                            controller.makeRequest(MainActivity.this, url, "MapBox");
                        }
                    });
                }
            }


        });

        final ListView list = findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {
                sendMessageToDisplayActivity(cities.get(position).getCenter());
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
                String[] mainTitle = new String[cities.size()];
                String[] subtitle = new String[cities.size()];
                int i = 0;
                for( City city : cities )
                {
                    mainTitle[i] = city.getName();
                    subtitle[i] = city.getPlace();
                    i++;
                }
                CityListAdapter adapter = new CityListAdapter(MainActivity.this, mainTitle, subtitle);
                ListView list = findViewById(R.id.list);
                list.setAdapter(adapter);
            }
        });
    }

    protected void onDestroy()
    {
        super.onDestroy();
        notificationCenter.unRegister(this);
    }

    public void sendMessageToDisplayActivity(String center)
    {
        Intent intent = new Intent(this, DisplayWeahterActivity.class);
        String key = getString(R.string.msg_inflater_key);
        intent.putExtra(key, center);
        startActivity(intent);
    }

}
