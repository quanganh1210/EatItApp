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

import com.example.eatitapp.Adapter.EditOrderRecyclerAdapter;
import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Interface.ChangeOrderItemQuantity;
import com.example.eatitapp.Model.Address;
import com.example.eatitapp.Model.Order;
import com.example.eatitapp.Model.OrderDetail;
import com.example.eatitapp.Model.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditOrderActivity extends AppCompatActivity {

    TextView txtAddressDetail;
    TextView txtAddressInfor;
    TextView txtPaymentName;
    RecyclerView recycler;
    TextView txtSubtotal;
    TextView txtDeliveryFee;
    TextView txtTotal;
    TextView toolBarTitle;
    Button btnCancel;
    Button btnSave;

    ImageView btnBack;
    String orderID;
    String addressID;
    String paymentID;
    Order order;
    Address address;
    Payment payment;
    ArrayList<OrderDetail> lstData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        Intent intent = getIntent();
        orderID = intent.getStringExtra("OrderID");


        txtAddressDetail = findViewById(R.id.txtAddressDetail);
        txtAddressInfor = findViewById(R.id.txtAddressExtraInfor);
        txtPaymentName = findViewById(R.id.txtPaymentName);
        recycler = findViewById(R.id.recycler_orderItem);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtDeliveryFee = findViewById(R.id.txtDeliveryFee);
        txtTotal = findViewById(R.id.txtTotal);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        //Tool bar
        toolBarTitle = findViewById(R.id.toolBarTitle);
        toolBarTitle.setText("Edit order");
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatabaseReference tbOrder = FirebaseDatabase.getInstance().getReference("Order");
        tbOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                order = snapshot.child(orderID).getValue(Order.class);
                order.setOrderID(orderID);
                addressID = order.getAddressID();
                paymentID = order.getPaymentID();

                //Load address
                DatabaseReference tbAddress = FirebaseDatabase.getInstance().getReference("Address");
                tbAddress.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        address = snapshot.child(addressID).getValue(Address.class);
                        if (address != null) {
                            txtAddressDetail.setText(address.getDetail());
                            txtAddressInfor.setText(address.getExtraInformation());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Load payment
                DatabaseReference tbPayment = FirebaseDatabase.getInstance().getReference("Payment");
                tbPayment.child(paymentID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            payment = snapshot.getValue(Payment.class);
                        if (payment != null) {
                            txtPaymentName.setText(payment.getName());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Load Order summary
                loadOrderSummary();


                //Load OrderDetail

                DatabaseReference tbOrderDetail = FirebaseDatabase.getInstance().getReference("OrderDetail");
                tbOrderDetail.orderByChild("orderID").equalTo(orderID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        lstData = new ArrayList<>();
                        for(DataSnapshot data: snapshot.getChildren()) {
                            OrderDetail orderDetail = data.getValue(OrderDetail.class);
                            orderDetail.setOrderDetailID(data.getKey());
                            lstData.add(orderDetail);

                        }
                        EditOrderRecyclerAdapter  editOrderRecyclerAdapter= new EditOrderRecyclerAdapter(EditOrderActivity.this, new ChangeOrderItemQuantity() {
                            @Override
                            public void updateOtderSummary(float unitSellingPrice) {
                                order.setTotalPrice(order.getTotalPrice() + unitSellingPrice);
                                loadOrderSummary();
                            }
                        });
                        Common.lstOrderDetail = lstData;
                        editOrderRecyclerAdapter.setLstData(lstData);
                        recycler.setAdapter(editOrderRecyclerAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference tbOrderDetail = FirebaseDatabase.getInstance().getReference("OrderDetail");
                tbOrder.child(order.getOrderID()).setValue(order);
                for(OrderDetail orderDetail: Common.lstOrderDetail) {
                    String tempKey = orderDetail.getOrderDetailID();
                    orderDetail.setOrderDetailID(null);
                    tbOrderDetail.child(tempKey).setValue(orderDetail);
                }
                finish();
            }
        });
    }
    public void loadOrderSummary() {
        txtSubtotal.setText(Float.toString(order.getTotalPrice() - order.getDeliveryFee()));
        txtDeliveryFee.setText(Float.toString(order.getDeliveryFee()));
        txtTotal.setText(Float.toString(order.getTotalPrice()));
    }
}