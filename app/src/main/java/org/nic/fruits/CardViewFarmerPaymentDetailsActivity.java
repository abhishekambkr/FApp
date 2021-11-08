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

import org.nic.fruits.adapter.PaymentDetailsCardAdapter;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;
import org.nic.fruits.pojo.ModelPaymentDetails;
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
public class CardViewFarmerPaymentDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PaymentDetailsCardAdapter adapter;
    private Context mContext;
    private String farmerID;
    private String keyValue;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view_farmer_payment_details);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.payment_data));
        SharedPreferences prefs = getSharedPreferences("com.example.fruites", MODE_PRIVATE);
        farmerID = prefs.getString("FarmerID", "default_value_here_if_string_is_missing");
        keyValue = prefs.getString("KeyValue", "default_value_here_if_string_is_missing");
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        initView();
        setupViewModel();
    }

    private void initView() {
        recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getgetPaymentDetailsBasedOnFid(farmerID).observe(this, new Observer<List<ModelPaymentDetails>>() {
            @Override
            public void onChanged(@Nullable List<ModelPaymentDetails> taskEntries) {

                if (taskEntries != null && !taskEntries.isEmpty() && !taskEntries.equals("null") && !taskEntries.equals("")){
                    adapter = new PaymentDetailsCardAdapter(CardViewFarmerPaymentDetailsActivity.this,taskEntries );
                    recyclerView.setAdapter(adapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(CardViewFarmerPaymentDetailsActivity.this, LinearLayoutManager.VERTICAL));
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
        // Inflate the menu; this adds items to the action bar if it is present.
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
                    new GetSyncPaymentDetails().execute();
                }else {
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

    private class GetSyncPaymentDetails extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait..Latest Payment Details are collecting");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String paymentDetailsString = new String();
            try {
                SoapProxy proxy2 = new SoapProxy(mContext);
                paymentDetailsString = proxy2.getSynchronizePaymentDetails(keyValue, farmerID);
                paymentDetailsString = paymentDetailsString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                paymentDetailsString = paymentDetailsString.replaceAll("[^\\x20-\\x7e]", "");
                if (paymentDetailsString.equalsIgnoreCase("NoUpdates")) {

                } else if (paymentDetailsString.equalsIgnoreCase("Failure")) {
                    paymentDetailsString = "InternetconectionProblem";
                } else if (paymentDetailsString.equalsIgnoreCase("Failure1")) {
                    paymentDetailsString = "InternetconectionProblem";
                } else if (paymentDetailsString.equalsIgnoreCase("anyType{}")) {
                    paymentDetailsString = "anyType{}";
                } else {
                    paymentDetailsString = parsePaymentDetails(paymentDetailsString);
                }

            } catch (Exception e) {
                e.printStackTrace();
                paymentDetailsString = "InternetconectionProblem";
            }
            return paymentDetailsString;
        }

        @Override
        protected void onPostExecute(String paymentDetailsString) {
            this.dialog.dismiss();
            if (paymentDetailsString.equals("Error")) {
                Toast.makeText(mContext, "Internet Connection unavailable", Toast.LENGTH_LONG).show();
            } else if (paymentDetailsString.equals("InternetconectionProblem")) {
                Toast.makeText(mContext, "Internet Disconnected", Toast.LENGTH_LONG).show();
            } else if (paymentDetailsString.equals("Success")) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Alert")
                        .setMessage("Farmer Details Downloaded Successfully")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                dialog.cancel();
                            }
                        })
                        .show();
            } else if (paymentDetailsString.equals("Failureparsing")) {
                Toast.makeText(mContext, "Application already Present in Device", Toast.LENGTH_LONG).show();
            }  else if(paymentDetailsString.equals("No Record Found")){
                Toast.makeText(mContext, "No updated Payment found", Toast.LENGTH_LONG).show();
            }
        }
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
            }else if(status.equals("No Record Found")){
                Parsemessage = "No Record Found";
            }
            NodeList nodeList = docxml.getElementsByTagName("PaymentDetails");
            for (int k = 0; k < nodeList.getLength(); k++){
                Node node = nodeList.item(k);
                for(int j = 0;j<node.getChildNodes().getLength();j++){
                    Node landDetailsNode = node.getChildNodes().item(j);
                    String beneficiaryId = null;
                    String name = null;
                    String department = null;
                    String scheme = null;
                    String financialYear = null;
                    String K2UTRNo= null;
                    String paymentDate = null;
                    String paymentMode = null;
                    String sanctionedAmount = null;

                    for(int i = 0;i <landDetailsNode.getChildNodes().getLength();i++){
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

                    final ModelPaymentDetails farmerPaymentDetails = new ModelPaymentDetails(farmerID,beneficiaryId,name,department,scheme,financialYear,K2UTRNo,paymentDate,paymentMode,sanctionedAmount);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (farmerPaymentDetails != null) {
                                appDatabase.paymentDetailsDAO().insertPayDetails(farmerPaymentDetails);
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
