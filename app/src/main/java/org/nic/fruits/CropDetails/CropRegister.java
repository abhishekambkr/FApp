/**/package org.nic.fruits.CropDetails;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;

import org.nic.fruits.R;
import android.app.DatePickerDialog;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.nic.fruits.CropSurveyCardAdapter;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;
import org.nic.fruits.pojo.ModelCropMaster;
import org.nic.fruits.pojo.ModelCropRegistration;
import org.nic.fruits.pojo.ModelCropSurveyDetails;
import org.nic.fruits.pojo.ModelInterCropRegistration;
import org.nic.fruits.pojo.ModelMixedCropRegistration;
import org.nic.fruits.pojo.ModelOwnerDetails;
import org.nic.fruits.viewmodel.MainViewModel;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


public class CropRegister extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Context mContext;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String DEFAULT_NOTIFICATION_CHANNEL_ID = "default" ;
    private static final int CAMERA_REQUEST = 1; //1888
    private static final int CAMERA_PERMISSION = 100;
    private CropSurveyCardAdapter adapter;
    private String farmerID;
    private String keyValue;
    private AppDatabase appDatabase;

    private String survey_number;
    private String surveynumberfrommap;
    DatePickerDialog picker;
    TextView tvFarmerID,tvYear,tvSowingDetails,tvOwnerAreaValue,tvSowingDate,tvPreSowingDetail,tvCropImg;
    TextView tvLatitude,tvLongitude;
    EditText etAcre,etGunta,etFGunta,et_cents,et_ares;
    Spinner spinnerSurveyNum,spinnerOwnerNames,spinnerSeason,spinnerCropName,spinnerCropVariety,spinnerSowingDetails,spinnerFertilizer,spinnerManure;
    RadioGroup rgCropDetails;
    RadioButton rbSingleCrop,rbMixedCrop,rbInterCrop;
    RadioGroup rgFarming;
    RadioButton rbOrganic,rbInOrganic,rbNatural;
    //  RecyclerView rv_surveynum;
    List<String> arrayYearList;
    List<String> arraySeasonList;
    List<String> arraySurveyList;
    List<String> arrayOwnerNames;
    List<String> arrayCropVariety;
    List<String> arrayIrrigationType;
    List<String> arrayIrrigationSource;
    List<String> arraySowingDetails;
    List<String> arrayFertilizer;
    List<String> arrayManure;
    Button btnCaptureCropPhoto,btnMap,btnRegister;
    ImageView ivCaptureCropPhoto;
    String[] filterSurveyNumber = new String[0];
    String district = "";
    String taluk = "";
    String village = "";
    private String surveySpinnerValue="",ownernameSpinnerValue="",seasonSpinnerValue="",presowingValue="",fertilizerValue="",manureValue="",cropnameValue="",cropVarietyValue="", farmingvalue= "";
    private String cropType = "";
    private String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    private String yearCode="";

    LinearLayout layoutSingleCrop;
    LinearLayout layoutMixedCrop;
    LinearLayout layoutInterCrop;
    LinearLayout layoutIrrigationSource;
    File appMediaFolderImagesEnc;
    public static Uri imageUri;
    File imageFile;
    private String area = "";
    private String ownerID = "";

    private String cropregistrationID;
   // final int random = new Random().nextInt(26) + 75;
    private String seasonCodeValue;

    private String currentFinancialYear;
    private String acreValue,guntaValue,fguntaValue;
    private int cropDurationMonths;
    private int cropDurationWeeks;
    String [] splitArrayAcre = new String[0];
    private String saved_SurveyNumber;
    List<String> array_savedsurvey;
    boolean isAllFieldsChecked = false;

    byte[] results;
    public static final int PICTURE_RESULT = 1;
    String imagePath;
    boolean isImageTaken = false;

    private Location latLongLocation;
    double latitude = 0.00;
    double longitude = 0.00;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x2;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    String imageName = "";
    private FusedLocationProviderClient mFusedLocationClient;

    LocationRequest locationRequest = LocationRequest.create();

    Boolean singleCrop = false;
    Boolean mixedCrop = false;
    Boolean interCrop = false;

    //mixed crop-details intiliazation
    List<String> arrayMCropName;
    List<String> arrayMixedCropOptions;
    Spinner spinnerMixedCropSelection;

    private int mixedCropCounts = 0;
    TableLayout tblCropPick;

    private String mcropnameValue="",mcropVarietyValue="", mcropextent = "0",mcropCode="";
    List<String>mSelectedCrop;
    List<String>mSelectedCropName;
    List<String>mSelectedCropVariety;
    List<String>mSelectedCropExtent;
    List<String>mSelectedCropCode;
    ArrayAdapter<String> mCropAdapter;
    TextView mTotalCropExtent;
    String [] splitAcreMCrop = new String[0];
    private int mCropAcre,mCropGunta,mCropFGunta,mCropAres;
    LinearLayout lytTotalMCropExtent;
    RelativeLayout lyRelativeMCrop;

    String acreMixed = "";
    //intercrop
    List<String> arrayInterCropName;
    List<String> arrayInterCropOptions;
    Spinner spinnerInterCropSelection;
    private int interCropCounts = 0;
    TableLayout tblInterCropPick;
    private String intercropnameValue="",intercropVarietyValue="", intercropextent = "0",intercropCode="";
    List<String>interSelectedCrop;
    List<String>interSelectedCropName;
    List<String>interSelectedCropVariety;
    List<String>interSelectedCropExtent;
    List<String>interSelectedCropCode;
    ArrayAdapter<String> interCropAdapter;
    TextView interTotalCropExtent;
    private int interCropAcre,interCropGunta,interCropFGunta;
    LinearLayout lytTotalInterCropExtent;
    RelativeLayout lyRelativeInterCrop;
    String acreInter = "";

    List<String> arrayAllCropsEng;
    List<String> array_allcrops_kn;
    private String cropCode = "";
    private String mixed_cropcode = "";
    private String inter_cropcode = "";

    private String totalCrops = "",mixedTotalCrops="",interTotalCrops="";

    String[] finalExtentValuesMixedCrop = {"0", "0", "0.0"};
    String[] finalExtentValuesInterCrop = {"0", "0", "0.0"};

    Spinner spinnerIrrigationType;
    Spinner spinnerIrrigationSource;
    private String irrigationTypeId="";
    private String irrigationType="";
    private String irrigationSourceId="";
    private String irrigationSource="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_register);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        getSupportActionBar().setTitle(getResources().getString(R.string.cropregistration));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mContext = this;
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        tvFarmerID = (TextView) findViewById(R.id.tv_farmer_id);
        tvOwnerAreaValue = (TextView) findViewById(R.id.textViewOwnerareavalue);
        tvYear = (TextView) findViewById(R.id.tv_Year);
        tvCropImg = (TextView) findViewById(R.id.tv_crop_image);
        tvLatitude = (TextView) findViewById(R.id.tv_latitude);
        tvLongitude = (TextView) findViewById(R.id.tv_longitude);
        spinnerSeason = (Spinner) findViewById(R.id.spinnerSeason);
        spinnerSurveyNum = (Spinner) findViewById(R.id.spinnerChangeSurveyNumber);
        spinnerOwnerNames = (Spinner) findViewById(R.id.spinnerOwnerName);
        spinnerCropName = (Spinner) findViewById(R.id.spinnerCrop);
        spinnerCropVariety = (Spinner) findViewById(R.id.spinnercropvariety);
        spinnerIrrigationType = (Spinner) findViewById(R.id.spinnerirrigationtype);
        spinnerIrrigationSource = (Spinner) findViewById(R.id.spinnerirrigationsource);
        spinnerSowingDetails = (Spinner) findViewById(R.id.spinnerpresowing);
        spinnerFertilizer=(Spinner) findViewById(R.id.spinnerfertilizer);
        spinnerManure=(Spinner) findViewById(R.id.spinnermanure);
        rgFarming = (RadioGroup) findViewById(R.id.radioGroupQuestionFarming);
        rgCropDetails = (RadioGroup) findViewById(R.id.radioGroupCropQuestion);
        rbSingleCrop = (RadioButton) findViewById(R.id.radioSingleCrop);
        rbMixedCrop = (RadioButton) findViewById(R.id.radioMixedCrop);
        rbInterCrop = (RadioButton) findViewById(R.id.radiointercrop);
        rbOrganic= (RadioButton) findViewById(R.id.radioOrganic);
        rbInOrganic= (RadioButton) findViewById(R.id.radioInOrganic);
        rbNatural= (RadioButton) findViewById(R.id.radioNatural);
        tvSowingDetails=(TextView) findViewById(R.id.tv_Required_crop_grown);
        /*rv_surveynum = (RecyclerView)findViewById(R.id.rv_surveynumber);*/
        tvSowingDate=(TextView) findViewById(R.id.txtv_sowingdate);
        tvPreSowingDetail=(TextView)findViewById(R.id.tv_Required_crop_grown);
        etAcre=(EditText)findViewById(R.id.et_acre);
        etGunta=(EditText) findViewById(R.id.et_gunta);
        etFGunta=(EditText) findViewById(R.id.et_fgunta);
/*        etCents=(EditText) findViewById(R.id.et_cents);
        et_ares=(EditText) findViewById(R.id.et_ares);*/
        btnCaptureCropPhoto=(Button) findViewById(R.id.btn_crop_Photo_Capture);
        btnRegister = (Button) findViewById(R.id.btn_Register_Crop);
        ivCaptureCropPhoto=(ImageView) findViewById(R.id.iv_capture_image);
        btnMap = (Button) findViewById(R.id.btn_survey_map);
        layoutSingleCrop =(LinearLayout) findViewById(R.id.layoutsinglecrop);
        layoutMixedCrop=(LinearLayout) findViewById(R.id.layoutmixedcrop);
        layoutInterCrop=(LinearLayout) findViewById(R.id.layoutintercrop);
        layoutIrrigationSource = (LinearLayout) findViewById(R.id.layout_irrigationsource);
        tblCropPick = (TableLayout) findViewById(R.id.table_mixed_main);
        spinnerMixedCropSelection = (Spinner) findViewById(R.id.spinnermixedcropoptions);
        mTotalCropExtent = (TextView) findViewById(R.id.tv_total_mcrop_extent);
        lytTotalMCropExtent = (LinearLayout) findViewById(R.id.layouttotalmcropextent);
        lyRelativeMCrop = (RelativeLayout) findViewById(R.id.RelativeLayoutMixedCrops);

        spinnerInterCropSelection = (Spinner) findViewById(R.id.spinnerintercropoptions);
        lytTotalInterCropExtent = (LinearLayout) findViewById(R.id.layouttotalicropextent);
        tblInterCropPick = (TableLayout) findViewById(R.id.table_inter_main);
        interTotalCropExtent = (TextView) findViewById(R.id.tv_total_icrop_extent);
        lyRelativeInterCrop = (RelativeLayout) findViewById(R.id.RelativeLayoutInterCrops);

        /*tableLayoutCropDetails = (TableLayout) findViewById(R.id.tableLayoutCropsDetails);
        tableCropDetails = (TableLayout) findViewById(R.id.tableCropDetails);*/
//        tableCropDetails.removeAllViews();
        lyRelativeMCrop.setVisibility(View.GONE);
        lytTotalMCropExtent.setVisibility(View.GONE);
        layoutSingleCrop.setVisibility(View.GONE);
        layoutMixedCrop.setVisibility(View.GONE);

        layoutInterCrop.setVisibility(View.GONE);
        lyRelativeInterCrop.setVisibility(View.GONE);
        lytTotalInterCropExtent.setVisibility(View.GONE);

        spinnerSowingDetails.setVisibility(View.GONE);
        spinnerFertilizer.setVisibility(View.GONE);
        spinnerManure.setVisibility(View.GONE);

        //mixed crop-details
        arrayMixedCropOptions = new ArrayList<>();
        setMixedCropViews();

        //inter crop-details
        arrayInterCropOptions = new ArrayList<>();
        setInterCropViews();

        SharedPreferences prefs = getSharedPreferences("com.example.fruites", MODE_PRIVATE);
        farmerID = prefs.getString("FarmerID", "default_value_here_if_string_is_missing");
        keyValue = prefs.getString("KeyValue", "default_value_here_if_string_is_missing");
        appDatabase = AppDatabase.getInstance(getApplicationContext());

//        String area1 = "1.28.3.4";
//        System.out.println("area " + area1);
//        String [] splitArrayAcre = area1.split("\\.");
//
//        System.out.println("acreValue " + splitArrayAcre[0] + " " + splitArrayAcre[1] + " " );

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        System.out.println("Fm : " + month);
        if (month < 3) {
            currentFinancialYear = (year - 1)+ "-" + year;

            // System.out.println("Financial Year : " + (year - 1) + "-" + year);
        }
        else {
            currentFinancialYear = year + "-" + (year + 1);
            // System.out.println("Financial Year : " + year + "-" + (year + 1));
        }

        //add logic pending
        yearCode = "119";

        tvFarmerID.setText(farmerID);
        arrayYearList = new ArrayList<>();
        arraySeasonList = new ArrayList<>();
        arraySurveyList= new ArrayList<>();
        arrayOwnerNames = new ArrayList<>();
        arrayCropVariety = new ArrayList<>();
        arraySowingDetails = new ArrayList<>();
        arrayFertilizer = new ArrayList<>();
        arrayManure = new ArrayList<>();
        arrayAllCropsEng = new ArrayList<>();
        arrayMCropName = new ArrayList<>();
        arrayInterCropName = new ArrayList<>();
        arrayIrrigationType = new ArrayList<>();
        arrayIrrigationSource = new ArrayList<>();

        arrayYearList.add(0,"Select Year");
        arraySeasonList.add(0,"Select Season");
        arraySurveyList.add(0,"Select Survey Number");

        tvPreSowingDetail.setVisibility(View.INVISIBLE);

       /* splitArrayAcre = area.split("\\.");
        acreValue = splitArrayAcre[0];
        guntaValue = splitArrayAcre[1];
        fguntaValue = splitArrayAcre[2];*/


        //if G{}
     /*   int tempvalue =  (interCropAcre*40*16) + (interCropGunta*16) + interCropFGunta;
        System.out.print("tempvalue : " + tempvalue);
        int tempresult = tempvalue / Integer.parseInt(interTotalCrops);
        System.out.print("tempresult : " + tempresult);
        interCropAcre = tempresult / (40*16);
        System.out.print("resultacre_intercropacre : " + interCropAcre);
        interCropGunta = tempresult / 16;
        System.out.print("resultgunta_intercropgunta : " + interCropGunta);
        interCropFGunta = tempresult;
        System.out.print("resultfgunta_intercropfgunta : " + interCropFGunta);*/

        //if C{}
          /*  int tempvalue_C = (mCropAcre*100*100) + (mCropGunta*100) + mCropFGunta;
            System.out.print("tempvalue_C : " + tempvalue_C);
            int tempresult_C = tempvalue_C / Integer.parseInt(mixed_total_crops);
            System.out.print("tempresult_C : " + tempresult_C);
            int resultacre_C = tempresult_C / (100*100);
            System.out.print("resultacre_C : " + resultacre_C);
            int resultgunta_C = tempresult_C / 100;
            System.out.print("resultgunta_C : " + resultgunta_C);
            int resultfgunta_C = tempresult_C;
            System.out.print("resultfgunta_C : " + resultfgunta_C);*/

        etAcre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(area!=null){
                    try {

                        if (Integer.parseInt(etAcre.getText().toString()) <= Integer.parseInt(acreValue.trim())) {

                            etGunta.setText("");
                            etFGunta.setText("");
                      /*  et_cents.setText("");
                        et_ares.setText("");*/


                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಎಕರೆ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etAcre.setText("");
                            etAcre.requestFocus();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("ಸೂಚನೆ :");
                    alertDialog.setMessage("ಸರಿಯಾದ ಸಮೀಕ್ಷೆ ಸಂಖ್ಯೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ");
                    alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            spinnerSurveyNum.requestFocus();
                        }
                    });
                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etGunta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (etGunta.getText().toString().length() > 1) {
                        if (Integer.parseInt(etGunta.getText().toString()) < 40) {
                            if (Integer.parseInt(etGunta.getText().toString()) <= Integer.parseInt(guntaValue.trim())) {

                                etFGunta.setText("");
                                    /*et_cents.setText("");
                                    et_ares.setText("");*/
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                                alertDialog.setTitle("ಸೂಚನೆ :");
                                alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                                alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        etFGunta.setText("");
                                           /* et_cents.setText("");
                                            et_ares.setText("");*/
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setCanceledOnTouchOutside(false);
                                etGunta.setText("");
                                etGunta.requestFocus();

                                etFGunta.setText("");
                                    /*et_cents.setText("");
                                    et_ares.setText("");*/
                            }

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    etFGunta.setText("");
                                        /*et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGunta.setText("");
                            etGunta.requestFocus();

                            etFGunta.setText("");
                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                        }
                    } else {
                        if (Integer.parseInt(etFGunta.getText().toString()) < 100) {

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    etFGunta.setText("");
                                       /* et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGunta.setText("");
                            etGunta.requestFocus();

                            etFGunta.setText("");
                               /* et_cents.setText("");
                                et_ares.setText("");*/
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

        etFGunta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    //   if (Integer.parseInt(et_fgunta.getText().toString()) >= 0) {

                    if (Integer.parseInt(etFGunta.getText().toString()) < 100) {
                        if (Integer.parseInt(etFGunta.getText().toString()) <= Integer.parseInt(fguntaValue.trim())){

                        }else{
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ fಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etFGunta.setText("");
                            etFGunta.requestFocus();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                        alertDialog.setTitle("ಸೂಚನೆ :");
                        alertDialog.setMessage("ಸರಿಯಾದ fಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                        alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                et_cents.setText("");
                                et_ares.setText("");
                            }
                        });
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        etFGunta.setText("");
                        etFGunta.requestFocus();

                        et_cents.setText("");
                        et_ares.setText("");
                    }
                        /*} else {
                            if (Integer.parseInt(et_fgunta.getText().toString()) < 100) {
                                if (Integer.parseInt(et_fgunta.getText().toString()) <= Integer.parseInt(fguntaValue.trim())){}
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                                alertDialog.setTitle("ಸೂಚನೆ :");
                                alertDialog.setMessage("ಸರಿಯಾದ ಅಣಾ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                                alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
                                alertDialog.show();
                                alertDialog.setCanceledOnTouchOutside(false);
                                et_fgunta.setText("");
                                et_fgunta.requestFocus();

                               *//* et_cents.setText("");
                                et_ares.setText("");*//*
                            }
                        }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rgCropDetails.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                checkedId = radioGroup.getCheckedRadioButtonId();

                switch (checkedId) {
                    case R.id.radioSingleCrop:
                        singleCrop = true;
                        mixedCrop = false;
                        interCrop = false;

                        totalCrops = "1";

                        layoutSingleCrop.setVisibility(View.VISIBLE);
                        layoutMixedCrop.setVisibility(View.GONE);
                        cropType = rbSingleCrop.getText().toString();

                        //reset mixed crop selections
                        spinnerMixedCropSelection.setSelection(0);
                        tblCropPick.setVisibility(View.INVISIBLE);
                        tblCropPick.removeAllViews();
                        lyRelativeMCrop.setVisibility(View.GONE);
                        lytTotalMCropExtent.setVisibility(View.GONE);

                        //reset inter crop selections
                        spinnerInterCropSelection.setSelection(0);
                        tblInterCropPick.setVisibility(View.INVISIBLE);
                        tblInterCropPick.removeAllViews();
                        layoutInterCrop.setVisibility(View.GONE);
                        lyRelativeInterCrop.setVisibility(View.GONE);
                        lytTotalInterCropExtent.setVisibility(View.GONE);

                        break;

                    case R.id.radioMixedCrop:
                        mixedCrop = true;
                        singleCrop = false;
                        interCrop = false;

                        layoutMixedCrop.setVisibility(View.VISIBLE);
                        layoutSingleCrop.setVisibility(View.GONE);
                        cropType = rbMixedCrop.getText().toString();

                        /*tblCropPick.setVisibility(View.VISIBLE);

                        tblCropPick.removeAllViews();
                        lyRelativeMCrop.setVisibility(View.VISIBLE);
                        lytTotalMCropExtent.setVisibility(View.VISIBLE);*/

                        //reset single crop selections
                        spinnerCropName.setSelection(0);
                        spinnerCropVariety.setSelection(0);
                        etAcre.setText("");
                        etGunta.setText("");
                        etFGunta.setText("");

                        //reset inter crop selections
                        spinnerInterCropSelection.setSelection(0);
                        tblInterCropPick.setVisibility(View.INVISIBLE);
                        tblInterCropPick.removeAllViews();
                        layoutInterCrop.setVisibility(View.GONE);
                        lyRelativeInterCrop.setVisibility(View.GONE);
                        lytTotalInterCropExtent.setVisibility(View.GONE);

                        break;

                    case R.id.radiointercrop:
                        interCrop = true;
                        mixedCrop = false;
                        singleCrop = false;

                        layoutInterCrop.setVisibility(View.VISIBLE);
                        layoutSingleCrop.setVisibility(View.GONE);
                        layoutMixedCrop.setVisibility(View.GONE);
                        cropType = rbInterCrop.getText().toString();

                        //reset single crop selections
                        spinnerCropName.setSelection(0);
                        spinnerCropVariety.setSelection(0);
                        etAcre.setText("");
                        etGunta.setText("");
                        etFGunta.setText("");

                        //reset mixed crop selections
                        spinnerMixedCropSelection.setSelection(0);
                        tblCropPick.setVisibility(View.INVISIBLE);
                        tblCropPick.removeAllViews();
                        lyRelativeMCrop.setVisibility(View.GONE);
                        lytTotalMCropExtent.setVisibility(View.GONE);
                }

                if(checkedId==0){

                }

            }
        });

        rgFarming.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                checkedId = radioGroup.getCheckedRadioButtonId();

                switch (checkedId) {
                    case R.id.radioOrganic:
                        spinnerSowingDetails.setVisibility(View.VISIBLE);
                        spinnerFertilizer.setVisibility(View.GONE);
                        spinnerManure.setVisibility(View.GONE);
                        farmingvalue = rbOrganic.getText().toString();
                        tvSowingDetails.setText("Any Pre-Sowing/Transplanting Organic Manure used?");
                        tvPreSowingDetail.setVisibility(View.VISIBLE);
                        //    idx = radioGroup.indexOfChild(rbOrganic);
                        break;
                    case R.id.radioInOrganic:
                        spinnerFertilizer.setVisibility(View.VISIBLE);
                        spinnerSowingDetails.setVisibility(View.GONE);
                        spinnerManure.setVisibility(View.GONE);
                        farmingvalue = rbInOrganic.getText().toString();
                        tvSowingDetails.setText("Any Pre-Sowing/Transplanting Fertilizers/Chemicals used?");
                        tvPreSowingDetail.setVisibility(View.VISIBLE);
                        //   idx = radioGroup.indexOfChild(rbInOrganic);
                        break;
                    case R.id.radioNatural:
                        spinnerManure.setVisibility(View.VISIBLE);
                        spinnerSowingDetails.setVisibility(View.GONE);
                        spinnerFertilizer.setVisibility(View.GONE);
                        farmingvalue = rbNatural.getText().toString();
                        tvSowingDetails.setText("Any Pre-Sowing/Transplanting Natural Manure used?");
                        tvPreSowingDetail.setVisibility(View.VISIBLE);
                        //   idx = radioGroup.indexOfChild(rbNatural);
                        break;
                }



             /*   if (rbOrganic.isChecked()) {
                } else if (rbInOrganic.isChecked()) {
                }else if (rbNatural.isChecked()) {
                }*/
            }
        });

        tvSowingDate.setInputType(InputType.TYPE_NULL);

        tvSowingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvSowingDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(System.currentTimeMillis());
                picker.show();
            }
        });

        btnCaptureCropPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera();
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map();
            }
        });

        String root = Environment.getExternalStorageDirectory().toString();

        appMediaFolderImagesEnc = new File(root + "/Fruits/" + "118" + "/" + 1 + "/" + farmerID +"/Image");

        if (!appMediaFolderImagesEnc.exists()) {
            appMediaFolderImagesEnc.mkdirs();
        }

        setupViewModel();

        cropDurationMonths = 5;
        Calendar cal = Calendar.getInstance();
        for(int i = 0 ; i < 11;i++){
            cal.set(Calendar.YEAR, Integer.parseInt(currentYear));
            cal.set(Calendar.DAY_OF_MONTH, cropDurationMonths);
            cal.set(Calendar.MONTH, i);

        }

        cropDurationWeeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH) * cropDurationMonths;
        Log.d("LOG","max week number" + cropDurationWeeks);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* System.out.println("currentFinancialYear " + currentFinancialYear );
                System.out.println("seasonSpinnerValue " + seasonSpinnerValue );
                System.out.println("surveySpinnerValue " + surveySpinnerValue );
                System.out.println("ownernameSpinnerValue " + ownernameSpinnerValue );
                System.out.println("cropnameValue " + cropnameValue );
                System.out.println("et_gunta.getText().toString() " + et_gunta.getText().toString() );
                System.out.println("et_anna.getText().toString() " + et_anna.getText().toString() );
                System.out.println("et_cents.getText().toString() " + et_cents.getText().toString() );
                System.out.println("et_ares.getText().toString() " + et_ares.getText().toString() );
                System.out.println("et_ares.getText().toString() " + et_ares.getText().toString() );
                System.out.println("et_sowingdate.getText().toString() " + tv_sowingdate.getText().toString() );*/
               
                //  if (seasonSpinnerValue != null && surveySpinnerValue != null && ownernameSpinnerValue != null && cropnameValue != null && cropVarietyValue != null && et_acre.getText().toString() != null && et_gunta.getText().toString() != null && et_cents.getText().toString() != null && et_ares.getText().toString() != null && tv_sowingdate.getText().toString() != null && farmingvalue != null) {
               if(!(latitude == 0.00 && longitude == 0.00)) {
                   if (fieldvalidations()) {
                       Log.d("LOG", "SaveData");
                       new SaveData().execute();
                   } else {
                       Log.d("LOG", "Incomplete crop registration form");
                       Toast.makeText(mContext,"Incomplete crop registration form",Toast.LENGTH_SHORT).show();

                   }
               }else{
                   checkPermissions();
               }
            }
        });

        setUpGClient();
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private boolean fieldvalidations() {

       if (seasonSpinnerValue.equals("")) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Select a valid Season");
            alertDialog .setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {} });
            alertDialog.show();
            return false;

        }
        if (surveySpinnerValue.equals("")) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Select a valid Survey Number");
            alertDialog .setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
            return false;
        }

        if(rgCropDetails.getCheckedRadioButtonId() == -1){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Choose type of Crops");
            alertDialog .setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            return false;

        }

        if(cropType.equals("Single Crop")) {
            if (cropnameValue.equals("")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Select a valid Single Crop");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                return false;
            }
            if (cropVarietyValue.equals("")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Select a valid Crop Variety");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                return false;
            }
            if (etAcre.getText().toString().equals("")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Enter a valid Acre");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        etAcre.setError(Html.fromHtml("<font color='red'>Enter Acre</font>"));
                        etAcre.setFocusable(true);
                    }
                });
                alertDialog.show();
                return false;
            }
            if (etGunta.getText().toString().equals("")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Enter a valid Gunta");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        etGunta.setError(Html.fromHtml("<font color='red'>Enter Gunta</font>"));
                        etGunta.setFocusable(true);
                    }
                });
                alertDialog.show();
                return false;
            }
            if (etFGunta.getText().toString().equals("")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Enter a valid FGunta");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        etFGunta.setError(Html.fromHtml("<font color='red'>Enter Anna</font>"));
                        etFGunta.setFocusable(true);
                    }
                });
                alertDialog.show();
                return false;
            }
        }
        else if(cropType.equals("Mixed Crop")){
            if(mixedTotalCrops.equals("")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Select valid number of Mixed Crops");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                return false;
            }
        }
        else if(cropType.equals("Inter Crop")){
            if(interTotalCrops.equals("")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Select valid number of Inter Crops");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                });
                alertDialog.show();
                return false;
            }
        }

        if (spinnerIrrigationType.getSelectedItem().toString().equals("Select Irrigation Type")) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Select Irrigation Type");
            alertDialog .setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
            return false;
        }
        if(spinnerIrrigationType.getSelectedItem().toString().equals("Irrigated")){
            if (spinnerIrrigationSource.getSelectedItem().toString().equals("Select Irrigation Source")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Select Irrigation Source");
                alertDialog .setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                return false;
            }
        }
        if(rgFarming.getCheckedRadioButtonId() == -1){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Choose type of Farming");
            alertDialog .setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
            return false;

        }

        if(farmingvalue.equals("Organic")){
            spinnerSowingDetails.setSelection(0);
         /*   if(spinnerSowingDetails.getSelectedItem().toString().equals("Select Organic Manure")){

            }*/
        }
        else if(farmingvalue.equals("Inorganic")){
            spinnerFertilizer.setSelection(0);
            /*if(spinnerFertilizer.getSelectedItem().toString().equals("Select Inorganic Manure")){

            }*/
        }else if(farmingvalue.equals("Natural")){
            spinnerManure.setSelection(0);
            /*if(spinnerManure.getSelectedItem().toString().equals("Select Natural Manure")){

            }*/
        }

        if (tvSowingDate.getText().toString().equals("") || tvSowingDate.getText().toString().equals("Click here")) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Choose Sowing Date");
            alertDialog .setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    tvSowingDate.setFocusable(true);
                }
            });
            alertDialog.show();
            return false;
        }
        if(tvCropImg.getText().toString().equals("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ")){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Crop image not captured");
            alertDialog .setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latLongLocation = location;

        if(location != null){
            latitude = latLongLocation.getLatitude();
            longitude = latLongLocation.getLongitude();

            tvLatitude.setText("Latitude : " +latitude);
            tvLongitude.setText("Longitude : " +longitude);

            if(latLongLocation.hasAccuracy()){
                //  accuracy = location.getAccuracy();
                //  tvCoordinates.setText("GPS Accuracy : " + accuracy + " m");
            }
            System.out.println("Latitude | Longitude in  onLocationChanged  " +latitude +" " +longitude);
        }
    }

    private class SaveData extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(CropRegister.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait.. Saving crop details");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String savedata;
            savedata = parsecropdata();
            return savedata;
        }

        @Override
        protected void onPostExecute(String savedata) {

            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (savedata.equals("success")) {
                Toast.makeText(mContext, "Your crop has been registered", Toast.LENGTH_LONG).show();
                Intent cropRegisterIntent = new Intent(mContext,CropDetails.class);
                cropRegisterIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(cropRegisterIntent);
                finish();

            } else if (savedata.equals("exists")){
                new AlertDialog.Builder(mContext)
                        .setTitle(getResources().getString(R.string.alert))
                        .setMessage(getResources().getString(R.string.surveyalreadysaved))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        })
                        .show();
            }
            else {
                Toast.makeText(mContext, "Problem While saving crop Details", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String parsecropdata(){

        String survey = surveySpinnerValue.replaceAll("[/]","");
        String crop_extent =etAcre.getText().toString().trim()+"."+ etGunta.getText().toString().trim()+"."+ etFGunta.getText().toString().trim();

        //pending
        //cropcode + yearCode + seasoncode  + crop extent + survey no.
        cropregistrationID = cropCode+yearCode+seasonCodeValue+area+survey;

        String crop_reg_Id = cropregistrationID.replaceAll("[.*#+-/%]","");
        System.out.println("crop_reg_id " + crop_reg_Id);

        //pnding
        String live_gps = latitude+","+longitude;


        if(cropType.equals("Single Crop")) {

            if (AppDatabase.getInstance(this).cropRegistrationDao().isDataExist(farmerID,surveySpinnerValue,currentFinancialYear,seasonSpinnerValue,ownerID) == 0) {
                // data not exist.
                final ModelCropRegistration singlecrop = new ModelCropRegistration(farmerID,crop_reg_Id,currentFinancialYear,yearCode,seasonSpinnerValue,seasonCodeValue,ownerID,ownernameSpinnerValue,area,surveySpinnerValue,district,taluk,village,cropnameValue,cropType,totalCrops,cropCode,cropVarietyValue,area,irrigationTypeId,irrigationType,irrigationSourceId,irrigationSource,farmingvalue,presowingValue,tvSowingDate.getText().toString(),live_gps,imagePath,imageName,"N");

                AppExecutors.getInstance().diskIO().execute(new Runnable() {

                    @Override
                    public void run() {

                        if (singlecrop != null) {
                            appDatabase.cropRegistrationDao().insertCropRegistrationDetails(singlecrop);
                        }
                    }

                });
                singlecropNotification(crop_reg_Id);
                return "success";
            } else {
                // data already exist.
                return "exists";
            }

        }
        else if(cropType.equals("Mixed Crop")){
            if (AppDatabase.getInstance(this).cropRegistrationDao().isDataExist(farmerID,surveySpinnerValue,currentFinancialYear,seasonSpinnerValue,ownerID) == 0) {
                final ModelCropRegistration mixedcrop = new ModelCropRegistration(farmerID,crop_reg_Id,currentFinancialYear,yearCode,seasonSpinnerValue,seasonCodeValue,ownerID,ownernameSpinnerValue,area,surveySpinnerValue,district,taluk,village,mSelectedCropName.toString(),cropType,mixedTotalCrops,mSelectedCropCode.toString(),mSelectedCropVariety.toString(),mSelectedCropExtent.toString(),irrigationTypeId,irrigationType,irrigationSourceId,irrigationSource,farmingvalue,presowingValue,tvSowingDate.getText().toString(),live_gps,imagePath,imageName,"N");
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mixedcrop != null) {
                            appDatabase.cropRegistrationDao().insertCropRegistrationDetails(mixedcrop);
                        }
                    }
                });
                System.out.print("mixed_total_crops: " + mixedTotalCrops.length());

                for (int i = 0; i < Integer.parseInt(mixedTotalCrops); i++) {
                    final ModelMixedCropRegistration mixedCropRegistration = new ModelMixedCropRegistration(farmerID, crop_reg_Id, currentFinancialYear, yearCode, seasonSpinnerValue, seasonCodeValue, ownerID, ownernameSpinnerValue, area, surveySpinnerValue,district,taluk,village, cropType, mSelectedCropName.get(i), mSelectedCropCode.get(i), mSelectedCrop.get(i), mSelectedCropVariety.get(i), mixedTotalCrops, finalExtentValuesMixedCrop[0] + "." + finalExtentValuesMixedCrop[1] + "." + finalExtentValuesMixedCrop[2], area, farmingvalue, presowingValue, tvSowingDate.getText().toString(), live_gps, imagePath, imageName, "N");
                    appDatabase.mixedCropRegistrationDao().insertMixedCropRegistrationDetails(mixedCropRegistration);

                }
                for (int i = 0; i < Integer.parseInt(mixedTotalCrops); i++) {
                    mixedcropNotification(mSelectedCropName.get(i),crop_reg_Id);
                }

                return "success";
            } else {
                // data already exist.

                return "exists";
            }

        }
        else if(cropType.equals("Inter Crop")) {
            if (AppDatabase.getInstance(this).cropRegistrationDao().isDataExist(farmerID,surveySpinnerValue,currentFinancialYear,seasonSpinnerValue,ownerID) == 0) {
                final ModelCropRegistration intercrop = new ModelCropRegistration(farmerID, crop_reg_Id, currentFinancialYear, yearCode, seasonSpinnerValue, seasonCodeValue, ownerID, ownernameSpinnerValue, area, surveySpinnerValue,district,taluk,village,interSelectedCropName.toString(), cropType, interTotalCrops, interSelectedCropCode.toString(), interSelectedCropVariety.toString(), interSelectedCropExtent.toString(),irrigationTypeId,irrigationType,irrigationSourceId,irrigationSource, farmingvalue, presowingValue, tvSowingDate.getText().toString(), live_gps, imagePath, imageName, "N"); //totalcropextent missing

                AppExecutors.getInstance().diskIO().execute(new Runnable() {

                    @Override
                    public void run() {

                        if (intercrop != null) {
                            appDatabase.cropRegistrationDao().insertCropRegistrationDetails(intercrop);
                        }
                    }
                });

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < Integer.parseInt(interTotalCrops); i++) {
                            final ModelInterCropRegistration interCropRegistration = new ModelInterCropRegistration(farmerID, crop_reg_Id, currentFinancialYear, yearCode, seasonSpinnerValue, seasonCodeValue, ownerID, ownernameSpinnerValue, area, surveySpinnerValue,district,taluk,village, cropType, interSelectedCropName.get(i), interSelectedCrop.get(i), interSelectedCropCode.get(i), interTotalCrops, interSelectedCropVariety.get(i), finalExtentValuesInterCrop[0] + "." + finalExtentValuesInterCrop[1] + "." + finalExtentValuesInterCrop[2], interSelectedCropExtent.toString(), farmingvalue, presowingValue, tvSowingDate.getText().toString(), live_gps, imagePath, imageName, "N");
                            if (interCropRegistration != null) {
                                appDatabase.interCropRegistrationDao().insertinterCropRegistrationDetails(interCropRegistration);
                                intercropNotification(interSelectedCropName.get(i),crop_reg_Id);
                            }
                        }
                    }
                 });

                return "success";
            } else {
                // data already exist.

                return "exists";
            }

        }
        else {
            return "nocroptype";
        }
    }

    private void map() {
        /// getMapdata()
       /* if (selectedLGV != null && selectedVillageName != null && selectedCropName != null && selectedExpID != null && selectedENGVN != null)
        {*/
        Intent intent = new Intent(CropRegister.this, com.ksrsac.hasiru.MainActivity_New.class);
        intent.putExtra("surveyno", "98");
        intent.putExtra("package_name", "org.nic.fruits");
        intent.putExtra("class_name", "CropRegister");
        intent.putExtra("LG_Village", "625133");
        startActivity(intent);
     /*   }
        else
        {
            Toast.makeText(this, "Lg village data not found.",Toast.LENGTH_SHORT).show();
        }*/
    }

    private void camera() {

        try{

            String root = Environment.getExternalStorageDirectory().toString();

            appMediaFolderImagesEnc = new File(root + "/Fruits/" + yearCode + "/" + seasonCodeValue + "/" + farmerID +"/Image");

            if (!appMediaFolderImagesEnc.exists()) {
                appMediaFolderImagesEnc.mkdirs();
            }

            if(cropType.equals("Single Crop")) {
                imageName = farmerID+"_"+yearCode+"_"+seasonCodeValue+"_"+cropType+"_"+cropnameValue;
            }else if(cropType.equals("Mixed Crop")){
                imageName = farmerID+"_"+yearCode+"_"+seasonCodeValue+"_"+cropType+"_"+mSelectedCropName.toString().replaceAll("\\[|\\]", "");
            }else if(cropType.equals("Inter Crop")){
                imageName = farmerID+"_"+yearCode+"_"+seasonCodeValue+"_"+cropType+"_"+interSelectedCropName.toString().replaceAll("\\[|\\]", "");
            }

            imageFile = new File(appMediaFolderImagesEnc, imageName + ".jpg");
            imageUri = Uri.fromFile(imageFile);
            imagePath = imageFile.toString();

            // For default camera
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            PackageManager packageManager = CropRegister.this.getPackageManager();
            List<ResolveInfo> listCam = packageManager.queryIntentActivities(cameraIntent, 0);
            cameraIntent.setPackage(listCam.get(0).activityInfo.packageName);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, PICTURE_RESULT);

        }catch(NullPointerException e){
            e.printStackTrace();
        }

    }

    private void setupViewModel() {
        SpinnerSelections();
        irrigationdetails();
        //   radioSelection();

    }

  /*  public void OnRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioOrganic:
                if (checked) {
                    Toast.makeText(mContext, "Organic", Toast.LENGTH_SHORT).show();
                    tv_sowing_details.setText("Pre Sowing / Transplanting Manure used?");
                    break;
                }
            case R.id.radioInOrganic:
                if (checked) {
                    Toast.makeText(mContext, "InOrganic", Toast.LENGTH_SHORT).show();
                    tv_sowing_details.setText("Fertilizers / Chemicals used?");
                    break;
                }
            case R.id.radioNatural:
                if (checked) {
                    Toast.makeText(mContext, "InOrganic", Toast.LENGTH_SHORT).show();
                    tv_sowing_details.setText("Manure used?");
                    break;
                }
        }
    }

    private void radioSelection() {
       // radioselectedvalue

        if (rbOrganic.isChecked()){
            rbOrganic.setChecked(true);

        }else if(rbInOrganic.isChecked()){
            rbInOrganic.setChecked(true);
            Toast.makeText(mContext,"InOrganic",Toast.LENGTH_SHORT).show();
            tv_sowing_details.setText("Fertilizers / Chemicals used?");
        }else if(rbNatural.isChecked()){
            rbNatural.setChecked(true);
            Toast.makeText(mContext,"Natural",Toast.LENGTH_SHORT).show();
            tv_sowing_details.setText("Manure used?");
        }

    }*/

    private void SpinnerSelections() {
        //For Year
       /* String year="";
        if(currentYear.equals("2021")){
            year = "2020-21";
        }*/
        tvYear.setText(currentFinancialYear);
        /*ArrayAdapter<String> year_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayYearList);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayYearList.add(1,year);
        year_adapter.notifyDataSetChanged();
        spinner_year.setAdapter(year_adapter);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    yearSpinnerValue = spinner_year.getSelectedItem().toString();
                    yearCode = "118";
                }else{
                    spinner_year.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


        //For Season
        ArrayAdapter<String> season_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arraySeasonList);
        season_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arraySeasonList.add(1,"Summer");
        arraySeasonList.add(2,"Rabi");
        arraySeasonList.add(3,"Khariff");

        season_adapter.notifyDataSetChanged();
        spinnerSeason.setAdapter(season_adapter);
        spinnerSeason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    seasonSpinnerValue = spinnerSeason.getSelectedItem().toString();
                    if(spinnerSeason.getSelectedItem().toString().equals("Summer")){
                        seasonCodeValue = "1";
                    }else if(spinnerSeason.getSelectedItem().toString().equals("Rabi")){
                        seasonCodeValue = "2";
                    }else if(spinnerSeason.getSelectedItem().toString().equals("Khariff")){
                        seasonCodeValue = "3";
                    }
                    spinnerSurveyNum.setSelection(0);
                    singleCrop = false;
                    mixedCrop = false;
                    interCrop = false;
                    spinnerSowingDetails.setVisibility(View.GONE);
                    spinnerFertilizer.setVisibility(View.GONE);
                    spinnerManure.setVisibility(View.GONE);
                    farmingvalue = "";
                    etAcre.setText("");
                    etAcre.clearFocus();
                    etGunta.setText("");
                    etGunta.clearFocus();
                    etFGunta.setText("");
                    etFGunta.clearFocus();
                    rgCropDetails.clearCheck();
                    rgFarming.clearCheck();
                    spinnerIrrigationType.setSelection(0);
                    spinnerIrrigationSource.setSelection(0);
                    spinnerSowingDetails.setSelection(0);
                    spinnerFertilizer.setSelection(0);
                    spinnerManure.setSelection(0);
                    layoutSingleCrop.setVisibility(View.GONE);
                    layoutMixedCrop.setVisibility(View.GONE);
                    layoutInterCrop.setVisibility(View.GONE);
                    ivCaptureCropPhoto.setVisibility(View.GONE);
                    tvCropImg.setText("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ");
                }else{
                    spinnerSeason.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        //For Survey Number
        ArrayAdapter<String> survey_num_adapters = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arraySurveyList);
        survey_num_adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //to remove duplicate values in lists from xml


        MainViewModel viewModelforSurveyNumbers = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModelforSurveyNumbers.getCropSurveBasedOnFid(farmerID).observe(this, new Observer<List<ModelCropSurveyDetails>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropSurveyDetails> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()){
                    for(ModelCropSurveyDetails taskEntry:taskEntries){
                        if(!arraySurveyList.contains(taskEntry.getSurveyNumber())){
                            arraySurveyList.add(taskEntry.getDistrictname() + " - " + taskEntry.getTalukName() + " - " + taskEntry.getVillageName() + " - " + taskEntry.getSurveyNumber() );

                            Set<String> lstsurveyaddons = new LinkedHashSet<String>(arraySurveyList);
                            arraySurveyList.clear();
                            arraySurveyList.addAll(lstsurveyaddons);

                        }
                    }
                }else{
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.beneficiary_not_present))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, CropRegister.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            })
                            .show();
                }
                survey_num_adapters.notifyDataSetChanged();
                rgCropDetails.clearCheck();
                rgFarming.clearCheck();
                layoutSingleCrop.setVisibility(View.GONE);
                layoutMixedCrop.setVisibility(View.GONE);
                layoutInterCrop.setVisibility(View.GONE);

                spinnerSurveyNum.setAdapter(survey_num_adapters);
                spinnerSurveyNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){

                            filterSurveyNumber = spinnerSurveyNum.getSelectedItem().toString().trim().split("\\-");

                            surveySpinnerValue = filterSurveyNumber[3].trim();
                            district = filterSurveyNumber[0].trim();
                            taluk = filterSurveyNumber[1].trim();
                            village = filterSurveyNumber[2].trim();

                            etAcre.setText("");
                            etAcre.clearFocus();
                            etGunta.setText("");
                            etGunta.clearFocus();
                            etFGunta.setText("");
                            etFGunta.clearFocus();
                            singleCrop = false;
                            mixedCrop = false;
                            interCrop = false;
                            spinnerSowingDetails.setVisibility(View.GONE);
                            spinnerFertilizer.setVisibility(View.GONE);
                            spinnerManure.setVisibility(View.GONE);
                            farmingvalue = "";
                            rgCropDetails.clearCheck();
                            rgFarming.clearCheck();
                            spinnerIrrigationType.setSelection(0);
                            spinnerIrrigationSource.setSelection(0);
                            spinnerSowingDetails.setSelection(0);
                            spinnerFertilizer.setSelection(0);
                            spinnerManure.setSelection(0);
                            layoutSingleCrop.setVisibility(View.GONE);
                            layoutMixedCrop.setVisibility(View.GONE);
                            layoutInterCrop.setVisibility(View.GONE);
                            ivCaptureCropPhoto.setVisibility(View.GONE);
                            tvCropImg.setText("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ");
                            getownernames();
                        }else{
                            spinnerSurveyNum.setSelection(0);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

        });

        //Pre Sowing details
        arraySowingDetails.add(0,"Select Organic Manure");
        arraySowingDetails.add(1,"Transplanting Manure");
        arraySowingDetails.add(2,"Not used");
        ArrayAdapter<String> presowing_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arraySowingDetails);
        presowing_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        presowing_adapter.notifyDataSetChanged();
        spinnerSowingDetails.setAdapter(presowing_adapter);
        spinnerSowingDetails.setSelection(0);
        spinnerSowingDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    presowingValue = spinnerSowingDetails.getSelectedItem().toString();

                }else{
                    spinnerSowingDetails.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //fertilizer
        arrayFertilizer.add(0,"Select Fertilizer");
        arrayFertilizer.add(1,"Urea");
        arrayFertilizer.add(2,"D.P.A");
        arrayFertilizer.add(3,"O.P.A (Potash)");
        arrayFertilizer.add(4,"20:20:0:15");
        arrayFertilizer.add(5,"Not used");
        ArrayAdapter<String> fertlizer_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayFertilizer);
        fertlizer_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fertlizer_adapter.notifyDataSetChanged();
        spinnerFertilizer.setAdapter(fertlizer_adapter);
        spinnerFertilizer.setSelection(0);
        spinnerFertilizer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    fertilizerValue = spinnerFertilizer.getSelectedItem().toString();

                }else{
                    spinnerFertilizer.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        arrayManure.add(0,"Select Natural Manure");
        arrayManure.add(1,"Manure");
        arrayManure.add(2,"Not used");

        ArrayAdapter<String> manure_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayManure);
        manure_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        manure_adapter.notifyDataSetChanged();
        spinnerManure.setAdapter(manure_adapter);
        spinnerManure.setSelection(0);
        spinnerManure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    presowingValue = spinnerManure.getSelectedItem().toString();

                }else{
                    spinnerManure.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getsinglecrops();
        arrayCropVariety.add(0,"Select Crop Variety");
        arrayCropVariety.add(1,"High (Hybrid)");
        arrayCropVariety.add(2,"Normal (Farm)");
        arrayCropVariety.add(3,"Low (Local)");
        ArrayAdapter<String> cropvariety_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayCropVariety);
        cropvariety_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cropvariety_adapter.notifyDataSetChanged();
        spinnerCropVariety.setAdapter(cropvariety_adapter);
        spinnerCropVariety.setSelection(0);
        spinnerCropVariety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    cropVarietyValue = spinnerCropVariety.getSelectedItem().toString();
                }else{
                    spinnerCropVariety.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void irrigationdetails() {

        arrayIrrigationType.add(0,"Select Irrigation Type");
        arrayIrrigationType.add(1,"Irrigated");
        arrayIrrigationType.add(2,"Non Irrigated");
        ArrayAdapter<String> irrigationtype_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayIrrigationType);
        irrigationtype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        irrigationtype_adapter.notifyDataSetChanged();
        spinnerIrrigationType.setAdapter(irrigationtype_adapter);
        spinnerIrrigationType.setSelection(0);
        spinnerIrrigationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    irrigationType = spinnerIrrigationType.getSelectedItem().toString();
                    irrigationTypeId = String.valueOf(i);
                    if(spinnerIrrigationType.getSelectedItem().toString().equals("Non Irrigated")){
                        irrigationSource = "Not irrigated";
                        spinnerIrrigationSource.setSelection(0);
                        layoutIrrigationSource.setVisibility(View.GONE);

                        System.out.print("irrigation type: " + irrigationType + " : " + irrigationTypeId);
                        System.out.print("irrigation source: " + irrigationSource);

                    }else{
                        layoutIrrigationSource.setVisibility(View.VISIBLE);
                    }
                }else{
                    spinnerIrrigationType.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        System.out.print("spinner_irrigation_source: " + irrigationSourceId + " irrgationsource: " + irrigationSource);



        arrayIrrigationSource.add(0,"Select Irrigation Source");
        arrayIrrigationSource.add(1,"Channel");
        arrayIrrigationSource.add(2,"Lake");
        arrayIrrigationSource.add(3,"Well/Bore well");
        arrayIrrigationSource.add(4,"Others");

        ArrayAdapter<String> irrigationsource_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayIrrigationSource);
        irrigationsource_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        irrigationsource_adapter.notifyDataSetChanged();
        spinnerIrrigationSource.setAdapter(irrigationsource_adapter);
        spinnerIrrigationSource.setSelection(0);
        spinnerIrrigationSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    irrigationSource = spinnerIrrigationSource.getSelectedItem().toString();

                }else{
                    spinnerIrrigationSource.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getownernames() {

        System.out.println("Spinner value in getownername " + surveySpinnerValue);
        ArrayAdapter<String> ownernames_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayOwnerNames);
        ownernames_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        MainViewModel viewModelForOwnerNames = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModelForOwnerNames.getOwnerdetails(farmerID,surveySpinnerValue).observe(this, new Observer<List<ModelOwnerDetails>>() {

            @Override
            public void onChanged(@Nullable List<ModelOwnerDetails> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){

                    for(ModelOwnerDetails taskEntry:taskEntries){
                        //  arrayOwnerNames.add(0,"Select Owner");
                        if(!arrayOwnerNames.contains(taskEntry.getOwnerName())){
                            arrayOwnerNames.add(taskEntry.getOwnerName());
                            ownerID = taskEntry.getOwnernumber();
                        }
                        //dontchangethis
                        area = taskEntry.getArea();
                        tvOwnerAreaValue.setText(area);
                        System.out.println("area " + area);
                        splitArrayAcre = area.split("\\.");
                        System.out.println("acreValue " + splitArrayAcre[0] + " gunta" + splitArrayAcre[1] + " anna" + splitArrayAcre[2] + " cents " + splitArrayAcre[3] );
                        acreValue = splitArrayAcre[0];
                        guntaValue = splitArrayAcre[1];
                        fguntaValue = splitArrayAcre[2];
                    }

                }else{
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.beneficiary_not_present))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, CropRegister.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            })
                            .show();
                }
                ownernames_adapter.notifyDataSetChanged();
                spinnerOwnerNames.setAdapter(ownernames_adapter);
                spinnerOwnerNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        /*   if(i!=0){*/
                        ownernameSpinnerValue = spinnerOwnerNames.getSelectedItem().toString();
                        System.out.println("selecteditemis: " + ownernameSpinnerValue);
                       /* } else{
                            spinnerOwnerNames.setSelection(0);
                        }*/


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });

    }

    private void getsinglecrops(){

        arrayAllCropsEng.add("Select crops");
        ArrayAdapter<String> adapter_allcrops = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayAllCropsEng);
        adapter_allcrops.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MainViewModel view_allcrops_eng = ViewModelProviders.of(this).get(MainViewModel.class);
        view_allcrops_eng.getAllcrops().observe(this, new Observer<List<ModelCropMaster>>() {
            @Override
            public void onChanged(@Nullable List<ModelCropMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    for(ModelCropMaster taskEntry:taskEntries){
                        //  arrayOwnerNames.add(0,"Select Owner");
                        arrayAllCropsEng.add(taskEntry.getCropname_eng());
                        //      array_allcrops_kn.add(taskEntry.getCropname_kn());


                    }
                }else{
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.crops_not_available))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, CropDetails.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            })
                            .show();
                }
                adapter_allcrops.notifyDataSetChanged();
                spinnerCropName.setAdapter(adapter_allcrops);
                spinnerCropName.setSelection(0);
                spinnerCropName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            cropnameValue = spinnerCropName.getSelectedItem().toString();
                            cropCode = String.valueOf(i);
                        }else{
                            spinnerCropName.setSelection(0);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });
    }

    private void setMixedCropViews() {

        arrayMixedCropOptions.add("Select number of crops");
        arrayMixedCropOptions.add(1, "2");
        arrayMixedCropOptions.add(2, "3");
        arrayMixedCropOptions.add(3, "4");
        arrayMixedCropOptions.add(4, "5");

            ArrayAdapter<String> croppick_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayMixedCropOptions);
            croppick_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            croppick_adapter.notifyDataSetChanged();
        spinnerMixedCropSelection.setAdapter(croppick_adapter);
        spinnerMixedCropSelection.setSelection(0);
        spinnerMixedCropSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //   int counts = adapterView.getCount();
                    if (i != 0) {
                        mixedTotalCrops = spinnerMixedCropSelection.getSelectedItem().toString();
                        mixedCropCounts = 0;
                        if (spinnerMixedCropSelection.getSelectedItem().toString().equals("Select number of crops")) {
                            tblCropPick.setVisibility(View.INVISIBLE);
                            tblCropPick.removeAllViews();
                            lyRelativeMCrop.setVisibility(View.GONE);
                            lytTotalMCropExtent.setVisibility(View.GONE);
                        } else {
                            tblCropPick.setVisibility(View.VISIBLE);

                            tblCropPick.removeAllViews();

                            TableRow tbrow = new TableRow(mContext);

                            TextView tv1 = new TextView(mContext);
                            tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tv1.setGravity(Gravity.CENTER);
                            tv1.setBackgroundColor(Color.parseColor("#81D4FA"));
                            tv1.setText("Crop");
                            tv1.setTextSize(18);
                            tv1.setTextColor(Color.BLACK);

                            TextView tv2 = new TextView(mContext);
                            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tv2.setGravity(Gravity.CENTER);
                            tv2.setBackgroundColor(Color.parseColor("#9FA8DA"));
                            tv2.setText("Crop Name");
                            tv2.setTextSize(18);
                            tv2.setTextColor(Color.BLACK);

                            TextView tv3 = new TextView(mContext);
                            tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tv3.setGravity(Gravity.CENTER);
                            tv3.setBackgroundColor(Color.parseColor("#81D4FA"));
                            tv3.setText("Variety");
                            tv3.setTextColor(Color.BLACK);
                            tv3.setTextSize(18);

                            TextView tv4 = new TextView(mContext);
                            tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tv4.setGravity(Gravity.CENTER);
                            tv4.setBackgroundColor(Color.parseColor("#9FA8DA"));
                            tv4.setText("Crop Extent");
                            tv4.setTextColor(Color.BLACK);
                            tv4.setTextSize(18);

                            tbrow.addView(tv1);
                            tbrow.addView(tv2);
                            tbrow.addView(tv3);
                            tbrow.addView(tv4);
                            tblCropPick.addView(tbrow);

                            mSelectedCrop = new ArrayList<String>();
                            mSelectedCropName = new ArrayList<String>();
                            mSelectedCropVariety = new ArrayList<String>();
                            mSelectedCropExtent = new ArrayList<String>();
                            mSelectedCropCode = new ArrayList<String>();

                            splitAcreMCrop = area.split("\\.");
                            mCropAcre = Integer.parseInt(splitAcreMCrop[0]);
                            mCropGunta = Integer.parseInt(splitAcreMCrop[1]);
                            mCropFGunta = Integer.parseInt(splitAcreMCrop[2]);
                            mCropAres = Integer.parseInt(splitAcreMCrop[3]);

                            Integer remainingAcres;
                            Integer remainingGuntas;
                            Double remainingFGuntas;

                            String acre;
                            String gunta;
                            double remgunta;
                            double remAnna;
                            //if{G}
                            double anna = mCropAcre * 640 + mCropGunta * 16 + mCropFGunta;
                            anna = anna / Integer.parseInt(mixedTotalCrops);
                            double acress = Double.parseDouble(String.valueOf(anna / 640.0));
                            acre = BigDecimal.valueOf(acress).toPlainString();
                            System.out.println("acress: " + acress + " / acre: " + acre);

                            String[] acres = String.valueOf(acre).split("\\.");
                            remainingAcres = Integer.parseInt(acres[0]);
                            System.out.println(remainingAcres);

                            double remguntass = Double.parseDouble(String.valueOf(anna % 640.0));
                            remgunta = Double.parseDouble(BigDecimal.valueOf(remguntass).toPlainString());

                            double guntass = Double.parseDouble(String.valueOf(remgunta / 16.0));
                            gunta = BigDecimal.valueOf(guntass).toPlainString();

                            String[] guntas = String.valueOf(gunta).split("\\.");
                            remainingGuntas = Integer.parseInt(guntas[0]);
                            System.out.println(remainingGuntas);

                            double remAnnass = Double.parseDouble(String.valueOf(remgunta % 16.0));
                            remAnna = Double.parseDouble(BigDecimal.valueOf(remAnnass).toPlainString());

                            remainingFGuntas = round(remAnna, 2);
                            System.out.println(remainingFGuntas);
                            if (remainingFGuntas == 16) {
                                remainingGuntas = remainingGuntas + 1;
                                remainingFGuntas = 0.0;
                            }
                            if (remainingGuntas == 40) {
                                remainingFGuntas = remainingFGuntas + 1;
                                remainingGuntas = 0;
                            }

                            finalExtentValuesMixedCrop[0] = remainingAcres + "";
                            finalExtentValuesMixedCrop[1] = remainingGuntas + "";
                            finalExtentValuesMixedCrop[2] = remainingFGuntas + "";

                            System.out.println("mCropAcre: " + finalExtentValuesMixedCrop[0] + " / mCropGunta: " + finalExtentValuesMixedCrop[1] + " / mCropFGunta " + finalExtentValuesMixedCrop[2]);

                            //if{C}
                    /*double anna = mCropAcre*10000 + mCropGunta*100.0 + mCropFGunta;
                    anna = anna / Integer.parseInt(mixed_total_crops);
                    double acress = Double.parseDouble(String.valueOf(anna / 10000.0));
                    acre = BigDecimal.valueOf(acress).toPlainString();
                    System.out.println("acress: " + acress +" / acre: " + acre);

                    String[] acres = String.valueOf(acre).split("\\.");
                    remainingAcres = Integer.parseInt(acres[0]);
                    System.out.println(remainingAcres);

                    double remguntass = Double.parseDouble(String.valueOf(anna % 10000.0));
                    remgunta = Double.parseDouble(BigDecimal.valueOf(remguntass).toPlainString());

                    double guntass = Double.parseDouble(String.valueOf(remgunta / 100.0));
                    gunta = BigDecimal.valueOf(guntass).toPlainString();

                    String[] guntas = String.valueOf(gunta).split("\\.");
                    remainingGuntas = Integer.parseInt(guntas[0]);
                    System.out.println(remainingGuntas);

                    double remAnnass = Double.parseDouble(String.valueOf(remgunta % 100.0));
                    remAnna = Double.parseDouble(BigDecimal.valueOf(remAnnass).toPlainString());

                    remainingFGuntas = round(remAnna, 2);
                    System.out.println(remainingFGuntas);


                    finalextentvalues[0] = remainingAcres + "";
                    finalextentvalues[1] = remainingGuntas + "";
                    finalextentvalues[2] = remainingFGuntas + "";

                    System.out.println("mCropAcre: " + finalextentvalues[0] +" / mCropGunta: " +  finalextentvalues[1] + " / mCropFGunta " +finalextentvalues[2]);



                    */

                            /////////////////

                            //if G{}
                    /* int tempvalue = mCropAcre*640 + mCropGunta*16 + mCropFGunta;
                    System.out.print("tempvalue : " + tempvalue);
                    int tempresult = tempvalue / Integer.parseInt(mixed_total_crops);
                    System.out.print("tempresult : " + tempresult);
                    mCropAcre = tempresult / 640;
                    System.out.print("resultacre_mcropacre : " + mCropAcre);
                    mCropGunta = tempresult / 16;
                    System.out.print("resultgunta_mcropgunta : " + mCropGunta);
                    mCropFGunta = tempresult;
                    if(mCropFGunta>=16){
                        mCropFGunta = mCropFGunta/16;
                    }*/
                            //if C{}
                  /*  int tempvalue_C = (mCropAcre*100*100) + (mcropgunta*100) + mCropFGunta;
                    System.out.print("tempvalue_C : " + tempvalue_C);
                    int tempresult_C = tempvalue_C / Integer.parseInt(mixed_total_crops);
                    System.out.print("tempresult_C : " + tempresult_C);
                    int resultacre_C = tempresult_C / (100*100);
                    System.out.print("resultacre_C : " + resultacre_C);
                    int resultgunta_C = tempresult_C / 100;
                    System.out.print("resultgunta_C : " + resultgunta_C);
                    int resultfgunta_C = tempresult_C;
                    System.out.print("resultfgunta_C : " + resultfgunta_C);*/


                            for (int k = 1; k <= Integer.parseInt(mixedTotalCrops); k++) {
                                if (k == 1) {
                                    alertformixedcropentry(mContext, mixedTotalCrops, finalExtentValuesMixedCrop[0], finalExtentValuesMixedCrop[1], finalExtentValuesMixedCrop[2], mCropAres, k);
                                } else {
                                    alertformixedcropentry(mContext, mixedTotalCrops, finalExtentValuesMixedCrop[0], finalExtentValuesMixedCrop[1], finalExtentValuesMixedCrop[2], mCropAres, k);
                                }

                            }

                            mTotalCropExtent.setText(area);
                            lyRelativeMCrop.setVisibility(View.VISIBLE);
                            lytTotalMCropExtent.setVisibility(View.VISIBLE);
                        }
                    } else {
                        spinnerMixedCropSelection.setSelection(0);
                        tblCropPick.setVisibility(View.INVISIBLE);

                        tblCropPick.removeAllViews();
                        lyRelativeMCrop.setVisibility(View.GONE);
                        lytTotalMCropExtent.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });


        }

    private String[] ConvertAnnaToExtent(double anna) {
        Integer remainingAcres;
        Integer remainingGuntas;
        Double remainingFGuntas;
        String[] s = {"0", "0", "0.0"};
        String acre;
        String gunta;
        double remgunta;
        double remAnna;
       /* if (G.equals("G")) {
            double acress = Double.parseDouble(String.valueOf(anna / 640.0));

            acre = BigDecimal.valueOf(acress).toPlainString();
            System.out.println(acress);

            String[] acres = String.valueOf(acre).split("\\.");
            remainingAcres = Integer.parseInt(acres[0]);
            System.out.println(remainingAcres);

            double remguntass = Double.parseDouble(String.valueOf(anna % 640.0));
            remgunta = Double.parseDouble(BigDecimal.valueOf(remguntass).toPlainString());

            ///////////////////////////////////////////////
            double guntass = Double.parseDouble(String.valueOf(remgunta / 16.0));
            gunta = BigDecimal.valueOf(guntass).toPlainString();

            String[] guntas = String.valueOf(gunta).split("\\.");
            remainingGuntas = Integer.parseInt(guntas[0]);
            System.out.println(remainingGuntas);

            double remAnnass = Double.parseDouble(String.valueOf(remgunta % 16.0));
            remAnna = Double.parseDouble(BigDecimal.valueOf(remAnnass).toPlainString());

            remainingFGuntas = round(remAnna, 2);
            System.out.println(remainingFGuntas);
            if (remainingFGuntas == 16) {
                remainingGuntas = remainingGuntas + 1;
                remainingFGuntas = 0.0;
            }
            if (remainingGuntas == 40) {
                remainingFGuntas = remainingFGuntas + 1;
                remainingGuntas = 0;
            }
        }
        else {*/
        double acress = Double.parseDouble(String.valueOf(anna / 10000.0));

        acre = BigDecimal.valueOf(acress).toPlainString();

        String[] acres = String.valueOf(acre).split("\\.");
        remainingAcres = Integer.parseInt(acres[0]);
        System.out.println(remainingAcres);

        double remguntass = Double.parseDouble(String.valueOf(anna % 10000.0));
        remgunta = Double.parseDouble(BigDecimal.valueOf(remguntass).toPlainString());
        /////////////////
        double guntass = Double.parseDouble(String.valueOf(remgunta / 100.0)); //100.0
        gunta = BigDecimal.valueOf(guntass).toPlainString();

        String[] guntas = String.valueOf(gunta).split("\\.");
        remainingGuntas = Integer.parseInt(guntas[0]);
        System.out.println(remainingGuntas);

        double remAnnass = (remgunta % 100.0);
        remAnna = Double.parseDouble(BigDecimal.valueOf(remAnnass).toPlainString());

        remainingFGuntas = round(remAnna, 2);
        System.out.println(remainingFGuntas);
        //    }
        s[0] = remainingAcres + "";
        s[1] = remainingGuntas + "";
        s[2] = remainingFGuntas + "";

        return s;
    }

    private Double getAreaG(Integer aAcre, Integer aGunta, double aFGunta) {
        Double d_area = 0.0;
       /* if (systemType.equalsIgnoreCase("G")) {
            d_area = (aAcre * 640) + (aGunta * 16) + (aFGunta);
        } else {*/
        d_area = (aAcre * 10000) + (aGunta * 100) + (aFGunta);
        // }
        Double d_area1 = Math.round(d_area * 1e5) / 1e5;
        return d_area1;
    }

    public Double round(Double value, Integer places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void getmixedcrops(){

        arrayMCropName.add("Select Crop");
        ArrayAdapter<String> adapter_allcrops = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayMCropName);
        adapter_allcrops.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MainViewModel view_allcrops_eng = ViewModelProviders.of(this).get(MainViewModel.class);
        view_allcrops_eng.getMixedCrops("Seasonal Crop").observe(this, new Observer<List<ModelCropMaster>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    for(ModelCropMaster taskEntry:taskEntries){
                        //  arrayOwnerNames.add(0,"Select Owner");
                        arrayMCropName.add(taskEntry.getCropname_eng());
                        //      array_allcrops_kn.add(taskEntry.getCropname_kn());
                        mixed_cropcode = taskEntry.getCropcode();
                    }
                }else{
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.crops_not_available))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, CropDetails.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            })
                            .show();
                }
                adapter_allcrops.notifyDataSetChanged();
                spinnerCropName.setAdapter(adapter_allcrops);
                spinnerCropName.setSelection(0);
                spinnerCropName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            cropnameValue = spinnerCropName.getSelectedItem().toString();
                        }else{
                            spinnerCropName.setSelection(0);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });
    }

    private void alertformixedcropentry(Context mContext, String mixed_crops_selected_value, String mcpacre,String mcpgunta, String mcpfgunta,int mpares,int k) {
        getmixedcrops();

        Set<String> listmcropnames = new LinkedHashSet<String>(arrayMCropName);
        arrayMCropName.clear();
        arrayMCropName.addAll(listmcropnames);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CropRegister.this);
        alertDialog.setTitle("Mixed Crop Details");
        alertDialog.setMessage("Select Crop Details");
        alertDialog.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(CropRegister.this);
        View mixed_crop_layout = inflater.inflate(R.layout.layout_mixed_crops,null);
        //final EditText edtYield = (EditText) mixed_crop_layout.findViewById(R.id.edt_CropPickYield);
        final Spinner sp_cropname = (Spinner) mixed_crop_layout.findViewById(R.id.spinnerMCropName);
        final Spinner sp_cropvariety = (Spinner) mixed_crop_layout.findViewById(R.id.spinnerMcropvariety);

//        final EditText et_mcrop_acre = (EditText) mixed_crop_layout.findViewById(R.id.edt_mcropacre);
//        final EditText et_mcrop_gunta = (EditText) mixed_crop_layout.findViewById(R.id.edt_mcropgunta);
//        final EditText et_mcrop_fgunta = (EditText) mixed_crop_layout.findViewById(R.id.edt_mcropfgunta);

        mCropAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayMCropName);
        mCropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCropAdapter.notifyDataSetChanged();
        sp_cropname.setAdapter(mCropAdapter);
        sp_cropname.setSelection(0);

        sp_cropname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    mcropnameValue = sp_cropname.getSelectedItem().toString();
                    mcropCode = String.valueOf(i);
                }else{
                    sp_cropname.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> cropvariety_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayCropVariety);
        cropvariety_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cropvariety_adapter.notifyDataSetChanged();
        sp_cropvariety.setAdapter(cropvariety_adapter);
        sp_cropvariety.setSelection(0);

        sp_cropvariety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    mcropVarietyValue = sp_cropvariety.getSelectedItem().toString();

                }else{
                    sp_cropvariety.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        alertDialog.setView(mixed_crop_layout);

        //set button
        alertDialog.setPositiveButton("Confirm Mixed Crop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                mixedCropCounts = mixedCropCounts + 1;

                //  if (!sp_cropname.getSelectedItem().toString().equals("") && !sp_cropvariety.getSelectedItem().toString().equals("")) {

                TableRow tbrow2 = new TableRow(mContext);
                TextView tv_mcrop = new TextView(mContext);
                tv_mcrop.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv_mcrop.setGravity(Gravity.CENTER);
                tv_mcrop.setBackgroundColor(Color.parseColor("#E1F5FE"));
                tv_mcrop.setTextColor(Color.BLACK);
                tv_mcrop.setTextSize(18);
                tv_mcrop.setText("" + mixedCropCounts);

                TextView tv_mcropname = new TextView(mContext);
                tv_mcropname.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv_mcropname.setGravity(Gravity.CENTER);
                tv_mcropname.setBackgroundColor(Color.parseColor("#E8EAF6"));
                tv_mcropname.setTextColor(Color.BLACK);
                tv_mcropname.setTextSize(18);
                tv_mcropname.setText("" + mcropnameValue);

                TextView tv_mcropvariety = new TextView(mContext);
                tv_mcropvariety.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv_mcropvariety.setBackgroundColor(Color.parseColor("#E1F5FE"));
                tv_mcropvariety.setTextSize(18);
                tv_mcropvariety.setTextColor(Color.BLACK);
                tv_mcropvariety.setGravity(Gravity.CENTER);
                tv_mcropvariety.setText("" + mcropVarietyValue);

                TextView tv_cropextent = new TextView(mContext);
                tv_cropextent.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv_cropextent.setBackgroundColor(Color.parseColor("#E8EAF6"));
                tv_cropextent.setTextSize(18);
                tv_cropextent.setTextColor(Color.BLACK);
                tv_cropextent.setGravity(Gravity.CENTER);
                tv_cropextent.setText("" +mcpacre+ "." +mcpgunta+ "." +mcpfgunta); // convertedextent[0] + "." + convertedextent[1] + "." + convertedextent[2]

                tbrow2.addView(tv_mcrop);
                tbrow2.addView(tv_mcropname);
                tbrow2.addView(tv_mcropvariety);
                tbrow2.addView(tv_cropextent);
                tblCropPick.addView(tbrow2);

                acreMixed = mcpacre+ "." +mcpgunta+ "." +mcpfgunta;
                mSelectedCrop.add(String.valueOf(mixedCropCounts));
                mSelectedCropName.add(mcropnameValue);
                mSelectedCropVariety.add(mcropVarietyValue);
                mSelectedCropExtent.add(acreMixed);
                mSelectedCropCode.add(mcropCode);

                mCropAdapter.remove((String) sp_cropname.getSelectedItem());
                mCropAdapter.notifyDataSetChanged();

                System.out.println("mSelectedCropExtent" + mSelectedCropExtent.get(0));
                System.out.println("mSelectedCrop size" + mSelectedCrop.size());
                System.out.println("mSelectedCropName size " + mSelectedCropName.size()+ ": " + mSelectedCropName);
                System.out.println("mSelectedCropVariety size " + mSelectedCropVariety.size());
                System.out.println("mSelectedCropExtent size " + mSelectedCropExtent.size());

               /* }else{
                    Toast.makeText(mixedle_crop_layout.getContext(),"Select crop name and crop variety",Toast.LENGTH_SHORT).show();
                }*/

            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                spinnerMixedCropSelection.setSelection(0);
                tblCropPick.setVisibility(View.INVISIBLE);
                tblCropPick.removeAllViews();
                lyRelativeMCrop.setVisibility(View.GONE);
                lytTotalMCropExtent.setVisibility(View.GONE);

            }

        });

        final AlertDialog dialog = alertDialog.create();
        dialog.show();
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);



//commented on 3.9.21
      /*  et_mcrop_acre.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    if (Integer.parseInt(et_mcrop_acre.getText().toString().trim()) <= mcpacre) {

                        mCropAcre = Integer.parseInt(et_mcrop_acre.getText().toString().trim());
                        et_mcrop_gunta.setText("");
                        et_mcrop_fgunta.setText("");
                      *//*  et_cents.setText("");
                        et_ares.setText("");*/
        /*
                    }
                    else {
                        AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                        alertDialog.setTitle("ಸೂಚನೆ :");
                        alertDialog.setMessage("ಸರಿಯಾದ ಎಕರೆ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                        alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        et_mcrop_acre.setText("");
                        et_mcrop_acre.requestFocus();
                    }
                }   catch (NumberFormatException npe){
                    npe.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                // Check if edittext is empty
                if (TextUtils.isEmpty(s)) {

                    //            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                } else {

                    //        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });

        et_mcrop_gunta.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    if (et_mcrop_gunta.getText().toString().trim().length() > 1) {
                        if (Integer.parseInt(et_mcrop_gunta.getText().toString().trim()) < 40) {
                            if (Integer.parseInt(et_mcrop_gunta.getText().toString().trim()) <= mcpgunta) {

                                mCropGunta = Integer.parseInt(et_mcrop_gunta.getText().toString().trim());
                                et_mcrop_fgunta.setText("");
                                    */
        /*et_cents.setText("");
                                    et_ares.setText("");*//*
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                                alertDialog.setTitle("ಸೂಚನೆ :");
                                alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                                alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        et_mcrop_gunta.setText("");
                                           *//* et_cents.setText("");
                                            et_ares.setText("");*//*
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setCanceledOnTouchOutside(false);
                                et_mcrop_gunta.requestFocus();
                                et_mcrop_gunta.setText("");
                                et_mcrop_fgunta.setText("");


                                    *//*et_cents.setText("");
                                    et_ares.setText("");*//*
                            }

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    et_mcrop_fgunta.setText("");
                                        *//*et_cents.setText("");
                                        et_ares.setText("");*//*
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            et_mcrop_gunta.requestFocus();
                            et_mcrop_gunta.setText("");
                            et_mcrop_fgunta.setText("");

                              *//*  et_cents.setText("");
                                et_ares.setText("");*//*
                        }
                    } else {
                        if (Integer.parseInt(et_mcrop_gunta.getText().toString()) < 100) {
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    et_mcrop_fgunta.setText("");
                                       *//* et_cents.setText("");
                                        et_ares.setText("");*//*
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            et_mcrop_gunta.requestFocus();
                            et_mcrop_gunta.setText("");
                            et_mcrop_fgunta.setText("");

                               *//* et_cents.setText("");
                                et_ares.setText("");*//*
                        }
                    }

                }
                catch (NumberFormatException npe){
                    npe.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                // Check if edittext is empty
                if (TextUtils.isEmpty(s)) {

                    //            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                } else {

                    //        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });

        et_mcrop_fgunta.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    if (et_mcrop_fgunta.getText().toString().trim().length() > 1) {

                        if (Integer.parseInt(et_mcrop_fgunta.getText().toString().trim()) < 16) {
                            if (Integer.parseInt(et_mcrop_fgunta.getText().toString().trim()) < mpfgunta){
                                mCropFGunta = Integer.parseInt(et_mcrop_fgunta.getText().toString().trim());
                            }
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಅಣಾ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                   *//* et_cents.setText("");
                                    et_ares.setText("");*//*
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            et_mcrop_fgunta.setText("");
                            et_mcrop_fgunta.requestFocus();

                           *//* et_cents.setText("");
                            et_ares.setText("");*//*
                        }
                    } else {
                        if (Integer.parseInt(et_mcrop_fgunta.getText().toString()) < 100) {
                            if (Integer.parseInt(et_mcrop_fgunta.getText().toString()) <= Integer.parseInt(guntaValue.trim())){}
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಅಣಾ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                       *//* et_cents.setText("");
                                        et_ares.setText("");*//*
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            et_mcrop_fgunta.setText("");
                            et_mcrop_fgunta.requestFocus();

                               *//* et_cents.setText("");
                                et_ares.setText("");*//*
                        }
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                // Check if edittext is empty
                if (TextUtils.isEmpty(s)) {

                    //            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                } else {

                    //        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });*/
        //commented 3.9.21

        //if(!sp_cropname.getSelectedItem().toString().equals(""))

      /*  ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);


        if(sp_cropname.getSelectedItem().toString().equals("Select Crop") && sp_cropvariety.getSelectedItem().toString().equals("Select Crop Variety")){
            System.out.println("not selected");
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        }else{
            System.out.println("selected");
           ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

        }*/
    }

    //intercrops
    private void setInterCropViews() {
        arrayInterCropOptions.add("Select number of crops");
        arrayInterCropOptions.add(1, "2");
        arrayInterCropOptions.add(2, "3");
        arrayInterCropOptions.add(3, "4");
        arrayInterCropOptions.add(4, "5");

        ArrayAdapter<String> croppick_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayInterCropOptions);
        croppick_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        croppick_adapter.notifyDataSetChanged();
        spinnerInterCropSelection.setAdapter(croppick_adapter);
        spinnerInterCropSelection.setSelection(0);
        spinnerInterCropSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
                    interTotalCrops = spinnerInterCropSelection.getSelectedItem().toString();
                    interCropCounts = 0;
                    if (spinnerInterCropSelection.getSelectedItem().toString().equals("Select number of crops")) {
                        tblInterCropPick.setVisibility(View.INVISIBLE);

                        tblInterCropPick.removeAllViews();
                        lyRelativeInterCrop.setVisibility(View.GONE);
                        lytTotalInterCropExtent.setVisibility(View.GONE);
                    } else {
                        tblInterCropPick.setVisibility(View.VISIBLE);

                        tblInterCropPick.removeAllViews();

                        TableRow tb_intercroprow = new TableRow(mContext);

                        TextView tv1_intercrop = new TextView(mContext);
                        tv1_intercrop.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tv1_intercrop.setGravity(Gravity.CENTER);
                        tv1_intercrop.setBackgroundColor(Color.parseColor("#81D4FA"));
                        tv1_intercrop.setText("Crop");
                        tv1_intercrop.setTextSize(18);
                        tv1_intercrop.setTextColor(Color.BLACK);

                        TextView tv2_intercrop = new TextView(mContext);
                        tv2_intercrop.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tv2_intercrop.setGravity(Gravity.CENTER);
                        tv2_intercrop.setBackgroundColor(Color.parseColor("#9FA8DA"));
                        tv2_intercrop.setText("Crop Name");
                        tv2_intercrop.setTextSize(18);
                        tv2_intercrop.setTextColor(Color.BLACK);

                        TextView tv3_intercrop = new TextView(mContext);
                        tv3_intercrop.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tv3_intercrop.setGravity(Gravity.CENTER);
                        tv3_intercrop.setBackgroundColor(Color.parseColor("#81D4FA"));
                        tv3_intercrop.setText("Variety");
                        tv3_intercrop.setTextColor(Color.BLACK);
                        tv3_intercrop.setTextSize(18);

                        TextView tv4_intercrop = new TextView(mContext);
                        tv4_intercrop.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tv4_intercrop.setGravity(Gravity.CENTER);
                        tv4_intercrop.setBackgroundColor(Color.parseColor("#9FA8DA"));
                        tv4_intercrop.setText("Crop Extent");
                        tv4_intercrop.setTextColor(Color.BLACK);
                        tv4_intercrop.setTextSize(18);

                        tb_intercroprow.addView(tv1_intercrop);
                        tb_intercroprow.addView(tv2_intercrop);
                        tb_intercroprow.addView(tv3_intercrop);
                        tb_intercroprow.addView(tv4_intercrop);
                        tblInterCropPick.addView(tb_intercroprow);

                        interSelectedCrop = new ArrayList<String>();
                        interSelectedCropName = new ArrayList<String>();
                        interSelectedCropVariety = new ArrayList<String>();
                        interSelectedCropExtent = new ArrayList<String>();
                        interSelectedCropCode = new ArrayList<String>();

                        splitAcreMCrop = area.split("\\.");
                        interCropAcre = Integer.parseInt(splitAcreMCrop[0]);
                        interCropGunta = Integer.parseInt(splitAcreMCrop[1]);
                        interCropFGunta = Integer.parseInt(splitAcreMCrop[2]);
                       // intercropares =  Integer.parseInt(splitAcreMCrop[3]);

                        Integer remainingAcres;
                        Integer remainingGuntas;
                        Double remainingFGuntas;

                        String acre;
                        String gunta;
                        double remgunta;
                        double remAnna;
                        //if{G}
                        double anna = interCropAcre*640 + interCropGunta*16 + interCropFGunta;
                        anna = anna / Integer.parseInt(interTotalCrops);
                        double acress = Double.parseDouble(String.valueOf(anna / 640.0));
                        acre = BigDecimal.valueOf(acress).toPlainString();
                        System.out.println("acress: " + acress +" / acre: " + acre);

                        String[] acres = String.valueOf(acre).split("\\.");
                        remainingAcres = Integer.parseInt(acres[0]);
                        System.out.println(remainingAcres);

                        double remguntass = Double.parseDouble(String.valueOf(anna % 640.0));
                        remgunta = Double.parseDouble(BigDecimal.valueOf(remguntass).toPlainString());

                        double guntass = Double.parseDouble(String.valueOf(remgunta / 16.0));
                        gunta = BigDecimal.valueOf(guntass).toPlainString();

                        String[] guntas = String.valueOf(gunta).split("\\.");
                        remainingGuntas = Integer.parseInt(guntas[0]);
                        System.out.println(remainingGuntas);

                        double remAnnass = Double.parseDouble(String.valueOf(remgunta % 16.0));
                        remAnna = Double.parseDouble(BigDecimal.valueOf(remAnnass).toPlainString());

                        remainingFGuntas = round(remAnna, 2);
                        System.out.println(remainingFGuntas);
                        if (remainingFGuntas == 16) {
                            remainingGuntas = remainingGuntas + 1;
                            remainingFGuntas = 0.0;
                        }
                        if (remainingGuntas == 40) {
                            remainingFGuntas = remainingFGuntas + 1;
                            remainingGuntas = 0;
                        }

                        finalExtentValuesInterCrop[0] = remainingAcres + "";
                        finalExtentValuesInterCrop[1] = remainingGuntas + "";
                        finalExtentValuesInterCrop[2] = remainingFGuntas + "";

                        System.out.println("icropacre: " + finalExtentValuesInterCrop[0] +" / icropgunta: " +  finalExtentValuesInterCrop[1] + " / icropfgunta " +finalExtentValuesInterCrop[2]);

                        //if{C}
                        /*double anna = mCropAcre*10000 + mCropGunta*100.0 + mCropFGunta;
                        anna = anna / Integer.parseInt(mixed_total_crops);
                        double acress = Double.parseDouble(String.valueOf(anna / 10000.0));
                        acre = BigDecimal.valueOf(acress).toPlainString();
                        System.out.println("acress: " + acress +" / acre: " + acre);

                        String[] acres = String.valueOf(acre).split("\\.");
                        remainingAcres = Integer.parseInt(acres[0]);
                        System.out.println(remainingAcres);

                        double remguntass = Double.parseDouble(String.valueOf(anna % 10000.0));
                        remgunta = Double.parseDouble(BigDecimal.valueOf(remguntass).toPlainString());

                        double guntass = Double.parseDouble(String.valueOf(remgunta / 100.0));
                        gunta = BigDecimal.valueOf(guntass).toPlainString();

                        String[] guntas = String.valueOf(gunta).split("\\.");
                        remainingGuntas = Integer.parseInt(guntas[0]);
                        System.out.println(remainingGuntas);

                        double remAnnass = Double.parseDouble(String.valueOf(remgunta % 100.0));
                        remAnna = Double.parseDouble(BigDecimal.valueOf(remAnnass).toPlainString());

                        remainingFGuntas = round(remAnna, 2);
                        System.out.println(remainingFGuntas);


                        finalextentvalues[0] = remainingAcres + "";
                        finalextentvalues[1] = remainingGuntas + "";
                        finalextentvalues[2] = remainingFGuntas + "";

                        System.out.println("mCropAcre: " + finalextentvalues[0] +" / mCropGunta: " +  finalextentvalues[1] + " / mCropFGunta " +finalextentvalues[2]);



                        */



                        //if G{}
                        /*int tempvalue =  interCropAcre*40*16 + interCropGunta*16 + interCropFGunta;
                        System.out.print("tempvalue : " + tempvalue);
                        int tempresult = tempvalue / Integer.parseInt(interTotalCrops);
                        System.out.print("tempresult : " + tempresult);
                        interCropAcre = tempresult / (40*16);
                        System.out.print("resultacre_intercropacre : " + interCropAcre);
                        interCropGunta = tempresult / 16;
                        System.out.print("resultgunta_intercropgunta : " + interCropGunta);
                        interCropFGunta = tempresult;
                        System.out.print("resultfgunta_intercropfgunta : " + interCropFGunta);
                        if(interCropFGunta>=100){
                            interCropFGunta = interCropFGunta/16;
                        }*/
                        //if C{}
                      /*  int tempvalue_C = (mCropAcre*100*100) + (mCropGunta*100) + mCropFGunta;
                        System.out.print("tempvalue_C : " + tempvalue_C);
                        int tempresult_C = tempvalue_C / Integer.parseInt(mixed_total_crops);
                        System.out.print("tempresult_C : " + tempresult_C);
                        int resultacre_C = tempresult_C / (100*100);
                        System.out.print("resultacre_C : " + resultacre_C);
                        int resultgunta_C = tempresult_C / 100;
                        System.out.print("resultgunta_C : " + resultgunta_C);
                        int resultfgunta_C = tempresult_C;
                        System.out.print("resultfgunta_C : " + resultfgunta_C);*/


                        for (int k = 1; k <= Integer.parseInt(interTotalCrops); k++) {

                            alertforintercropentry(mContext, finalExtentValuesInterCrop[0],finalExtentValuesInterCrop[1],finalExtentValuesInterCrop[2], k);
                        }

                        // intercropextentverify(Integer.parseInt(interTotalCrops));
                        interTotalCropExtent.setText(area); //sample[2]+"."+sample[0]+"."+sample[1] //interCropAcre+"."+interCropGunta+"."+interCropFGunta
                        lyRelativeInterCrop.setVisibility(View.VISIBLE);
                        lytTotalInterCropExtent.setVisibility(View.VISIBLE);
                    }
                } else {
                    spinnerInterCropSelection.setSelection(0);
                    tblInterCropPick.setVisibility(View.INVISIBLE);
                    tblInterCropPick.removeAllViews();
                    lyRelativeInterCrop.setVisibility(View.GONE);
                    lytTotalInterCropExtent.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getIntercrops(){
        arrayInterCropName.add("Select Crop");
        ArrayAdapter<String> adapter_allcrops = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayInterCropName);
        adapter_allcrops.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MainViewModel view_allcrops_eng = ViewModelProviders.of(this).get(MainViewModel.class);
        view_allcrops_eng.getAllcrops().observe(this, new Observer<List<ModelCropMaster>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    for(ModelCropMaster taskEntry:taskEntries){
                        //  arrayOwnerNames.add(0,"Select Owner");
                        arrayInterCropName.add(taskEntry.getCropname_eng());
                        //      array_allcrops_kn.add(taskEntry.getCropname_kn());
                        inter_cropcode = taskEntry.getCropcode();
                    }
                }else{
                    new AlertDialog.Builder(mContext)
                            .setTitle(getResources().getString(R.string.alert))
                            .setMessage(getResources().getString(R.string.crops_not_available))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent mainActivity = new Intent(mContext, CropDetails.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            })
                            .show();
                }
                adapter_allcrops.notifyDataSetChanged();
                spinnerCropName.setAdapter(adapter_allcrops);
                spinnerCropName.setSelection(0);
                spinnerCropName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            cropnameValue = spinnerCropName.getSelectedItem().toString();
                        }else{
                            spinnerCropName.setSelection(0);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });
    }

    private void alertforintercropentry(Context mContext, String icpacre,String icpgunta, String icpfgunta, int k) {

        getIntercrops();
        Set<String> listicropnames = new LinkedHashSet<String>(arrayInterCropName);
        arrayInterCropName.clear();
        arrayInterCropName.addAll(listicropnames);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CropRegister.this);
        alertDialog.setTitle("Inter Crop Details");
        alertDialog.setMessage("Select Crop Details");
        alertDialog.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(CropRegister.this);
        View inter_crop_layout = inflater.inflate(R.layout.layout_inter_crops,null);
        final Spinner sp_icropname = (Spinner) inter_crop_layout.findViewById(R.id.spinneriCropName);
        final Spinner sp_icropvariety = (Spinner) inter_crop_layout.findViewById(R.id.spinnericropvariety);

        interCropAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayInterCropName);
        interCropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interCropAdapter.notifyDataSetChanged();
        sp_icropname.setAdapter(interCropAdapter);
        sp_icropname.setSelection(0);
        sp_icropname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    intercropnameValue = sp_icropname.getSelectedItem().toString();
                    intercropCode = String.valueOf(i);
                }else{
                    sp_icropname.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> intercropvariety_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayCropVariety);
        intercropvariety_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intercropvariety_adapter.notifyDataSetChanged();
        sp_icropvariety.setAdapter(intercropvariety_adapter);
        sp_icropvariety.setSelection(0);
        sp_icropvariety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    intercropVarietyValue = sp_icropvariety.getSelectedItem().toString();

                }else{
                    sp_icropvariety.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        alertDialog.setView(inter_crop_layout);

        //set button
        alertDialog.setPositiveButton("Confirm Inter Crop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                interCropCounts = interCropCounts + 1;

                TableRow tbrow_inter = new TableRow(mContext);
                TextView tv_intercrop = new TextView(mContext);
                tv_intercrop.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv_intercrop.setGravity(Gravity.CENTER);
                tv_intercrop.setBackgroundColor(Color.parseColor("#E1F5FE"));
                tv_intercrop.setTextColor(Color.BLACK);
                tv_intercrop.setTextSize(18);
                tv_intercrop.setText("" + interCropCounts);

                TextView tv_intercropname = new TextView(mContext);
                tv_intercropname.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv_intercropname.setGravity(Gravity.CENTER);
                tv_intercropname.setBackgroundColor(Color.parseColor("#E8EAF6"));
                tv_intercropname.setTextColor(Color.BLACK);
                tv_intercropname.setTextSize(18);
                tv_intercropname.setText("" + intercropnameValue);

                TextView tv_intercropvariety = new TextView(mContext);
                tv_intercropvariety.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv_intercropvariety.setBackgroundColor(Color.parseColor("#E1F5FE"));
                tv_intercropvariety.setTextSize(18);
                tv_intercropvariety.setTextColor(Color.BLACK);
                tv_intercropvariety.setGravity(Gravity.CENTER);
                tv_intercropvariety.setText("" + intercropVarietyValue);

                TextView tv_intercropextent = new TextView(mContext);
                tv_intercropextent.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv_intercropextent.setBackgroundColor(Color.parseColor("#E8EAF6"));
                tv_intercropextent.setTextSize(18);
                tv_intercropextent.setTextColor(Color.BLACK);
                tv_intercropextent.setGravity(Gravity.CENTER);
                tv_intercropextent.setText("" + icpacre+"."+icpgunta+"."+icpfgunta);

                tbrow_inter.addView(tv_intercrop);
                tbrow_inter.addView(tv_intercropname);
                tbrow_inter.addView(tv_intercropvariety);
                tbrow_inter.addView(tv_intercropextent);
                tblInterCropPick.addView(tbrow_inter);
                acreInter = icpacre+"."+icpgunta+"."+icpfgunta;
                interSelectedCrop.add(String.valueOf(interCropCounts));
                interSelectedCropName.add(intercropnameValue);
                interSelectedCropVariety.add(intercropVarietyValue);
                interSelectedCropExtent.add(acreInter);
                interSelectedCropCode.add(intercropCode);
                interCropAdapter.remove((String) sp_icropname.getSelectedItem());
                interCropAdapter.notifyDataSetChanged();

            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                spinnerInterCropSelection.setSelection(0);
                dialogInterface.dismiss();
                tblInterCropPick.setVisibility(View.INVISIBLE);
                tblInterCropPick.removeAllViews();
                lyRelativeInterCrop.setVisibility(View.GONE);
                lytTotalInterCropExtent.setVisibility(View.GONE);
            }
        });

        final AlertDialog dialog = alertDialog.create();
        dialog.show();

      /*  ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);


        if(sp_cropname.getSelectedItem().toString().equals("Select Crop") && sp_cropvariety.getSelectedItem().toString().equals("Select Crop Variety")){
            System.out.println("not selected");
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        }else{
            System.out.println("selected");
           ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

        }*/
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
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Home", "Camera Permission Granted");
                } else {
                    Log.d("Home", "Camera Permission Failed");
                    Toast.makeText(mContext, "You must allow camera permission to capture image", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }


        }
        getMyLocation();


    }

    public void getMapdata(String survey_no){

      /*  Log.d("map_village_name111",textViewHissaNumber.getText().toString());
        String[] arrSplit = textViewHissaNumber.getText().toString().split("/");
        Log.d("arrSplit",String.valueOf(arrSplit));
        String splisurveyno = "";

        for (int i=0; i < arrSplit.length; i++)
        {
            splisurveyno = arrSplit[0];

        }
        Log.d("splitsurveyno",splisurveyno);

        String userEnteredNumber = null;
        String[] EnteredNumberSplit = EnteredNumber.split("/");

        try {
            if (survey_no != null && splisurveyno != null) {
                System.out.println("svname " +selectedENGVillageName);

                if (splisurveyno.equals(survey_no)) {
                    System.out.println("calling if splitsurvey or entered sno" + splisurveyno.equals(survey_no));
                    issurveyfromMap = true;
                    btnMap.setEnabled(false);
                    btnMap.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.VISIBLE);

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("ನಕ್ಷೆಯ ಸೂಚನೆ:");
                    alertDialog.setMessage("ನಕ್ಷೆಯಿಂದ ಆಯ್ಕೆಯಾದ\n"+ "ಸರ್ವೆನಂಬರ್ : " + survey_no +"ದೃಡೀಕರಿಸಲಾಗಿದೆ");
                    alertDialog.setPositiveButton("ಸರಿ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int n) {

                        }
                    });

                    alertDialog.setCancelable(false);
                    alertDialog.show();
                } else {
                    System.out.println("calling else splitsurvey or entered sno");
                    //  System.out.println("calling aa inside else");
                    issurveyfromMap = false;
                    btnMap.setEnabled(true);
                    btnMap.setVisibility(View.VISIBLE);
                    btnAdd.setVisibility(View.GONE);
                    //   System.out.println("map_village_name " + village_name + " map_survey_no " + survey_no);

                    //dialogbox

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("ಸೂಚನೆ :");
                    alertDialog.setMessage("ನಕ್ಷೆಯಿಂದ ಆಯ್ಕೆಯಾದ ಸರ್ವೆನಂಬರ್ : " + survey_no + " ತಪ್ಪಾಗಿದೆ, ದಯವಿಟ್ಟು ನಕ್ಷೆಯಿಂದ  ಸರಿಯಾದ ಸರ್ವೆನಂಬರ್ ಆಯ್ಕೆಮಾಡಿ");
                    alertDialog.setPositiveButton("ಸರಿ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int n) {
                            gotoMap();

                        }
                    });

                    alertDialog.setNegativeButton("ಇಲ್ಲ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int n) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                }
            }
            else {
                *//*issurveyfromMap = false;
                btnMap.setEnabled(true);
                btnMap.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);*//*
                Toast.makeText(this, "No data from Map", Toast.LENGTH_SHORT).show();
            }
        }catch(NullPointerException e){
            System.out.println(e);
        }*/
        //    if(!Objects.equals(survey_no, splisurveyno)){}
    }

    public void gotoMap(String selectedLGV,String selectedVillageName, String selectedCropName, String selectedExpID, String selectedENGVN) {

        if (selectedLGV != null && selectedVillageName != null && selectedCropName != null && selectedExpID != null && selectedENGVN != null)
        {
            Intent intent = new Intent(CropRegister.this, com.ksrsac.hasiru.MainActivity_New.class);
            intent.putExtra("surveyno", "171");
            intent.putExtra("package_name", "org.nic.fruits");
            intent.putExtra("class_name", "CropRegister");
            intent.putExtra("LG_Village", "625133");
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Lg village data not found.",Toast.LENGTH_SHORT).show();
        }


    }

    @SuppressWarnings("deprecation")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FileOutputStream fileOutputStream = null;

        if (requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                tvCropImg.setText("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ");

            }
            try {
                Bitmap bitmap = decodeUri(imageUri);
                if (bitmap != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                    byte[] b = baos.toByteArray();
                    bitmap = StoreByteImage(b, imageUri);
                    String compressedImage = compressImage(String.valueOf(imageUri),imagePath,"multipick");


                    ivCaptureCropPhoto.setImageBitmap(bitmap);
                    ivCaptureCropPhoto.setVisibility(View.VISIBLE);


                    File file = new File(imageFile.getPath() + ".img");
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    String encriptedDate = Encript(b, "1212121212");
                    FileOutputStream fos = new FileOutputStream(file.getPath());
                    fos.write(results);
                    fos.close();

                    String filePath = getRealPathFromURI(imageUri.toString());
                    Bitmap scaledBitmap = null;

                    BitmapFactory.Options options = new BitmapFactory.Options();

                    // by setting this field as true, the actual bitmap pixels
                    // are not
                    // loaded in the memory. Just the bounds are loaded. If
                    // you try the use the bitmap here, you will get null.
                    options.inJustDecodeBounds = true;
                    Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
                    SimpleDateFormat sdate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

                    int actualHeight = options.outHeight;
                    int actualWidth = options.outWidth;

                    // max Height and width values of the compressed image is
                    // taken as
                    // 816x612

                    float maxHeight = 816.0f;
                    float maxWidth = 612.0f;
                    float imgRatio = actualWidth / actualHeight;
                    float maxRatio = maxWidth / maxHeight;

                    // width and height values are set maintaining the aspect
                    // ratio of the
                    // image

                    if (actualHeight > maxHeight || actualWidth > maxWidth) {
                        if (imgRatio < maxRatio) {
                            imgRatio = maxHeight / actualHeight;
                            actualWidth = (int) (imgRatio * actualWidth);
                            actualHeight = (int) maxHeight;
                        } else if (imgRatio > maxRatio) {
                            imgRatio = maxWidth / actualWidth;
                            actualHeight = (int) (imgRatio * actualHeight);
                            actualWidth = (int) maxWidth;
                        } else {
                            actualHeight = (int) maxHeight;
                            actualWidth = (int) maxWidth;
                        }
                    }
                    // setting inSampleSize value allows to load a scaled down
                    // version of
                    // the original image

                    options.inSampleSize = calculateInSampleSize(options,
                            actualWidth, actualHeight);

                    // inJustDecodeBounds set to false to load the actual bitmap
                    options.inJustDecodeBounds = false;

                    // this options allow android to claim the bitmap memory if
                    // it runs low
                    // on memory
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    options.inTempStorage = new byte[16 * 1024];
                    bmp = BitmapFactory.decodeFile(filePath, options);

                    bmp = timestampItAndSave(bmp, "time stamp", sdate.toString(),"multipick");

                    try {
                        scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
                    } catch (OutOfMemoryError exception) {
                        exception.printStackTrace();
                    }

                    float ratioX = actualWidth / (float) options.outWidth;
                    float ratioY = actualHeight / (float) options.outHeight;
                    float middleX = actualWidth / 2.0f;
                    float middleY = actualHeight / 2.0f;

                    Matrix scaleMatrix = new Matrix();
                    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

                    Canvas canvas = new Canvas(scaledBitmap);
                    canvas.setMatrix(scaleMatrix);
                    canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,middleY - bmp.getHeight() / 2,
                            new Paint(Paint.FILTER_BITMAP_FLAG));

                    // check the rotation of the image and display it properly
                    ExifInterface exif;
                    try {
                        exif = new ExifInterface(filePath);

                        int orientation = exif.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION, 0);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                            Log.d("EXIF", "Exif: " + orientation);
                        } else if (orientation == 3) {
                            matrix.postRotate(180);
                            Log.d("EXIF", "Exif: " + orientation);
                        } else if (orientation == 8) {
                            matrix.postRotate(270);
                            Log.d("EXIF", "Exif: " + orientation);
                        }
                        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                                scaledBitmap.getWidth(),
                                scaledBitmap.getHeight(), matrix, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FileOutputStream out = null;
                    String filename = filePath;
                    try {
                        out = new FileOutputStream(filename);

                        // write the compressed bitmap at the destination
                        // specified by
                        // filename.
                        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80,
                                out);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                // CurrentImage=IncreamentImageCount(CurrentImage);
                // insertIntoComponentUpdationsPhoto(imageUri.getPath());
            }
            catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (InvalidAlgorithmParameterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalBlockSizeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (BadPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        //changed in version 3.1 test version of 3.0.8
        if (requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                // Toast.makeText(FormTwoCaptureMediasActivity.this,
                // "Image not taken", Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                alertDialog.setTitle("ಸೂಚನೆ :");
                alertDialog.setMessage("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ");
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setButton("ಸರಿ",new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog,int which) { } });
                alertDialog.show();
                tvCropImg.setText("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ");

            } else {
                // Toast.makeText(FormTwoCaptureMediasActivity.this,
                // "Image saved", Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                alertDialog.setTitle("ಸೂಚನೆ :");
                alertDialog.setMessage("ಛಾಯಾಚಿತ್ರ ಉಳಿಸಲಾಗಿದೆ");
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setButton("ಸರಿ",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {}
                });
                alertDialog.show();
                //   isImageMahajarTaken = true;
                tvCropImg.setText("ಛಾಯಾಚಿತ್ರ ಉಳಿಸಲಾಗಿದೆ");
                //    mixedCrop.setImageMahajarReport(ImageMahajarpath);
                //mixedCrop.setCoordinates(latitude + "," + longitude);
            }
            try {
                Bitmap bitmap = decodeUri(imageUri);
                if (bitmap != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                    byte[] b = baos.toByteArray();
                    bitmap = StoreByteImage(b,imageUri);

                    //    btnCaptureMahajarReportImg.setVisibility(View.VISIBLE);
                    tvCropImg.setVisibility(View.VISIBLE);

                    ivCaptureCropPhoto.setImageBitmap(bitmap);
                    ivCaptureCropPhoto.setVisibility(View.VISIBLE);
                    String compressedImage = compressImage(String
                            .valueOf(imageUri), imagePath,"crop");

                    File file = new File(imageFile.getPath() + ".img");
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    String encriptedDate = Encript(b, "1212121212");
                    FileOutputStream fos = new FileOutputStream(file.getPath());
                    fos.write(results);
                    fos.close();

                    String filePath = getRealPathFromURI(imageUri.toString());
                    Bitmap scaledBitmap = null;

                    BitmapFactory.Options options = new BitmapFactory.Options();

                    // by setting this field as true, the actual bitmap pixels
                    // are not
                    // loaded in the memory. Just the bounds are loaded. If
                    // you try the use the bitmap here, you will get null.
                    options.inJustDecodeBounds = true;
                    Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
                    SimpleDateFormat sdate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

                    int actualHeight = options.outHeight;
                    int actualWidth = options.outWidth;

                    // max Height and width values of the compressed image is
                    // taken as
                    // 816x612

                    float maxHeight = 816.0f;
                    float maxWidth = 612.0f;
                    float imgRatio = actualWidth / actualHeight;
                    float maxRatio = maxWidth / maxHeight;

                    // width and height values are set maintaining the aspect
                    // ratio of the
                    // image

                    if (actualHeight > maxHeight || actualWidth > maxWidth) {
                        if (imgRatio < maxRatio) {
                            imgRatio = maxHeight / actualHeight;
                            actualWidth = (int) (imgRatio * actualWidth);
                            actualHeight = (int) maxHeight;
                        } else if (imgRatio > maxRatio) {
                            imgRatio = maxWidth / actualWidth;
                            actualHeight = (int) (imgRatio * actualHeight);
                            actualWidth = (int) maxWidth;
                        } else {
                            actualHeight = (int) maxHeight;
                            actualWidth = (int) maxWidth;
                        }
                    }
                    // setting inSampleSize value allows to load a scaled down
                    // version of
                    // the original image

                    options.inSampleSize = calculateInSampleSize(options,
                            actualWidth, actualHeight);

                    // inJustDecodeBounds set to false to load the actual bitmap
                    options.inJustDecodeBounds = false;

                    // this options allow android to claim the bitmap memory if
                    // it runs low
                    // on memory
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    options.inTempStorage = new byte[16 * 1024];
                    bmp = BitmapFactory.decodeFile(filePath, options);

                    bmp = timestampItAndSave(bmp, "time stamp",
                            sdate.toString(),"Mahajar");

                    try {
                        scaledBitmap = Bitmap.createBitmap(actualWidth,
                                actualHeight, Bitmap.Config.ARGB_8888);
                    } catch (OutOfMemoryError exception) {
                        exception.printStackTrace();
                    }

                    float ratioX = actualWidth / (float) options.outWidth;
                    float ratioY = actualHeight / (float) options.outHeight;
                    float middleX = actualWidth / 2.0f;
                    float middleY = actualHeight / 2.0f;

                    Matrix scaleMatrix = new Matrix();
                    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

                    Canvas canvas = new Canvas(scaledBitmap);
                    canvas.setMatrix(scaleMatrix);
                    canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                            middleY - bmp.getHeight() / 2, new Paint(
                                    Paint.FILTER_BITMAP_FLAG));

                    // check the rotation of the image and display it properly
                    ExifInterface exif;
                    try {
                        exif = new ExifInterface(filePath);

                        int orientation = exif.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION, 0);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                            Log.d("EXIF", "Exif: " + orientation);
                        } else if (orientation == 3) {
                            matrix.postRotate(180);
                            Log.d("EXIF", "Exif: " + orientation);
                        } else if (orientation == 8) {
                            matrix.postRotate(270);
                            Log.d("EXIF", "Exif: " + orientation);
                        }
                        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                                scaledBitmap.getWidth(),
                                scaledBitmap.getHeight(), matrix, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FileOutputStream out = null;
                    String filename = filePath;
                    try {
                        out = new FileOutputStream(filename);

                        // write the compressed bitmap at the destination
                        // specified by
                        // filename.
                        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80,
                                out);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                // CurrentImage=IncreamentImageCount(CurrentImage);
                // insertIntoComponentUpdationsPhoto(imageUri.getPath());
            }
            catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (BadPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        System.out.println("in onNewIntent");

        String intent_sur_no = intent.getStringExtra("servey_no");

        if(intent_sur_no != null){
            System.out.println("if block: " + intent.getStringExtra("servey_no"));

            setIntent(intent);
            processExtraData();
        }
        else{
            /*issurveyfromMap=false;
            btnMap.setVisibility(View.VISIBLE);
            btnMap.setEnabled(true);*/
            //Toast.makeText(this, "Village name is incorrect from map", Toast.LENGTH_SHORT).show();
            // System.out.println("null in else  ");
        }


    }

    private void processExtraData() {
        Intent intent = getIntent();

       /* if (intent != null)
        {
*//*            lg_code = intent.getStringExtra("villagecode");
            village_name = intent.getStringExtra("villagename");
            survey_no = intent.getStringExtra("servey_no");*//*
            //   System.out.println("ivn " + village_name);
            final ProgressDialog progress = new ProgressDialog(mContext);
            progress.setTitle("ಸೂಚನೆ:");
            progress.setMessage("ಗ್ರಾಮ ಮತ್ತು ಸರ್ವೆನಂಬರನ್ನು ದೃಡಪಡಿಸಲಾಗುತ್ತಿದೆ..");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();

            Runnable progressRunnable = new Runnable() {
                @Override
                public void run() {

                    // if(selectedLGVillage.equals(lg_code)){}

                    if(EnteredNumber.equals(surveynumberfrommap)) {
                        getMapdata(surveynumberfrommap);
                    }
                    else{

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                        alertDialog.setTitle("ಸೂಚನೆ :");
                        alertDialog.setMessage("ನೀವು ಆಯ್ಕೆ ಮಾಡಿದ ಸರ್ವೆನಂಬರ " +survey_no+ " ನಕ್ಷೆಯಿಂದ ಹಳ್ಳಿ ತಪ್ಪಾಗಿದೆ. ನಕ್ಷೆಯಲ್ಲಿ ಸರಿಯಾದ ಗ್ರಾಮವನ್ನು ದಯವಿಟ್ಟು ಆಯ್ಕೆಮಾಡಿ.");
                        alertDialog.setPositiveButton("ಸರಿ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int n) {
                                gotoMap();

                            }
                        });

                        alertDialog.setNegativeButton("ಇಲ್ಲ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int n) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.setCancelable(false);
                        alertDialog.show();


                    }

                    progress.cancel();
                }
            };
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 3000);
        }
        else{
            System.out.println("null intent value");
        }*/

    }

    @SuppressLint("NewApi")
    public String compressImage(String imageUri, String imagepath, String imgtype) throws InvalidKeyException,
            InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, IOException {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        SimpleDateFormat sdate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }
        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path

            bmp = BitmapFactory.decodeFile(filePath, options);

            bmp = timestampItAndSave(bmp, "time stamp", sdate.toString(),imgtype);

            int bytes = bmp.getByteCount();
            // or we can calculate bytes this way. Use a different value than 4
            // if you don't use 32bit images.
            // int bytes = b.getWidth()*b.getHeight()*4;

            ByteBuffer buffer = ByteBuffer.allocate(bytes); // Create a new
            // buffer
            bmp.copyPixelsToBuffer(buffer); // Move the byte data to the buffer
            // imageView.setImageBitmap(bmp);
            byte[] array = buffer.array();

            String encriptedDate = Encript(array, "1212121212");

        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = imagepath;
        try {
            out = new FileOutputStream(filename);

            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    private Bitmap timestampItAndSave(Bitmap bmp, String caption, String time,String imgType) {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        Bitmap canvasBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Canvas imageCanvas = new Canvas(canvasBitmap);
        Paint imagePaint = new Paint();
        imagePaint.setTextAlign(Paint.Align.CENTER);
        imagePaint.setTextSize(25f);
        imagePaint.setColor(Color.RED);
        imageCanvas.drawText(currentDateTimeString.toString(),
                canvasBitmap.getWidth() / 2, (canvasBitmap.getHeight() - 15),imagePaint);

        imagePaint.setTextAlign(Paint.Align.CENTER);
        imagePaint.setTextSize(30f);
//		imagePaint.setStyle(Style.FILL);
        imagePaint.setColor(getResources().getColor(R.color.orange));
        if (latitude != 0.00 && longitude != 0.00 && !imgType.equalsIgnoreCase("Mahajar"))
            imageCanvas.drawText("Lat:"+latitude+ "  Long:"+longitude, canvasBitmap.getWidth() / 2,-25, imagePaint);

        //changed in 3.0.6
        imagePaint.setTextAlign(Paint.Align.CENTER);
        imagePaint.setTextSize(25f);
        imagePaint.setColor(getResources().getColor(R.color.orange));
        // if (selectedExperimentNumber!=0)
        imageCanvas.drawText("Farmer ID : "+farmerID , canvasBitmap.getWidth() / 2,25, imagePaint);

        if(cropType.equals("Single Crop")) {
            imageCanvas.drawText("Survey No.: " + surveySpinnerValue + "  Crop: " + cropnameValue, canvasBitmap.getWidth() / 2, (canvasBitmap.getHeight() - 50), imagePaint);
        }else if(cropType.equals("Mixed Crop")){
            imageCanvas.drawText("Survey No.: " + surveySpinnerValue + "  Crop: " + mSelectedCropName.toString(), canvasBitmap.getWidth() / 2, (canvasBitmap.getHeight() - 50), imagePaint);

        }else if(cropType.equals("Inter Crop")){
            imageCanvas.drawText("Survey No.: " + surveySpinnerValue + "  Crop: " + interSelectedCropName.toString(), canvasBitmap.getWidth() / 2, (canvasBitmap.getHeight() - 50), imagePaint);

        }
        return canvasBitmap;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null,
                null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private String Encript(byte[] b, String key) throws InvalidKeyException,
            InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b1 = key.getBytes("UTF-8");
        int len = b1.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b1, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        results = cipher.doFinal(b);
        String fileName = "abcd.mpe";
        // BASE64Encoder encoder = new BASE64Encoder();
        // return encoder.encode(results); // it returns the result as a String
        // Save results as binaryfile in local folder

        // return fileName
        return fileName;

    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 500;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o2);
    }

    private Bitmap StoreByteImage(byte[] imageData,Uri imgUri) {

        Bitmap rotatedBitmap = null;
        Bitmap capturedImage = null;
        FileOutputStream fileOutputStream = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) 0.5;
            Bitmap myImage = BitmapFactory.decodeByteArray(imageData, 0,
                    imageData.length, options);
            Matrix matrix = new Matrix();
            ExifInterface exifReader = null;

            try {
                exifReader = new ExifInterface(imgUri.getPath());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }// Location of your image

            int orientation = exifReader.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            if (orientation == ExifInterface.ORIENTATION_NORMAL) {

                // Do nothing. The original image is fine.
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {

                matrix.postRotate(90);

            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {

                matrix.postRotate(180);

            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {

                matrix.postRotate(270);

            }
            rotatedBitmap = Bitmap.createBitmap(myImage, 0, 0,
                    myImage.getWidth(), myImage.getHeight(), matrix, true);
            capturedImage = rotatedBitmap;


            String ipath = imgUri.getPath().replace(root + "/CIMS/FarmerFolder/", "");// for nexus 4
            String url = "/sdcard/CIMS/FarmerFolder/" + "C-" + ipath;// for
            // nexus
            // 4
            fileOutputStream = new FileOutputStream(url);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream, 8129);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            // byte[] b = baos.toByteArray();

            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("mock", "onresume");
    }

    @Override
    protected void onPause() { super.onPause(); }

    private void checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(CropRegister.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            getMyLocation();
        }

    }

  /*  private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {


                int permissionLocation = ContextCompat.checkSelfPermission(CropRegister.this,Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient  = LocationServices.getFusedLocationProviderClient(this);
                    mFusedLocationClient.getLastLocation()
                            .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                                @Override
                                public void onComplete(@NonNull Task<Location> task) {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        latLongLocation = task.getResult();
                                        latitude = latLongLocation.getLatitude();
                                        longitude = latLongLocation.getLongitude();
                                        tv_latitude.setText("Latitude : " + latitude);
                                        tv_longitude.setText("Longitude : " + longitude);
                                        //updateUI();
                                    }else{

                                    }
                                }
                            });

                }
            }
        }
    }*/

    private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(CropRegister.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    latLongLocation =  LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    //  LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(1000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(CropRegister.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        latLongLocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(CropRegister.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    private void singlecropNotification(String crID) {
        Intent intent = new Intent( this , CropDetails.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(CropRegister. this,
                DEFAULT_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Crop Registration - Single Crop")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Crop has been registered successfully for \nFarmer ID - "+farmerID+" \nCrop name - " + cropnameValue + "\nand unique Crop Registration ID is "+crID));

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE ) ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notificationChannel.enableLights( true ) ;
            notificationChannel.setLightColor(Color.GREEN) ;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        //    notificationManager.notify(1, mBuilder.build());
            assert mNotificationManager != null;
            notificationManager.notify((int)System.currentTimeMillis(), mBuilder.build());

        } else {

            Notification.Builder nBuilder = new Notification.Builder(CropRegister.this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher))
                    .setContentTitle("Crop Registration - Single Crop")
                  //  .setContentText("Farmer Id- "+farmerID+" has registered to crop - " + cropnameValue)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setStyle(new Notification.BigTextStyle().bigText("Crop has been registered successfully for \nFarmer ID - "+farmerID+" \nCrop name - " + cropnameValue + "\nand unique Crop Registration ID is "+crID));
       //     ((NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, builder.build()); // build API 16
            assert mNotificationManager != null;
            notificationManager.notify((int)System.currentTimeMillis(), nBuilder.build());
        }

    }

    private void mixedcropNotification(String cropname,String crID) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(CropRegister. this,
                DEFAULT_NOTIFICATION_CHANNEL_ID )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentTitle("Crop Registration - Mixed Crop")
              //  .setContentText("Farmer Id- "+farmerID+" has registered to crop - " + cropname);
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Crop has been registered successfully for \nFarmer ID - "+farmerID+" \nCrop name - " + cropname + "\nand unique Crop Registration ID is "+crID));

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE ) ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notificationChannel.enableLights( true );
            notificationChannel.setLightColor(Color.GREEN);
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
          //  notificationManager.notify(i, mBuilder.build());
            assert mNotificationManager != null;
            notificationManager.notify((int)System.currentTimeMillis(), mBuilder.build());

        } else {

            Notification.Builder nbuilder = new Notification.Builder(CropRegister.this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher))
                    .setContentTitle("Crop Registration - Mixed Crop")
                 //   .setContentText("Farmer Id- "+farmerID+" has registered to crop - " + cropname)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setStyle(new Notification.BigTextStyle().bigText("Crop has been registered successfully for \nFarmer ID - "+farmerID+" \nCrop name - " + cropname + "\nand unique Crop Registration ID is "+crID));
            // ((NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE)).notify(i, builder.build()); // build API 16
            assert mNotificationManager != null;
            notificationManager.notify((int)System.currentTimeMillis(), nbuilder.build());

        }


    }

    private void intercropNotification(String cropname,String crID) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(CropRegister. this,
                DEFAULT_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentTitle("Crop Registration - Inter Crop")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Crop has been registered successfully for \nFarmer ID - "+farmerID+" \nCrop name - " + cropname + "\nand unique Crop Registration ID is "+crID));

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE ) ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notificationChannel.enableLights( true ) ;
            notificationChannel.setLightColor(Color.GREEN) ;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        //    notificationManager.notify(1, mBuilder.build());
            assert mNotificationManager != null;
            notificationManager.notify((int)System.currentTimeMillis(), mBuilder.build());
        } else {

            Notification.Builder nbuilder = new Notification.Builder(CropRegister.this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher))
                    .setContentTitle("Crop Registration - Inter Crop")
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setStyle(new Notification.BigTextStyle().bigText("Crop has been registered successfully for \nFarmer ID - "+farmerID+" \nCrop name - " + cropname + "\nand unique Crop Registration ID is "+crID));
            //    ((NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, builder.build()); // build API 16
            assert mNotificationManager != null;
            notificationManager.notify((int)System.currentTimeMillis(), nbuilder.build());
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}