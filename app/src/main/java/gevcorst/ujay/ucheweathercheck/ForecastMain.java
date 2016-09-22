package gevcorst.ujay.ucheweathercheck;

import android.app.Activity;
import android.app.FragmentManager;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class ForecastMain extends Fragment {


     private TextView cityText;
     private TextView condDescr;
     private TextView temp;
     private TextView press;
     private TextView windSpeed;
     private TextView windDeg;
     private TextView unitTemp;
     private TextView hum;
     private ImageView imgView;
     private static String forecastDaysNum = "5";
     private ViewPager pager;
     String city;
    private FragmentActivity myContext;

    public ForecastMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v=inflater.inflate(R.layout.fragment_forecast_main, container, false);
        CityName cityName= new CityName(getActivity());
        city= cityName.getCityName();
        String lang = "en";
        cityText = (TextView) v.findViewById(R.id.cityText);
       /* temp = (TextView) v.findViewById(R.id.temp);
        unitTemp = (TextView)v. findViewById(R.id.unittemp);
        unitTemp.setText("°C");
        condDescr = (TextView)v. findViewById(R.id.skydesc);*/
        pager = (ViewPager) v.findViewById(R.id.pager);
        imgView = (ImageView) v.findViewById(R.id.condIcon);

       // condDescr = (TextView) v.findViewById(R.id.condDescr);
       // hum = (TextView) v.findViewById(R.id.hum);
       // press = (TextView) v.findViewById(R.id.press);
        //windSpeed = (TextView) v.findViewById(R.id.windSpeed);
        //windDeg = (TextView) v.findViewById(R.id.windDeg);

        JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
        task1.execute(new String[]{city,lang, forecastDaysNum});
         return  v;
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }


    private class JSONForecastWeatherTask extends AsyncTask<String, Void, WeatherForecast> {
        @Override
        protected WeatherForecast doInBackground(String... params) {
            String data = ( (new WeatherHttpClient()).getForecastWeatherData(params[0], params[1], params[2]));
            WeatherForecast forecast = new WeatherForecast();
            try {
                forecast = JSONWeatherParse.getForecastWeather(data);
                System.out.println("Weather ["+forecast+"]");
// Let's retrieve the icon
//weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return forecast;
        }
        @Override
        protected void onPostExecute(WeatherForecast forecastWeather) {
            super.onPostExecute(forecastWeather);
            FragmentManager fragmentManager=getFragmentManager();
            DailyForecastPageAdapter adapter = new DailyForecastPageAdapter(Integer.parseInt(forecastDaysNum),
                    myContext.getSupportFragmentManager(), forecastWeather);
            pager.setAdapter(adapter);
        }
    }


}
