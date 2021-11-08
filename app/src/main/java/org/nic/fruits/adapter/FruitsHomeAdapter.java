package org.nic.fruits.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.nic.fruits.FarmFertilizer;
import org.nic.fruits.FertilizerCalculation;
import org.nic.fruits.R;

import org.nic.fruits.CardViewCropSurveyDetailsActivity;
import org.nic.fruits.CardViewFarmerDetailsActivity;
import org.nic.fruits.CardViewFarmerPaymentDetailsActivity;
import org.nic.fruits.CardViewLandDetailsActivity;
import org.nic.fruits.CropDetails.CropDetails;
import org.nic.fruits.FeedBackDetailsActivity;
import org.nic.fruits.NPCIActivity;
import org.nic.fruits.SoapProxy;
import org.nic.fruits.Utils;
import org.nic.fruits.WeatherForecastActivity;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;
import org.nic.fruits.pojo.ModelCropFertilizerMasternpk;
import org.nic.fruits.pojo.ModelCropMasterType;
import org.nic.fruits.pojo.ModelFertilizerCropMaster;
import org.nic.fruits.pojo.ModelFertilizerNameMaster;
import org.nic.fruits.pojo.ModelForeCastDetails;
import org.nic.fruits.pojo.ModelIconWIthDescriptiveName;
import org.nic.fruits.pojo.ModelIrrigationType;
import org.nic.fruits.pojo.ModelPlantAgeMaster;
import org.nic.fruits.pojo.ModelweatherDetails;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.content.Context.MODE_PRIVATE;


//Programming by Harsha  for version 1.0 release
public class FruitsHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<ModelIconWIthDescriptiveName> viewitemlists;
    private Context context;
    private String NPCI;
    private String LastUpdated;
    private String Bank;
    private String RequestNumber;
    private AppDatabase appDatabase;
    private List<ModelCropFertilizerMasternpk> fertilizerMasternpk = new ArrayList<>();
    private ModelFertilizerCropMaster modelFertilizerCropMaster;
    private ModelPlantAgeMaster modelPlantAgeMaster;
    private ModelFertilizerNameMaster modelFertilizerNameMaster;
    private ModelCropFertilizerMasternpk modelCropFertilizerMasternpk;
    private ModelCropMasterType modelCropMasterType;
    private ModelIrrigationType modelIrrigationType;
    boolean dataAvailable = false;

    Integer[] imageId = {R.drawable.viewpager_one, R.drawable.viewpager_two, R.drawable.viewpager_three, R.drawable.viewpager_four, R.drawable.viewpager_five, R.drawable.viewpager_six, R.drawable.viewpager_seven, R.drawable.viewpager_eight};
    String[] imagesName = {"image1", "image2", "image3", "image4", "image5", "image6", "image7", "image8"};

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;

    private int dotscount;
    private ImageView[] dots;
    private String farmerID;
    private String keyValue;

    public FruitsHomeAdapter(Context context, List<ModelIconWIthDescriptiveName> list) {
        this.context = context;
        this.viewitemlists = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SharedPreferences prefs = context.getSharedPreferences("com.example.fruites", MODE_PRIVATE);
        farmerID = prefs.getString("FarmerID", "default_value_here_if_string_is_missing");
        keyValue = prefs.getString("KeyValue", "default_value_here_if_string_is_missing");
        appDatabase = AppDatabase.getInstance(context);
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listviewitem, parent, false);
            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_headeritem, parent, false);
            return new HeaderViewHolder(itemView);
        } else return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            PagerAdapter adapter = new ViewPagerImageAdapter(imageId, imagesName);
            headerViewHolder.viewPager.setAdapter(adapter);

            dotscount = adapter.getCount();
            dots = new ImageView[dotscount];

            for (int i = 0; i < dotscount; i++) {
                dots[i] = new ImageView(context);
                dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.non_active_dot));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 0, 8, 0);
                headerViewHolder.sliderDotspanel.addView(dots[i], params);
            }
            dots[0].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_dot));
            headerViewHolder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {

                    for (int i = 0; i < dotscount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.non_active_dot));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_dot));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == 8) {
                        currentPage = 0;
                    }
                    headerViewHolder.viewPager.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer();
            timer.schedule(new TimerTask() { // task to be scheduled
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, DELAY_MS, PERIOD_MS);

        } else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final ModelIconWIthDescriptiveName data = viewitemlists.get(position - 1);
            itemViewHolder.title.setText(data.getText());
            itemViewHolder.descriptionImg.setImageResource(data.getImage());

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return viewitemlists.size() + 1;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        ViewPager viewPager;
        LinearLayout sliderDotspanel;

        public HeaderViewHolder(View view) {
            super(view);
            sliderDotspanel = view.findViewById(R.id.SliderDots);
            viewPager = view.findViewById(R.id.viewpager);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView descriptionImg;

        public ItemViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_name);
            descriptionImg = itemView.findViewById(R.id.img_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getLayoutPosition() == 1) {
                        Intent intent = new Intent(context, CardViewFarmerDetailsActivity.class);
                        v.getContext().startActivity(intent);
                    } else if (getLayoutPosition() == 2) {
                        Intent paymentDetailsIntent = new Intent(context, CardViewFarmerPaymentDetailsActivity.class);
                        v.getContext().startActivity(paymentDetailsIntent);
                    } else if (getLayoutPosition() == 3) {
                        Intent intent = new Intent(context, CardViewLandDetailsActivity.class);
                        v.getContext().startActivity(intent);
                    } else if (getLayoutPosition() == 4) {
                        Intent intent = new Intent(context, CardViewCropSurveyDetailsActivity.class);
                        v.getContext().startActivity(intent);
                    } else if (getLayoutPosition() == 5) {
                        if (Utils.isNetworkConnected(context)) {
                            new GetweatherData().execute();
                        } else {
                            Intent intent = new Intent(context, WeatherForecastActivity.class);
                            v.getContext().startActivity(intent);
                        }

                    } else if (getLayoutPosition() == 6) {
                        if (Utils.isNetworkConnected(context)) {
                            new GetNPCDeatails().execute();
                        } else {
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert")
                                    .setMessage("Please Connect To Internet To Check NPCI Status")

                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .show();
                        }

                    } else if (getLayoutPosition() == 7) {
                        Intent intent = new Intent(context, FeedBackDetailsActivity.class);
                        v.getContext().startActivity(intent);
                    }
                    else if (getLayoutPosition() == 8) {
                        Intent intent = new Intent(context, CropDetails.class);
                        v.getContext().startActivity(intent);
                    }
                    else if (getLayoutPosition() == 9) {
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {

                                fertilizerMasternpk = appDatabase.cropFertilizerMasterDAO().getcropforverify(String.valueOf(1));
                                if (fertilizerMasternpk.size() > 0) {
                                    System.out.println("if validateCropMasterModels" + fertilizerMasternpk.size());

                                    Intent intent = new Intent(context, FertilizerCalculation.class);
                                    v.getContext().startActivity(intent);
                                }else{
                                    String saved =  parseFertilizerData();
                                    System.out.println("else validateCropMasterModels");
                                    if(saved.equals("Success")){
                                        Intent intent = new Intent(context, FertilizerCalculation.class);
                                        v.getContext().startActivity(intent);
                                    }
                                }

                            }
                        });

                    }
                    else if (getLayoutPosition() == 10) {
                        Intent intent = new Intent(context, FarmFertilizer.class);
                        v.getContext().startActivity(intent);
                    }
                }
            });

        }
    }

    class ViewPagerImageAdapter extends PagerAdapter {
        private Integer[] imagesArray;
        private String[] namesArray;

        public ViewPagerImageAdapter(Integer[] imagesArray, String[] namesArray) {
            this.imagesArray = imagesArray;
            this.namesArray = namesArray;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View viewItem = LayoutInflater.from(container.getContext()).inflate(R.layout.content_custom, container, false);
            ImageView imageView = viewItem.findViewById(R.id.imageView);
            imageView.setBackgroundResource(imagesArray[position]);
            container.addView(viewItem);
            return viewItem;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imagesArray.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }
    }

    private class GetNPCDeatails extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait...NPCI Details Are Collecting..");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String npcStatusCheck = new String();
            try {
                SoapProxy proxy2 = new SoapProxy(context);
                npcStatusCheck = proxy2.CheckNPCStatus(keyValue, farmerID);
                npcStatusCheck = npcStatusCheck.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                npcStatusCheck = npcStatusCheck.replaceAll("[^\\x20-\\x7e]", "");

                if (npcStatusCheck.equalsIgnoreCase("Failure")) {
                    npcStatusCheck = "InternetconectionProblem";
                } else if (npcStatusCheck.equalsIgnoreCase("Failure1")) {
                    npcStatusCheck = "InternetconectionProblem";
                } else if (npcStatusCheck.equalsIgnoreCase("anyType{}")) {
                    npcStatusCheck = "anyType{}";
                } else {
                    npcStatusCheck = ParseNPCStatusCheck(npcStatusCheck);
                }
            } catch (Exception e) {
                e.printStackTrace();
                npcStatusCheck = "InternetconectionProblem";
            }

            return npcStatusCheck;
        }

        @Override
        protected void onPostExecute(String npcStatusCheck) {
            this.dialog.dismiss();
            if (npcStatusCheck.equals("Success")) {
                Intent i = new Intent(context, NPCIActivity.class);
                i.putExtra("NPCI", NPCI);
                i.putExtra("Last_updation", LastUpdated);
                i.putExtra("By_Bank", Bank);
                i.putExtra("Request_Number", RequestNumber);
                context.startActivity(i);
            }

        }
    }

    public String ParseNPCStatusCheck(String npcStatuesCheck) {
        String Parsemessage = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setCharacterStream(new StringReader(npcStatuesCheck));
            Document docxml = builder.parse(src);
            String status = docxml.getElementsByTagName("Status").item(0).getTextContent();
            if (status.equals("1")) {
                Parsemessage = "Success";
            }
            NPCI = docxml.getElementsByTagName("NPCI").item(0).getTextContent();
            LastUpdated = docxml.getElementsByTagName("LastUpdated").item(0).getTextContent();
            Bank = docxml.getElementsByTagName("Bank").item(0).getTextContent();
            RequestNumber = docxml.getElementsByTagName("RequestNumber").item(0).getTextContent();

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

    private class GetweatherData extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please Wait Weather Details Downlading");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }


        @Override
        protected String doInBackground(String... arg0) {
            String weatherDataString = new String();
            try {
                SoapProxy proxy2 = new SoapProxy(context);
                weatherDataString = proxy2.getWeatherData(keyValue, farmerID);
                weatherDataString = weatherDataString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
                String xmlFormatOfReturnXMLString2 = "<?xml version=" + "\"1.0\"" + "?>" + weatherDataString;
                System.out.println("calling aa  xml string weatherDataString " + xmlFormatOfReturnXMLString2);

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

        //Programming by Harsha  for version 1.0 release
        @Override
        protected void onPostExecute(String weatherDataString) {
            dialog.dismiss();
            if (weatherDataString.equals("Success")) {
                Toast.makeText(context, "Weather Details Updated ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, WeatherForecastActivity.class);
               context.startActivity(intent);
            } else {
                Toast.makeText(context, "Problem while downloading weather details ", Toast.LENGTH_SHORT).show();
            }

        }
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
                /*appDatabase.weatherDetailsDAO().deleteAllWeatherDetailsBasedOnFarmerID(farmerID);
                appDatabase.forecastDetailsDAO().deleteAllForecastDetailsBasedOnFarmerID(farmerID);*/

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        appDatabase.weatherDetailsDAO().deleteAllWeatherDetailsBasedOnFarmerID(farmerID);
                        appDatabase.forecastDetailsDAO().deleteAllForecastDetailsBasedOnFarmerID(farmerID);

                    }
                });
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

                        if (childWeather.getNodeName().equalsIgnoreCase("villagename")) {
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


    private String parseFertilizerData() {

        String fertilizercropmaster = new String();
        try {
            fertilizercropmaster = parsefertilizercropmaster();

        }
        catch (Exception e) {
            e.printStackTrace();
            fertilizercropmaster = "InternetconectionProblem";
        }

        String plantagemaster = new String();
        try {
            plantagemaster = parseplantagemaster();

        }
        catch (Exception e) {
            e.printStackTrace();
            plantagemaster = "InternetconectionProblem";
        }

        String fertilizernamemaster = new String();
        try {
            fertilizernamemaster = parsefertlizernamemaster();

        }
        catch (Exception e) {
            e.printStackTrace();
            fertilizernamemaster = "InternetconectionProblem";
        }

        String cropfertilizermaster = new String();
        try {
            cropfertilizermaster = parsecropfertlizermaster();

        }
        catch (Exception e) {
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
        return irrigationtypemaster;

    }

    private String parseirrigationtype() {

        String irrigationtype = new String();
        NodeList nodeList;
        String irrigationtypeid = "";
        String irrigationtypeenname = "";
        String irrigationtypeknname = "";

        try {
            InputStream is1 = context.getAssets().open("IrrigationType.xml");
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
                /*handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);*/

            }
            irrigationtype = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            irrigationtype = "Failure";
        }
        return irrigationtype;

    }

    private String parsecroptype() {
        String croptype = new String();
        NodeList nodeList;
        String croptypeid = "";
        String croptypeenname = "";
        String croptypeknname = "";

        try {
            InputStream is1 = context.getAssets().open("CropType.xml");
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
                /*handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);*/

            }
            croptype = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            croptype = "Failure";
        }
        return croptype;
    }

    private String parsecropfertlizermaster() {

        String cropfertilizer = new String();
        NodeList nodeList;
        String cf_id = "";
        String cf_crop_code = "";
        String cf_irrigation_type = "";
        String cf_plant_age = "";
        String cf_nitrogen = "";
        String cf_phosphorous = "";
        String cf_potash = "";


        try {
            InputStream is1 = context.getAssets().open("CropFertilizermaster.xml");
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

                    modelCropFertilizerMasternpk = new ModelCropFertilizerMasternpk(cf_id, cf_crop_code, cf_irrigation_type, cf_plant_age, cf_nitrogen, cf_phosphorous, cf_potash);
                }
                // appDatabase.cropMasterDao().deletecropmaster();
                if (modelCropFertilizerMasternpk != null) {
                    appDatabase.cropFertilizerMasterDAO().insertfertilizercrop(modelCropFertilizerMasternpk);
                }
                /*handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);*/

            }
            cropfertilizer = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            cropfertilizer = "Failure";
        }
        return cropfertilizer;

    }

    private String parsefertlizernamemaster() {

        String fertilizername = new String();
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
            InputStream is1 = context.getAssets().open("FertilizerMaster.xml");
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

                    modelFertilizerNameMaster = new ModelFertilizerNameMaster(feid, fename, fekname, fertilizertype, fertilizernitrogen, fertilizerphosphorous, fertilizerpotash, fertilizernutrient);
                }
                // appDatabase.cropMasterDao().deletecropmaster();
                if (modelFertilizerNameMaster != null) {
                    appDatabase.fertilizerNameMasterDAO().insertFertilizerNames(modelFertilizerNameMaster);
                }
                /*handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);*/

            }
            fertilizername = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            fertilizername = "Failure";
        }
        return fertilizername;

    }

    private String parseplantagemaster() {

        String plantage = new String();
        NodeList nodeList;
        String plantid = "";
        String plantname = "";

        try {
            InputStream is1 = context.getAssets().open("PlantAgeMaster.xml");
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


                    modelPlantAgeMaster = new ModelPlantAgeMaster(plantid, plantname);
                }
                // appDatabase.cropMasterDao().deletecropmaster();
                if (modelPlantAgeMaster != null) {
                    appDatabase.plantAgeMasterDAO().insertplantage(modelPlantAgeMaster);
                }
                /*handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);*/

            }
            plantage = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            plantage = "Failure";
        }
        return plantage;

    }

    private String parsefertilizercropmaster() {

        String fcropdata = new String();
        NodeList nodeList;
        String cropcode = "";
        String cropname_eng = "";
        String cropname_kn = "";
        String croptype = "";

        try {
            InputStream is1 = context.getAssets().open("CropMaster_F.xml");
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
               /* handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);*/

            }
            fcropdata = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            fcropdata = "Failure";
        }
        return fcropdata;
    }

}

