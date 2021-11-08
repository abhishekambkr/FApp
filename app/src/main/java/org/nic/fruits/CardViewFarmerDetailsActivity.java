
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
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.nic.fruits.R;

import org.nic.fruits.adapter.FarmerDetailsCardAdapter;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;
import org.nic.fruits.pojo.ModelFarmerDetails;
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
public class CardViewFarmerDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FarmerDetailsCardAdapter adapter;
    private Context mContext;
    private String farmerID;
    private AppDatabase appDatabase;
    private String keyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view_farmer_details);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.personal_data));
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
        viewModel.getFarmerDetailsBasedOnFid(farmerID).observe(this, new Observer<List<ModelFarmerDetails>>() {
            @Override
            public void onChanged(@Nullable List<ModelFarmerDetails> taskEntries) {
                adapter = new FarmerDetailsCardAdapter(CardViewFarmerDetailsActivity.this, taskEntries);
                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(CardViewFarmerDetailsActivity.this, LinearLayoutManager.VERTICAL));
                adapter.notifyDataSetChanged();
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
            return true;
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
                if(Utils.isNetworkConnected(mContext)){
                    new GetSyncFarmerDeatails().execute();
                }else {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Alert")
                            .setMessage("Please Connect To Internet")

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

    private class GetSyncFarmerDeatails extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait,, Framer Data is fetching...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String stringFarmerDetails = new String();
            try {
                SoapProxy proxy2 = new SoapProxy(mContext);
                stringFarmerDetails = proxy2.getSyncFarmerData(keyValue, farmerID);
                stringFarmerDetails = stringFarmerDetails.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                stringFarmerDetails = stringFarmerDetails.replaceAll("[^\\x20-\\x7e]", "");
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

            return stringFarmerDetails;
        }

        @Override
        protected void onPostExecute(String stringFarmerDetails) {
            this.dialog.dismiss();
            if (stringFarmerDetails.equals("Error")) {
                Toast.makeText(mContext, "Internet Connection unavailable", Toast.LENGTH_LONG).show();
            } else if (stringFarmerDetails.equals("InternetconectionProblem")) {
                Toast.makeText(mContext, "Internet Disconnected", Toast.LENGTH_LONG).show();
            } else if (stringFarmerDetails.equals("Success")) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Alert")
                        .setMessage("Farmer Details Downloaded Successfully")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            } else if (stringFarmerDetails.equals("Failureparsing")) {
                Toast.makeText(mContext, "Application already Present in Device", Toast.LENGTH_LONG).show();
            }else if(stringFarmerDetails.equals("No Record Found")){
                Toast.makeText(mContext, "No updated New Farmer Data found", Toast.LENGTH_LONG).show();
            }
        }
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
            }else if(status.equals("No Record Found")){
                Parsemessage = "No Record Found";
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
}


