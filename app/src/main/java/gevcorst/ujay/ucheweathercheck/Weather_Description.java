package gevcorst.ujay.ucheweathercheck;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;



public class Weather_Description extends Fragment {

    private RelativeLayout weather_description_layout;



    public Weather_Description() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weather__description, container, false);
        SnowFallView snowFallView = new SnowFallView(getActivity());
        RelativeLayout.LayoutParams lprams=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
       // lprams.addRule(RelativeLayout.BELOW, layout.getId());

        snowFallView.setLayoutParams(lprams);


        snowFallView.setBackgroundResource(R.drawable.winter);

        weather_description_layout=(RelativeLayout)v.findViewById(R.id.description_anim_layout);

        weather_description_layout.addView(snowFallView);

        return v;
    }

}
