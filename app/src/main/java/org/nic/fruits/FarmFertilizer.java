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
    String yearValue = "", seasonValue = "", cropNameValue = "";
    String districtValue = "",talukValue = "",hobliValue = "",villageValue = "",surveyValue = "",cropname = "", cropextentValue="";
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
    List<String> arrayAddRequiredNitrogen;
    List<String> arrayAddRequiredPhospohorous;
    List<String> arrayAddRequiredPotash;
    int sumNitrogen = 0;
    int sumPhospohorous= 0;
    int sumPotash = 0;

    List<String> arrayData;
    int totalPhosphorous =0;
    int totalNitrogen = 0;
    int totalPotash = 0;

    RelativeLayout rlTableUserDetails;
    RelativeLayout rlTableNutrientDetails;
    RelativeLayout rlTableArea;
    RelativeLayout rlTableFertilizer;
    TableLayout tbUserDetails;
    TableLayout tbNutrientDetail;
    TableLayout tbAreaDetails;
    TableLayout tbNPKDetails;
    int count =0;
    final Handler handler = new Handler();
    String cropExtentTNitrogen;
    String cropExtentTPhosphorous;
    String cropExtentTPotash;
    String recommendednpkvalue;
    String areaVal;
    List<String> arrayAllValues;
    String crpName="";
    String crpcodeFertilizer="";


    String [] tempfmdata1 = new String[0];
    int nitrogen1 = 0;
    int phosphorous1 = 0;
    int potash1 = 0;
    int fmPhosphorous1 =0;
    int fmNitrogen1 = 0;
    int fmPotash1 = 0;
    int requiredNitrogen1;
    int requiredPhosphorous1;
    int requiredPotash1;

    String [] tempfmdata2 = new String[0];
    int nitrogen2 = 0;
    int phosphorous2 = 0;
    int potash2 = 0;
    int fmPhosphorous2 =0;
    int fmNitrogen2 = 0;
    int fmPotash2 = 0;
    int requiredNitrogen2;
    int requiredPhosphorous2;
    int requiredPotash2;


    String [] tempfmdata3 = new String[0];
    int nitrogen3 = 0;
    int phosphorous3 = 0;
    int potash3 = 0;
    int fmPhosphorous3 =0;
    int fmNitrogen3 = 0;
    int fmPotash3 = 0;
    int requiredNitrogen3;
    int requiredPhosphorous3;
    int requiredPotash3;


    String [] tempfmdata4 = new String[0];
    int nitrogen4 = 0;
    int phosphorous4 = 0;
    int potash4 = 0;
    int fmPhosphorous4 =0;
    int fmNitrogen4 = 0;
    int fmPotash4 = 0;
    int requiredNitrogen4;
    int requiredPhosphorous4;
    int requiredPotash4;

    List<String> arrayFertilizerName1;
    List<String> arrayFertilizerName2;
    List<String> arrayFertilizerName3;
    List<String> arrayFertilizerName4;
    List<String> arrayFertilizerCode1;
    List<String> arrayFertilizerCode2;
    List<String> arrayFertilizerCode3;
    List<String> arrayFertilizerCode4;

    double value1 = 0.0;
    double bagValue1 = 0.0;
    List<String> arrayKGValue1;
    List<String> arraybagValue1;
    int finalDataObtained1 = 0;

    double value2 = 0.0;
    double bagValue2 = 0.0;
    List<String> arrayKGValue2;
    List<String> arraybagValue2;
    int finalDataObtained2 = 0;

    double value3 = 0.0;
    double bagValue3 = 0.0;
    List<String> arrayKGValue3;
    List<String> arraybagValue3;
    int finalDataObtained3 = 0;

    double value4 = 0.0;
    double bagValue4 = 0.0;
    List<String> arrayKGValue4;
    List<String> arraybagValue4;
    int finalDataObtained4 = 0;

    int totalNitro = 0;
    int totalPhos = 0;
    int totalPotas = 0;

    List<String> arrayFertilizerCalculate1;
    List<String> arrayFertilizerCalculate2;
    List<String> arrayFertilizerCalculate3;
    List<String> arrayFertilizerCalculate4;

    Set<String> npk1;
    Set<String> npk2;
    Set<String> npk3;
    Set<String> npk4;
    int combiCount = 0;
    int combiCount2 = 0;
    int combiCount3 = 0;
    int combiCount4 = 0;
    int combinationcountValue = 0;
    List<Integer> arrayNutrientValue1;
    List<Integer> arrayNutrientValue2;

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

        rlTableUserDetails= findViewById(R.id.rlUserDetails);
        rlTableNutrientDetails = findViewById(R.id.rlNutrientdetails);
        rlTableArea = findViewById(R.id.rlAreaDetails);
        rlTableFertilizer = findViewById(R.id.rlnpkdetails);
        tbUserDetails = findViewById(R.id.tbUserDetail);
        tbNutrientDetail = findViewById(R.id.tbNutrientdetails);
        tbAreaDetails = findViewById(R.id.tbAreaDetail);
        tbNPKDetails = findViewById(R.id.tbnpkdetails);

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
        arrayFertilizerCode1 = new ArrayList<>();
        arrayFertilizerCode2 = new ArrayList<>();
        arrayFertilizerCode3 = new ArrayList<>();
        arrayFertilizerCode4 = new ArrayList<>();
        arrayData = new ArrayList<>();
        arrayFertilizerName1 = new ArrayList<>();
        arrayFertilizerName2 = new ArrayList<>();
        arrayFertilizerName3 = new ArrayList<>();
        arrayFertilizerName4 = new ArrayList<>();
        arrayFertilizerCalculate1= new ArrayList<>();
        arrayFertilizerCalculate2= new ArrayList<>();
        arrayFertilizerCalculate3= new ArrayList<>();
        arrayFertilizerCalculate4= new ArrayList<>();
        arrayNutrientValue1 = new ArrayList<>();
        arrayNutrientValue2 = new ArrayList<>();
        arrayKGValue1 = new ArrayList<>();
        arraybagValue1 = new ArrayList<>();

        arrayKGValue2 = new ArrayList<>();
        arraybagValue2 = new ArrayList<>();

        arrayKGValue3 = new ArrayList<>();
        arraybagValue3 = new ArrayList<>();

        arrayKGValue4 = new ArrayList<>();
        arraybagValue4 = new ArrayList<>();

        arrayAllValues = new ArrayList<>();

        arrayFertilizerName1.clear();
        arrayFertilizerName2.clear();
        arrayFertilizerName3.clear();
        arrayFertilizerName4.clear();
        arrayFertilizerCode1.clear();
        arrayFertilizerCode2.clear();
        arrayFertilizerCode3.clear();
        arrayFertilizerCode4.clear();

        arrayFertilizerName1.add(0, "Urea");
        arrayFertilizerName1.add(1, "MOP");
        arrayFertilizerName1.add(2, "SSP-Granular");
        arrayFertilizerName2.add(0, "Urea");
        arrayFertilizerName2.add(1, "DAP");
        arrayFertilizerName2.add(2, "MOP");
        arrayNutrientValue1.add(0,1);
        arrayNutrientValue1.add(1,2);
        arrayNutrientValue1.add(2,1);
        arrayFertilizerName3.add(0, "SSP-Granular");
        arrayFertilizerName3.add(1, "Urea");
        arrayFertilizerName3.add(2, "MOP");
        arrayFertilizerName4.add(0, "SSP-Granular");
        arrayFertilizerName4.add(1, "10-26-26");
        arrayNutrientValue2.add(0,1);
        arrayNutrientValue2.add(1,3);
        arrayFertilizerCode1.add(0, "46-0-0");
        arrayFertilizerCode1.add(1, "0-0-60");
        arrayFertilizerCode1.add(2, "0-16-0");
        arrayFertilizerCode2.add(0, "46-0-0");
        arrayFertilizerCode2.add(1, "18-46-0");
        arrayFertilizerCode2.add(2, "0-0-60");
        arrayFertilizerCode3.add(0, "0-16-0");
        arrayFertilizerCode3.add(1, "46-0-0");
        arrayFertilizerCode3.add(2, "0-0-60");
        arrayFertilizerCode4.add(0, "0-16-0");
        arrayFertilizerCode4.add(1, "10-26-26");

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
                            count = 0;
                            sumNitrogen = 0;
                            sumPhospohorous = 0;
                            sumPotash = 0;
                            arrayData.clear();
                            finalDataObtained1 = 0;
                            finalDataObtained2 = 0;
                            finalDataObtained3 = 0;
                            finalDataObtained4 = 0;
                            totalPhosphorous = 0;
                            totalNitrogen = 0;
                            totalPotash = 0;

                            nitrogen1 = 0;
                            phosphorous1 = 0;
                            potash1= 0;
                            fmNitrogen1 = 0;
                            fmPhosphorous1 = 0;
                            fmPotash1 = 0;

                            nitrogen2 = 0;
                            phosphorous2 = 0;
                            potash2 = 0;
                            fmNitrogen2 = 0;
                            fmPhosphorous2 = 0;
                            fmPotash2 = 0;

                            nitrogen3 = 0;
                            phosphorous3 = 0;
                            potash3= 0;
                            fmNitrogen3 = 0;
                            fmPhosphorous3 = 0;
                            fmPotash3 = 0;

                            nitrogen4 = 0;
                            phosphorous4 = 0;
                            potash4 = 0;
                            fmNitrogen4 = 0;
                            fmPhosphorous4 = 0;
                            fmPotash4 = 0;

                            totalNitro = 0;
                            totalPhos = 0;
                            totalPotas = 0;
                            combiCount = 1;
                            combiCount2 = 1;
                            combiCount3 = 1;
                            combiCount4 = 1;
                            combinationcountValue = 0;
                            arrayFertilizerCalculate1.clear();
                            arrayNutrientValue1.clear();
                            arrayNutrientValue2.clear();
                            linearLayoutFertilizerDetails.setVisibility(View.GONE);
                            clearViews();
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
                                count = 0;
                                sumNitrogen = 0;
                                sumPhospohorous = 0;
                                sumPotash = 0;
                                arrayData.clear();
                                finalDataObtained1 = 0;
                                finalDataObtained2 = 0;
                                finalDataObtained3 = 0;
                                finalDataObtained4 = 0;
                                totalPhosphorous = 0;
                                totalNitrogen = 0;
                                totalPotash = 0;

                                nitrogen1 = 0;
                                phosphorous1 = 0;
                                potash1= 0;
                                fmNitrogen1 = 0;
                                fmPhosphorous1 = 0;
                                fmPotash1 = 0;

                                nitrogen2 = 0;
                                phosphorous2 = 0;
                                potash2 = 0;
                                fmNitrogen2 = 0;
                                fmPhosphorous2 = 0;
                                fmPotash2 = 0;

                                nitrogen3 = 0;
                                phosphorous3 = 0;
                                potash3= 0;
                                fmNitrogen3 = 0;
                                fmPhosphorous3 = 0;
                                fmPotash3 = 0;

                                nitrogen4 = 0;
                                phosphorous4 = 0;
                                potash4 = 0;
                                fmNitrogen4 = 0;
                                fmPhosphorous4 = 0;
                                fmPotash4 = 0;
                                combiCount = 1;
                                combiCount2 = 1;
                                combiCount3 = 1;
                                combiCount4 = 1;

                                combinationcountValue = 0;
                               /* totalNitro = 0;
                                totalPhos = 0;
                                totalPotas = 0;*/
                                arrayFertilizerCalculate1.clear();
                                linearLayoutFertilizerDetails.setVisibility(View.GONE);
                                arrayNutrientValue1.clear();
                                arrayNutrientValue2.clear();
                                clearViews();

                                Toast toast = Toast.makeText(mContext, "Fertilizers are being calculated... please wait", Toast.LENGTH_LONG);
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

                                /*arrayCropName.add(0,"Coconut");
                                arrayCropName.add(1,"Groundnut");*/
                                arrayCropName.add(taskEntry.getCropName());
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
                            else {
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

                                 /*arrayCropName.add(0,"Coconut");
                                arrayCropName.add(1,"Groundnut");*/
                                arrayCropName.add(taskEntry.getCropName());
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
        System.out.println("arraycrpname size- " + arrayCropName.size());
        for(int k=0 ;k<arrayCropName.size();k++) {

            cropNameValue = arrayCropName.get(k);
            System.out.println("arraycrpname - " + arrayCropName.get(k));
            MainViewModel viewFertilizerCropMaster = ViewModelProviders.of(this).get(MainViewModel.class);
            viewFertilizerCropMaster.getFertilizerCrops(arrayCropName.get(k)).observe(this, new Observer<List<ModelFertilizerCropMaster>>() {
                @Override
                public void onChanged(@Nullable List<ModelFertilizerCropMaster> taskEntries) {

                    if (taskEntries != null && !taskEntries.isEmpty()) {

                        for (ModelFertilizerCropMaster taskEntry : taskEntries) {

                            if (locale.toString().equals("en")) {

                            /*arrayFertilizerCropCode.add(0,"35");
                            arrayFertilizerCropCode.add(1,"11");*/
                                arrayFertilizerCropCode.add(taskEntry.getCropcode());
                                Set<String> filterCropCode;
                                filterCropCode = new LinkedHashSet<String>(arrayFertilizerCropCode);
                                arrayFertilizerCropCode.clear();
                                arrayFertilizerCropCode.addAll(filterCropCode);
                                arrayFertilizerCropType.add(taskEntry.getCroptype());
                                Set<String> filterCropType;
                                filterCropType = new LinkedHashSet<String>(arrayFertilizerCropType);
                                arrayFertilizerCropType.clear();
                                arrayFertilizerCropType.addAll(filterCropType);
                                System.out.println("calling  cropNameValue " + cropNameValue);
                                System.out.println("calling  arrayFertilizerCropCode " + arrayFertilizerCropCode);
                            } else {

                             /*arrayFertilizerCropCode.add(0,"35");
                            arrayFertilizerCropCode.add(1,"11");*/
                                arrayFertilizerCropCode.add(taskEntry.getCropcode());
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
                    } else {
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
       /* for (int l = 0; l < arrayCropName.size(); l++) {
            crpName = arrayCropName.get(l).toString();
            crpcodeFertilizer = arrayFertilizerCropCode.get(l).toString();
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.farmFertilizerDAO().updateFarmFertilizerCropCode(farmerID, crpName, crpcodeFertilizer);
                }
            });
            getCropNPK(crpName,crpcodeFertilizer);
        }*/

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateFarmfertlizer();
            }
        }, 5000);




    }

    private void updateFarmfertlizer() {
        for(int k=0 ;k<arrayCropName.size();k++) {
            crpName = arrayCropName.get(k).toString();
            crpcodeFertilizer = arrayFertilizerCropCode.get(k).toString();
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.farmFertilizerDAO().updateFarmFertilizerCropCode(farmerID, crpName, crpcodeFertilizer);
                }
            });
            getCropNPK(crpName,crpcodeFertilizer);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                calculateFinalNPKDB();
            }
        }, 5000);
    }

    private void getCropNPK(String cropName,String fertilizercropcode) {

        MainViewModel viewFertilizerCropMaster = ViewModelProviders.of(this).get(MainViewModel.class);
        viewFertilizerCropMaster.getCropNPK(fertilizercropcode).observe(this, new Observer<List<ModelCropFertilizerMasternpk>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropFertilizerMasternpk> taskEntries) {

                if (taskEntries != null && !taskEntries.isEmpty()) {

                    for (ModelCropFertilizerMasternpk taskEntry : taskEntries) {

                        if (locale.toString().equals("en")) {

                            arrayFertilizerCropNitrogen.add(taskEntry.getCf_nitrogen());
                            System.out.println("in arrayFertilizerCropNitrogen "+ arrayFertilizerCropNitrogen + " "+ arrayFertilizerCropNitrogen.size());
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

                            for(int j=0;j<arrayFertilizerCropNitrogen.size();j++) {

                                String nitrogen = arrayFertilizerCropNitrogen.get(j);
                                String phosphorous = arrayFertilizerCropPhospohorous.get(j);
                                String potash = arrayFertilizerCropPotash.get(j);
                                appDatabase.farmFertilizerDAO().updateFarmFertilizerrecommendedNPK(farmerID,yearValue, seasonValue, cropName,nitrogen+"-"+phosphorous+"-"+potash);

                            }
                        }
                    });
                    //          arrayRequiredNitrogen.clear();
                    //           arrayRequiredPhospohorous.clear();
                    //            arrayRequiredPotash.clear();
                    arrayAddRequiredNitrogen.clear();
                    arrayAddRequiredPhospohorous.clear();
                    arrayAddRequiredPotash.clear();
                    calculaterequiredNPK(cropName);
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

    private void calculaterequiredNPK(String cropnames) {
        if(cropNameValue!=null){
            System.out.println("in calculaterequiredNPK - " + arrayCropArea);
            System.out.println("in size - " + arrayCropArea.size());
            sumNitrogen = 0;
            sumPhospohorous = 0;
            sumPotash = 0;

            for (int i = 0; i < arrayCropArea.size(); i++) {
                areaVal = arrayCropArea.get(i);
                String temp = String.valueOf(arrayCropArea.get(i));
                String[] temp2 = temp.split("\\.");
                System.out.println("temp: " + temp);
                System.out.println("temp2: " + temp2[0] + " " + temp2[1] + " " + temp2[3]);

                double totalArea = Integer.parseInt(temp2[0]) + (Float.parseFloat(temp2[1]) / 40.0);
                System.out.println("totalArea - " + totalArea);

                System.out.println("arycropname " + cropnames);
                String fertilizerCropNitrogen="";
                String fertilizerCropPhospohorous="";
                String fertilizerCropPotash="";

                for (int s = 0; s < arrayFertilizerCropNitrogen.size(); s++) {
                    System.out.println("arycropname " + cropnames);
                    fertilizerCropNitrogen = String.valueOf((int) Math.round(Integer.parseInt(arrayFertilizerCropNitrogen.get(s).toString()) * totalArea));
                    fertilizerCropPhospohorous = String.valueOf((int) Math.round(Integer.parseInt(arrayFertilizerCropPhospohorous.get(s).toString()) * totalArea));
                    fertilizerCropPotash = String.valueOf((int) Math.round(Integer.parseInt(arrayFertilizerCropPotash.get(s).toString()) * totalArea));

                    recommendednpkvalue = " - Nitrogen : " + arrayFertilizerCropNitrogen.get(s) + ", Phospohorous : " + arrayFertilizerCropPhospohorous.get(s) + ", Potash : " + arrayFertilizerCropPotash.get(s);
                }

                arrayRequiredNitrogen.add(fertilizerCropNitrogen);
                arrayRequiredPhospohorous.add(fertilizerCropPhospohorous);
                arrayRequiredPotash.add(fertilizerCropPotash);


                arrayAllValues.add(arrayRequiredNitrogen.get(i)+"-"+arrayRequiredPhospohorous.get(i)+"-"+arrayRequiredPotash.get(i));

                cropExtentTNitrogen = arrayRequiredNitrogen.get(i);
                cropExtentTPhosphorous = arrayRequiredPhospohorous.get(i);
                cropExtentTPotash = arrayRequiredPotash.get(i);

                sumNitrogen = sumNitrogen + Integer.parseInt(arrayRequiredNitrogen.get(i));
                sumPhospohorous = sumPhospohorous + Integer.parseInt(arrayRequiredPhospohorous.get(i));
                sumPotash = sumPotash + Integer.parseInt(arrayRequiredPotash.get(i));

            }

            for(int i=0;i<arrayAllValues.size();i++){
                String arrayValues = arrayAllValues.get(i);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {

                    @Override
                    public void run() {
                        appDatabase.farmFertilizerDAO().updateFarmFertilizerrecommendedCENPK(farmerID, yearValue, seasonValue, cropnames, areaVal, arrayValues);
                    }
                });
            }
            arrayAddRequiredNitrogen.add(String.valueOf(sumNitrogen));
            arrayAddRequiredPhospohorous.add(String.valueOf(sumPhospohorous));
            arrayAddRequiredPotash.add(String.valueOf(sumPotash));

        }
        AppExecutors.getInstance().diskIO().execute(new Runnable() {

            @Override
            public void run() {
                for(int j=0;j<arrayAddRequiredNitrogen.size();j++) {
                    appDatabase.farmFertilizerDAO().updateFarmFertilizerrecommendedSumNPK(farmerID, yearValue, seasonValue, cropnames, arrayAddRequiredNitrogen.get(j).toString() + "-" + arrayAddRequiredPhospohorous.get(j).toString() + "-" + arrayAddRequiredPotash.get(j).toString());
                }
            }
        });



   //     calculateNPKDB();
     //   calculateNPK();
    }

    private void calculateFinalNPKDB(){
        linearLayoutFertilizerDetails.setVisibility(View.VISIBLE);
        if(count == 0) {
            adduserTable();
            //  addrecommendedTable();
            addFertilizerTable();
            addNPKTable();

        }
        totalNitro = 0;
        totalPhos = 0;
        totalPotas =0;
        requiredNitrogen1 = 0;
        requiredPhosphorous1 = 0;
        requiredPotash1 = 0;
        requiredNitrogen2 = 0;
        requiredPhosphorous2 = 0;
        requiredPotash2 = 0;
        requiredNitrogen3 = 0;
        requiredPhosphorous3 = 0;
        requiredPotash3 = 0;
        requiredNitrogen4 = 0;
        requiredPhosphorous4 = 0;
        requiredPotash4 = 0;
        for(int j=0;j<arrayRequiredNitrogen.size();j++) {
            totalNitro = totalNitro + Integer.parseInt(arrayRequiredNitrogen.get(j));
            totalPhos = totalPhos + Integer.parseInt(arrayRequiredPhospohorous.get(j));
            totalPotas = totalPotas + Integer.parseInt(arrayRequiredPotash.get(j));

        }
        requiredNitrogen1 = totalNitro;
        requiredPhosphorous1 = totalPhos;
        requiredPotash1 = totalPotas;
        requiredNitrogen2 = totalNitro;
        requiredPhosphorous2 = totalPhos;
        requiredPotash2 = totalPotas;
        requiredNitrogen3 = totalNitro;
        requiredPhosphorous3 = totalPhos;
        requiredPotash3 = totalPotas;
        requiredNitrogen4 = totalNitro;
        requiredPhosphorous4 = totalPhos;
        requiredPotash4 = totalPotas;

        System.out.println("Nitrogen: " + totalNitro + ", Phosphorous: " + totalPhos + ", Potash: " + totalPotas);
        System.out.println("requiredNitrogen1: " + requiredNitrogen1 + ", requiredPhosphorous1: " + requiredPhosphorous1 + ", requiredPotash1: " + requiredPotash1);

        TableRow tbrowdataArea = new TableRow(mContext);
        TextView tvNPKData = new TextView(mContext);
        tvNPKData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvNPKData.setGravity(Gravity.CENTER);
        tvNPKData.setBackgroundColor(Color.parseColor("#CFD8DC"));
        tvNPKData.setText("Nitrogen: " + totalNitro + ", Phosphorous: " + totalPhos + ", Potash: " + totalPotas);
        tvNPKData.setTextColor(Color.BLACK);
        tvNPKData.setTextSize(18);
        tbrowdataArea.addView(tvNPKData);
        tbAreaDetails.addView(tbrowdataArea);

        calculatefertilizer1();
        calculatefertilizer2();
        calculatefertilizer3();
        calculatefertilizer4();


    }

    private void calculatefertilizer1() {

        if(finalDataObtained1==0) {
            arrayFertilizerCalculate1.clear();
            System.out.println("arrayFertilizerName1 - " + arrayFertilizerName1.size());
            for (int j = 0; j < arrayFertilizerName1.size(); j++) {

                String temp = arrayFertilizerCode1.get(j);

                tempfmdata1 = temp.split("-");
                fmNitrogen1 = Integer.parseInt(tempfmdata1[0]);
                fmPhosphorous1 = Integer.parseInt(tempfmdata1[1]);
                fmPotash1 = Integer.parseInt(tempfmdata1[2]);
                System.out.println("calculatefertilizer1 - ");

                if (fmPhosphorous1 != 0 && fmNitrogen1 != 0 && fmPotash1 != 0) {
                    if (fmPhosphorous1 > fmNitrogen1 && fmPhosphorous1 > fmPotash1 && requiredPhosphorous1 != 0) {
                        phosphorous1 = (int) ((float) 100 / (float) fmPhosphorous1 * (requiredPhosphorous1));
                        requiredPhosphorous1 = 0;
                        requiredNitrogen1 = (int) (requiredNitrogen1 - ((float) fmNitrogen1 / (float) 100 * phosphorous1));
                        requiredPotash1 = (int) (requiredPotash1 - ((float) fmPotash1 / (float) 100 * phosphorous1));
                    } else if (fmNitrogen1 > fmPhosphorous1 && fmNitrogen1 > fmPotash1 && requiredNitrogen1 != 0) {

                        nitrogen1 = (int) ((float) 100 / (float) fmNitrogen1 * (requiredNitrogen1));
                        requiredNitrogen1 = 0;
                        requiredPhosphorous1 = (int) (requiredPhosphorous1 - ((float) fmPhosphorous1 / (float) 100 * nitrogen1));
                        requiredPotash1 = (int) (requiredPotash1 - ((float) fmPotash1 / (float) 100 * nitrogen1));

                    } else if (fmPotash1 > fmPhosphorous1 && fmPotash1 > fmNitrogen1 && requiredPotash1 != 0) {

                        potash1 = (int) ((float) 100 / (float) fmPotash1 * (requiredPotash1));
                        requiredPotash1 = 0;
                        requiredNitrogen1 = (int) (requiredNitrogen1 - ((float) fmNitrogen1 / (float) 100 * potash1));
                        requiredPhosphorous1 = (int) (requiredPhosphorous1 - ((float) fmPhosphorous1 / (float) 100 * potash1));
                    } else if (requiredPhosphorous1 != 0) {
                        phosphorous1 = (int) ((float) 100 / (float) fmPhosphorous1 * (requiredPhosphorous1));
                        requiredPhosphorous1 = 0;
                        requiredNitrogen1 = (int) (requiredNitrogen1 - ((float) fmNitrogen1 / (float) 100 * phosphorous1));
                        requiredPotash1 = (int) (requiredPotash1 - ((float) fmPotash1 / (float) 100 * phosphorous1));
                    }
                } else if (fmPhosphorous1 != 0 && fmNitrogen1 != 0) {
                    if (fmPhosphorous1 > fmNitrogen1 && requiredPhosphorous1 != 0) {
                        phosphorous1 = (int) ((float) 100 / (float) fmPhosphorous1 * (requiredPhosphorous1));
                        requiredPhosphorous1 = 0;
                        requiredNitrogen1 = (int) (requiredNitrogen1 - ((float) fmNitrogen1 / (float) 100 * phosphorous1));
                    } else if (fmNitrogen1 > fmPhosphorous1 && requiredNitrogen1 != 0) {
                        nitrogen1 = (int) ((float) 100 / (float) fmNitrogen1 * (requiredNitrogen1));
                        requiredNitrogen1 = 0;
                        requiredPhosphorous1 = (int) (requiredPhosphorous1 - ((float) fmPhosphorous1 / (float) 100 * nitrogen1));
                    }
                } else if (fmNitrogen1 != 0 && fmPotash1 != 0) {

                    if (fmNitrogen1 > fmPotash1 && requiredNitrogen1 != 0) {
                        nitrogen1 = (int) ((float) 100 / (float) fmNitrogen1 * (requiredNitrogen1));
                        requiredNitrogen1 = 0;
                        requiredPotash1 = (int) (requiredPotash1 - ((float) fmPotash1 / (float) 100 * nitrogen1));
                    } else if (fmPotash1 > fmNitrogen1 && requiredPotash1 != 0) {
                        potash1 = (int) ((float) 100 / (float) fmPotash1 * (requiredPotash1));
                        requiredPotash1 = 0;
                        requiredNitrogen1 = (int) (requiredNitrogen1 - ((float) fmNitrogen1 / (float) 100 * potash1));
                    }
                } else if (fmPhosphorous1 != 0 && fmPotash1 != 0) {

                    if (fmPhosphorous1 > fmPotash1 && requiredPhosphorous1 != 0) {
                        phosphorous1 = (int) ((float) 100 / (float) fmPhosphorous1 * (requiredPhosphorous1));
                        requiredPhosphorous1 = 0;
                        requiredPotash1 = (int) (requiredPotash1 - ((float) fmPotash1 / (float) 100 * phosphorous1));
                    } else if (fmPotash1 > fmPhosphorous1 && requiredPotash1 != 0) {
                        potash1 = (int) ((float) 100 / (float) fmPotash1 * (requiredPotash1));
                        requiredPotash1 = 0;
                        requiredPhosphorous1 = (int) (requiredPhosphorous1 - ((float) fmPhosphorous1 / (float) 100 * potash1));
                    }
                } else if (fmPhosphorous1 != 0 && requiredPhosphorous1 != 0) {
                    phosphorous1 = (int) ((float) 100 / (float) fmPhosphorous1 * (requiredPhosphorous1));
                    requiredPhosphorous1 = 0;

                } else if (fmNitrogen1 != 0 && requiredNitrogen1 != 0) {
                    nitrogen1 = (int) ((float) 100 / (float) fmNitrogen1 * requiredNitrogen1);
                    requiredNitrogen1 = 0;
                } else if (fmPotash1 != 0 && requiredPotash1 != 0) {
                    potash1 = (int) ((float) 100 / (float) fmPotash1 * (requiredPotash1));
                    requiredPotash1 = 0;
                }

                if (phosphorous1 != 0) {
               //     totalP1 = totalP1 + phosphorous1;
                    System.out.println("phosphorous1 - " + phosphorous1);
                    finalDataObtained1 = phosphorous1;
                    arrayFertilizerCalculate1.add(String.valueOf(finalDataObtained1));
                }
                if (nitrogen1 != 0) {
                 //   totalN1 = totalN1 + nitrogen1;
                    System.out.println("nitrogen1 - " + nitrogen1);
                    finalDataObtained1 = nitrogen1;
                    arrayFertilizerCalculate1.add(String.valueOf(finalDataObtained1));
                }
                if (potash1 != 0) {
                  //  totalK1 = totalK1 + potash1;
                    System.out.println("potash1 - " + potash1);
                    finalDataObtained1 = potash1;
                    arrayFertilizerCalculate1.add(String.valueOf(finalDataObtained1));
                }

            }
        }
        System.out.println("requiredNitrogen1 - " + requiredNitrogen1 + " requiredPhosphorous1 " + requiredPhosphorous1 + " requiredPotash1 " );
        if (requiredNitrogen1 == 0 && requiredPhosphorous1 == 0 && requiredPotash1 == 0) {
            npk1 = new LinkedHashSet<String>(arrayFertilizerCalculate1);

            arrayFertilizerCalculate1.clear();
            arrayFertilizerCalculate1.addAll(npk1);



            for (int j = 0; j < arrayFertilizerName1.size(); j++) {
                System.out.println("arrayFertilizerCalculate1 - " + arrayFertilizerCalculate1);
                value1 = Double.valueOf(String.valueOf(arrayFertilizerCalculate1.get(j)));
                bagValue1 = Math.round(value1 / 50);
                arrayKGValue1.add(String.valueOf((int) value1));
                if (value1 < 50) {
                    bagValue1 = 1;
                }
                arraybagValue1.add(String.valueOf((int) bagValue1));

                TableRow tbrowData = new TableRow(mContext);
                TextView tvCombination = new TextView(mContext);

                if(combiCount == 1) {
                    combinationcountValue++;
                    tvCombination.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tvCombination.setGravity(Gravity.CENTER);
                    tvCombination.setBackgroundColor(Color.parseColor("#BA68C8"));
                    tvCombination.setText("Combination " + combinationcountValue);
                    tvCombination.setTextColor(Color.BLACK);
                    tvCombination.setTextSize(18);
                    combiCount++;
                }else{
                    tvCombination.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tvCombination.setGravity(Gravity.CENTER);
                    tvCombination.setBackgroundColor(Color.parseColor("#BA68C8"));
                    tvCombination.setText("");
                    tvCombination.setTextColor(Color.BLACK);
                    tvCombination.setTextSize(18);
                }


                TextView tvFertilizer = new TextView(mContext);
                TextView tvKG = new TextView(mContext);
                TextView tvBag = new TextView(mContext);

                tvFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvFertilizer.setGravity(Gravity.CENTER);
                tvFertilizer.setBackgroundColor(Color.parseColor("#BA68C8"));
                tvFertilizer.setText(arrayFertilizerName1.get(j));
                tvFertilizer.setTextColor(Color.BLACK);
                tvFertilizer.setTextSize(18);

                tvKG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvKG.setGravity(Gravity.CENTER);
                tvKG.setBackgroundColor(Color.parseColor("#BA68C8"));
                tvKG.setText("" + (int) value1);
                tvKG.setTextColor(Color.BLACK);
                tvKG.setTextSize(18);

                tvBag.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvBag.setGravity(Gravity.CENTER);
                tvBag.setBackgroundColor(Color.parseColor("#BA68C8"));
                tvBag.setText("" + (int) bagValue1);
                tvBag.setTextColor(Color.BLACK);
                tvBag.setTextSize(18);

                tbrowData.addView(tvCombination);
                tbrowData.addView(tvFertilizer);
                tbrowData.addView(tvKG);
                tbrowData.addView(tvBag);
                tbNPKDetails.addView(tbrowData);

            }

        }else{
          /*  for (int j = 0; j < arrayFertilizerName1.size(); j++) {
                TableRow tbrowData = new TableRow(mContext);
                TextView tvCombination = new TextView(mContext);
                TextView tvFertilizer = new TextView(mContext);
                TextView tvKG = new TextView(mContext);
                TextView tvBag = new TextView(mContext);

                tvCombination.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvCombination.setGravity(Gravity.CENTER);
                tvCombination.setBackgroundColor(Color.parseColor("#CFD8DC"));
                tvCombination.setText("Combination 1");
                tvCombination.setTextColor(Color.BLACK);
                tvCombination.setTextSize(18);

                tvFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvFertilizer.setGravity(Gravity.CENTER);
                tvFertilizer.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvFertilizer.setText(arrayFertilizerName1.get(j));
                tvFertilizer.setTextColor(Color.BLACK);
                tvFertilizer.setTextSize(18);

                tvKG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvKG.setGravity(Gravity.CENTER);
                tvKG.setBackgroundColor(Color.parseColor("#CFD8DC"));
                tvKG.setText("Nutrients not available");
                tvKG.setTextColor(Color.BLACK);
                tvKG.setTextSize(18);

                tvBag.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvBag.setGravity(Gravity.CENTER);
                tvBag.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvBag.setText("0");
                tvBag.setTextColor(Color.BLACK);
                tvBag.setTextSize(18);
                tbrowData.addView(tvCombination);
                tbrowData.addView(tvFertilizer);
                tbrowData.addView(tvKG);
                tbrowData.addView(tvBag);
                tbNPKDetails.addView(tbrowData);
            }*/
        }
    }

    private void calculatefertilizer2() {
        int tmp0 = 0;
        String tmp1 = "";
        String tmp2 = "";
        for(int i=0; i<arrayNutrientValue1.size();i++){
            for(int j=i+1;j<arrayNutrientValue1.size();j++){
                if(arrayNutrientValue1.get(j) > arrayNutrientValue1.get(i)){
                    tmp0 = arrayNutrientValue1.get(i);
                    arrayNutrientValue1.set(i, arrayNutrientValue1.get(j));
                    arrayNutrientValue1.set(j,tmp0);
                    System.out.println("arrayNutrientValue1 " + arrayNutrientValue1);
                    tmp1 = arrayFertilizerName2.get(i);
                    arrayFertilizerName2.set(i,arrayFertilizerName2.get(j));
                    arrayFertilizerName2.set(j,tmp1);
                    System.out.println("arrayFertilizerName2 " + arrayFertilizerName2);
                    tmp2 = arrayFertilizerCode2.get(i);
                    arrayFertilizerCode2.set(i,arrayFertilizerCode2.get(j));
                    arrayFertilizerCode2.set(j,tmp2);
                    System.out.println("arrayFertilizerCode2 " + arrayFertilizerCode2);
                }
            }
        }

        if(finalDataObtained2==0) {
            arrayFertilizerCalculate2.clear();
            for (int j = 0; j < arrayFertilizerName2.size(); j++) {

                String temp = arrayFertilizerCode2.get(j);
                tempfmdata2 = temp.split("-");
                fmNitrogen2 = Integer.parseInt(tempfmdata2[0]);
                fmPhosphorous2 = Integer.parseInt(tempfmdata2[1]);
                fmPotash2 = Integer.parseInt(tempfmdata2[2]);

                if (fmPhosphorous2 != 0 && fmNitrogen2 != 0 && fmPotash2 != 0) {
                    if (fmPhosphorous2 > fmNitrogen2 && fmPhosphorous2 > fmPotash2 && requiredPhosphorous2 != 0) {
                        phosphorous2 = (int) ((float) 100 / (float) fmPhosphorous2 * (requiredPhosphorous2));
                        requiredPhosphorous2 = 0;
                        requiredNitrogen2 = (int) (requiredNitrogen2 - ((float) fmNitrogen2 / (float) 100 * phosphorous2));
                        requiredPotash2 = (int) (requiredPotash2 - ((float) fmPotash2 / (float) 100 * phosphorous2));
                    } else if (fmNitrogen2 > fmPhosphorous2 && fmNitrogen2 > fmPotash2 && requiredNitrogen2 != 0) {

                        nitrogen2 = (int) ((float) 100 / (float) fmNitrogen2 * (requiredNitrogen2));
                        requiredNitrogen2 = 0;
                        requiredPhosphorous2 = (int) (requiredPhosphorous2 - ((float) fmPhosphorous2 / (float) 100 * nitrogen2));
                        requiredPotash2 = (int) (requiredPotash2 - ((float) fmPotash2 / (float) 100 * nitrogen2));

                    } else if (fmPotash2 > fmPhosphorous2 && fmPotash2 > fmNitrogen2 && requiredPotash2 != 0) {

                        potash2 = (int) ((float) 100 / (float) fmPotash2 * (requiredPotash2));
                        requiredPotash2 = 0;
                        requiredNitrogen2 = (int) (requiredNitrogen2 - ((float) fmNitrogen2 / (float) 100 * potash2));
                        requiredPhosphorous2 = (int) (requiredPhosphorous2 - ((float) fmPhosphorous2 / (float) 100 * potash2));
                    } else if (requiredPhosphorous2 != 0) {
                        phosphorous2 = (int) ((float) 100 / (float) fmPhosphorous2 * (requiredPhosphorous2));
                        requiredPhosphorous2 = 0;
                        requiredNitrogen2 = (int) (requiredNitrogen2 - ((float) fmNitrogen2 / (float) 100 * phosphorous2));
                        requiredPotash2 = (int) (requiredPotash2 - ((float) fmPotash2 / (float) 100 * phosphorous2));
                    }
                } else if (fmPhosphorous2 != 0 && fmNitrogen2 != 0) {
                    if (fmPhosphorous2 > fmNitrogen2 && requiredPhosphorous2 != 0) {
                        phosphorous2 = (int) ((float) 100 / (float) fmPhosphorous2 * (requiredPhosphorous2));
                        requiredPhosphorous2 = 0;
                        requiredNitrogen2 = (int) (requiredNitrogen2 - ((float) fmNitrogen2 / (float) 100 * phosphorous2));
                    } else if (fmNitrogen2 > fmPhosphorous2 && requiredNitrogen2 != 0) {
                        nitrogen2 = (int) ((float) 100 / (float) fmNitrogen2 * (requiredNitrogen2));
                        requiredNitrogen2 = 0;
                        requiredPhosphorous2 = (int) (requiredPhosphorous2 - ((float) fmPhosphorous2 / (float) 100 * nitrogen2));
                    }
                } else if (fmNitrogen2 != 0 && fmPotash2 != 0) {

                    if (fmNitrogen2 > fmPotash2 && requiredNitrogen2 != 0) {
                        nitrogen2 = (int) ((float) 100 / (float) fmNitrogen2 * (requiredNitrogen2));
                        requiredNitrogen2 = 0;
                        requiredPotash2 = (int) (requiredPotash2 - ((float) fmPotash2 / (float) 100 * nitrogen2));
                    } else if (fmPotash2 > fmNitrogen2 && requiredPotash2 != 0) {
                        potash2 = (int) ((float) 100 / (float) fmPotash2 * (requiredPotash2));
                        requiredPotash2 = 0;
                        requiredNitrogen2 = (int) (requiredNitrogen2 - ((float) fmNitrogen2 / (float) 100 * potash2));
                    }
                } else if (fmPhosphorous2 != 0 && fmPotash2 != 0) {

                    if (fmPhosphorous2 > fmPotash2 && requiredPhosphorous2 != 0) {
                        phosphorous2 = (int) ((float) 100 / (float) fmPhosphorous2 * (requiredPhosphorous2));
                        requiredPhosphorous2 = 0;
                        requiredPotash2 = (int) (requiredPotash2 - ((float) fmPotash2 / (float) 100 * phosphorous2));
                    } else if (fmPotash2 > fmPhosphorous2 && requiredPotash2 != 0) {
                        potash2 = (int) ((float) 100 / (float) fmPotash2 * (requiredPotash2));
                        requiredPotash2 = 0;
                        requiredPhosphorous2 = (int) (requiredPhosphorous2 - ((float) fmPhosphorous2 / (float) 100 * potash2));
                    }
                } else if (fmPhosphorous2 != 0 && requiredPhosphorous2 != 0) {
                    phosphorous2 = (int) ((float) 100 / (float) fmPhosphorous2 * (requiredPhosphorous2));
                    requiredPhosphorous2 = 0;

                } else if (fmNitrogen2 != 0 && requiredNitrogen2 != 0) {
                    nitrogen2 = (int) ((float) 100 / (float) fmNitrogen2 * requiredNitrogen2);
                    requiredNitrogen2 = 0;
                } else if (fmPotash2 != 0 && requiredPotash2 != 0) {
                    potash2 = (int) ((float) 100 / (float) fmPotash2 * (requiredPotash2));
                    requiredPotash2 = 0;
                }

                if (phosphorous2 != 0) {
                  //  totalP2 = totalP2 + phosphorous2;
                    System.out.println("phosphorous2 - " + phosphorous2);
                    finalDataObtained2 = phosphorous2;
                    arrayFertilizerCalculate2.add(String.valueOf(finalDataObtained2));
                }
                if (nitrogen2 != 0) {
                 //   totalN2 = totalN2 + nitrogen2;
                    System.out.println("nitrogen2 - " + nitrogen2);
                    finalDataObtained2 = nitrogen2;
                    arrayFertilizerCalculate2.add(String.valueOf(finalDataObtained2));
                }
                if (potash2 != 0) {
               //     totalK2 = totalK2 + potash2;
                    System.out.println("potash2 - " + potash2);
                    finalDataObtained2 = potash2;
                    arrayFertilizerCalculate2.add(String.valueOf(finalDataObtained2));
                }

            }
        }
        if (requiredNitrogen2 == 0 && requiredPhosphorous2 == 0 && requiredPotash2 == 0) {

            npk2 = new LinkedHashSet<String>(arrayFertilizerCalculate2);
            arrayFertilizerCalculate2.clear();
            arrayFertilizerCalculate2.addAll(npk2);

            for (int j = 0; j < arrayFertilizerName2.size(); j++) {
                System.out.println("arrayFertilizerCalculate2 - " + arrayFertilizerCalculate2 + " " + arrayFertilizerCalculate2.size());
                value2 = Double.valueOf(String.valueOf(arrayFertilizerCalculate2.get(j)));
                bagValue2 = Math.round(value2 / 50);
                arrayKGValue2.add(String.valueOf((int) value2));
                if (value2 < 50) {
                    bagValue2 = 1;
                }
                arraybagValue2.add(String.valueOf((int) bagValue2));

                TableRow tbrowData = new TableRow(mContext);
                TextView tvCombination = new TextView(mContext);
                TextView tvFertilizer = new TextView(mContext);
                TextView tvKG = new TextView(mContext);
                TextView tvBag = new TextView(mContext);

                if(combiCount2 == 1) {
                    combinationcountValue++;
                    tvCombination.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tvCombination.setGravity(Gravity.CENTER);
                    tvCombination.setBackgroundColor(Color.parseColor("#9FA8DA"));
                    tvCombination.setText("Combination " + combinationcountValue);
                    tvCombination.setTextColor(Color.BLACK);
                    tvCombination.setTextSize(18);
                    combiCount2++;
                }else{
                    tvCombination.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tvCombination.setGravity(Gravity.CENTER);
                    tvCombination.setBackgroundColor(Color.parseColor("#9FA8DA"));
                    tvCombination.setText("");
                    tvCombination.setTextColor(Color.BLACK);
                    tvCombination.setTextSize(18);
                }

                tvFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvFertilizer.setGravity(Gravity.CENTER);
                tvFertilizer.setBackgroundColor(Color.parseColor("#9FA8DA"));
                tvFertilizer.setText(arrayFertilizerName2.get(j));
                tvFertilizer.setTextColor(Color.BLACK);
                tvFertilizer.setTextSize(18);

                tvKG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvKG.setGravity(Gravity.CENTER);
                tvKG.setBackgroundColor(Color.parseColor("#9FA8DA"));
                tvKG.setText("" + (int) value2);
                tvKG.setTextColor(Color.BLACK);
                tvKG.setTextSize(18);

                tvBag.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvBag.setGravity(Gravity.CENTER);
                tvBag.setBackgroundColor(Color.parseColor("#9FA8DA"));
                tvBag.setText("" + (int) bagValue2);
                tvBag.setTextColor(Color.BLACK);
                tvBag.setTextSize(18);

                tbrowData.addView(tvCombination);
                tbrowData.addView(tvFertilizer);
                tbrowData.addView(tvKG);
                tbrowData.addView(tvBag);
                tbNPKDetails.addView(tbrowData);

            }
        }else{

       /*     for (int j = 0; j < arrayFertilizerName2.size(); j++) {
                TableRow tbrowData = new TableRow(mContext);
                TextView tvCombination = new TextView(mContext);
                TextView tvFertilizer = new TextView(mContext);
                TextView tvKG = new TextView(mContext);
                TextView tvBag = new TextView(mContext);

                tvCombination.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvCombination.setGravity(Gravity.CENTER);
                tvCombination.setBackgroundColor(Color.parseColor("#CFD8DC"));
                tvCombination.setText("Combination 2");
                tvCombination.setTextColor(Color.BLACK);
                tvCombination.setTextSize(18);

                tvFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvFertilizer.setGravity(Gravity.CENTER);
                tvFertilizer.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvFertilizer.setText(arrayFertilizerName2.get(j));
                tvFertilizer.setTextColor(Color.BLACK);
                tvFertilizer.setTextSize(18);

                tvKG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvKG.setGravity(Gravity.CENTER);
                tvKG.setBackgroundColor(Color.parseColor("#CFD8DC"));
                tvKG.setText("Nutrients not available");
                tvKG.setTextColor(Color.BLACK);
                tvKG.setTextSize(18);

                tvBag.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvBag.setGravity(Gravity.CENTER);
                tvBag.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvBag.setText("0");
                tvBag.setTextColor(Color.BLACK);
                tvBag.setTextSize(18);
                tbrowData.addView(tvCombination);
                tbrowData.addView(tvFertilizer);
                tbrowData.addView(tvKG);
                tbrowData.addView(tvBag);
                tbNPKDetails.addView(tbrowData);
            }*/
        }

    }

    private void calculatefertilizer3() {

        if(finalDataObtained3==0) {
            arrayFertilizerCalculate3.clear();
            for (int j = 0; j < arrayFertilizerName3.size(); j++) {

                String temp = arrayFertilizerCode3.get(j);
                System.out.println("arrayFertilizerName3 for " + arrayFertilizerName3 + " " + arrayFertilizerName3.size());
                System.out.println("arrayFertilizerCode3 for " + arrayFertilizerCode3 + " " + arrayFertilizerCode3.size());

                tempfmdata3 = temp.split("-");
                fmNitrogen3 = Integer.parseInt(tempfmdata3[0]);
                fmPhosphorous3 = Integer.parseInt(tempfmdata3[1]);
                fmPotash3 = Integer.parseInt(tempfmdata3[2]);

                System.out.println("fmNitrogen3 for " + fmNitrogen3);
                System.out.println("fmPhosphorous3 for " + fmPhosphorous3);
                System.out.println("fmPotash3 for " + fmPotash3);

                if (fmPhosphorous3 != 0 && fmNitrogen3 != 0 && fmPotash3 != 0) {
                    if (fmPhosphorous3 > fmNitrogen3 && fmPhosphorous3 > fmPotash3 && requiredPhosphorous3 != 0) {
                        phosphorous3 = (int) ((float) 100 / (float) fmPhosphorous3 * (requiredPhosphorous3));
                        requiredPhosphorous3 = 0;
                        requiredNitrogen3 = (int) (requiredNitrogen3 - ((float) fmNitrogen3 / (float) 100 * phosphorous3));
                        requiredPotash3 = (int) (requiredPotash3 - ((float) fmPotash3 / (float) 100 * phosphorous3));
                    } else if (fmNitrogen3 > fmPhosphorous3 && fmNitrogen3 > fmPotash3 && requiredNitrogen3 != 0) {

                        nitrogen3 = (int) ((float) 100 / (float) fmNitrogen3 * (requiredNitrogen3));
                        requiredNitrogen3 = 0;
                        requiredPhosphorous3 = (int) (requiredPhosphorous3 - ((float) fmPhosphorous3 / (float) 100 * nitrogen3));
                        requiredPotash3 = (int) (requiredPotash3 - ((float) fmPotash3 / (float) 100 * nitrogen3));

                    } else if (fmPotash3 > fmPhosphorous3 && fmPotash3 > fmNitrogen3 && requiredPotash3 != 0) {

                        potash3 = (int) ((float) 100 / (float) fmPotash3 * (requiredPotash3));
                        requiredPotash3 = 0;
                        requiredNitrogen3 = (int) (requiredNitrogen3 - ((float) fmNitrogen3 / (float) 100 * potash3));
                        requiredPhosphorous3 = (int) (requiredPhosphorous3 - ((float) fmPhosphorous3 / (float) 100 * potash3));
                    } else if (requiredPhosphorous3 != 0) {
                        phosphorous3 = (int) ((float) 100 / (float) fmPhosphorous3 * (requiredPhosphorous3));
                        requiredPhosphorous3 = 0;
                        requiredNitrogen3 = (int) (requiredNitrogen3 - ((float) fmNitrogen3 / (float) 100 * phosphorous3));
                        requiredPotash3 = (int) (requiredPotash3 - ((float) fmPotash3 / (float) 100 * phosphorous3));
                    }
                } else if (fmPhosphorous3 != 0 && fmNitrogen3 != 0) {
                    if (fmPhosphorous3 > fmNitrogen3 && requiredPhosphorous3 != 0) {
                        phosphorous3 = (int) ((float) 100 / (float) fmPhosphorous3 * (requiredPhosphorous3));
                        requiredPhosphorous3 = 0;
                        requiredNitrogen3 = (int) (requiredNitrogen3 - ((float) fmNitrogen3 / (float) 100 * phosphorous3));
                    } else if (fmNitrogen3 > fmPhosphorous3 && requiredNitrogen3 != 0) {
                        nitrogen3 = (int) ((float) 100 / (float) fmNitrogen3 * (requiredNitrogen3));
                        requiredNitrogen3 = 0;
                        requiredPhosphorous3 = (int) (requiredPhosphorous3 - ((float) fmPhosphorous3 / (float) 100 * nitrogen3));
                    }
                } else if (fmNitrogen3 != 0 && fmPotash3 != 0) {

                    if (fmNitrogen3 > fmPotash3 && requiredNitrogen3 != 0) {
                        nitrogen3 = (int) ((float) 100 / (float) fmNitrogen3 * (requiredNitrogen3));
                        requiredNitrogen3 = 0;
                        requiredPotash3 = (int) (requiredPotash3 - ((float) fmPotash3 / (float) 100 * nitrogen3));
                    } else if (fmPotash3 > fmNitrogen3 && requiredPotash3 != 0) {
                        potash3 = (int) ((float) 100 / (float) fmPotash3 * (requiredPotash3));
                        requiredPotash3 = 0;
                        requiredNitrogen3 = (int) (requiredNitrogen3 - ((float) fmNitrogen3 / (float) 100 * potash3));
                    }
                } else if (fmPhosphorous3 != 0 && fmPotash3 != 0) {

                    if (fmPhosphorous3 > fmPotash3 && requiredPhosphorous3 != 0) {
                        phosphorous3 = (int) ((float) 100 / (float) fmPhosphorous3 * (requiredPhosphorous3));
                        requiredPhosphorous3 = 0;
                        requiredPotash3 = (int) (requiredPotash3 - ((float) fmPotash3 / (float) 100 * phosphorous3));
                    } else if (fmPotash3 > fmPhosphorous3 && requiredPotash3 != 0) {
                        potash3 = (int) ((float) 100 / (float) fmPotash3 * (requiredPotash3));
                        requiredPotash3 = 0;
                        requiredPhosphorous3 = (int) (requiredPhosphorous3 - ((float) fmPhosphorous3 / (float) 100 * potash3));
                    }
                } else if (fmPhosphorous3 != 0 && requiredPhosphorous3 != 0) {
                    phosphorous3 = (int) ((float) 100 / (float) fmPhosphorous3 * (requiredPhosphorous3));
                    requiredPhosphorous3 = 0;

                } else if (fmNitrogen3 != 0 && requiredNitrogen3 != 0) {
                    nitrogen3 = (int) ((float) 100 / (float) fmNitrogen3 * requiredNitrogen3);
                    requiredNitrogen3 = 0;
                } else if (fmPotash3 != 0 && requiredPotash3 != 0) {
                    potash3 = (int) ((float) 100 / (float) fmPotash3 * (requiredPotash3));
                    requiredPotash3 = 0;
                }

                if (phosphorous3 != 0) {
                //    totalP3 = totalP3 + phosphorous3;
                    System.out.println("phosphorous3 - " + phosphorous3);
                    finalDataObtained3 = phosphorous3;
                    arrayFertilizerCalculate3.add(String.valueOf(finalDataObtained3));
                }
                if (nitrogen3 != 0) {
                 //   totalN3 = totalN3 + nitrogen3;
                    System.out.println("nitrogen3 - " + nitrogen3);
                    finalDataObtained3 = nitrogen3;
                    arrayFertilizerCalculate3.add(String.valueOf(finalDataObtained3));
                }
                if (potash3 != 0) {
                  //  totalK3 = totalK3 + potash3;
                    System.out.println("potash3 - " + potash3);
                    finalDataObtained3 = potash3;
                    arrayFertilizerCalculate3.add(String.valueOf(finalDataObtained3));
                }

            }
        }
        if (requiredNitrogen3 == 0 && requiredPhosphorous3 == 0 && requiredPotash3 == 0) {

            npk3 = new LinkedHashSet<String>(arrayFertilizerCalculate3);
            arrayFertilizerCalculate3.clear();
            arrayFertilizerCalculate3.addAll(npk3);
            for (int j = 0; j < arrayFertilizerName3.size(); j++) {
                System.out.println("arrayFertilizerCalculate3 - " + arrayFertilizerCalculate3 + " " + arrayFertilizerCalculate3.size());

                value3 = Double.valueOf(String.valueOf(arrayFertilizerCalculate3.get(j)));
                bagValue3 = Math.round(value3 / 50);
                arrayKGValue3.add(String.valueOf((int) value3));
                if (value3 < 50) {
                    bagValue3 = 1;
                }
                arraybagValue3.add(String.valueOf((int) bagValue3));


                TableRow tbrowData = new TableRow(mContext);
                TextView tvCombination = new TextView(mContext);
                TextView tvFertilizer = new TextView(mContext);
                TextView tvKG = new TextView(mContext);
                TextView tvBag = new TextView(mContext);

                if(combiCount3 == 1) {
                    combinationcountValue++;
                    tvCombination.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tvCombination.setGravity(Gravity.CENTER);
                    tvCombination.setBackgroundColor(Color.parseColor("#90CAF9"));
                    tvCombination.setText("Combination " + combinationcountValue);
                    tvCombination.setTextColor(Color.BLACK);
                    tvCombination.setTextSize(18);
                    combiCount3++;
                }else{
                    tvCombination.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tvCombination.setGravity(Gravity.CENTER);
                    tvCombination.setBackgroundColor(Color.parseColor("#90CAF9"));
                    tvCombination.setText("");
                    tvCombination.setTextColor(Color.BLACK);
                    tvCombination.setTextSize(18);
                }

                tvFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvFertilizer.setGravity(Gravity.CENTER);
                tvFertilizer.setBackgroundColor(Color.parseColor("#90CAF9"));
                tvFertilizer.setText(arrayFertilizerName3.get(j));
                tvFertilizer.setTextColor(Color.BLACK);
                tvFertilizer.setTextSize(18);

                tvKG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvKG.setGravity(Gravity.CENTER);
                tvKG.setBackgroundColor(Color.parseColor("#90CAF9"));
                tvKG.setText("" + (int) value3);
                tvKG.setTextColor(Color.BLACK);
                tvKG.setTextSize(18);

                tvBag.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvBag.setGravity(Gravity.CENTER);
                tvBag.setBackgroundColor(Color.parseColor("#90CAF9"));
                tvBag.setText("" + (int) bagValue3);
                tvBag.setTextColor(Color.BLACK);
                tvBag.setTextSize(18);
                tbrowData.addView(tvCombination);
                tbrowData.addView(tvFertilizer);
                tbrowData.addView(tvKG);
                tbrowData.addView(tvBag);
                tbNPKDetails.addView(tbrowData);

            }
        }else{

         /*   for (int j = 0; j < arrayFertilizerName3.size(); j++) {
                TableRow tbrowData = new TableRow(mContext);
                TextView tvCombination = new TextView(mContext);
                TextView tvFertilizer = new TextView(mContext);
                TextView tvKG = new TextView(mContext);
                TextView tvBag = new TextView(mContext);

                tvCombination.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvCombination.setGravity(Gravity.CENTER);
                tvCombination.setBackgroundColor(Color.parseColor("#CFD8DC"));
                tvCombination.setText("Combination 3");
                tvCombination.setTextColor(Color.BLACK);
                tvCombination.setTextSize(18);

                tvFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvFertilizer.setGravity(Gravity.CENTER);
                tvFertilizer.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvFertilizer.setText(arrayFertilizerName3.get(j));
                tvFertilizer.setTextColor(Color.BLACK);
                tvFertilizer.setTextSize(18);

                tvKG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvKG.setGravity(Gravity.CENTER);
                tvKG.setBackgroundColor(Color.parseColor("#CFD8DC"));
                tvKG.setText("Nutrients not available");
                tvKG.setTextColor(Color.BLACK);
                tvKG.setTextSize(18);

                tvBag.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvBag.setGravity(Gravity.CENTER);
                tvBag.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvBag.setText("0");
                tvBag.setTextColor(Color.BLACK);
                tvBag.setTextSize(18);
                tbrowData.addView(tvCombination);
                tbrowData.addView(tvFertilizer);
                tbrowData.addView(tvKG);
                tbrowData.addView(tvBag);
                tbNPKDetails.addView(tbrowData);
            }*/
        }

    }

    private void calculatefertilizer4() {
        int tmp0 = 0;
        String tmp1 = "";
        String tmp2 = "";
        for(int i=0; i<arrayNutrientValue2.size();i++){
            for(int j=i+1;j<arrayNutrientValue2.size();j++){
                if(arrayNutrientValue2.get(j) > arrayNutrientValue2.get(i)){
                    tmp0 = arrayNutrientValue2.get(i);
                    arrayNutrientValue2.set(i, arrayNutrientValue2.get(j));
                    arrayNutrientValue2.set(j,tmp0);
                    System.out.println("arrayNutrientValue2 " + arrayNutrientValue2);
                    tmp1 = arrayFertilizerName4.get(i);
                    arrayFertilizerName4.set(i,arrayFertilizerName4.get(j));
                    arrayFertilizerName4.set(j,tmp1);
                    System.out.println("arrayFertilizerName4 " + arrayFertilizerName4);
                    tmp2 = arrayFertilizerCode4.get(i);
                    arrayFertilizerCode4.set(i,arrayFertilizerCode4.get(j));
                    arrayFertilizerCode4.set(j,tmp2);
                    System.out.println("arrayFertilizerCode4 " + arrayFertilizerCode4);
                }
            }
        }

        if(finalDataObtained4==0) {
            arrayFertilizerCalculate4.clear();
            for (int j = 0; j < arrayFertilizerName4.size(); j++) {

                String temp = arrayFertilizerCode4.get(j);
                System.out.println("arrayFertilizerName4 for " + arrayFertilizerName4 + " " + arrayFertilizerName4.size());
                System.out.println("arrayFertilizerCode4 for " + arrayFertilizerCode4 + " " + arrayFertilizerCode4.size());

                tempfmdata4 = temp.split("-");
                fmNitrogen4 = Integer.parseInt(tempfmdata4[0]);
                fmPhosphorous4 = Integer.parseInt(tempfmdata4[1]);
                fmPotash4 = Integer.parseInt(tempfmdata4[2]);

                System.out.println("fmNitrogen4 for " + fmNitrogen4);
                System.out.println("fmPhosphorous4 for " + fmPhosphorous4);
                System.out.println("fmPotash4 for " + fmPotash4);

                if (fmPhosphorous4 != 0 && fmNitrogen4 != 0 && fmPotash4 != 0) {
                    if (fmPhosphorous4 > fmNitrogen4 && fmPhosphorous4 > fmPotash4 && requiredPhosphorous4 != 0) {
                        phosphorous4 = (int) ((float) 100 / (float) fmPhosphorous4 * (requiredPhosphorous4));
                        requiredPhosphorous4 = 0;
                        requiredNitrogen4 = (int) (requiredNitrogen4 - ((float) fmNitrogen4 / (float) 100 * phosphorous4));
                        requiredPotash4 = (int) (requiredPotash4 - ((float) fmPotash4 / (float) 100 * phosphorous4));
                    } else if (fmNitrogen4 > fmPhosphorous4 && fmNitrogen4 > fmPotash4 && requiredNitrogen4 != 0) {

                        nitrogen4 = (int) ((float) 100 / (float) fmNitrogen4 * (requiredNitrogen4));
                        requiredNitrogen4 = 0;
                        requiredPhosphorous4 = (int) (requiredPhosphorous4 - ((float) fmPhosphorous4 / (float) 100 * nitrogen4));
                        requiredPotash4 = (int) (requiredPotash4 - ((float) fmPotash4 / (float) 100 * nitrogen4));

                    } else if (fmPotash4 > fmPhosphorous4 && fmPotash4 > fmNitrogen4 && requiredPotash4 != 0) {

                        potash4 = (int) ((float) 100 / (float) fmPotash4 * (requiredPotash4));
                        requiredPotash4 = 0;
                        requiredNitrogen4 = (int) (requiredNitrogen4 - ((float) fmNitrogen4 / (float) 100 * potash4));
                        requiredPhosphorous4 = (int) (requiredPhosphorous4 - ((float) fmPhosphorous4 / (float) 100 * potash4));
                    } else if (requiredPhosphorous4 != 0) {
                        phosphorous4 = (int) ((float) 100 / (float) fmPhosphorous4 * (requiredPhosphorous4));
                        requiredPhosphorous4 = 0;
                        requiredNitrogen4 = (int) (requiredNitrogen4 - ((float) fmNitrogen4 / (float) 100 * phosphorous4));
                        requiredPotash4 = (int) (requiredPotash4 - ((float) fmPotash4 / (float) 100 * phosphorous4));
                    }
                } else if (fmPhosphorous4 != 0 && fmNitrogen4 != 0) {
                    if (fmPhosphorous4 > fmNitrogen4 && requiredPhosphorous4 != 0) {
                        phosphorous4 = (int) ((float) 100 / (float) fmPhosphorous4 * (requiredPhosphorous4));
                        requiredPhosphorous4 = 0;
                        requiredNitrogen4 = (int) (requiredNitrogen4 - ((float) fmNitrogen4 / (float) 100 * phosphorous4));
                    } else if (fmNitrogen4 > fmPhosphorous4 && requiredNitrogen4 != 0) {
                        nitrogen4 = (int) ((float) 100 / (float) fmNitrogen4 * (requiredNitrogen4));
                        requiredNitrogen4 = 0;
                        requiredPhosphorous4 = (int) (requiredPhosphorous4 - ((float) fmPhosphorous4 / (float) 100 * nitrogen4));
                    }
                } else if (fmNitrogen4 != 0 && fmPotash4 != 0) {

                    if (fmNitrogen4 > fmPotash4 && requiredNitrogen4 != 0) {
                        nitrogen4 = (int) ((float) 100 / (float) fmNitrogen4 * (requiredNitrogen4));
                        requiredNitrogen3 = 0;
                        requiredPotash4 = (int) (requiredPotash4 - ((float) fmPotash4 / (float) 100 * nitrogen4));
                    } else if (fmPotash4 > fmNitrogen4 && requiredPotash4 != 0) {
                        potash4 = (int) ((float) 100 / (float) fmPotash4 * (requiredPotash4));
                        requiredPotash4 = 0;
                        requiredNitrogen4 = (int) (requiredNitrogen4 - ((float) fmNitrogen4 / (float) 100 * potash4));
                    }
                } else if (fmPhosphorous4 != 0 && fmPotash4 != 0) {

                    if (fmPhosphorous4 > fmPotash4 && requiredPhosphorous4 != 0) {
                        phosphorous4 = (int) ((float) 100 / (float) fmPhosphorous4 * (requiredPhosphorous4));
                        requiredPhosphorous4 = 0;
                        requiredPotash4 = (int) (requiredPotash4 - ((float) fmPotash4 / (float) 100 * phosphorous4));
                    } else if (fmPotash4 > fmPhosphorous4 && requiredPotash4 != 0) {
                        potash4 = (int) ((float) 100 / (float) fmPotash4 * (requiredPotash4));
                        requiredPotash4 = 0;
                        requiredPhosphorous4 = (int) (requiredPhosphorous4 - ((float) fmPhosphorous4 / (float) 100 * potash4));
                    }
                } else if (fmPhosphorous4 != 0 && requiredPhosphorous4 != 0) {
                    phosphorous4 = (int) ((float) 100 / (float) fmPhosphorous4 * (requiredPhosphorous4));
                    requiredPhosphorous4 = 0;

                } else if (fmNitrogen4 != 0 && requiredNitrogen4 != 0) {
                    nitrogen4 = (int) ((float) 100 / (float) fmNitrogen4 * requiredNitrogen4);
                    requiredNitrogen4 = 0;
                } else if (fmPotash4 != 0 && requiredPotash4 != 0) {
                    potash4 = (int) ((float) 100 / (float) fmPotash4 * (requiredPotash4));
                    requiredPotash4 = 0;
                }

                if (phosphorous4 != 0) {
                 //   totalP4 = totalP4 + phosphorous4;
                    System.out.println("phosphorous4 - " + phosphorous4);
                    finalDataObtained4 = phosphorous4;
                    arrayFertilizerCalculate4.add(String.valueOf(finalDataObtained4));
                }
                if (nitrogen4 != 0) {
                  //  totalN4 = totalN4 + nitrogen4;
                    System.out.println("nitrogen4 - " + nitrogen4);
                    finalDataObtained4 = nitrogen4;
                    arrayFertilizerCalculate4.add(String.valueOf(finalDataObtained4));
                }
                if (potash4 != 0) {
                  //  totalK4 = totalK4 + potash4;
                    finalDataObtained4 = potash4;
                    arrayFertilizerCalculate4.add(String.valueOf(finalDataObtained4));
                }

            }
        }
        if (requiredNitrogen4 == 0 && requiredPhosphorous4 == 0 && requiredPotash4 == 0) {

            npk4 = new LinkedHashSet<String>(arrayFertilizerCalculate4);
            arrayFertilizerCalculate4.clear();
            arrayFertilizerCalculate4.addAll(npk4);

            for (int j = 0; j < arrayFertilizerName4.size(); j++) {
                System.out.println("arrayFertilizerCalculate4 - " + arrayFertilizerCalculate4 + " " + arrayFertilizerCalculate4.size());

                value4 = Double.valueOf(String.valueOf(arrayFertilizerCalculate4.get(j)));
                bagValue4 = Math.round(value4 / 50);
                arrayKGValue4.add(String.valueOf((int) value4));
                if (value4 < 50) {
                    bagValue4 = 1;
                }
                arraybagValue4.add(String.valueOf((int) bagValue4));


                TableRow tbrowData = new TableRow(mContext);
                TextView tvCombination = new TextView(mContext);
                TextView tvFertilizer = new TextView(mContext);
                TextView tvKG = new TextView(mContext);
                TextView tvBag = new TextView(mContext);

                if(combiCount4 == 1) {
                    combinationcountValue++;
                    tvCombination.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tvCombination.setGravity(Gravity.CENTER);
                    tvCombination.setBackgroundColor(Color.parseColor("#80DEEA"));
                    tvCombination.setText("Combination " + combinationcountValue);
                    tvCombination.setTextColor(Color.BLACK);
                    tvCombination.setTextSize(18);
                    combiCount4++;
                }else{
                    tvCombination.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tvCombination.setGravity(Gravity.CENTER);
                    tvCombination.setBackgroundColor(Color.parseColor("#80DEEA"));
                    tvCombination.setText("");
                    tvCombination.setTextColor(Color.BLACK);
                    tvCombination.setTextSize(18);
                }

                tvFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvFertilizer.setGravity(Gravity.CENTER);
                tvFertilizer.setBackgroundColor(Color.parseColor("#80DEEA"));
                tvFertilizer.setText(arrayFertilizerName4.get(j));
                tvFertilizer.setTextColor(Color.BLACK);
                tvFertilizer.setTextSize(18);

                tvKG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvKG.setGravity(Gravity.CENTER);
                tvKG.setBackgroundColor(Color.parseColor("#80DEEA"));
                tvKG.setText("" + (int) value4);
                tvKG.setTextColor(Color.BLACK);
                tvKG.setTextSize(18);

                tvBag.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvBag.setGravity(Gravity.CENTER);
                tvBag.setBackgroundColor(Color.parseColor("#80DEEA"));
                tvBag.setText("" + (int) bagValue4);
                tvBag.setTextColor(Color.BLACK);
                tvBag.setTextSize(18);

                tbrowData.addView(tvCombination);
                tbrowData.addView(tvFertilizer);
                tbrowData.addView(tvKG);
                tbrowData.addView(tvBag);
                tbNPKDetails.addView(tbrowData);

            }
        }else{

        /*    for (int j = 0; j < arrayFertilizerName4.size(); j++) {
                TableRow tbrowData = new TableRow(mContext);
                TextView tvCombination = new TextView(mContext);
                TextView tvFertilizer = new TextView(mContext);
                TextView tvKG = new TextView(mContext);
                TextView tvBag = new TextView(mContext);

                tvCombination.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvCombination.setGravity(Gravity.CENTER);
                tvCombination.setBackgroundColor(Color.parseColor("#CFD8DC"));
                tvCombination.setText("Combination 4");
                tvCombination.setTextColor(Color.BLACK);
                tvCombination.setTextSize(18);

                tvFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvFertilizer.setGravity(Gravity.CENTER);
                tvFertilizer.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvFertilizer.setText(arrayFertilizerName4.get(j));
                tvFertilizer.setTextColor(Color.BLACK);
                tvFertilizer.setTextSize(18);

                tvKG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvKG.setGravity(Gravity.CENTER);
                tvKG.setBackgroundColor(Color.parseColor("#CFD8DC"));
                tvKG.setText("Nutrients not available");
                tvKG.setTextColor(Color.BLACK);
                tvKG.setTextSize(18);

                tvBag.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvBag.setGravity(Gravity.CENTER);
                tvBag.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvBag.setText("0");
                tvBag.setTextColor(Color.BLACK);
                tvBag.setTextSize(18);

                tbrowData.addView(tvCombination);
                tbrowData.addView(tvFertilizer);
                tbrowData.addView(tvKG);
                tbrowData.addView(tvBag);
                tbNPKDetails.addView(tbrowData);
            }*/
        }

    }

    private void addNPKTable() {
        count = 1;

        TableRow tbrow= new TableRow(mContext);
        TextView tvCombination = new TextView(mContext);
        TextView tvFertilizer = new TextView(mContext);
        TextView tvKG = new TextView(mContext);
        TextView tvBag = new TextView(mContext);

        tvCombination.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvCombination.setGravity(Gravity.CENTER);
        tvCombination.setBackgroundColor(Color.parseColor("#263238"));
        tvCombination.setText("Combination");
        tvCombination.setTextColor(Color.WHITE);
        tvCombination.setTextSize(18);

        tvFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvFertilizer.setGravity(Gravity.CENTER);
        tvFertilizer.setBackgroundColor(Color.parseColor("#424242"));
        tvFertilizer.setText("Fertilizer");
        tvFertilizer.setTextColor(Color.WHITE);
        tvFertilizer.setTextSize(18);

        tvKG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvKG.setGravity(Gravity.CENTER);
        tvKG.setBackgroundColor(Color.parseColor("#263238"));
        tvKG.setText("KGs");
        tvKG.setTextColor(Color.WHITE);
        tvKG.setTextSize(18);

        tvBag.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvBag.setGravity(Gravity.CENTER);
        tvBag.setBackgroundColor(Color.parseColor("#424242"));
        tvBag.setText("Bags");
        tvBag.setTextColor(Color.WHITE);
        tvBag.setTextSize(18);

        tbrow.addView(tvCombination);
        tbrow.addView(tvFertilizer);
        tbrow.addView(tvKG);
        tbrow.addView(tvBag);

        tbNPKDetails.addView(tbrow);
    }

    private void addFertilizerTable() {
        count = 1;

        TableRow tbrow= new TableRow(mContext);
        TextView tvNPK = new TextView(mContext);
        tvNPK.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvNPK.setGravity(Gravity.CENTER);
        tvNPK.setBackgroundColor(Color.parseColor("#263238"));
        tvNPK.setText("Total recommendation of NPK");
        tvNPK.setTextColor(Color.WHITE);
        tvNPK.setTextSize(18);
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
        tvDistrict.setBackgroundColor(Color.parseColor("#263238"));
        tvDistrict.setText("District");
        tvDistrict.setTextColor(Color.WHITE);
        tvDistrict.setTextSize(18);

        tvTaluk.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvTaluk.setGravity(Gravity.CENTER);
        tvTaluk.setBackgroundColor(Color.parseColor("#424242"));
        tvTaluk.setText("Taluk");
        tvTaluk.setTextColor(Color.WHITE);
        tvTaluk.setTextSize(18);

        tvHobli.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvHobli.setGravity(Gravity.CENTER);
        tvHobli.setBackgroundColor(Color.parseColor("#263238"));
        tvHobli.setText("Hobli");
        tvHobli.setTextColor(Color.WHITE);
        tvHobli.setTextSize(18);

        tvVillage.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvVillage.setGravity(Gravity.CENTER);
        tvVillage.setBackgroundColor(Color.parseColor("#424242"));
        tvVillage.setText("Village");
        tvVillage.setTextColor(Color.WHITE);
        tvVillage.setTextSize(18);

        tvSurvey.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvSurvey.setGravity(Gravity.CENTER);
        tvSurvey.setBackgroundColor(Color.parseColor("#263238"));
        tvSurvey.setText("Survey");
        tvSurvey.setTextColor(Color.WHITE);
        tvSurvey.setTextSize(18);

        tvCrop.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tvCrop.setGravity(Gravity.CENTER);
        tvCrop.setBackgroundColor(Color.parseColor("#424242"));
        tvCrop.setText("Crop");
        tvCrop.setTextColor(Color.WHITE);
        tvCrop.setTextSize(18);

        tbrow.addView(tvDistrict);
        tbrow.addView(tvTaluk);
        tbrow.addView(tvHobli);
        tbrow.addView(tvVillage);
        tbrow.addView(tvSurvey);
        tbrow.addView(tvCrop);
        tbUserDetails.addView(tbrow);

        adduserTableData();
        addrecommendedTable();
        addrecommendedTableData();

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
                tvDistrictData.setBackgroundColor(Color.parseColor("#CFD8DC"));
                tvDistrictData.setText(arrayDistrict.get(0));
                tvDistrictData.setTextColor(Color.BLACK);
                tvDistrictData.setTextSize(18);

                tvTalukData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvTalukData.setGravity(Gravity.CENTER);
                tvTalukData.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvTalukData.setText(arrayTaluk.get(0));
                tvTalukData.setTextColor(Color.BLACK);
                tvTalukData.setTextSize(18);

                tvHobliData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvHobliData.setGravity(Gravity.CENTER);
                tvHobliData.setBackgroundColor(Color.parseColor("#CFD8DC"));
                tvHobliData.setText(arrayHobli.get(0));
                tvHobliData.setTextColor(Color.BLACK);
                tvHobliData.setTextSize(18);

                tvVillageData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvVillageData.setGravity(Gravity.CENTER);
                tvVillageData.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvVillageData.setText(arrayVillage.get(0));
                tvVillageData.setTextColor(Color.BLACK);
                tvVillageData.setTextSize(18);

                tvSurveyData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvSurveyData.setGravity(Gravity.CENTER);
                tvSurveyData.setBackgroundColor(Color.parseColor("#CFD8DC"));
                tvSurveyData.setText(arraySurvey.get(j));
                tvSurveyData.setTextColor(Color.BLACK);
                tvSurveyData.setTextSize(18);

                tvCropData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvCropData.setGravity(Gravity.CENTER);
                tvCropData.setBackgroundColor(Color.parseColor("#F5F5F5"));
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
    }

    private void addrecommendedTable() {

            TableRow tbrowRecomended = new TableRow(mContext);
            TextView tvRFertilizer = new TextView(mContext);
            tvRFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvRFertilizer.setGravity(Gravity.CENTER);
            tvRFertilizer.setBackgroundColor(Color.parseColor("#263238"));
            tvRFertilizer.setText("Recommended Nutrient for 1 acre");
            tvRFertilizer.setTextColor(Color.WHITE);
            tvRFertilizer.setTextSize(18);
            tbrowRecomended.addView(tvRFertilizer);
            tbNutrientDetail.addView(tbrowRecomended);

    }

    private void addrecommendedTableData() {
           for(int i=0;i<arrayCropName.size();i++) {

            String nitrogen = arrayFertilizerCropNitrogen.get(i);
            String phosphorous = arrayFertilizerCropPhospohorous.get(i);
            String potash = arrayFertilizerCropPotash.get(i);

            TableRow tbrowData = new TableRow(mContext);
            TextView tvRFertilizer = new TextView(mContext);

            tvRFertilizer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvRFertilizer.setGravity(Gravity.CENTER);
            tvRFertilizer.setBackgroundColor(Color.parseColor("#CFD8DC"));
            tvRFertilizer.setText(arrayCropName.get(i) + "  -  " + "Nitrogen:" + nitrogen + ", Phosphorous:" + phosphorous + ", Potash:" + potash);
            tvRFertilizer.setTextColor(Color.BLACK);
            tvRFertilizer.setTextSize(18);

            tbrowData.addView(tvRFertilizer);
            tbNutrientDetail.addView(tbrowData);

        }

    }

    private void clearViews() {
        int tablecount1 = tbUserDetails.getChildCount();
        for(int i =0 ;i<tablecount1;i++){
            View child = tbUserDetails.getChildAt(i);
            if(child instanceof TableRow)((ViewGroup) child).removeAllViews();
        }

        int tablecount2 = tbNutrientDetail.getChildCount();
        for(int i =0 ;i<tablecount2;i++){
            View child = tbNutrientDetail.getChildAt(i);
            if(child instanceof TableRow)((ViewGroup) child).removeAllViews();
        }

        int tablecount3 = tbAreaDetails.getChildCount();
        for(int i =0 ;i<tablecount3;i++){
            View child = tbAreaDetails.getChildAt(i);
            if(child instanceof TableRow)((ViewGroup) child).removeAllViews();
        }

        int tablecount4 = tbNPKDetails.getChildCount();
        for(int i =0 ;i<tablecount4;i++){
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
