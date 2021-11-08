package org.nic.fruits.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.nic.fruits.CardViewCropSurveyDetailsActivity;
import org.nic.fruits.CardViewFarmerDetailsActivity;
import org.nic.fruits.CardViewFarmerPaymentDetailsActivity;
import org.nic.fruits.CardViewLandDetailsActivity;
import org.nic.fruits.CropDetails.CropDetails;
import org.nic.fruits.CropDetails.CropRegister;
import org.nic.fruits.CropDetails.CropWeeklyData;
import org.nic.fruits.CropDetails.UploadCropDetails;
import org.nic.fruits.FeedBackDetailsActivity;
import org.nic.fruits.NPCIActivity;
import org.nic.fruits.R;
import org.nic.fruits.SoapProxy;
import org.nic.fruits.Utils;
import org.nic.fruits.WeatherForecastActivity;
import org.nic.fruits.database.AppDatabase;
import org.nic.fruits.database.AppExecutors;
import org.nic.fruits.pojo.ModelForeCastDetails;
import org.nic.fruits.pojo.ModelIconWIthDescriptiveName;
import org.nic.fruits.pojo.ModelweatherDetails;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.content.Context.MODE_PRIVATE;
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
import org.nic.fruits.pojo.ModelForeCastDetails;
import org.nic.fruits.pojo.ModelIconWIthDescriptiveName;
import org.nic.fruits.pojo.ModelweatherDetails;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.content.Context.MODE_PRIVATE;
public class CropDetailsHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<ModelIconWIthDescriptiveName> viewitemlists;
    private Context context;
    private String NPCI;
    private String LastUpdated;
    private String Bank;
    private String RequestNumber;
    private AppDatabase appDatabase;

    Integer[] imageId = {R.drawable.cd_1, R.drawable.cd_2, R.drawable.cd_3, R.drawable.cd_4, R.drawable.cd_5, R.drawable.cd_6};
    String[] imagesName = {"image1", "image2", "image3", "image4", "image5", "image6"};

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;

    private int dotscount;
    private ImageView[] dots;
    private String farmerID;
    private String keyValue;

    public CropDetailsHomeAdapter(Context context, List<ModelIconWIthDescriptiveName> list) {
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
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cropdetails_headeritem, parent, false);
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
                        Intent intent = new Intent(context, CropRegister.class);
                        v.getContext().startActivity(intent);
                    } else if (getLayoutPosition() == 2) {
                        Intent paymentDetailsIntent = new Intent(context, CropWeeklyData.class);
                        v.getContext().startActivity(paymentDetailsIntent);
                    } else if (getLayoutPosition() == 3) {
                        Intent intent = new Intent(context, UploadCropDetails.class);
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
            View viewItem = LayoutInflater.from(container.getContext()).inflate(R.layout.cropdetails_content_custom, container, false);
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



}

