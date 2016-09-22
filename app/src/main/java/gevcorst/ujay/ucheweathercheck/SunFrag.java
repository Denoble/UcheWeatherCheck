package gevcorst.ujay.ucheweathercheck;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class SunFrag extends Fragment {
    private RelativeLayout sun_layout;
    private int hr, min, sec, time_now, peakTime, timeAvg, positionInterval;
    private float sunPostion;
    private ImageView sun;
    private Calendar date;
    public static int sunRiseTime, sunSetTime;
    public GetTimeHourMinuteSeconds forsunrise;
    public GetTimeHourMinuteSeconds forsunset;
    private String city;


    public SunFrag() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_sun, container, false);
        sun_layout = (RelativeLayout) v.findViewById(R.id.sun_layout);
        sun =(ImageView)v.findViewById(R.id.sun);
        date= Calendar.getInstance();
        CityName cityName= new CityName(getActivity());
        city= cityName.getCityName();
        // Figure out the current time and set the sunrise or sunset accordingly
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});


        return v;
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {


        @Override
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




            SimpleDateFormat sdfDateTime = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
            String newtime =  sdfDateTime.format(new Date(System.currentTimeMillis()));

            GetTimeHourMinuteSeconds secondsTime=new GetTimeHourMinuteSeconds(newtime);
            // Get sunrise and sunset times in HR,MIN AND SEC
            forsunrise = new GetTimeHourMinuteSeconds(weather.location.getDateFromLong(weather.location.getSunrise()));
            sunRiseTime = forsunrise.convertToSeconds();
            forsunset = new GetTimeHourMinuteSeconds(weather.location.getDateFromLong(weather.location.getSunset()));
            sunSetTime = forsunset.convertToSeconds();

            time_now=(secondsTime.convertToSeconds());
            timeAvg= (sunSetTime-sunRiseTime)/2;

            peakTime=sunRiseTime+timeAvg;

            float scale = getResources().getDisplayMetrics().density;

            if(time_now>=sunRiseTime && time_now<= peakTime){
                positionInterval= 50/((timeAvg)/3600);

                   sunPostion=66-((time_now-sunRiseTime)/3600)*positionInterval;






            }
        else if(time_now>peakTime && time_now<=sunSetTime){
                positionInterval= 50/((timeAvg)/3600);
            sunPostion=16+(((time_now-peakTime)/3600)*positionInterval);

        }
          else
                sunPostion=345.00f;
            //Load Animation
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(sun,
                    "y", 355.00f * scale, sunPostion * scale);
            AnimatorSet animSet = new AnimatorSet();
            animSet.play(anim2);
            animSet.setDuration(1000);
            animSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animSet.start();



        }


    }

}
