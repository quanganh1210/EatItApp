package com.example.eatitapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Model.Address;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class NewAddressActivity extends AppCompatActivity {
    EditText txtName;
    EditText txtDetail;
    EditText txtExtraInfor;
    Button btnAdd;
    DatabaseReference tbAddress;
    //PlacesClient placesClient;
    String detail;
    TextView txtToolBarTitle;
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
//Phan lay
        txtName = findViewById(R.id.txtName);
        txtDetail = findViewById(R.id.txtDetail);
        txtExtraInfor = findViewById(R.id.txtExtraInfor);
        btnAdd = findViewById(R.id.btnAdd);
        tbAddress = FirebaseDatabase.getInstance().getReference("Address");

        //Tool bar
        txtToolBarTitle = findViewById(R.id.toolBarTitle);
        btnBack = findViewById(R.id.btnBack);
        txtToolBarTitle.setText("New address");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Places API
       String apiKey = "AIzaSyDSLnJTWSkuLCpDhqglwUDG8QkvS332c-U";
        //String apiKey = "AIzaSyAWYqWofZcIbDdDFx0XCvy7vw-nnuPGCIE";

        if(!Places.isInitialized())
            Places.initialize(getApplicationContext(), apiKey);
        txtDetail.setFocusable(false);
        txtDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(NewAddressActivity.this);
                startActivityForResult(intent, 100);
            }
        });


//        String apiKey = "AIzaSyCV0FKVi2Ym59dl3Qy8j0JrGtxkBLNCtt4";
//        if(!Places.isInitialized())
//            Places.initialize(getApplicationContext(), apiKey);
//        placesClient = Places.createClient(this);
//        final AutocompleteSupportFragment autocompleteSupportFragment =
//                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.addressSuggest);
//        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
//        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onError(@NonNull Status status) {
//
//            }
//
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                final LatLng latLng = place.getLatLng();
//                detail = place.getAddress().toString();
//
//
//            }
//        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtName.getText().toString().length() == 0)
                    Toast.makeText(NewAddressActivity.this, "Name is required", Toast.LENGTH_SHORT).show();
                else if(txtDetail.getText().toString().length() == 0)
                    Toast.makeText(NewAddressActivity.this, "Address is required", Toast.LENGTH_SHORT).show();
                else {
                    Address address = new Address(txtName.getText().toString(), txtDetail.getText().toString(), txtExtraInfor.getText().toString(), null);
                    address.setUserID(Common.currentUser.getPhone());
                    tbAddress.child(String.valueOf(System.currentTimeMillis())).setValue(address).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(NewAddressActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewAddressActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
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