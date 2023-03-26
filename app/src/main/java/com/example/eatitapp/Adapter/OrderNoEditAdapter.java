package com.example.eatitapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Model.Order;
import com.example.eatitapp.R;

import java.util.ArrayList;

public class OrderNoEditAdapter extends RecyclerView.Adapter<OrderNoEditAdapter.ViewHolder> {
    ArrayList<Order> lstData;
    Context context;
    public OrderNoEditAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_no_edit_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = lstData.get(position);
        holder.txtOrderID.setText(order.getOrderID());
        holder.txtDate.setText(order.getOrderDate());
        holder.txtPrice.setText(Float.toString(order.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public void setLstData(ArrayList<Order> lstData) {
        this.lstData = lstData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderID;
        TextView txtDate;
        TextView txtPrice;
        CardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderID = itemView.findViewById(R.id.txtOrderID);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
