package org.nic.fruits.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import org.nic.fruits.R;


//Programming by Harsha  for version 1.0 release
public class WeatherViewHolder extends RecyclerView.ViewHolder {
    public TextView hours;
    public TextView rain;
    public TextView cloud;
    public TextView temprature;
    public TextView humidity;
    public TextView windSpeed;
    public TextView foreCastDate;
    public View lineView;

    public WeatherViewHolder(View view) {
        super(view);
        this.hours = (TextView) view.findViewById(R.id.itemHours);
        this.rain = (TextView) view.findViewById(R.id.itemRain);
        this.cloud = (TextView) view.findViewById(R.id.itemCloud);
        this.temprature = (TextView) view.findViewById(R.id.itemTemprature);
        this.humidity = (TextView) view.findViewById(R.id.itemHumidity);
        this.windSpeed = (TextView) view.findViewById(R.id.itemWindSpeed);
        this.foreCastDate = view.findViewById(R.id.itemForeCastDate);
        this.lineView = view.findViewById(R.id.lineView);
    }
}

