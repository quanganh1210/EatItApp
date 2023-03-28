package com.example.eatitapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Interface.ChangeOrderItemQuantity;
import com.example.eatitapp.Model.Food;
import com.example.eatitapp.Model.Order;
import com.example.eatitapp.Model.OrderDetail;
import com.example.eatitapp.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    ArrayList<OrderDetail> lstData;
    Context context;
    Food food;

    public OrderDetailAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_detail_item_2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail orderDetail = lstData.get(position);

        DatabaseReference tbFood = FirebaseDatabase.getInstance().getReference("Food");
        tbFood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(orderDetail.getFoodID()).exists()) {
                    food = snapshot.child(orderDetail.getFoodID()).getValue(Food.class);
                    food.setFoodID(orderDetail.getFoodID());
                    holder.txtName.setText(food.getName());
                    holder.txtQuantity.setText("x " + Integer.toString(orderDetail.getQuantity()));
                    holder.txtPrice.setText(Float.toString(orderDetail.getUnitSellingPrice()));
                    Picasso.get().load(food.getImage()).fit().centerCrop().into(holder.imgDaiDien);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }
    public void setLstData(ArrayList<OrderDetail> lstData) {
        this.lstData = lstData;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtQuantity;
        TextView txtPrice;
        ShapeableImageView imgDaiDien;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtPrice = itemView.findViewById(R.id.txtDate);
            imgDaiDien = itemView.findViewById(R.id.imgDaiDien);
        }
    }
}
