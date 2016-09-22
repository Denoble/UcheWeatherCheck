package gevcorst.ujay.ucheweathercheck;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.*;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

/**
 * Created by Ujay on 12/22/2014.
 */
public class CityName implements LocationListener {
    public Double lat,longi;
    public Location location;
    public String provider;
    public Context context;
    String city,state,country;
    public LocationManager locationManager;

   public CityName( Context context){

       if (android.os.Build.VERSION.SDK_INT > 9) {
           StrictMode.ThreadPolicy strictMode = new StrictMode.ThreadPolicy.Builder()
                   .permitAll().build();
           StrictMode.setThreadPolicy(strictMode);
       }
       this.context=context;
       Criteria criteria = new Criteria();
       locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
       provider = locationManager.getBestProvider(criteria, false);
       location = locationManager.getLastKnownLocation(provider);
       if(location!=null){
           lat=location.getLatitude();
           longi=location.getLongitude();
       }
       getAddress(lat,longi);
   }
    public String getAddress(Double latt,Double longg) {
        String address1 = "";
        String  address2 = "";
        String  city = "";
        String state = "";
        String country = "";
        String county = "";
        String  PIN = "";
        String full_address = "";
        try {

            JSONObject jsonObj =  getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" +latt + ","
                    + longg + "&sensor=true");
            String Status = jsonObj.getString("status");
            if (Status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero.getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);

                    if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                        if (Type.equalsIgnoreCase("street_number")) {
                            address1 = long_name + " ";
                        } else if (Type.equalsIgnoreCase("route")) {
                            address1 = address1 + long_name;
                        } else if (Type.equalsIgnoreCase("sublocality")) {
                            address2 = long_name;
                        } else if (Type.equalsIgnoreCase("locality")) {
                            // Address2 = Address2 + long_name + ", ";
                            city = long_name;

                        }
                        else if (Type.equalsIgnoreCase("administrative_area_level_3")) {
                            city = long_name;
                        }
                        else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            county = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            state = long_name;
                        } else if (Type.equalsIgnoreCase("country")) {
                            country = long_name;
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            PIN = long_name;
                        }

                    }
                    this.city=city;
                    this.state=state;
                    this.country=country;
                    full_address = address1  + ","+PIN+","+city +","+country;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return full_address;

    }
    public String getCityName(){
        return this.city+","+this.country;
    }
    public static JSONObject getJSONfromURL(String url) {

        // initialize
        InputStream is = null;
        String result = "";
        JSONObject jObject = null;

        // http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        // convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObject = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }

        return jObject;
    }
    @Override
    public void onLocationChanged(Location location) {
        lat = (double) (location.getLatitude());
        longi = (double) (location.getLongitude());

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
