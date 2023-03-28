package com.example.eatitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eatitapp.Common.Common;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeNameActivity extends AppCompatActivity {

    TextView txtToolBarTitle;
    ImageView btnBack;
    TextInputEditText txtName;

    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        txtName = findViewById(R.id.txtName);
        btnSave = findViewById(R.id.btnSave);

        txtName.setText(Common.currentUser.getName());
        //Tool bar
        txtToolBarTitle = findViewById(R.id.toolBarTitle);
        btnBack = findViewById(R.id.btnBack);
        txtToolBarTitle.setText("Name");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmpUserID = Common.currentUser.getPhone();
                Common.currentUser.setPhone(null);
                Common.currentUser.setName(txtName.getText().toString());
                DatabaseReference tbUser = FirebaseDatabase.getInstance().getReference("User");
                tbUser.child(tmpUserID).setValue(Common.currentUser);
                Common.currentUser.setPhone(tmpUserID);
                setResult(RESULT_OK);
                finish();
            }
        });

    }
}