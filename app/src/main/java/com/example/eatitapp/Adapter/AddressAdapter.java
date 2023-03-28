package com.example.eatitapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.EditAddressActivity;
import com.example.eatitapp.Interface.ChooseAddressListener;
import com.example.eatitapp.Model.Address;
import com.example.eatitapp.R;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    Context context;
    ArrayList<Address> lstAddress;
    public AddressAdapter(Context context) {
        this.context = context;
    }

    public void setLstAddress(ArrayList<Address> lstAddress) {
        this.lstAddress = lstAddress;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_address_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Address address = lstAddress.get(holder.getAdapterPosition());
        holder.txtName.setText(address.getName());
        holder.txtDetail.setText(address.getDetail());
        holder.txtExtraInfor.setText(address.getExtraInformation());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditAddressActivity.class);
                intent.putExtra("addressID", address.getAddressID());
                context.startActivity(intent);
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
        CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtName = itemView.findViewById(R.id.txtName);
            txtDetail = itemView.findViewById(R.id.txtDetail);
            txtExtraInfor = itemView.findViewById(R.id.txtExtraInfor);
        }
    }
}
