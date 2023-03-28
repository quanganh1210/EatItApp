package com.example.eatitapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eatitapp.Model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SignUp extends AppCompatActivity {

    TextInputEditText edtName, edtPhone, edtPassword;
    TextInputLayout txtLayoutPhone, txtLayoutName, txtLayoutPassword;
    String phoneNumber, password, name;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtPassword =  findViewById(R.id.edtPassword);
        edtPhone =  findViewById(R.id.edtPhone);
        edtName = findViewById(R.id.edtName);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtLayoutPhone = findViewById(R.id.txtLayoutPhone);
        txtLayoutPassword = findViewById(R.id.txtLayoutPassword);
        txtLayoutName = findViewById(R.id.txtLayoutName);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = db.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();
                phoneNumber = edtPhone.getText().toString();
                password = edtPassword.getText().toString();
                name = edtName.getText().toString();
                if(!validateForm()) {
                    mDialog.dismiss();
                    return;
                }

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(edtPhone.getText().toString()).exists()) {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Phone number is already registered !", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mDialog.dismiss();
                            User user = new User(edtName.getText().toString(), edtPassword.getText().toString(), "Customer");
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "Sign up sucessfully", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    private boolean validateForm() {
        if(phoneNumber.isEmpty()) {
            txtLayoutPhone.setError("Field can't be empty");
            return false;
        }
        else
            txtLayoutPhone.setError(null);
        if(password.isEmpty()) {
            txtLayoutPassword.setError("Field can't be empty");
            return false;
        }
        else
            txtLayoutPhone.setError(null);
        if(name.isEmpty()) {
            txtLayoutName.setError("Field can't be empty");
            return false;
        }
        else
            txtLayoutPhone.setError(null);
        return true;
    }
}