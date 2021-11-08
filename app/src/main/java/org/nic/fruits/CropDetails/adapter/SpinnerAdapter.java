package org.nic.fruits.CropDetails.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.nic.fruits.R;

import org.nic.fruits.pojo.ModelCropSurveyDetails;

import java.util.List;

public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.PlanetHolder>{

    private Context context;
    private List<ModelCropSurveyDetails> planets  ;

    public SpinnerAdapter(Context context, List<ModelCropSurveyDetails> planets) {
        this.context = context;
        this.planets = planets;
    }

    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_survey_number, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(SpinnerAdapter.PlanetHolder holder, int position) {
        ModelCropSurveyDetails planet = planets.get(position);
        holder.setDetails(planet);
    }

    @Override
    public int getItemCount() {
        return planets.size();
    }

    class PlanetHolder extends RecyclerView.ViewHolder {

        private TextView txt_SurveyNumber;

        PlanetHolder(View itemView) {
            super(itemView);
            txt_SurveyNumber = itemView.findViewById(R.id.txt_surveyNumber);
        }

        void setDetails(ModelCropSurveyDetails planet) {
            txt_SurveyNumber.setText(planet.getSurveyNumber().toString());

        }
    }
}
