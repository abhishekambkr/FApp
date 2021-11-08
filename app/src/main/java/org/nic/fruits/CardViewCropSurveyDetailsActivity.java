package org.nic.fruits;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.nic.fruits.R;

import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;
import org.nic.fruits.pojo.ModelCropSurveyDetails;
import org.nic.fruits.viewmodel.MainViewModel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//Programming by Harsha  for version 1.0 release
public class CardViewCropSurveyDetailsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CropSurveyCardAdapter adapter;
    private String farmerID;
    private String keyValue;
    private Context mContext;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view_crop_survey_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.cropsurvey_data));
        mContext = this;
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        SharedPreferences prefs = getSharedPreferences("com.example.fruites", MODE_PRIVATE);
        farmerID = prefs.getString("FarmerID", "default_value_here_if_string_is_missing");
        keyValue = prefs.getString("KeyValue", "default_value_here_if_string_is_missing");
        initView();
        setupViewModel();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getCropSurveBasedOnFid(farmerID).observe(this, new Observer<List<ModelCropSurveyDetails>>() {
            @Override
            public void onChanged(@Nullable List<ModelCropSurveyDetails> taskEntries) {

                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    adapter = new CropSurveyCardAdapter(CardViewCropSurveyDetailsActivity.this, taskEntries);
                    recyclerView.setAdapter(adapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(CardViewCropSurveyDetailsActivity.this, LinearLayoutManager.VERTICAL));
                    adapter.notifyDataSetChanged();
                }else{
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
                    new GetCropSurveyDetails().execute();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class GetCropSurveyDetails extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(mContext);
        @Override
        protected String doInBackground(String... arg0) {
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
            return cropSurveyDetailsString;
        }

        @Override
        protected void onPostExecute(String cropSurveyDetailsString) {
            if (cropSurveyDetailsString.equals("Success")) {

            } else {
                Toast.makeText(mContext, "Problem While Downloading Details", Toast.LENGTH_SHORT).show();
            }
        }
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
                appDatabase.cropDetailsDAO().deleteAllCropSurvey();
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
}
