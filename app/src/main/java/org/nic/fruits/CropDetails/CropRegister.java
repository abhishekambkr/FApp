package org.nic.fruits.CropDetails;

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
import android.widget.ImageButton;
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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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
import org.nic.fruits.pojo.ModelCropDetailFertilizer;
import org.nic.fruits.pojo.ModelCropMaster;
import org.nic.fruits.pojo.ModelCropRegistration;
import org.nic.fruits.pojo.ModelCropSurveyDetails;
import org.nic.fruits.pojo.ModelFertilizerCropMaster;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

public class CropRegister extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Context mContext;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String DEFAULT_NOTIFICATION_CHANNEL_ID = "default" ;
    private static final int CAMERA_REQUEST = 1888; //1888
    private static final int CAMERA_PERMISSION = 100;
    private CropSurveyCardAdapter adapter;
    private String farmerID;
    private String keyValue;
    private AppDatabase appDatabase;

    private String survey_number;
    private String surveynumberfrommap;
    DatePickerDialog pickerSowing,pickerNursery,pickerTransplanting;
    LinearLayout linearlayoutSowingDate,linearLayoutTransplanting,linearLayoutTransplantingib,linearLayoutTransplantingtv;
    TextView tvFarmerID,tvYear,tvSowingDetails,tvOwnerAreaValue,tvSowingDate,tvNurseryDate,tvTransplantingDate,tvPreSowingDetail,tvCropImg;
    TextView tvLatitude,tvLongitude;

    Spinner spinnerSurveyNum,spinnerOwnerNames,spinnerSeason,spinnerSowingDetails,spinnerFertilizer,spinnerManure;
    RadioGroup rgCropDetails;
    RadioButton rbSingleCrop,rbMixedCrop,rbInterCrop;
    RadioGroup rgCultivation;
    RadioButton rbDirectSeeding,rbTransplanting;
    RadioGroup rgFarming;
    RadioButton rbOrganic,rbInOrganic;
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
    Button btnCaptureSingleCropPhoto,btnMap,btnRegister;
    ImageView ivCaptureCropPhoto;
    String[] filterSurveyNumber = new String[0];
    String district = "";
    String taluk = "";
    String village = "";
    private String surveySpinnerValue="",ownernameSpinnerValue="",seasonSpinnerValue="",presowingValue="",fertilizerValue="",manureValue="",cropnameValue="",cropVarietyValue="", farmingvalue= "";
    private String cropType = "";
    private String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    private String yearCode="";

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

    //single crop-details init
    LinearLayout layoutSingleCrop;
    Spinner spinnerCropName,spinnerCropVariety;
    EditText etAcre,etGunta,etFGunta,et_cents,et_ares;

    //mixed crop-details init
    String mixedCrop1Value="",mixedCrop2Value="",mixedCrop3Value="",mixedCrop4Value="";
    String mixedCropVariety1Value="",mixedCropVariety2Value="",mixedCropVariety3Value="",mixedCropVariety4Value="";
    LinearLayout linearlayoutMixedCrop;
    Spinner spinnerMixedCrop1,spinnerMixedCrop2,spinnerMixedCrop3,spinnerMixedCrop4;
    Spinner spinnerMixedCropVariety1,spinnerMixedCropVariety2,spinnerMixedCropVariety3,spinnerMixedCropVariety4;
    EditText etAcreMixedCrop1,etAcreMixedCrop2,etAcreMixedCrop3,etAcreMixedCrop4;
    EditText etGuntaMixedCrop1,etGuntaMixedCrop2,etGuntaMixedCrop3,etGuntaMixedCrop4;
    EditText etFGuntaMixedCrop1,etFGuntaMixedCrop2,etFGuntaMixedCrop3,etFGuntaMixedCrop4;
    List<String> arrayMixedCropNames;
    List<String> arrayMixedCropVariety;
    String mixedCropAcreValue1,mixedCropGuntaValue1,mixedCropFGuntaValue1;
    String mixedCropAcreValue2,mixedCropGuntaValue2,mixedCropFGuntaValue2;
    String mixedCropAcreValue3,mixedCropGuntaValue3,mixedCropFGuntaValue3;
    String mixedCropAcreValue4,mixedCropGuntaValue4,mixedCropFGuntaValue4;

    //inter crop-details init
    String interCrop1Value="",interCrop2Value="",interCrop3Value="",interCrop4Value="";
    String interCropVariety1Value="",interCropVariety2Value="",interCropVariety3Value="",interCropVariety4Value="";
    LinearLayout linearlayoutInterCrop;
    Spinner spinnerInterCrop1,spinnerInterCrop2,spinnerInterCrop3,spinnerInterCrop4;
    Spinner spinnerInterCropVariety1,spinnerInterCropVariety2,spinnerInterCropVariety3,spinnerInterCropVariety4;
    EditText etAcreInterCrop1,etAcreInterCrop2,etAcreInterCrop3,etAcreInterCrop4;
    EditText etGuntaInterCrop1,etGuntaInterCrop2,etGuntaInterCrop3,etGuntaInterCrop4;
    EditText etFGuntaInterCrop1,etFGuntaInterCrop2,etFGuntaInterCrop3,etFGuntaInterCrop4;
    List<String> arrayInterCropNames;
    List<String> arrayInterCropVariety;
    String interCropAcreValue1,interCropGuntaValue1,interCropFGuntaValue1;
    String interCropAcreValue2,interCropGuntaValue2,interCropFGuntaValue2;
    String interCropAcreValue3,interCropGuntaValue3,interCropFGuntaValue3;
    String interCropAcreValue4,interCropGuntaValue4,interCropFGuntaValue4;

    private int mixedCropCounts = 0;
    TableLayout tblCropPick;

    private String mcropnameValue="",mcropVarietyValue="", mcropextent = "0",mcropCode="";
    List<String>mSelectedCrop;
    List<String>mSelectedCropName;
    List<String>mSelectedCropVariety;
    List<String>mSelectedCropExtent;
    List<String>mSelectedCropCode;
    ArrayAdapter<String> mCropAdapter;
  //  TextView mTotalCropExtent;
    String [] splitAcreMCrop = new String[0];
    private int mCropAcre,mCropGunta,mCropFGunta,mCropAres;
    LinearLayout lytTotalMCropExtent;
    RelativeLayout lyRelativeMCrop;

    String acreMixed = "";
    //intercrop
    List<String> arrayInterCropName;
    List<String> arrayInterCropOptions;

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

    ImageButton ibNursery,ibTransplanting;
    int nursingWeek,transplantingWeek;
    int noOfWeek = 0;
    Button btnMCrop1,btnMCrop2,btnMCrop3,btnMCrop4,btnICrop1,btnICrop2,btnICrop3,btnICrop4;
    String cropTypePhoto = "";
    private static final int MY_PERMISSIONS = 29;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_register);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        getSupportActionBar().setTitle(getResources().getString(R.string.crop_register));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS);
            } else {
                Log.d("Home", "Already granted access");
            }
        }

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
        rgCultivation = (RadioGroup) findViewById(R.id.radioGroupCultivation);
        rbSingleCrop = (RadioButton) findViewById(R.id.radioSingleCrop);
        rbMixedCrop = (RadioButton) findViewById(R.id.radioMixedCrop);
        rbInterCrop = (RadioButton) findViewById(R.id.radiointercrop);
        rbOrganic= (RadioButton) findViewById(R.id.radioOrganic);
        rbInOrganic= (RadioButton) findViewById(R.id.radioInOrganic);
        rbDirectSeeding = (RadioButton) findViewById(R.id.radioDirectSeeding);
        rbTransplanting = (RadioButton) findViewById(R.id.radioTransplanting);
        tvSowingDetails=(TextView) findViewById(R.id.tv_Required_crop_grown);
        /*rv_surveynum = (RecyclerView)findViewById(R.id.rv_surveynumber);*/
        linearlayoutSowingDate = (LinearLayout)findViewById(R.id.linearLayoutsowingdate);
        linearLayoutTransplanting = (LinearLayout)findViewById(R.id.linearLayoutTransplanting);
        linearLayoutTransplantingib = (LinearLayout)findViewById(R.id.linearLayoutTransplantingIB);
        linearLayoutTransplantingtv = (LinearLayout)findViewById(R.id.linearLayoutTransplantingtv);
        tvSowingDate=(TextView) findViewById(R.id.txtv_sowingdate);
        tvNurseryDate=(TextView) findViewById(R.id.txtvNurseryDate);
        tvTransplantingDate =(TextView) findViewById(R.id.txtvTransplantingdate);
        ibNursery = (ImageButton) findViewById(R.id.ibNurseryDate);
        ibTransplanting = (ImageButton) findViewById(R.id.ibTransplantingDate);
        ibTransplanting.setVisibility(View.GONE);
        tvTransplantingDate.setVisibility(View.INVISIBLE);
        tvPreSowingDetail=(TextView)findViewById(R.id.tv_Required_crop_grown);
        etAcre=(EditText)findViewById(R.id.et_acre);
        etGunta=(EditText) findViewById(R.id.et_gunta);
        etFGunta=(EditText) findViewById(R.id.et_fgunta);
/*        etCents=(EditText) findViewById(R.id.et_cents);
        et_ares=(EditText) findViewById(R.id.et_ares);*/
        btnCaptureSingleCropPhoto=(Button) findViewById(R.id.btn_crop_Photo_Capture);
        btnRegister = (Button) findViewById(R.id.btn_Register_Crop);
        ivCaptureCropPhoto=(ImageView) findViewById(R.id.iv_capture_image);
        btnMap = (Button) findViewById(R.id.btn_survey_map);
        layoutSingleCrop =(LinearLayout) findViewById(R.id.layoutsinglecrop);

        layoutIrrigationSource = (LinearLayout) findViewById(R.id.layout_irrigationsource);
   //     tblCropPick = (TableLayout) findViewById(R.id.table_mixed_main);

        //Mixedcrop
        linearlayoutMixedCrop= (LinearLayout) findViewById(R.id.layoutmixedcrop);
        spinnerMixedCrop1 = (Spinner) findViewById(R.id.spinnerMixedCrop1);
        spinnerMixedCropVariety1 = (Spinner) findViewById(R.id.spinnerMixedCropVariety1);
        spinnerMixedCrop2 = (Spinner) findViewById(R.id.spinnerMixedCrop2);
        spinnerMixedCropVariety2= (Spinner) findViewById(R.id.spinnerMixedCropVariety2);
        spinnerMixedCrop3 = (Spinner) findViewById(R.id.spinnerMixedCrop3);
        spinnerMixedCropVariety3 = (Spinner) findViewById(R.id.spinnerMixedCropVariety3);
        spinnerMixedCrop4 = (Spinner) findViewById(R.id.spinnerMixedCrop4);
        spinnerMixedCropVariety4 = (Spinner) findViewById(R.id.spinnerMixedCropVariety4);

        etAcreMixedCrop1 = (EditText)findViewById(R.id.et_acre_mixedcrop1);
        etGuntaMixedCrop1 = (EditText)findViewById(R.id.et_gunta_mixedcrop1);
        etFGuntaMixedCrop1 = (EditText)findViewById(R.id.et_fgunta_mixedcrop1);

        etAcreMixedCrop2 = (EditText)findViewById(R.id.et_acre_mixedcrop2);
        etGuntaMixedCrop2 = (EditText)findViewById(R.id.et_gunta_mixedcrop2);
        etFGuntaMixedCrop2 = (EditText)findViewById(R.id.et_fgunta_mixedcrop2);

        etAcreMixedCrop3 = (EditText)findViewById(R.id.et_acre_mixedcrop3);
        etGuntaMixedCrop3 = (EditText)findViewById(R.id.et_gunta_mixedcrop3);
        etFGuntaMixedCrop3 = (EditText)findViewById(R.id.et_fgunta_mixedcrop3);

        etAcreMixedCrop4 = (EditText)findViewById(R.id.et_acre_mixedcrop4);
        etGuntaMixedCrop4 = (EditText)findViewById(R.id.et_gunta_mixedcrop4);
        etFGuntaMixedCrop4 = (EditText)findViewById(R.id.et_fgunta_mixedcrop4);

        btnMCrop1 = (Button) findViewById(R.id.btnCaptureMixedCrop1);
        btnMCrop2 = (Button) findViewById(R.id.btnCaptureMixedCrop2);
        btnMCrop3 = (Button) findViewById(R.id.btnCaptureMixedCrop3);
        btnMCrop4 = (Button) findViewById(R.id.btnCaptureMixedCrop4);
        //Intercrop
        linearlayoutInterCrop= (LinearLayout) findViewById(R.id.layoutintercrop);
        spinnerInterCrop1 = (Spinner) findViewById(R.id.spinnerInterCrop1);
        spinnerInterCropVariety1 = (Spinner) findViewById(R.id.spinnerInterCropVariety1);
        spinnerInterCrop2 = (Spinner) findViewById(R.id.spinnerInterCrop2);
        spinnerInterCropVariety2= (Spinner) findViewById(R.id.spinnerInterCropVariety2);
        spinnerInterCrop3 = (Spinner) findViewById(R.id.spinnerInterCrop3);
        spinnerInterCropVariety3 = (Spinner) findViewById(R.id.spinnerInterCropVariety3);
        spinnerInterCrop4 = (Spinner) findViewById(R.id.spinnerInterCrop4);
        spinnerInterCropVariety4 = (Spinner) findViewById(R.id.spinnerInterCropVariety4);

        etAcreInterCrop1 = (EditText)findViewById(R.id.et_acre_intercrop1);
        etGuntaInterCrop1 = (EditText)findViewById(R.id.et_gunta_intercrop1);
        etFGuntaInterCrop1 = (EditText)findViewById(R.id.et_fgunta_intercrop1);

        etAcreInterCrop2 = (EditText)findViewById(R.id.et_acre_intercrop2);
        etGuntaInterCrop2 = (EditText)findViewById(R.id.et_gunta_intercrop2);
        etFGuntaInterCrop2 = (EditText)findViewById(R.id.et_fgunta_intercrop2);

        etAcreInterCrop3 = (EditText)findViewById(R.id.et_acre_intercrop3);
        etGuntaInterCrop3 = (EditText)findViewById(R.id.et_gunta_intercrop3);
        etFGuntaInterCrop3 = (EditText)findViewById(R.id.et_fgunta_intercrop3);

        etAcreInterCrop4 = (EditText)findViewById(R.id.et_acre_intercrop4);
        etGuntaInterCrop4 = (EditText)findViewById(R.id.et_gunta_intercrop4);
        etFGuntaInterCrop4 = (EditText)findViewById(R.id.et_fgunta_intercrop4);

        btnICrop1 = (Button) findViewById(R.id.btnCaptureInterCrop1);
        btnICrop2 = (Button) findViewById(R.id.btnCaptureInterCrop2);
        btnICrop3 = (Button) findViewById(R.id.btnCaptureInterCrop3);
        btnICrop4 = (Button) findViewById(R.id.btnCaptureInterCrop4);

    //    mTotalCropExtent = (TextView) findViewById(R.id.tv_total_mcrop_extent);
    //    lytTotalMCropExtent = (LinearLayout) findViewById(R.id.layouttotalmcropextent);
     //   lyRelativeMCrop = (RelativeLayout) findViewById(R.id.RelativeLayoutMixedCrops);


   //     lytTotalInterCropExtent = (LinearLayout) findViewById(R.id.layouttotalicropextent);
     //   tblInterCropPick = (TableLayout) findViewById(R.id.table_inter_main);
  //      interTotalCropExtent = (TextView) findViewById(R.id.tv_total_icrop_extent);
   //     lyRelativeInterCrop = (RelativeLayout) findViewById(R.id.RelativeLayoutInterCrops);

        /*tableLayoutCropDetails = (TableLayout) findViewById(R.id.tableLayoutCropsDetails);
        tableCropDetails = (TableLayout) findViewById(R.id.tableCropDetails);*/
//        tableCropDetails.removeAllViews();



        spinnerSowingDetails.setVisibility(View.GONE);
        spinnerFertilizer.setVisibility(View.GONE);
        spinnerManure.setVisibility(View.GONE);

        //mixed crop-details
        //arrayMixedCropOptions = new ArrayList<>();


        //inter crop-details
        arrayInterCropOptions = new ArrayList<>();


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
      //  arrayMCropName = new ArrayList<>();
        arrayInterCropName = new ArrayList<>();
        arrayIrrigationType = new ArrayList<>();
        arrayIrrigationSource = new ArrayList<>();

        arrayMixedCropNames = new ArrayList<>();
        arrayMixedCropVariety = new ArrayList<>();
        arrayInterCropNames = new ArrayList<>();
        arrayInterCropVariety = new ArrayList<>();

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

        layoutSingleCrop.setVisibility(View.GONE);
        linearlayoutMixedCrop.setVisibility(View.GONE);
        linearlayoutInterCrop.setVisibility(View.GONE);
        rgCropDetails.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                checkedId = radioGroup.getCheckedRadioButtonId();

                switch (checkedId) {
                    case R.id.radioSingleCrop:
                        singleCrop = true;
                        mixedCrop = false;
                        interCrop = false;
                        layoutSingleCrop.setVisibility(View.VISIBLE);
                        linearlayoutMixedCrop.setVisibility(View.GONE);
                        linearlayoutInterCrop.setVisibility(View.GONE);
                        totalCrops = "1";
                        cropType = rbSingleCrop.getText().toString();
                        arrayAllCropsEng.clear();
                        arrayCropVariety.clear();
                        setsinglecropViews();
                        break;

                    case R.id.radioMixedCrop:
                        mixedCrop = true;
                        singleCrop = false;
                        interCrop = false;
                        layoutSingleCrop.setVisibility(View.GONE);
                        linearlayoutMixedCrop.setVisibility(View.VISIBLE);
                        linearlayoutInterCrop.setVisibility(View.GONE);
                        cropType = rbMixedCrop.getText().toString();
                        arrayMixedCropNames.clear();
                        arrayMixedCropVariety.clear();
                        setMixedCropViews();
                        break;

                    case R.id.radiointercrop:
                        interCrop = true;
                        mixedCrop = false;
                        singleCrop = false;
                        layoutSingleCrop.setVisibility(View.GONE);
                        linearlayoutMixedCrop.setVisibility(View.GONE);
                        linearlayoutInterCrop.setVisibility(View.VISIBLE);
                        cropType = rbInterCrop.getText().toString();
                        arrayInterCropNames.clear();
                        arrayInterCropVariety.clear();
                        setInterCropViews();
                        break;
                }

            }
        });
        linearlayoutSowingDate.setVisibility(View.GONE);
        linearLayoutTransplanting.setVisibility(View.GONE);
        linearLayoutTransplantingib.setVisibility(View.GONE);
        linearLayoutTransplantingtv.setVisibility(View.GONE);

        rgCultivation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int cultivationId) {

                cultivationId = radioGroup.getCheckedRadioButtonId();

                switch (cultivationId) {
                    case R.id.radioDirectSeeding:
                        linearlayoutSowingDate.setVisibility(View.VISIBLE);
                        linearLayoutTransplanting.setVisibility(View.GONE);
                        linearLayoutTransplantingib.setVisibility(View.GONE);
                        linearLayoutTransplantingtv.setVisibility(View.GONE);
                        break;

                    case R.id.radioTransplanting:
                        linearlayoutSowingDate.setVisibility(View.GONE);
                        linearLayoutTransplanting.setVisibility(View.VISIBLE);
                        linearLayoutTransplantingib.setVisibility(View.VISIBLE);
                        linearLayoutTransplantingtv.setVisibility(View.VISIBLE);
                        break;

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
                pickerSowing = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvSowingDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                pickerSowing.getDatePicker().setMaxDate(System.currentTimeMillis());
                pickerSowing.show();
            }
        });

        tvNurseryDate.setInputType(InputType.TYPE_NULL);

        ibNursery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                nursingWeek = cldr.get(Calendar.WEEK_OF_YEAR);

                // date picker dialog
                pickerNursery = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvNurseryDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                tvTransplantingDate.setInputType(InputType.TYPE_NULL);
                                noOfWeek = 0;
                                ibTransplanting.setVisibility(View.VISIBLE);
                                tvTransplantingDate.setVisibility(View.VISIBLE);
                                tvTransplantingDate.setText("");
                            }
                        }, year, month, day);
                pickerNursery.getDatePicker().setMaxDate(System.currentTimeMillis());
                pickerNursery.show();
            }
        });

        tvTransplantingDate.setInputType(InputType.TYPE_NULL);

        ibTransplanting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                noOfWeek = 0;
                transplantingWeek = cldr.get(Calendar.WEEK_OF_YEAR);
                // date picker
                pickerTransplanting = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");
                        Date date1 = new Date();
                        Date date2 = new Date();

                        try {
                            date1 = dates.parse(tvNurseryDate.getText().toString());
                            date2 = dates.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        noOfWeek = calculateWeekNo(date1, date2);
                        System.out.println("noOfWeek " + noOfWeek + " transplantingWeek " + transplantingWeek + " nursingWeek " +nursingWeek);

                        if(noOfWeek>=4) {

                            tvTransplantingDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }else{
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                            alertDialog.setTitle("Warning :");
                            alertDialog.setMessage("Nursery preparation and Transplanting should be a gap of 4 weeks");
                            alertDialog.setCancelable(false);
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    tvNurseryDate.setInputType(InputType.TYPE_NULL);
                                    tvTransplantingDate.setInputType(InputType.TYPE_NULL);
                                    tvNurseryDate.setText("");
                                    tvTransplantingDate.setText("");
                                    noOfWeek = 0;
                                    ibTransplanting.setVisibility(View.GONE);
                                    tvTransplantingDate.setVisibility(View.INVISIBLE);
                                } });
                            alertDialog.show();
                        }

                      /*  int calculateWeek = 0;
                        calculateWeek = option1(date1,date2);
                        System.out.println("calculateWeek " + calculateWeek + " transplantingWeek " + transplantingWeek + " nursingWeek " +nursingWeek);
                        if(calculateWeek>=4) {

                            tvTransplantingDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }else{
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                            alertDialog.setTitle("Warning :");
                            alertDialog.setMessage("Nursery preparation and Transplanting should be gap of 4 weeks");
                            alertDialog .setCancelable(false);
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                   // tvTransplantingDate.setText("Transplanting Date");
                                } });
                            alertDialog.show();
                        } */
                    }
                }, year, month, day);
                pickerTransplanting.getDatePicker().setMaxDate(System.currentTimeMillis());
                pickerTransplanting.show();
            }
        });

        btnCaptureSingleCropPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropTypePhoto = "SingleCrop";
                camera(cropTypePhoto);
            }
        });

        btnMCrop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropTypePhoto = "MixedCrop1";
                camera(cropTypePhoto);
            }
        });
        btnMCrop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropTypePhoto = "MixedCrop2";
                camera(cropTypePhoto);
            }
        });
        btnMCrop3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropTypePhoto = "MixedCrop3";
                camera(cropTypePhoto);
            }
        });
        btnMCrop4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropTypePhoto = "MixedCrop4";
                camera(cropTypePhoto);
            }
        });

        btnICrop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropTypePhoto = "InterCrop1";
                camera(cropTypePhoto);
            }
        });
        btnICrop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropTypePhoto = "InterCrop2";
                camera(cropTypePhoto);
            }
        });
        btnICrop3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropTypePhoto = "InterCrop3";
                camera(cropTypePhoto);
            }
        });
        btnICrop4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropTypePhoto = "InterCrop4";
                camera(cropTypePhoto);
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map();
            }
        });

        String root = Environment.getExternalStorageDirectory().toString();

        appMediaFolderImagesEnc = new File(root + "/Fruits/" + yearCode + "/" + seasonCodeValue + "/" + farmerID +"/Image");

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

  /*  public static int calculateWeekNo(Date start, Date end) {
        Calendar a = new GregorianCalendar();
        Calendar b = new GregorianCalendar();
        a.setTime(start);
        b.setTime(end);
        return b.get(Calendar.WEEK_OF_YEAR) - a.get(Calendar.WEEK_OF_YEAR) + 1;
    }*/

    public static int calculateWeekNo(Date start, Date end) {

        Calendar cal = new GregorianCalendar();
        cal.setTime(start);

        int weeks = 0;
        while (cal.getTime().before(end)) {
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            weeks++;
        }
        return weeks;
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

        if(cropType.equals("SingleCrop")) {
            if (cropnameValue.equals("")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Select a valid SingleCrop");
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
        /*    if (etFGunta.getText().toString().equals("")) { //23/12/2021
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
            }*/
        }
        else if(cropType.equals("MixedCrop")){
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
        else if(cropType.equals("InterCrop")){
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
                        .setMessage(getResources().getString(R.string.cropdataexists))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        })
                        .show();
            }
            else {
                Toast.makeText(mContext, "Issue occured while saving crop details, please try in sometime", Toast.LENGTH_SHORT).show();
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


        if(cropType.equals("SingleCrop")) {

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
        else if(cropType.equals("MixedCrop")){
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
        else if(cropType.equals("InterCrop")) {
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

    private void camera(String croptype) {
        try{

            String root = Environment.getExternalStorageDirectory().toString();
            appMediaFolderImagesEnc = new File(root + "/Fruits/" + yearCode + "/" + seasonCodeValue + "/" + farmerID +"/Image");
            if (!appMediaFolderImagesEnc.exists()) {
                appMediaFolderImagesEnc.mkdirs();
            }
//cropType
            if(croptype.equals("SingleCrop")) {
                imageName = farmerID+"_"+yearCode+"_"+seasonCodeValue+"_SingleCrop_"+cropnameValue;
            }else if(croptype.equals("MixedCrop1")){
                imageName = farmerID+"_"+yearCode+"_"+seasonCodeValue+"_MixedCrop1_"+mixedCrop1Value;
            }else if(croptype.equals("MixedCrop2")){
                imageName = farmerID+"_"+yearCode+"_"+seasonCodeValue+"_MixedCrop2_"+mixedCrop2Value;
            }else if(croptype.equals("MixedCrop3")){
                imageName = farmerID+"_"+yearCode+"_"+seasonCodeValue+"_MixedCrop3_"+mixedCrop3Value;
            }else if(croptype.equals("MixedCrop4")){
                imageName = farmerID+"_"+yearCode+"_"+seasonCodeValue+"_MixedCrop4_"+mixedCrop4Value;
            }else if(croptype.equals("InterCrop1")){
                imageName = farmerID+"_"+yearCode+"_"+seasonCodeValue+"_InterCrop1_"+interCrop1Value;
            }else if(croptype.equals("InterCrop2")){
                imageName = farmerID+"_"+yearCode+"_"+seasonCodeValue+"_InterCrop2_"+interCrop2Value;
            }else if(croptype.equals("InterCrop3")){
                imageName = farmerID+"_"+yearCode+"_"+seasonCodeValue+"_InterCrop3_"+interCrop3Value;
            }else if(croptype.equals("InterCrop4")){
                imageName = farmerID+"_"+yearCode+"_"+seasonCodeValue+"_InterCrop4_"+interCrop4Value;
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

        tvYear.setText(currentFinancialYear);

        //For Season
        ArrayAdapter<String> season_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arraySeasonList);
        season_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arraySeasonList.add(1,"Summer : January 1st to March 31st");
        arraySeasonList.add(2,"Rabi : September 1st to December 31st");
        arraySeasonList.add(3,"Kharif : April 1st to August 30th");

        season_adapter.notifyDataSetChanged();
        spinnerSeason.setAdapter(season_adapter);
        spinnerSeason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    seasonSpinnerValue = spinnerSeason.getSelectedItem().toString();
                    if(spinnerSeason.getSelectedItem().toString().equals("Summer : January 1st to March 31st")){
                        seasonCodeValue = "1";
                    }else if(spinnerSeason.getSelectedItem().toString().equals("Rabi : September 1st to December 31st")){
                        seasonCodeValue = "2";
                    }else if(spinnerSeason.getSelectedItem().toString().equals("Kharif : April 1st to August 30th")){
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
        arraySowingDetails.add(1,"Farm Yard Manure");
        arraySowingDetails.add(2,"Green Leaf Manure");
        arraySowingDetails.add(3,"Vermicompost");
        arraySowingDetails.add(4,"Oil Cakes");
        arraySowingDetails.add(5,"Sheep & Goat Manure");
        arraySowingDetails.add(6,"Poultry Manure");
        arraySowingDetails.add(7,"Night Soil");
        arraySowingDetails.add(8,"Animal based concentrated organic Manure");
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
                    spinnerFertilizer.setSelection(0);
                    fertilizerValue="";

                }else{
                    spinnerSowingDetails.setSelection(0);
                    presowingValue="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //fertilizer
        arrayFertilizer.add(0,"Select Fertilizer");
        ArrayAdapter<String> adaptercropdetailfertilizer = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayFertilizer);
        adaptercropdetailfertilizer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        MainViewModel viewmodelcropdetailfertilizer = ViewModelProviders.of(this).get(MainViewModel.class);
        viewmodelcropdetailfertilizer.getCropDetailsfertilizers().observe(this, new Observer<List<ModelCropDetailFertilizer>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropDetailFertilizer> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()){
                    for(ModelCropDetailFertilizer taskEntry:taskEntries){
                        if(!arrayFertilizer.contains(taskEntry.getFertilizername())){
                            arrayFertilizer.add(taskEntry.getFertilizername());
                            Set<String> lstfertilizeraddons = new LinkedHashSet<String>(arrayFertilizer);
                            arrayFertilizer.clear();
                            arrayFertilizer.addAll(lstfertilizeraddons);

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
                                    Intent mainActivity = new Intent(mContext, CropRegister.class);
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainActivity);
                                    finish();
                                }
                            })
                            .show();
                }

                //spinner
                adaptercropdetailfertilizer.notifyDataSetChanged();
                spinnerFertilizer.setAdapter(adaptercropdetailfertilizer);
                spinnerFertilizer.setSelection(0);
                spinnerFertilizer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            fertilizerValue = spinnerFertilizer.getSelectedItem().toString();
                            spinnerSowingDetails.setSelection(0);
                            presowingValue = "";


                        }else{
                            spinnerFertilizer.setSelection(0);
                            fertilizerValue="";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
          }

        });


    }

    private void irrigationdetails() {

        arrayIrrigationType.add(0,"Select Irrigation Type");
        arrayIrrigationType.add(1,"Irrigated");
        arrayIrrigationType.add(2,"Rainfed");
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
                    if(spinnerIrrigationType.getSelectedItem().toString().equals("Rainfed")){
                        irrigationSource = "Rainfed";
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

    private void setsinglecropViews(){

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
                }
                else{
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

                            spinnerMixedCrop1.setSelection(0);
                            mixedCrop1Value = "";
                            spinnerMixedCropVariety1.setSelection(0);
                            mixedCropVariety1Value = "";
                            spinnerMixedCrop2.setSelection(0);
                            mixedCrop2Value = "";
                            spinnerMixedCropVariety2.setSelection(0);
                            mixedCropVariety2Value = "";
                            spinnerMixedCrop3.setSelection(0);
                            mixedCrop3Value = "";
                            spinnerMixedCropVariety3.setSelection(0);
                            mixedCropVariety3Value = "";
                            spinnerMixedCrop4.setSelection(0);
                            mixedCrop4Value = "";
                            spinnerMixedCropVariety4.setSelection(0);
                            mixedCropVariety4Value = "";

                            spinnerInterCrop1.setSelection(0);
                            interCrop1Value = "";
                            spinnerInterCropVariety1.setSelection(0);
                            interCropVariety1Value = "";
                            spinnerInterCrop2.setSelection(0);
                            interCrop2Value = "";
                            spinnerInterCropVariety2.setSelection(0);
                            interCropVariety2Value = "";
                            spinnerInterCrop3.setSelection(0);
                            interCrop3Value = "";
                            spinnerInterCropVariety3.setSelection(0);
                            interCropVariety3Value = "";
                            spinnerInterCrop4.setSelection(0);
                            interCrop4Value = "";
                            spinnerInterCropVariety4.setSelection(0);
                            interCropVariety4Value = "";
                        }else{
                            spinnerCropName.setSelection(0);
                            cropnameValue="";
                            cropCode="";
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });

        arrayCropVariety.add(0,"Select Crop Variety");
        arrayCropVariety.add(1,"Hybrid");
        arrayCropVariety.add(2,"Local");
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
    }

    private void setMixedCropViews() {
        //mixed crop names
        arrayMixedCropNames.add(0,"Select Mixed Crops");
        ArrayAdapter<String> mixedcrop1adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayMixedCropNames);
        mixedcrop1adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mixedcrop1adapter.notifyDataSetChanged();
        MainViewModel viewMixedCrop1Names = ViewModelProviders.of(this).get(MainViewModel.class);
        viewMixedCrop1Names.getMixedCrops("S").observe(this, new Observer<List<ModelFertilizerCropMaster>>() {
            @Override
            public void onChanged(@Nullable List<ModelFertilizerCropMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    for(ModelFertilizerCropMaster taskEntry:taskEntries){

                        arrayMixedCropNames.add(taskEntry.getCropname_eng());
                        //      array_allcrops_kn.add(taskEntry.getCropname_kn());
                    }
                }
                else{
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
                mixedcrop1adapter.notifyDataSetChanged();
                spinnerMixedCrop1.setAdapter(mixedcrop1adapter);
                spinnerMixedCrop1.setSelection(0);
                spinnerMixedCrop1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            mixedCrop1Value = spinnerMixedCrop1.getSelectedItem().toString();
                           // cropCode = String.valueOf(i);
                            spinnerCropName.setSelection(0);
                            cropnameValue = "";
                            cropCode = "";
                            spinnerInterCrop1.setSelection(0);
                            interCrop1Value = "";
                            spinnerInterCropVariety1.setSelection(0);
                            interCropVariety1Value = "";
                            spinnerInterCrop2.setSelection(0);
                            interCrop2Value = "";
                            spinnerInterCropVariety2.setSelection(0);
                            interCropVariety2Value = "";
                            spinnerInterCrop3.setSelection(0);
                            interCrop3Value = "";
                            spinnerInterCropVariety3.setSelection(0);
                            interCropVariety3Value = "";
                            spinnerInterCrop4.setSelection(0);
                            interCrop4Value = "";
                            spinnerInterCropVariety4.setSelection(0);
                            interCropVariety4Value = "";
                        }else{
                            spinnerMixedCrop1.setSelection(0);
                            mixedCrop1Value="";
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });

        ArrayAdapter<String> mixedcrop2adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayMixedCropNames);
        mixedcrop2adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mixedcrop2adapter.notifyDataSetChanged();
        MainViewModel viewMixedCrop2Names = ViewModelProviders.of(this).get(MainViewModel.class);
        viewMixedCrop2Names.getMixedCrops("S").observe(this, new Observer<List<ModelFertilizerCropMaster>>() {
            @Override
            public void onChanged(@Nullable List<ModelFertilizerCropMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    for(ModelFertilizerCropMaster taskEntry:taskEntries){
                        //  arrayOwnerNames.add(0,"Select Owner");
                        arrayMixedCropNames.add(taskEntry.getCropname_eng());
                        //      array_allcrops_kn.add(taskEntry.getCropname_kn());

                    }
                }
                else{
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
                mixedcrop2adapter.notifyDataSetChanged();
                spinnerMixedCrop2.setAdapter(mixedcrop2adapter);
                spinnerMixedCrop2.setSelection(0);
                spinnerMixedCrop2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            mixedCrop2Value = spinnerMixedCrop2.getSelectedItem().toString();
                            // cropCode = String.valueOf(i);
                            spinnerCropName.setSelection(0);
                            cropnameValue = "";
                            cropCode = "";
                            spinnerInterCrop1.setSelection(0);
                            interCrop1Value = "";
                            spinnerInterCropVariety1.setSelection(0);
                            interCropVariety1Value = "";
                            spinnerInterCrop2.setSelection(0);
                            interCrop2Value = "";
                            spinnerInterCropVariety2.setSelection(0);
                            interCropVariety2Value = "";
                            spinnerInterCrop3.setSelection(0);
                            interCrop3Value = "";
                            spinnerInterCropVariety3.setSelection(0);
                            interCropVariety3Value = "";
                            spinnerInterCrop4.setSelection(0);
                            interCrop4Value = "";
                            spinnerInterCropVariety4.setSelection(0);
                            interCropVariety4Value = "";
                        }else{
                            spinnerMixedCrop2.setSelection(0);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });

        ArrayAdapter<String> mixedcrop3adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayMixedCropNames);
        mixedcrop3adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mixedcrop3adapter.notifyDataSetChanged();
        MainViewModel viewMixedCrop3Names = ViewModelProviders.of(this).get(MainViewModel.class);
        viewMixedCrop3Names.getMixedCrops("S").observe(this, new Observer<List<ModelFertilizerCropMaster>>() {
            @Override
            public void onChanged(@Nullable List<ModelFertilizerCropMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    for(ModelFertilizerCropMaster taskEntry:taskEntries){
                        //  arrayOwnerNames.add(0,"Select Owner");
                        arrayMixedCropNames.add(taskEntry.getCropname_eng());
                        //      array_allcrops_kn.add(taskEntry.getCropname_kn());


                    }
                }
                else{
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
                mixedcrop3adapter.notifyDataSetChanged();
                spinnerMixedCrop3.setAdapter(mixedcrop3adapter);
                spinnerMixedCrop3.setSelection(0);
                spinnerMixedCrop3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            mixedCrop3Value = spinnerMixedCrop3.getSelectedItem().toString();
                            // cropCode = String.valueOf(i);
                            spinnerCropName.setSelection(0);
                            cropnameValue = "";
                            cropCode = "";
                            spinnerInterCrop1.setSelection(0);
                            interCrop1Value = "";
                            spinnerInterCropVariety1.setSelection(0);
                            interCropVariety1Value = "";
                            spinnerInterCrop2.setSelection(0);
                            interCrop2Value = "";
                            spinnerInterCropVariety2.setSelection(0);
                            interCropVariety2Value = "";
                            spinnerInterCrop3.setSelection(0);
                            interCrop3Value = "";
                            spinnerInterCropVariety3.setSelection(0);
                            interCropVariety3Value = "";
                            spinnerInterCrop4.setSelection(0);
                            interCrop4Value = "";
                            spinnerInterCropVariety4.setSelection(0);
                            interCropVariety4Value = "";
                        }else{
                            spinnerMixedCrop3.setSelection(0);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });

        ArrayAdapter<String> mixedcrop4adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayMixedCropNames);
        mixedcrop4adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mixedcrop4adapter.notifyDataSetChanged();
        MainViewModel viewMixedCrop4Names = ViewModelProviders.of(this).get(MainViewModel.class);
        viewMixedCrop4Names.getMixedCrops("S").observe(this, new Observer<List<ModelFertilizerCropMaster>>() {
            @Override
            public void onChanged(@Nullable List<ModelFertilizerCropMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    for(ModelFertilizerCropMaster taskEntry:taskEntries){
                        arrayMixedCropNames.add(taskEntry.getCropname_eng());
                        //      array_allcrops_kn.add(taskEntry.getCropname_kn());
                    }
                }
                else{
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
                mixedcrop4adapter.notifyDataSetChanged();
                spinnerMixedCrop4.setAdapter(mixedcrop4adapter);
                spinnerMixedCrop4.setSelection(0);
                spinnerMixedCrop4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            mixedCrop4Value = spinnerMixedCrop4.getSelectedItem().toString();
                            // cropCode = String.valueOf(i);
                            spinnerCropName.setSelection(0);
                            cropnameValue = "";
                            cropCode = "";
                            spinnerInterCrop1.setSelection(0);
                            interCrop1Value = "";
                            spinnerInterCropVariety1.setSelection(0);
                            interCropVariety1Value = "";
                            spinnerInterCrop2.setSelection(0);
                            interCrop2Value = "";
                            spinnerInterCropVariety2.setSelection(0);
                            interCropVariety2Value = "";
                            spinnerInterCrop3.setSelection(0);
                            interCrop3Value = "";
                            spinnerInterCropVariety3.setSelection(0);
                            interCropVariety3Value = "";
                            spinnerInterCrop4.setSelection(0);
                            interCrop4Value = "";
                            spinnerInterCropVariety4.setSelection(0);
                            interCropVariety4Value = "";
                        }else{
                            spinnerMixedCrop4.setSelection(0);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });

        //mixed crop variety
        arrayMixedCropVariety.add(0,"Select Crop Variety");
        arrayMixedCropVariety.add(1,"Hybrid");
        arrayMixedCropVariety.add(2,"Local");

        ArrayAdapter<String> adapterMixedCrop1Variety = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayMixedCropVariety);
        adapterMixedCrop1Variety.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMixedCrop1Variety.notifyDataSetChanged();
        spinnerMixedCropVariety1.setAdapter(adapterMixedCrop1Variety);
        spinnerMixedCropVariety1.setSelection(0);
        spinnerMixedCropVariety1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    mixedCropVariety1Value = spinnerMixedCropVariety1.getSelectedItem().toString();

                    spinnerCropName.setSelection(0);
                    cropnameValue = "";
                    cropCode = "";
                    spinnerInterCrop1.setSelection(0);
                    interCrop1Value = "";
                    spinnerInterCropVariety1.setSelection(0);
                    interCropVariety1Value = "";
                    spinnerInterCrop2.setSelection(0);
                    interCrop2Value = "";
                    spinnerInterCropVariety2.setSelection(0);
                    interCropVariety2Value = "";
                    spinnerInterCrop3.setSelection(0);
                    interCrop3Value = "";
                    spinnerInterCropVariety3.setSelection(0);
                    interCropVariety3Value = "";
                    spinnerInterCrop4.setSelection(0);
                    interCrop4Value = "";
                    spinnerInterCropVariety4.setSelection(0);
                    interCropVariety4Value = "";
                }else{
                    spinnerMixedCropVariety1.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapterMixedCrop2Variety = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayMixedCropVariety);
        adapterMixedCrop2Variety.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMixedCrop2Variety.notifyDataSetChanged();
        spinnerMixedCropVariety2.setAdapter(adapterMixedCrop2Variety);
        spinnerMixedCropVariety2.setSelection(0);
        spinnerMixedCropVariety2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    mixedCropVariety2Value = spinnerMixedCropVariety2.getSelectedItem().toString();
                }else{
                    spinnerMixedCropVariety2.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapterMixedCrop3Variety = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayMixedCropVariety);
        adapterMixedCrop3Variety.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMixedCrop3Variety.notifyDataSetChanged();
        spinnerMixedCropVariety3.setAdapter(adapterMixedCrop3Variety);
        spinnerMixedCropVariety3.setSelection(0);
        spinnerMixedCropVariety3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    mixedCropVariety3Value = spinnerMixedCropVariety3.getSelectedItem().toString();
                    spinnerCropName.setSelection(0);
                    cropnameValue = "";
                    cropCode = "";
                    spinnerInterCrop1.setSelection(0);
                    interCrop1Value = "";
                    spinnerInterCropVariety1.setSelection(0);
                    interCropVariety1Value = "";
                    spinnerInterCrop2.setSelection(0);
                    interCrop2Value = "";
                    spinnerInterCropVariety2.setSelection(0);
                    interCropVariety2Value = "";
                    spinnerInterCrop3.setSelection(0);
                    interCrop3Value = "";
                    spinnerInterCropVariety3.setSelection(0);
                    interCropVariety3Value = "";
                    spinnerInterCrop4.setSelection(0);
                    interCrop4Value = "";
                    spinnerInterCropVariety4.setSelection(0);
                    interCropVariety4Value = "";
                }else{
                    spinnerMixedCropVariety3.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapterMixedCrop4Variety = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayMixedCropVariety);
        adapterMixedCrop4Variety.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMixedCrop4Variety.notifyDataSetChanged();
        spinnerMixedCropVariety4.setAdapter(adapterMixedCrop4Variety);
        spinnerMixedCropVariety4.setSelection(0);
        spinnerMixedCropVariety4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    mixedCropVariety4Value = spinnerMixedCropVariety4.getSelectedItem().toString();
                    spinnerCropName.setSelection(0);
                    cropnameValue = "";
                    cropCode = "";
                    spinnerInterCrop1.setSelection(0);
                    interCrop1Value = "";
                    spinnerInterCropVariety1.setSelection(0);
                    interCropVariety1Value = "";
                    spinnerInterCrop2.setSelection(0);
                    interCrop2Value = "";
                    spinnerInterCropVariety2.setSelection(0);
                    interCropVariety2Value = "";
                    spinnerInterCrop3.setSelection(0);
                    interCrop3Value = "";
                    spinnerInterCropVariety3.setSelection(0);
                    interCropVariety3Value = "";
                    spinnerInterCrop4.setSelection(0);
                    interCrop4Value = "";
                    spinnerInterCropVariety4.setSelection(0);
                    interCropVariety4Value = "";
                }else{
                    spinnerMixedCropVariety4.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //mixed crop1 acre,gunta,fgunta
        etAcreMixedCrop1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(area!=null){
                    try {
                        if (Integer.parseInt(etAcreMixedCrop1.getText().toString()) <= Integer.parseInt(mixedCropAcreValue1.trim())) {
                            etGuntaMixedCrop1.setText("");
                            etFGuntaMixedCrop1.setText("");
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
                            etAcreMixedCrop1.setText("");
                            etAcreMixedCrop1.requestFocus();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("ಸೂಚನೆ :");
                    alertDialog.setMessage("ಸರಿಯಾದ ಸಮೀಕ್ಷೆ ಸಂಖ್ಯೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ");
                    alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

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
        etGuntaMixedCrop1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (etGuntaMixedCrop1.getText().toString().length() > 1) {
                        if (Integer.parseInt(etGuntaMixedCrop1.getText().toString()) < 40) {
                            if (Integer.parseInt(etGuntaMixedCrop1.getText().toString()) <= Integer.parseInt(mixedCropGuntaValue1.trim())) {

                                etFGuntaMixedCrop1.setText("");
                                    /*et_cents.setText("");
                                    et_ares.setText("");*/
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                                alertDialog.setTitle("ಸೂಚನೆ :");
                                alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                                alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        etFGuntaMixedCrop1.setText("");
                                           /* et_cents.setText("");
                                            et_ares.setText("");*/
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setCanceledOnTouchOutside(false);
                                etGuntaMixedCrop1.setText("");
                                etGuntaMixedCrop1.requestFocus();

                                etFGuntaMixedCrop1.setText("");
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
                                    etFGuntaMixedCrop1.setText("");
                                        /*et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaMixedCrop1.setText("");
                            etGuntaMixedCrop1.requestFocus();

                            etFGuntaMixedCrop1.setText("");
                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                        }
                    } else {
                        if (Integer.parseInt(etFGuntaMixedCrop1.getText().toString()) < 100) {

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    etFGuntaMixedCrop1.setText("");
                                       /* et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaMixedCrop1.setText("");
                            etGuntaMixedCrop1.requestFocus();

                            etFGuntaMixedCrop1.setText("");
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
        etFGuntaMixedCrop1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    //   if (Integer.parseInt(et_fgunta.getText().toString()) >= 0) {

                    if (Integer.parseInt(etFGuntaMixedCrop1.getText().toString()) < 100) {
                        if (Integer.parseInt(etFGuntaMixedCrop1.getText().toString()) <= Integer.parseInt(mixedCropFGuntaValue1.trim())){

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
                            etFGuntaMixedCrop1.setText("");
                            etFGuntaMixedCrop1.requestFocus();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                        alertDialog.setTitle("ಸೂಚನೆ :");
                        alertDialog.setMessage("ಸರಿಯಾದ fಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                        alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                            }
                        });
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        etFGuntaMixedCrop1.setText("");
                        etFGuntaMixedCrop1.requestFocus();

                       /* et_cents.setText("");
                        et_ares.setText("");*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //mixed crop2 acre,gunta,fgunta
        etAcreMixedCrop2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(area!=null){
                    try {
                        if (Integer.parseInt(etAcreMixedCrop2.getText().toString()) <= Integer.parseInt(mixedCropAcreValue2.trim())) {
                            etGuntaMixedCrop2.setText("");
                            etFGuntaMixedCrop2.setText("");
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
                            etAcreMixedCrop2.setText("");
                            etAcreMixedCrop2.requestFocus();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("ಸೂಚನೆ :");
                    alertDialog.setMessage("ಸರಿಯಾದ ಸಮೀಕ್ಷೆ ಸಂಖ್ಯೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ");
                    alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

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
        etGuntaMixedCrop2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (etGuntaMixedCrop2.getText().toString().length() > 1) {
                        if (Integer.parseInt(etGuntaMixedCrop2.getText().toString()) < 40) {
                            if (Integer.parseInt(etGuntaMixedCrop2.getText().toString()) <= Integer.parseInt(mixedCropGuntaValue2.trim())) {

                                etFGuntaMixedCrop2.setText("");
                                    /*et_cents.setText("");
                                    et_ares.setText("");*/
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                                alertDialog.setTitle("ಸೂಚನೆ :");
                                alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                                alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        etFGuntaMixedCrop2.setText("");
                                           /* et_cents.setText("");
                                            et_ares.setText("");*/
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setCanceledOnTouchOutside(false);
                                etGuntaMixedCrop2.setText("");
                                etGuntaMixedCrop2.requestFocus();

                                etFGuntaMixedCrop2.setText("");
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
                                    etFGuntaMixedCrop2.setText("");
                                        /*et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaMixedCrop2.setText("");
                            etGuntaMixedCrop2.requestFocus();

                            etFGuntaMixedCrop2.setText("");
                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                        }
                    } else {
                        if (Integer.parseInt(etFGuntaMixedCrop2.getText().toString()) < 100) {

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    etFGuntaMixedCrop2.setText("");
                                       /* et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaMixedCrop2.setText("");
                            etGuntaMixedCrop2.requestFocus();

                            etFGuntaMixedCrop2.setText("");
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
        etFGuntaMixedCrop2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    //   if (Integer.parseInt(et_fgunta.getText().toString()) >= 0) {

                    if (Integer.parseInt(etFGuntaMixedCrop2.getText().toString()) < 100) {
                        if (Integer.parseInt(etFGuntaMixedCrop2.getText().toString()) <= Integer.parseInt(mixedCropFGuntaValue2.trim())){

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
                            etFGuntaMixedCrop2.setText("");
                            etFGuntaMixedCrop2.requestFocus();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                        alertDialog.setTitle("ಸೂಚನೆ :");
                        alertDialog.setMessage("ಸರಿಯಾದ fಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                        alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                            }
                        });
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        etFGuntaMixedCrop2.setText("");
                        etFGuntaMixedCrop2.requestFocus();

                       /* et_cents.setText("");
                        et_ares.setText("");*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //mixed crop3 acre,gunta,fgunta
        etAcreMixedCrop3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(area!=null){
                    try {
                        if (Integer.parseInt(etAcreMixedCrop3.getText().toString()) <= Integer.parseInt(mixedCropAcreValue3.trim())) {
                            etAcreMixedCrop3.setText("");
                            etAcreMixedCrop3.setText("");
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
                            etAcreMixedCrop3.setText("");
                            etAcreMixedCrop3.requestFocus();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("ಸೂಚನೆ :");
                    alertDialog.setMessage("ಸರಿಯಾದ ಸಮೀಕ್ಷೆ ಸಂಖ್ಯೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ");
                    alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

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
        etGuntaMixedCrop3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (etGuntaMixedCrop3.getText().toString().length() > 1) {
                        if (Integer.parseInt(etGuntaMixedCrop3.getText().toString()) < 40) {
                            if (Integer.parseInt(etGuntaMixedCrop3.getText().toString()) <= Integer.parseInt(mixedCropGuntaValue3.trim())) {

                                etFGuntaMixedCrop3.setText("");
                                    /*et_cents.setText("");
                                    et_ares.setText("");*/
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                                alertDialog.setTitle("ಸೂಚನೆ :");
                                alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                                alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        etFGuntaMixedCrop3.setText("");
                                           /* et_cents.setText("");
                                            et_ares.setText("");*/
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setCanceledOnTouchOutside(false);
                                etGuntaMixedCrop3.setText("");
                                etGuntaMixedCrop3.requestFocus();

                                etFGuntaMixedCrop3.setText("");
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
                                    etFGuntaMixedCrop3.setText("");
                                        /*et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaMixedCrop3.setText("");
                            etGuntaMixedCrop3.requestFocus();

                            etFGuntaMixedCrop3.setText("");
                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                        }
                    } else {
                        if (Integer.parseInt(etFGuntaMixedCrop3.getText().toString()) < 100) {

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    etFGuntaMixedCrop3.setText("");
                                       /* et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaMixedCrop3.setText("");
                            etGuntaMixedCrop3.requestFocus();

                            etFGuntaMixedCrop3.setText("");
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
        etFGuntaMixedCrop3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    //   if (Integer.parseInt(et_fgunta.getText().toString()) >= 0) {

                    if (Integer.parseInt(etFGuntaMixedCrop3.getText().toString()) < 100) {
                        if (Integer.parseInt(etFGuntaMixedCrop3.getText().toString()) <= Integer.parseInt(mixedCropFGuntaValue3.trim())){

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
                            etFGuntaMixedCrop3.setText("");
                            etFGuntaMixedCrop3.requestFocus();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                        alertDialog.setTitle("ಸೂಚನೆ :");
                        alertDialog.setMessage("ಸರಿಯಾದ fಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                        alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                            }
                        });
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        etFGuntaMixedCrop3.setText("");
                        etFGuntaMixedCrop3.requestFocus();

                       /* et_cents.setText("");
                        et_ares.setText("");*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //mixed crop4 acre,gunta,fgunta
        etAcreMixedCrop4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(area!=null){
                    try {
                        if (Integer.parseInt(etAcreMixedCrop4.getText().toString()) <= Integer.parseInt(mixedCropAcreValue4.trim())) {
                            etAcreMixedCrop4.setText("");
                            etAcreMixedCrop4.setText("");
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
                            etAcreMixedCrop4.setText("");
                            etAcreMixedCrop4.requestFocus();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("ಸೂಚನೆ :");
                    alertDialog.setMessage("ಸರಿಯಾದ ಸಮೀಕ್ಷೆ ಸಂಖ್ಯೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ");
                    alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

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
        etGuntaMixedCrop4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (etGuntaMixedCrop4.getText().toString().length() > 1) {
                        if (Integer.parseInt(etGuntaMixedCrop4.getText().toString()) < 40) {
                            if (Integer.parseInt(etGuntaMixedCrop4.getText().toString()) <= Integer.parseInt(mixedCropGuntaValue4.trim())) {

                                etFGuntaMixedCrop4.setText("");
                                    /*et_cents.setText("");
                                    et_ares.setText("");*/
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                                alertDialog.setTitle("ಸೂಚನೆ :");
                                alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                                alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        etFGuntaMixedCrop4.setText("");
                                           /* et_cents.setText("");
                                            et_ares.setText("");*/
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setCanceledOnTouchOutside(false);
                                etGuntaMixedCrop4.setText("");
                                etGuntaMixedCrop4.requestFocus();

                                etFGuntaMixedCrop4.setText("");
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
                                    etFGuntaMixedCrop4.setText("");
                                        /*et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaMixedCrop4.setText("");
                            etGuntaMixedCrop4.requestFocus();

                            etFGuntaMixedCrop4.setText("");
                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                        }
                    } else {
                        if (Integer.parseInt(etFGuntaMixedCrop4.getText().toString()) < 100) {

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    etFGuntaMixedCrop3.setText("");
                                       /* et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaMixedCrop4.setText("");
                            etGuntaMixedCrop4.requestFocus();

                            etFGuntaMixedCrop4.setText("");
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
        etFGuntaMixedCrop4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    //   if (Integer.parseInt(et_fgunta.getText().toString()) >= 0) {

                    if (Integer.parseInt(etFGuntaMixedCrop4.getText().toString()) < 100) {
                        if (Integer.parseInt(etFGuntaMixedCrop4.getText().toString()) <= Integer.parseInt(mixedCropFGuntaValue4.trim())){

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
                            etFGuntaMixedCrop4.setText("");
                            etFGuntaMixedCrop4.requestFocus();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                        alertDialog.setTitle("ಸೂಚನೆ :");
                        alertDialog.setMessage("ಸರಿಯಾದ fಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                        alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                            }
                        });
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        etFGuntaMixedCrop4.setText("");
                        etFGuntaMixedCrop4.requestFocus();

                       /* et_cents.setText("");
                        et_ares.setText("");*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

//        arrayMCropName.add("Select Crop");
       /* ArrayAdapter<String> adapter_allcrops = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayMCropName);
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
*/    }

    private void alertformixedcropentry(Context mContext, String mixed_crops_selected_value, String mcpacre,String mcpgunta, String mcpfgunta,int mpares,int k) {
        getmixedcrops();

      /*  Set<String> listmcropnames = new LinkedHashSet<String>(arrayMCropName);
        arrayMCropName.clear();
        arrayMCropName.addAll(listmcropnames);*/

    }

    //intercrops
    private void setInterCropViews() {
        //inter crop names
        arrayInterCropNames.add(0,"Select Inter Crops");
        ArrayAdapter<String> intercrop1adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayInterCropNames);
        intercrop1adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intercrop1adapter.notifyDataSetChanged();
        MainViewModel viewInterCrop1Names = ViewModelProviders.of(this).get(MainViewModel.class);
        viewInterCrop1Names.getInterCrops().observe(this, new Observer<List<ModelFertilizerCropMaster>>() {
            @Override
            public void onChanged(@Nullable List<ModelFertilizerCropMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    for(ModelFertilizerCropMaster taskEntry:taskEntries){

                        arrayInterCropNames.add(taskEntry.getCropname_eng());
                        //      array_allcrops_kn.add(taskEntry.getCropname_kn());

                    }
                }
                else{
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
                intercrop1adapter.notifyDataSetChanged();
                spinnerInterCrop1.setAdapter(intercrop1adapter);
                spinnerInterCrop1.setSelection(0);
                spinnerInterCrop1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            interCrop1Value = spinnerInterCrop1.getSelectedItem().toString();
                            // cropCode = String.valueOf(i);
                            spinnerCropName.setSelection(0);
                            cropnameValue = "";
                            cropCode = "";
                            spinnerMixedCrop1.setSelection(0);
                            mixedCrop1Value = "";
                            spinnerMixedCropVariety1.setSelection(0);
                            mixedCropVariety1Value = "";
                            spinnerMixedCrop2.setSelection(0);
                            mixedCrop2Value = "";
                            spinnerMixedCropVariety2.setSelection(0);
                            mixedCropVariety2Value = "";
                            spinnerMixedCrop3.setSelection(0);
                            mixedCrop3Value = "";
                            spinnerMixedCropVariety3.setSelection(0);
                            mixedCropVariety3Value = "";
                            spinnerMixedCrop4.setSelection(0);
                            mixedCrop4Value = "";
                            spinnerMixedCropVariety4.setSelection(0);
                            mixedCropVariety4Value = "";
                        }else{
                            spinnerInterCrop1.setSelection(0);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });

        ArrayAdapter<String> intercrop2adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayInterCropNames);
        intercrop2adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intercrop2adapter.notifyDataSetChanged();
        MainViewModel viewInterCrop2Names = ViewModelProviders.of(this).get(MainViewModel.class);
        viewInterCrop2Names.getInterCrops().observe(this, new Observer<List<ModelFertilizerCropMaster>>() {
            @Override
            public void onChanged(@Nullable List<ModelFertilizerCropMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    for(ModelFertilizerCropMaster taskEntry:taskEntries){
                        arrayInterCropNames.add(taskEntry.getCropname_eng());
                        //      array_allcrops_kn.add(taskEntry.getCropname_kn());
                    }
                }
                else{
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
                intercrop2adapter.notifyDataSetChanged();
                spinnerInterCrop2.setAdapter(intercrop2adapter);
                spinnerInterCrop2.setSelection(0);
                spinnerInterCrop2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            interCrop2Value = spinnerInterCrop2.getSelectedItem().toString();
                            // cropCode = String.valueOf(i);
                            spinnerCropName.setSelection(0);
                            cropnameValue = "";
                            cropCode = "";
                            spinnerMixedCrop1.setSelection(0);
                            mixedCrop1Value = "";
                            spinnerMixedCropVariety1.setSelection(0);
                            mixedCropVariety1Value = "";
                            spinnerMixedCrop2.setSelection(0);
                            mixedCrop2Value = "";
                            spinnerMixedCropVariety2.setSelection(0);
                            mixedCropVariety2Value = "";
                            spinnerMixedCrop3.setSelection(0);
                            mixedCrop3Value = "";
                            spinnerMixedCropVariety3.setSelection(0);
                            mixedCropVariety3Value = "";
                            spinnerMixedCrop4.setSelection(0);
                            mixedCrop4Value = "";
                            spinnerMixedCropVariety4.setSelection(0);
                            mixedCropVariety4Value = "";
                        }else{
                            spinnerInterCrop2.setSelection(0);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });

        ArrayAdapter<String> intercrop3adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayInterCropNames);
        intercrop3adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intercrop3adapter.notifyDataSetChanged();
        MainViewModel viewInterCrop3Names = ViewModelProviders.of(this).get(MainViewModel.class);
        viewInterCrop3Names.getInterCrops().observe(this, new Observer<List<ModelFertilizerCropMaster>>() {
            @Override
            public void onChanged(@Nullable List<ModelFertilizerCropMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    for(ModelFertilizerCropMaster taskEntry:taskEntries){
                        arrayInterCropNames.add(taskEntry.getCropname_eng());
                        //      array_allcrops_kn.add(taskEntry.getCropname_kn());
                    }
                }
                else{
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
                intercrop3adapter.notifyDataSetChanged();
                spinnerInterCrop3.setAdapter(intercrop3adapter);
                spinnerInterCrop3.setSelection(0);
                spinnerInterCrop3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            interCrop3Value = spinnerMixedCrop3.getSelectedItem().toString();
                            // cropCode = String.valueOf(i);
                            spinnerCropName.setSelection(0);
                            cropnameValue = "";
                            cropCode = "";
                            spinnerMixedCrop1.setSelection(0);
                            mixedCrop1Value = "";
                            spinnerMixedCropVariety1.setSelection(0);
                            mixedCropVariety1Value = "";
                            spinnerMixedCrop2.setSelection(0);
                            mixedCrop2Value = "";
                            spinnerMixedCropVariety2.setSelection(0);
                            mixedCropVariety2Value = "";
                            spinnerMixedCrop3.setSelection(0);
                            mixedCrop3Value = "";
                            spinnerMixedCropVariety3.setSelection(0);
                            mixedCropVariety3Value = "";
                            spinnerMixedCrop4.setSelection(0);
                            mixedCrop4Value = "";
                            spinnerMixedCropVariety4.setSelection(0);
                            mixedCropVariety4Value = "";
                        }else{
                            spinnerInterCrop3.setSelection(0);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });

        ArrayAdapter<String> intercrop4adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayInterCropNames);
        intercrop4adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intercrop4adapter.notifyDataSetChanged();
        MainViewModel viewInterCrop4Names = ViewModelProviders.of(this).get(MainViewModel.class);
        viewInterCrop4Names.getInterCrops().observe(this, new Observer<List<ModelFertilizerCropMaster>>() {
            @Override
            public void onChanged(@Nullable List<ModelFertilizerCropMaster> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    for(ModelFertilizerCropMaster taskEntry:taskEntries){
                        arrayInterCropNames.add(taskEntry.getCropname_eng());
                        //      array_allcrops_kn.add(taskEntry.getCropname_kn());
                    }
                }
                else{
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
                intercrop4adapter.notifyDataSetChanged();
                spinnerInterCrop4.setAdapter(intercrop4adapter);
                spinnerInterCrop4.setSelection(0);
                spinnerInterCrop4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            interCrop4Value = spinnerInterCrop4.getSelectedItem().toString();
                            // cropCode = String.valueOf(i);
                            spinnerCropName.setSelection(0);
                            cropnameValue = "";
                            cropCode = "";
                            spinnerMixedCrop1.setSelection(0);
                            mixedCrop1Value = "";
                            spinnerMixedCropVariety1.setSelection(0);
                            mixedCropVariety1Value = "";
                            spinnerMixedCrop2.setSelection(0);
                            mixedCrop2Value = "";
                            spinnerMixedCropVariety2.setSelection(0);
                            mixedCropVariety2Value = "";
                            spinnerMixedCrop3.setSelection(0);
                            mixedCrop3Value = "";
                            spinnerMixedCropVariety3.setSelection(0);
                            mixedCropVariety3Value = "";
                            spinnerMixedCrop4.setSelection(0);
                            mixedCrop4Value = "";
                            spinnerMixedCropVariety4.setSelection(0);
                            mixedCropVariety4Value = "";
                        }else{
                            spinnerInterCrop4.setSelection(0);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });

        //inter crop variety
        arrayInterCropVariety.add(0,"Select Crop Variety");
        arrayInterCropVariety.add(1,"Hybrid");
        arrayInterCropVariety.add(2,"Local");

        ArrayAdapter<String> adapterInterCrop1Variety = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayInterCropVariety);
        adapterInterCrop1Variety.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterInterCrop1Variety.notifyDataSetChanged();
        spinnerInterCropVariety1.setAdapter(adapterInterCrop1Variety);
        spinnerInterCropVariety1.setSelection(0);
        spinnerInterCropVariety1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    interCropVariety1Value = spinnerInterCropVariety1.getSelectedItem().toString();
                    spinnerCropName.setSelection(0);
                    cropnameValue = "";
                    cropCode = "";
                    spinnerMixedCrop1.setSelection(0);
                    mixedCrop1Value = "";
                    spinnerMixedCropVariety1.setSelection(0);
                    mixedCropVariety1Value = "";
                    spinnerMixedCrop2.setSelection(0);
                    mixedCrop2Value = "";
                    spinnerMixedCropVariety2.setSelection(0);
                    mixedCropVariety2Value = "";
                    spinnerMixedCrop3.setSelection(0);
                    mixedCrop3Value = "";
                    spinnerMixedCropVariety3.setSelection(0);
                    mixedCropVariety3Value = "";
                    spinnerMixedCrop4.setSelection(0);
                    mixedCrop4Value = "";
                    spinnerMixedCropVariety4.setSelection(0);
                    mixedCropVariety4Value = "";
                }else{
                    spinnerInterCropVariety1.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapterInterCrop2Variety = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayInterCropVariety);
        adapterInterCrop2Variety.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterInterCrop2Variety.notifyDataSetChanged();
        spinnerInterCropVariety2.setAdapter(adapterInterCrop2Variety);
        spinnerInterCropVariety2.setSelection(0);
        spinnerInterCropVariety2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    interCropVariety2Value = spinnerInterCropVariety2.getSelectedItem().toString();
                    spinnerCropName.setSelection(0);
                    cropnameValue = "";
                    cropCode = "";
                    spinnerMixedCrop1.setSelection(0);
                    mixedCrop1Value = "";
                    spinnerMixedCropVariety1.setSelection(0);
                    mixedCropVariety1Value = "";
                    spinnerMixedCrop2.setSelection(0);
                    mixedCrop2Value = "";
                    spinnerMixedCropVariety2.setSelection(0);
                    mixedCropVariety2Value = "";
                    spinnerMixedCrop3.setSelection(0);
                    mixedCrop3Value = "";
                    spinnerMixedCropVariety3.setSelection(0);
                    mixedCropVariety3Value = "";
                    spinnerMixedCrop4.setSelection(0);
                    mixedCrop4Value = "";
                    spinnerMixedCropVariety4.setSelection(0);
                    mixedCropVariety4Value = "";
                }else{
                    spinnerInterCropVariety2.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapterInterCrop3Variety = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayInterCropVariety);
        adapterInterCrop3Variety.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterInterCrop3Variety.notifyDataSetChanged();
        spinnerInterCropVariety3.setAdapter(adapterInterCrop3Variety);
        spinnerInterCropVariety3.setSelection(0);
        spinnerInterCropVariety3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    interCropVariety3Value = spinnerInterCropVariety3.getSelectedItem().toString();
                    spinnerCropName.setSelection(0);
                    cropnameValue = "";
                    cropCode = "";
                    spinnerMixedCrop1.setSelection(0);
                    mixedCrop1Value = "";
                    spinnerMixedCropVariety1.setSelection(0);
                    mixedCropVariety1Value = "";
                    spinnerMixedCrop2.setSelection(0);
                    mixedCrop2Value = "";
                    spinnerMixedCropVariety2.setSelection(0);
                    mixedCropVariety2Value = "";
                    spinnerMixedCrop3.setSelection(0);
                    mixedCrop3Value = "";
                    spinnerMixedCropVariety3.setSelection(0);
                    mixedCropVariety3Value = "";
                    spinnerMixedCrop4.setSelection(0);
                    mixedCrop4Value = "";
                    spinnerMixedCropVariety4.setSelection(0);
                    mixedCropVariety4Value = "";
                }else{
                    spinnerInterCropVariety3.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapterInterCrop4Variety = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayInterCropVariety);
        adapterInterCrop4Variety.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterInterCrop4Variety.notifyDataSetChanged();
        spinnerInterCropVariety4.setAdapter(adapterInterCrop4Variety);
        spinnerInterCropVariety4.setSelection(0);
        spinnerInterCropVariety4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    interCropVariety4Value = spinnerInterCropVariety4.getSelectedItem().toString();
                    spinnerCropName.setSelection(0);
                    cropnameValue = "";
                    cropCode = "";
                    spinnerMixedCrop1.setSelection(0);
                    mixedCrop1Value = "";
                    spinnerMixedCropVariety1.setSelection(0);
                    mixedCropVariety1Value = "";
                    spinnerMixedCrop2.setSelection(0);
                    mixedCrop2Value = "";
                    spinnerMixedCropVariety2.setSelection(0);
                    mixedCropVariety2Value = "";
                    spinnerMixedCrop3.setSelection(0);
                    mixedCrop3Value = "";
                    spinnerMixedCropVariety3.setSelection(0);
                    mixedCropVariety3Value = "";
                    spinnerMixedCrop4.setSelection(0);
                    mixedCrop4Value = "";
                    spinnerMixedCropVariety4.setSelection(0);
                    mixedCropVariety4Value = "";
                }else{
                    spinnerInterCropVariety4.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //mixed crop1 acre,gunta,fgunta
        etAcreInterCrop1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(area!=null){
                    try {
                        if (Integer.parseInt(etAcreInterCrop1.getText().toString()) <= Integer.parseInt(interCropAcreValue1.trim())) {
                            etGuntaInterCrop1.setText("");
                            etFGuntaInterCrop1.setText("");
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
                            etAcreInterCrop1.setText("");
                            etAcreInterCrop1.requestFocus();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("ಸೂಚನೆ :");
                    alertDialog.setMessage("ಸರಿಯಾದ ಸಮೀಕ್ಷೆ ಸಂಖ್ಯೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ");
                    alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

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
        etGuntaInterCrop1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (etGuntaInterCrop1.getText().toString().length() > 1) {
                        if (Integer.parseInt(etGuntaInterCrop1.getText().toString()) < 40) {
                            if (Integer.parseInt(etGuntaInterCrop1.getText().toString()) <= Integer.parseInt(interCropGuntaValue1.trim())) {

                                etFGuntaInterCrop1.setText("");
                                    /*et_cents.setText("");
                                    et_ares.setText("");*/
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                                alertDialog.setTitle("ಸೂಚನೆ :");
                                alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                                alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        etFGuntaInterCrop1.setText("");
                                           /* et_cents.setText("");
                                            et_ares.setText("");*/
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setCanceledOnTouchOutside(false);
                                etGuntaInterCrop1.setText("");
                                etGuntaInterCrop1.requestFocus();

                                etFGuntaInterCrop1.setText("");
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
                                    etFGuntaInterCrop1.setText("");
                                        /*et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaInterCrop1.setText("");
                            etGuntaInterCrop1.requestFocus();

                            etFGuntaInterCrop1.setText("");
                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                        }
                    } else {
                        if (Integer.parseInt(etFGuntaInterCrop1.getText().toString()) < 100) {

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    etFGuntaInterCrop1.setText("");
                                       /* et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaInterCrop1.setText("");
                            etGuntaInterCrop1.requestFocus();

                            etFGuntaInterCrop1.setText("");
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
        etFGuntaInterCrop1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    //   if (Integer.parseInt(et_fgunta.getText().toString()) >= 0) {

                    if (Integer.parseInt(etFGuntaInterCrop1.getText().toString()) < 100) {
                        if (Integer.parseInt(etFGuntaInterCrop1.getText().toString()) <= Integer.parseInt(interCropFGuntaValue1.trim())){

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
                            etFGuntaInterCrop1.setText("");
                            etFGuntaInterCrop1.requestFocus();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                        alertDialog.setTitle("ಸೂಚನೆ :");
                        alertDialog.setMessage("ಸರಿಯಾದ fಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                        alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                            }
                        });
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        etFGuntaInterCrop1.setText("");
                        etFGuntaInterCrop1.requestFocus();

                       /* et_cents.setText("");
                        et_ares.setText("");*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //mixed crop2 acre,gunta,fgunta
        etAcreInterCrop2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(area!=null){
                    try {
                        if (Integer.parseInt(etAcreInterCrop2.getText().toString()) <= Integer.parseInt(interCropAcreValue2.trim())) {
                            etGuntaInterCrop2.setText("");
                            etFGuntaInterCrop2.setText("");
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
                            etAcreInterCrop2.setText("");
                            etAcreInterCrop2.requestFocus();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("ಸೂಚನೆ :");
                    alertDialog.setMessage("ಸರಿಯಾದ ಸಮೀಕ್ಷೆ ಸಂಖ್ಯೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ");
                    alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

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
        etGuntaInterCrop2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (etGuntaInterCrop2.getText().toString().length() > 1) {
                        if (Integer.parseInt(etGuntaInterCrop2.getText().toString()) < 40) {
                            if (Integer.parseInt(etGuntaInterCrop2.getText().toString()) <= Integer.parseInt(interCropGuntaValue2.trim())) {

                                etFGuntaInterCrop2.setText("");
                                    /*et_cents.setText("");
                                    et_ares.setText("");*/
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                                alertDialog.setTitle("ಸೂಚನೆ :");
                                alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                                alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        etFGuntaInterCrop2.setText("");
                                           /* et_cents.setText("");
                                            et_ares.setText("");*/
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setCanceledOnTouchOutside(false);
                                etGuntaInterCrop2.setText("");
                                etGuntaInterCrop2.requestFocus();

                                etFGuntaInterCrop2.setText("");
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
                                    etFGuntaInterCrop2.setText("");
                                        /*et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaInterCrop2.setText("");
                            etGuntaInterCrop2.requestFocus();

                            etFGuntaInterCrop2.setText("");
                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                        }
                    } else {
                        if (Integer.parseInt(etFGuntaInterCrop2.getText().toString()) < 100) {

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    etFGuntaInterCrop2.setText("");
                                       /* et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaInterCrop2.setText("");
                            etGuntaInterCrop2.requestFocus();

                            etFGuntaInterCrop2.setText("");
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
        etFGuntaInterCrop2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    //   if (Integer.parseInt(et_fgunta.getText().toString()) >= 0) {

                    if (Integer.parseInt(etFGuntaInterCrop2.getText().toString()) < 100) {
                        if (Integer.parseInt(etFGuntaInterCrop2.getText().toString()) <= Integer.parseInt(interCropFGuntaValue2.trim())){

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
                            etFGuntaInterCrop2.setText("");
                            etFGuntaInterCrop2.requestFocus();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                        alertDialog.setTitle("ಸೂಚನೆ :");
                        alertDialog.setMessage("ಸರಿಯಾದ fಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                        alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                            }
                        });
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        etFGuntaInterCrop2.setText("");
                        etFGuntaInterCrop2.requestFocus();

                       /* et_cents.setText("");
                        et_ares.setText("");*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //mixed crop3 acre,gunta,fgunta
        etAcreInterCrop3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(area!=null){
                    try {
                        if (Integer.parseInt(etAcreInterCrop3.getText().toString()) <= Integer.parseInt(interCropAcreValue3.trim())) {
                            etAcreInterCrop3.setText("");
                            etAcreInterCrop3.setText("");
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
                            etAcreInterCrop3.setText("");
                            etAcreInterCrop3.requestFocus();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("ಸೂಚನೆ :");
                    alertDialog.setMessage("ಸರಿಯಾದ ಸಮೀಕ್ಷೆ ಸಂಖ್ಯೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ");
                    alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

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
        etGuntaInterCrop3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (etGuntaInterCrop3.getText().toString().length() > 1) {
                        if (Integer.parseInt(etGuntaMixedCrop3.getText().toString()) < 40) {
                            if (Integer.parseInt(etGuntaInterCrop3.getText().toString()) <= Integer.parseInt(interCropGuntaValue3.trim())) {

                                etFGuntaInterCrop3.setText("");
                                    /*et_cents.setText("");
                                    et_ares.setText("");*/
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                                alertDialog.setTitle("ಸೂಚನೆ :");
                                alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                                alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        etFGuntaInterCrop3.setText("");
                                           /* et_cents.setText("");
                                            et_ares.setText("");*/
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setCanceledOnTouchOutside(false);
                                etGuntaInterCrop3.setText("");
                                etGuntaInterCrop3.requestFocus();

                                etFGuntaInterCrop3.setText("");
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
                                    etFGuntaInterCrop3.setText("");
                                        /*et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaInterCrop3.setText("");
                            etGuntaInterCrop3.requestFocus();

                            etFGuntaInterCrop3.setText("");
                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                        }
                    } else {
                        if (Integer.parseInt(etFGuntaInterCrop3.getText().toString()) < 100) {

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    etFGuntaInterCrop3.setText("");
                                       /* et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaInterCrop3.setText("");
                            etGuntaInterCrop3.requestFocus();

                            etFGuntaInterCrop3.setText("");
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
        etFGuntaInterCrop3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    //   if (Integer.parseInt(et_fgunta.getText().toString()) >= 0) {

                    if (Integer.parseInt(etFGuntaInterCrop3.getText().toString()) < 100) {
                        if (Integer.parseInt(etFGuntaInterCrop3.getText().toString()) <= Integer.parseInt(interCropFGuntaValue3.trim())){

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
                            etFGuntaInterCrop3.setText("");
                            etFGuntaInterCrop3.requestFocus();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                        alertDialog.setTitle("ಸೂಚನೆ :");
                        alertDialog.setMessage("ಸರಿಯಾದ fಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                        alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                            }
                        });
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        etFGuntaInterCrop3.setText("");
                        etFGuntaInterCrop3.requestFocus();

                       /* et_cents.setText("");
                        et_ares.setText("");*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //mixed crop4 acre,gunta,fgunta
        etAcreInterCrop4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(area!=null){
                    try {
                        if (Integer.parseInt(etAcreInterCrop4.getText().toString()) <= Integer.parseInt(interCropAcreValue4.trim())) {
                            etAcreInterCrop4.setText("");
                            etAcreInterCrop4.setText("");
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
                            etAcreInterCrop4.setText("");
                            etAcreInterCrop4.requestFocus();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("ಸೂಚನೆ :");
                    alertDialog.setMessage("ಸರಿಯಾದ ಸಮೀಕ್ಷೆ ಸಂಖ್ಯೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ");
                    alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

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
        etGuntaInterCrop4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (etGuntaInterCrop4.getText().toString().length() > 1) {
                        if (Integer.parseInt(etGuntaInterCrop4.getText().toString()) < 40) {
                            if (Integer.parseInt(etGuntaInterCrop4.getText().toString()) <= Integer.parseInt(interCropGuntaValue4.trim())) {

                                etFGuntaInterCrop4.setText("");
                                    /*et_cents.setText("");
                                    et_ares.setText("");*/
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                                alertDialog.setTitle("ಸೂಚನೆ :");
                                alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                                alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        etFGuntaInterCrop4.setText("");
                                           /* et_cents.setText("");
                                            et_ares.setText("");*/
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setCanceledOnTouchOutside(false);
                                etGuntaInterCrop4.setText("");
                                etGuntaInterCrop4.requestFocus();

                                etFGuntaInterCrop4.setText("");
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
                                    etFGuntaInterCrop4.setText("");
                                        /*et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaInterCrop4.setText("");
                            etGuntaInterCrop4.requestFocus();

                            etFGuntaInterCrop4.setText("");
                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                        }
                    } else {
                        if (Integer.parseInt(etFGuntaInterCrop4.getText().toString()) < 100) {

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                            alertDialog.setTitle("ಸೂಚನೆ :");
                            alertDialog.setMessage("ಸರಿಯಾದ ಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                            alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    etFGuntaInterCrop3.setText("");
                                       /* et_cents.setText("");
                                        et_ares.setText("");*/
                                }
                            });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            etGuntaInterCrop4.setText("");
                            etGuntaInterCrop4.requestFocus();

                            etFGuntaInterCrop4.setText("");
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
        etFGuntaInterCrop4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    //   if (Integer.parseInt(et_fgunta.getText().toString()) >= 0) {

                    if (Integer.parseInt(etFGuntaInterCrop4.getText().toString()) < 100) {
                        if (Integer.parseInt(etFGuntaInterCrop4.getText().toString()) <= Integer.parseInt(interCropFGuntaValue4.trim())){

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
                            etFGuntaInterCrop4.setText("");
                            etFGuntaInterCrop4.requestFocus();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(CropRegister.this).create();
                        alertDialog.setTitle("ಸೂಚನೆ :");
                        alertDialog.setMessage("ಸರಿಯಾದ fಗುಂಟ ಅಳತೆ ಸಂಖ್ಯೆಯನ್ನು ನಮೂದಿಸಿ");
                        alertDialog.setButton("ಸರಿ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                              /*  et_cents.setText("");
                                et_ares.setText("");*/
                            }
                        });
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        etFGuntaInterCrop4.setText("");
                        etFGuntaInterCrop4.requestFocus();

                       /* et_cents.setText("");
                        et_ares.setText("");*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void getIntercrops(){

    }

 /*   private void alertforintercropentry(Context mContext, String icpacre,String icpgunta, String icpfgunta, int k) {

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

                dialogInterface.dismiss();
                tblInterCropPick.setVisibility(View.INVISIBLE);
                tblInterCropPick.removeAllViews();
                lyRelativeInterCrop.setVisibility(View.GONE);
                lytTotalInterCropExtent.setVisibility(View.GONE);
            }
        });

        final AlertDialog dialog = alertDialog.create();
        dialog.show();


    }
*/
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Home", "Permission Granted");
                } else {
                    Log.d("Home", "Permission Failed");
                    Toast.makeText(mContext, "You must allow permission record audio to your mobile device.", Toast.LENGTH_SHORT).show();
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

   /* public void gotoMap(String selectedLGV,String selectedVillageName, String selectedCropName, String selectedExpID, String selectedENGVN) {

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


    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FileOutputStream fileOutputStream = null;

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                if (resultCode == RESULT_OK)
                    getMyLocation();
                Toast.makeText(getApplicationContext(), "Gps Is Enabled", Toast.LENGTH_LONG).show();
                break;
            case PICTURE_RESULT:
                if (resultCode == RESULT_OK)

                    if (data != null && data.getData() != null) {
                        imageUri = data.getData();
                        Toast.makeText(getApplicationContext(), "Image not Captured ", Toast.LENGTH_SHORT).show();
                    } else {
                        isImageTaken = true;
                        new AlertDialog.Builder(CropRegister.this)
                                .setTitle("Alert")
                                .setMessage("Image Captured")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .show();

                        ivCaptureCropPhoto.setVisibility(View.VISIBLE);
                    }
                try {
                    Bitmap bitmap = decodeUri(imageUri);
                    if (bitmap != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();
                        bitmap = StoreByteImage(b,cropTypePhoto);
                        ivCaptureCropPhoto.setImageBitmap(bitmap);
//                        bitmap.recycle();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
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

    public Bitmap StoreByteImage(byte[] imageData,String crpType) {

        Bitmap rotatedBitmap = null;
        Bitmap capturedImage = null;
        FileOutputStream fileOutputStream = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) 0.5;
            Bitmap myImage = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
            Matrix matrix = new Matrix();
            ExifInterface exifReader = null;

            try {
                exifReader = new ExifInterface(imageUri.getPath());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // Location of your image

            int orientation = exifReader.getAttributeInt(ExifInterface.TAG_ORIENTATION,
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

            rotatedBitmap = Bitmap.createBitmap(myImage, 0, 0, myImage.getWidth(), myImage.getHeight(), matrix, true);
            capturedImage = timestampItAndSave(rotatedBitmap,crpType);

            String ipath = imageUri.getPath().replace("/mnt/sdcard/CII_IMAGES-", "");
            String url = ipath;

            fileOutputStream = new FileOutputStream(url);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream, 8129);
            capturedImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            capturedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            // byte[] b = baos.toByteArray();

            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return capturedImage;
    }

    private Bitmap timestampItAndSave(Bitmap toEdit,String crpType){
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Bitmap canvasBitmap = toEdit.copy(Bitmap.Config.RGB_565, true);
        Canvas imageCanvas = new Canvas(canvasBitmap);
        Paint imagePaint = new Paint();
        imagePaint.setTextAlign(Paint.Align.CENTER);
        imagePaint.setTextSize(20f);
        imagePaint.setStyle(Paint.Style.FILL);
        imagePaint.setColor(Color.CYAN);

        if(crpType.equals("SingleCrop")){
            imageCanvas.drawText(currentDateTimeString.toString(),canvasBitmap.getWidth() / 2, (canvasBitmap.getHeight() - 15),imagePaint);
          //  if (latitude != 0.00 && longitude != 0.00) {
            imageCanvas.drawText("GPS : " + latitude + "," + longitude, canvasBitmap.getWidth() / 2, (canvasBitmap.getHeight() - 25), imagePaint);
            imageCanvas.drawText("Farmer ID : "+farmerID , canvasBitmap.getWidth() / 2,25, imagePaint);
            imageCanvas.drawText("Survey No. : " + surveySpinnerValue + " Crop : " + cropnameValue + " Type : SingleCrop", canvasBitmap.getWidth() / 2, (canvasBitmap.getHeight() - 30), imagePaint);
     //   }
        }else if(crpType.equals("MixedCrop")){
        }else if(crpType.equals("InterCrop")){
        }
        return canvasBitmap;
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // TODO Auto-generated method stub
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
        final int REQUIRED_SIZE = 150;
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
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
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
                .setContentTitle("Crop Registration - SingleCrop")
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
                    .setContentTitle("Crop Registration - SingleCrop")
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
