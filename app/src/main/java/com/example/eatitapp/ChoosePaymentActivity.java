package com.example.eatitapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Adapter.ChoosePaymentRecyclerAdapter;
import com.example.eatitapp.Interface.ChoosePaymentListener;
import com.example.eatitapp.Model.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChoosePaymentActivity extends AppCompatActivity {

    RecyclerView rc;
    TextView txtToolBarTitle;
    ImageView btnBack;
    ArrayList<Payment> lstPayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment);

        rc = findViewById(R.id.recycler_payment);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(this));
        lstPayment = new ArrayList<>();

        //Tool bar
        txtToolBarTitle = findViewById(R.id.toolBarTitle);
        btnBack = findViewById(R.id.btnBack);
        txtToolBarTitle.setText("Payment");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatabaseReference tbPayment = FirebaseDatabase.getInstance().getReference("Payment");
        tbPayment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item : snapshot.getChildren()) {
                    Payment payment = item.getValue(Payment.class);
                    payment.setPaymentID(item.getKey());
                    lstPayment.add(payment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ChoosePaymentRecyclerAdapter adapter = new ChoosePaymentRecyclerAdapter(this, new ChoosePaymentListener() {
            @Override
            public void changed() {
                finish();
            }
        });
        adapter.setLstPayment(lstPayment);
        rc.setAdapter(adapter);
    }
}