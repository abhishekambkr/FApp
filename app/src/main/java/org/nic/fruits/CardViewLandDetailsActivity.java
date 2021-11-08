package org.nic.fruits;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.nic.fruits.R;

import org.nic.fruits.adapter.LandDetailsCardAdapter;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;
import org.nic.fruits.pojo.ModelFarmerLandDeatails;
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
public class CardViewLandDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LandDetailsCardAdapter adapter;
    private Context mContext;
    private String farmerID;
    private String keyValue;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_card_view_land_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.land_data));
        SharedPreferences prefs = getSharedPreferences("com.example.fruites", MODE_PRIVATE);
        farmerID = prefs.getString("FarmerID", "default_value_here_if_string_is_missing");
        keyValue = prefs.getString("KeyValue", "default_value_here_if_string_is_missing");
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        initView();
        setupViewModel();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getLandBasedOnFid(farmerID).observe(this, new Observer<List<ModelFarmerLandDeatails>>() {
            @Override
            public void onChanged(@Nullable List<ModelFarmerLandDeatails> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    adapter = new LandDetailsCardAdapter(CardViewLandDetailsActivity.this,taskEntries );
                    recyclerView.setAdapter(adapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(CardViewLandDetailsActivity.this, LinearLayoutManager.VERTICAL));
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                    new GetSyncFarmerlandDetails().execute();
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

    private class GetSyncFarmerlandDetails extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait..Updated Land Details are collecting..");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }
        @Override
        protected String doInBackground(String... arg0) {
            String syncFarmerLandDataString = new String();
            try {
                SoapProxy proxy2 = new SoapProxy(mContext);
                syncFarmerLandDataString = proxy2.getSynchronizeFarmerLandData(keyValue, farmerID);
                syncFarmerLandDataString = syncFarmerLandDataString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                syncFarmerLandDataString = syncFarmerLandDataString.replaceAll("[^\\x20-\\x7e]", "");
                if (syncFarmerLandDataString.equalsIgnoreCase("No Record Found")) {
                    syncFarmerLandDataString = "No  Updated Record found";
                } else if (syncFarmerLandDataString.equalsIgnoreCase("Failure")) {
                    syncFarmerLandDataString = "InternetconectionProblem";
                } else if (syncFarmerLandDataString.equalsIgnoreCase("Failure1")) {
                    syncFarmerLandDataString = "InternetconectionProblem";
                } else if (syncFarmerLandDataString.equalsIgnoreCase("anyType{}")) {
                    syncFarmerLandDataString = "anyType{}";
                } else {
                     syncFarmerLandDataString = parseAndInsertLandDetails(syncFarmerLandDataString);
                    System.out.println("calling aa post parser  " + syncFarmerLandDataString);
                }

            } catch (Exception e) {
                e.printStackTrace();
                syncFarmerLandDataString = "InternetconectionProblem";
            }

            return syncFarmerLandDataString;
        }

        @Override
        protected void onPostExecute(String syncFarmerLandDataString) {
            this.dialog.dismiss();
            if (syncFarmerLandDataString.equals("Error")) {
                Toast.makeText(mContext, "Internet Connection unavailable", Toast.LENGTH_LONG).show();
            } else if (syncFarmerLandDataString.equals("InternetconectionProblem")) {
                Toast.makeText(mContext, "Internet Disconnected", Toast.LENGTH_LONG).show();
            } else if (syncFarmerLandDataString.equals("Success")) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Alert")
                        .setMessage("Farmer land Details Downloaded Successfully")

                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                dialog.cancel();
                            }
                        })
                        .show();
            } else if (syncFarmerLandDataString.equals("Failureparsing")) {
                Toast.makeText(mContext, "Application already Present in Device", Toast.LENGTH_LONG).show();
            } else if(syncFarmerLandDataString.equals("No Record Found")){
                Toast.makeText(mContext, "No updated Land Records found", Toast.LENGTH_LONG).show();
            }
        }
    }

    public String parseAndInsertLandDetails(String landDataString) {
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
            }else if(status.equals("No Record Found")){
                Parsemessage = "No Record Found";
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
                            finish();
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
