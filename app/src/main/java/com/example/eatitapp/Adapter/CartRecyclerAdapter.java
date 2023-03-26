package com.example.eatitapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Database.Database;
import com.example.eatitapp.Interface.ChangeNumberItemListener;
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

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder> {
    ArrayList<OrderDetail> lstData;
    Context context;
    Food food;
    int quantity;
    ChangeNumberItemListener changeNumberItemListener;
    public CartRecyclerAdapter(Context context, ChangeNumberItemListener changeNumberItemListener) {
        this.context = context;
        this.changeNumberItemListener = changeNumberItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_detail_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail item = lstData.get(position);
        DatabaseReference tbFood = FirebaseDatabase.getInstance().getReference("Food");
        tbFood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(item.getFoodID()).exists()) {
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
        quantity = item.getQuantity();
        Log.d("dfasd", Integer.toString(quantity));

        //Change quantity
        holder.btnAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                quantity++;
//                Log.d("f", Integer.toString(quantity));

                Database db = new Database(context);
                int quantity = db.getCartItem(item.getFoodID()).getQuantity();
                quantity++;
                db.updateCartQuantity(item.getFoodID(), 1);
                holder.txtQuantity.setText(String.valueOf(quantity));
                Common.TotalPrice += item.getUnitSellingPrice();
                changeNumberItemListener.Changed();
            }
        });
        holder.btnSubtractQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("f", Integer.toString(quantity));

                Database db = new Database(context);
                int quantity = db.getCartItem(item.getFoodID()).getQuantity();
                if(quantity > 1) {
                    quantity--;
                    db.updateCartQuantity(item.getFoodID(), -1);
                    holder.txtQuantity.setText(String.valueOf(quantity));
                }
                else if(quantity == 1) {
                    db.deleteCartItem(item.getFoodID());
                    setLstData(db.getCarts());
                }
                Common.TotalPrice -= item.getUnitSellingPrice();
                changeNumberItemListener.Changed();
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
