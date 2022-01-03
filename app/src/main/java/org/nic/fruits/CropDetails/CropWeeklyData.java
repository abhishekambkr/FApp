package org.nic.fruits.CropDetails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
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

import org.nic.fruits.R;

import org.nic.fruits.Utils;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;
import org.nic.fruits.pojo.ModelCropMultipickingData;
import org.nic.fruits.pojo.ModelCropRegistration;
import org.nic.fruits.pojo.ModelCropWeeklyData;
import org.nic.fruits.viewmodel.MainViewModel;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CropWeeklyData extends AppCompatActivity {

    private Context mContext;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private String farmerID;
    private String keyValue;
    private AppDatabase appDatabase;
    LinearLayout linear_organic,linear_inorganic,linear_natural;
    TextView tv_FID,tv_year,tv_season,tv_ownername,tv_district,tv_taluk,tv_village,tv_area,tv_cropextent,tv_total_cropextent,tv_surveynumber,tv_croptype,tv_cropname,tv_croppickdate,et_croppickyield,tv_cropsowed;
    EditText et_escalate,et_fertilizer_quantity,et_pesticide_quantity,et_manure_quantity,et_natural_quantity;
    Spinner cropregisid,spinnerweeknumber,spinnermanuretype,spinnernaturaltype,spinnerfertilizertype,spinnerpesticidetype,spinnercroppick,spinnerselectcrop;
    RadioGroup rg_escalate;
    RadioButton rb_yes_escalate;
    RadioButton rb_no_escalate;
    Button btn_crop_photo,bt_crop_escalate_photo,bt_cropweeklydata;
    List<String> array_crid;
    List<String> array_regdetailsbasedonfidcrid;
    List<String> array_weeknumber;
    List<String> array_manuretype;
    List<String> array_fertlizertype;
    List<String> array_pesticidetype;
    List<String> array_naturaltype;
    List<String> array_croppick;
    List<String> array_selectcrop;
    String ownerid,ownerarea;
    String cridvalue="",weekvalue="",farmingtypevalue="",pesticidevalue="",croppickvalue="",cropdiseasevalue,radioescalatevalue="";
    String yeardisplay,yearcode,seasoncode,seasondisplay,ownernamedisplay,districtdisplay,talukdisplay,villagedisplay,cropextentdisplay,surveynumberdisplay,croptypedisplay,cropnamedisplay,cropsoweddisplay;
    private ArrayList<EditText> ListEditText_croppick;
    File AppMediaFolderImagesEnc;
    public static Uri imageUri;
    File imageFile;
    File AppMediaFolderImagesEnc_escalate;
    public static Uri imageUri_escalate;
    File imageFile_ecalate;
    public static final int PICTURE_RESULT = 1;
    public static final int ESCALATE_CAPTURE = 1;
    String Imagepath_crop;
    String Imagepath_escalate;
    private static final int CAMERA_PERMISSION = 100;
    private int weekvalidate= 0;
    TableLayout tbl_crop_pick;
    private String yieldvalue = "";
    private int count = 0;
    RelativeLayout rl_table;
    private int week=0;

    private String currentDate = "";
    private int dateDifference;
    private int firstdatediff;
    String previous_weekdate;
    int totalcrops;
    List<String>picks;
    List<String>yields;
    String cropnameValue="";
    String quantityValue="";
    String pesticide_quantityValue="";
    String escalationValue="";
    TextView tv_crop_imgtext;
    byte[] results;
    String crop_Imagepath;
    String escalate_Imagepath;
    ImageView iv_capture_cropimage;
    ImageView iv_capture_escimage;
    TextView tv_crop_escimgtext;
    Boolean escalate = false;
    LinearLayout linearLayout_escalatecamera;
    TextView tv_farming_type;
    String farming_type_display;
    private ImageButton play, stop, record;
    private Button btnUploadAudio;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private File audioFile;
    byte[] bytes;
    String base64String;
    private TextView textRecord;
    private TextView textStop;
    private TextView textPlay;
    private TextView fileName;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private LinearLayout linearAudioImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_weekly_data);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        getSupportActionBar().setTitle(getResources().getString(R.string.cropWeeklyData));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mContext = this;
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        linear_organic = (LinearLayout) findViewById(R.id.layoutorgnainc);
        linear_inorganic = (LinearLayout) findViewById(R.id.layoutinorgnainc);
        linear_natural = (LinearLayout) findViewById(R.id.layoutnatural);
        tv_FID = (TextView) findViewById(R.id.tv_farmerid);
        tv_year = (TextView)findViewById(R.id.tv_year_wd);
        tv_season=(TextView)findViewById(R.id.tv_season_wd);
        tv_ownername=(TextView)findViewById(R.id.tv_owner_wd);
        tv_district=(TextView)findViewById(R.id.tv_district_wd);
        tv_taluk=(TextView)findViewById(R.id.tv_taluk_wd);
        tv_village=(TextView)findViewById(R.id.tv_village_wd);
        tv_area=(TextView)findViewById(R.id.tv_owner_area_wd);
        tv_surveynumber=(TextView)findViewById(R.id.tv_survey_number_wd);
        tv_croptype=(TextView)findViewById(R.id.tv_croptype_wd);
        tv_cropextent=(TextView)findViewById(R.id.tv_cropextent_wd);
        tv_cropname=(TextView)findViewById(R.id.tv_crop_name_wd);
        tv_cropsowed=(TextView)findViewById(R.id.tv_crop_sowed);
        tv_croppickdate=(TextView)findViewById(R.id.txtv_pickingdate);
        iv_capture_cropimage = (ImageView)findViewById(R.id.iv_capture_crop_image);
        tv_crop_imgtext = (TextView)findViewById(R.id.tv_crop_text);
        iv_capture_escimage = (ImageView) findViewById(R.id.iv_capture_esc_image);
        tv_crop_escimgtext = (TextView)findViewById(R.id.tv_crop_esc_image);
        tv_farming_type= (TextView)findViewById(R.id.tv_farmingtype_wd);
        et_escalate = (EditText) findViewById(R.id.et_EscalateIssue);
        et_fertilizer_quantity = (EditText)findViewById(R.id.et_FertilizerQuantitypAcre);
        et_pesticide_quantity = (EditText)findViewById(R.id.et_PesticideDosagelitre);
        et_manure_quantity = (EditText)findViewById(R.id.et_manureQuantitypAcre);
        et_natural_quantity = (EditText)findViewById(R.id.et_naturalQuantitypAcre);
        //   et_croppickyield = (TextView)findViewById(R.id.et_pickinkgs);
        cropregisid = (Spinner)findViewById(R.id.spinnerCroprid);
        spinnerweeknumber=(Spinner)findViewById(R.id.spinnerWeekNumber);
        spinnerfertilizertype = (Spinner) findViewById(R.id.spinnerFertilizerType);
        spinnerpesticidetype=(Spinner) findViewById(R.id.spinnerPesticideType);
        spinnermanuretype = (Spinner) findViewById(R.id.spinnerManureType);
        spinnernaturaltype = (Spinner) findViewById(R.id.spinnerNaturalType);
        spinnercroppick = (Spinner) findViewById(R.id.spinnernumberofpicks);
        spinnerselectcrop=(Spinner) findViewById(R.id.spinnerselectcrop);
        rg_escalate = (RadioGroup) findViewById(R.id.radioGroupEscalate);
        rb_yes_escalate = (RadioButton) findViewById(R.id.radioEscalateYes);
        rb_no_escalate = (RadioButton) findViewById(R.id.radioEscalateNo);
        btn_crop_photo = (Button) findViewById(R.id.btn_crop_Weekly_Photo_Capture);
        bt_crop_escalate_photo = (Button)findViewById(R.id.btn_crop_Weekly_Escalate_Photo);
        bt_cropweeklydata = (Button) findViewById(R.id.btn_submit_weekly_data);
        rl_table = (RelativeLayout) findViewById(R.id.RelativeLayout1);
        tbl_crop_pick = (TableLayout) findViewById(R.id.table_main);
        linearLayout_escalatecamera = (LinearLayout) findViewById(R.id.escalate_camera);
        play = findViewById(R.id.escalateplay);
        stop = findViewById(R.id.escalatestop);
        record = findViewById(R.id.escalaterecord);
        fileName = findViewById(R.id.escalatefile_name);
        linearAudioImage  = findViewById(R.id.layoutescalate_audio_img);
        textRecord = findViewById(R.id.txt_recordescalate);
        textStop = findViewById(R.id.txt_stopescalate);
        textPlay = findViewById(R.id.txt_playescalate);
        final Chronometer escalateChronometer = findViewById(R.id.escalatechronometer);

        linear_organic.setVisibility(View.GONE);
        linear_inorganic.setVisibility(View.GONE);
        linear_natural.setVisibility(View.GONE);
        et_manure_quantity.setVisibility(View.GONE);
        et_fertilizer_quantity.setVisibility(View.GONE);
        et_pesticide_quantity.setVisibility(View.GONE);
        et_natural_quantity.setVisibility(View.GONE);
        //  et_croppickyield.setVisibility(View.INVISIBLE);
        bt_crop_escalate_photo.setVisibility(View.GONE);
        rl_table.setVisibility(View.INVISIBLE);
        play.setVisibility(View.INVISIBLE);
        textPlay.setVisibility(View.INVISIBLE);
        stop.setEnabled(false);
        play.setEnabled(false);

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/escalaterecording.ogg";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);





        SharedPreferences prefs = getSharedPreferences("com.example.fruites", MODE_PRIVATE);
        farmerID = prefs.getString("FarmerID", "default_value_here_if_string_is_missing");
        keyValue = prefs.getString("KeyValue", "default_value_here_if_string_is_missing");

        array_crid = new ArrayList<>();
        array_crid.add(0,"Select Crop Registered ID");
        array_regdetailsbasedonfidcrid = new ArrayList<>();
        ListEditText_croppick = new ArrayList<>();
        array_weeknumber = new ArrayList<>();
        array_manuretype = new ArrayList<>();
        array_fertlizertype = new ArrayList<>();
        array_pesticidetype = new ArrayList<>();
        array_naturaltype = new ArrayList<>();
        array_croppick = new ArrayList<>();
        array_selectcrop = new ArrayList<>();
        picks = new ArrayList<String>();
        yields = new ArrayList<String>();
        tv_FID.setText(farmerID);

        currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        System.out.println("currentDate " + currentDate);

        et_escalate.setVisibility(View.GONE);
        linearLayout_escalatecamera.setVisibility(View.GONE);
        iv_capture_escimage.setVisibility(View.GONE);
        bt_crop_escalate_photo.setVisibility(View.GONE);

        rg_escalate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                checkedId = radioGroup.getCheckedRadioButtonId();

                switch (checkedId) {
                    case R.id.radioEscalateYes:
                        escalate = true;
                        radioescalatevalue = rb_yes_escalate.getText().toString();
                        et_escalate.setVisibility(View.VISIBLE);
                        escalationValue = et_escalate.getText().toString().trim();
                        linearLayout_escalatecamera.setVisibility(View.VISIBLE);
                     /*   linearLayout_escalatecamera.setVisibility(View.VISIBLE);
                        iv_capture_escimage.setVisibility(View.VISIBLE);
                        bt_crop_escalate_photo.setVisibility(View.VISIBLE);*/
                        break;
                    case R.id.radioEscalateNo:
                        escalate = false;
                        radioescalatevalue = rb_no_escalate.getText().toString();
                        et_escalate.setVisibility(View.GONE);
                        escalationValue = "No escalation";
                        linearLayout_escalatecamera.setVisibility(View.GONE);
                        iv_capture_escimage.setVisibility(View.GONE);
                        bt_crop_escalate_photo.setVisibility(View.GONE);
                        break;

                }

            }
        });

        et_escalate.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                et_escalate.setVisibility(View.VISIBLE);
                iv_capture_escimage.setVisibility(View.VISIBLE);
                bt_crop_escalate_photo.setVisibility(View.VISIBLE);
            }
        });

        btn_crop_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera();
            }
        });

        bt_crop_escalate_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraEscalate();
            }
        });

        setview();

        bt_cropweeklydata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   if(cropregisid!=null && weekvalue!=null && fertilizervalue!=null && pesticidevalue!=null && weekvalue!=null){
                //  new SaveMultipickData().execute();
                if(fieldvalidations()) {
                    new SaveWeeklyData().execute();
                }else{
                    Toast.makeText(mContext,"Incomplete Crop Weekly Data",Toast.LENGTH_LONG).show();
                }
                //  }
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                } catch (IOException ioe) {
                    // make something
                }
                record.setEnabled(false);
                stop.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                record.setVisibility(View.INVISIBLE);
                textRecord.setVisibility(View.INVISIBLE);
                escalateChronometer.start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    myAudioRecorder.stop();
                }catch(RuntimeException stopException) {
                    // handle cleanup here
                }
                myAudioRecorder.release();
                myAudioRecorder = null;
                audioFile = new File(outputFile);
                try{
                    bytes = loadFile(audioFile);
                }catch (Exception e){
                    e.printStackTrace();
                }

                record.setEnabled(true);
                stop.setEnabled(false);
                play.setEnabled(true);
                record.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
                textRecord.setVisibility(View.INVISIBLE);
                textStop.setVisibility(View.INVISIBLE);
                textPlay.setVisibility(View.VISIBLE);
                escalateChronometer.stop();
                Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                    base64String = Base64.encodeToString(bytes, Base64.DEFAULT);
                    escalateChronometer.setVisibility(View.INVISIBLE);
                    Uri uri = Uri.parse(outputFile);
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(getApplicationContext(), uri);
                    String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    int millSecond = Integer.parseInt(durationStr);

                    new CountDownTimer(millSecond, 1) {
                        public void onTick(long millisUntilFinished) {
                            textPlay.setText("" + millisUntilFinished / 1000
                                    + "." + millisUntilFinished % 1000);
                        }

                        public void onFinish() {
                            textPlay.setText(getResources().getString(R.string.play));
                        }
                    }.start();

                } catch (Exception e) {
                }
                linearAudioImage.setVisibility(View.GONE);
//                play.setVisibility(View.GONE);
                textPlay.setVisibility(View.GONE);
                fileName.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean fieldvalidations() {
        if(cridvalue.equals("")){     //cropregisid.getSelectedItem().equals(0)
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Select Crop registered id");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {} });
            alertDialog.show();
            return false;
        }

        if(weekvalue.equals("")){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Select Week number");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {} });
            alertDialog.show();
            return false;
        }
        if(cropnameValue.equals("")){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Select Crop");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {} });
            alertDialog.show();
            return false;
        }
        /*if(farmingtypevalue.equals("")){
            if(farming_type_display.equals("Organic")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Select Organic Manure");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {} });
                alertDialog.show();
            }else if(farming_type_display.equals("Inorganic")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Select Pre-Sowing Fertlizer");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {} });
                alertDialog.show();


            }else if(farming_type_display.equals("Natural")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Select Natural Manure");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {} });
                alertDialog.show();
            }

            return false;
        }*/
        if(farming_type_display.equals("Inorganic")) {
            if (pesticidevalue.equals("")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Select Pre-Sowing Pesticide/Insectiside/Germicide/Hormones");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {} });
                alertDialog.show();
            }
        }


         /*   if(farming_type_display.equals("Organic")){
                if(!quantityValue.equals("Not used")) {
                    if (et_manure_quantity.equals("")) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                        alertDialog.setTitle("Warning :");
                        alertDialog.setMessage("Enter quantity value in Organic farming");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }
                }
                return false;

            }else if(farming_type_display.equals("Inorganic")){
                if(quantityValue.equals(""))
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Warning :");
                    alertDialog.setMessage("Enter quantity value in Inorganic farming");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {} });
                    alertDialog.show();
                }
                return false;

            }else if(farming_type_display.equals("Natural")){
                if(quantityValue.equals(""))
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Warning :");
                    alertDialog.setMessage("Enter quantity value in Natural farming");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {} });
                    alertDialog.show();
                }
                return false;
            }




        if(!spinnerfertilizertype.getSelectedItem().equals("Not used")) {
            if (et_fertilizer_quantity.getText().toString().equals("")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Enter fertilizer quantity");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                return false;
            }
        }
        if(pesticidevalue.equals("")){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Select type of pesticide");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {} });
            alertDialog.show();
            return false;
        }
        if(!spinnerpesticidetype.getSelectedItem().equals("Not used")) {
            if (et_pesticide_quantity.getText().toString().equals("")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Enter pesticide quantity");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                return false;
            }
        }*/
        if(tv_crop_imgtext.getText().toString().equals("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ")){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Crop image not captured");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            return false;
        }
        if(rg_escalate.getCheckedRadioButtonId() == -1){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Choose crop disease for escalation");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
            return false;
        }
        if(escalate == true && radioescalatevalue.equals("Yes")){
            if(et_escalate.getText().toString().equals("")){

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Enter crop disease details for escalation");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                return false;
            }
            if(tv_crop_escimgtext.getText().toString().equals("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Warning :");
                alertDialog.setMessage("Crop escalation image not captured");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                return false;
            }
        }

        if(croppickvalue.equals("")){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Warning :");
            alertDialog.setMessage("Select crop pick");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
            return false;
        }
        return true;
    }

    private void setview() {
        ArrayAdapter<String> rcrid_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,array_crid);
        rcrid_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        MainViewModel viewModelforSurveyNumbers = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModelforSurveyNumbers.getCropregistration(farmerID).observe(this, new Observer<List<ModelCropRegistration>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropRegistration> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()){

                    for(ModelCropRegistration taskEntry:taskEntries){

                        if(!array_crid.contains(taskEntry.getCrid())){
                            array_crid.add(taskEntry.getCrid() + " - " + taskEntry.getCropname());

                            System.out.println("taskEntry1 " + taskEntry.getCrid());
                        }
                        System.out.println("taskEntry2 " + taskEntry.getCrid());
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
                rcrid_adapter.notifyDataSetChanged();
                cropregisid.setAdapter(rcrid_adapter);
                cropregisid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){

                            //    cridvalue = cropregisid.getSelectedItem().toString();
                            String[] cropreg_name = cropregisid.getSelectedItem().toString().trim().split("\\-");
                            cridvalue = cropreg_name[0].trim();
                            System.out.println("cridvalue " + cridvalue);
                            spinnerselectcrop.setSelection(0);
                            spinnerfertilizertype.setSelection(0);
                            et_fertilizer_quantity.setText("");
                            farmingtypevalue = "";
                            quantityValue = "";
                            spinnerpesticidetype.setSelection(0);
                            et_pesticide_quantity.setText("");
                            pesticidevalue = "";
                            pesticide_quantityValue = "";
                            iv_capture_cropimage.setVisibility(View.GONE);
                            tv_crop_imgtext.setText("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ");
                            rg_escalate.clearCheck();
                            et_escalate.setText("");
                            escalate = false;
                            escalationValue="";
                            bt_crop_escalate_photo.setVisibility(View.GONE);
                            iv_capture_escimage.setVisibility(View.GONE);
                            tv_crop_escimgtext.setText("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ");
                            spinnercroppick.setSelection(0);
                            rl_table.setVisibility(View.GONE);
                            croppickvalue = "";
                            getregistereddetails(cridvalue);
                        }else{
                            cropregisid.setSelection(0);
                           /* tv_year.setText("");
                            tv_season.setText("");
                            tv_ownername.setText("");
                            tv_surveynumber.setText("");
                            tv_cropextent.setText("");
                            tv_cropname.setText("");*/
                        }

                        //    System.out.println("selecteditemis: " + surveySpinnerValue);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

        });

      /*  ArrayAdapter<String> weeknumber_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,array_weeknumber);
        rcrid_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        MainViewModel viewModelcropregs = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModelcropregs.getCropregistration(farmerID).observe(this, new Observer<List<ModelCropRegistration>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropRegistration> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){

                    for(ModelCropRegistration taskEntry:taskEntries){

                        if(!array_weeknumber.contains(taskEntry.getCrid())){

                            array_weeknumber.add(taskEntry.getweeknumber());
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

            }

        });
*/
        array_weeknumber.add("Current Week");
        array_weeknumber.add(1,"1");
        array_weeknumber.add(2,"2");
        array_weeknumber.add(3,"3");
        array_weeknumber.add(4,"4");
        array_weeknumber.add(5,"5");
        array_weeknumber.add(6,"6");
        array_weeknumber.add(7,"7");
        array_weeknumber.add(8,"8");
        array_weeknumber.add(9,"9");
        array_weeknumber.add(10,"10");

        ArrayAdapter<String> weeknumber_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,array_weeknumber){
            @Override
            public boolean isEnabled(int position) {
                if (position != week) {
                    return false;
                } else {
                    return true;
                }

            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position != week) {
                    textview.setTextSize(18);
                    textview.setTextColor(mContext.getResources().getColor(R.color.dark_blue));
                } else {
                    textview.setTextSize(18);
                    textview.setTextColor(mContext.getResources().getColor(R.color.deep_pink));
                }
                return view;
            }
        };
        weeknumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weeknumber_adapter.notifyDataSetChanged();
        spinnerweeknumber.setAdapter(weeknumber_adapter);
        //   spinnerweeknumber.setSelection(0);

        spinnerweeknumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                weekvalue = spinnerweeknumber.getSelectedItem().toString();
                System.out.println("weekselectedvalue " + weekvalue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //for organic manure
        array_manuretype.add(0,"Select Organic Manure");
        array_manuretype.add(1,"Transplanting Manure");
        array_manuretype.add(2,"Not used");
        et_manure_quantity.setText("");

        ArrayAdapter<String> manuretype_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,array_manuretype);
        manuretype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        manuretype_adapter.notifyDataSetChanged();
        spinnermanuretype.setAdapter(manuretype_adapter);
        spinnermanuretype.setSelection(0);
        spinnermanuretype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    farmingtypevalue = spinnermanuretype.getSelectedItem().toString();
                    if(spinnermanuretype.getSelectedItem().toString().equals("Not used")){

                        et_manure_quantity.setVisibility(View.GONE);
                        quantityValue = "Not used";
                    }else{
                        et_manure_quantity.setVisibility(View.VISIBLE);
                        quantityValue = et_manure_quantity.getText().toString().trim();
                    }

                    pesticidevalue = "NA";
                    pesticide_quantityValue = "NA";
                }else{
                    spinnermanuretype.setSelection(0);
                    quantityValue = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //for inorganic fertilizer
        array_fertlizertype.add("Select Pre-Sowing Fertlizer");
        array_fertlizertype.add(1,"Urea");
        array_fertlizertype.add(2,"D.P.A");
        array_fertlizertype.add(3,"O.P.A (Potash)");
        array_fertlizertype.add(4,"20:20:0:15");
        array_fertlizertype.add(5,"Not used");

        et_fertilizer_quantity.setText("");
        ArrayAdapter<String> fertlizer_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,array_fertlizertype);
        fertlizer_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fertlizer_adapter.notifyDataSetChanged();
        spinnerfertilizertype.setAdapter(fertlizer_adapter);
        spinnerfertilizertype.setSelection(0);
        spinnerfertilizertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    farmingtypevalue = spinnerfertilizertype.getSelectedItem().toString();
                    if(spinnerfertilizertype.getSelectedItem().toString().equals("Not used")){

                        et_fertilizer_quantity.setVisibility(View.GONE);
                        quantityValue = "Not used";
                    }else{
                        et_fertilizer_quantity.setVisibility(View.VISIBLE);
                        quantityValue = et_fertilizer_quantity.getText().toString().trim();
                    }
                }else{
                    spinnerfertilizertype.setSelection(0);
                    quantityValue = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        array_pesticidetype.add("Select Pre-Sowing Pesticide/Insectiside/Germicide/Hormones");
        array_pesticidetype.add(1,"Acephate");
        array_pesticidetype.add(2,"Acetamiprid");
        array_pesticidetype.add(3,"Alphacypermethrin");
        array_pesticidetype.add(4,"Acephate");
        array_pesticidetype.add(5,"Not used");

        ArrayAdapter<String> pesticide_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,array_pesticidetype);
        pesticide_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pesticide_adapter.notifyDataSetChanged();
        spinnerpesticidetype.setAdapter(pesticide_adapter);
        spinnerpesticidetype.setSelection(0);
        spinnerpesticidetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    pesticidevalue = spinnerpesticidetype.getSelectedItem().toString();
                    if(spinnerpesticidetype.getSelectedItem().toString().equals("Not used")){
                        et_pesticide_quantity.setText("");
                        et_pesticide_quantity.setVisibility(View.GONE);
                        pesticide_quantityValue = "Not used";
                    }else{
                        et_pesticide_quantity.setVisibility(View.VISIBLE);
                        pesticide_quantityValue = et_pesticide_quantity.getText().toString().trim();
                    }
                }else{
                    spinnerpesticidetype.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //for natural manure
        array_naturaltype.add(0,"Select Natural Manure");
        array_naturaltype.add(1,"Manure");
        array_naturaltype.add(2,"Not used");
        et_natural_quantity.setText("");

        ArrayAdapter<String> naturaltype_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,array_naturaltype);
        naturaltype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        naturaltype_adapter.notifyDataSetChanged();
        spinnernaturaltype.setAdapter(naturaltype_adapter);
        spinnernaturaltype.setSelection(0);
        spinnernaturaltype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    pesticidevalue = spinnernaturaltype.getSelectedItem().toString();
                    if(spinnernaturaltype.getSelectedItem().toString().equals("Not used")){

                        et_natural_quantity.setVisibility(View.GONE);
                        pesticide_quantityValue = "Not used";
                    }else{
                        et_natural_quantity.setVisibility(View.VISIBLE);
                        pesticide_quantityValue = et_natural_quantity.getText().toString().trim();
                    }
                    pesticidevalue = "NA";
                    pesticide_quantityValue = "NA";
                }else{
                    spinnernaturaltype.setSelection(0);
                    pesticide_quantityValue = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        array_croppick.add(0,"Select Crop Picking");
        array_croppick.add(1,"1");
        array_croppick.add(2,"2");
        array_croppick.add(3,"3");
        array_croppick.add(4,"4");
        array_croppick.add(5,"5");
        array_croppick.add(6,"6");
        array_croppick.add(7,"7");
        array_croppick.add(8,"No Crop Pick");

        ArrayAdapter<String> croppick_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,array_croppick);
        croppick_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        croppick_adapter.notifyDataSetChanged();
        spinnercroppick.setAdapter(croppick_adapter);
        spinnercroppick.setSelection(0);
        spinnercroppick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //   int counts = adapterView.getCount();
                if(i!=0){
                    croppickvalue = spinnercroppick.getSelectedItem().toString();
                    count = 0;
                    if(spinnercroppick.getSelectedItem().toString().equals("No Crop Pick")){
                        tbl_crop_pick.setVisibility(View.INVISIBLE);
                        rl_table.setVisibility(View.INVISIBLE);
                        tbl_crop_pick.removeAllViews();
                        croppickvalue = "0";
                        count = 0;
                        picks.add("0");
                        yields.add("0");
                        //      et_croppickyield.setVisibility(View.INVISIBLE);
                    }
                    else{
                      /*  et_croppickyield.setVisibility(View.VISIBLE);
                        et_croppickyield.setText("");*/
                        tbl_crop_pick.setVisibility(View.VISIBLE);
                        rl_table.setVisibility(View.VISIBLE);
                        tbl_crop_pick.removeAllViews();

                        TableRow tbrow= new TableRow(mContext);
                        TextView tv1 = new TextView(mContext);
                        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tv1.setGravity(Gravity.CENTER);
                        tv1.setBackgroundColor(Color.parseColor("#12b4ba"));
                        tv1.setText("Pickings");
                        tv1.setTextSize(18);
                        tv1.setTextColor(Color.BLACK);

                        TextView tv3 = new TextView(mContext);
                        tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tv3.setGravity(Gravity.CENTER);
                        tv3.setBackgroundColor(Color.parseColor("#12b4ba"));
                        tv3.setText("Yields(in Kgs)");
                        tv3.setTextColor(Color.BLACK);
                        tv3.setTextSize(18);
                        tbrow.addView(tv1);

                        tbrow.addView(tv3);
                        tbl_crop_pick.addView(tbrow);


                        for (int k = 1; k <= Integer.parseInt(croppickvalue); k++) {

                            String yield = alertforcall(mContext,croppickvalue,k);

                        }

                    }
                }else{
                    spinnercroppick.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private class validateWeek extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait.. loading week data");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String weekdata;
            weekdata = checkWeek();
            return weekdata;
        }

        @Override
        protected void onPostExecute(String weekdata) {

            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (weekdata.equals("notavailable")) {
                Toast.makeText(mContext, "Week available", Toast.LENGTH_SHORT).show();

                spinnerweeknumber.setSelection(weekvalidate);
                weekvalue = spinnerweeknumber.getSelectedItem().toString();

                System.out.println("weekvalue " + weekvalue);

            } else if (weekdata.equals("available")){
                new AlertDialog.Builder(mContext)
                        .setTitle(getResources().getString(R.string.alert))
                        .setMessage("Week has already been conducted, select a new week")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        })
                        .show();
            }else {
                Toast.makeText(mContext, "Problem while loading weekly data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String checkWeek() {

        weekvalidate = AppDatabase.getInstance(this).cropWeeklyDataDAO().isWeekExist(farmerID, cridvalue, surveynumberdisplay, yearcode, seasoncode, ownerid, cropnamedisplay,weekvalue);

        if (weekvalidate == 0)
        {
            // data not exist.
            // weekvalidate = weekvalidate + 1;

            return "notavailable";
        }
        else {
            // data already exist.

            return "available";
        }

    }

    private String alertforcall(Context mContext, String croppickvalue, int p) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CropWeeklyData.this);
        alertDialog.setTitle("Crop Pick yield details");
        alertDialog.setMessage("Enter crop pick yield for " + croppickvalue + " picks");
        alertDialog.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(CropWeeklyData.this);
        View forgot_pwd_layout = inflater.inflate(R.layout.layout_pick_yield,null);
        final EditText edtYield = (EditText) forgot_pwd_layout.findViewById(R.id.edt_CropPickYield);

        alertDialog.setView(forgot_pwd_layout);

        //set button
        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which){
                yieldvalue = edtYield.getText().toString().trim();
                count =  count + 1;
                //et_croppickyield.append("Pick " + p + " Yield :"  +edtYield.getText().toString() + " ");
                TableRow tbrow2 = new TableRow(mContext);
                TextView tv_pick = new TextView(mContext);

                tv_pick.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv_pick.setGravity(Gravity.CENTER);

                tv_pick.setBackgroundColor(Color.parseColor("#B2EBF2"));

                tv_pick.setTextColor(Color.BLACK);
                tv_pick.setTextSize(18);
                tv_pick.setText("No. "+count);

                TextView tv_yield = new TextView(mContext);

                tv_yield.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv_yield.setBackgroundColor(Color.parseColor("#B2EBF2"));
                tv_yield.setTextSize(18);
                tv_yield.setTextColor(Color.BLACK);
                tv_yield.setGravity(Gravity.CENTER);
                tv_yield.setText(""+yieldvalue);
                tbrow2.addView(tv_pick);
                tbrow2.addView(tv_yield);
                tbl_crop_pick.addView(tbrow2);
                picks.add(String.valueOf(count));
                yields.add(yieldvalue);

                System.out.println("picks in array size "  +picks.size());
                System.out.println("yields in array size "  +yields.size());
            }
        });

        // alertDialog.show();
        final AlertDialog dialog = alertDialog.create();
        dialog.show();
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        edtYield.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                // Check if edittext is empty
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                } else {
                    // Something into edit text. Enable the button.
                    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });

        return yieldvalue;
    }

    private void getregistereddetails(String crid) {

        System.out.println("calldhere");

        MainViewModel viewmodelCropregdata = ViewModelProviders.of(this).get(MainViewModel.class);
        viewmodelCropregdata.getCroprgisteredforweeklydata(farmerID,crid).observe(CropWeeklyData.this, new Observer<List<ModelCropRegistration>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropRegistration> taskEntries) {
                System.out.println("calldhere2");
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    System.out.println("calldhere3");
                    for(ModelCropRegistration taskEntry:taskEntries){
                        System.out.println("calldhere4");
                        if(!array_regdetailsbasedonfidcrid.contains(taskEntry.getCrid())){
                            System.out.println("calldhere5");
                            /*array_regdetailsbasedonfidcrid.add(taskEntry.getOwnerName());
                            ownerid = taskEntry.getOwnernumber();*/
                            yeardisplay = taskEntry.getYear();
                            yearcode = taskEntry.getYearcode();
                            seasoncode = taskEntry.getSeason_code();
                            seasondisplay = taskEntry.getSeason();
                            ownernamedisplay = taskEntry.getOwnername();
                            districtdisplay = taskEntry.getDistrict();
                            talukdisplay = taskEntry.getTaluk();
                            villagedisplay = taskEntry.getVillage();
                            ownerid = taskEntry.getOwnerid();
                            ownerarea = taskEntry.getOwnerarea();
                            surveynumberdisplay = taskEntry.getSurveynumber();
                            croptypedisplay = taskEntry.getCroptype();
                            farming_type_display = taskEntry.getFarming();
                            String cropextent_split = taskEntry.getCropextent();
                            cropextentdisplay = cropextent_split.replaceAll("\\[|\\]", "");
                            String cropname_split = taskEntry.getCropname();
                            cropnamedisplay = cropname_split.replaceAll("\\[|\\]", "");
                            cropsoweddisplay = taskEntry.getSowingdate();
                            totalcrops = Integer.parseInt(taskEntry.getTotalcrops());
                            System.out.print("cropnamedisplay " + cropnamedisplay + " " + "cropextentdisplay " + cropextentdisplay);
                            array_selectcrop.clear();
                            array_selectcrop.add(0,"Select Crop");
                            String cropssaved= taskEntry.getCropname();
                            cropssaved= cropssaved.replaceAll("\\[|\\]", "");
                            String [] convertedstringvalue = cropssaved.split(",");
                            for(String each : convertedstringvalue){
                                array_selectcrop.add(each);
                            }

                            tv_year.setText(yeardisplay);
                            tv_season.setText(seasondisplay);
                            tv_ownername.setText(ownernamedisplay);
                            tv_district.setText(districtdisplay);
                            tv_taluk.setText(talukdisplay);
                            tv_village.setText(villagedisplay);
                            tv_area.setText(ownerarea);
                            tv_surveynumber.setText(surveynumberdisplay);
                            tv_croptype.setText(croptypedisplay);
                            tv_cropextent.setText(cropextentdisplay);
                            tv_cropname.setText(cropnamedisplay);
                            tv_cropsowed.setText(cropsoweddisplay);
                            tv_farming_type.setText(farming_type_display);

                            if(farming_type_display.equals("Organic")){
                                linear_organic.setVisibility(View.VISIBLE);
                                linear_inorganic.setVisibility(View.GONE);
                                linear_natural.setVisibility(View.GONE);
                                et_fertilizer_quantity.setText("");
                                et_pesticide_quantity.setText("");
                                et_natural_quantity.setText("");
                            }else if(farming_type_display.equals("Inorganic")){
                                linear_organic.setVisibility(View.GONE);
                                linear_inorganic.setVisibility(View.VISIBLE);
                                linear_natural.setVisibility(View.GONE);
                                et_manure_quantity.setText("");
                                et_natural_quantity.setText("");
                            }else if(farming_type_display.equals("Natural")){
                                linear_organic.setVisibility(View.GONE);
                                linear_inorganic.setVisibility(View.GONE);
                                linear_natural.setVisibility(View.VISIBLE);
                                et_manure_quantity.setText("");
                                et_fertilizer_quantity.setText("");
                                et_pesticide_quantity.setText("");
                            }

                            /*try{
                                Date date1;
                                Date date2;
                                SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");
                                date1 = dates.parse(currentDate);
                                date2 = dates.parse(taskEntry.getSowingdate());

                                long difference = Math.abs(date2.getTime() - date1.getTime());
                                long differenceDates = difference/(24 * 60 *60 * 1000);
                                String diff_date_value = Long.toString(differenceDates);

                                firstdatediff = Integer.parseInt(diff_date_value);
                                System.out.println("firstdatediff " + firstdatediff);
                            }catch (Exception e){
                                System.out.println("Exception " + e);
                            }

                            if(firstdatediff >= 7) {*/
                            tv_croppickdate.setText(currentDate);
                            getWeek(farmerID, cridvalue, surveynumberdisplay, yearcode, seasoncode, ownerid, cropnamedisplay);
                            setsavedcropname();
                            /*}else{
                                new AlertDialog.Builder(mContext)
                                    .setTitle(getResources().getString(R.string.alert))
                                    .setMessage("Crop picking cannot be possible")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            Intent mainActivity = new Intent(mContext, CropDetails.class);
                                            mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(mainActivity);
                                            finish();
                                            dialog.dismiss();
                                        }

                                    })
                                    .setCancelable(false)
                                    .show();
                            }*/
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
                            .setCancelable(false)
                            .show();
                }

            }
        });
    }

    private void setsavedcropname(){

        ArrayAdapter<String> adapter_savedcrops = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,array_selectcrop);
        adapter_savedcrops.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter_savedcrops.notifyDataSetChanged();
        spinnerselectcrop.setAdapter(adapter_savedcrops);
        spinnerselectcrop.setSelection(0);
        spinnerselectcrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    cropnameValue = spinnerselectcrop.getSelectedItem().toString();
                    // crop_code = String.valueOf(i);
                }else{
                    spinnerselectcrop.setSelection(0);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getWeek(String farmerID, String cridval, String surveynumber, String yearcd, String seasoncd, String ownerid, String cropname) {

        //   calculatedatediff();
        MainViewModel viewmodelWeek = ViewModelProviders.of(this).get(MainViewModel.class);
        viewmodelWeek.getWeek(farmerID,cridval,surveynumber,yearcd,seasoncd,ownerid,cropname).observe(this, new Observer<List<ModelCropWeeklyData>>() {

            @Override
            public void onChanged(@Nullable List<ModelCropWeeklyData> taskEntries) {
                week = taskEntries.size() + 1;

                if (taskEntries != null && !taskEntries.isEmpty()){

                    for(ModelCropWeeklyData taskEntry:taskEntries) {
                        previous_weekdate = taskEntry.getPickingdate();
                    }
                    //currentdate shld be greater than 7 days

                    try{
                        Date date1;
                        Date date2;
                        SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");
                        date1 = dates.parse(currentDate);
                        date2 = dates.parse(previous_weekdate);

                        long difference = Math.abs(date2.getTime() - date1.getTime());
                        long differenceDates = difference/(24 * 60 *60 * 1000);
                        String diff_date_value = Long.toString(differenceDates);

                        dateDifference = Integer.parseInt(diff_date_value);
                        System.out.println("dateDifference " + dateDifference);
                    }catch (Exception e){
                        System.out.println("Exception " + e);
                    }

                    if(dateDifference >= 7) {

                        spinnerweeknumber.setSelection(week, false);
                        System.out.println("week in if " + week);
                    }
                    else {
                        new AlertDialog.Builder(mContext)
                                .setTitle(getResources().getString(R.string.alert))
                                .setMessage("Crop picking already done for this week")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        Intent mainActivity = new Intent(mContext, CropDetails.class);
                                        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(mainActivity);
                                        finish();
                                        dialog.dismiss();
                                    }

                                })
                                .setCancelable(false)
                                .show();
                    }

                }else{
                    spinnerweeknumber.setSelection(week,false);
                    System.out.println("week in else " + week);

                }


            }

        });
    }

    private void camera() {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            AppMediaFolderImagesEnc = new File(root + "/Fruits/" + yearcode + "/" + seasoncode + "/" + farmerID + "/"  + "/WeeklyImages" + "/" + cridvalue);

            if (!AppMediaFolderImagesEnc.exists()) {
                AppMediaFolderImagesEnc.mkdirs();
            }
            imageFile = new File(AppMediaFolderImagesEnc, "Crop_Weekly_Image_week" + weekvalue + ".jpg");
            imageUri = Uri.fromFile(imageFile);
            Imagepath_crop = imageFile.toString();
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            PackageManager packageManager = this.getPackageManager();
            List<ResolveInfo> listCam = packageManager.queryIntentActivities(cameraIntent, 0);
            cameraIntent.setPackage(listCam.get(0).activityInfo.packageName);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, PICTURE_RESULT);

        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    private void cameraEscalate() {

        String root = Environment.getExternalStorageDirectory().toString();
        AppMediaFolderImagesEnc_escalate = new File(root + "/Fruits/" + yearcode + "/" + seasoncode + "/" + farmerID + "/"  +"/WeeklyImages" + cridvalue + "/Escalate");

        if (!AppMediaFolderImagesEnc_escalate.exists()) {
            AppMediaFolderImagesEnc_escalate.mkdirs();
        }
        imageFile_ecalate = new File(AppMediaFolderImagesEnc_escalate, "Crop_Weekly_Image_Escalate"+ weekvalue + ".jpg");
        imageUri_escalate = Uri.fromFile(imageFile_ecalate);
        Imagepath_escalate = imageUri_escalate.toString();
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = CropWeeklyData.this.getPackageManager();
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(cameraIntent, 0);
        cameraIntent.setPackage(listCam.get(0).activityInfo.packageName);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri_escalate);
        startActivityForResult(cameraIntent, ESCALATE_CAPTURE);

    }

    private class SaveWeeklyData extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait.. Saving Crop Weekly Data");
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
                Toast.makeText(mContext, "Crop Weekly Data saved successfully", Toast.LENGTH_SHORT).show();
                Intent cropweeklydata = new Intent(mContext,CropDetails.class);
                cropweeklydata.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(cropweeklydata);
                finish();

            } else if (savedata.equals("exists")){
                new AlertDialog.Builder(mContext)
                        .setTitle(getResources().getString(R.string.alert))
                        .setMessage(getResources().getString(R.string.weekalreadysaved))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        })
                        .show();
            }else {
                Toast.makeText(mContext, "Problem while saving crop weekly data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String parsecropdata() {

        if (AppDatabase.getInstance(this).cropWeeklyDataDAO().isCropWeeklyDataExist(farmerID,cridvalue,weekvalue,surveynumberdisplay,yeardisplay,seasondisplay,ownerid) == 0) {
            // data not exist.
            final ModelCropWeeklyData cropWeekly =new ModelCropWeeklyData(farmerID,cridvalue,yeardisplay,yearcode,seasondisplay,seasoncode,ownerid,ownernamedisplay,districtdisplay,talukdisplay,villagedisplay,ownerarea,surveynumberdisplay,cropextentdisplay,cropnameValue,weekvalue,farming_type_display,farmingtypevalue,quantityValue,pesticidevalue,pesticide_quantityValue,radioescalatevalue,escalationValue,picks.toString(),yields.toString(),crop_Imagepath,escalate_Imagepath,cropsoweddisplay,currentDate,"N");

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (cropWeekly != null) {
                        appDatabase.cropWeeklyDataDAO().insertCropWeeklyData(cropWeekly);
                        weeklyNotification(cridvalue);
                    }
                }
            });

            return "success";
        }
        else {
            // data already exist.

            return "exists";
        }

    }

    private void weeklyNotification(String crID) {
        Intent intent = new Intent( this , CropDetails.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(CropWeeklyData. this,
                default_notification_channel_id )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Weekly Data")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Crop weekly data has been saved successfully for \nCrop Reg. ID - "+crID+", \nCrop - " + cropnameValue + " with total crop picking " + croppickvalue));

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

            Notification.Builder nBuilder = new Notification.Builder(CropWeeklyData.this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher))
                    .setContentTitle("Weekly Data")
                    //  .setContentText("Farmer Id- "+farmerID+" has registered to crop - " + cropnameValue)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setStyle(new Notification.BigTextStyle().bigText("Crop weekly data has been saved successfully for \nCrop Reg. ID - "+crID+", \nCrop - " + cropnameValue + " with total crop picking " + croppickvalue));

            //     ((NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, builder.build()); // build API 16
            assert mNotificationManager != null;
            notificationManager.notify((int)System.currentTimeMillis(), nBuilder.build());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FileOutputStream fileOutputStream = null;


        if (requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                tv_crop_imgtext.setText("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ");

            }
            try {
                Bitmap bitmap = decodeUri(imageUri);
                if (bitmap != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                    byte[] b = baos.toByteArray();
                    bitmap = StoreByteImage(b, imageUri);
                    String compressedImage = compressImage(String.valueOf(imageUri),crop_Imagepath,".jpg");


                    iv_capture_cropimage.setImageBitmap(bitmap);
                    iv_capture_cropimage.setVisibility(View.VISIBLE);


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

                    bmp = timestampItAndSave(bmp, "crop");

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


        if (requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {

            try {
                Bitmap bitmap = decodeUri(imageUri);
                if (bitmap != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                    byte[] b = baos.toByteArray();
                    bitmap = StoreByteImage(b,imageUri);

                    String compressedImage = compressImage(String.valueOf(imageUri), crop_Imagepath,".jpg");

                    tv_crop_imgtext.setVisibility(View.VISIBLE);

                    iv_capture_cropimage.setImageBitmap(bitmap);
                    iv_capture_cropimage.setVisibility(View.VISIBLE);

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

                    bmp = timestampItAndSave(bmp, "crop");


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

            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                // Toast.makeText(FormTwoCaptureMediasActivity.this,
                // "Image not taken", Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(CropWeeklyData.this).create();
                alertDialog.setTitle("ಸೂಚನೆ :");
                alertDialog.setMessage("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ");
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setButton("ಸರಿ",new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog,int which) { } });
                alertDialog.show();
                tv_crop_imgtext.setText("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ");

            } else {
                // Toast.makeText(FormTwoCaptureMediasActivity.this,
                // "Image saved", Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(CropWeeklyData.this).create();
                alertDialog.setTitle("ಸೂಚನೆ :");
                alertDialog.setMessage("ಛಾಯಾಚಿತ್ರ ಉಳಿಸಲಾಗಿದೆ");
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setButton("ಸರಿ",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {}
                });
                alertDialog.show();
                //   isImageMahajarTaken = true;
                tv_crop_imgtext.setText("ಛಾಯಾಚಿತ್ರ ಉಳಿಸಲಾಗಿದೆ");
                //    mixedCrop.setImageMahajarReport(ImageMahajarpath);
                //mixedCrop.setCoordinates(latitude + "," + longitude);
            }
        }

        if(escalate == true){
            if (requestCode == ESCALATE_CAPTURE && resultCode == Activity.RESULT_OK) {
                if (data != null && data.getData() != null) {
                    imageUri_escalate = data.getData();
                    tv_crop_escimgtext.setText("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ");

                }
                try {
                    Bitmap bitmap = decodeUri(imageUri);
                    if (bitmap != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                        byte[] b = baos.toByteArray();
                        bitmap = StoreByteImage(b, imageUri_escalate);
                        String compressedImage = compressImage(String.valueOf(imageUri_escalate),escalate_Imagepath,".jpg");


                        iv_capture_escimage.setImageBitmap(bitmap);
                        iv_capture_escimage.setVisibility(View.VISIBLE);


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

                        String filePath = getRealPathFromURI(imageUri_escalate.toString());
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

                        bmp = timestampItAndSave(bmp, "escalate");

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


            if (requestCode == ESCALATE_CAPTURE && resultCode == Activity.RESULT_OK) {

                try {
                    Bitmap bitmap = decodeUri(imageUri_escalate);
                    if (bitmap != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                        byte[] b = baos.toByteArray();
                        bitmap = StoreByteImage(b,imageUri_escalate);

                        String compressedImage = compressImage(String.valueOf(imageUri_escalate), escalate_Imagepath,".jpg");

                        tv_crop_escimgtext.setVisibility(View.VISIBLE);

                        iv_capture_escimage.setImageBitmap(bitmap);
                        iv_capture_escimage.setVisibility(View.VISIBLE);

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

                        String filePath = getRealPathFromURI(imageUri_escalate.toString());
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

                        bmp = timestampItAndSave(bmp, "escalate");


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

                if (data != null && data.getData() != null) {
                    imageUri_escalate = data.getData();
                    // Toast.makeText(FormTwoCaptureMediasActivity.this,
                    // "Image not taken", Toast.LENGTH_SHORT).show();
                    AlertDialog alertDialog = new AlertDialog.Builder(CropWeeklyData.this).create();
                    alertDialog.setTitle("ಸೂಚನೆ :");
                    alertDialog.setMessage("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ");
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setButton("ಸರಿ",new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog,int which) { } });
                    alertDialog.show();
                    tv_crop_escimgtext.setText("ಛಾಯಾಚಿತ್ರ ಸೆರೆಹಿಡಿದಿಲ್ಲ");

                } else {
                    // Toast.makeText(FormTwoCaptureMediasActivity.this,
                    // "Image saved", Toast.LENGTH_SHORT).show();
                    AlertDialog alertDialog = new AlertDialog.Builder(CropWeeklyData.this).create();
                    alertDialog.setTitle("ಸೂಚನೆ :");
                    alertDialog.setMessage("ಛಾಯಾಚಿತ್ರ ಉಳಿಸಲಾಗಿದೆ");
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setButton("ಸರಿ",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {}
                    });
                    alertDialog.show();
                    //   isImageMahajarTaken = true;
                    tv_crop_escimgtext.setText("ಛಾಯಾಚಿತ್ರ ಉಳಿಸಲಾಗಿದೆ");
                    //    mixedCrop.setImageMahajarReport(ImageMahajarpath);
                    //mixedCrop.setCoordinates(latitude + "," + longitude);
                }
            }
        }
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

            bmp = timestampItAndSave(bmp, imgtype);

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
        String filename = getFilename();
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

    public String getFilename() {
        //  File file = new File(crop_Imagepath);

        String uriSting = String.valueOf((imageUri));
        return uriSting;

    }

    private Bitmap timestampItAndSave(Bitmap bmp,String imgType) {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        Bitmap canvasBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Canvas imageCanvas = new Canvas(canvasBitmap);
        Paint imagePaint = new Paint();
        imagePaint.setTextAlign(Paint.Align.CENTER);
        imagePaint.setTextSize(25f);
        imagePaint.setColor(Color.RED);
        imageCanvas.drawText(currentDateTimeString.toString(),canvasBitmap.getWidth() / 2, (canvasBitmap.getHeight() - 15),imagePaint);

        if(imgType.equals("crop")){
            imagePaint.setColor(getResources().getColor(R.color.orange));
            imageCanvas.drawText("Farmer ID : "+farmerID , canvasBitmap.getWidth() / 2,25, imagePaint);
            imageCanvas.drawText("Crop Reg ID : "+cridvalue , canvasBitmap.getWidth() / 2,50, imagePaint);
            imageCanvas.drawText(surveynumberdisplay + "  |  " + cropnameValue + "  |  " + croptypedisplay, canvasBitmap.getWidth() / 2, (canvasBitmap.getHeight() - 50), imagePaint);

        }else if (imgType.equals("escalate")){
            imagePaint.setColor(Color.RED);
            imageCanvas.drawText("Crop Disease Image", canvasBitmap.getWidth() / 2,25, imagePaint);
            imageCanvas.drawText("Farmer ID : "+farmerID, canvasBitmap.getWidth() / 2,50, imagePaint);
            imageCanvas.drawText("Crop Reg ID : "+cridvalue , canvasBitmap.getWidth() / 2,75, imagePaint);
            imageCanvas.drawText(surveynumberdisplay + "  |  " + cropnameValue + "  |  " + croptypedisplay, canvasBitmap.getWidth() / 2, (canvasBitmap.getHeight() - 50), imagePaint);

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
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

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



            String url = String.valueOf(imageUri);

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
                    //     new GetSyncCropVarietyMaster().execute();
                } else {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Alert")
                            .setMessage("Please Connect To Internet")

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

    private class SaveMultipickData extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait.. Saving crop multipick data");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String savemultipick;
            savemultipick = parsemultipickdata();
            return savemultipick;
        }

        @Override
        protected void onPostExecute(String savemultipick) {

            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (savemultipick.equals("success")) {
                Toast.makeText(mContext, "Crop weekly multipick data saved successfully", Toast.LENGTH_SHORT).show();
                new SaveWeeklyData().execute();

            }
            /* else if (savemultipick.equals("exists")){
                new AlertDialog.Builder(mContext)
                        .setTitle(getResources().getString(R.string.alert))
                        .setMessage(getResources().getString(R.string.weekalreadysaved))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        })
                        .show();
            }*/else {
                Toast.makeText(mContext, "Problem while saving crop weekly data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String parsemultipickdata() {
        //here
        /*for(int i = 1; i<=Integer.parseInt(croppickvalue);i++){

        }*/
          /*  final ModelCropMultipickingData multipickingData =new ModelCropMultipickingData(farmerID,cridvalue,yeardisplay,yearcode,seasondisplay,seasoncode,ownerid,ownernamedisplay,ownerarea,surveynumberdisplay,cropnamedisplay,cropextentdisplay,cropsoweddisplay,currentDate,weekvalue,croppick,yield,croppickvalue);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (multipickingData != null) {
                        appDatabase.cropmultipickDataDAO().insertCropweeklypickingData(multipickingData);

                    }
                }
            });*/
        return "success";

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

    private static byte[] loadFile(File file) throws IOException {
        // TODO Auto-generated method stub
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
            System.out.println(offset);
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }

}
