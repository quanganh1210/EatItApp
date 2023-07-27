package com.example.eatitapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Database.Database;
import com.example.eatitapp.FoodDetail;
import com.example.eatitapp.Model.Food;
import com.example.eatitapp.Model.OrderDetail;
import com.example.eatitapp.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Food> lstFood;

    public FoodRecyclerAdapter(Context context) {
        this.context = context;

    }

    public void setLstFood(ArrayList<Food> lstFood) {
        this.lstFood = lstFood;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = lstFood.get(holder.getPosition());
        holder.txtName.setText(food.getName());
        holder.txtDescription.setText(food.getDescription());
        holder.txtPrice.setText(Float.toString(food.getPrice()));
        Picasso.get().load(food.getImage()).fit().centerCrop().into(holder.img);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodDetail = new Intent(context, FoodDetail.class);
                foodDetail.putExtra("foodID", lstFood.get(holder.getPosition()).getFoodID());
                context.startActivity(foodDetail);
            }
        });
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db = new Database(context);
                ArrayList<OrderDetail> lstOrderDetail = db.getCarts();
                boolean isContain = false;
                for(OrderDetail item : lstOrderDetail)
                    if (item.getFoodID().equals(food.getFoodID())) {
                        isContain = true;
                        break;
                    }
                if(isContain == true)
                    db.updateCartQuantity(food.getFoodID(), 1);
                else
                    db.addToCart(new OrderDetail(food.getFoodID(), 1, food.getPrice(), food.getDiscount()));
                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstFood.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtDescription;
        private TextView txtPrice;
        private ShapeableImageView img;
        private ShapeableImageView btnAdd;
        private ConstraintLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtDate);
            img = itemView.findViewById(R.id.img);
            btnAdd = itemView.findViewById(R.id.imgAdd);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
