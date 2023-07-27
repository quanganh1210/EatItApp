package com.example.eatitapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Interface.ChangeOrderItemQuantity;
import com.example.eatitapp.Model.Food;
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

public class EditOrderDetailAdapter extends RecyclerView.Adapter<EditOrderDetailAdapter.ViewHolder> {
    ArrayList<OrderDetail> lstData;
    Context context;
    Food food;
    int quantity;
    ChangeOrderItemQuantity changeOrderItemQuantity;

    public EditOrderDetailAdapter(Context context, ChangeOrderItemQuantity changeOrderItemQuantity) {
        this.context = context;
        this.changeOrderItemQuantity = changeOrderItemQuantity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_detail_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail item = lstData.get(holder.getPosition());
        DatabaseReference tbFood = FirebaseDatabase.getInstance().getReference("Food");
        tbFood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(item.getFoodID()).exists()) {
                    food = snapshot.child(item.getFoodID()).getValue(Food.class);
                    holder.txtName.setText(food.getName());
                    Picasso.get().load(food.getImage()).fit().centerCrop().into(holder.imgDaiDien);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.txtQuantity.setText(Integer.toString(item.getQuantity()));
        holder.txtPrice.setText(Float.toString(item.getUnitSellingPrice()));

        //Change quantity
        holder.btnAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setQuantity(item.getQuantity() + 1);
                holder.txtQuantity.setText(String.valueOf(item.getQuantity()));
//                DatabaseReference tbOrderDetail = FirebaseDatabase.getInstance().getReference("OrderDetail");
//                tbOrderDetail.orderByChild("orderID").equalTo(item.getOrderID()).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot data : snapshot.getChildren()) {
//                            OrderDetail orderDetail = data.getValue(OrderDetail.class);
//                            if (item.getFoodID().equals(orderDetail.getFoodID())) {
//                                tbOrderDetail.child(data.getKey()).setValue(item);
//                                break;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
                Common.lstOrderDetail.set(holder.getPosition(), item);
                changeOrderItemQuantity.updateOtderSummary(food.getPrice());
            }
        });
        holder.btnSubtractQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item.setQuantity(item.getQuantity() - 1);
                holder.txtQuantity.setText(String.valueOf(item.getQuantity()));
//                DatabaseReference tbOrderDetail = FirebaseDatabase.getInstance().getReference("OrderDetail");
//                tbOrderDetail.orderByChild("orderID").equalTo(item.getOrderID()).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot data : snapshot.getChildren()) {
//                            OrderDetail orderDetail = data.getValue(OrderDetail.class);
//                            if (item.getFoodID().equals(orderDetail.getFoodID())) {
//                                if(item.getQuantity() == 0) {
//                                    tbOrderDetail.child(data.getKey()).removeValue();
//                                    break;
//                                }
//                                tbOrderDetail.child(data.getKey()).setValue(item);
//                                break;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
                if(item.getQuantity() == 0) {
                    Common.lstOrderDetail.remove(holder.getPosition());
                    setLstData(Common.lstOrderDetail);
                    return;
                }
                Common.lstOrderDetail.set(holder.getPosition(), item);
                changeOrderItemQuantity.updateOtderSummary(- food.getPrice());
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
        ImageButton btnAddQuantity;
        ImageButton btnSubtractQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtPrice = itemView.findViewById(R.id.txtDate);
            imgDaiDien = itemView.findViewById(R.id.imgDaiDien);
            btnAddQuantity = itemView.findViewById(R.id.btnAddQuantity);
            btnSubtractQuantity = itemView.findViewById(R.id.btnSubtractQuantity);
        }
    }
}
