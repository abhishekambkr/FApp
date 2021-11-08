package org.nic.fruits.CropDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import org.nic.fruits.CropDetails.UImodel.UiModel;
import org.nic.fruits.CropDetails.adapter.UIadapter;
import org.nic.fruits.FruitsHomeActivity;
import org.nic.fruits.R;
import org.nic.fruits.SaveSharedPreference;
import org.nic.fruits.SoapProxy;
import org.nic.fruits.ValidateOTPActivity;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;
import org.nic.fruits.database.dao.CropMasterDao;
import org.nic.fruits.pojo.ModelCropMaster;
import org.nic.fruits.pojo.ModelCropSurveyDetails;
import org.nic.fruits.pojo.ModelFarmerLandDeatails;
import org.nic.fruits.pojo.ModelOwnerDetails;
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
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class UploadCropDetails extends AppCompatActivity {

    private AppDatabase appDatabase;
    private Context mContext;
/*    ViewPager viewPager;
    UIadapter adapter;
    List<UiModel> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_crop_details);
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        mContext = this;

      /*  models = new ArrayList<>();
        models.add(new UiModel(R.drawable.crp_register,"Crop Registration","To get crop details, farmer has to register crop"));
        models.add(new UiModel(R.drawable.week,"Crop Weekly Data","Farmer has to enter the weekly crop data after crop has been registered"));
        models.add(new UiModel(R.drawable.upload,"Upload Crop Details","Upload the obtained crop details"));
        adapter = new UIadapter(models, this);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130,0,130,0);

        Integer[] colors_temp = {getResources().getColor(R.color.main_screen_bg),
                getResources().getColor(R.color.beige_blue),getResources().getColor(R.color.light_purple)};

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position < (adapter.getCount() - 1) && position < (colors.length - 1)){
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset,colors[position],colors[position + 1]));

                }else{
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/




      new Upload().execute();

    }

    private class Upload extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(UploadCropDetails.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait.. uploading crop details");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String savedata;
            savedata = uploadcropdata();
            return savedata;
        }

        private String uploadcropdata() {

            //xml for upload

            return "failure";
        }

        @Override
        protected void onPostExecute(String savedata) {

            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (savedata.equals("success")) {
                Toast.makeText(mContext, "Crop details uploaded", Toast.LENGTH_SHORT).show();

            } else if (savedata.equals("failure")){
                Toast.makeText(mContext, "No webservice found", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(mContext)
                        .setTitle(getResources().getString(R.string.alert))
                        .setMessage("Unable to upload crop details")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Intent cropupload = new Intent(mContext,CropDetails.class);
                                cropupload.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(cropupload);
                                finish();
                            }
                        })
                        .show();
            }
            else {
                Toast.makeText(mContext, "Problem uploading crop details", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /*private class SaveCropData extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(UploadCropDetails.this);

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

                cropdata = parsecropmaster(cropdata);
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
                SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                Intent mainActivityIntent = new Intent(UploadCropDetails.this, CropDetails.class);
                startActivity(mainActivityIntent);
            } else {
                Toast.makeText(getApplicationContext(), "Problem saving crop details", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String parsecropmaster(String cropdata){
        cropdata= null;
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
                appDatabase.cropMasterDao().insertcrop(modelCropMaster);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);
                cropdata = "Success";
            }

        } catch (Exception e) {
            e.printStackTrace();
            cropdata = "Failure";
        }
        return cropdata;


    }*/

}