package org.nic.fruits;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.nic.fruits.R;

import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;
import org.nic.fruits.pojo.ModelCropFertilizerMasternpk;
import org.nic.fruits.pojo.ModelCropMaster;
import org.nic.fruits.pojo.ModelCropMasterType;
import org.nic.fruits.pojo.ModelCropSurveyDetails;
import org.nic.fruits.pojo.ModelFarmerDetails;
import org.nic.fruits.pojo.ModelFarmerLandDeatails;
import org.nic.fruits.pojo.ModelFertilizerCropMaster;
import org.nic.fruits.pojo.ModelFertilizerNameMaster;
import org.nic.fruits.pojo.ModelForeCastDetails;
import org.nic.fruits.pojo.ModelIrrigationType;
import org.nic.fruits.pojo.ModelOwnerDetails;
import org.nic.fruits.pojo.ModelPaymentDetails;
import org.nic.fruits.pojo.ModelPlantAgeMaster;
import org.nic.fruits.pojo.ModelweatherDetails;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//Programming by Harsha  for version 1.0 release
public class ValidateOTPActivity extends AppCompatActivity {

    private String generatedOTP;
    private String farmerID;
    private EditText editTextValidateOTP;
    private Button btnValidate,btnResend;
    private Context mContext;
    private AppDatabase appDatabase;
    public ProgressDialog progressDialog;
    private String keyValue;
    private static final int MY_PERMISSIONS_setAudioSource = 29;
    private static final int CAMERA_PERMISSION = 100;
    private boolean dataPresent = false;
    private List<ModelFarmerLandDeatails> landListData = new ArrayList<>();
    private String aadhaarNumber;
    private String mobileNumber;
    private String otp;
    ModelCropMaster modelCropMaster;
    ModelFertilizerCropMaster modelFertilizerCropMaster;
    ModelPlantAgeMaster modelPlantAgeMaster;
    ModelFertilizerNameMaster modelFertilizerNameMaster;
    ModelCropFertilizerMasternpk modelCropFertilizerMasternpk;
    ModelCropMasterType modelCropMasterType;
    ModelIrrigationType modelIrrigationType;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_otp);
        mContext = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_setAudioSource);
            } else {
                Log.d("Home", "Already granted access");
            }
        }

        editTextValidateOTP = (EditText) findViewById(R.id.et_Otp);
        btnValidate = (Button) findViewById(R.id.btn_validate);
        btnResend = (Button) findViewById(R.id.btn_resend);
        progressDialog = new ProgressDialog(mContext);

        // test
        Intent intent = getIntent();
        generatedOTP = "1234";//intent.getStringExtra("OTP");
        farmerID = intent.getStringExtra("FarmerID");
        aadhaarNumber = "685440321377";//intent.getStringExtra("AadhaarNumber");
        mobileNumber = "9663869607";//intent.getStringExtra("MobileNumber");
        keyValue = "4513D4EDA84E7EE00D5EF91AA634C0";
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        boolean res = setupViewModel();

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEmptyEditText = isEmpty(editTextValidateOTP);

                if (Utils.isNetworkConnected(mContext)) {
                    if (!isEmptyEditText) {
                        //test
/*
                        farmerID =  editTextValidateOTP.getText().toString();

                        Toast.makeText(mContext, "Entered OTP is Correct", Toast.LENGTH_SHORT).show();

                        SharedPreferences prefs = getSharedPreferences("com.example.fruites",
                                MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("FarmerID", farmerID);
                        editor.putString("KeyValue", "4513D4EDA84E7EE00D5EF91AA634C0");
                        editor.commit();

                        if (setupViewModel()) {
                            System.out.println("calling aa inside setupViewModel");
                            SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                            Intent mainActivityIntent = new Intent(ValidateOTPActivity.this, FruitsHomeActivity.class);
                            startActivity(mainActivityIntent);
                        }else {
                            new GetLandDetails().execute();
                            System.out.println("calling aa outside setupViewModel");
                        }
*/

                        String inputOtp = editTextValidateOTP.getText().toString();
                        if (inputOtp.equals("1234")) { //generatedOTP
                            Toast.makeText(mContext, "Entered OTP is Correct", Toast.LENGTH_SHORT).show();

                            SharedPreferences prefs = getSharedPreferences("com.example.fruites",
                                    MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("FarmerID", farmerID);
                            editor.putString("KeyValue", "4513D4EDA84E7EE00D5EF91AA634C0");
                            editor.commit();

                            if (setupViewModel()) {
                                System.out.println("calling aa inside setupViewModel");
                                SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                                Intent mainActivityIntent = new Intent(ValidateOTPActivity.this, FruitsHomeActivity.class);
                                startActivity(mainActivityIntent);
                            } else {
                                //     new SaveCropData().execute();
                                new GetLandDetails().execute();
                                System.out.println("calling aa outside setupViewModel");
                            }
                        } else {
                            Toast.makeText(mContext, "Please Enter the Correct OTP", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Enter the OTP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Internet is Not Avilable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(mContext)) {
                    //    new GetOTP().execute();
                    Intent validateOtpIntent = new Intent(mContext, ValidateOTPActivity.class);
                    validateOtpIntent.putExtra("OTP", "1234");
                    validateOtpIntent.putExtra("FarmerID", farmerID);
                    validateOtpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(validateOtpIntent);
                    finish();
                } else {
                    Toast.makeText(mContext, "Internet is Not Avilable", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean setupViewModel() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                landListData = appDatabase.landDetailsDAO().getAllFarmerLandBasedOnFidNoLive(farmerID);
                if (landListData.size() > 0) {
                    dataPresent = true;
                }
            }
        });
        return dataPresent;
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_setAudioSource: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Home", "Permission Granted");
                } else {
                    Log.d("Home", "Permission Failed");
                    Toast.makeText(mContext, "You must allow permission record audio to your mobile device.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            case CAMERA_PERMISSION:{
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

    /*private class GetOTP extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please Wait OTP is Generating");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String farmerDataString = new String();
            try {
                SoapProxy proxy2 = new SoapProxy(mContext);
              farmerDataString = proxy2.getOTP(keyValue, mobileNumber, aadhaarNumber);
                farmerDataString = farmerDataString.replaceAll("[^\\x20-\\x7e]", "");
//                farmerDataString = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><node><ResponseStatus><Status>1</Status><FarmerID>FID0101000000028</FarmerID><OTP>23273</OTP></ResponseStatus></node>";

                if (farmerDataString.equalsIgnoreCase("Failure")) {
                    farmerDataString = "InternetconectionProblem";
                } else if (farmerDataString.equalsIgnoreCase("Failure1")) {
                    farmerDataString = "InternetconectionProblem";
                } else if (farmerDataString.equalsIgnoreCase("anyType{}")) {
                    farmerDataString = "anyType{}";
                } else {
                    farmerDataString = ParseOTP(farmerDataString);
                }
            } catch (Exception e) {
                e.printStackTrace();
                farmerDataString = "InternetconectionProblem";
            }
            return farmerDataString;
        }

        @Override
        protected void onPostExecute(String hortidownloadPostInspection) {
            this.dialog.dismiss();
            if (hortidownloadPostInspection.equals("Error")) {
                Toast.makeText(mContext, "Internet Connection unavailable", Toast.LENGTH_LONG).show();
            } else if (hortidownloadPostInspection.equals("InternetconectionProblem")) {
                Toast.makeText(mContext, "Internet Disconnected", Toast.LENGTH_LONG).show();
            } else if (hortidownloadPostInspection.equals("Invalid Mobile No")) {
                Toast.makeText(mContext, "Invalid  Mobile Number", Toast.LENGTH_LONG).show();
            } else if (hortidownloadPostInspection.equals("Mobile No should start with 6 or 7 or 8")) {
                Toast.makeText(mContext, "Mobile No should start with 6 or 7 or 8 or 9", Toast.LENGTH_LONG).show();
            } else if (hortidownloadPostInspection.equals("Invalid Aadhar Number")) {
                Toast.makeText(mContext, "Aadhar Number is Invalid", Toast.LENGTH_LONG).show();
            } else if (hortidownloadPostInspection.equals("Aadhar Number Not exists")) {
                Toast.makeText(mContext, "Aadhar Number Not Exists in Fruits", Toast.LENGTH_LONG).show();
            } else if (hortidownloadPostInspection.equals("Given Mobile and Aadhar Number is not matching")) {
                Toast.makeText(mContext, "Given Mobile Number is not Matching with Aadhar Number", Toast.LENGTH_LONG).show();
            } else if (hortidownloadPostInspection.equals("Error In Process")) {
                Toast.makeText(mContext, "Exception Or Error", Toast.LENGTH_LONG).show();
            } else if (hortidownloadPostInspection.equals("1") && otp != null && farmerID != null) {
                Toast.makeText(mContext, "OTP Sent Successfully", Toast.LENGTH_LONG).show();
                Intent validateOtpIntent = new Intent(mContext, ValidateOTPActivity.class);
                validateOtpIntent.putExtra("OTP", otp);
                validateOtpIntent.putExtra("FarmerID", farmerID);
                validateOtpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(validateOtpIntent);
                finish();
            } else if (hortidownloadPostInspection.equals("Failureparsing")) {
                Toast.makeText(mContext, "Failure Occured", Toast.LENGTH_LONG).show();
            } else {

            }
        }
    }

    public String ParseOTP(String npcStatuesCheck) {
        String Parsemessage = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setCharacterStream(new StringReader(npcStatuesCheck));
            Document docxml = builder.parse(src);
            String status = docxml.getElementsByTagName("Status").item(0).getTextContent();
            if (status.equals("1")) {
                otp = docxml.getElementsByTagName("OTP").item(0).getTextContent();
                farmerID = docxml.getElementsByTagName("FarmerID").item(0).getTextContent();
            }

            Parsemessage = status;

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
    }*/

    private class GetLandDetails extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(ValidateOTPActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait..Collecting Farmer Details. It will take some time.");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String landDetailsString = new String();
            try {

                SoapProxy proxy2 = new SoapProxy(mContext);
                landDetailsString = proxy2.getLandDetails(keyValue, farmerID);
                landDetailsString = landDetailsString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                landDetailsString.replaceAll(">\\s+<", "><").trim();

                if (landDetailsString.equalsIgnoreCase("Failure")) {
                    landDetailsString = "InternetconectionProblem";
                } else if (landDetailsString.equalsIgnoreCase("Failure1")) {
                    landDetailsString = "InternetconectionProblem";
                } else if (landDetailsString.equalsIgnoreCase("anyType{}")) {
                    landDetailsString = "anyType{}";
                } else {
                    landDetailsString = parseAndInsertLandDetails(landDetailsString);
                }
            } catch (Exception e) {
                e.printStackTrace();
                landDetailsString = "InternetconectionProblem";
            }
            String cropSurveyDetailsString = new String();
            try {
                SoapProxy proxy2 = new SoapProxy(mContext);
                cropSurveyDetailsString = proxy2.getCropSurveyData(keyValue, farmerID);
                cropSurveyDetailsString = cropSurveyDetailsString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                cropSurveyDetailsString = cropSurveyDetailsString.replaceAll(">\\s+<", "><").trim();

                if (cropSurveyDetailsString.equalsIgnoreCase("Failure")) {
                    cropSurveyDetailsString = "InternetconectionProblem";
                } else if (cropSurveyDetailsString.equalsIgnoreCase("Failure1")) {
                    cropSurveyDetailsString = "InternetconectionProblem";
                } else if (cropSurveyDetailsString.equalsIgnoreCase("anyType{}")) {
                    cropSurveyDetailsString = "anyType{}";
                } else {
                    cropSurveyDetailsString = parseCropSurveyDetails(cropSurveyDetailsString);
                }
            } catch (Exception e) {
                e.printStackTrace();
                cropSurveyDetailsString = "InternetconectionProblem";
            }
            String stringFarmerDetails = new String();
            try {
                SoapProxy proxy2 = new SoapProxy(mContext);
                stringFarmerDetails = proxy2.getFarmerData(keyValue, farmerID);
                stringFarmerDetails = stringFarmerDetails.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                stringFarmerDetails = stringFarmerDetails.replaceAll(">\\s+<", "><").trim();

                if (stringFarmerDetails.equalsIgnoreCase("NoUpdates")) {

                } else if (stringFarmerDetails.equalsIgnoreCase("Failure")) {
                    stringFarmerDetails = "InternetconectionProblem";
                } else if (stringFarmerDetails.equalsIgnoreCase("Failure1")) {
                    stringFarmerDetails = "InternetconectionProblem";
                } else if (stringFarmerDetails.equalsIgnoreCase("anyType{}")) {
                    stringFarmerDetails = "anyType{}";
                } else {
                    stringFarmerDetails = parseFarmerDetails(stringFarmerDetails);
                }

            } catch (Exception e) {
                e.printStackTrace();
                stringFarmerDetails = "InternetconectionProblem";
            }
            String paymentDetailsDetailsString = new String();
            try {

                SoapProxy proxy2 = new SoapProxy(mContext);
                paymentDetailsDetailsString = proxy2.getPaymentDetails(keyValue, farmerID);
                paymentDetailsDetailsString = paymentDetailsDetailsString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                paymentDetailsDetailsString = paymentDetailsDetailsString.replaceAll(">\\s+<", "><").trim();
                if (paymentDetailsDetailsString.equalsIgnoreCase("NoUpdates")) {

                } else if (paymentDetailsDetailsString.equalsIgnoreCase("Failure")) {
                    paymentDetailsDetailsString = "InternetconectionProblem";
                } else if (paymentDetailsDetailsString.equalsIgnoreCase("Failure1")) {
                    paymentDetailsDetailsString = "InternetconectionProblem";
                } else if (paymentDetailsDetailsString.equalsIgnoreCase("anyType{}")) {
                    paymentDetailsDetailsString = "anyType{}";
                } else {
                    paymentDetailsDetailsString = parsePaymentDetails(paymentDetailsDetailsString);
                }
            } catch (Exception e) {
                e.printStackTrace();
                paymentDetailsDetailsString = "InternetconectionProblem";
            }

            String ownerDetails = new String();
            try {

                SoapProxy proxy2 = new SoapProxy(mContext);
                ownerDetails = proxy2.getLandDetails(keyValue, farmerID);
                ownerDetails = ownerDetails.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                ownerDetails.replaceAll(">\\s+<", "><").trim();

                if (ownerDetails.equalsIgnoreCase("Failure")) {
                    ownerDetails = "InternetconectionProblem";
                } else if (ownerDetails.equalsIgnoreCase("Failure1")) {
                    ownerDetails = "InternetconectionProblem";
                } else if (ownerDetails.equalsIgnoreCase("anyType{}")) {
                    ownerDetails = "anyType{}";
                } else {
                    ownerDetails = parseAndInsertOwnerDetails(ownerDetails);


                }
            } catch (Exception e) {
                e.printStackTrace();
                ownerDetails = "InternetconectionProblem";
            }

            /*String cropVarietyData = new String();
            try {
                SoapProxy proxy2 = new SoapProxy(mContext);
                cropVarietyData = proxy2.getCropVarietyData(keyValue, farmerID);
                cropVarietyData = cropVarietyData.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();

                if (cropVarietyData.equalsIgnoreCase("Failure")) {
                    cropVarietyData = "InternetconectionProblem";
                } else if (cropVarietyData.equalsIgnoreCase("Failure1")) {
                    cropVarietyData = "InternetconectionProblem";
                } else if (cropVarietyData.equalsIgnoreCase("anyType{}")) {
                    cropVarietyData = "anyType{}";
                } else {
                    String parser = parseInsertCropVarietyData(cropVarietyData);
                    cropVarietyData = parser;
                }

            } catch (Exception e) {
                e.printStackTrace();
                cropVarietyData = "InternetconectionProblem";
            }*/

            String cropdata = new String();

            try {
                cropdata = parsecropmaster();

            } catch (Exception e) {
                e.printStackTrace();
                cropdata = "InternetconectionProblem";
            }

            String fertilizercropmaster = new String();
            try {
                fertilizercropmaster = parsefertilizercropmaster();

            } catch (Exception e) {
                e.printStackTrace();
                fertilizercropmaster = "InternetconectionProblem";
            }

            String plantagemaster = new String();
            try {
                plantagemaster = parseplantagemaster();

            } catch (Exception e) {
                e.printStackTrace();
                plantagemaster = "InternetconectionProblem";
            }

            String fertilizernamemaster = new String();
            try {
                fertilizernamemaster = parsefertlizernamemaster();

            } catch (Exception e) {
                e.printStackTrace();
                fertilizernamemaster = "InternetconectionProblem";
            }

            String cropfertilizermaster = new String();
            try {
                cropfertilizermaster = parsecropfertlizermaster();

            } catch (Exception e) {
                e.printStackTrace();
                cropfertilizermaster = "InternetconectionProblem";
            }


            String croptypemaster = new String();
            try {
                croptypemaster = parsecroptype();

            }
            catch (Exception e) {
                e.printStackTrace();
                croptypemaster = "InternetconectionProblem";
            }

            String irrigationtypemaster = new String();
            try {
                irrigationtypemaster = parseirrigationtype();

            }
            catch (Exception e) {
                e.printStackTrace();
                irrigationtypemaster = "InternetconectionProblem";
            }


            String weatherDataString = new String();
            try {
                SoapProxy proxy2 = new SoapProxy(mContext);
                weatherDataString = proxy2.getWeatherData(keyValue, farmerID);
                weatherDataString = weatherDataString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();

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

            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (weatherDataString.equals("Success")) {
                Toast.makeText(mContext, "Farmer Details Downloaded successfully", Toast.LENGTH_SHORT).show();
                SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                Intent mainActivityIntent = new Intent(ValidateOTPActivity.this, FruitsHomeActivity.class);
                startActivity(mainActivityIntent);
            } else {
                Toast.makeText(mContext, "Problem While Downloading Details", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private String parsecropfertlizermaster() {

        String cropfertilizer= new String();
        NodeList nodeList;
        String cf_id = "";
        String cf_crop_code = "";
        String cf_irrigation_type = "";
        String cf_plant_age = "";
        String cf_nitrogen = "";
        String cf_phosphorous = "";
        String cf_potash = "";


        try {
            InputStream is1 = getAssets().open("CropFertilizermaster.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(is1);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName("CropFertilizersData");
            for (int k = 0; k < nodeList.getLength(); k++) {
                Node node = nodeList.item(k);
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    Node temp = node.getChildNodes().item(i);
                    if (temp.getNodeName().equalsIgnoreCase("CF_Id")) {
                        cf_id = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("CF_Crop_Code")) {
                        cf_crop_code = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("CF_Irrigation_Type")) {
                        cf_irrigation_type = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("CF_Plant_Age")) {
                        cf_plant_age = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("CF_Nitrogen")) {
                        cf_nitrogen = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("CF_Phosphorous")) {
                        cf_phosphorous = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("CF_Potash")) {
                        cf_potash = temp.getTextContent();
                    }

                    modelCropFertilizerMasternpk = new ModelCropFertilizerMasternpk(cf_id, cf_crop_code, cf_irrigation_type, cf_plant_age,cf_nitrogen,cf_phosphorous,cf_potash);
                }
                // appDatabase.cropMasterDao().deletecropmaster();
                if (modelCropFertilizerMasternpk != null) {
                    appDatabase.cropFertilizerMasterDAO().insertfertilizercrop(modelCropFertilizerMasternpk);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);

            }
            cropfertilizer = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            cropfertilizer = "Failure";
        }
        return cropfertilizer;

    }

    private String parsefertlizernamemaster() {

        String fertilizername= new String();
        NodeList nodeList;
        String feid = "";
        String fename = "";
        String fekname = "";
        String fertilizertype = "";
        String fertilizernitrogen = "";
        String fertilizerphosphorous = "";
        String fertilizerpotash = "";
        String fertilizernutrient = "";


        try {
            InputStream is1 = getAssets().open("FertilizerMaster.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(is1);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName("FertilizersData");
            for (int k = 0; k < nodeList.getLength(); k++) {
                Node node = nodeList.item(k);
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    Node temp = node.getChildNodes().item(i);
                    if (temp.getNodeName().equalsIgnoreCase("FM_Id")) {
                        feid = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("FM_Name")) {
                        fename = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("FM_KName")) {
                        fekname = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("FM_Type")) {
                        fertilizertype = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("FM_Nitrogen")) {
                        fertilizernitrogen = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("FM_Phosphorous")) {
                        fertilizerphosphorous = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("FM_Potash")) {
                        fertilizerpotash = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("FM_No_Nutrient")) {
                        fertilizernutrient = temp.getTextContent();
                    }

                    modelFertilizerNameMaster = new ModelFertilizerNameMaster(feid, fename,fekname, fertilizertype, fertilizernitrogen,fertilizerphosphorous,fertilizerpotash,fertilizernutrient);
                }
                // appDatabase.cropMasterDao().deletecropmaster();
                if (modelFertilizerNameMaster != null) {
                    appDatabase.fertilizerNameMasterDAO().insertFertilizerNames(modelFertilizerNameMaster);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);

            }
            fertilizername = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            fertilizername = "Failure";
        }
        return fertilizername;

    }

    private String parseplantagemaster() {

        String plantage= new String();
        NodeList nodeList;
        String plantid = "";
        String plantname = "";

        try {
            InputStream is1 = getAssets().open("PlantAgeMaster.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(is1);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName("PlantAgeData");
            for (int k = 0; k < nodeList.getLength(); k++) {
                Node node = nodeList.item(k);
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    Node temp = node.getChildNodes().item(i);
                    if (temp.getNodeName().equalsIgnoreCase("PAM_Id")) {
                        plantid = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("PAM_Name")) {
                        plantname = temp.getTextContent();
                    }


                    modelPlantAgeMaster = new ModelPlantAgeMaster(plantid,plantname);
                }
                // appDatabase.cropMasterDao().deletecropmaster();
                if (modelPlantAgeMaster != null) {
                    appDatabase.plantAgeMasterDAO().insertplantage(modelPlantAgeMaster);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);

            }
            plantage = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            plantage = "Failure";
        }
        return plantage;

    }

    private String parsefertilizercropmaster() {

        String fcropdata= new String();
        NodeList nodeList;
        String cropcode = "";
        String cropname_eng = "";
        String cropname_kn = "";
        String croptype = "";

        try {
            InputStream is1 = getAssets().open("CropMaster_F.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(is1);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName("CropData");
            for (int k = 0; k < nodeList.getLength(); k++) {
                Node node = nodeList.item(k);
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    Node temp = node.getChildNodes().item(i);
                    if (temp.getNodeName().equalsIgnoreCase("Crop_Code")) {
                        cropcode = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("Crop_Name_Eng")) {
                        cropname_eng = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("Crop_Name_Kan")) {
                        cropname_kn = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("Crop_Type")) {
                        croptype = temp.getTextContent();
                    }

                    modelFertilizerCropMaster = new ModelFertilizerCropMaster(cropcode, cropname_eng, cropname_kn, croptype);
                }
                // appDatabase.cropMasterDao().deletecropmaster();
                if (modelFertilizerCropMaster != null) {
                    appDatabase.fertilizerCropMasterDAO().insertcrop(modelFertilizerCropMaster);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);

            }
            fcropdata = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            fcropdata = "Failure";
        }
        return fcropdata;
    }

    public String parseAndInsertLandDetails(String landDataString)    {
        String Parsemessage = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            String s1 = URLDecoder.decode(landDataString, "UTF-8");
            Document docxml = builder.parse(new ByteArrayInputStream(s1.getBytes()));
            docxml.getDocumentElement().normalize();


            String status = docxml.getElementsByTagName("Status").item(0).getTextContent();
            if (status.equals("1")) {
                Parsemessage = "Success";
//                appDatabase.landDetailsDAO().deleteAllLand();
            }
            NodeList nodeList = docxml.getElementsByTagName("LandDetails");
            for (int k = 0; k < nodeList.getLength(); k++) {
                Node node = nodeList.item(k);
                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node landDetailsNode = node.getChildNodes().item(j);

                    String framerName = null;
                    String districtName = null;
                    String talukName = null;
                    String hobliName = null;
                    String villageName = null;
                    String surveyNumber = null;
                    String area = null;

                    for (int i = 0; i < landDetailsNode.getChildNodes().getLength(); i++) {

                        Node childWeather = landDetailsNode.getChildNodes().item(i);
                        if (childWeather.getNodeName().equalsIgnoreCase("ownername")) {
                            framerName = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("districtname")) {
                            districtName = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("talukname")) {
                            talukName = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("hobliname")) {
                            hobliName = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("villagename")) {
                            villageName = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("surveynumber")) {
                            surveyNumber = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("area")) {
                            area = childWeather.getTextContent();
                        }

                    }

                    final ModelFarmerLandDeatails farmerLandDetails = new ModelFarmerLandDeatails(farmerID, framerName, districtName, talukName, hobliName, villageName, surveyNumber, area);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (farmerLandDetails != null) {
                                appDatabase.landDetailsDAO().insertLandDetails(farmerLandDetails);
                            }
//                            finish();
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

    public String parseCropSurveyDetails(String cropSurveyString) {
        String Parsemessage = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            String s1 = URLDecoder.decode(cropSurveyString, "UTF-8");
            Document docxml = builder.parse(new ByteArrayInputStream(s1.getBytes()));
            docxml.getDocumentElement().normalize();

            String status = docxml.getElementsByTagName("Status").item(0).getTextContent();
            if (status.equals("1")) {
                Parsemessage = "Success";

            }
            NodeList nodeList = docxml.getElementsByTagName("CropSurveyDetails");
            for (int k = 0; k < nodeList.getLength(); k++) {
                Node node = nodeList.item(k);
                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node landDetailsNode = node.getChildNodes().item(j);
                    String framerName = null;
                    String districtName = null;
                    String talukName = null;
                    String hobliName = null;
                    String villageName = null;
                    String surveyNumber = null;
                    String cropArea = null;
                    String year = null;
                    String season = null;
                    String cropName = null;
                    for (int i = 0; i < landDetailsNode.getChildNodes().getLength(); i++) {
                        Node childWeather = landDetailsNode.getChildNodes().item(i);
                        if (childWeather.getNodeName().equalsIgnoreCase("ownername")) {
                            framerName = childWeather.getTextContent();
                        }
                        if (childWeather.getNodeName().equalsIgnoreCase("districtname")) {
                            districtName = childWeather.getTextContent();
                        }
                        if (childWeather.getNodeName().equalsIgnoreCase("talukname")) {
                            talukName = childWeather.getTextContent();
                        }
                        if (childWeather.getNodeName().equalsIgnoreCase("hobliname")) {
                            hobliName = childWeather.getTextContent();
                        }
                        if (childWeather.getNodeName().equalsIgnoreCase("villagename")) {
                            villageName = childWeather.getTextContent();
                        }
                        if (childWeather.getNodeName().equalsIgnoreCase("SurveyNo")) {
                            surveyNumber = childWeather.getTextContent();
                        }
                        if (childWeather.getNodeName().equalsIgnoreCase("croparea")) {
                            cropArea = childWeather.getTextContent();
                        }
                        if (childWeather.getNodeName().equalsIgnoreCase("yearname")) {
                            year = childWeather.getTextContent();
                        }
                        if (childWeather.getNodeName().equalsIgnoreCase("seasonname")) {
                            season = childWeather.getTextContent();
                        }
                        if (childWeather.getNodeName().equalsIgnoreCase("cropname")) {
                            cropName = childWeather.getTextContent();
                        }
                    }

                    final ModelCropSurveyDetails cropsDetails = new ModelCropSurveyDetails(farmerID, framerName, districtName, talukName, hobliName, villageName, surveyNumber, cropArea, year, season, cropName);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (cropsDetails != null) {
                                appDatabase.cropDetailsDAO().insertCropDetails(cropsDetails);
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

    public String parseFarmerDetails(String farmerDataString) {
        String Parsemessage = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            String s1 = URLDecoder.decode(farmerDataString, "UTF-8");
            Document docxml = builder.parse(new ByteArrayInputStream(s1.getBytes()));
            docxml.getDocumentElement().normalize();

            String status = docxml.getElementsByTagName("Status").item(0).getTextContent();
            if (status.equals("1")) {
                Parsemessage = "Success";
//                appDatabase.farmerDetailsDAO().deleteAllFarmer();
            }
            NodeList nodeList = docxml.getElementsByTagName("PersonalDetails");
            for (int k = 0; k < nodeList.getLength(); k++) {

                String framerNameInKannada = null;
                String framerNameInEnglish = null;
                String framerFatherNameInKannada = null;
                String framerFatherNameInEnglish = null;
                String age = null;
                String gender = null;
                String districtName = null;
                String talukName = null;
                String hobliName = null;
                String villageName = null;
                String address = null;
                String cast = null;
                String physicallyDisabled = null;
                String minorities = null;
                String farmerType = null;

                Node node = nodeList.item(k);
                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node farmerDetails = node.getChildNodes().item(j);
                    if (farmerDetails.getNodeName().equalsIgnoreCase("FarmerNameKannada")) {
                        framerNameInKannada = farmerDetails.getTextContent();
                    }
                    if (farmerDetails.getNodeName().equalsIgnoreCase("FarmerName")) {
                        framerNameInEnglish = farmerDetails.getTextContent();
                    }

                    if (farmerDetails.getNodeName().equalsIgnoreCase("FatherNameKannada")) {
                        framerFatherNameInKannada = farmerDetails.getTextContent();
                    }

                    if (farmerDetails.getNodeName().equalsIgnoreCase("FatherName")) {
                        framerFatherNameInEnglish = farmerDetails.getTextContent();
                    }

                    if (farmerDetails.getNodeName().equalsIgnoreCase("Age")) {
                        age = farmerDetails.getTextContent();
                    }

                    if (farmerDetails.getNodeName().equalsIgnoreCase("Gender")) {
                        gender = farmerDetails.getTextContent();
                    }


                    if (farmerDetails.getNodeName().equalsIgnoreCase("districtname")) {
                        districtName = farmerDetails.getTextContent();
                    }

                    if (farmerDetails.getNodeName().equalsIgnoreCase("talukname")) {
                        talukName = farmerDetails.getTextContent();
                    }

                    if (farmerDetails.getNodeName().equalsIgnoreCase("hobliname")) {
                        hobliName = farmerDetails.getTextContent();
                    }

                    if (farmerDetails.getNodeName().equalsIgnoreCase("villagename")) {
                        villageName = farmerDetails.getTextContent();
                    }

                    if (farmerDetails.getNodeName().equalsIgnoreCase("residentialaddress")) {
                        address = farmerDetails.getTextContent();
                    }

                    if (farmerDetails.getNodeName().equalsIgnoreCase("caste")) {
                        cast = farmerDetails.getTextContent();
                    }

                    if (farmerDetails.getNodeName().equalsIgnoreCase("ph")) {
                        physicallyDisabled = farmerDetails.getTextContent();
                    }

                    if (farmerDetails.getNodeName().equalsIgnoreCase("MinoritiesStatus")) {
                        minorities = farmerDetails.getTextContent();
                    }

                    if (farmerDetails.getNodeName().equalsIgnoreCase("FarmerCategory")) {
                        farmerType = farmerDetails.getTextContent();
                    }
                }
                final ModelFarmerDetails farmerDetails = new ModelFarmerDetails(farmerID, framerNameInKannada, framerNameInEnglish, framerFatherNameInKannada, framerFatherNameInEnglish, age, gender, districtName, talukName, hobliName, villageName, address, cast, physicallyDisabled, minorities, farmerType);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (farmerDetails != null) {
                            appDatabase.farmerDetailsDAO().insertFarmerDetails(farmerDetails);
                        }

//                        finish();
                    }
                });
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

    public String parsePaymentDetails(String paymentDetails) {
        String Parsemessage = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            String s1 = URLDecoder.decode(paymentDetails, "UTF-8");
            Document docxml = builder.parse(new ByteArrayInputStream(s1.getBytes()));
            docxml.getDocumentElement().normalize();

            String status = docxml.getElementsByTagName("Status").item(0).getTextContent();
            if (status.equals("1")) {
                Parsemessage = "Success";
//                appDatabase.paymentDetailsDAO().deleteAllPayment();
            }
            NodeList nodeList = docxml.getElementsByTagName("PaymentDetails");
            for (int k = 0; k < nodeList.getLength(); k++) {
                Node node = nodeList.item(k);
                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node landDetailsNode = node.getChildNodes().item(j);
                    String beneficiaryId = null;
                    String name = null;
                    String department = null;
                    String scheme = null;
                    String financialYear = null;
                    String K2UTRNo = null;
                    String paymentDate = null;
                    String paymentMode = null;
                    String sanctionedAmount = null;

                    for (int i = 0; i < landDetailsNode.getChildNodes().getLength(); i++) {
                        Node childWeather = landDetailsNode.getChildNodes().item(i);
                        if (childWeather.getNodeName().equalsIgnoreCase("beneficiaryid")) {
                            beneficiaryId = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("name")) {
                            name = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("deptname")) {
                            department = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("schemename")) {
                            scheme = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("finyear")) {
                            financialYear = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("k2utrno")) {
                            K2UTRNo = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("paymentdate")) {
                            paymentDate = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("paymentmode")) {
                            paymentMode = childWeather.getTextContent();
                        }

                        if (childWeather.getNodeName().equalsIgnoreCase("sanctionamount")) {
                            sanctionedAmount = childWeather.getTextContent();
                        }
                    }

                    final ModelPaymentDetails farmerPaymentDetails = new ModelPaymentDetails(farmerID, beneficiaryId, name, department, scheme, financialYear, K2UTRNo, paymentDate, paymentMode, sanctionedAmount);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (farmerPaymentDetails != null) {
                                appDatabase.paymentDetailsDAO().insertPayDetails(farmerPaymentDetails);
                            }
//                            finish();
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
              /*  appDatabase.weatherDetailsDAO().deleteAllWeather();
                appDatabase.forecastDetailsDAO().deleteAllForeCast();*/
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

                        if (childWeather.getNodeName().equalsIgnoreCase("VillageName")) {
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
//                            finish();
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
//                            finish();
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

    public String parseAndInsertOwnerDetails(String ownerDetails) {
        String Parsemessage = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            String s1 = URLDecoder.decode(ownerDetails, "UTF-8");
            Document docxml = builder.parse(new ByteArrayInputStream(s1.getBytes()));
            docxml.getDocumentElement().normalize();


            String status = docxml.getElementsByTagName("Status").item(0).getTextContent();
            if (status.equals("1")) {
                Parsemessage = "Success";
                appDatabase.ownerDetailsDAO().deleteAllOwner();
            }
            NodeList nodeList = docxml.getElementsByTagName("LandDetails");
            for (int k = 0; k < nodeList.getLength(); k++) {
                Node node = nodeList.item(k);
                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node ownerDetailsNode = node.getChildNodes().item(j);

                    String farmerid = null;
                    String ownername = null;
                    String ownernumber = null;
                    String ownermainnumber = null;
                    String surveynumber = null;
                    String relativename = null;
                    String area = null;
                    for (int i = 0; i < ownerDetailsNode.getChildNodes().getLength(); i++) {

                        Node childdata = ownerDetailsNode.getChildNodes().item(i);
                        if (childdata.getNodeName().equalsIgnoreCase("FarmerRegNo")) {
                            farmerid = childdata.getTextContent();
                        }
                        if (childdata.getNodeName().equalsIgnoreCase("OwnerName")) {
                            ownername = childdata.getTextContent();
                        }
                        if (childdata.getNodeName().equalsIgnoreCase("OwnerNo")) {
                            ownernumber = childdata.getTextContent();
                        }

                        if (childdata.getNodeName().equalsIgnoreCase("MainOwnerNo")) {
                            ownermainnumber = childdata.getTextContent();
                        }

                        if (childdata.getNodeName().equalsIgnoreCase("SurveyNumber")) {
                            surveynumber = childdata.getTextContent();
                        }

                        if (childdata.getNodeName().equalsIgnoreCase("Relativename")) {
                            relativename = childdata.getTextContent();
                        }
                        if (childdata.getNodeName().equalsIgnoreCase("Area")) {
                            area = childdata.getTextContent();
                        }


                    }

                    final ModelOwnerDetails modelownerdetails = new ModelOwnerDetails(farmerid, ownername, ownernumber, ownermainnumber, relativename,surveynumber,"","",area);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (modelownerdetails != null) {
                                appDatabase.ownerDetailsDAO().insertOwnerDetails(modelownerdetails);
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

    public String parsecropmaster(){
        String cropdata= new String();
        NodeList nodeList;
        String cropcode = "";
        String cropname_eng = "";
        String cropname_kn = "";
        String croptype = "";
        String intercroptype = "";
        String cropcategory = "";
        String cropgroup = "";
        String crop_link_code = "";

        try {
            InputStream is1 = getAssets().open("cropmaster.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(is1);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName("Sheet1");
            for (int k = 0; k < nodeList.getLength(); k++) {
                Node node = nodeList.item(k);
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    Node temp = node.getChildNodes().item(i);
                    if (temp.getNodeName().equalsIgnoreCase("cropcode")) {
                        cropcode = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("cropname_eng")) {
                        cropname_eng = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("cropname_kn")) {
                        cropname_kn = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("croptype")) {
                        croptype = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("intercroptype")) {
                        intercroptype = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("cropcategory")) {
                        cropcategory = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("cropgroup")) {
                        cropgroup = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("crop_link_code")) {
                        crop_link_code = temp.getTextContent();
                    }
                    modelCropMaster = new ModelCropMaster(cropcode, cropname_eng, cropname_kn, croptype, intercroptype, cropcategory, cropgroup, crop_link_code);
                }
                // appDatabase.cropMasterDao().deletecropmaster();
                if (modelCropMaster != null) {
                    appDatabase.cropMasterDao().insertcrop(modelCropMaster);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);

            }
            cropdata = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            cropdata = "Failure";
        }
        return cropdata;

    }

    private String parsecroptype() {
        String croptype = new String();
        NodeList nodeList;
        String croptypeid = "";
        String croptypeenname = "";
        String croptypeknname = "";

        try {
            InputStream is1 = getAssets().open("CropType.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(is1);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName("Sheet1");
            for (int k = 0; k < nodeList.getLength(); k++) {
                Node node = nodeList.item(k);
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    Node temp = node.getChildNodes().item(i);
                    if (temp.getNodeName().equalsIgnoreCase("croptypeid")) {
                        croptypeid = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("croptypename_eng")) {
                        croptypeenname = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("croptypename_kn")) {
                        croptypeknname = temp.getTextContent();
                    }

                    modelCropMasterType = new ModelCropMasterType(croptypeid, croptypeenname, croptypeknname);
                }
                // appDatabase.cropMasterDao().deletecropmaster();
                if (modelCropMasterType != null) {
                    appDatabase.cropMasterTypeDao().insertcrop(modelCropMasterType);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);

            }
            croptype = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            croptype = "Failure";
        }
        return croptype;
    }

    private String parseirrigationtype() {

        String irrigationtype = new String();
        NodeList nodeList;
        String irrigationtypeid = "";
        String irrigationtypeenname = "";
        String irrigationtypeknname = "";

        try {
            InputStream is1 = getAssets().open("IrrigationType.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(is1);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName("Sheet1");
            for (int k = 0; k < nodeList.getLength(); k++) {
                Node node = nodeList.item(k);
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    Node temp = node.getChildNodes().item(i);
                    if (temp.getNodeName().equalsIgnoreCase("irrigationtypeid")) {
                        irrigationtypeid = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("irrigationtypename_eng")) {
                        irrigationtypeenname = temp.getTextContent();
                    }
                    if (temp.getNodeName().equalsIgnoreCase("irrigationtypename_kn")) {
                        irrigationtypeknname = temp.getTextContent();
                    }

                    modelIrrigationType = new ModelIrrigationType(irrigationtypeid, irrigationtypeenname, irrigationtypeknname);
                }

                if (modelIrrigationType != null) {
                    appDatabase.irrigationTypeDao().insertirrigationtype(modelIrrigationType);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);

            }
            irrigationtype = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            irrigationtype = "Failure";
        }
        return irrigationtype;

    }
 /*   private class SaveCropData extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(ValidateOTPActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait..Collecting Farmer Details. It will take some time.");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String cropdata = new String();
            try {
              //  cropdata = parsecropmaster();
            } catch (Exception e) {
                e.printStackTrace();
                cropdata = "Unable to get crop data";
            }
            return cropdata;
        }

        @Override
        protected void onPostExecute(String weatherDataString) {

            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (weatherDataString.equals("Success")) {
                Toast.makeText(getApplicationContext(), "Saving crop details", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Problem saving crop details", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

   /* public String parseInsertCropVarietyData(String cropVarietyData) {
        String Parsemessage = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            String s1 = URLDecoder.decode(cropVarietyData, "UTF-8");
            Document docxml = builder.parse(new ByteArrayInputStream(s1.getBytes()));
            docxml.getDocumentElement().normalize();

            String status = docxml.getElementsByTagName("Status").item(0).getTextContent();
            if (status.equals("1")) {
                Parsemessage = "Success";
            }

            NodeList nodeList = docxml.getElementsByTagName("cropVarietyData");
            for (int k = 0; k < nodeList.getLength(); k++) {
                Node node = nodeList.item(k);
                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node landDetailsNode = node.getChildNodes().item(j);
                     int cropcode = 0;
                     String cropvariety = null;
                     String cropsowingduration = null;
                     String periodicitypicking = null;
                     int totalpicks =0;
                     int numberofdayfirstpicking =0;
                     int expyieldinfirstpick =0;

                    for (int i = 0; i < landDetailsNode.getChildNodes().getLength(); i++) {

                        Node childCropVariety = landDetailsNode.getChildNodes().item(i);

                        if (childCropVariety.getNodeName().equalsIgnoreCase("districtname")) {
                            cropcode = Integer.parseInt(childCropVariety.getTextContent());
                        }

                        if (childCropVariety.getNodeName().equalsIgnoreCase("talukname")) {
                            cropvariety = childCropVariety.getTextContent();
                        }

                        if (childCropVariety.getNodeName().equalsIgnoreCase("VillageName")) {
                            cropsowingduration = childCropVariety.getTextContent();
                        }

                        if (childCropVariety.getNodeName().equalsIgnoreCase("rainfall")) {
                            periodicitypicking = childCropVariety.getTextContent();
                        }

                        if (childCropVariety.getNodeName().equalsIgnoreCase("avgtemp")) {
                            totalpicks = Integer.parseInt(childCropVariety.getTextContent());
                        }

                        if (childCropVariety.getNodeName().equalsIgnoreCase("avgrh")) {
                            numberofdayfirstpicking = Integer.parseInt(childCropVariety.getTextContent());
                        }

                        if (childCropVariety.getNodeName().equalsIgnoreCase("avgwind")) {
                            expyieldinfirstpick = Integer.parseInt(childCropVariety.getTextContent());
                        }

                    }

                    final ModelCropVarietyMaster cropVarietyMaster = new ModelCropVarietyMaster(cropcode, cropvariety, cropsowingduration, periodicitypicking, totalpicks, numberofdayfirstpicking, expyieldinfirstpick);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (cropVarietyMaster != null) {
                                appDatabase.cropVarietyMasterDao().insertCropVarietyMaster(cropVarietyMaster);
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
    }*/
}
