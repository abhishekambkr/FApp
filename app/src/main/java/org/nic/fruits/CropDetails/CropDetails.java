package org.nic.fruits.CropDetails;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nic.fruits.R;

import org.nic.fruits.Utils;
import org.nic.fruits.adapter.CropDetailsHomeAdapter;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;
import org.nic.fruits.pojo.ModelIconWIthDescriptiveName;

import java.util.ArrayList;
import java.util.List;

public class CropDetails extends AppCompatActivity {

    private Context mContext;
    private String farmerID;
    private String keyValue;
    private List<ModelIconWIthDescriptiveName> mainOptionItemList = new ArrayList<>();
    private AppDatabase appDatabase;
    CardView cv_crop_register,cv_weekly_data,cv_cropdetailsupload;
    private CropDetailsHomeAdapter adapter;
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    private LinearLayout linearLayoutBottom;
    private TextView textViewDesignDevelopment;
    private DrawerLayout cropdetailsDrawerLayout;
    private ActionBarDrawerToggle hasiruDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_details);

        getSupportActionBar().setTitle(getResources().getString(R.string.cropdetails));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        cropdetailsDrawerLayout = (DrawerLayout) findViewById(R.id.cropdetails_drawer);

        SharedPreferences prefs = getSharedPreferences("com.example.fruites", MODE_PRIVATE);
        farmerID = prefs.getString("FarmerID", "default_value_here_if_string_is_missing");
        keyValue = prefs.getString("KeyValue", "default_value_here_if_string_is_missing");
        mContext = this;
        appDatabase = AppDatabase.getInstance(getApplicationContext());

//     9663869607

        recyclerView = findViewById(R.id.crop_details_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager = new GridLayoutManager(this, 3);
        linearLayoutBottom =  findViewById(R.id.cd_bottomBox);
        textViewDesignDevelopment =  findViewById(R.id.design_development);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 3 : 1;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
////                    Toast.makeText(FruitsHomeActivity.this, "Last", Toast.LENGTH_LONG).show();
                    linearLayoutBottom.setVisibility(View.GONE);
                }else {
                    if(linearLayoutBottom.getVisibility() == View.GONE){
                        linearLayoutBottom.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });

        adapter();
        clearData();
        addItemList();
    }

    private void addItemList() {
        ModelIconWIthDescriptiveName itemAdapter = new ModelIconWIthDescriptiveName();
        itemAdapter.setImage(R.drawable.crp_register);
        itemAdapter.setText(getResources().getString(R.string.crop_register));
        mainOptionItemList.add(itemAdapter);

        itemAdapter = new ModelIconWIthDescriptiveName();
        itemAdapter.setImage(R.drawable.week);
        itemAdapter.setText(getResources().getString(R.string.crop_weekly_data));
        mainOptionItemList.add(itemAdapter);

        itemAdapter = new ModelIconWIthDescriptiveName();
        itemAdapter.setImage(R.drawable.upload);
        itemAdapter.setText(getResources().getString(R.string.crop_details_upload));
        mainOptionItemList.add(itemAdapter);

        textViewDesignDevelopment.setText(getResources().getString(R.string.design_devlopment));
    }

    private void adapter() {
        adapter = new CropDetailsHomeAdapter(mContext, mainOptionItemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void clearData() {
        mainOptionItemList.clear();
        adapter.notifyDataSetChanged();
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
                            .setMessage("Please connect to internet")
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


    /*private class GetSyncCropVarietyMaster extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait..Updated Land Details are collecting..");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }
        @Override
        protected String doInBackground(String... arg0) {
            String cropVarietyData = new String();
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
            }

            return cropVarietyData;
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

    public String parseInsertCropVarietyData(String cropVarietyData) {
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

    /*public String parseInsertCropVarietyData(String cropVarietyData) {
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
                    int cropcode = 1;
                    String cropvariety = null;
                    String cropsowingduration = null;
                    String periodicitypicking = null;
                    int totalpicks =0;
                    int numberofdayfirstpicking =0;
                    int expyieldinfirstpick =0;


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}