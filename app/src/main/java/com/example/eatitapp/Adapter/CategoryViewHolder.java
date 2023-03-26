package com.example.eatitapp.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Interface.ItemClickListener;
import com.example.eatitapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ItemClickListener itemClickListener;
    public TextView txtName;
    public ImageView img;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        //txtName = (TextView) itemView.findViewById(R.id.);
        img = itemView.findViewById(R.id.imageView);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);
    }
}
