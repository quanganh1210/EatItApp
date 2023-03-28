package com.example.eatitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eatitapp.Adapter.AddressAdapter;
import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Model.Address;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity {
    RecyclerView rcAddress;
    Button btnNewAddress;
    TextView txtToolBarTitle;
    ArrayList<Address> lstAddress;
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        rcAddress = findViewById(R.id.recycler_address);
        rcAddress.setLayoutManager(new LinearLayoutManager(this));
        rcAddress.setHasFixedSize(true);
        btnNewAddress = findViewById(R.id.btnNewAddress);

        //Tool bar
        txtToolBarTitle = findViewById(R.id.toolBarTitle);
        btnBack = findViewById(R.id.btnBack);
        txtToolBarTitle.setText("Address");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, NewAddressActivity.class);
                startActivity(intent);
            }
        });

        DatabaseReference tbAddress = FirebaseDatabase.getInstance().getReference("Address");
        tbAddress.orderByChild("userID").equalTo(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstAddress = new ArrayList<>();
                for(DataSnapshot data: snapshot.getChildren()) {
                    Address address = data.getValue(Address.class);
                    address.setAddressID(data.getKey());
                    lstAddress.add(address);
                }
                AddressAdapter adapter = new AddressAdapter(AddressActivity.this);
                adapter.setLstAddress(lstAddress);
                rcAddress.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}