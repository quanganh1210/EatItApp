package com.example.eatitapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.FoodList;
import com.example.eatitapp.Model.Category;
import com.example.eatitapp.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Category> lstCategory;

    public CategoryRecyclerAdapter(Context context) {
        this.context = context;

    }

    public void setLstCategory(List<Category> lstCategory) {
        this.lstCategory = lstCategory;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = lstCategory.get(holder.getAdapterPosition());
        holder.txtName.setText(category.getName());
        Picasso.get().load(category.getImage()).fit().centerCrop().into(holder.img);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodList.class);
                Bundle b = new Bundle();
                b.putString("CategoryID", lstCategory.get(holder.getAdapterPosition()).getCategoryID());
                b.putString("CategoryName", lstCategory.get(holder.getAdapterPosition()).getName());
                intent.putExtras(b);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return lstCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ShapeableImageView img;
        private CardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtName = itemView.findViewById(R.id.txtName);
            img = itemView.findViewById(R.id.img);
        }
    }
}
