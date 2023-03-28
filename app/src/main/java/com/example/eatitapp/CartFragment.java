package com.example.eatitapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Adapter.CartRecyclerAdapter;
import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Database.Database;
import com.example.eatitapp.Interface.ChangeNumberItemListener;
import com.example.eatitapp.Model.Order;
import com.example.eatitapp.Model.OrderDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    FirebaseDatabase db;
    final int CHOOSE_ADDRESS = 100;
    final int CHOOSE_PAYMENT = 200;
    DatabaseReference request;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView txtAddressDetail;
    TextView txtAddressExtraInfor;
    Button btnOrder;
    CartRecyclerAdapter adapter;
    ArrayList<OrderDetail> lstData;
    ImageView btnChangeAddress;
    TextView txtQuantity;
    DatabaseReference tbAddress;
    TextView txtPaymentName;
    ImageView btnChangePayment;
    TextView txtSubtotal;
    TextView txtTotal;
    TextView txtDeliveryFee;
    float deliveryFee = 4;
    View mView;
    //Paypal
//    static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
//            .clientId("AYeL7Q7-1APY7Ua-8koD4nrCyuAA-_rrN500kAq7FNxyuzwcVZ8oQA7eriyJ-NJBiHhH32FrB3v7EQwO");
//    static final int PAYPAL_REQUEST_CODE = 9999;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseDatabase.getInstance();
        request = db.getReference("Request");
        txtAddressDetail = view.findViewById(R.id.txtDetail);
        txtAddressExtraInfor = view.findViewById(R.id.txtAddressExtraInfor);
        btnOrder = view.findViewById(R.id.btnSave);
        btnChangeAddress = view.findViewById(R.id.btnChangeAddress);
        txtPaymentName = view.findViewById(R.id.txtPaymentName);
        btnChangePayment = view.findViewById(R.id.btnChangePayment);
        txtSubtotal = view.findViewById(R.id.txtSubtotal);
        txtTotal = view.findViewById(R.id.txtTotal);
        txtDeliveryFee = view.findViewById(R.id.txtDeliveryFee);

        //Recycler
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_orderItem);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new CartRecyclerAdapter(getContext(), new ChangeNumberItemListener() {
            @Override
            public void Changed() {
                CalculateTotalPrice();
            }
        });
        lstData = new Database(getContext()).getCarts();
        adapter.setLstData(lstData);
        recyclerView.setAdapter(adapter);

        //Load address & payment
        if(Common.deliveryAddress != null) {
            txtAddressDetail.setText(Common.deliveryAddress.getDetail());
            txtAddressExtraInfor.setText(Common.deliveryAddress.getExtraInformation());
        }
        if(Common.payment != null) {
            txtPaymentName.setText(Common.payment.getName());
        }

        //Caculate total price
        txtDeliveryFee.setText(Float.toString(deliveryFee));
        CalculateTotalPrice();

        //Change address
        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChooseAddressActivity.class);
                startActivityForResult(intent, CHOOSE_ADDRESS);
            }
        });

        //Change payment
        btnChangePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChoosePaymentActivity.class);
                startActivityForResult(intent, CHOOSE_PAYMENT);
            }
        });

        //Place order
        btnOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lstData = new Database(getContext()).getCarts();
                if(Common.deliveryAddress == null) {
                    Toast.makeText(getContext(), "Your delivery address is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(lstData.size() == 0) {
                    Toast.makeText(getContext(), "Your basket is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(Common.TotalPrice > Common.currentUser.getEatItCoin() && Common.payment.getName().equals("EatIt coin")) {
                    Toast.makeText(getContext(), "Your EatIt coin is not enough", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Common.payment.getName().equals("EatIt coin")) {
                    float currentCoin = Common.currentUser.getEatItCoin();
                    Common.currentUser.setEatItCoin(currentCoin - Common.TotalPrice);
                    String phoneTemp = Common.currentUser.getPhone();
                    Common.currentUser.setPhone(null);
                    DatabaseReference tb = FirebaseDatabase.getInstance().getReference("User");
                    tb.child(phoneTemp).setValue(Common.currentUser);
                    Common.currentUser.setPhone(phoneTemp);
                }
                //MMM d,yyyy h:m:s a
                String currentDate = new SimpleDateFormat("MMM d,yyyy h:m:s a").format(Calendar.getInstance().getTime());
                Order order = new Order(currentDate, Common.TotalPrice, "Waitting", Common.currentUser.getPhone(), Common.payment.getPaymentID(), Common.deliveryAddress.getAddressID());
                DatabaseReference table = FirebaseDatabase.getInstance().getReference("Order");
                String orderID = String.valueOf(System.currentTimeMillis());
                table.child(orderID).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Your order is completed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Your order is failed", Toast.LENGTH_SHORT).show();
                    }
                });

                Long orderDetailID = System.currentTimeMillis();
                table = FirebaseDatabase.getInstance().getReference("OrderDetail");
                for(OrderDetail orderDetail: lstData) {
                    orderDetailID++;
                    orderDetail.setOrderID(orderID);
                    table.child(String.valueOf(orderDetailID)).setValue(orderDetail);
                }
                Database sqldb = new Database(getContext());
                sqldb.cleanCart();
                lstData = sqldb.getCarts();
                adapter.setLstData(lstData);
                txtSubtotal.setText("$0");
                txtDeliveryFee.setText("$0");
                txtTotal.setText("$0");

            }
        });

        //Paypal
//        Intent intent = new Intent(getContext(), PayPalService.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        getContext().startService(intent);
//        btnOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(true) {
//                    PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal("1"), "USD", "Eat it app order", PayPalPayment.PAYMENT_INTENT_SALE);
//                    Intent intent2 = new Intent(getContext().getApplicationContext(), PaymentActivity.class);
//                    intent2.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//                    intent2.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
//                    startActivityForResult(intent2, PAYPAL_REQUEST_CODE);
//                }
//
//            }
//        });
    }
    public void CalculateTotalPrice() {
        float subTotal = 0;
        lstData = new Database((getContext())).getCarts();
        for(OrderDetail item:lstData)
            subTotal += item.getUnitSellingPrice() * item.getQuantity();
        txtSubtotal.setText(Float.toString(subTotal));
        txtTotal.setText(Float.toString(subTotal + deliveryFee));
        Common.TotalPrice = subTotal + deliveryFee;
        txtTotal.setText(Float.toString(Common.TotalPrice));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_ADDRESS) {
            if(Common.deliveryAddress != null) {
                txtAddressDetail.setText(Common.deliveryAddress.getDetail());
                txtAddressExtraInfor.setText(Common.deliveryAddress.getExtraInformation());
            }

        }
        if(requestCode == CHOOSE_PAYMENT) {
            if(Common.payment != null) {
                txtPaymentName.setText(Common.payment.getName());
            }
        }
//        if(requestCode == PAYPAL_REQUEST_CODE) {
//            if(resultCode == RESULT_OK) {
//                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//                if(confirmation != null) {
//                    Toast.makeText(getContext(), "Placed order successfully", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }

    }

}