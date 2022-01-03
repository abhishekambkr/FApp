
package org.nic.fruits;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.nic.fruits.CropDetails.CropDetails;
import org.nic.fruits.adapter.FruitsHomeAdapter;
import org.nic.fruits.adapter.TitleNavigationAdapter;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.pojo.ModelCropFertilizerMasternpk;
import org.nic.fruits.pojo.ModelCropMasterType;
import org.nic.fruits.pojo.ModelFertilizerCropMaster;
import org.nic.fruits.pojo.ModelFertilizerNameMaster;
import org.nic.fruits.pojo.ModelIconWIthDescriptiveName;
import org.nic.fruits.pojo.ModelIrrigationType;
import org.nic.fruits.pojo.ModelPlantAgeMaster;
import org.nic.fruits.pojo.SpinnerNavItem;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



//Programming by Harsha  for version 1.0 release
public class FruitsHomeActivity extends AppCompatActivity  {

    private DrawerLayout hasiruDrawerLayout;
    private ActionBarDrawerToggle hasiruDrawerToggle;
    private NavigationView hasiruNavigationView;
    private TextView navHeaderSubTitle;
    private String farmerID;
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    private Context mContext;
    private List<ModelIconWIthDescriptiveName> mainOptionItemList = new ArrayList<>();
    private FruitsHomeAdapter adapter;
    private String currentLanguage;
    private ActionBar actionBar;
    private ArrayList<SpinnerNavItem> navSpinner;
    private TitleNavigationAdapter navSpinnerAdapter;
    private Locale myLocale;
    private String NPCI;
    private String LastUpdated;
    private String Bank;
    private String RequestNumber;
    private String keyValue;
    private LinearLayout linearLayoutBottom;
    private Spinner spinner;
    private TextView textViewDesignDevelopment;
    private List<ModelCropFertilizerMasternpk> fertilizerMasternpk = new ArrayList<>();
    private AppDatabase appDatabase;
    private boolean dataAvailable = false;
    private ModelFertilizerCropMaster modelFertilizerCropMaster;
    private ModelPlantAgeMaster modelPlantAgeMaster;
    private ModelFertilizerNameMaster modelFertilizerNameMaster;
    private ModelCropFertilizerMasternpk modelCropFertilizerMasternpk;
    private ModelCropMasterType modelCropMasterType;
    private ModelIrrigationType modelIrrigationType;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruits_home);
        mContext = this;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hasiruDrawerLayout = (DrawerLayout) findViewById(R.id.fruite_drawer);
        hasiruDrawerToggle = new ActionBarDrawerToggle(this, hasiruDrawerLayout, R.string.Open, R.string.Close);
        hasiruDrawerLayout.addDrawerListener(hasiruDrawerToggle);
        hasiruDrawerToggle.syncState();
        hasiruNavigationView = (NavigationView) findViewById(R.id.nv);

        SharedPreferences prefs = getSharedPreferences("com.example.fruites", MODE_PRIVATE);
        farmerID = prefs.getString("FarmerID", "default_value_here_if_string_is_missing");
        keyValue = prefs.getString("KeyValue", "default_value_here_if_string_is_missing");
        View headerView = hasiruNavigationView.getHeaderView(0);
        navHeaderSubTitle = (TextView) headerView.findViewById(R.id.farmer_id);
        navHeaderSubTitle.setText(farmerID);

        hasiruDrawerLayout.addDrawerListener(hasiruDrawerToggle);
        hasiruDrawerToggle.syncState();
        hasiruNavigationView = (NavigationView) findViewById(R.id.nv);

        hasiruNavigationView.setItemIconTintList(null);

        actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#00b300'>FRUITS</font>"));
        navSpinner = new ArrayList<SpinnerNavItem>();
        navSpinner.add(new SpinnerNavItem(" ಕನ್ನಡ ", R.drawable.kannada_icon));
        navSpinner.add(new SpinnerNavItem("English", R.drawable.english_icon));
        navSpinnerAdapter = new TitleNavigationAdapter(mContext, navSpinner);
//        actionBar.setListNavigationCallbacks(navSpinnerAdapter, this);

        hasiruNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.personal_details:
                        Intent hmbsActivityIntent = new Intent(mContext, CardViewFarmerDetailsActivity.class);
                        startActivity(hmbsActivityIntent);
                        break;

                    case R.id.payment_details:
                        Intent preInspectionIntent = new Intent(mContext, CardViewFarmerPaymentDetailsActivity.class);
                        startActivity(preInspectionIntent);
                        break;

                    case R.id.land_details:
                        Intent postInspectionIntent = new Intent(mContext, CardViewLandDetailsActivity.class);
                        startActivity(postInspectionIntent);
                        break;

                    case R.id.crop_survey_details:
                        Intent intent = new Intent(mContext, CardViewCropSurveyDetailsActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.weather_forecast_details:
                        Intent WeatherIntent = new Intent(mContext, WeatherForecastActivity.class);
                        startActivity(WeatherIntent);
                        break;

                    case R.id.npci:
                        if (Utils.isNetworkConnected(mContext)) {
                            new GetNPCDeatails().execute();
                        } else {
                            new AlertDialog.Builder(mContext)
                                    .setTitle("Alert")
                                    .setMessage("Please Connect To Internet To Check NPCI Status")

                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .show();
                        }

                        break;

                    case R.id.feedback:
                        Intent feedbackIntent = new Intent(mContext, FeedBackDetailsActivity.class);
                        startActivity(feedbackIntent);
                        break;

                    case R.id.cropdetails:
                        Intent cropdetailsIntent = new Intent(mContext, CropDetails.class);
                        startActivity(cropdetailsIntent);
                        break;

                    case R.id.fertilizer_calculation:
                        Intent fertilizercalc_intent = new Intent(mContext, FertilizerCalculation.class);
                        startActivity(fertilizercalc_intent);
                        break;

                    default:
                        return true;
                }
                return true;

            }
        });

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager = new GridLayoutManager(this, 3);
        linearLayoutBottom =  findViewById(R.id.bottomBox);
        textViewDesignDevelopment =  findViewById(R.id.design_development);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 3 : 1;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
//                    Toast.makeText(FruitsHomeActivity.this, "Last", Toast.LENGTH_LONG).show();
                    linearLayoutBottom.setVisibility(View.GONE);
                }else {
                    if(linearLayoutBottom.getVisibility() == View.GONE){
                        linearLayoutBottom.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });

    }

    private void addItemList() {
        ModelIconWIthDescriptiveName itemAdapter = new ModelIconWIthDescriptiveName();
        itemAdapter.setImage(R.drawable.personal);
        itemAdapter.setText(getResources().getString(R.string.personal_data));
        mainOptionItemList.add(itemAdapter);

        itemAdapter = new ModelIconWIthDescriptiveName();
        itemAdapter.setImage(R.drawable.pay);
        itemAdapter.setText(getResources().getString(R.string.payment_data));
        mainOptionItemList.add(itemAdapter);

        itemAdapter = new ModelIconWIthDescriptiveName();
        itemAdapter.setImage(R.drawable.farm);
        itemAdapter.setText(getResources().getString(R.string.land_data));
        mainOptionItemList.add(itemAdapter);

        itemAdapter = new ModelIconWIthDescriptiveName();
        itemAdapter.setImage(R.drawable.cropsurvey);
        itemAdapter.setText(getResources().getString(R.string.cropsurvey_data));
        mainOptionItemList.add(itemAdapter);

        itemAdapter = new ModelIconWIthDescriptiveName();
        itemAdapter.setImage(R.drawable.weather);
        itemAdapter.setText(getResources().getString(R.string.weather_forecast));
        mainOptionItemList.add(itemAdapter);

        itemAdapter = new ModelIconWIthDescriptiveName();
        itemAdapter.setImage(R.drawable.npci);
        itemAdapter.setText(getResources().getString(R.string.npci));
        mainOptionItemList.add(itemAdapter);

        itemAdapter = new ModelIconWIthDescriptiveName();
        itemAdapter.setImage(R.drawable.feedback);
        itemAdapter.setText(getResources().getString(R.string.feedback));
        mainOptionItemList.add(itemAdapter);

        itemAdapter = new ModelIconWIthDescriptiveName();
        itemAdapter.setImage(R.drawable.cropdetail);
        itemAdapter.setText(getResources().getString(R.string.cropdetails));
        mainOptionItemList.add(itemAdapter);

        itemAdapter = new ModelIconWIthDescriptiveName();
        itemAdapter.setImage(R.drawable.fertilizer);
        itemAdapter.setText(getResources().getString(R.string.fertilizercalculation));
        mainOptionItemList.add(itemAdapter);

         itemAdapter = new ModelIconWIthDescriptiveName();
        itemAdapter.setImage(R.drawable.farmfertilizer);
        itemAdapter.setText(getResources().getString(R.string.farmfertilizer));
        mainOptionItemList.add(itemAdapter);

        textViewDesignDevelopment.setText(getResources().getString(R.string.design_devlopment));
    }

    private void adapter() {
        adapter = new FruitsHomeAdapter(mContext, mainOptionItemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void logout() {
        Intent intent = new Intent(mContext, OTPGenerationMainActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        spinner = (Spinner) item.getActionView();
        spinner.setAdapter(navSpinnerAdapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected( AdapterView<?> parent, View view, int position,  long id) {
                        System.out.println("calling aa position " +position);

                        if (position == 0) {
                            setLocale("kn");
                        } else if (position == 1) {
                            setLocale("en");
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hasiruDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        hasiruDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (Build.VERSION.SDK_INT > 11) {
            menu.findItem(R.id.sync).setVisible(false);
            invalidateOptionsMenu();
            return true;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (hasiruDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;

            res.updateConfiguration(conf, dm);
            adapter();
            clearData();
            addItemList();

        } else {
            Toast.makeText(FruitsHomeActivity.this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearData() {
        mainOptionItemList.clear();
        adapter.notifyDataSetChanged();
    }

    private class GetNPCDeatails extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait...NPCI Details Are Collecting..");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String npcStatusCheck = new String();
            try {
                SoapProxy proxy2 = new SoapProxy(mContext);
                npcStatusCheck = proxy2.CheckNPCStatus(keyValue, farmerID);
                npcStatusCheck = npcStatusCheck.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                npcStatusCheck = npcStatusCheck.replaceAll("[^\\x20-\\x7e]", "");

                if (npcStatusCheck.equalsIgnoreCase("Failure")) {
                    npcStatusCheck = "InternetconectionProblem";
                } else if (npcStatusCheck.equalsIgnoreCase("Failure1")) {
                    npcStatusCheck = "InternetconectionProblem";
                } else if (npcStatusCheck.equalsIgnoreCase("anyType{}")) {
                    npcStatusCheck = "anyType{}";
                } else {
                    npcStatusCheck = ParseNPCStatusCheck(npcStatusCheck);
                }
            } catch (Exception e) {
                e.printStackTrace();
                npcStatusCheck = "InternetconectionProblem";
            }

            return npcStatusCheck;
        }

        @Override
        protected void onPostExecute(String npcStatusCheck) {
            this.dialog.dismiss();
            if (npcStatusCheck.equals("Success")) {
                Intent i = new Intent(mContext, NPCIActivity.class);
                i.putExtra("NPCI", NPCI);
                i.putExtra("Last_updation", LastUpdated);
                i.putExtra("By_Bank", Bank);
                i.putExtra("Request_Number", RequestNumber);
                startActivity(i);
            }

        }
    }

    public String ParseNPCStatusCheck(String npcStatuesCheck) {
        String Parsemessage = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setCharacterStream(new StringReader(npcStatuesCheck));
            Document docxml = builder.parse(src);
            String status = docxml.getElementsByTagName("Status").item(0).getTextContent();
            if (status.equals("1")) {
                Parsemessage = "Success";
            }
            NPCI = docxml.getElementsByTagName("NPCI").item(0).getTextContent();
            LastUpdated = docxml.getElementsByTagName("LastUpdated").item(0).getTextContent();
            Bank = docxml.getElementsByTagName("Bank").item(0).getTextContent();
            RequestNumber = docxml.getElementsByTagName("RequestNumber").item(0).getTextContent();

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
