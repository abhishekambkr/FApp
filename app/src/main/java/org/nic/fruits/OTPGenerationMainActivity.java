package org.nic.fruits;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.nic.fruits.R;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



//Programming by Harsha  for version 1.0 release
public class OTPGenerationMainActivity extends AppCompatActivity {

    private EditText editTextMobileNumber;
    private EditText editTextAadhaaar;
    private Button btnGetOtp;
    private Context mContext;
    private String otp;
    private String farmerID;
    private String aadhaarNumber;
    private String mobileNumber;
    private String keyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_generation_main);
        mContext = this;
        editTextMobileNumber = findViewById(R.id.edit_text_mobile_number);
        editTextAadhaaar = findViewById(R.id.edit_text_aadhaar_number);
        btnGetOtp = findViewById(R.id.btn_get_otp);
        keyValue = "4513D4EDA84E7EE00D5EF91AA634C0";

        if (SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), FruitsHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // test

              /*  Intent validateOtpIntent = new Intent(mContext, ValidateOTPActivity.class);
               *//* validateOtpIntent.putExtra("OTP", otp);
                validateOtpIntent.putExtra("FarmerID", farmerID);*//*
                validateOtpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(validateOtpIntent);
*/
                boolean isEmptyEditTextAadhar = Utils.isEmpty(editTextAadhaaar);
                boolean isEmptyEditTextMobileNumber = Utils.isEmpty(editTextMobileNumber);
                if (Utils.isNetworkConnected(mContext)) {
                    if (!isEmptyEditTextAadhar && !isEmptyEditTextMobileNumber) {
                        aadhaarNumber = editTextAadhaaar.getText().toString();
                        mobileNumber = editTextMobileNumber.getText().toString();

                        Intent validateOtpIntent = new Intent(mContext, ValidateOTPActivity.class);
                        validateOtpIntent.putExtra("OTP", "1234");
                        validateOtpIntent.putExtra("FarmerID", "FID2303000005885");
                        validateOtpIntent.putExtra("MobileNumber", mobileNumber);
                        validateOtpIntent.putExtra("AadhaarNumber", aadhaarNumber);
                        validateOtpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(validateOtpIntent);
                        finish();
                     //   new GetOTP().execute();
                    } else {
                        Toast.makeText(mContext, "Please Enter Both Aadhaar Number And Mobile Number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Internet is Not Avilable", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
//            farmerDataString = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><node><ResponseStatus><Status>1</Status><FarmerID>FID0101000000028</FarmerID><OTP>23273</OTP></ResponseStatus></node>";
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
                validateOtpIntent.putExtra("MobileNumber", mobileNumber);
                validateOtpIntent.putExtra("AadhaarNumber", aadhaarNumber);
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
}