package org.nic.fruits.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.nic.fruits.R;
import org.nic.fruits.pojo.ModelForeCastDetails;

import java.util.List;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;



//Programming by Harsha  for version 1.0 release
public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForeCastHolder> {

  private Context context;
  private List<ModelForeCastDetails> planets;

  public ForecastRecyclerViewAdapter(Context context, List<ModelForeCastDetails> planets) {
    System.out.println("calling aa ForecastRecyclerViewAdapter context "+context);
    this.context = context;
    this.planets = planets;
  }

  @NonNull
  @Override
  public ForeCastHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_planet, parent, false);
    return new ForeCastHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ForeCastHolder holder, int position) {
    ModelForeCastDetails planet = planets.get(position);
    holder.setDetails(planet);
  }

  @Override
  public int getItemCount() {
    return planets.size();
  }


  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }



  //Programming by Harsha  for version 1.0 release
  class ForeCastHolder extends RecyclerView.ViewHolder {

    private TextView hour;
    private TextView rain;
    private TextView cloud;
    private TextView temperature;
    private TextView humidity;
    private TextView windSpeed;
    private TextView forecastDate;

    ForeCastHolder(View itemView) {
      super(itemView);
      hour = itemView.findViewById(R.id.hours);
      rain = itemView.findViewById(R.id.rain);
      cloud = itemView.findViewById(R.id.cloud);
      temperature = itemView.findViewById(R.id.temperature);
      humidity = itemView.findViewById(R.id.humidity);
      windSpeed = itemView.findViewById(R.id.wind_speed);
      forecastDate = itemView.findViewById(R.id.forecast_date);
    }

    void setDetails(ModelForeCastDetails planet) {
      hour.setText(context.getResources().getString(R.string.forecast_hour)+ " : " +planet.getHours());
      rain.setText(context.getResources().getString(R.string.rain)+ " : "  +planet.getRainFall()+" mm");
      cloud.setText(context.getResources().getString(R.string.cloud)+ " : "  +planet.getCloud());
      temperature.setText(context.getResources().getString(R.string.temperature)+ " : "  +planet.getTemperature()+" °C");
      humidity.setText(context.getResources().getString(R.string.humidity)+ " : " +planet.getHumidity()+" %");
      windSpeed.setText(context.getResources().getString(R.string.wind_speed)+ " : " +planet.getWindSpeed()+" m/s");
      forecastDate.setText(context.getResources().getString(R.string.forecast_date)+ " : "  +planet.getLastUpadtedDate());
    }
  }
}
