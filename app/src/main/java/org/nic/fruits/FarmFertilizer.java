package org.nic.fruits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.nic.fruits.CropDetails.CropRegister;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;
import org.nic.fruits.pojo.ModelCropFertilizerMasternpk;
import org.nic.fruits.pojo.ModelCropRegistration;
import org.nic.fruits.pojo.ModelCropSurveyDetails;
import org.nic.fruits.pojo.ModelFarmFertilizer;
import org.nic.fruits.pojo.ModelFarmerLandDeatails;
import org.nic.fruits.pojo.ModelFertilizerCropMaster;
import org.nic.fruits.pojo.ModelFertilizerNameMaster;
import org.nic.fruits.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FarmFertilizer extends AppCompatActivity {

    Context mContext;
    String farmerID;
    String keyValue;
    AppDatabase appDatabase;
    Locale locale;
    String yearValue = "", seasonValue = "", cropNameValue = "", cropAreaValue="", cropCodeValue="";
    String districtValue = "",talukValue = "",hobliValue = "",villageValue = "",surveyValue = "",cropname = "", cropextentValue="", cropcode ="";
    LinearLayout linearLayoutFertilizerDetails;
    TextView tvFarmerId;
    Spinner spYear,spSeason;
    List<String> arrayYear;
    List<String> arraySeason;
    List<String> arrayCropCodeValue;
    List<String> arrayDistrict;
    List<String> arrayTaluk;
    List<String> arrayHobli;
    List<String> arrayVillage;
    List<String> arraySurvey;

    List<String> arrayCropName;
    List<String> arrayCropArea;
    List<String> arrayFertilizerCropCode;
    List<String> arrayFertilizerCropType;
    List<String> arrayFertilizerCropNitrogen;
    List<String> arrayFertilizerCropPhospohorous;
    List<String> arrayFertilizerCropPotash;
    List<String> arrayRequiredNitrogen;
    List<String> arrayRequiredPhospohorous;
    List<String> arrayRequiredPotash;
    String [] tempfmdata = new String[0];
    List<String> arrayAddRequiredNitrogen;
    List<String> arrayAddRequiredPhospohorous;
    List<String> arrayAddRequiredPotash;
    int sumNitrogen = 0;
    int sumPhospohorous= 0;
    int sumPotash = 0;
    List<String> arrayNutrientCombination;
    List<String> arrayFertilizerIdCombination;
    List<String> arrayFinalNitrogenValue;
    List<String> arrayFinalPhospohorousValue;
    List<String> arrayFinalPotashValue;

    List<String> arrayData;
    int nitrogen;
    int phosphorous;
    int potash;
    int fmPhosphorous =0;
    int fmNitrogen = 0;
    int fmPotash = 0;
    int requiredNitrogen;
    int requiredPhosphorous;
    int requiredPotash;
    List<String> arrayFertilizerName;
    List<String> arrayPhosphorous;
    List<String> arrayNitrogen;
    List<String> arrayPotash;
    int totalPhosphorous =0;
    int totalNitrogen = 0;
    int totalPotash = 0;
    double totalN = 0;
    double totalP = 0;
    double totalK = 0;
    //   TextView tvuserDetail;
    RelativeLayout rlTableUserDetails;
    RelativeLayout rlTableNutrientDetails;
    RelativeLayout rlTableArea;
    RelativeLayout rlTableFertilizer;
    TableLayout tbUserDetails;
    TableLayout tbNutrientDetail;
    TableLayout tbAreaDetails;
    TableLayout tbNPKDetails;

    TextView tvNutrientdetail;
    TextView tvNPKDetails;
    Set<String> npk;
    int count =0;
    final Handler handler = new Handler();

    String checkcropname;
    String cropExtentTNitrogen;
    String cropExtentTPhosphorous;
    String cropExtentTPotash;

    List<String> arrayCropExtentTotalNPK;
    String cropextentTotalNPK ="";
    int finalDataObtained = 0;
    String recommendednpkvalue;
    double value;
    double bagValue;
    List<String> arrayKGValue;
    List<String> arraybagValue;
    String displayRecommendedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_fertilizer);

        getSupportActionBar().setTitle(getResources().getString(R.string.farmfertilizer));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SharedPreferences prefs = getSharedPreferences("com.example.fruites", MODE_PRIVATE);
        farmerID = prefs.getString("FarmerID", "default_value_here_if_string_is_missing");
        keyValue = prefs.getString("KeyValue", "default_value_here_if_string_is_missing");
        mContext = this;
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        locale = Utils.getCurrentLocale(mContext);
        linearLayoutFertilizerDetails = findViewById(R.id.linearlayoutfertilizerdetails);
        tvFarmerId = findViewById(R.id.textviewFarmerId);
        spYear = findViewById(R.id.spinnerFFYear);
        spSeason = findViewById(R.id.spinnerFFSeason);
        //    tvuserDetail = findViewById(R.id.tvuserdetails);
        rlTableUserDetails= findViewById(R.id.rlUserDetails);
        rlTableNutrientDetails = findViewById(R.id.rlNutrientdetails);
        rlTableArea = findViewById(R.id.rlAreaDetails);
        rlTableFertilizer = findViewById(R.id.rlnpkdetails);
        tbUserDetails = findViewById(R.id.tbUserDetail);
        tbNutrientDetail = findViewById(R.id.tbNutrientdetails);
        tbAreaDetails = findViewById(R.id.tbAreaDetail);
        tbNPKDetails = findViewById(R.id.tbnpkdetails);

        //  tvNPKDetails = findViewById(R.id.npkdetails);
        arrayYear = new ArrayList<>();
        arraySeason = new ArrayList<>();
        arrayCropCodeValue = new ArrayList<>();
        arrayDistrict = new ArrayList<>();
        arrayTaluk = new ArrayList<>();
        arrayHobli = new ArrayList<>();
        arrayVillage = new ArrayList<>();
        arraySurvey = new ArrayList<>();
        arrayCropName = new ArrayList<>();
        arrayCropArea = new ArrayList<>();
        arrayFertilizerCropCode = new ArrayList<>();
        arrayFertilizerCropType = new ArrayList<>();
        arrayFertilizerCropNitrogen = new ArrayList<>();
        arrayFertilizerCropPhospohorous = new ArrayList<>();
        arrayFertilizerCropPotash = new ArrayList<>();
        arrayRequiredNitrogen = new ArrayList<>();
        arrayRequiredPhospohorous = new ArrayList<>();
        arrayRequiredPotash = new ArrayList<>();
        arrayAddRequiredNitrogen = new ArrayList<>();
        arrayAddRequiredPhospohorous = new ArrayList<>();
        arrayAddRequiredPotash = new ArrayList<>();
        arrayNutrientCombination = new ArrayList<>();
        arrayFertilizerIdCombination = new ArrayList<>();
        arrayFinalNitrogenValue = new ArrayList<>();
        arrayFinalPhospohorousValue = new ArrayList<>();
        arrayFinalPotashValue = new ArrayList<>();

        arrayData = new ArrayList<>();
        arrayFertilizerName = new ArrayList<>();

        arrayPhosphorous = new ArrayList<>();
        arrayNitrogen = new ArrayList<>();
        arrayPotash = new ArrayList<>();

        arrayFertilizerName = new ArrayList<>();
        arrayCropExtentTotalNPK = new ArrayList<>();
        arrayKGValue = new ArrayList<>();
        arraybagValue = new ArrayList<>();

        arrayNutrientCombination.clear();
        arrayNutrientCombination.add(0,"1");
        arrayNutrientCombination.add(1,"1");
        arrayNutrientCombination.add(2,"1");
        arrayNutrientCombination.add(3,"2");
        arrayNutrientCombination.add(4,"2");
        arrayNutrientCombination.add(5,"2");
        arrayNutrientCombination.add(6,"2");
        arrayNutrientCombination.add(7,"3");
        arrayNutrientCombination.add(8,"3");
        arrayNutrientCombination.add(9,"3");
        arrayNutrientCombination.add(10,"3");
        arrayFertilizerIdCombination.clear();
        arrayFertilizerIdCombination.add(0,"1");
        arrayFertilizerIdCombination.add(1,"10");
        arrayFertilizerIdCombination.add(2,"11");
        arrayFertilizerIdCombination.add(3,"1");
        arrayFertilizerIdCombination.add(4,"4");
        arrayFertilizerIdCombination.add(5,"10");
        arrayFertilizerIdCombination.add(6,"11");
        arrayFertilizerIdCombination.add(7,"1");
        arrayFertilizerIdCombination.add(8,"10");
        arrayFertilizerIdCombination.add(9,"11");
        arrayFertilizerIdCombination.add(10,"18");

        tvFarmerId.setText(farmerID);
        linearLayoutFertilizerDetails.setVisibility(View.GONE);
        getYear();
        if (locale.toString().equals("en")) {
            arraySeason.add(0, "Select Season");
        } else {
            arraySeason.add(0, "ಋತುವನ್ನು ಆಯ್ಕೆ ಮಾಡಿ");
        }
    }

    private void getYear() {

        if (locale.toString().equals("en")) {
            arrayYear.add(0, "Select Year");
        }else{
            arrayYear.add(0, "ವರ್ಷವನ್ನು ಆಯ್ಕೆ ಮಾಡಿ");
        }
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayYear);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        MainViewModel viewModelYear = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModelYear.getCropSurveBasedOnFid(farmerID).observe(this, new Observer<List<ModelCropSurveyDetails>>() {
            @Override
            public void onChanged(@Nullable List<ModelCropSurveyDetails> taskEntries) {

                if (taskEntries != null && !taskEntries.isEmpty()){
               /*     adapter = new CropSurveyCardAdapter(CardViewCropSurveyDetailsActivity.this, taskEntries);
                    recyclerView.setAdapter(adapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(CardViewCropSurveyDetailsActivity.this, LinearLayoutManager.VERTICAL));
                    adapter.notifyDataSetChanged();*/
                    for(ModelCropSurveyDetails taskEntry:taskEntries){

                        if (locale.toString().equals("en")) {
                            arrayYear.add(taskEntry.getYear());
                            Set<String> filterYear;
                            filterYear = new LinkedHashSet<String>(arrayYear);
                            arrayYear.clear();
                            arrayYear.addAll(filterYear);

                        } else {
                            arrayYear.add(taskEntry.getYear());
                            Set<String> filterYear;
                            filterYear = new LinkedHashSet<String>(arrayYear);
                            arrayYear.clear();
                            arrayYear.addAll(filterYear);

                        }

                    }
                }
                else{
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.beneficiary_not_present))
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
                adapterYear.notifyDataSetChanged();
                spYear.setAdapter(adapterYear);
                spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            yearValue = spYear.getSelectedItem().toString();
                            arraySeason.clear();
                            arrayDistrict.clear();
                            arrayTaluk.clear();
                            arrayHobli.clear();
                            arrayVillage.clear();
                            arraySurvey.clear();
                            arrayCropName.clear();
                            arrayCropArea.clear();
                            arrayFertilizerCropCode.clear();
                            arrayFertilizerCropType.clear();
                            arrayFertilizerCropNitrogen.clear();
                            arrayFertilizerCropPhospohorous.clear();
                            arrayFertilizerCropPotash.clear();
                            arrayRequiredNitrogen.clear();
                            arrayRequiredPhospohorous.clear();
                            arrayRequiredPotash.clear();
                            arrayAddRequiredNitrogen.clear();
                            arrayAddRequiredPhospohorous.clear();
                            arrayAddRequiredPotash.clear();
                            arrayFertilizerName.clear();
                            sumNitrogen = 0;
                            sumPhospohorous = 0;
                            sumPotash = 0;

                            arrayData.clear();

                            arrayFinalNitrogenValue.clear();
                            arrayFinalPhospohorousValue.clear();
                            arrayFinalPotashValue.clear();
                            nitrogen = 0;
                            phosphorous = 0;
                            potash= 0;
                            fmNitrogen = 0;
                            fmPhosphorous = 0;
                            fmPotash = 0;

                            arrayNitrogen.clear();
                            arrayPhosphorous.clear();
                            arrayPotash.clear();

                            totalPhosphorous = 0;
                            totalNitrogen = 0;
                            totalPotash = 0;
                            totalN = 0;
                            totalP = 0;
                            totalK = 0;
                            arrayCropExtentTotalNPK.clear();
                            linearLayoutFertilizerDetails.setVisibility(View.GONE);
                            clearViews();


                            //   tvNPKDetails.setText("");
                            getSeason();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                });
            }
        });
    }

    private void getSeason() {
        if(yearValue!=null) {
            arraySeason.clear();
            if (locale.toString().equals("en")) {
                arraySeason.add(0, "Select Season");
            } else {
                arraySeason.add(0, "ಋತುವನ್ನು ಆಯ್ಕೆ ಮಾಡಿ");
            }
            ArrayAdapter<String> adapterSeason = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arraySeason);
            adapterSeason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            MainViewModel viewModelYear = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModelYear.getSeason(farmerID, yearValue).observe(this, new Observer<List<ModelCropSurveyDetails>>() {

                @Override
                public void onChanged(@Nullable List<ModelCropSurveyDetails> taskEntries) {

                    if (taskEntries != null && !taskEntries.isEmpty()) {

                        for (ModelCropSurveyDetails taskEntry : taskEntries) {

                            if (locale.toString().equals("en")) {
                                arraySeason.add(taskEntry.getSeason());
                                Set<String> filterSeason;
                                filterSeason = new LinkedHashSet<String>(arraySeason);
                                arraySeason.clear();
                                arraySeason.addAll(filterSeason);

                            } else {
                                arraySeason.add(taskEntry.getSeason());
                                Set<String> filterSeason;
                                filterSeason = new LinkedHashSet<String>(arraySeason);
                                arraySeason.clear();
                                arraySeason.addAll(filterSeason);

                            }

                        }
                    }
                    else {
                        new AlertDialog.Builder(mContext)
                                .setTitle(getResources().getString(R.string.alert))
                                .setMessage(getResources().getString(R.string.beneficiary_not_present))
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
                    adapterSeason.notifyDataSetChanged();
                    spSeason.setAdapter(adapterSeason);
                    spSeason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i != 0) {
                                seasonValue = spSeason.getSelectedItem().toString();
                                arrayDistrict.clear();
                                arrayTaluk.clear();
                                arrayHobli.clear();
                                arrayVillage.clear();
                                arraySurvey.clear();
                                arrayCropName.clear();
                                arrayCropArea.clear();
                                arrayFertilizerCropCode.clear();
                                arrayFertilizerCropType.clear();
                                arrayFertilizerCropNitrogen.clear();
                                arrayFertilizerCropPhospohorous.clear();
                                arrayFertilizerCropPotash.clear();
                                arrayRequiredNitrogen.clear();
                                arrayRequiredPhospohorous.clear();
                                arrayRequiredPotash.clear();
                                arrayAddRequiredNitrogen.clear();
                                arrayAddRequiredPhospohorous.clear();
                                arrayAddRequiredPotash.clear();
                                arrayFertilizerName.clear();
                                sumNitrogen = 0;
                                sumPhospohorous = 0;
                                sumPotash = 0;

                                arrayData.clear();

                                arrayFinalNitrogenValue.clear();
                                arrayFinalPhospohorousValue.clear();
                                arrayFinalPotashValue.clear();
                                nitrogen = 0;
                                phosphorous = 0;
                                potash= 0;
                                fmNitrogen = 0;
                                fmPhosphorous = 0;
                                fmPotash = 0;

                                arrayNitrogen.clear();
                                arrayPhosphorous.clear();
                                arrayPotash.clear();

                                totalPhosphorous = 0;
                                totalNitrogen = 0;
                                totalPotash = 0;
                                totalN = 0;
                                totalP = 0;
                                totalK = 0;
                                arrayCropExtentTotalNPK.clear();
                                linearLayoutFertilizerDetails.setVisibility(View.GONE);
                                clearViews();

                                Toast toast = Toast.makeText(mContext, "Loading... please wait", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                getCropDetails();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            });
        }
    }

    private void getCropDetails() {
        arrayCropExtentTotalNPK.clear();
        arrayCropName.clear();
        arrayCropArea.clear();
        if(yearValue!=null && seasonValue !=null)
        {
            MainViewModel viewModelCrop = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModelCrop.getCropDetails(farmerID, yearValue,seasonValue).observe(this, new Observer<List<ModelCropSurveyDetails>>() {
                @Override
                public void onChanged(@Nullable List<ModelCropSurveyDetails> taskEntries) {

                    if (taskEntries != null && !taskEntries.isEmpty()) {

                        for (ModelCropSurveyDetails taskEntry : taskEntries) {

                            if (locale.toString().equals("en")) {

                                arrayDistrict.add(taskEntry.getDistrictname());
                                Set<String> filterDistrictName;
                                filterDistrictName = new LinkedHashSet<String>(arrayDistrict);
                                arrayDistrict.clear();
                                arrayDistrict.addAll(filterDistrictName);

                                arrayTaluk.add(taskEntry.getTalukName());
                                Set<String> filterTalukName;
                                filterTalukName = new LinkedHashSet<String>(arrayTaluk);
                                arrayTaluk.clear();
                                arrayTaluk.addAll(filterTalukName);

                                arrayHobli.add(taskEntry.getHobliName());
                                Set<String> filterHobliName;
                                filterHobliName = new LinkedHashSet<String>(arrayHobli);
                                arrayHobli.clear();
                                arrayHobli.addAll(filterHobliName);

                                arrayVillage.add(taskEntry.getVillageName());
                                Set<String> filterVillageName;
                                filterVillageName = new LinkedHashSet<String>(arrayVillage);
                                arrayVillage.clear();
                                arrayVillage.addAll(filterVillageName);

                                arraySurvey.add(taskEntry.getSurveyNumber());
                                Set<String> filterSurvey;
                                filterSurvey = new LinkedHashSet<String>(arraySurvey);
                                arraySurvey.clear();
                                arraySurvey.addAll(filterSurvey);

                                arrayCropName.add(taskEntry.getCropName());
             /*                   arrayCropName.add(0,"Coconut");
                                arrayCropName.add(1,"Groundnut");*/
                                Set<String> filterCropName;
                                filterCropName = new LinkedHashSet<String>(arrayCropName);
                                arrayCropName.clear();
                                arrayCropName.addAll(filterCropName);

                                arrayCropArea.add(taskEntry.getCropArea());
                                Set<String> filterCropArea;
                                filterCropArea = new LinkedHashSet<String>(arrayCropArea);
                                arrayCropArea.clear();
                                arrayCropArea.addAll(filterCropArea);

                                districtValue = taskEntry.getDistrictname();
                                talukValue = taskEntry.getTalukName();
                                hobliValue = taskEntry.getHobliName();
                                villageValue = taskEntry.getVillageName();
                                surveyValue = taskEntry.getSurveyNumber();
                                cropname = taskEntry.getCropName();
                                cropextentValue = taskEntry.getCropArea();

                            } else {
                                arrayDistrict.add(taskEntry.getDistrictname());
                                Set<String> filterDistrictName;
                                filterDistrictName = new LinkedHashSet<String>(arrayDistrict);
                                arrayDistrict.clear();
                                arrayDistrict.addAll(filterDistrictName);

                                arrayTaluk.add(taskEntry.getTalukName());
                                Set<String> filterTalukName;
                                filterTalukName = new LinkedHashSet<String>(arrayTaluk);
                                arrayTaluk.clear();
                                arrayTaluk.addAll(filterTalukName);

                                arrayHobli.add(taskEntry.getHobliName());
                                Set<String> filterHobliName;
                                filterHobliName = new LinkedHashSet<String>(arrayHobli);
                                arrayHobli.clear();
                                arrayHobli.addAll(filterHobliName);

                                arrayVillage.add(taskEntry.getVillageName());
                                Set<String> filterVillageName;
                                filterVillageName = new LinkedHashSet<String>(arrayVillage);
                                arrayVillage.clear();
                                arrayVillage.addAll(filterVillageName);

                                arraySurvey.add(taskEntry.getSurveyNumber());
                                Set<String> filterSurvey;
                                filterSurvey = new LinkedHashSet<String>(arraySurvey);
                                arraySurvey.clear();
                                arraySurvey.addAll(filterSurvey);

                                arrayCropName.add(taskEntry.getCropName());
                               /* arrayCropName.add(0,"Coconut");
                                arrayCropName.add(1,"Groundnut");*/
                                Set<String> filterCropName;
                                filterCropName = new LinkedHashSet<String>(arrayCropName);
                                arrayCropName.clear();
                                arrayCropName.addAll(filterCropName);

                                arrayCropArea.add(taskEntry.getCropArea());
                                Set<String> filterCropArea;
                                filterCropArea = new LinkedHashSet<String>(arrayCropArea);
                                arrayCropArea.clear();
                                arrayCropArea.addAll(filterCropArea);

                                districtValue = taskEntry.getDistrictname();
                                talukValue = taskEntry.getTalukName();
                                hobliValue = taskEntry.getHobliName();
                                villageValue = taskEntry.getVillageName();
                                surveyValue = taskEntry.getSurveyNumber();
                                cropname = taskEntry.getCropName();
                                cropextentValue = taskEntry.getCropArea();
                            }
                        }
                        System.out.println("CropName array - " + arrayCropName);
                        System.out.println("CropArea array - " + arrayCropArea);
                        System.out.println("arrayDistrict array - " + arrayDistrict);
                        System.out.println("arraySurvey array - " + arraySurvey);

                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                appDatabase.farmFertilizerDAO().deleteAllFarmFertilizer();
                            }
                        });
                        for (int k = 0; k < arrayCropName.size(); k++){
                            for (int l = 0; l < arraySurvey.size(); l++) {
                                final ModelFarmFertilizer modelFarmFertilizer = new ModelFarmFertilizer(farmerID, yearValue, seasonValue, arrayCropName.get(k), "", arrayCropArea.get(l), "", districtValue, talukValue, hobliValue, villageValue, arraySurvey.get(l), "", "","", "", "", "", "");
                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        appDatabase.farmFertilizerDAO().insertCropDetails(modelFarmFertilizer);
                                    }
                                });
                            }
                        }
                        getFertilizerDetails();
                    }
                    else {
                        new AlertDialog.Builder(mContext)
                                .setTitle(getResources().getString(R.string.alert))
                                .setMessage(getResources().getString(R.string.beneficiary_not_present))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        Intent mainActivity = new Intent(mContext, FruitsHomeActivity.class);
                                        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(mainActivity);
                                        finish();
                                    }
                                }).show();
                    }
                }
            });
        }

    }

    private void getFertilizerDetails() {
        for(int k=0 ;k<arrayCropName.size();k++){

            cropNameValue = arrayCropName.get(k);

            MainViewModel viewFertilizerCropMaster = ViewModelProviders.of(this).get(MainViewModel.class);
            viewFertilizerCropMaster.getFertilizerCrops(arrayCropName.get(k)).observe(this, new Observer<List<ModelFertilizerCropMaster>>() {
                @Override
                public void onChanged(@Nullable List<ModelFertilizerCropMaster> taskEntries) {

                    if (taskEntries != null && !taskEntries.isEmpty()) {

                        for (ModelFertilizerCropMaster taskEntry : taskEntries) {

                            if (locale.toString().equals("en")) {
                                arrayFertilizerCropCode.add(taskEntry.getCropcode());
                                /*arrayFertilizerCropCode.add(0,"35");
                                arrayFertilizerCropCode.add(1,"11");*/
                                Set<String> filterCropCode;
                                filterCropCode = new LinkedHashSet<String>(arrayFertilizerCropCode);
                                arrayFertilizerCropCode.clear();
                                arrayFertilizerCropCode.addAll(filterCropCode);
                                arrayFertilizerCropType.add(taskEntry.getCroptype());
                                Set<String> filterCropType;
                                filterCropType = new LinkedHashSet<String>(arrayFertilizerCropType);
                                arrayFertilizerCropType.clear();
                                arrayFertilizerCropType.addAll(filterCropType);

                            }
                            else {
                                arrayFertilizerCropCode.add(taskEntry.getCropcode());
                               /* arrayFertilizerCropCode.add(0,"35");
                                arrayFertilizerCropCode.add(1,"11");*/
                                Set<String> filterCropCode;
                                filterCropCode = new LinkedHashSet<String>(arrayFertilizerCropCode);
                                arrayFertilizerCropCode.clear();
                                arrayFertilizerCropCode.addAll(filterCropCode);
                                arrayFertilizerCropType.add(taskEntry.getCroptype());
                                Set<String> filterCropType;
                                filterCropType = new LinkedHashSet<String>(arrayFertilizerCropType);
                                arrayFertilizerCropType.clear();
                                arrayFertilizerCropType.addAll(filterCropType);

                            }
                        }
                        System.out.println("CropCode array - " + arrayFertilizerCropCode);
                        System.out.println("CropType array - " + arrayFertilizerCropType);
                        for (int k = 0; k < arrayCropName.size(); k++){
                            String cropname = arrayCropName.get(k).toString();
                            String cropcodeFertilizer = arrayFertilizerCropCode.get(k).toString();
                            //    final ModelFarmFertilizer modelFarmFertilizer = new ModelFarmFertilizer(farmerID, yearValue, seasonValue, arrayCropName.get(k), "", arrayCropArea.get(l), "", districtValue, talukValue, hobliValue, villageValue, arraySurvey.get(l), "", "", "", "", "", "");
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    //        appDatabase.farmFertilizerDAO().insertCropDetails(modelFarmFertilizer);

                                    appDatabase.farmFertilizerDAO().updateFarmFertilizerCropCode(farmerID,cropname,cropcodeFertilizer);

                                }
                            });

                        }

                        getCropNPK();
                    }
                    else {
                        new AlertDialog.Builder(mContext)
                                .setTitle(getResources().getString(R.string.alert))
                                .setMessage(getResources().getString(R.string.beneficiary_not_present))
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
    }

    private void getCropNPK() {

        for(int k=0;k<arrayFertilizerCropCode.size();k++){

            MainViewModel viewFertilizerCropMaster = ViewModelProviders.of(this).get(MainViewModel.class);
            viewFertilizerCropMaster.getCropNPK(arrayFertilizerCropCode.get(k)).observe(this, new Observer<List<ModelCropFertilizerMasternpk>>() {

                @Override
                public void onChanged(@Nullable List<ModelCropFertilizerMasternpk> taskEntries) {

                    if (taskEntries != null && !taskEntries.isEmpty()) {

                        for (ModelCropFertilizerMasternpk taskEntry : taskEntries) {

                            if (locale.toString().equals("en")) {

                                arrayFertilizerCropNitrogen.add(taskEntry.getCf_nitrogen());
                                Set<String> filterFertilizerCropNitrogen;
                                filterFertilizerCropNitrogen = new LinkedHashSet<String>(arrayFertilizerCropNitrogen);
                                arrayFertilizerCropNitrogen.clear();
                                arrayFertilizerCropNitrogen.addAll(filterFertilizerCropNitrogen);

                                arrayFertilizerCropPhospohorous.add(taskEntry.getCf_phosphorous());
                                Set<String> filterFertilizerCropPhospohorous;
                                filterFertilizerCropPhospohorous = new LinkedHashSet<String>(arrayFertilizerCropPhospohorous);
                                arrayFertilizerCropPhospohorous.clear();
                                arrayFertilizerCropPhospohorous.addAll(filterFertilizerCropPhospohorous);

                                arrayFertilizerCropPotash.add(taskEntry.getCf_potash());
                                Set<String> filterFertilizerCropPotash;
                                filterFertilizerCropPotash = new LinkedHashSet<String>(arrayFertilizerCropPotash);
                                arrayFertilizerCropPotash.clear();
                                arrayFertilizerCropPotash.addAll(filterFertilizerCropPotash);

                            }
                            else {
                                arrayFertilizerCropNitrogen.add(taskEntry.getCf_nitrogen());
                                Set<String> filterFertilizerCropNitrogen;
                                filterFertilizerCropNitrogen = new LinkedHashSet<String>(arrayFertilizerCropCode);
                                arrayFertilizerCropNitrogen.clear();
                                arrayFertilizerCropNitrogen.addAll(filterFertilizerCropNitrogen);

                                arrayFertilizerCropPhospohorous.add(taskEntry.getCf_phosphorous());
                                Set<String> filterFertilizerCropPhospohorous;
                                filterFertilizerCropPhospohorous = new LinkedHashSet<String>(arrayFertilizerCropPhospohorous);
                                arrayFertilizerCropPhospohorous.clear();
                                arrayFertilizerCropPhospohorous.addAll(filterFertilizerCropPhospohorous);

                                arrayFertilizerCropPotash.add(taskEntry.getCf_potash());
                                Set<String> filterFertilizerCropPotash;
                                filterFertilizerCropPotash = new LinkedHashSet<String>(arrayFertilizerCropPotash);
                                arrayFertilizerCropPotash.clear();
                                arrayFertilizerCropPotash.addAll(filterFertilizerCropPotash);

                            }
                        }
                        System.out.println("CropNitrogen array - " + arrayFertilizerCropNitrogen);
                        System.out.println("CropPhospohorous array - " + arrayFertilizerCropPhospohorous);
                        System.out.println("CropPotash array - " + arrayFertilizerCropPotash);

                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                for (int k = 0; k < arrayCropName.size(); k++){
                                    String checkcropname = arrayCropName.get(k).toString();
                                    for(int j=0;j<arrayFertilizerCropNitrogen.size();j++) { //arrayFertilizerCropNitrogen

                                        String nitrogen = arrayFertilizerCropNitrogen.get(j).toString();
                                        String phosphorous = arrayFertilizerCropPhospohorous.get(j).toString();
                                        String potash = arrayFertilizerCropPotash.get(j).toString();
                                        appDatabase.farmFertilizerDAO().updateFarmFertilizerrecommendedNPK(farmerID,yearValue, seasonValue, checkcropname,nitrogen+"-"+phosphorous+"-"+potash);

                                    }
                                }
                            }
                        });
                        calculaterequiredNPK();
                    }
                    else {
                        new AlertDialog.Builder(mContext)
                                .setTitle(getResources().getString(R.string.alert))
                                .setMessage(getResources().getString(R.string.beneficiary_not_present))
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
    }

    private void calculaterequiredNPK() {

        if(cropNameValue!=null){
            System.out.println("in calculaterequiredNPK - " + arrayCropArea);
            System.out.println("in size - " + arrayCropArea.size());
            sumNitrogen = 0;
            sumPhospohorous = 0;
            sumPotash = 0;

            for(int i=0;i<arrayCropArea.size();i++){

                String temp = String.valueOf(arrayCropArea.get(i));
                String [] temp2 = temp.split("\\.");
                System.out.println("temp: " + temp);
                System.out.println("temp2: " + temp2[0] + " " + temp2[1] + " " + temp2[3]);

                double totalArea = Integer.parseInt(temp2[0]) + (Float.parseFloat(temp2[1]) / 40.0);
                System.out.println("totalArea - " + totalArea);

                for(int s=0;s<arrayFertilizerCropNitrogen.size();s++){
                    arrayRequiredNitrogen.add(String.valueOf((int) Math.round(Integer.parseInt(arrayFertilizerCropNitrogen.get(s).toString()) * totalArea)));
                    arrayRequiredPhospohorous.add(String.valueOf((int) Math.round(Integer.parseInt(arrayFertilizerCropPhospohorous.get(s).toString()) * totalArea)));
                    arrayRequiredPotash.add(String.valueOf((int) Math.round(Integer.parseInt(arrayFertilizerCropPotash.get(s).toString()) * totalArea)));
                    recommendednpkvalue = " - Nitrogen : " + arrayFertilizerCropNitrogen.get(s) +", Phospohorous : "+arrayFertilizerCropPhospohorous.get(s)+", Potash : "+arrayFertilizerCropPotash.get(s);

                }
                sumNitrogen = sumNitrogen + Integer.parseInt(arrayRequiredNitrogen.get(i));
                sumPhospohorous = sumPhospohorous + Integer.parseInt(arrayRequiredPhospohorous.get(i));
                sumPotash = sumPotash + Integer.parseInt(arrayRequiredPotash.get(i));
            }
            arrayAddRequiredNitrogen.add(String.valueOf(sumNitrogen));
            arrayAddRequiredPhospohorous.add(String.valueOf(sumPhospohorous));
            arrayAddRequiredPotash.add(String.valueOf(sumPotash));


            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {

                    for (int k = 0; k < arrayCropName.size(); k++){
                        checkcropname = arrayCropName.get(k).toString();
                        displayRecommendedValue = " crop : " +arrayCropName.get(k) + recommendednpkvalue;
                        for(int j=0;j<arrayCropArea.size();j++) {
                            cropExtentTNitrogen = arrayRequiredNitrogen.get(j).toString();
                            cropExtentTPhosphorous = arrayRequiredPhospohorous.get(j).toString();
                            cropExtentTPotash = arrayRequiredPotash.get(j).toString();

                            appDatabase.farmFertilizerDAO().updateFarmFertilizerrecommendedCENPK(farmerID,yearValue, seasonValue, checkcropname,arrayCropArea.get(j),arrayRequiredNitrogen.get(j).toString()+"-"+ arrayRequiredPhospohorous.get(j).toString()+"-"+arrayRequiredPotash.get(j).toString());

                        }
                    }

                    for (int k = 0; k < arrayCropName.size(); k++){
                        checkcropname = arrayCropName.get(k).toString();
                        for(int j=0;j<arrayAddRequiredNitrogen.size();j++) {

                            appDatabase.farmFertilizerDAO().updateFarmFertilizerrecommendedSumNPK(farmerID,yearValue, seasonValue, checkcropname,arrayAddRequiredNitrogen.get(j).toString()+"-"+ arrayAddRequiredPhospohorous.get(j).toString()+"-"+arrayAddRequiredPotash.get(j).toString());

                        }
                    }
                }
            });
            calculateNPKDB();
            calculateNPK();


        }
    }

    private void calculateNPK() {

        System.out.println("func calculateNPK ");
        System.out.println("arrayNutrientCombination.size() " + arrayNutrientCombination.size());
        for(int i=0;i<arrayNutrientCombination.size();i++){
            System.out.println("func calculateNPK for loop");
            MainViewModel viewFertilizerNPK = ViewModelProviders.of(this).get(MainViewModel.class);
            viewFertilizerNPK.getFertilizerNPK(arrayFertilizerIdCombination.get(i)).observe(this, new Observer<List<ModelFertilizerNameMaster>>()
            {
                @Override
                public void onChanged(@Nullable List<ModelFertilizerNameMaster> taskEntries) {

                    if (taskEntries != null && !taskEntries.isEmpty()){

                        for(ModelFertilizerNameMaster taskEntry:taskEntries){
                            //    System.out.println("taskEntry.getFertilizername() - " + taskEntry.getFertilizername());
                            //            Toast.makeText(mContext,"Fertilizers : " + taskEntry.getFertilizername(),Toast.LENGTH_SHORT).show();
                            //   arrayFertilizerName.add(taskEntry.getFertilizername());
                           /* arrayFinalNitrogenValue.add(taskEntry.getFertilizernitrogen());
                            arrayFinalPhospohorousValue.add(taskEntry.getFertilizerphosphorous());
                            arrayFinalPotashValue.add(taskEntry.getFertilizerpotash());*/
                            System.out.println("combination - " + taskEntry.getFertilizername() +" "+taskEntry.getFertilizernitrogen() + " "+taskEntry.getFertilizerphosphorous() + " "+taskEntry.getFertilizerpotash());
                            //    if(Integer.parseInt(taskEntry.getFertilizernitrogen())>Integer.parseInt(arrayFinalNitrogenValue.get(0)))

                           /* System.out.println("arrayFinalNitrogenValue - " + arrayFinalNitrogenValue);
                            System.out.println("arrayFinalPhospohorousValue - " + arrayFinalPhospohorousValue);
                            System.out.println("arrayFinalPotashValue - " + arrayFinalPotashValue);*/
                        }
                    }

                }
            });
        }
  /*      arrayFertilizerName.add(0,"MOP");
        arrayFertilizerName.add(1,"Urea");
        arrayFertilizerName.add(2,"DAP");
        arrayFinalNitrogenValue.add(0,"0");
        arrayFinalNitrogenValue.add(1,"46");
        arrayFinalNitrogenValue.add(2,"18");
        arrayFinalPhospohorousValue.add(0,"0");
        arrayFinalPhospohorousValue.add(1,"0");
        arrayFinalPhospohorousValue.add(2,"46");
        arrayFinalPotashValue.add(0,"60");
        arrayFinalPotashValue.add(1,"0");
        arrayFinalPotashValue.add(2,"0");*/
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //    caluculateFinalNPK();
                calculateFinalNPKDB();
            }
        }, 1000);

    }

    private void calculateNPKDB() {
        arrayCropExtentTotalNPK.clear();
        final CountDownLatch countDownLatch = new CountDownLatch(2);

        for (int i = 0; i < arrayCropName.size(); i++) {

            appDatabase.farmFertilizerDAO().getCropExtentTotalDataForNPK(farmerID,yearValue,seasonValue,arrayCropName.get(i)).observe(this, new Observer<List<ModelFarmFertilizer>>() {

                @Override
                public void onChanged(@Nullable List<ModelFarmFertilizer> modelFarmFertilizers) { //List<ModelFarmFertilizer> modelFarmFertilizers
                    if (modelFarmFertilizers != null && !modelFarmFertilizers.isEmpty()){
                        for(ModelFarmFertilizer taskEntry:modelFarmFertilizers){
                            countDownLatch.countDown();
                            cropextentTotalNPK = taskEntry.getCropextentTotalNPK();
                            System.out.println("taskEntry " + cropextentTotalNPK);
                            if(taskEntry.getCropextentTotalNPK()!=null){
                                arrayCropExtentTotalNPK.add(taskEntry.getCropextentTotalNPK());
                                Set<String> filterCropExtentTotalNPK;
                                filterCropExtentTotalNPK = new LinkedHashSet<String>(arrayCropExtentTotalNPK);
                                arrayCropExtentTotalNPK.clear();
                                arrayCropExtentTotalNPK.addAll(filterCropExtentTotalNPK);
                            }

                        }
                    }
                }
            });
            try {
                countDownLatch.await(1, TimeUnit.SECONDS);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            // arrayCropExtentTotalNPK.add(value);
        }

    }

    private void calculateFinalNPKDB(){

        System.out.println("arrayCropExtentTotalNPKsize " + arrayCropExtentTotalNPK.size() + " " + arrayCropExtentTotalNPK);

        for (int i = 0; i < arrayCropName.size(); i++) {

            arrayFertilizerName.add(0, "MOP");
            arrayFertilizerName.add(1, "Urea");
            arrayFertilizerName.add(2, "DAP");
            arrayFinalNitrogenValue.add(0, "0-46-18");
            arrayFinalNitrogenValue.add(1, "46-0-0");
            arrayFinalNitrogenValue.add(2, "18-46-0");
                  /*  arrayFinalPhospohorousValue.add(0,"0");
                    arrayFinalPhospohorousValue.add(1,"0");
                    arrayFinalPhospohorousValue.add(2,"46");
                    arrayFinalPotashValue.add(0,"60");
                    arrayFinalPotashValue.add(1,"0");
                    arrayFinalPotashValue.add(2,"0");*/
            for (int d = 1; d < arrayCropExtentTotalNPK.size(); d++) {

                String cropextnt = arrayCropExtentTotalNPK.get(d);
                String[] splitextent = cropextnt.split("\\-");
                String tempextent1 = splitextent[0];
                String tempextent2 = splitextent[1];
                String tempextent3 = splitextent[2];

                System.out.println("tempextent1 " + tempextent1 + " tempextent2 " + tempextent2 + " tempextent3 " + tempextent3);
                requiredNitrogen = Integer.parseInt(tempextent1);
                requiredPhosphorous = Integer.parseInt(tempextent2);
                requiredPotash = Integer.parseInt(tempextent3);
                for (int j = 0; j < arrayFertilizerName.size(); j++) {

                    String temp = arrayFinalNitrogenValue.get(j);

                    tempfmdata = temp.split("-");
                    fmNitrogen = Integer.parseInt(tempfmdata[0]);
                    fmPhosphorous = Integer.parseInt(tempfmdata[1]);
                    fmPotash = Integer.parseInt(tempfmdata[2]);

                      /*  fmNitrogen = Integer.parseInt(arrayFinalNitrogenValue.get(j));
                        fmPhosphorous = Integer.parseInt(arrayFinalPhospohorousValue.get(j));
                        fmPotash = Integer.parseInt(arrayFinalPotashValue.get(j));*/

                       /* System.out.println("fmNitrogen values " + fmNitrogen );
                        System.out.println("fmPhosphorous values " + fmPhosphorous );
                        System.out.println("fmPotash values " + fmPotash );*/

                    if (fmPhosphorous != 0 && fmNitrogen != 0 && fmPotash != 0) {
                        if (fmPhosphorous > fmNitrogen && fmPhosphorous > fmPotash && requiredPhosphorous != 0) {
                            phosphorous = (int) ((float) 100 / (float) fmPhosphorous * (requiredPhosphorous));
                            requiredPhosphorous = 0;
                            requiredNitrogen = (int) (requiredNitrogen - ((float) fmNitrogen / (float) 100 * phosphorous));
                            requiredPotash = (int) (requiredPotash - ((float) fmPotash / (float) 100 * phosphorous));
                        } else if (fmNitrogen > fmPhosphorous && fmPhosphorous > fmPotash && requiredPhosphorous != 0) {

                            nitrogen = (int) ((float) 100 / (float) fmNitrogen * (requiredNitrogen));
                            requiredNitrogen = 0;
                            requiredPhosphorous = (int) (requiredPhosphorous - ((float) fmPhosphorous / (float) 100 * nitrogen));
                            requiredPotash = (int) (requiredPotash - ((float) fmPotash / (float) 100 * nitrogen));

                        } else if (fmPotash > fmPhosphorous && fmPotash > fmNitrogen && requiredPotash != 0) {

                            potash = (int) ((float) 100 / (float) fmPotash * (requiredPotash));
                            requiredPotash = 0;
                            requiredNitrogen = (int) (requiredNitrogen - ((float) fmNitrogen / (float) 100 * potash));
                            requiredPhosphorous = (int) (requiredPhosphorous - ((float) fmPhosphorous / (float) 100 * potash));
                        } else if (requiredPhosphorous != 0) {
                            phosphorous = (int) ((float) 100 / (float) fmPhosphorous * (requiredPhosphorous));
                            requiredPhosphorous = 0;
                            requiredNitrogen = (int) (requiredNitrogen - ((float) fmNitrogen / (float) 100 * phosphorous));
                            requiredPotash = (int) (requiredPotash - ((float) fmPotash / (float) 100 * phosphorous));
                        }
                    } else if (fmPhosphorous != 0 && fmNitrogen != 0) {
                        if (fmPhosphorous > fmNitrogen && requiredPhosphorous != 0) {
                            phosphorous = (int) ((float) 100 / (float) fmPhosphorous * (requiredPhosphorous));
                            requiredPhosphorous = 0;
                            requiredNitrogen = (int) (requiredNitrogen - ((float) fmNitrogen / (float) 100 * phosphorous));
                        } else if (fmNitrogen > fmPhosphorous && requiredNitrogen != 0) {
                            nitrogen = (int) ((float) 100 / (float) fmNitrogen * (requiredNitrogen));
                            requiredNitrogen = 0;
                            requiredPhosphorous = (int) (requiredPhosphorous - ((float) fmPhosphorous / (float) 100 * nitrogen));
                        }
                    } else if (fmNitrogen != 0 && fmPotash != 0) {

                        if (fmNitrogen > fmPotash && requiredNitrogen != 0) {
                            nitrogen = (int) ((float) 100 / (float) fmNitrogen * (requiredNitrogen));
                            requiredNitrogen = 0;
                            requiredPotash = (int) (requiredPotash - ((float) fmPotash / (float) 100 * nitrogen));
                        } else if (fmPotash > fmNitrogen && requiredPotash != 0) {
                            potash = (int) ((float) 100 / (float) fmPotash * (requiredPotash));
                            requiredPotash = 0;
                            requiredNitrogen = (int) (requiredNitrogen - ((float) fmNitrogen / (float) 100 * potash));
                        }
                    } else if (fmPhosphorous != 0 && fmPotash != 0) {

                        if (fmPhosphorous > fmPotash && requiredPhosphorous != 0) {
                            phosphorous = (int) ((float) 100 / (float) fmPhosphorous * (requiredPhosphorous));
                            requiredPhosphorous = 0;
                            requiredPotash = (int) (requiredPotash - ((float) fmPotash / (float) 100 * phosphorous));
                        } else if (fmPotash > fmPhosphorous && requiredPotash != 0) {
                            potash = (int) ((float) 100 / (float) fmPotash * (requiredPotash));
                            requiredPotash = 0;
                            requiredPhosphorous = (int) (requiredPhosphorous - ((float) fmPhosphorous / (float) 100 * potash));
                        }
                    } else if (fmPhosphorous != 0 && requiredPhosphorous != 0) {
                        phosphorous = (int) ((float) 100 / (float) fmPhosphorous * (requiredPhosphorous));
                        requiredPhosphorous = 0;

                    } else if (fmNitrogen != 0 && requiredNitrogen != 0) {
                        nitrogen = (int) ((float) 100 / (float) fmNitrogen * requiredNitrogen);
                        requiredNitrogen = 0;
                    } else if (fmPotash != 0 && requiredPotash != 0) {
                        potash = (int) ((float) 100 / (float) fmPotash * (requiredPotash));
                        requiredPotash = 0;
                    }

                    if (phosphorous != 0) {
                        totalP = totalP + phosphorous;
                        finalDataObtained = phosphorous;
                        arrayPhosphorous.add(String.valueOf(totalP));
                    }
                    if (nitrogen != 0) {
                        totalN = totalN + nitrogen;
                        finalDataObtained = nitrogen;
                        arrayPhosphorous.add(String.valueOf(totalN));
                    }
                    if (potash != 0) {
                        totalK = totalK + potash;
                        finalDataObtained = potash;
                        arrayPhosphorous.add(String.valueOf(totalK));
                    }

                    System.out.println("requiredNitrogen - " + requiredNitrogen);

                    value = Double.valueOf(String.valueOf(arrayPhosphorous.get(j)));
                    bagValue = Math.round(value / 50);
                    arrayKGValue.add(String.valueOf((int)value));
                    if (value < 50) {
                        bagValue = 1;
                    }
                    arraybagValue.add(String.valueOf((int)bagValue));

                    linearLayoutFertilizerDetails.setVisibility(View.VISIBLE);

                    if(count == 0) {
                        adduserTable();
                        addFertilizerTable();
                        addNPKTable();
                    }

                    addNPKTableData(arrayFertilizerName.get(j),(int)value,(int)bagValue);
                    String cname = arrayCropName.get(i);

                    System.out.println("Fertilizername - " + arrayFertilizerName.get(j) + " KGs. - " + value + " bags - " + bagValue);

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            appDatabase.farmFertilizerDAO().updatefertilizername(farmerID,yearValue,seasonValue,cname,arrayFertilizerName.toString());
                            appDatabase.farmFertilizerDAO().updatekg(farmerID,yearValue,seasonValue,cname,arrayKGValue.toString());
                            appDatabase.farmFertilizerDAO().updatebags(farmerID,yearValue,seasonValue,cname,arraybagValue.toString());
                        }
                    });
                }
                addFertilizerTableData();
            }
            //addrecommendedTableData(displayRecommendedValue);
        }
        adduserTableData();


    }



    private void addNPKTableData(String fname,int value,int bagvalue) {
        System.out.println("addNPKTableData - " );

        TableRow tbrowData = new TableRow(mContext);
        TextView tvFertilizer = new TextView(mContext);
        TextView tvKG = new TextView(mContext);
        TextView tvBag = new TextView(mContext);

        tvFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvFertilizer.setGravity(Gravity.CENTER);
        tvFertilizer.setBackgroundColor(Color.parseColor("#FFFDE7"));
        tvFertilizer.setText(fname);
        tvFertilizer.setTextColor(Color.BLACK);
        tvFertilizer.setTextSize(18);

        tvKG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvKG.setGravity(Gravity.CENTER);
        tvKG.setBackgroundColor(Color.parseColor("#ECEFF1"));
        tvKG.setText(""+value);
        tvKG.setTextColor(Color.BLACK);
        tvKG.setTextSize(18);

        tvBag.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvBag.setGravity(Gravity.CENTER);
        tvBag.setBackgroundColor(Color.parseColor("#FFFDE7"));
        tvBag.setText(""+bagvalue);
        tvBag.setTextColor(Color.BLACK);
        tvBag.setTextSize(18);

        tbrowData.addView(tvFertilizer);
        tbrowData.addView(tvKG);
        tbrowData.addView(tvBag);
        tbNPKDetails.addView(tbrowData);

    }

    private void addNPKTable() {
        count = 1;

        TableRow tbrow= new TableRow(mContext);
        TextView tvFertilizer = new TextView(mContext);
        TextView tvKG = new TextView(mContext);
        TextView tvBag = new TextView(mContext);

        tvFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvFertilizer.setGravity(Gravity.CENTER);
        tvFertilizer.setBackgroundColor(Color.parseColor("#EF9A9A"));
        tvFertilizer.setText("Fertilizer");
        tvFertilizer.setTextColor(Color.BLACK);
        tvFertilizer.setTextSize(18);

        tvKG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvKG.setGravity(Gravity.CENTER);
        tvKG.setBackgroundColor(Color.parseColor("#448AFF"));
        tvKG.setText("KGs");
        tvKG.setTextColor(Color.BLACK);
        tvKG.setTextSize(18);

        tvBag.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvBag.setGravity(Gravity.CENTER);
        tvBag.setBackgroundColor(Color.parseColor("#EF9A9A"));
        tvBag.setText("Bags");
        tvBag.setTextColor(Color.BLACK);
        tvBag.setTextSize(18);

        tbrow.addView(tvFertilizer);
        tbrow.addView(tvKG);
        tbrow.addView(tvBag);

        tbNPKDetails.addView(tbrow);
    }

    private void addFertilizerTable() {
        count = 1;

        TableRow tbrow= new TableRow(mContext);
        TextView tvArea = new TextView(mContext);
        TextView tvNPK = new TextView(mContext);

        tvArea.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvArea.setGravity(Gravity.CENTER);
        tvArea.setBackgroundColor(Color.parseColor("#EF9A9A"));
        tvArea.setText("Crop Extent");
        tvArea.setTextColor(Color.BLACK);
        tvArea.setTextSize(18);

        tvNPK.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvNPK.setGravity(Gravity.CENTER);
        tvNPK.setBackgroundColor(Color.parseColor("#448AFF"));
        tvNPK.setText("Recommendation of NPK");
        tvNPK.setTextColor(Color.BLACK);
        tvNPK.setTextSize(18);

        tbrow.addView(tvArea);
        tbrow.addView(tvNPK);

        tbAreaDetails.addView(tbrow);

    }

    private void adduserTable() {
        count = 1;
        TableRow tbrow= new TableRow(mContext);
        TextView tvDistrict = new TextView(mContext);
        TextView tvTaluk = new TextView(mContext);
        TextView tvHobli = new TextView(mContext);
        TextView tvVillage = new TextView(mContext);
        TextView tvSurvey = new TextView(mContext);
        TextView tvCrop = new TextView(mContext);

        tvDistrict.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvDistrict.setGravity(Gravity.CENTER);
        tvDistrict.setBackgroundColor(Color.parseColor("#E6EE9C"));
        tvDistrict.setText("District");
        tvDistrict.setTextColor(Color.BLACK);
        tvDistrict.setTextSize(18);

        tvTaluk.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvTaluk.setGravity(Gravity.CENTER);
        tvTaluk.setBackgroundColor(Color.parseColor("#CFD8DC"));
        tvTaluk.setText("Taluk");
        tvTaluk.setTextColor(Color.BLACK);
        tvTaluk.setTextSize(18);

        tvHobli.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvHobli.setGravity(Gravity.CENTER);
        tvHobli.setBackgroundColor(Color.parseColor("#E6EE9C"));
        tvHobli.setText("Hobli");
        tvHobli.setTextColor(Color.BLACK);
        tvHobli.setTextSize(18);

        tvVillage.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvVillage.setGravity(Gravity.CENTER);
        tvVillage.setBackgroundColor(Color.parseColor("#CFD8DC"));
        tvVillage.setText("Village");
        tvVillage.setTextColor(Color.BLACK);
        tvVillage.setTextSize(18);

        tvSurvey.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvSurvey.setGravity(Gravity.CENTER);
        tvSurvey.setBackgroundColor(Color.parseColor("#E6EE9C"));
        tvSurvey.setText("Survey");
        tvSurvey.setTextColor(Color.BLACK);
        tvSurvey.setTextSize(18);

        tvCrop.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvCrop.setGravity(Gravity.CENTER);
        tvCrop.setBackgroundColor(Color.parseColor("#CFD8DC"));
        tvCrop.setText("Crop");
        tvCrop.setTextColor(Color.BLACK);
        tvCrop.setTextSize(18);

        tbrow.addView(tvDistrict);
        tbrow.addView(tvTaluk);
        tbrow.addView(tvHobli);
        tbrow.addView(tvVillage);
        tbrow.addView(tvSurvey);
        tbrow.addView(tvCrop);
        tbUserDetails.addView(tbrow);
    }

    private void adduserTableData() {

        for(int i=0;i<arrayCropName.size();i++) {
            for (int j = 0; j < arraySurvey.size(); j++) {
                TableRow tbrowData = new TableRow(mContext);
                TextView tvDistrictData = new TextView(mContext);
                TextView tvTalukData = new TextView(mContext);
                TextView tvHobliData = new TextView(mContext);
                TextView tvVillageData = new TextView(mContext);
                TextView tvSurveyData = new TextView(mContext);
                TextView tvCropData = new TextView(mContext);

                tvDistrictData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvDistrictData.setGravity(Gravity.CENTER);
                tvDistrictData.setBackgroundColor(Color.parseColor("#FFFDE7"));
                tvDistrictData.setText(arrayDistrict.get(i));
                tvDistrictData.setTextColor(Color.BLACK);
                tvDistrictData.setTextSize(18);

                tvTalukData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvTalukData.setGravity(Gravity.CENTER);
                tvTalukData.setBackgroundColor(Color.parseColor("#ECEFF1"));
                tvTalukData.setText(arrayTaluk.get(i));
                tvTalukData.setTextColor(Color.BLACK);
                tvTalukData.setTextSize(18);

                tvHobliData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvHobliData.setGravity(Gravity.CENTER);
                tvHobliData.setBackgroundColor(Color.parseColor("#FFFDE7"));
                tvHobliData.setText(arrayHobli.get(i));
                tvHobliData.setTextColor(Color.BLACK);
                tvHobliData.setTextSize(18);

                tvVillageData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvVillageData.setGravity(Gravity.CENTER);
                tvVillageData.setBackgroundColor(Color.parseColor("#ECEFF1"));
                tvVillageData.setText(arrayVillage.get(i));
                tvVillageData.setTextColor(Color.BLACK);
                tvVillageData.setTextSize(18);

                tvSurveyData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvSurveyData.setGravity(Gravity.CENTER);
                tvSurveyData.setBackgroundColor(Color.parseColor("#FFFDE7"));
                tvSurveyData.setText(arraySurvey.get(j));
                tvSurveyData.setTextColor(Color.BLACK);
                tvSurveyData.setTextSize(18);

                tvCropData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvCropData.setGravity(Gravity.CENTER);
                tvCropData.setBackgroundColor(Color.parseColor("#ECEFF1"));
                tvCropData.setText(arrayCropName.get(i));
                tvCropData.setTextColor(Color.BLACK);
                tvCropData.setTextSize(18);

                tbrowData.addView(tvDistrictData);
                tbrowData.addView(tvTalukData);
                tbrowData.addView(tvHobliData);
                tbrowData.addView(tvVillageData);
                tbrowData.addView(tvSurveyData);
                tbrowData.addView(tvCropData);

                tbUserDetails.addView(tbrowData);
            }

        }
      addrecommendedTableData();

    }

    private void addrecommendedTableData() {
    for(int i=0;i<arrayCropName.size();i++) {

          MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
         //   viewModel.getFarmFertilizerRecomendNPKdetail(farmerID,arrayCropName.get(j)).removeObservers(this);
            viewModel.getFarmFertilizerRecomendNPKdetail(farmerID,arrayCropName.get(i)).observe(this, new Observer<List<ModelFarmFertilizer>>() {

                @Override
                public void onChanged(@Nullable List<ModelFarmFertilizer> taskEntries) {

                    if(taskEntries==null){

                    }else {
                        if (taskEntries != null && !taskEntries.isEmpty()) {

                            for (ModelFarmFertilizer taskEntry : taskEntries) {

                                String[] value = taskEntry.getRecommendedNPK().split("-");
                                String temp1 = value[0];
                                String temp2 = value[1];
                                String temp3 = value[2];
                                System.out.println("temp1 " + temp1 + " temp2 " + temp2 + " temp3 "+ temp3);
                                TableRow tbrowData = new TableRow(mContext);
                                TextView tvRFertilizer = new TextView(mContext);

                                tvRFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvRFertilizer.setGravity(Gravity.CENTER);
                                tvRFertilizer.setBackgroundColor(Color.parseColor("#F3E5F5"));
                                tvRFertilizer.setText("Recommended Nutrient for 1 acre :" + taskEntry.getCropname() + "\nNitrogen:" + temp1 + ",Phosphorous:" + temp2 + ",Potash:" + temp3);
                                tvRFertilizer.setTextColor(Color.BLACK);
                                tvRFertilizer.setTextSize(18);

                                tbrowData.addView(tvRFertilizer);
                                tbNutrientDetail.addView(tbrowData);

                            }
                        }
                    }
                }
            });

    }

    }

    private void addFertilizerTableData() {
      for(int j=0;j<arrayCropName.size();j++) {
          for (int i = 0; i < arrayCropArea.size(); i++) {
              TableRow tbrowdataArea = new TableRow(mContext);
              TextView tvAreaData = new TextView(mContext);
              TextView tvNPKData = new TextView(mContext);

              tvAreaData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
              tvAreaData.setGravity(Gravity.CENTER);
              tvAreaData.setBackgroundColor(Color.parseColor("#FFEBEE"));
              tvAreaData.setText(arrayCropArea.get(i));
              tvAreaData.setTextColor(Color.BLACK);
              tvAreaData.setTextSize(18);

              tvNPKData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
              tvNPKData.setGravity(Gravity.CENTER);
              tvNPKData.setBackgroundColor(Color.parseColor("#E3F2FD"));
              tvNPKData.setText("Nitrogen: " + arrayRequiredNitrogen.get(i) + ", Phosphorous: " + arrayRequiredPhospohorous.get(i) + ", Potash: " + arrayRequiredPotash.get(i));
              tvNPKData.setTextColor(Color.BLACK);
              tvNPKData.setTextSize(18);

              tbrowdataArea.addView(tvAreaData);
              tbrowdataArea.addView(tvNPKData);
              tbAreaDetails.addView(tbrowdataArea);
          }
      }

    }

    private void clearViews() {
        int tablecount1 = tbUserDetails.getChildCount();
        for(int i =1 ;i<tablecount1;i++){
            View child = tbUserDetails.getChildAt(i);
            if(child instanceof TableRow)((ViewGroup) child).removeAllViews();
        }

        int tablecount2 = tbNutrientDetail.getChildCount();
        for(int i =0 ;i<tablecount2;i++){
            View child = tbNutrientDetail.getChildAt(i);
            if(child instanceof TableRow)((ViewGroup) child).removeAllViews();
        }

        int tablecount3 = tbAreaDetails.getChildCount();
        for(int i =1 ;i<tablecount3;i++){
            View child = tbAreaDetails.getChildAt(i);
            if(child instanceof TableRow)((ViewGroup) child).removeAllViews();
        }

        int tablecount4 = tbNPKDetails.getChildCount();
        for(int i =1 ;i<tablecount4;i++){
            View child = tbNPKDetails.getChildAt(i);
            if(child instanceof TableRow)((ViewGroup) child).removeAllViews();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (Build.VERSION.SDK_INT > 11) {
            menu.findItem(R.id.spinner).setVisible(false);
            invalidateOptionsMenu();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.sync:
                if (Utils.isNetworkConnected(mContext)){
                } else {
                    new AlertDialog.Builder(mContext)
                            .setTitle(R.string.Note)
                            .setMessage(R.string.internet)
                            .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
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
}
