package org.nic.fruits.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nic.fruits.R;

import org.nic.fruits.pojo.ModelFarmerDetails;

import java.util.List;




//Programming by Harsha  for version 1.0 release
public class FarmerDetailsCardAdapter extends RecyclerView.Adapter<FarmerDetailsCardAdapter.PlanetHolder> {

    private Context context;
    private List<ModelFarmerDetails> planets  ;

    public FarmerDetailsCardAdapter(Context context, List<ModelFarmerDetails> planets) {
        this.context = context;
        this.planets = planets;
    }

    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_farmer_details, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, int position) {
        ModelFarmerDetails planet = planets.get(position);
        holder.setDetails(planet);
    }

    @Override
    public int getItemCount() {
        return planets.size();
    }


    //Programming by Harsha  for version 1.0 release
    class PlanetHolder extends RecyclerView.ViewHolder {

        private TextView txtFarmerNameInKannada, txtFarmerNameInEnglish, txtFarmerFatherNameInKannada, txtAge, txtGender, txtCaste, txtPA,txtMinorities,txtFarmerType,txtAddress,txtDist,txtTaluku,txtVillage,txtHobli;

        PlanetHolder(View itemView) {
            super(itemView);
            txtFarmerNameInKannada = itemView.findViewById(R.id.txtFarmerNameInKannada);
            txtFarmerFatherNameInKannada =  itemView.findViewById(R.id.txtFarmerFatherNameInKannada);
            txtAge =  itemView.findViewById(R.id.txtAge);
            txtGender = itemView.findViewById(R.id.txtGender);
            txtCaste =  itemView.findViewById(R.id.txtCaste);
            txtPA = itemView.findViewById(R.id.txtPA);
            txtMinorities = itemView.findViewById(R.id.txtMinorities);
            txtFarmerType = itemView.findViewById(R.id.txtFarmerType);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtDist = itemView.findViewById(R.id.txtDist);
            txtTaluku = itemView.findViewById(R.id.txtTaluka);
            txtHobli = itemView.findViewById(R.id.txtHobli);
            txtVillage =  itemView.findViewById(R.id.txtVillage);
        }

        void setDetails(ModelFarmerDetails planet) {
            txtFarmerNameInKannada.setText(context.getResources().getString(R.string.name) + " : " +planet.getFarmerNameInKannada());
            txtFarmerFatherNameInKannada.setText(context.getResources().getString(R.string.father_name) + " : " +planet.getFarmerFatherNameInKannada());
            txtAge.setText(context.getResources().getString(R.string.age) + " : " +planet.getAge());
            txtGender.setText(context.getResources().getString(R.string.gender) + " : " +planet.getGender());
            txtCaste.setText(context.getResources().getString(R.string.caste) + " : " +planet.getCaste());
            txtPA.setText(context.getResources().getString(R.string.pa) + " : " +planet.getSpeciallyAbled());
            txtMinorities.setText(context.getResources().getString(R.string.minorities) + " : " +planet.getMinorities());
            txtFarmerType.setText(context.getResources().getString(R.string.farmer_type) + " : " +planet.getFarmerType());
            txtAddress.setText(context.getResources().getString(R.string.address) + " : " +planet.getAddress());
            txtDist.setText(context.getResources().getString(R.string.dist) + " : " +planet.getDistrict());
            txtTaluku.setText(context.getResources().getString(R.string.taluk) + " : " +planet.getTaluku());
            txtHobli.setText(context.getResources().getString(R.string.hobli) + " : " +planet.getHobli());
            txtVillage.setText(context.getResources().getString(R.string.village) + " : " +planet.getVillage());
        }
    }
}