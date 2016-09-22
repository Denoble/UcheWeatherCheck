package gevcorst.ujay.ucheweathercheck;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.*;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.graphics.Color;
import android.location.*;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;





public class Temperature extends Fragment {
    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView sunset;
    private TextView sunsetdata;
    private TextView sunrise;
    private TextView sunrisedata;
    private TextView windDeg;
    public static int sunRiseTime, sunSetTime;
    private TextView hum;
    private ImageView imgView;
    public GetTimeHourMinuteSeconds forsunrise;
    public GetTimeHourMinuteSeconds forsunset;
    private String city;
    private Context context;
    private RelativeLayout layout,sun_layout,weather_description_layout;



    public Temperature() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_temperature, container, false);



        CityName cityName= new CityName(getActivity());
        city= cityName.getCityName();
        cityText = (TextView) v.findViewById(R.id.cityText);
        condDescr = (TextView) v.findViewById(R.id.condDescr);
        temp = (TextView) v.findViewById(R.id.temp);
        hum = (TextView) v.findViewById(R.id.hum);
        press = (TextView) v.findViewById(R.id.press);
        windSpeed = (TextView) v.findViewById(R.id.windSpeed);
        windDeg = (TextView) v.findViewById(R.id.windDeg);
        imgView = (ImageView) v.findViewById(R.id.condIcon);
        sunrise=(TextView)v.findViewById(R.id.sunrise);
        sunrisedata=(TextView)v.findViewById(R.id.sunrisedata);
        sunset=(TextView)v.findViewById(R.id.sunset);
        sunsetdata=(TextView)v.findViewById(R.id.sunsetdata);

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});
        Toast.makeText(getActivity(),city,Toast.LENGTH_LONG).show();

        return v;
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
        Bitmap bitmap;

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

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgView.setMaxHeight(200);
                imgView.setMaxWidth(200);
                imgView.setImageBitmap(img);
            }


            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());

            condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");
            press.setText("" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
            windDeg.setText("" + weather.wind.getDeg());
            sunrisedata.setText(weather.location.getDateFromLong(weather.location.getSunrise()));
            sunsetdata.setText(weather.location.getDateFromLong(weather.location.getSunset()));
            // Get sunrise and sunset times in HR,MIN AND SEC
            forsunrise = new GetTimeHourMinuteSeconds(weather.location.getDateFromLong(weather.location.getSunrise()));
            sunRiseTime = forsunrise.convertToSeconds();
            forsunset = new GetTimeHourMinuteSeconds(weather.location.getDateFromLong(weather.location.getSunset()));
            sunSetTime = forsunset.convertToSeconds();

        }


    }
}
