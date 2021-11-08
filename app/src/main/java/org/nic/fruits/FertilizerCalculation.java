package org.nic.fruits;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.pojo.ModelCropFertilizerMasternpk;
import org.nic.fruits.pojo.ModelCropMasterType;
import org.nic.fruits.pojo.ModelFertilizerCropMaster;
import org.nic.fruits.pojo.ModelFertilizerNameMaster;
import org.nic.fruits.pojo.ModelIrrigationType;
import org.nic.fruits.pojo.ModelPlantAgeMaster;
import org.nic.fruits.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

//Code by Abhishek
public class FertilizerCalculation extends AppCompatActivity {

    Context mContext;
    String farmerID;
    String keyValue;
    AppDatabase appDatabase;
    LinearLayout lyAgeplant,lyRecommendation;
    RelativeLayout rlTableAddfertlizer;
    Spinner spPlanType,spCropName,spAgePlant,spIrrigationType,spFertilizerName;
    TextView tvNutrient,tvTotalNutrient,tvNutrientValue,tvNPKValue;
    EditText etAcre,etGunta;
    RadioGroup rgFertilizerType;
    RadioButton rbStraightFertilizer,rbComplexFertilizer;
    Button btnAddFertlizer,btnCheckFertilizer,btnClear,btnDeleteFertilzer;
    List<String> arrayPlanType;
    List<String> arrayCropName;
    List<String> arrayAgeofPlant;
    List<String> arrayIrrigationType;
    List<String> arrayFertilizerName;
    List<String> arrayFertilizerId;
    List<String> arrayAllCrops;
    List<String> arrayCount;
    List<String> arrayFertilizerDataPhosphorous;
    List<String> arrayFertilizerDataNitrogen;
    List<String> arrayFertilizerDataPotash;
    List<String> arrayCropFertilizerData;
    List<String> arrayFertilizerMasterData;
    List<String> arrayDisplay;
    String spPlanTypeValue = "";
    String planTypeValue = "";
    String fertilizerCropNameSelected = "";
    String plantAgeSelected = "";
    String spIrrigationTypeValue ="";
    String irrigationTypeValue = "";
    String fertilizerNameSelected = "";
    String fertilizerIdSelected = "";
    String fertilizerTypeSelected = "";
    String fertilizerType = "";
    int count = 1;
    TableLayout tableAddFertilizer;
    List<String> arrayCropCodeCropMaster;
    List<String> arrayNPK;
    List<String> arrayNitrogenNPK;
    List<String> arrayPhosphorousNPK;
    List<String> arrayPotashNPK;
    List<String> arrayCropCodeNPK;
    String cropcodeCropMaster = "";
    List<String> arrayNutrients;
    int requiredNitrogen,requiredPhosphorous,requiredPotash;
    String cropfertilizerNitrogen="",cropfertilizerPhosphorous="",cropfertilizerPotash="";
    String fertilizermasterNitrogen="",fertilizermasterPhosphorous="",fertilizermasterPotash="";
    List<String> arrayFertilizerDetailsList;
    String[] tempfmdata = new String[0];
    int finalDataObtained = 0;
    int fmNitrogen = 0, fmPhosphorous = 0, fmPotash = 0;
    int phosphorous = 0;
    int nitrogen = 0;
    int potash = 0;
    Locale locale;
    Set<String> npk;
    String remainingGunta;
    Float guntaTemp;
    Double totalArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer_calculation);

        getSupportActionBar().setTitle(getResources().getString(R.string.fertilizercalculation));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SharedPreferences prefs = getSharedPreferences("com.example.fruites", MODE_PRIVATE);
        farmerID = prefs.getString("FarmerID", "default_value_here_if_string_is_missing");
        keyValue = prefs.getString("KeyValue", "default_value_here_if_string_is_missing");
        mContext = this;
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        locale = Utils.getCurrentLocale(mContext);

        lyAgeplant= (LinearLayout) findViewById(R.id.linearLayoutageplant);
        lyRecommendation = (LinearLayout) findViewById(R.id.linearLayoutnutrientrecototal);
        rlTableAddfertlizer = (RelativeLayout) findViewById(R.id.RelativeLayoutaddfertilizer);
        tableAddFertilizer = (TableLayout) findViewById(R.id.table_main_add_fertilizer);
        spPlanType = (Spinner) findViewById(R.id.spinnerPlanType);
        spCropName = (Spinner) findViewById(R.id.spinnerCropName);
        spAgePlant = (Spinner) findViewById(R.id.spinnerageplant);
        spIrrigationType = (Spinner) findViewById(R.id.spinnerIrrigationType);
        spFertilizerName = (Spinner) findViewById(R.id.spinnerFertilizerName);
        tvNutrient = (TextView) findViewById(R.id.tv_nutrient_recommendation);
        tvNutrientValue = (TextView) findViewById(R.id.tv_nutrientvalue);
        tvNPKValue = (TextView) findViewById(R.id.tv_npkvalue);
        tvTotalNutrient = (TextView) findViewById(R.id.tv_nutrient_recommendationtotal);
        etAcre = (EditText) findViewById(R.id.et_AcreFertilizer);
        etGunta = (EditText) findViewById(R.id.et_GuntaFertilizer);
        rgFertilizerType = (RadioGroup) findViewById(R.id.radioGroupFertilizerType);
        rbStraightFertilizer = (RadioButton) findViewById(R.id.radioStraightFertilizer);
        rbComplexFertilizer = (RadioButton) findViewById(R.id.radioComplexFertilizer);
        btnAddFertlizer = (Button) findViewById(R.id.btn_add_fertilizer);
        btnCheckFertilizer = (Button) findViewById(R.id.btn_check_fertilizer_reco);
        btnClear = (Button) findViewById(R.id.btn_check_clear);
        btnDeleteFertilzer= (Button) findViewById(R.id.btn_delete_fertilizer);
        rlTableAddfertlizer.setVisibility(View.GONE);

        spPlanType.setSelection(0);
        spCropName.setSelection(0);
        spAgePlant.setSelection(0);
        spIrrigationType.setSelection(0);
        spFertilizerName.setSelection(0);

        try {
            etAcre.getText().clear();
            etGunta.getText().clear();
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        rgFertilizerType.clearCheck();

        arrayPlanType = new ArrayList<>();
        arrayCropName = new ArrayList<>();
        arrayAgeofPlant = new ArrayList<>();
        arrayIrrigationType = new ArrayList<>();
        arrayFertilizerName = new ArrayList<>();
        arrayFertilizerId = new ArrayList<>();
        arrayCropCodeNPK = new ArrayList<>();
        arrayCropCodeCropMaster = new ArrayList<>();
        arrayAllCrops = new ArrayList<>();
        arrayNutrients = new ArrayList<>();
        arrayCropFertilizerData = new ArrayList<>();
        arrayFertilizerMasterData = new ArrayList<>();

        arrayCount = new ArrayList<>();
        arrayDisplay = new ArrayList<>();
        arrayFertilizerDataPhosphorous = new ArrayList<>();
        arrayFertilizerDataNitrogen = new ArrayList<>();
        arrayFertilizerDataPotash = new ArrayList<>();

        arrayNPK= new ArrayList<>();
        arrayNitrogenNPK= new ArrayList<>();
        arrayPhosphorousNPK= new ArrayList<>();
        arrayPotashNPK= new ArrayList<>();
        arrayFertilizerDetailsList = new ArrayList<>();
        lyRecommendation.setVisibility(View.GONE);
        arrayFertilizerName.clear();

        ArrayAdapter<String> fertilizername_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayFertilizerName);
        fertilizername_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fertilizername_adapter.notifyDataSetChanged();
        spFertilizerName.setAdapter(fertilizername_adapter);
        spFertilizerName.setSelection(0);
        if(count==1){
            loadtable();
        }
        rgFertilizerType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                checkedId = radioGroup.getCheckedRadioButtonId();

                switch (checkedId) {
                    case R.id.radioStraightFertilizer:
                        fertilizerTypeSelected = rbStraightFertilizer.getText().toString();
                        fertilizerType = "S";
                        if(!planTypeValue.equals("") && !fertilizerCropNameSelected.equals("") && !plantAgeSelected.equals("") && !spIrrigationTypeValue.equals("") && !etAcre.getText().toString().equals("") && !etGunta.getText().toString().equals("")) {
                            arrayFertilizerName.clear();
                            if (locale.toString().equals("en")) {
                                arrayFertilizerName.add(0,"Select Fertilizer");
                            }else{
                                arrayFertilizerName.add(0, "ರಸಗೊಬ್ಬರವನ್ನು ಆಯ್ಕೆಮಾಡಿ");
                            }

                            Fertilizernamemaster(fertilizerType);
                        }else{
                            spFertilizerName.setSelection(0);
                        }
                        break;

                    case R.id.radioComplexFertilizer:
                        fertilizerTypeSelected = rbComplexFertilizer.getText().toString();
                        fertilizerType = "C";
                        if(!planTypeValue.equals("") && !fertilizerCropNameSelected.equals("") && !plantAgeSelected.equals("") && !spIrrigationTypeValue.equals("") && !etAcre.getText().toString().equals("") && !etGunta.getText().toString().equals("")) {
                            arrayFertilizerName.clear();
                            if (locale.toString().equals("en")) {
                                arrayFertilizerName.add(0,"Select Fertilizer");
                            }else{
                                arrayFertilizerName.add(0, "ರಸಗೊಬ್ಬರವನ್ನು ಆಯ್ಕೆಮಾಡಿ");
                            }
                            Fertilizernamemaster(fertilizerType);
                        }else{
                            spFertilizerName.setSelection(0);
                        }
                        break;

                }

            }
        });

        btnAddFertlizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (locale.toString().equals("en")) {

                        if (!spFertilizerName.getSelectedItem().toString().equals("Select Fertilizer")) {
                            if (count <= 3) {
                                if (!arrayFertilizerDetailsList.contains(spFertilizerName.getSelectedItem().toString())) {
                                    rlTableAddfertlizer.setVisibility(View.VISIBLE);
                                    arrayFertilizerDetailsList.add(spFertilizerName.getSelectedItem().toString());
                                    arrayCount.add(String.valueOf(count));
                                    addfertilizer();
                                    fmcalculation(fertilizerIdSelected);
                                } else {
                                    Toast.makeText(mContext, "Fertilizer already selected", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Cannot add fertilizers reached limit", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "Please select fertilizer name", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        if (!spFertilizerName.getSelectedItem().toString().equals("ರಸಗೊಬ್ಬರವನ್ನು ಆಯ್ಕೆಮಾಡಿ")) {
                            if (count <= 3) {
                                if (!arrayFertilizerDetailsList.contains(spFertilizerName.getSelectedItem().toString())) {
                                    rlTableAddfertlizer.setVisibility(View.VISIBLE);
                                    arrayFertilizerDetailsList.add(spFertilizerName.getSelectedItem().toString());
                                    arrayCount.add(String.valueOf(count));
                                    addfertilizer();
                                    fmcalculation(fertilizerIdSelected);
                                } else {
                                    Toast.makeText(mContext, "ರಸಗೊಬ್ಬರವನ್ನು ಈಗಾಗಲೇ ಆಯ್ಕೆ ಮಾಡಲಾಗಿದೆ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "ಮಿತಿಯನ್ನು ತಲುಪಿದ ರಸಗೊಬ್ಬರಗಳನ್ನು ಸೇರಿಸಲು ಸಾಧ್ಯವಿಲ್ಲ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "ದಯವಿಟ್ಟು ರಸಗಗೊಬ್ಬರದ ಹೆಸರನ್ನು ಆಯ್ಕೆ ಮಾಡಿ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

        loadSpinners();
        arrayAllCrops.clear();

        MainViewModel allcrops = ViewModelProviders.of(this).get(MainViewModel.class);
        allcrops.getallcropsf().observe(this, new Observer<List<ModelFertilizerCropMaster>>() {

            @Override
            public void onChanged(@Nullable List<ModelFertilizerCropMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()){
                    for(ModelFertilizerCropMaster taskEntry:taskEntries) {

                        if (locale.toString().equals("en")) {
                            if (!arrayAllCrops.contains(taskEntry.getCropname_eng())) {
                                arrayAllCrops.add(taskEntry.getCropname_eng());
                            }
                        } else {
                            if (!arrayAllCrops.contains(taskEntry.getCropname_kn())) {
                                arrayAllCrops.add(taskEntry.getCropname_kn());
                            }
                        }
                    }
                }
            }
        });

        btnCheckFertilizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    calculation();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        etAcre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rlTableAddfertlizer.setVisibility(View.GONE);

                arrayCount.clear();
                count = 1;
                arrayFertilizerDetailsList.clear();
                arrayFertilizerDataPhosphorous.clear();
                arrayFertilizerMasterData.clear();
                rgFertilizerType.clearCheck();
                spFertilizerName.setSelection(0);
                clearViews();
                fmNitrogen = 0;
                fmPhosphorous = 0;
                fmPotash = 0;
                phosphorous = 0;
                nitrogen = 0;
                potash = 0;
                finalDataObtained = 0;
                etGunta.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etGunta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    if (etGunta.getText().toString().length() >= 1) {
                        if (Integer.parseInt(etGunta.getText().toString()) < 40) {
                            rlTableAddfertlizer.setVisibility(View.GONE);
                            arrayCount.clear();
                            count = 1;
                            arrayFertilizerDetailsList.clear();
                            arrayFertilizerDataPhosphorous.clear();
                            arrayFertilizerMasterData.clear();
                            rgFertilizerType.clearCheck();
                            spFertilizerName.setSelection(0);
                            clearViews();
                            fmNitrogen = 0;
                            fmPhosphorous = 0;
                            fmPotash = 0;
                            phosphorous = 0;
                            nitrogen = 0;
                            potash = 0;
                            finalDataObtained = 0;
                            try {
                                caltotalarea();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(FertilizerCalculation.this).create();
                            alertDialog.setTitle(R.string.Note);
                            alertDialog.setMessage(getString(R.string.guntaalertmessage));
                            alertDialog.setButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    etGunta.setText("");
                                    tvTotalNutrient.setText(R.string.recommendation_of_npk_for_extent_selected);
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGunta.setText("");
                            etGunta.requestFocus();

                        }
                    }

                }catch (NumberFormatException npe){
                    npe.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FertilizerCalculation.this,FertilizerCalculation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

        btnDeleteFertilzer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    clearViews();
                    rgFertilizerType.clearCheck();
                    spFertilizerName.setSelection(0);
                    arrayCount.clear();
                    count = 1;
                    arrayFertilizerDetailsList.clear();
                    arrayFertilizerDataPhosphorous.clear();
                    arrayFertilizerMasterData.clear();
                    requiredNitrogen = (int) Math.round(Integer.parseInt(cropfertilizerNitrogen) * totalArea);
                    requiredPhosphorous = (int) Math.round(Integer.parseInt(cropfertilizerPhosphorous) * totalArea);
                    requiredPotash = (int) Math.round(Integer.parseInt(cropfertilizerPotash) * totalArea);
                    fmNitrogen = 0;
                    fmPhosphorous = 0;
                    fmPotash = 0;
                    phosphorous = 0;
                    nitrogen = 0;
                    potash = 0;
                    finalDataObtained = 0;
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void loadtable(){

        TableRow tbrow= new TableRow(mContext);
        TextView tv_slno = new TextView(mContext);

        tv_slno.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_slno.setGravity(Gravity.CENTER);
        tv_slno.setBackgroundColor(Color.parseColor("#42be7b"));

        tv_slno.setText(R.string.slno);
        tv_slno.setTextSize(18);
        tv_slno.setTextColor(Color.BLACK);

        TextView tv_fertilizer_name = new TextView(mContext);

        tv_fertilizer_name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_fertilizer_name.setGravity(Gravity.CENTER);
        tv_fertilizer_name.setBackgroundColor(Color.parseColor("#81D4FA"));

        tv_fertilizer_name.setText(R.string.fertilizer_name);
        tv_fertilizer_name.setTextColor(Color.BLACK);
        tv_fertilizer_name.setTextSize(18);

        tbrow.addView(tv_slno);
        tbrow.addView(tv_fertilizer_name);

        tableAddFertilizer.addView(tbrow);

    }

    private void addfertilizer() {
        System.out.println("array_fertilizerdetailslist : " + arrayFertilizerDetailsList);

        TableRow tbrow_content = new TableRow(mContext);
        tbrow_content.removeAllViews();
        TextView tv_sl = new TextView(mContext);
        tv_sl.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_sl.setGravity(Gravity.CENTER);
        tv_sl.setBackgroundColor(Color.parseColor("#a8dba8"));
        tv_sl.setTextColor(Color.BLACK);
        tv_sl.setTextSize(18);
        tv_sl.setText("" + count);

        TextView tv_fname = new TextView(mContext);
        tv_fname.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_fname.setBackgroundColor(Color.parseColor("#E1F5FE"));
        tv_fname.setTextSize(18);
        tv_fname.setTextColor(Color.BLACK);
        tv_fname.setGravity(Gravity.CENTER);
        tv_fname.setText("" + spFertilizerName.getSelectedItem().toString()); //arrayFertilizerDetailsList.get(count-1) spFertilizerName.getSelectedItem().toString()

        tbrow_content.addView(tv_sl);
        tbrow_content.addView(tv_fname);

        tableAddFertilizer.addView(tbrow_content);
        count = count + 1;
    }

    private void fmcalculation(String feid) {

        MainViewModel viewModelfmaster = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModelfmaster.getAllFertilizerID(feid).observe(this, new Observer<List<ModelFertilizerNameMaster>>() {

            @Override
            public void onChanged(@Nullable List<ModelFertilizerNameMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()){
                    for(ModelFertilizerNameMaster taskEntry:taskEntries) {
                        fertilizermasterNitrogen = taskEntry.getFertilizernitrogen();
                        fertilizermasterPhosphorous = taskEntry.getFertilizerphosphorous();
                        fertilizermasterPotash = taskEntry.getFertilizerpotash();
                        arrayFertilizerMasterData.add(taskEntry.getFertilizernitrogen()+"-"+taskEntry.getFertilizerphosphorous()+"-"+taskEntry.getFertilizerpotash());
                    }
                }
            }
        });
    }

    private void loadSpinners() {
        if (locale.toString().equals("en")) {
            arrayPlanType.add(0, "Select Plan Type");
        }else{
            arrayPlanType.add(0, "ಯೋಜನೆ ಪ್ರಕಾರವನ್ನು ಆಯ್ಕೆ ಮಾಡಿ");
        }
        ArrayAdapter<String> plantype_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayPlanType);
        plantype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        MainViewModel viewModelPlanType = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModelPlanType.getCropMastertype().observe(this, new Observer<List<ModelCropMasterType>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropMasterType> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()){
                    for(ModelCropMasterType taskEntry:taskEntries){
                        if (locale.toString().equals("en")) {
                            if(!arrayPlanType.add(taskEntry.getCroptype_eng())){
                                arrayPlanType.add(taskEntry.getCroptype_eng());
                            }

                        } else {
                            if(!arrayPlanType.contains(taskEntry.getCroptype_kn())){
                                arrayPlanType.add(taskEntry.getCroptype_kn());
                            }
                        }

                    }
                }
                else{
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.planDetails))
                            .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, FertilizerCalculation.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            })
                            .show();
                }
                plantype_adapter.notifyDataSetChanged();
                spPlanType.setAdapter(plantype_adapter);
                spPlanType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            if(spPlanType.getSelectedItem().toString().equals("Perennial") || spPlanType.getSelectedItem().toString().equals("ದೀರ್ಘಕಾಲಿಕ")){
                                spPlanTypeValue = spPlanType.getSelectedItem().toString();
                                planTypeValue = "P";
                                lyAgeplant.setVisibility(View.VISIBLE);
                                spAgePlant.setSelection(0);
                                spIrrigationType.setSelection(0);
                                tvNutrient.setText(R.string.nutrient_recommendation_for_1_acre);
                                try {
                                    etAcre.getText().clear();
                                    etGunta.getText().clear();
                                }catch (NumberFormatException e){
                                    e.printStackTrace();
                                }


                                arrayCount.clear();
                                count = 1;
                                arrayFertilizerDetailsList.clear();
                                arrayFertilizerDataPhosphorous.clear();
                                arrayFertilizerMasterData.clear();
                                rgFertilizerType.clearCheck();
                                spFertilizerName.setSelection(0);
                                clearViews();
                                fmNitrogen = 0;
                                fmPhosphorous = 0;
                                fmPotash = 0;
                                phosphorous = 0;
                                nitrogen = 0;
                                potash = 0;
                                finalDataObtained = 0;
                                Perennialcropmaster("P");
                            }else if(spPlanType.getSelectedItem().toString().equals("Seasonal") || spPlanType.getSelectedItem().toString().equals("ಕಾಲೋಚಿತ")){
                                spPlanTypeValue = spPlanType.getSelectedItem().toString();
                                planTypeValue = "S";
                                lyAgeplant.setVisibility(View.GONE);
                                spAgePlant.setSelection(0);
                                spIrrigationType.setSelection(0);
                                tvNutrient.setText(R.string.nutrient_recommendation_for_1_acre);
                                try {
                                    etAcre.getText().clear();
                                    etGunta.getText().clear();
                                }catch (NumberFormatException e){
                                    e.printStackTrace();
                                }

                                arrayCount.clear();
                                count = 1;
                                arrayFertilizerDetailsList.clear();
                                arrayFertilizerDataPhosphorous.clear();
                                arrayFertilizerMasterData.clear();
                                rgFertilizerType.clearCheck();
                                spFertilizerName.setSelection(0);
                                clearViews();
                                fmNitrogen = 0;
                                fmPhosphorous = 0;
                                fmPotash = 0;
                                phosphorous = 0;
                                nitrogen = 0;
                                potash = 0;
                                finalDataObtained = 0;
                                Seasonalcropmaster("S");
                            }

                        }else{
                            spPlanType.setSelection(0);

                            arrayCropName.clear();
                            if (locale.toString().equals("en")) {
                                arrayCropName.add(0,"Select Crop");
                            }else{
                                arrayCropName.add(0, "ಬೆಳೆ ಆಯ್ಕೆಮಾಡಿ");
                            }

                            ArrayAdapter<String> cropmaster_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayCropName);
                            cropmaster_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            cropmaster_adapter.notifyDataSetChanged();
                            spCropName.setAdapter(cropmaster_adapter);
                            spCropName.setSelection(0);

                            lyAgeplant.setVisibility(View.VISIBLE);
                            spAgePlant.setSelection(0);
                            spIrrigationType.setSelection(0);
                            tvNutrient.setText(R.string.nutrient_recommendation_for_1_acre);

                            spFertilizerName.setSelection(0);
                            rgFertilizerType.clearCheck();
                            try {
                                etAcre.getText().clear();
                                etGunta.getText().clear();
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                            }
                            //   rlTableAddfertlizer.setVisibility(View.GONE);
                            tvTotalNutrient.setText(R.string.recommendation_of_npk_for_extent_selected);

                            arrayCount.clear();
                            count = 1;
                            arrayFertilizerDetailsList.clear();
                            arrayFertilizerDataPhosphorous.clear();
                            arrayFertilizerMasterData.clear();
                            rgFertilizerType.clearCheck();
                            spFertilizerName.setSelection(0);
                            clearViews();
                            fmNitrogen = 0;
                            fmPhosphorous = 0;
                            fmPotash = 0;
                            phosphorous = 0;
                            nitrogen = 0;
                            potash = 0;
                            finalDataObtained = 0;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                });
            }

        });



        if (locale.toString().equals("en")) {
            arrayIrrigationType.add(0,"Select Irrigation Type");
        }else{
            arrayIrrigationType.add(0, "ನೀರಾವರಿ ಪ್ರಕಾರವನ್ನು ಆಯ್ಕೆಮಾಡಿ");
        }
        ArrayAdapter<String> irrigationtype_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayIrrigationType);
        irrigationtype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        MainViewModel viewModelIrrigationType = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModelIrrigationType.getirrigationtype().observe(this, new Observer<List<ModelIrrigationType>>() {

            @Override
            public void onChanged(@Nullable List<ModelIrrigationType> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()){
                    for(ModelIrrigationType taskEntry:taskEntries){
                        if (locale.toString().equals("en")) {
                            if(!arrayIrrigationType.add(taskEntry.getIrrigationtypeeng())){
                                arrayIrrigationType.add(taskEntry.getIrrigationtypeeng());
                            }

                        } else {
                            if(!arrayIrrigationType.contains(taskEntry.getIrrigationtypekn())){
                                arrayIrrigationType.add(taskEntry.getIrrigationtypekn());
                            }
                        }

                    }
                }
                else{
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.irrigationDetails))
                            .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, FertilizerCalculation.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            })
                            .show();
                }
                irrigationtype_adapter.notifyDataSetChanged();
                spIrrigationType.setAdapter(irrigationtype_adapter);
                spIrrigationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){

                            if (locale.toString().equals("en")) {
                                if(spIrrigationType.getSelectedItem().toString().equals("Rainfed")){
                                    spIrrigationTypeValue = spIrrigationType.getSelectedItem().toString();
                                    irrigationTypeValue = "R";
                                    try {
                                        etAcre.getText().clear();
                                        etGunta.getText().clear();
                                    }catch (NumberFormatException e){
                                        e.printStackTrace();
                                    }
                                    arrayCount.clear();
                                    count = 1;
                                    arrayFertilizerDetailsList.clear();
                                    arrayFertilizerDataPhosphorous.clear();
                                    arrayFertilizerMasterData.clear();
                                    rgFertilizerType.clearCheck();
                                    spFertilizerName.setSelection(0);
                                    clearViews();
                                    fmNitrogen = 0;
                                    fmPhosphorous = 0;
                                    fmPotash = 0;
                                    phosphorous = 0;
                                    nitrogen = 0;
                                    potash = 0;
                                    finalDataObtained = 0;
                                }
                                else if(spIrrigationType.getSelectedItem().toString().equals("Irrigated")){
                                    spIrrigationTypeValue = spIrrigationType.getSelectedItem().toString();
                                    irrigationTypeValue = "I";
                                    try {
                                        etAcre.getText().clear();
                                        etGunta.getText().clear();
                                    }catch (NumberFormatException e){
                                        e.printStackTrace();
                                    }
                                    arrayCount.clear();
                                    count = 1;
                                    arrayFertilizerDetailsList.clear();
                                    arrayFertilizerDataPhosphorous.clear();
                                    arrayFertilizerMasterData.clear();
                                    clearViews();
                                }

                            }else{
                                if(spIrrigationType.getSelectedItem().toString().equals("ಮಳೆಯಾಶ್ರಿತ")){
                                    spIrrigationTypeValue = spIrrigationType.getSelectedItem().toString();
                                    irrigationTypeValue = "R";
                                    try {
                                        etAcre.getText().clear();
                                        etGunta.getText().clear();
                                    }catch (NumberFormatException e){
                                        e.printStackTrace();
                                    }
                                    arrayCount.clear();
                                    count = 1;
                                    arrayFertilizerDetailsList.clear();
                                    arrayFertilizerDataPhosphorous.clear();
                                    arrayFertilizerMasterData.clear();
                                    rgFertilizerType.clearCheck();
                                    spFertilizerName.setSelection(0);
                                    clearViews();
                                    fmNitrogen = 0;
                                    fmPhosphorous = 0;
                                    fmPotash = 0;
                                    phosphorous = 0;
                                    nitrogen = 0;
                                    potash = 0;
                                    finalDataObtained = 0;
                                }
                                else if(spIrrigationType.getSelectedItem().toString().equals("ನೀರಾವರಿ")){
                                    spIrrigationTypeValue = spIrrigationType.getSelectedItem().toString();
                                    irrigationTypeValue = "I";
                                    try {
                                        etAcre.getText().clear();
                                        etGunta.getText().clear();
                                    }catch (NumberFormatException e){
                                        e.printStackTrace();
                                    }
                                    arrayCount.clear();
                                    count = 1;
                                    arrayFertilizerDetailsList.clear();
                                    arrayFertilizerDataPhosphorous.clear();
                                    arrayFertilizerMasterData.clear();
                                    rgFertilizerType.clearCheck();
                                    spFertilizerName.setSelection(0);
                                    clearViews();
                                    fmNitrogen = 0;
                                    fmPhosphorous = 0;
                                    fmPotash = 0;
                                    phosphorous = 0;
                                    nitrogen = 0;
                                    potash = 0;
                                    finalDataObtained = 0;
                                }

                            }

                            tvNutrient.setText(R.string.nutrient_recommendation_for_1_acre);

                            if(plantAgeSelected.equals("0")) {
                                //String plantage = "0";

                                checkNutrient(cropcodeCropMaster, irrigationTypeValue, "0");
                                cropcalculation1(cropcodeCropMaster,irrigationTypeValue,"0");
                            }else{

                                if(plantAgeSelected.matches("[0-9+]+")){
                                    String result = plantAgeSelected.replaceAll("[+]","");
                                    System.out.println("result " + result);
                                    plantAgeSelected=result;
                                }
                                checkNutrient(cropcodeCropMaster, irrigationTypeValue, plantAgeSelected);
                                cropcalculation2(cropcodeCropMaster,irrigationTypeValue,plantAgeSelected);
                            }


                        }else{
                            spIrrigationType.setSelection(0);
                            tvNutrient.setText(R.string.nutrient_recommendation_for_1_acre);

                            spFertilizerName.setSelection(0);
                            rgFertilizerType.clearCheck();
                            try {
                                etAcre.getText().clear();
                                etGunta.getText().clear();
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                            }
                            rlTableAddfertlizer.setVisibility(View.GONE);
                            tvTotalNutrient.setText(R.string.recommendation_of_npk_for_extent_selected);
                            rlTableAddfertlizer.setVisibility(View.GONE);

                            arrayCount.clear();
                            count = 1;
                            arrayFertilizerDetailsList.clear();
                            arrayFertilizerDataPhosphorous.clear();
                            arrayFertilizerMasterData.clear();
                            rgFertilizerType.clearCheck();
                            spFertilizerName.setSelection(0);
                            clearViews();
                            fmNitrogen = 0;
                            fmPhosphorous = 0;
                            fmPotash = 0;
                            phosphorous = 0;
                            nitrogen = 0;
                            potash = 0;
                            finalDataObtained = 0;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                });
            }

        });
    }

    private void clearViews() {
        int tablecount = tableAddFertilizer.getChildCount();
        for(int i =1 ;i<tablecount;i++){
            View child = tableAddFertilizer.getChildAt(i);
            if(child instanceof TableRow)((ViewGroup) child).removeAllViews();
        }

        // tableLayout
    }

    private void Fertilizernamemaster(String type){

        //    arrayFertilizerName.clear();
        arrayFertilizerId.clear();
        arrayFertilizerId.add("");
        // arrayFertilizerName.add(0,"Select Fertilizer");
        ArrayAdapter<String> fertilizername_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayFertilizerName);
        fertilizername_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MainViewModel viewModelFertilizerName = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModelFertilizerName.getAllFertilizerNames(type).observe(this, new Observer<List<ModelFertilizerNameMaster>>() {

            @Override
            public void onChanged(@Nullable List<ModelFertilizerNameMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()){
                    for(ModelFertilizerNameMaster taskEntry:taskEntries){

                        if (locale.toString().equals("en")) {
                            if(!arrayFertilizerName.contains(taskEntry.getFertilizername())){
                                arrayFertilizerName.add(taskEntry.getFertilizername());
                                arrayFertilizerId.add(taskEntry.getFeid());
                            }

                        } else {
                         /*   if(!arrayFertilizerName.contains(taskEntry.getFertilizerknname())){
                                arrayFertilizerName.add(taskEntry.getFertilizerknname());
                                arrayFertilizerId.add(taskEntry.getFeid());
                            }*/
                        }


                    }
                }
                else{
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.fertilizerDetails))
                            .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, FertilizerCalculation.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            })
                            .show();
                }

                fertilizername_adapter.notifyDataSetChanged();
                spFertilizerName.setAdapter(fertilizername_adapter);
                spFertilizerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){

                            fertilizerNameSelected = spFertilizerName.getSelectedItem().toString();
                            fertilizerIdSelected = arrayFertilizerId.get(i);
                            btnAddFertlizer.setVisibility(View.VISIBLE);

                        }else{
                            spFertilizerName.setSelection(0);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        btnAddFertlizer.setVisibility(View.GONE);
                        rlTableAddfertlizer.removeAllViews();
                    }
                });
            }

        });
    }

    private void Seasonalcropmaster(String S) {
        arrayCropName.clear();
        if (locale.toString().equals("en")) {
            arrayCropName.add(0,"Select Crop");
        }else{
            arrayCropName.add(0, "ಬೆಳೆ ಆಯ್ಕೆಮಾಡಿ");
        }
        ArrayAdapter<String> seasonalcropmaster_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayCropName);
        seasonalcropmaster_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MainViewModel viewModelFCropMaster = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModelFCropMaster.getcropsbasedontype(S).observe(this, new Observer<List<ModelFertilizerCropMaster>>() {

            @Override
            public void onChanged(@Nullable List<ModelFertilizerCropMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()){
                    for(ModelFertilizerCropMaster taskEntry:taskEntries){

                        if (locale.toString().equals("en")) {
                            if(!arrayCropName.contains(taskEntry.getCropname_eng())){
                                arrayCropName.add(taskEntry.getCropname_eng());
                            }

                        } else {
                            if(!arrayCropName.contains(taskEntry.getCropname_kn())){
                                arrayCropName.add(taskEntry.getCropname_kn());

                            }
                        }


                    }
                }else{
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.cropDetails))
                            .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, FertilizerCalculation.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            })
                            .show();
                }

                seasonalcropmaster_adapter.notifyDataSetChanged();
                spCropName.setAdapter(seasonalcropmaster_adapter);

                spCropName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            fertilizerCropNameSelected = spCropName.getSelectedItem().toString();
                            try {
                                etAcre.getText().clear();
                                etGunta.getText().clear();
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                            }

                            plantAgeSelected = "0";
                            spAgePlant.setSelection(0);
                            System.out.println("\nspCropName.getSelectedItem().toString() : " + spCropName.getSelectedItem().toString());
                            System.out.println("\narrayAllCropssize " + arrayAllCrops.size() );
                            cropcodeCropMaster = String.valueOf(arrayAllCrops.indexOf(spCropName.getSelectedItem().toString())+1);
                            System.out.print("cropcode_cropmaster " + cropcodeCropMaster);
                            cropfertilizermaster_npk(cropcodeCropMaster);
                            spAgePlant.setSelection(0);
                            arrayCount.clear();
                            count = 1;
                            arrayFertilizerDetailsList.clear();
                            arrayFertilizerDataPhosphorous.clear();
                            arrayFertilizerMasterData.clear();
                            rgFertilizerType.clearCheck();
                            spFertilizerName.setSelection(0);
                            clearViews();
                            fmNitrogen = 0;
                            fmPhosphorous = 0;
                            fmPotash = 0;
                            phosphorous = 0;
                            nitrogen = 0;
                            potash = 0;
                            finalDataObtained = 0;
                        }else{
                            spCropName.setSelection(0);
                            spAgePlant.setSelection(0);
                            spIrrigationType.setSelection(0);
                            tvNutrient.setText(R.string.nutrient_recommendation_for_1_acre);
                            try {
                                etAcre.getText().clear();
                                etGunta.getText().clear();
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                            }
                            spFertilizerName.setSelection(0);
                            rgFertilizerType.clearCheck();

                            rlTableAddfertlizer.setVisibility(View.GONE);
                            tvTotalNutrient.setText(R.string.recommendation_of_npk_for_extent_selected);
                            arrayCount.clear();
                            count = 1;
                            arrayFertilizerDetailsList.clear();
                            arrayFertilizerDataPhosphorous.clear();
                            arrayFertilizerMasterData.clear();
                            clearViews();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

        });

    }

    private void Perennialcropmaster(String P) {

        arrayCropName.clear();
        arrayCropCodeCropMaster.clear();
        if (locale.toString().equals("en")) {
            arrayCropName.add(0,"Select Crop");
        }else{
            arrayCropName.add(0, "ಬೆಳೆ ಆಯ್ಕೆಮಾಡಿ");
        }
        ArrayAdapter<String> perennialcropmaster_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayCropName);
        perennialcropmaster_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCropName.setAdapter(perennialcropmaster_adapter);
        MainViewModel viewModelFCropMaster = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModelFCropMaster.getcropsbasedontype(P).observe(this, new Observer<List<ModelFertilizerCropMaster>>() {

            @Override
            public void onChanged(@Nullable List<ModelFertilizerCropMaster> taskEntries) {

                if (taskEntries != null && !taskEntries.isEmpty()){

                    for(ModelFertilizerCropMaster taskEntry:taskEntries) {

                        if (locale.toString().equals("en")) {
                            if(!arrayCropName.contains(taskEntry.getCropname_eng())){
                                arrayCropName.add(taskEntry.getCropname_eng());
                                // arrayCropCodeCropMaster.add(taskEntry.getCropcode());
                            }

                        } else {
                            if (!arrayCropName.contains(taskEntry.getCropname_kn())) {
                                arrayCropName.add(taskEntry.getCropname_kn());

                            }
                        }
                        arrayCropCodeCropMaster.add(taskEntry.getCropcode());

                    }
                    perennialcropmaster_adapter.notifyDataSetChanged();

                }else{
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.cropDetails))
                            .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, FertilizerCalculation.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            })
                            .show();
                }

                System.out.print("array_cropcode_cropmaster " + arrayCropCodeCropMaster);

                spCropName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            fertilizerCropNameSelected = spCropName.getSelectedItem().toString();
                            try {
                                etAcre.getText().clear();
                                etGunta.getText().clear();
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                            }

                            System.out.println("\nspCropName.getSelectedItem().toString() : " + spCropName.getSelectedItem().toString());
                            System.out.println("\narrayAllCropssize " + arrayAllCrops.size() );
                            cropcodeCropMaster = String.valueOf(arrayAllCrops.indexOf(spCropName.getSelectedItem().toString())+1);
                            System.out.print("cropcode_cropmaster " + cropcodeCropMaster);
                            cropfertilizermaster_npk(cropcodeCropMaster);
                            spAgePlant.setSelection(0);
                            arrayCount.clear();
                        }else{
                            spCropName.setSelection(0);
                            spAgePlant.setSelection(0);
                            spIrrigationType.setSelection(0);
                            tvNutrient.setText(R.string.nutrient_recommendation_for_1_acre);
                            try {
                                etAcre.getText().clear();
                                etGunta.getText().clear();
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                            }
                            spFertilizerName.setSelection(0);
                            rgFertilizerType.clearCheck();

                            rlTableAddfertlizer.setVisibility(View.GONE);
                            tvTotalNutrient.setText(R.string.recommendation_of_npk_for_extent_selected);
                            //  count = 1;
                            arrayCount.clear();
                            arrayFertilizerDetailsList.clear();
                            arrayFertilizerDataPhosphorous.clear();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });
    }

    private void cropfertilizermaster_npk(String cropcode_cropmaster) {
        arrayCropCodeNPK.clear();

        MainViewModel npk_master = ViewModelProviders.of(this).get(MainViewModel.class);
        npk_master.getAllCropFertilizerMasternpk(cropcode_cropmaster).observe(this, new Observer<List<ModelCropFertilizerMasternpk>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropFertilizerMasternpk> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()) {
                    for (ModelCropFertilizerMasternpk taskEntry : taskEntries) {

                        arrayCropCodeNPK.add(taskEntry.getCf_plant_age());
                        Set<String> npk = new LinkedHashSet<String>(arrayCropCodeNPK);
                        arrayCropCodeNPK.clear();
                        arrayCropCodeNPK.add(0,"");
                        arrayCropCodeNPK.addAll(npk);
                        System.out.println("array_cropcode_npk: " +arrayCropCodeNPK+" :: size ::" + arrayCropCodeNPK.size());

                        arrayNitrogenNPK.add(0,"");
                        arrayNitrogenNPK.add(taskEntry.getCf_nitrogen());
                        Set<String> nitrogen_npk = new LinkedHashSet<String>(arrayNitrogenNPK);
                        arrayNitrogenNPK.clear();
                        arrayNitrogenNPK.addAll(nitrogen_npk);
                        System.out.print("array_nitrogen_npk: " + arrayNitrogenNPK);

                        arrayPhosphorousNPK.add(taskEntry.getCf_phosphorous());
                        Set<String> phosphorous_npk = new LinkedHashSet<String>(arrayPhosphorousNPK);
                        arrayPhosphorousNPK.clear();
                        arrayPhosphorousNPK.addAll(phosphorous_npk);
                        System.out.print("array_phosphorous_npk: " + arrayPhosphorousNPK);

                        arrayPotashNPK.add(taskEntry.getCf_potash());
                        Set<String> potash_npk = new LinkedHashSet<String>(arrayPotashNPK);
                        arrayPotashNPK.clear();
                        arrayPotashNPK.addAll(potash_npk);
                        System.out.print("array_potash_npk: " + arrayPotashNPK);
                    }

                    ageplant();

                }
                else {
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.cropDetails))
                            .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, FertilizerCalculation.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            }).show();
                }
            }

        });

    }

    private void ageplant() {

        arrayAgeofPlant.clear();
        if (locale.toString().equals("en")) {
            arrayAgeofPlant.add(0, "Select Age of Plant");
        }else{
            arrayAgeofPlant.add(0, "ಸಸ್ಯದ ವಯಸ್ಸನ್ನು ಆಯ್ಕೆಮಾಡಿ");
        }

        ArrayAdapter<String> plantage_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayAgeofPlant);
        plantage_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAgePlant.setAdapter(plantage_adapter);
        plantage_adapter.notifyDataSetChanged();

        for(int k=0;k<arrayCropCodeNPK.size();k++) {
            System.out.print("\narray_cropcode_npk.size " + arrayCropCodeNPK.size());
            System.out.print("\narray_cropcode_npk.get(k) " + arrayCropCodeNPK.get(k));

            MainViewModel viewModelPlantAge = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModelPlantAge.getAllPlants(arrayCropCodeNPK.get(k)).observe(this, new Observer<List<ModelPlantAgeMaster>>() {

                @Override
                public void onChanged(@Nullable List<ModelPlantAgeMaster> taskEntries) {
                    if (taskEntries != null && !taskEntries.isEmpty()) {
                        for (ModelPlantAgeMaster taskEntry : taskEntries) {
                            if (!arrayAgeofPlant.contains(taskEntry.getPlantname())) {
                                arrayAgeofPlant.add(taskEntry.getPlantname());
                            }

                        }

                    }

                    spAgePlant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(i!=0){
                                plantAgeSelected = spAgePlant.getSelectedItem().toString().trim();
                                try {
                                    etAcre.getText().clear();
                                    etGunta.getText().clear();
                                }catch (NumberFormatException e){
                                    e.printStackTrace();
                                }
                                arrayCount.clear();
                                count = 1;
                                arrayFertilizerDetailsList.clear();
                                arrayFertilizerDataPhosphorous.clear();
                                arrayFertilizerMasterData.clear();
                                rgFertilizerType.clearCheck();
                                spFertilizerName.setSelection(0);
                                clearViews();
                                fmNitrogen = 0;
                                fmPhosphorous = 0;
                                fmPotash = 0;
                                phosphorous = 0;
                                nitrogen = 0;
                                potash = 0;
                                finalDataObtained = 0;
                            }else{
                                spAgePlant.setSelection(0);
                                spIrrigationType.setSelection(0);
                                spFertilizerName.setSelection(0);
                                rgFertilizerType.clearCheck();
                                try {
                                    etAcre.getText().clear();
                                    etGunta.getText().clear();
                                }catch (NumberFormatException e){
                                    e.printStackTrace();
                                }
                                rlTableAddfertlizer.setVisibility(View.GONE);
                                tvTotalNutrient.setText(R.string.recommendation_of_npk_for_extent_selected);
                                arrayCount.clear();
                                count = 1;
                                arrayFertilizerDetailsList.clear();
                                arrayFertilizerDataPhosphorous.clear();
                                arrayFertilizerMasterData.clear();
                                rgFertilizerType.clearCheck();
                                spFertilizerName.setSelection(0);
                                clearViews();
                                fmNitrogen = 0;
                                fmPhosphorous = 0;
                                fmPotash = 0;
                                phosphorous = 0;
                                nitrogen = 0;
                                potash = 0;
                                finalDataObtained = 0;
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

    public void cropcalculation1(String cropcode,String irrigationtype,String plantage){
        System.out.println("cropcode: " + cropcode);
        System.out.println("irrigationtype: " + irrigationtype);
        System.out.println("plantage: " + plantage);

        /*f(plantage.matches("[0-9+]+")){
            String result = plantage.replaceAll("[+]","");
            System.out.println("result " + result);
            plantage=result;
        }*/
        MainViewModel viewModelFertlizer = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModelFertlizer.getNutrients(cropcode,irrigationtype,plantage).observe(this, new Observer<List<ModelCropFertilizerMasternpk>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropFertilizerMasternpk> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()){
                    //   System.out.println("cf_Nitrogen: " + taskEntries.get(0).getCf_nitrogen());
                    for(ModelCropFertilizerMasternpk taskEntry:taskEntries) {

                        cropfertilizerNitrogen = taskEntry.getCf_nitrogen();
                        cropfertilizerPhosphorous = taskEntry.getCf_phosphorous();
                        cropfertilizerPotash = taskEntry.getCf_potash();
                        arrayCropFertilizerData.add(taskEntry.getCf_nitrogen()+"-"+taskEntry.getCf_phosphorous()+"-"+taskEntry.getCf_potash());
                        System.out.println("cf_Nitrogen: " + cropfertilizerNitrogen+ " " + cropfertilizerPhosphorous + " " + cropfertilizerPotash);
                    }
                }
                else {
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.plantAgeDetails))
                            .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                 /*   Intent mainActivity = new Intent(mContext, FertilizerCalculation.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();*/
                                }
                            })
                            .show();
                }
            }

        });

    }

    public void cropcalculation2(String cropcode,String irrigationtype,String plantage){
        System.out.println("cropcode2: " + cropcode);
        System.out.println("irrigationtype2: " + irrigationtype);
        System.out.println("plantage2: " + plantage);
        MainViewModel viewModelFertlizer = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModelFertlizer.getNutrients(cropcode,irrigationtype,plantage).observe(this, new Observer<List<ModelCropFertilizerMasternpk>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropFertilizerMasternpk> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()){
                    //   System.out.println("cf_Nitrogen: " + taskEntries.get(0).getCf_nitrogen());
                    for(ModelCropFertilizerMasternpk taskEntry:taskEntries) {

                        cropfertilizerNitrogen = taskEntry.getCf_nitrogen();
                        cropfertilizerPhosphorous = taskEntry.getCf_phosphorous();
                        cropfertilizerPotash = taskEntry.getCf_potash();
                        arrayCropFertilizerData.add(taskEntry.getCf_nitrogen()+"-"+taskEntry.getCf_phosphorous()+"-"+taskEntry.getCf_potash());
                        System.out.println("cf_Nitrogen: " + cropfertilizerNitrogen+ " " + cropfertilizerPhosphorous + " " + cropfertilizerPotash);
                    }
                }
                else {
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.plantAgeDetails))
                            .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, FertilizerCalculation.class);
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

    public void caltotalarea(){
        remainingGunta = etGunta.getText().toString().trim();
        guntaTemp = Float.parseFloat(remainingGunta);
        //   int TotalArea = (int) (Integer.parseInt(et_acreF.getText().toString().trim()) + (Float.parseFloat(et_guntaF.getText().toString().trim()) / 40.0));
        totalArea = Integer.parseInt(etAcre.getText().toString().trim()) + (guntaTemp / 40.0);
        requiredNitrogen = (int) Math.round(Integer.parseInt(cropfertilizerNitrogen) * totalArea);
        requiredPhosphorous = (int) Math.round(Integer.parseInt(cropfertilizerPhosphorous) * totalArea);
        requiredPotash = (int) Math.round(Integer.parseInt(cropfertilizerPotash) * totalArea);
        tvNPKValue.setText("Nitrogen: "+requiredNitrogen+ ", Phosphorous: " + requiredPhosphorous + ", Potash: " + requiredPotash);
        lyRecommendation.setVisibility(View.VISIBLE);
    }

    public void calculation(){
        if(finalDataObtained==0) {
            arrayFertilizerDataPhosphorous.clear();

            for (int q = 0; q < arrayCount.size(); q++) {
                if (!fertilizermasterNitrogen.equals("") && !fertilizermasterPhosphorous.equals("") && !fertilizermasterPotash.equals("")) {

                    String temp = arrayFertilizerMasterData.get(q);

                    tempfmdata = temp.split("-");
                    fmNitrogen = Integer.parseInt(tempfmdata[0]);
                    fmPhosphorous = Integer.parseInt(tempfmdata[1]);
                    fmPotash = Integer.parseInt(tempfmdata[2]);


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
                        finalDataObtained = phosphorous;
                        arrayFertilizerDataPhosphorous.add(String.valueOf(finalDataObtained));
                    }
                    if (nitrogen != 0) {
                        finalDataObtained = nitrogen;
                        arrayFertilizerDataPhosphorous.add(String.valueOf(finalDataObtained));
                    }
                    if (potash != 0) {
                        finalDataObtained = potash;
                        arrayFertilizerDataPhosphorous.add(String.valueOf(finalDataObtained));
                    }
                }
            }
        }
        if(requiredNitrogen == 0 && requiredPhosphorous == 0 && requiredPotash == 0) {

            displayFertilizers();


        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(FertilizerCalculation.this).create();
            alertDialog.setTitle(R.string.Note);
            alertDialog.setMessage(getString(R.string.alertmessage));
            alertDialog.setButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        }
    }

    private void displayFertilizers() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.recommendedFertilizers);
        builder.setCancelable(false);
        Context dialogContext = builder.getContext();
        LayoutInflater inflater = LayoutInflater.from(dialogContext);
        View alertView = inflater.inflate(R.layout.layout_fertilizer_display, null);

        builder.setView(alertView);
        TableLayout tableLayout = (TableLayout)alertView.findViewById(R.id.table_chkfertilizer);

        TableRow tableRow = new TableRow(dialogContext);
        tableRow.setLayoutParams(new LinearLayout.LayoutParams  (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView textView1 = new TextView(dialogContext);
        textView1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textView1.setGravity(Gravity.CENTER);
        textView1.setBackgroundColor(Color.parseColor("#42be7b"));
        textView1.setText(R.string.fertilizer_name);
        textView1.setTextColor(Color.BLACK);
        textView1.setTextSize(20);

        TextView textView2 = new TextView(dialogContext);
        textView2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textView2.setGravity(Gravity.CENTER);
        textView2.setBackgroundColor(Color.parseColor("#81D4FA"));
        textView2.setText(R.string.KG);
        textView2.setTextColor(Color.BLACK);
        textView2.setTextSize(20);

        TextView textView3 = new TextView(dialogContext);
        textView3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textView3.setGravity(Gravity.CENTER);
        textView3.setBackgroundColor(Color.parseColor("#9FA8DA"));
        textView3.setText(R.string.Bags);
        textView3.setTextColor(Color.BLACK);
        textView3.setTextSize(20);
        tableRow.addView(textView1);
        tableRow.addView(textView2);
        tableRow.addView(textView3);
        tableLayout.addView(tableRow);

        npk = new LinkedHashSet<String>(arrayFertilizerDataPhosphorous);

        arrayFertilizerDataPhosphorous.clear();
        arrayFertilizerDataPhosphorous.addAll(npk);

        for (int j = 0; j < arrayFertilizerDataPhosphorous.size(); j++) {
            try{
                double value = Double.valueOf(String.valueOf(arrayFertilizerDataPhosphorous.get(j)));
                double bagValue = Math.round(value/50);
                if(value<50){
                    bagValue=1;
                }
                System.out.println("arrayFertilizerDetailsList.get(j) " +arrayFertilizerDetailsList.get(j));
                System.out.println("arrayFertilizerDataPhosphorous.get(j) " +arrayFertilizerDataPhosphorous.get(j));

                TableRow tbrow_content = new TableRow(dialogContext);
                tbrow_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TextView tv_fname_fer = new TextView(dialogContext);
                tv_fname_fer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv_fname_fer.setBackgroundColor(Color.parseColor("#a8dba8"));
                tv_fname_fer.setTextSize(20);
                tv_fname_fer.setTextColor(Color.BLACK);
                tv_fname_fer.setGravity(Gravity.CENTER);
                tv_fname_fer.setText(arrayFertilizerDetailsList.get(j));

                TextView tv_kg_fer = new TextView(dialogContext);
                tv_kg_fer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv_kg_fer.setBackgroundColor(Color.parseColor("#E1F5FE"));
                tv_kg_fer.setTextSize(20);
                tv_kg_fer.setTextColor(Color.BLACK);
                tv_kg_fer.setGravity(Gravity.CENTER);
                tv_kg_fer.setText(arrayFertilizerDataPhosphorous.get(j));

                TextView tv_bags_fer = new TextView(dialogContext);
                tv_bags_fer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv_bags_fer.setBackgroundColor(Color.parseColor("#E8EAF6"));
                tv_bags_fer.setTextSize(20);
                tv_bags_fer.setTextColor(Color.BLACK);
                tv_bags_fer.setGravity(Gravity.CENTER);
                tv_bags_fer.setText("" + (int)bagValue);

                tbrow_content.addView(tv_fname_fer);
                tbrow_content.addView(tv_kg_fer);
                tbrow_content.addView(tv_bags_fer);
                tableLayout.addView(tbrow_content);
            }catch(IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }



        builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void checkNutrient(String cropcode, String irrigation, String plantage) {
        System.out.print("cropcode : " + cropcode + " irrigation : " + irrigation + " plantage : " + plantage);
        if(plantage.matches("[0-9+]+")){
            String result = plantage.replaceAll("[+]","");
            System.out.println("result " + result);
            plantage=result;
        }
        MainViewModel viewModelPlantAge = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModelPlantAge.getNutrients(cropcode,irrigation,plantage).observe(this, new Observer<List<ModelCropFertilizerMasternpk>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropFertilizerMasternpk> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()) {
                    for (ModelCropFertilizerMasternpk taskEntry : taskEntries) {

                        tvNutrientValue.setText("Nitrogen: "+taskEntry.getCf_nitrogen()+", Phosphorous: " + taskEntry.getCf_phosphorous() + ", Potash: " +taskEntry.getCf_potash());
                    }

                }

            }
        });
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