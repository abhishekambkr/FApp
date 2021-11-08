package org.nic.fruits.fragment;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nic.fruits.pojo.ModelweatherDetails;
import org.nic.fruits.R;
import org.nic.fruits.adapter.ForecastRecyclerViewAdapter;
import org.nic.fruits.adapter.ViewPagerAdapter;
import org.nic.fruits.adapter.WeatherRecyclerAdapter;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.pojo.ModelForeCastDetails;
import org.nic.fruits.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;



//Programming by Harsha  for version 1.0 release
public class OneFragment extends Fragment{

    private ArrayList<ModelweatherDetails> modelweatherDetails;
    private ArrayList<ModelForeCastDetails> modelForeCastDetails;
    private TextView villageName;
    private TextView todayTemperature;
    private TextView todayRainFall;
    private TextView todayWind;
    private TextView district;
    private TextView todayHumidity;
    private TextView taluku;
    private TextView lastUpdate;
    private View appView;
    List<ModelForeCastDetails> foreCastArrayList;
    private WeatherRecyclerAdapter weatherRecyclerAdapter;
    private AppDatabase appDatabase;
    private Context mContext;
    private String farmerID;
    private ViewPagerAdapter viewPagerAdapter;
    private String keyValue;
    private String villageNameSelected;


    private RecyclerView recyclerView;
    private ForecastRecyclerViewAdapter adapter;
    private Toolbar toolbar;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        villageNameSelected = getArguments().getString("villageName");
        System.out.println("calling aa villageNameSelected " +villageNameSelected);
        foreCastArrayList = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_one, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());


        todayTemperature = (TextView)  getView().findViewById(R.id.todayTemperature);
        villageName = (TextView)  getView().findViewById(R.id.villageName);
        todayRainFall = (TextView)  getView().findViewById(R.id.todayRainFall);
        todayWind = (TextView)  getView().findViewById(R.id.todayWind);
        todayHumidity = (TextView)  getView().findViewById(R.id.todayHumidity);
        taluku = (TextView)  getView().findViewById(R.id.taluku);
        district = (TextView)  getView().findViewById(R.id.district);
        lastUpdate = (TextView)  getView().findViewById(R.id.lastUpdate);
        recyclerView = (RecyclerView)getView(). findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        setupViewModel();


    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getWeatherDetailsBasedOnVillageName(villageNameSelected).observe(this, new Observer<List<ModelweatherDetails>>() {
            @Override
            public void onChanged(@Nullable List<ModelweatherDetails> taskEntries) {
                for (int i = 0; i <= taskEntries.size() - 1; i++){
                    villageName.setText(taskEntries.get(i).getPanchayathName());
                    todayTemperature.setText(getResources().getString(R.string.temperature)+ " : " +taskEntries.get(i).getMinMaxTempetarure() +" °C");
                    todayRainFall.setText(getResources().getString(R.string.rain)+ " : " +taskEntries.get(i).getRainFall() +" mm");
                    todayHumidity.setText(getResources().getString(R.string.humidity)+ " : " +taskEntries.get(i).getMinMaxRh() +" %");
                    todayWind.setText(getResources().getString(R.string.wind_speed)+ " : " +taskEntries.get(i).getMinMaxWindSpeed() +" m/s");
                    taluku.setText(getResources().getString(R.string.taluk)+ " : " +taskEntries.get(i).getTaluk());
                    district.setText(getResources().getString(R.string.dist)+ " : " +taskEntries.get(i).getDistrict());
                    lastUpdate.setText(getResources().getString(R.string.last_update)+ " : " +taskEntries.get(i).getWeatherStationDate());
                }
            }
        });

        viewModel.getForeCastDetailsBasedOnVillageName(villageNameSelected).observe(this, new Observer<List<ModelForeCastDetails>>() {
            @Override
            public void onChanged(@Nullable List<ModelForeCastDetails> taskEntries) {
                foreCastArrayList.clear();
                foreCastArrayList.addAll(taskEntries);
//                foreCastArrayList =taskEntries;
                adapter = new ForecastRecyclerViewAdapter(getContext(), foreCastArrayList);
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

            }
        });
    }

}