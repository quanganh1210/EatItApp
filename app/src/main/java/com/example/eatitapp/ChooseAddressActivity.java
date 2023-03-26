package com.example.eatitapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Adapter.ChooseAddressRecyclerAdapter;
import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Interface.ChooseAddressListener;
import com.example.eatitapp.Model.Address;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseAddressActivity extends AppCompatActivity {

    Button btnNewAddress;
    RecyclerView recycler;
    DatabaseReference tbAddress;
    ChooseAddressRecyclerAdapter adapter;
    ArrayList<Address> lstAddress;
    TextView txtToolBarTitle;
    ImageView btnBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);

        btnNewAddress = findViewById(R.id.btnNewAddress);
        recycler = findViewById(R.id.recycler_address);


        tbAddress = FirebaseDatabase.getInstance().getReference("Address");
        recycler = findViewById(R.id.recycler_address);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        //Tool bar
        txtToolBarTitle = findViewById(R.id.toolBarTitle);
        btnBack = findViewById(R.id.btnBack);
        txtToolBarTitle.setText("Addresses");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tbAddress.orderByChild("userID").equalTo(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstAddress = new ArrayList<>();
                for(DataSnapshot item: snapshot.getChildren()) {
                    Address address = item.getValue(Address.class);
                    address.setAddressID(item.getKey());
                    lstAddress.add(address);
                }
                adapter = new ChooseAddressRecyclerAdapter(ChooseAddressActivity.this, new ChooseAddressListener() {
                    @Override
                    public void changed(Address address) {
                        Common.deliveryAddress = address;
                        finish();
                    }
                });

                adapter.setLstAddress(lstAddress);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAddressActivity.this, NewAddressActivity.class);
                startActivity(intent);
            }
        });


    }
}