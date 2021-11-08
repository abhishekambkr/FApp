package org.nic.fruits.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.nic.fruits.R;
import org.nic.fruits.pojo.ModelPaymentDetails;

import java.util.List;



//Programming by Harsha  for version 1.0 release
public class PaymentDetailsCardAdapter extends RecyclerView.Adapter<PaymentDetailsCardAdapter.PlanetHolder> {

    private Context context;
    private List<ModelPaymentDetails> planets;

    public PaymentDetailsCardAdapter(Context context, List<ModelPaymentDetails> planets) {
        this.context = context;
        this.planets = planets;
    }

    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_payment_details, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, int position) {
        ModelPaymentDetails planet = planets.get(position);
        holder.setDetails(planet);
    }

    @Override
    public int getItemCount() {
        return planets.size();
    }

    class PlanetHolder extends RecyclerView.ViewHolder {

        private TextView txtBid, name, department, scheme, financialYear, k2UTRNo, paymentDate, paymentMode, sanctionedAmount;

        PlanetHolder(View itemView) {
            super(itemView);
            txtBid = itemView.findViewById(R.id.txtBID);
            name = itemView.findViewById(R.id.txtName);
            department = itemView.findViewById(R.id.txtDepartment);
            scheme = itemView.findViewById(R.id.txtScheme);
            financialYear = itemView.findViewById(R.id.txtFinYear);
            k2UTRNo = itemView.findViewById(R.id.txtK2UTR);
            paymentDate = itemView.findViewById(R.id.txtPaymentDate);
            paymentMode = itemView.findViewById(R.id.txtPaymentMode);
            sanctionedAmount = itemView.findViewById(R.id.txtAmount);
        }

        void setDetails(ModelPaymentDetails planet) {
            txtBid.setText(context.getResources().getString(R.string.bid) + " : " + planet.getBeneficiaryId());
            name.setText(context.getResources().getString(R.string.name) + " : " + planet.getName());
            department.setText(context.getResources().getString(R.string.department) + " : " + planet.getDepartment());
            scheme.setText(context.getResources().getString(R.string.scheme) + " : " + planet.getScheme());
            financialYear.setText(context.getResources().getString(R.string.finanacial_year) + " : " + planet.getFinancialYear());
            k2UTRNo.setText(context.getResources().getString(R.string.k2utr_no) + " : " + planet.getK2UTRNo());
            paymentDate.setText(context.getResources().getString(R.string.payment_date) + " : " + planet.getPaymentDate());
            paymentMode.setText(context.getResources().getString(R.string.payment_mode) + " : " + planet.getPaymentMode());
            sanctionedAmount.setText(context.getResources().getString(R.string.amount) + " : " + planet.getSanctionedAmount());
        }
    }

}
