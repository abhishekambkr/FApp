package org.nic.fruits.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.nic.fruits.R;
import org.nic.fruits.pojo.ModelForeCastDetails;

import java.util.List;



//Programming by Harsha  for version 1.0 release
public class WeatherRecyclerAdapter extends RecyclerView.Adapter<WeatherViewHolder> {
    private List<ModelForeCastDetails> itemList;
    private Context context;

    public WeatherRecyclerAdapter(Context context, List<ModelForeCastDetails> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        WeatherViewHolder viewHolder = new WeatherViewHolder(view);
        System.out.println("calling aa foreCastArrayList abnbnb taskEntries " +itemList.size());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder customViewHolder, int i) {
        ModelForeCastDetails forecastItem = itemList.get(i);
        customViewHolder.hours.setText("Hours : " + forecastItem.getHours());
        customViewHolder.rain.setText("Rain : " + forecastItem.getRainFall() +" mm");
        customViewHolder.cloud.setText("Cloud : " + forecastItem.getCloud());
        customViewHolder.temprature.setText("Temperature  : " + forecastItem.getTemperature() +" %c");
        customViewHolder.humidity.setText("Humidity : " + forecastItem.getHumidity() +" %");
        customViewHolder.windSpeed.setText("Wind Speed : " + forecastItem.getWindSpeed() +" m/s");
        customViewHolder.foreCastDate.setText("Forecast Date: " + forecastItem.getLastUpadtedDate());
    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }
}

