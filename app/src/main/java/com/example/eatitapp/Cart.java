package com.example.eatitapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Adapter.CartRecyclerAdapter;
import com.example.eatitapp.Database.Database;
import com.example.eatitapp.Interface.ChangeNumberItemListener;
import com.example.eatitapp.Model.OrderDetail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference request;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView txtAddress;
    Button btnOrder;
    CartRecyclerAdapter adapter;
    ArrayList<OrderDetail> lstData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = FirebaseDatabase.getInstance();
        request = db.getReference("Request");
        txtAddress = findViewById(R.id.txtAddress);
        btnOrder = findViewById(R.id.btnSave);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Recycler
        adapter = new CartRecyclerAdapter(this, new ChangeNumberItemListener() {
            @Override
            public void Changed() {
                CalculateTotalPrice();
            }
        });
        lstData = new Database(this).getCarts();
        adapter.setLstData(lstData);
        recyclerView.setAdapter(adapter);

        //Caculate total price


    }
    public void CalculateTotalPrice() {
        float totalFoodfee = 0;
        float total = 0;
        float percentTax = 0.02f;
        float delivery = 4;
        lstData = new Database(this).getCarts();
        for(OrderDetail item:lstData)
            totalFoodfee += item.getUnitSellingPrice() * item.getQuantity();
        btnOrder.setText("Place Order - " + Float.toString(totalFoodfee) + " $");

    }
}