package org.nic.fruits.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.nic.fruits.R;

import org.nic.fruits.pojo.ModelFarmerLandDeatails;

import java.util.List;



//Programming by Harsha  for version 1.0 release
public class LandDetailsCardAdapter extends RecyclerView.Adapter<LandDetailsCardAdapter.PlanetHolder> {

    private Context context;
    private List<ModelFarmerLandDeatails> planets;

    public LandDetailsCardAdapter(Context context, List<ModelFarmerLandDeatails> planets) {
        this.context = context;
        this.planets = planets;
    }

    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_land_details, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, int position) {
        ModelFarmerLandDeatails planet = planets.get(position);
        holder.setDetails(planet);
    }

    @Override
    public int getItemCount() {
        return planets.size();
    }

    class PlanetHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtDistrict, txtTaluku, txtHobli, txtVillage, txtSurveyNumber, txtArea;

        PlanetHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtFarmerNameInKannada);
            txtDistrict = itemView.findViewById(R.id.txtFarmerNameInEnglish);
            txtTaluku = itemView.findViewById(R.id.txtFarmerFatherNameInKannada);
            txtHobli = itemView.findViewById(R.id.txtAge);
            txtVillage = itemView.findViewById(R.id.txtGender);
            txtSurveyNumber = itemView.findViewById(R.id.txtCaste);
            txtArea = itemView.findViewById(R.id.txtArea);
        }

        void setDetails(ModelFarmerLandDeatails planet) {
            txtName.setText(context.getResources().getString(R.string.owner_name) + " : " + planet.getFarmerName());
            txtDistrict.setText(context.getResources().getString(R.string.dist) + " : " + planet.getDistrictname());
            txtTaluku.setText(context.getResources().getString(R.string.taluk) + " : " + planet.getTalukName());
            txtHobli.setText(context.getResources().getString(R.string.hobli) + " : " + planet.getHobliName());
            txtVillage.setText(context.getResources().getString(R.string.village) + " : " + planet.getVillageName());
            txtSurveyNumber.setText(context.getResources().getString(R.string.survey_number) + " : " + planet.getSurveyNumber());
            txtArea.setText(context.getResources().getString(R.string.area) + " : " + planet.getArea());
        }
    }
}