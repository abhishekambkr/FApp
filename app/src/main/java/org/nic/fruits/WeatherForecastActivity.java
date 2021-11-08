package org.nic.fruits;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.Nullable;

import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.nic.fruits.R;
import com.google.android.material.tabs.TabLayout;

import org.nic.fruits.adapter.PagerAdapterNew;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;
import org.nic.fruits.fragment.ContentFragment;
import org.nic.fruits.pojo.ModelForeCastDetails;
import org.nic.fruits.pojo.ModelweatherDetails;
import org.nic.fruits.viewmodel.MainViewModel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



//Programming by Harsha  for version 1.0 release
public class WeatherForecastActivity extends AppCompatActivity {

    private Context mContext;
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapterNew adapter;
    private String farmerID;
    private String keyValue;
    private String villageName;
    private List<ModelweatherDetails> modelweatherDetails = new ArrayList<>();
    private List<ModelForeCastDetails> foreCastArrayList = new ArrayList<>();
    private AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast_new);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences("com.example.fruites", MODE_PRIVATE);
        farmerID = prefs.getString("FarmerID", "default_value_here_if_string_is_missing");
        keyValue = prefs.getString("KeyValue", "default_value_here_if_string_is_missing");
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapterNew(getSupportFragmentManager(), this);
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                viewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setUpViewModel();
    }

    private void setUpViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getWeatherDetailsBasedOnFid(farmerID).observe(this, new Observer<List<ModelweatherDetails>>() {
            @Override
            public void onChanged(@Nullable List<ModelweatherDetails> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")) {
                    modelweatherDetails = listToArrayList(taskEntries);
                    String villageName = null;
                    for (int i = 0; i < taskEntries.size(); i++) {
                        System.out.println("calling bb modelweatherDetails " + modelweatherDetails.size() + " " + modelweatherDetails.get(i).getPanchayathName());
                        villageName = modelweatherDetails.get(i).getPanchayathName();
                        setupViewModel(villageName);
                    }
                } else {
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.weatherforecast))

                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, FruitsHomeActivity.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            })
                            .show();
                }
            }
        });
    }

    private void setupViewModel(final String villageName) {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getWeatherDetailsBasedOnVillageName(villageName).observe(this, new Observer<List<ModelweatherDetails>>() {
            @Override
            public void onChanged(@Nullable List<ModelweatherDetails> taskEntries) {
                modelweatherDetails = taskEntries;
            }
        });

        viewModel.getForeCastDetailsBasedOnVillageName(villageName).observe(this, new Observer<List<ModelForeCastDetails>>() {
            @Override
            public void onChanged(@Nullable List<ModelForeCastDetails> taskEntries) {


                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    foreCastArrayList = taskEntries;
                    if (viewPager != null) {
                        adapter.addFragment(new ContentFragment().newInstance(modelweatherDetails, foreCastArrayList), villageName);
                        viewPager.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        tabLayout.setupWithViewPager(viewPager);
                        System.out.println("calling aa setupViewModel village modelweatherDetails " + modelweatherDetails.size() + " " + foreCastArrayList.size() + " " + villageName);
                    }

                } else {
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.weatherforecast))

                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, FruitsHomeActivity.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            })
                            .show();
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.findItem(R.id.spinner).setVisible(false);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (Build.VERSION.SDK_INT > 11) {
//            menu.findItem(R.id.logout).setVisible(false);
            invalidateOptionsMenu();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public static ArrayList<ModelweatherDetails> listToArrayList(List<ModelweatherDetails> myList) {
        ArrayList<ModelweatherDetails> arl = new ArrayList<ModelweatherDetails>();
        for (Object object : myList) {
            arl.add((ModelweatherDetails) object);
        }
        return arl;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.sync:
                if (Utils.isNetworkConnected(mContext)) {
                    new GetweatherData().execute();
                } else {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Alert")
                            .setMessage("Please Connect To Internet To Get Weather Status")

                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class GetweatherData extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please Wait Weather Details Downlading");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }


        @Override
        protected String doInBackground(String... arg0) {
            String weatherDataString = new String();
            try {
                SoapProxy proxy2 = new SoapProxy(mContext);
                weatherDataString = proxy2.getWeatherData(keyValue, farmerID);
                weatherDataString = weatherDataString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                String xmlFormatOfReturnXMLString2 = "<?xml version=" + "\"1.0\"" + "?>" + weatherDataString;
                System.out.println("calling aa  xml string weatherDataString " + xmlFormatOfReturnXMLString2);

                if (weatherDataString.equalsIgnoreCase("Failure")) {
                    weatherDataString = "InternetconectionProblem";
                } else if (weatherDataString.equalsIgnoreCase("Failure1")) {
                    weatherDataString = "InternetconectionProblem";
                } else if (weatherDataString.equalsIgnoreCase("anyType{}")) {
                    weatherDataString = "anyType{}";
                } else {
                    String parser = parseWeatherForeCastDeatails(weatherDataString);
                    weatherDataString = parser;
                }

            } catch (Exception e) {
                e.printStackTrace();
                weatherDataString = "InternetconectionProblem";
            }
            return weatherDataString;
        }

        @Override
        protected void onPostExecute(String weatherDataString) {
            dialog.dismiss();
            if (weatherDataString.equals("Success")) {
                Toast.makeText(mContext, "Weather Details Updated ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Problem while downloading weather details ", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public String parseWeatherForeCastDeatails(String weatherForecastString) {
        String Parsemessage = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            String s1 = URLDecoder.decode(weatherForecastString, "UTF-8");
            Document docxml = builder.parse(new ByteArrayInputStream(s1.getBytes()));
            docxml.getDocumentElement().normalize();

            String status = docxml.getElementsByTagName("Status").item(0).getTextContent();
            if (status.equals("1")) {
                Parsemessage = "Success";
                /*appDatabase.weatherDetailsDAO().deleteAllWeatherDetailsBasedOnFarmerID(farmerID);
                appDatabase.forecastDetailsDAO().deleteAllForecastDetailsBasedOnFarmerID(farmerID);*/

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        appDatabase.weatherDetailsDAO().deleteAllWeatherDetailsBasedOnFarmerID(farmerID);
                        appDatabase.forecastDetailsDAO().deleteAllForecastDetailsBasedOnFarmerID(farmerID);

                    }
                });
            }

            NodeList nodeList = docxml.getElementsByTagName("WeatherDetails");
            for (int k = 0; k < nodeList.getLength(); k++) {
                Node node = nodeList.item(k);
                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node landDetailsNode = node.getChildNodes().item(j);
                    String districtName = null;
                    String talukName = null;
                    String villageName = null;
                    String rainFall = null;
                    String minMaxTemperature = null;
                    String minMaxRh = null;
                    String minMaxWindSpeed = null;
                    String weatherStationDate = null;

                    for (int i = 0; i < landDetailsNode.getChildNodes().getLength(); i++) {

                        Node childWeather = landDetailsNode.getChildNodes().item(i);

                        if (childWeather.getNodeName().equalsIgnoreCase("districtname")) {
                            districtName = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("talukname")) {
                            talukName = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("villagename")) {
                            villageName = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("rainfall")) {
                            rainFall = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("avgtemp")) {
                            minMaxTemperature = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("avgrh")) {
                            minMaxRh = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("avgwind")) {
                            minMaxWindSpeed = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("weatherdate")) {
                            weatherStationDate = childWeather.getTextContent();
                        }
                    }

                    final ModelweatherDetails weatherDetails = new ModelweatherDetails(farmerID, districtName, talukName, villageName, rainFall, minMaxTemperature, minMaxRh, minMaxWindSpeed, weatherStationDate);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (weatherDetails != null) {
                                appDatabase.weatherDetailsDAO().insertWeatherDetails(weatherDetails);
                            }
                        }
                    });
                }
            }

            NodeList nodeListForeCast = docxml.getElementsByTagName("ForeCasteDetails");
            for (int k = 0; k < nodeListForeCast.getLength(); k++) {
                Node node = nodeListForeCast.item(k);
                System.out.println("calling aa temp" + node.getChildNodes().getLength());
                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node landDetailsNode = node.getChildNodes().item(j);

                    String districtName = null;
                    String talukName = null;
                    String villageName = null;
                    String lastUpdationDate = null;
                    String hours = null;
                    String rainFall = null;
                    String cloud = null;
                    String temperature = null;
                    String humidity = null;
                    String windSpeed = null;

                    for (int i = 0; i < landDetailsNode.getChildNodes().getLength(); i++) {

                        Node childWeather = landDetailsNode.getChildNodes().item(i);

                        if (childWeather.getNodeName().equalsIgnoreCase("districtname")) {
                            districtName = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("talukname")) {
                            talukName = childWeather.getTextContent();
                        }


                        if (childWeather.getNodeName().equalsIgnoreCase("villagename")) {
                            villageName = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("weatherstationdate")) {
                            lastUpdationDate = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("forecaste")) {
                            hours = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("rain")) {
                            rainFall = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("cloudiness")) {
                            cloud = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("temperature")) {
                            temperature = childWeather.getTextContent();
                        }


                        if (childWeather.getNodeName().equalsIgnoreCase("humidity")) {
                            humidity = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("windspeed")) {
                            windSpeed = childWeather.getTextContent();
                        }
                    }

                    final ModelForeCastDetails foreCastDetails = new ModelForeCastDetails(farmerID, districtName, talukName, villageName, lastUpdationDate, hours, rainFall, cloud, temperature, humidity, windSpeed);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (foreCastDetails != null) {
                                appDatabase.forecastDetailsDAO().insertForeCastDetails(foreCastDetails);
                            }
                        }
                    });
                }

            }


        } catch (SAXException e) {
            e.printStackTrace();
            Parsemessage = "error";
        } catch (IOException e) {
            e.printStackTrace();
            Parsemessage = "error";
        } catch (Exception e) {
            e.printStackTrace();
            Parsemessage = "error";
        }
        return Parsemessage;
    }
}
