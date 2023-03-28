package com.example.eatitapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Model.Address;
import com.example.eatitapp.Model.Payment;
import com.example.eatitapp.Model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button btnSignIn;
    TextView txtSlogan, txtCreateAccount;
    TextInputEditText edtPhone, edtPassword;
    DatabaseReference tbAddress;
    boolean isValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        txtSlogan = (TextView) findViewById(R.id.txtSlogan);
        txtCreateAccount = (TextView) findViewById(R.id.txtCreateAccount);
        edtPassword =  findViewById(R.id.edtPassword);
        edtPhone =  findViewById(R.id.edtPhone);
        isValid = false;

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = db.getReference("User");


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();
                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(edtPhone.getText().toString()).exists()) {
                            //Get user information
                            mDialog.dismiss();
                            User user = snapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                Common.currentUser = user;
                                Common.currentUser.setPhone(edtPhone.getText().toString());
                                if(Common.isLoadingUserFirstTime) {
                                    //Load first address
                                    tbAddress = FirebaseDatabase.getInstance().getReference("Address");
                                    tbAddress.orderByChild("userID").equalTo(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot item : snapshot.getChildren()) {
                                                Common.deliveryAddress = item.getValue(Address.class);
                                                Common.deliveryAddress.setAddressID(item.getKey());
                                                break;
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    //Load default payment method
                                    DatabaseReference tbPayment = FirebaseDatabase.getInstance().getReference("Payment");
                                    tbPayment.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot item : snapshot.getChildren()) {
                                                Common.payment = item.getValue(Payment.class);
                                                Common.payment.setPaymentID(item.getKey());
                                                break;
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Intent homeIntent = new Intent(MainActivity.this, Home.class);
                                    startActivity(homeIntent);
                                    Common.isLoadingUserFirstTime = false;
                                    finish();
                                }

                            } else
                                Toast.makeText(MainActivity.this, "Wrong password !", Toast.LENGTH_SHORT).show();
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this, "User doesn't exist !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });
        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(signUp);
            }
        });
    }
}