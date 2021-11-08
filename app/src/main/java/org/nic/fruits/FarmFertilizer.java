package org.nic.fruits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.pojo.ModelCropFertilizerMasternpk;
import org.nic.fruits.pojo.ModelCropSurveyDetails;
import org.nic.fruits.pojo.ModelFertilizerCropMaster;
import org.nic.fruits.pojo.ModelFertilizerNameMaster;
import org.nic.fruits.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class FarmFertilizer extends AppCompatActivity {

    Context mContext;
    String farmerID;
    String keyValue;
    AppDatabase appDatabase;
    Locale locale;
    String yearValue = "", seasonValue = "", cropNameValue = "", cropAreaValue="", cropCodeValue="";
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
    int requiredNitrogen = 0;
    int requiredPhosphorous = 0;
    int requiredPotash = 0;
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
    RelativeLayout rlTableArea;
    RelativeLayout rlTableFertilizer;
    TableLayout tbUserDetails;
    TableLayout tbAreaDetails;

    TextView tvNutrientdetail;
    TextView tvNPKDetails;
    int count =0 ;
    final Handler handler = new Handler();

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
        rlTableArea = findViewById(R.id.rlAreaDetails);
        tbUserDetails = findViewById(R.id.tbUserDetail);
        tbAreaDetails = findViewById(R.id.tbAreaDetail);
        tvNutrientdetail = findViewById(R.id.tvNutrientdetails);
        tvNPKDetails = findViewById(R.id.npkdetails);
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
                         //   tvuserDetail.setText("");
                            linearLayoutFertilizerDetails.setVisibility(View.GONE);
                            clearViews();

                            tvNutrientdetail.setText("");
                            tvNPKDetails.setText("");
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
                            //    tvuserDetail.setText("");
                                linearLayoutFertilizerDetails.setVisibility(View.GONE);
                                clearViews();
                                tvNutrientdetail.setText("");
                                tvNPKDetails.setText("");
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
                                Set<String> filterCropName;
                                filterCropName = new LinkedHashSet<String>(arrayCropName);
                                arrayCropName.clear();
                                arrayCropName.addAll(filterCropName);

                                arrayCropArea.add(taskEntry.getCropArea());
                                Set<String> filterCropArea;
                                filterCropArea = new LinkedHashSet<String>(arrayCropArea);
                                arrayCropArea.clear();
                                arrayCropArea.addAll(filterCropArea);

                            } else {
                                arrayDistrict.add(taskEntry.getDistrictname());
                            /*    Set<String> filterDistrictName;
                                filterDistrictName = new LinkedHashSet<String>(arrayDistrict);
                                arrayDistrict.clear();
                                arrayDistrict.addAll(filterDistrictName);*/

                                arrayTaluk.add(taskEntry.getTalukName());
                         /*       Set<String> filterTalukName;
                                filterTalukName = new LinkedHashSet<String>(arrayTaluk);
                                arrayTaluk.clear();
                                arrayTaluk.addAll(filterTalukName);*/

                                arrayHobli.add(taskEntry.getHobliName());
                           /*     Set<String> filterHobliName;
                                filterHobliName = new LinkedHashSet<String>(arrayHobli);
                                arrayHobli.clear();
                                arrayHobli.addAll(filterHobliName);*/

                                arrayVillage.add(taskEntry.getVillageName());
                     /*           Set<String> filterVillageName;
                                filterVillageName = new LinkedHashSet<String>(arrayVillage);
                                arrayVillage.clear();
                                arrayVillage.addAll(filterVillageName);*/

                                arraySurvey.add(taskEntry.getSurveyNumber());
                           /*     Set<String> filterSurvey;
                                filterSurvey = new LinkedHashSet<String>(arraySurvey);
                                arraySurvey.clear();
                                arraySurvey.addAll(filterSurvey);*/

                                arrayCropName.add(taskEntry.getCropName());
                            /*    Set<String> filterCropName;
                                filterCropName = new LinkedHashSet<String>(arrayCropName);
                                arrayCropName.clear();
                                arrayCropName.addAll(filterCropName);*/

                                arrayCropArea.add(taskEntry.getCropArea());
                                /*Set<String> filterCropArea;
                                filterCropArea = new LinkedHashSet<String>(arrayCropArea);
                                arrayCropArea.clear();
                                arrayCropArea.addAll(filterCropArea);*/
                            }
                        }
                        System.out.println("CropName array - " + arrayCropName);
                        System.out.println("CropArea array - " + arrayCropArea);
                        System.out.println("arrayDistrict array - " + arrayDistrict);
                        System.out.println("arraySurvey array - " + arraySurvey);
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
                                arrayCropName.add(taskEntry.getCropcode());
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

        for(int k=0 ;k<arrayFertilizerCropCode.size();k++){

       //     cropCodeValue = arrayFertilizerCropCode.get(k);
            arrayCropCodeValue.add(arrayFertilizerCropCode.get(k));
            MainViewModel viewFertilizerCropMaster = ViewModelProviders.of(this).get(MainViewModel.class);
            viewFertilizerCropMaster.getCropNPK(arrayCropCodeValue.get(k)).observe(this, new Observer<List<ModelCropFertilizerMasternpk>>() {

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
                    arrayRequiredNitrogen.add(String.valueOf((int) Math.round(Integer.parseInt(arrayFertilizerCropNitrogen.get(s)) * totalArea)));
                    arrayRequiredPhospohorous.add(String.valueOf((int) Math.round(Integer.parseInt(arrayFertilizerCropPhospohorous.get(s)) * totalArea)));
                    arrayRequiredPotash.add(String.valueOf((int) Math.round(Integer.parseInt(arrayFertilizerCropPotash.get(s)) * totalArea)));
                }
                sumNitrogen = sumNitrogen + Integer.parseInt(arrayRequiredNitrogen.get(i));
                sumPhospohorous = sumPhospohorous + Integer.parseInt(arrayRequiredPhospohorous.get(i));
                sumPotash = sumPotash + Integer.parseInt(arrayRequiredPotash.get(i));
            }
            arrayAddRequiredNitrogen.add(String.valueOf(sumNitrogen));
            arrayAddRequiredPhospohorous.add(String.valueOf(sumPhospohorous));
            arrayAddRequiredPotash.add(String.valueOf(sumPotash));

            System.out.println("arrayRequiredNitrogen array - " + arrayRequiredNitrogen + " " + arrayRequiredNitrogen.size());
            System.out.println("arrayRequiredPhospohorous array - " + arrayRequiredPhospohorous);
            System.out.println("arrayRequiredPotash array - " + arrayRequiredPotash);

            System.out.println("sum arrayAddNitrogen - " + arrayAddRequiredNitrogen );
            System.out.println("sum arrayAddPhospohorous - " + arrayAddRequiredPhospohorous);
            System.out.println("sum arrayAddPotash - " + arrayAddRequiredPotash);

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
        arrayFertilizerName.add(0,"MOP");
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
        arrayFinalPotashValue.add(2,"0");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(arrayFinalNitrogenValue.size() !=0){
                    System.out.println("arrayFinalNitrogenValue.size - " + arrayFinalNitrogenValue.size());
                    System.out.println("arrayFinalPhosphorousValue.size - " + arrayFinalPhospohorousValue.size());
                    System.out.println("arrayFinalPotashValue.size - " + arrayFinalPotashValue.size());
                    caluculateFinalNPK();
                }
            }
        }, 1000);


    }

    private void caluculateFinalNPK() {
        System.out.println("arrayFinalNitrogenValue.size " + arrayFinalNitrogenValue.size());
    //    Toast.makeText(mContext,"Calculating..",Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                for(int j=0;j<arrayFinalNitrogenValue.size();j++){ //arrayFinalNitrogenValue.size() arrayRequiredNitrogen
                    try {
                        fmNitrogen = Integer.parseInt(arrayFinalNitrogenValue.get(j));
                        fmPhosphorous = Integer.parseInt(arrayFinalPhospohorousValue.get(j));
                        fmPotash = Integer.parseInt(arrayFinalPotashValue.get(j));

                       /* System.out.println("fmNitrogen values " + fmNitrogen );
                        System.out.println("fmPhosphorous values " + fmPhosphorous );
                        System.out.println("fmPotash values " + fmPotash );*/

                        requiredNitrogen = Integer.parseInt(arrayRequiredNitrogen.get(j));
                        requiredPhosphorous = Integer.parseInt(arrayRequiredPhospohorous.get(j));
                        requiredPotash = Integer.parseInt(arrayRequiredPotash.get(j));

                        /*System.out.println("requiredNitrogen values " + requiredNitrogen );
                        System.out.println("requiredPhosphorous values " + requiredPhosphorous );
                        System.out.println("requiredPotash values " + requiredPotash );*/

                        if (fmPhosphorous != 0 && fmNitrogen != 0 && fmPotash != 0) {
                            if (fmPhosphorous > fmNitrogen && fmPhosphorous > fmPotash && requiredPhosphorous != 0) {
                                phosphorous = (int) ((float) 100 / (float) fmPhosphorous * (requiredPhosphorous));
                                requiredPhosphorous = 0;
                                requiredNitrogen = (int) (requiredNitrogen - ((float) fmNitrogen / (float) 100 * phosphorous));
                                requiredPotash = (int) (requiredPotash - ((float) fmPotash / (float) 100 * phosphorous));
                                System.out.println("requiredPhosphorous 1 " + requiredPhosphorous + " requiredNitrogen " + requiredNitrogen + " requiredPotash " + requiredPotash);
                            } else if (fmNitrogen > fmPhosphorous && fmPhosphorous > fmPotash && requiredPhosphorous != 0) {

                                nitrogen = (int) ((float) 100 / (float) fmNitrogen * (requiredNitrogen));
                                requiredNitrogen = 0;
                                requiredPhosphorous = (int) (requiredPhosphorous - ((float) fmPhosphorous / (float) 100 * nitrogen));
                                requiredPotash = (int) (requiredPotash - ((float) fmPotash / (float) 100 * nitrogen));
                                System.out.println("requiredNitrogen 2 " + requiredNitrogen + " requiredPhosphorous " + requiredPhosphorous + " requiredPotash " + requiredPotash);

                            } else if (fmPotash > fmPhosphorous && fmPotash > fmNitrogen && requiredPotash != 0) {

                                potash = (int) ((float) 100 / (float) fmPotash * (requiredPotash));
                                requiredPotash = 0;
                                requiredNitrogen = (int) (requiredNitrogen - ((float) fmNitrogen / (float) 100 * potash));
                                requiredPhosphorous = (int) (requiredPhosphorous - ((float) fmPhosphorous / (float) 100 * potash));
                                System.out.println("requiredPotash 3 " + requiredPotash + " requiredNitrogen " + requiredNitrogen + " requiredPhosphorous " + requiredPhosphorous);
                            } else if (requiredPhosphorous != 0) {
                                phosphorous = (int) ((float) 100 / (float) fmPhosphorous * (requiredPhosphorous));
                                requiredPhosphorous = 0;
                                requiredNitrogen = (int) (requiredNitrogen - ((float) fmNitrogen / (float) 100 * phosphorous));
                                requiredPotash = (int) (requiredPotash - ((float) fmPotash / (float) 100 * phosphorous));
                                System.out.println("requiredPhosphorous 4 " + requiredPhosphorous + " requiredNitrogen " + requiredNitrogen + " requiredPotash " + requiredPotash);
                            }
                        }
                        else if (fmPhosphorous != 0 && fmNitrogen != 0) {
                            if (fmPhosphorous > fmNitrogen && requiredPhosphorous != 0) {
                                phosphorous = (int) ((float) 100 / (float) fmPhosphorous * (requiredPhosphorous));
                                requiredPhosphorous = 0;
                                requiredNitrogen = (int) (requiredNitrogen - ((float) fmNitrogen / (float) 100 * phosphorous));
                                System.out.println("requiredPhosphorous 5 " + requiredPhosphorous + " requiredNitrogen " + requiredNitrogen + " requiredPotash -no");
                            } else if (fmNitrogen > fmPhosphorous && requiredNitrogen != 0) {
                                nitrogen = (int) ((float) 100 / (float) fmNitrogen * (requiredNitrogen));
                                requiredNitrogen = 0;
                                requiredPhosphorous = (int) (requiredPhosphorous - ((float) fmPhosphorous / (float) 100 * nitrogen));
                                System.out.println("requiredNitrogen 6 " + requiredNitrogen + " requiredPhosphorous " + requiredPhosphorous + " requiredPotash -no");
                            }
                        }
                        else if (fmNitrogen != 0 && fmPotash != 0) {

                            if (fmNitrogen > fmPotash && requiredNitrogen != 0) {
                                nitrogen = (int) ((float) 100 / (float) fmNitrogen * (requiredNitrogen));
                                requiredNitrogen = 0;
                                requiredPotash = (int) (requiredPotash - ((float) fmPotash / (float) 100 * nitrogen));
                                System.out.println("requiredNitrogen 7 " + requiredNitrogen + " requiredPotash " + requiredPotash + " requiredPhosphorous - no");
                            } else if (fmPotash > fmNitrogen && requiredPotash != 0) {
                                potash = (int) ((float) 100 / (float) fmPotash * (requiredPotash));
                                requiredPotash = 0;
                                requiredNitrogen = (int) (requiredNitrogen - ((float) fmNitrogen / (float) 100 * potash));
                                System.out.println("requiredPotash 8 " + requiredPotash + " requiredNitrogen " + requiredNitrogen + " requiredPhosphorous - no");
                            }
                        }
                        else if (fmPhosphorous != 0 && fmPotash != 0) {

                            if (fmPhosphorous > fmPotash && requiredPhosphorous != 0) {
                                phosphorous = (int) ((float) 100 / (float) fmPhosphorous * (requiredPhosphorous));
                                requiredPhosphorous = 0;
                                requiredPotash = (int) (requiredPotash - ((float) fmPotash / (float) 100 * phosphorous));
                                System.out.println("requiredPhosphorous 9 " + requiredPhosphorous + " requiredPotash " + requiredPotash + " requiredNitrogen - no");
                            } else if (fmPotash > fmPhosphorous && requiredPotash != 0) {
                                potash = (int) ((float) 100 / (float) fmPotash * (requiredPotash));
                                requiredPotash = 0;
                                requiredPhosphorous = (int) (requiredPhosphorous - ((float) fmPhosphorous / (float) 100 * potash));
                                System.out.println("requiredPotash 10 " + requiredPotash + " requiredPhosphorous " + requiredPhosphorous + " requiredNitrogen -no " );
                            }
                        }
                        else if (fmPhosphorous != 0 && requiredPhosphorous != 0) {
                            phosphorous = (int) ((float) 100 / (float) fmPhosphorous * (requiredPhosphorous));
                            requiredPhosphorous = 0;
                            System.out.println("requiredPhosphorous 11 " + requiredPhosphorous);
                        }
                        else if (fmNitrogen != 0 && requiredNitrogen != 0) {
                            nitrogen = (int) ((float) 100 / (float) fmNitrogen * requiredNitrogen);
                            requiredNitrogen = 0;
                            System.out.println("requiredNitrogen 12 " + requiredNitrogen);
                        }
                        else if (fmPotash != 0 && requiredPotash != 0) {
                            potash = (int) ((float) 100 / (float) fmPotash * (requiredPotash));
                            requiredPotash = 0;
                            System.out.println("requiredPotash 13 " + requiredPotash);
                        }

                        if (phosphorous != 0) {
                            // arrayData.add(String.valueOf(phosphorous));
                            totalPhosphorous = totalPhosphorous + phosphorous;
                            arrayPhosphorous.add(String.valueOf(totalPhosphorous));
                            totalP = totalP + totalPhosphorous;
                            System.out.println("totalPhosphorous " + totalPhosphorous);
                            System.out.println("arrayFertilizerName in phosphorous " + arrayFertilizerName.get(j));
                        }
                        if (nitrogen != 0) {
                            //  arrayData.add(String.valueOf(nitrogen));
                            totalNitrogen = totalNitrogen + nitrogen;
                            arrayNitrogen.add(String.valueOf(totalNitrogen));
                            totalN = totalN + totalNitrogen;
                            System.out.println("totalNitrogen " + totalNitrogen);
                            System.out.println("arrayFertilizerName in nitrogen " + arrayFertilizerName.get(j));
                        }
                        if (potash != 0) {
                            // arrayData.add(String.valueOf(potash));
                            totalPotash = totalPotash + potash;
                            arrayPotash.add(String.valueOf(totalPotash));
                            totalK = totalK + totalPotash;
                            System.out.println("totalPotash " + totalPotash);
                            System.out.println("arrayFertilizerName in potash " + arrayFertilizerName.get(j));
                        }

                        //  if(requiredNitrogen == 0 && requiredPhosphorous == 0 && requiredPotash == 0) {

                        //  }
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }

                }
                System.out.println("arrayPhosphorous - " + arrayPhosphorous);
                System.out.println("arrayNitrogen - " + arrayNitrogen);
                System.out.println("arrayPotash - " + arrayPotash);
                double bag1;
                double bag2;
                double bag3;

                bag1 = Math.round(totalK/50);
                System.out.println("bag1 - " + bag1);
                if(totalK<50){
                    bag1 = 1;
                }

                bag2 = Math.round(totalN/50);
                System.out.println("bag2 - " + bag2);
                if(totalN<50){
                    bag2 = 1;
                }
                bag3 = Math.round(totalP/50);
                System.out.println("bag3 - " + bag3);
                if(totalP<50){
                    bag3 = 1;
                }
//arrayFertilizerCropNitrogen

                System.out.println("arrayCropArea.size - " + arrayCropArea.size());
                linearLayoutFertilizerDetails.setVisibility(View.VISIBLE);
                tvNutrientdetail.setText("Recommended Nutrient for 1 acre :"+"\nNitrogen : " +arrayFertilizerCropNitrogen.get(0) +", Phospohorous : "+arrayFertilizerCropPhospohorous.get(0)+", Potash : "+arrayFertilizerCropPotash.get(0));
                  if(count == 0) {
                      adduserTable();
                      addFertilizerTable();
                  }
                adduserTableData();
                addFertilizerTableData();
                tvNPKDetails.setText(arrayFertilizerName.get(0) + " KGs - " + (int)totalK + " Bags - " + (int)bag1 +"\n"+ arrayFertilizerName.get(1) + " KGs - " + (int)totalN + " Bags - " + (int)bag2 +"\n"+ arrayFertilizerName.get(2) + " KGs - " + (int)totalP + " Bags - " + (int)bag3);
               /* System.out.println("arrayFinalNitrogenValue - " + arrayFinalNitrogenValue);
                    System.out.println("arrayFinalPhosphorousValue - " + arrayFinalPhospohorousValue);
                    System.out.println("arrayFinalPotashValue - " + arrayFinalPotashValue);*/

                //      if(requiredNitrogen == 0 && requiredPhosphorous == 0 && requiredPotash == 0) {

                //     }
                //     System.out.println("arrayFinal values " + arrayPhospohorous + " " + arrayNitrogen + " " + arrayPotash);
            }
        }, 1000);

    }

    private void adduserTable() {
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

    private void addFertilizerTable() {
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
    //    tvuserDetail.setText("District " + arrayDistrict.get(0) + "\n Taluk " + arrayTaluk.get(0) + "\n Hobli " + arrayHobli.get(0) + "\n Village "+ arrayVillage.get(0) + "\n Survey " + arraySurvey.get(0));
        for(int i =0;i<arraySurvey.size();i++) {
        TableRow tbrowData = new TableRow(mContext);
        TextView tvDistrictData = new TextView(mContext);
        TextView tvTalukData = new TextView(mContext);
        TextView tvHobliData = new TextView(mContext);
        TextView tvVillageData = new TextView(mContext);
        TextView tvSurveyData = new TextView(mContext);
        TextView tvCropData = new TextView(mContext);

        if(arrayDistrict.size()>1) {
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
            tvSurveyData.setText(arraySurvey.get(i));
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
        }else{
            tvDistrictData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvDistrictData.setGravity(Gravity.CENTER);
            tvDistrictData.setBackgroundColor(Color.parseColor("#FFFDE7"));
            tvDistrictData.setText(arrayDistrict.get(0));
            tvDistrictData.setTextColor(Color.BLACK);
            tvDistrictData.setTextSize(18);

            tvTalukData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvTalukData.setGravity(Gravity.CENTER);
            tvTalukData.setBackgroundColor(Color.parseColor("#ECEFF1"));
            tvTalukData.setText(arrayTaluk.get(0));
            tvTalukData.setTextColor(Color.BLACK);
            tvTalukData.setTextSize(18);

            tvHobliData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvHobliData.setGravity(Gravity.CENTER);
            tvHobliData.setBackgroundColor(Color.parseColor("#FFFDE7"));
            tvHobliData.setText(arrayHobli.get(0));
            tvHobliData.setTextColor(Color.BLACK);
            tvHobliData.setTextSize(18);

            tvVillageData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvVillageData.setGravity(Gravity.CENTER);
            tvVillageData.setBackgroundColor(Color.parseColor("#ECEFF1"));
            tvVillageData.setText(arrayVillage.get(0));
            tvVillageData.setTextColor(Color.BLACK);
            tvVillageData.setTextSize(18);

            tvSurveyData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvSurveyData.setGravity(Gravity.CENTER);
            tvSurveyData.setBackgroundColor(Color.parseColor("#FFFDE7"));
            tvSurveyData.setText(arraySurvey.get(i));
            tvSurveyData.setTextColor(Color.BLACK);
            tvSurveyData.setTextSize(18);

            tvCropData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvCropData.setGravity(Gravity.CENTER);
            tvCropData.setBackgroundColor(Color.parseColor("#ECEFF1"));
            tvCropData.setText(arrayCropName.get(0));
            tvCropData.setTextColor(Color.BLACK);
            tvCropData.setTextSize(18);

            tbrowData.addView(tvDistrictData);
            tbrowData.addView(tvTalukData);
            tbrowData.addView(tvHobliData);
            tbrowData.addView(tvVillageData);
            tbrowData.addView(tvSurveyData);
            tbrowData.addView(tvCropData);
        }
        tbUserDetails.addView(tbrowData);
        }
    }

    private void addFertilizerTableData() {

        for(int i=0;i<arrayCropArea.size();i++){
            TableRow tbrowdata= new TableRow(mContext);
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
            tvNPKData.setText("Nitrogen: "+arrayRequiredNitrogen.get(i)+", Phosphorous: "+arrayRequiredPhospohorous.get(i)+", Potash: "+arrayRequiredPotash.get(i));
            tvNPKData.setTextColor(Color.BLACK);
            tvNPKData.setTextSize(18);

            tbrowdata.addView(tvAreaData);
            tbrowdata.addView(tvNPKData);
            tbAreaDetails.addView(tbrowdata);
        }

    }

    private void clearViews() {
        int tablecount1 = tbUserDetails.getChildCount();
        for(int i =1 ;i<tablecount1;i++){
            View child = tbUserDetails.getChildAt(i);
            if(child instanceof TableRow)((ViewGroup) child).removeAllViews();
        }

        int tablecount2 = tbAreaDetails.getChildCount();
        for(int i =1 ;i<tablecount2;i++){
            View child = tbAreaDetails.getChildAt(i);
            if(child instanceof TableRow)((ViewGroup) child).removeAllViews();
        }
        // tableLayout
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