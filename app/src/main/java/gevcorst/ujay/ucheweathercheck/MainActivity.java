package gevcorst.ujay.ucheweathercheck;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.location.*;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends FragmentActivity {
    private TextView instruction;
    public String  conditionDescription,city;
    private final String snowcond="Snow";
    private final String raincond="Rain";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        CityName cityName= new CityName(this);
        city= cityName.getCityName();
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});
       LinearLayout layout=(LinearLayout)findViewById(R.id.layout);
        layout.setBackgroundColor(Color.BLACK);
        instruction = (TextView) findViewById(R.id.cityText);
       instruction.setText("Double tap the screen in order to get the temperature to display"+"\n"+"\n"+
                       "Triple tap in order to get the forecast."

       );
        layout.setOnTouchListener(new View.OnTouchListener() {
            Handler handler = new Handler();

            int numberOfTaps = 0;
            long lastTapTimeMs = 0;
            long touchDownMs = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownMs = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);

                        if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                            //it was not a tap

                            numberOfTaps = 0;
                            lastTapTimeMs = 0;
                            break;
                        }

                        if (numberOfTaps > 0
                                && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                            numberOfTaps += 1;
                        } else {
                            numberOfTaps = 1;
                        }

                        lastTapTimeMs = System.currentTimeMillis();

                        if (numberOfTaps == 3) {
                            Toast.makeText(MainActivity.this, "triple", Toast.LENGTH_SHORT).show();
                            //handle triple tap
                            instruction.setVisibility(View.INVISIBLE);
                            ForecastMain foremain= new ForecastMain();
                            FragmentManager fm= getFragmentManager();
                            FragmentTransaction ft=fm.beginTransaction();
                            ft.replace(R.id.layout,foremain);
                            ft.addToBackStack(null);
                            ft.commit();

                        } else if (numberOfTaps == 2) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //handle double tap
                                    Toast.makeText(MainActivity.this, "double", Toast.LENGTH_SHORT).show();
                                    instruction.setVisibility(View.INVISIBLE);
                                    Fragment temp = new Temperature();
                                    Fragment rain_anim= new Weather_Descrip_Rain();
                                    Fragment snow_anim = new Weather_Description();
                                    Fragment sunrise= new SunFrag();
                                    FragmentManager fm =getFragmentManager();
                                    FragmentTransaction ft=fm.beginTransaction();
                                    ft.replace(R.id.My_Container_1_ID,temp);

                                    ft.add(R.id.My_Container_3_ID,sunrise);
                                    if(conditionDescription.equals(snowcond))
                                        ft.add(R.id.My_Container_2_ID, snow_anim);
                                    else if(conditionDescription.equals(raincond))
                                        ft.add(R.id.My_Container_4_ID,rain_anim);
                                    ft.addToBackStack(null);
                                    ft.commit();



                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                }

                return true;
            }
        });

    }


    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            WeatherHttpClient weatherHttpClient = new WeatherHttpClient();
            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParse.getWeather(data);

                // Let's retrieve the icon

                weather.iconData = weatherHttpClient.getImage(weather.currentCondition.getIcon());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }
        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if (weather.currentCondition != null && weather.iconData.length > 0) {
                conditionDescription=weather.currentCondition.getCondition();
            }

        }
    }



}
