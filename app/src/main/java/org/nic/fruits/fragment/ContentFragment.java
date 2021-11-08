package org.nic.fruits.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.nic.fruits.R;


import org.nic.fruits.adapter.ForecastRecyclerViewAdapter;
import org.nic.fruits.pojo.ModelForeCastDetails;
import org.nic.fruits.pojo.ModelweatherDetails;

import java.util.ArrayList;
import java.util.List;



//Programming by Harsha  for version 1.0 release
public class ContentFragment extends Fragment {
    private static final String ARG_PARAM_WEATHER = "param1";
    private static final String ARG_PARAM_FORECAST = "param2";
    private List<ModelweatherDetails> modelweatherDetails = new ArrayList<>();
    private List<ModelForeCastDetails> modelForeCastDetails = new ArrayList<>();

    private TextView villageName;
    private TextView todayTemperature;
    private TextView todayRainFall;
    private TextView todayWind;
    private TextView district;
    private TextView todayHumidity;
    private TextView taluku;
    private TextView lastUpdate;
    private RecyclerView recyclerView;
    private ForecastRecyclerViewAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;

    private TextView titleTxtView;
    private static ContentFragment fragment;
    private boolean isVisible;
    private boolean isStarted;


    public static ContentFragment newInstance(List<ModelweatherDetails> modelweatherDetails, List<ModelForeCastDetails> foreCastArrayList) {


        System.out.println("calling bb ContentFragment modelweatherDetails  newInstance" + modelweatherDetails.size()+" "+foreCastArrayList.size());
        Bundle args = new Bundle();
        args.putParcelableArrayList("ARG_PARAM_WEATHER", (ArrayList<? extends Parcelable>) modelweatherDetails);
        args.putParcelableArrayList("ARG_PARAM_FORECAST", (ArrayList<? extends Parcelable>) foreCastArrayList);
         fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        modelweatherDetails =  bundle.getParcelableArrayList("ARG_PARAM_WEATHER");
        modelForeCastDetails =  bundle.getParcelableArrayList("ARG_PARAM_FORECAST");

        System.out.println("calling bb ContentFragment modelweatherDetails onCreate " + modelweatherDetails.size()+" "+modelForeCastDetails.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_content, container, false);



        todayTemperature = (TextView)  rootView.findViewById(R.id.todayTemperature);
        villageName = (TextView)  rootView.findViewById(R.id.villageName);
        todayRainFall = (TextView)  rootView.findViewById(R.id.todayRainFall);
        todayWind = (TextView)  rootView.findViewById(R.id.todayWind);
        todayHumidity = (TextView) rootView.findViewById(R.id.todayHumidity);
        taluku = (TextView)  rootView.findViewById(R.id.taluku);
        district = (TextView)  rootView.findViewById(R.id.district);
        lastUpdate = (TextView)  rootView.findViewById(R.id.lastUpdate);
        recyclerView = (RecyclerView) rootView. findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
     /*   titleTxtView = (TextView)myView.findViewById(R.id.title_txtView);
        if(modelweatherDetails != null){
                for(int i =0;i<= modelweatherDetails.size() - 1;i++){
                    titleTxtView.setText(modelweatherDetails.get(i).getPanchayathName());
                }
        }*/
        if (modelweatherDetails != null && !modelweatherDetails.isEmpty() && !modelweatherDetails.equals("null") && !modelweatherDetails.equals("")){
            setupViewModel();
        }

        return rootView;
    }

   /* @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
        if (isVisible)
            setupViewModel(); //your request method
    }

    @Override
    public void onStop() {
        super.onStop();
        isStarted = false;
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible) {
            //   load data here
            if (modelweatherDetails != null && !modelweatherDetails.isEmpty() && !modelweatherDetails.equals("null") && !modelweatherDetails.equals("")){
                setupViewModel();
            }
//            setupViewModel();
        }else {
            // fragment is no longer visible
        }
    }*/



    private void setupViewModel() {
                for (int i = 0; i <= modelweatherDetails.size() - 1; i++){
                    villageName.setText(modelweatherDetails.get(i).getPanchayathName());
                    todayTemperature.setText(getResources().getString(R.string.temperature)+ " : " +modelweatherDetails.get(i).getMinMaxTempetarure() +" °C");
                    todayRainFall.setText(getResources().getString(R.string.rain)+ " : " +modelweatherDetails.get(i).getRainFall() +" mm");
                    todayHumidity.setText(getResources().getString(R.string.humidity)+ " : " +modelweatherDetails.get(i).getMinMaxRh() +" %");
                    todayWind.setText(getResources().getString(R.string.wind_speed)+ " : " +modelweatherDetails.get(i).getMinMaxWindSpeed() +" m/s");
                    taluku.setText(getResources().getString(R.string.taluk)+ " : " +modelweatherDetails.get(i).getTaluk());
                    district.setText(getResources().getString(R.string.dist)+ " : " +modelweatherDetails.get(i).getDistrict());
                    lastUpdate.setText(getResources().getString(R.string.last_update)+ " : " +modelweatherDetails.get(i).getWeatherStationDate());

                    adapter = new ForecastRecyclerViewAdapter(getContext(), modelForeCastDetails);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }

}