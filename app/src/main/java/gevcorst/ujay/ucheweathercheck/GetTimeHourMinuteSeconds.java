package gevcorst.ujay.ucheweathercheck;

/**
 * Created by Ujay on 1/10/2015.
 */

// Get sunrisetime and sunset time in hour, minutes and seconds
public class GetTimeHourMinuteSeconds {
 public int hr,min,sec;
    public GetTimeHourMinuteSeconds(String string){
        if(string !=null){
            hr=Integer.valueOf(string.substring(0,2));
            min=Integer.valueOf(string.substring(3,5));
            sec=Integer.valueOf(string.substring(6));
        }
    }
    //  Convert all hour and minutes to seconds
    public  int convertToSeconds(){
        return hr*3600+min*60+sec;
    }
}
