 package org.nic.fruits;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.nic.fruits.R;

import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


//Programming by Harsha  for version 1.0 release
public class FeedBackDetailsActivity extends AppCompatActivity {

    private ImageButton play, stop, record;
    private Button btnUploadText, btnUploadAudio;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private EditText editTextFeedBack;
    private Context mContext;
    private File audioFile;
    byte[] bytes;
    String base64String;
    private String farmerID;
    private String keyValue;
    private String farmerName;
    private AppDatabase appDatabase;
    private TextView textRecord;
    private TextView textStop;
    private TextView textPlay;
    private TextView fileName;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private LinearLayout linearAudioImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_details);

        getSupportActionBar().setTitle(getResources().getString(R.string.feedback));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mContext = this;
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        record = findViewById(R.id.record);
        btnUploadText = findViewById(R.id.send);
        btnUploadAudio = findViewById(R.id.btn_audio_send);
        editTextFeedBack = findViewById(R.id.edittext_textfeedback);
        fileName = findViewById(R.id.file_name);
        linearAudioImage  = findViewById(R.id.layout_audio_img);

        textRecord = findViewById(R.id.txt_record);
        textStop = findViewById(R.id.txt_stop);
        textPlay = findViewById(R.id.txt_play);

        final Chronometer myChronometer = findViewById(R.id.chronometer);
        play.setVisibility(View.INVISIBLE);
        textPlay.setVisibility(View.INVISIBLE);

        SharedPreferences prefs = getSharedPreferences("com.example.fruites", MODE_PRIVATE);
        farmerID = prefs.getString("FarmerID", "default_value_here_if_string_is_missing");
        keyValue = prefs.getString("KeyValue", "default_value_here_if_string_is_missing");

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                farmerName = appDatabase.farmerDetailsDAO().getFarmerName(farmerID);
                System.out.println("calling bb farmerName  +farmerName" + farmerName);
            }
        });

        stop.setEnabled(false);
        play.setEnabled(false);

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.ogg";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

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
                myChronometer.start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
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
                myChronometer.stop();
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
                    myChronometer.setVisibility(View.INVISIBLE);
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
        btnUploadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(mContext)) {
                    boolean isEmptyFeedBack = Utils.isEmpty(editTextFeedBack);
                    if (!isEmptyFeedBack) {
                        new UploadFeedBackTextDetails().execute();
                    } else {
                        Toast.makeText(mContext, "FeedBack Should Not be Empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Internet is Not Avilable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUploadAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(mContext)) {
                    if (audioFile != null) {
                        if (audioFile.exists()) {
                            new UploadFeedBackVoiceDetails().execute();
                        } else {
                            Toast.makeText(mContext, "audio Should Not be Empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Please add audio File", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Internet is Not Avilable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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

    private class UploadFeedBackTextDetails extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please Wait Your Feedback is uploading");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String cropSurveyDetailsString = new String();
            try {
                SoapProxy proxy = new SoapProxy(mContext);
                System.out.println("calling bb farmerName  +farmerName" + farmerName + " " + keyValue + " " + farmerID + " " + editTextFeedBack);
                cropSurveyDetailsString = proxy.sendTextFeedBack(keyValue, farmerID, farmerName, editTextFeedBack.getText().toString());
                cropSurveyDetailsString = cropSurveyDetailsString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                cropSurveyDetailsString = cropSurveyDetailsString.replaceAll(">\\s+<", "><").trim();
            } catch (Exception e) {
                e.printStackTrace();
                cropSurveyDetailsString = "InternetconectionProblem";
            }
            return cropSurveyDetailsString;
        }

        @Override
        protected void onPostExecute(String cropSurveyDetailsString) {
            dialog.dismiss();
            if (cropSurveyDetailsString.equals("1")) {
                Toast.makeText(mContext, "Feedback Uploaded Successuly", Toast.LENGTH_SHORT).show();


                Intent mainActivity = new Intent(mContext, FruitsHomeActivity.class);
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainActivity);
                finish();
            } else {
                Toast.makeText(mContext, "Problem While Uploading Feedback Details", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UploadFeedBackVoiceDetails extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please Wait Your Feedback is uploading");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String cropSurveyDetailsString = new String();
            try {
                SoapProxy proxy = new SoapProxy(mContext);

                cropSurveyDetailsString = proxy.sendVoiceFeedBack(keyValue, farmerID, farmerName, bytes);
                cropSurveyDetailsString = cropSurveyDetailsString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                cropSurveyDetailsString = cropSurveyDetailsString.replaceAll(">\\s+<", "><").trim();

            } catch (Exception e) {
                e.printStackTrace();
                cropSurveyDetailsString = "InternetconectionProblem";
            }
            return cropSurveyDetailsString;
        }

        @Override
        protected void onPostExecute(String cropSurveyDetailsString) {
            dialog.dismiss();
            if (cropSurveyDetailsString.equals("1")) {
                Toast.makeText(mContext, "Feedback Uploaded Successuly", Toast.LENGTH_SHORT).show();

                Intent mainActivity = new Intent(mContext, FruitsHomeActivity.class);
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainActivity);
                finish();
            } else {
                Toast.makeText(mContext, "Problem While Uploading Feedback Details", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
