package com.example.eatitapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Interface.ChoosePaymentListener;
import com.example.eatitapp.Model.Payment;
import com.example.eatitapp.R;

import java.util.ArrayList;

public class ChoosePaymentRecyclerAdapter extends RecyclerView.Adapter<ChoosePaymentRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Payment> lstPayment;
    private Boolean isNewRadioBtnChecked = false;
    private int lastCheckedPosition = -1;
    private ChoosePaymentListener choosePaymentListener;

    public ChoosePaymentRecyclerAdapter(Context context, ChoosePaymentListener choosePaymentListener) {
        this.context = context;
        this.choosePaymentListener = choosePaymentListener;
    }

    public void setLstPayment(ArrayList<Payment> lstPayment) {
        this.lstPayment = lstPayment;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.choose_payment_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Payment payment = lstPayment.get(holder.getAdapterPosition());
        holder.txtName.setText(payment.getName());

        //try {

        if(isNewRadioBtnChecked && payment != null)
            holder.radioButton.setChecked(payment.getSelected());
        else if(payment.getPaymentID() == Common.payment.getPaymentID()) {
            holder.radioButton.setChecked(true);
            lastCheckedPosition = position;
        }
        //}catch(Exception e) {
//            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG);
//            Log.d("Error", "Loi o day : ________" + e.toString());
//        }


        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNewRadioBtnChecked = true;
                lstPayment.get(lastCheckedPosition).setSelected(false);
                lstPayment.get(position).setSelected(true);
                Common.payment = payment;
                lastCheckedPosition = position;
                //notifyDataSetChanged();
                choosePaymentListener.changed();
            }
        });


    }

    @Override
    public int getItemCount() {
        return lstPayment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }
}
