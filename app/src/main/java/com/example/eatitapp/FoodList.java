package com.example.eatitapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Adapter.FoodRecyclerAdapter;
import com.example.eatitapp.Model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodList extends AppCompatActivity {

    DatabaseReference tbFood;
    RecyclerView recycler;
    String categoryID;
    String categoryName;
    ArrayList<Food> lstFood;
    FoodRecyclerAdapter adapter;
    TextView txtToolBarTitle;
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        tbFood = FirebaseDatabase.getInstance().getReference("Food");
        recycler = findViewById(R.id.recycler_food);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        //Tool bar
        txtToolBarTitle = findViewById(R.id.toolBarTitle);
        btnBack = findViewById(R.id.btnBack);
        txtToolBarTitle.setText("Food");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        categoryID = bundle.getString("CategoryID");
        categoryName = bundle.getString("CategoryName");

        //Tool bar
        txtToolBarTitle = findViewById(R.id.toolBarTitle);
        btnBack = findViewById(R.id.btnBack);
        txtToolBarTitle.setText(categoryName);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(!categoryID.isEmpty() && categoryID != null) {
            tbFood.orderByChild("categoryID").equalTo(categoryID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    lstFood = new ArrayList<>();
                    for(DataSnapshot item: snapshot.getChildren()) {
                        Food food = item.getValue(Food.class);
                        food.setFoodID(item.getKey());
                        lstFood.add(food);
                    }
                    adapter = new FoodRecyclerAdapter(FoodList.this);
                    adapter.setLstFood(lstFood);
                    recycler.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }


}