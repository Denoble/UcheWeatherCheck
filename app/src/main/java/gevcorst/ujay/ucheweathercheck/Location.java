package gevcorst.ujay.ucheweathercheck;

/**
 * Created by Ujay on 12/22/2014.
 */
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Location implements Serializable {
    private float longitude;
    private float latitude;
    private long sunset;
    private long sunrise;
    private String country;
    private String city;

    public float getLongitude() {
        return longitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
    public float getLatitude() {
        return latitude;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
    public long getSunset() {
        return sunset;
    }
    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
    public long getSunrise() {
        return sunrise;
    }
    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getDateFromLong(long input){

        String dtime = new SimpleDateFormat("HH:mm:ss").format(new Date (input*1000L));

         return dtime;

    }


}
