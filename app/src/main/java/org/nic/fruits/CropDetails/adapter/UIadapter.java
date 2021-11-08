package org.nic.fruits.CropDetails.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.nic.fruits.CropDetails.CropRegister;
import org.nic.fruits.CropDetails.CropWeeklyData;
import org.nic.fruits.CropDetails.UImodel.UiModel;
import org.nic.fruits.CropDetails.UploadCropDetails;
import org.nic.fruits.R;

import java.util.List;

public class UIadapter extends PagerAdapter {

    private List<UiModel> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public UIadapter(List<UiModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container,int position){
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cropdetail_item,container,false);
        ImageView imageView;
        TextView title, desc;
        imageView = view.findViewById(R.id.image_cropregister);
        title = view.findViewById(R.id.title_cropregister);
        desc = view.findViewById(R.id.desc_cropregister);

        imageView.setImageResource(models.get(position).getImage());
        title.setText(models.get(position).getTitle());
        desc.setText(models.get(position).getDesc());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (models.get(position).getTitle().equals("Crop Registration")) {
                    Intent intent_cr = new Intent(context, CropRegister.class);
                    intent_cr.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent_cr);
                } else if (models.get(position).getTitle().equals("Crop Weekly Data")) {
                    Intent intent_cwd = new Intent(context, CropWeeklyData.class);
                    intent_cwd.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent_cwd);
                } else if (models.get(position).getTitle().equals("Upload Crop Details")) {
                    Intent intent_ucd = new Intent(context, UploadCropDetails.class);
                    intent_ucd.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent_ucd);

                }

                /*Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("param", models.get(position).getTitle());
                context.startActivity(intent);*/
                // finish();
            }
        });

        container.addView(view,0);


        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
        container.removeView((View) object);
    }
}
