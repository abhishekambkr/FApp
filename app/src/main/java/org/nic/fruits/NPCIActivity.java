package org.nic.fruits;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import org.nic.fruits.R;

//Programming by Harsha  for version 1.0 release
public class NPCIActivity extends AppCompatActivity {

    private TextView npcResult,lastUpdate,bank,requestNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npci);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        npcResult = findViewById(R.id.txtNPCResult);
        lastUpdate = findViewById(R.id.txtLastUpdate);
        bank = findViewById(R.id.txtBank);
        requestNumber = findViewById(R.id.txtRequestNumber);

        Intent i = getIntent();
        String npciResult = i.getStringExtra("NPCI");
        String lastUpdates = i.getStringExtra("Last_updation");
        String banks = i.getStringExtra("By_Bank");
        String requestNumbers = i.getStringExtra("Request_Number");

        npcResult.setText(getResources().getString(R.string.npci_result) +" : "+npciResult);
        lastUpdate.setText(getResources().getString(R.string.last_update) +" : "+lastUpdates);
        bank.setText(getResources().getString(R.string.bank) +" : "+banks);
        requestNumber.setText(getResources().getString(R.string.request_number) +" : "+requestNumbers);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
//            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.sync).setVisible(false);
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

}
