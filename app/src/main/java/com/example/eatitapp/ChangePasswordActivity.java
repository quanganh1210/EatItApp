package com.example.eatitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatitapp.Common.Common;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {

    TextInputEditText txtOldPassword;
    TextInputEditText txtNewPassword;
    TextInputEditText txtConfirmPassword;
    TextView txtToolBarTitle;
    ImageView btnBack;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        txtOldPassword = findViewById(R.id.txtOldPassword);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnSave = findViewById(R.id.btnSave);

        //Tool bar
        txtToolBarTitle = findViewById(R.id.toolBarTitle);
        btnBack = findViewById(R.id.btnBack);
        txtToolBarTitle.setText("Password");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = txtOldPassword.getText().toString();
                String newPassword = txtNewPassword.getText().toString();
                String confirmPassword = txtConfirmPassword.getText().toString();
                if(!oldPassword.equals(Common.currentUser.getPassword())) {
                    Toast.makeText(ChangePasswordActivity.this, "Old password isn't correct", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "New password and confirm pass do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                String tmpUserID = Common.currentUser.getPhone();
                Common.currentUser.setPhone(null);
                Common.currentUser.setPassword(newPassword);
                DatabaseReference tbUser = FirebaseDatabase.getInstance().getReference("User");
                tbUser.child(tmpUserID).setValue(Common.currentUser);
                Common.currentUser.setPhone(tmpUserID);
                finish();
            }
        });

    }
}