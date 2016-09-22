package gevcorst.ujay.ucheweathercheck;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class Weather_Descrip_Rain extends Fragment {

    private RelativeLayout weather_description_rain_layout;

    public Weather_Descrip_Rain() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weather__descrip__rain, container, false);
        RainFallView rainFallView = new RainFallView(getActivity());
        RelativeLayout.LayoutParams lprams=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        rainFallView.setLayoutParams(lprams);
        rainFallView.setBackgroundResource(R.drawable.rainfall);
        weather_description_rain_layout=(RelativeLayout)v.findViewById(R.id.description_anim_layout);

        weather_description_rain_layout.addView(rainFallView);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event

}
