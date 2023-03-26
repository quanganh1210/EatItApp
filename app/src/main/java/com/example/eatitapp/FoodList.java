package com.example.eatitapp;

import android.os.Bundle;
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
    ArrayList<Food> lstFood;
    FoodRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        tbFood = FirebaseDatabase.getInstance().getReference("Food");
        recycler = findViewById(R.id.recycler_food);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        if(getIntent() != null)
            categoryID = getIntent().getStringExtra("CategoryID");

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
                    Toast.makeText(FoodList.this, categoryID + " " + lstFood.size() + " " + adapter.getItemCount(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }


}