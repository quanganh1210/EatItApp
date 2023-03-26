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
import com.example.eatitapp.Interface.ChooseAddressListener;
import com.example.eatitapp.Model.Address;
import com.example.eatitapp.R;

import java.util.ArrayList;

public class ChooseAddressRecyclerAdapter extends RecyclerView.Adapter<ChooseAddressRecyclerAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Address> lstAddress;
    private Boolean isNewRadioBtnChecked = false;
    private int lastCheckedPosition = -1;
    private ChooseAddressListener chooseAddressListener;


    public ChooseAddressRecyclerAdapter(Context context, ChooseAddressListener chooseAddressListener) {
        this.context = context;
        this.chooseAddressListener = chooseAddressListener;
    }

    public void setLstAddress(ArrayList<Address> lstAddress) {
        this.lstAddress = lstAddress;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.choose_address_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Address address = lstAddress.get(holder.getAdapterPosition());
        holder.txtName.setText(address.getName());
        holder.txtDetail.setText(address.getDetail());
        holder.txtExtraInfor.setText(address.getExtraInformation());
        //try {
            if(isNewRadioBtnChecked && address != null)
                holder.radioButton.setChecked(address.getSelected());
            else if(address.getAddressID() == Common.deliveryAddress.getAddressID()) {
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
                lstAddress.get(lastCheckedPosition).setSelected(false);
                lstAddress.get(position).setSelected(true);
                Common.deliveryAddress = address;
                lastCheckedPosition = position;
                //notifyDataSetChanged();
                chooseAddressListener.changed(address);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstAddress.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtDetail;
        private TextView txtExtraInfor;
        private RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtDetail = itemView.findViewById(R.id.txtDetail);
            txtExtraInfor = itemView.findViewById(R.id.txtExtraInfor);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }
}
