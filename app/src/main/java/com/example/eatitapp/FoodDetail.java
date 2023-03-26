package com.example.eatitapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eatitapp.Database.Database;
import com.example.eatitapp.Model.Food;
import com.example.eatitapp.Model.OrderDetail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodDetail extends AppCompatActivity {

    private TextView txtName;
    private TextView txtDescription;
    private TextView txtPrice;
    private DatabaseReference tbFood;
    private String foodID;
    private ImageView imgDD;
    private FloatingActionButton btnAddToCart;
    private TextView txtQuantity;
    private Food food;
    private int quantity = 1;
    private ImageView btnAddQuantity;
    private ImageView btnSubtractQuantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtDate);
        txtQuantity = findViewById(R.id.txtQuantity);
        imgDD = findViewById(R.id.imgDD);
        tbFood = FirebaseDatabase.getInstance().getReference("Food");
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddQuantity = findViewById(R.id.btnAddQuantity);
        btnSubtractQuantity = findViewById(R.id.btnSubtractQuantity);


        if(getIntent() != null)
            foodID = getIntent().getStringExtra("foodID");
        if(!foodID.isEmpty() && foodID != null) {
            tbFood.child(foodID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    food = snapshot.getValue(Food.class);
                    food.setFoodID(snapshot.getKey());
                    txtName.setText(food.getName());
                    txtDescription.setText(food.getDescription());
                    txtPrice.setText(Float.toString(food.getPrice()));
                    Picasso.get().load(food.getImage()).fit().centerCrop().into(imgDD);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db = new Database(FoodDetail.this);
                ArrayList<OrderDetail> lstOrderDetail = db.getCarts();
                boolean isContain = false;
                for(OrderDetail item : lstOrderDetail) {
                    if(item.getFoodID().equals(food.getFoodID())) {
                        isContain = true;
                        break;
                    }
                }
                if(isContain == false) {
                    db.addToCart(new OrderDetail(food.getFoodID(), Integer.parseInt(txtQuantity.getText().toString()), food.getPrice(), food.getDiscount()));
                }
                else
                    db.updateCartQuantity(food.getFoodID(), Integer.parseInt(txtQuantity.getText().toString()));

                Toast.makeText(FoodDetail.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        //Change quantity
        btnAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                txtQuantity.setText(String.valueOf(quantity));
            }
        });
        btnSubtractQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity > 1) {
                    quantity--;
                    txtQuantity.setText(String.valueOf(quantity));
                }

            }
        });

    }
}