package gevcorst.ujay.ucheweathercheck;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class DayForecastFragment extends Fragment {
    private DayForecast dayForecast;
    private ImageView iconWeather;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView sunset;
    private TextView hum;
    private TextView windDeg;
    private TextView sunrise;
    private TextView sunsetdata;
    private TextView sunrisedata;
    public DayForecastFragment() {}
    public void setForecast(DayForecast dayForecast) {
        this.dayForecast = dayForecast;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_day_forecast, container, false);
        TextView tempView = (TextView) v.findViewById(R.id.tempForecast);
        TextView descView = (TextView) v.findViewById(R.id.skydescForecast);
        temp = (TextView) v.findViewById(R.id.temp);
        hum = (TextView) v.findViewById(R.id.hum);
        press = (TextView) v.findViewById(R.id.press);
        windSpeed = (TextView) v.findViewById(R.id.windSpeed);
        windDeg = (TextView) v.findViewById(R.id.windDeg);
        sunrise=(TextView)v.findViewById(R.id.sunrise);
        sunset=(TextView)v.findViewById(R.id.sunset);

        tempView.setText( "Low "+(int) (dayForecast.forecastTemp.min - 275.15)+"°C" +","+ "High " + (int) (dayForecast.forecastTemp.max - 275.15)+"°C" );
        descView.setText(dayForecast.weather.currentCondition.getDescr());
        press.setText(Float.toString(dayForecast.weather.currentCondition.getPressure())+ " hPa");
        hum.setText(Float.toString(dayForecast.weather.currentCondition.getHumidity())+ " %");

        iconWeather = (ImageView) v.findViewById(R.id.forcondIcon);

// Now we retrieve the weather icon
        JSONIconWeatherTask task = new JSONIconWeatherTask();
        task.execute(new String[]{dayForecast.weather.currentCondition.getIcon()});
        return v;
    }
    private class JSONIconWeatherTask extends AsyncTask<String, Void, byte[]> {
        @Override
        protected byte[] doInBackground(String... params) {
            byte[] data = null;
            try {
// Let's retrieve the icon
                data = ( (new WeatherHttpClient()).getImage(params[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }
        @Override
        protected void onPostExecute(byte[] data) {
            super.onPostExecute(data);
            if (data != null) {
                Bitmap img = BitmapFactory.decodeByteArray(data, 0, data.length);
                iconWeather.setImageBitmap(img);
            }
        }
    }
}