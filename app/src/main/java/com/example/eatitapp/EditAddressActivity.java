package com.example.eatitapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Model.Address;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class EditAddressActivity extends AppCompatActivity {

    TextView txtName;
    TextView txtDetail;
    TextView txtExtraInfor;
    String addressID;
    Address address;
    Button btnSave, btnDelete;
    TextView txtToolBarTitle;
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        txtName = findViewById(R.id.txtName);
        txtDetail = findViewById(R.id.txtDetail);
        txtExtraInfor = findViewById(R.id.txtExtraInfor);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        Intent intent = getIntent();
        addressID = intent.getStringExtra("addressID");

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

        DatabaseReference tbAddress = FirebaseDatabase.getInstance().getReference("Address");
        tbAddress.child(addressID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                address = snapshot.getValue(Address.class);
                address.setAddressID(addressID);
                txtName.setText(address.getName());
                txtDetail.setText(address.getDetail());
                txtExtraInfor.setText(address.getExtraInformation());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //Places API
        String apiKey = "AIzaSyBl3V6YriQBVBZrvPanhs47zLseoBStIQU";
        if(!Places.isInitialized())
            Places.initialize(getApplicationContext(), apiKey);
        txtDetail.setFocusable(false);
        txtDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(EditAddressActivity.this);
                startActivityForResult(intent, 100);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtName.getText().toString().isEmpty()) {
                    Toast.makeText(EditAddressActivity.this, "Your address name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                address.setName(txtName.getText().toString());
                address.setDetail(txtDetail.getText().toString());
                address.setExtraInformation(txtExtraInfor.getText().toString());
                address.setAddressID(null);
                tbAddress.child(addressID).setValue(address);
                if (Common.deliveryAddress.getAddressID().equals(addressID)) {
                    Common.deliveryAddress = address;
                    Common.deliveryAddress.setAddressID(addressID);
                }
                finish();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tbAddress.child(addressID).removeValue();
                if (Common.deliveryAddress.getAddressID().equals(addressID)) {
                    Common.deliveryAddress = null;
                }
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            txtDetail.setText(place.getAddress());
        }
        else if(requestCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}