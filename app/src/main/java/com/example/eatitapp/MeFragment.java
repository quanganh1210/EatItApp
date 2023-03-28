package com.example.eatitapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eatitapp.Common.Common;

public class MeFragment extends Fragment {
    TextView txtName;
    TextView txtPhone;
    TextView txtEatItCoin;
    ImageView btnChangeName;
    ImageView btnChangePhone;
    ImageView btnChangePassword;
    ImageView btnChangeAddress;
    final int CHANGE_NAME = 100;
    Button btnLogOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtName = view.findViewById(R.id.txtName);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtEatItCoin = view.findViewById(R.id.txtEatItCoin);
        btnChangeName = view.findViewById(R.id.btnChangeName);

        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnChangeAddress = view.findViewById(R.id.btnChangeAddress);

        txtName.setText(Common.currentUser.getName());
        txtPhone.setText(Common.currentUser.getPhone());
        txtEatItCoin.setText(Float.toString(Common.currentUser.getEatItCoin()));



        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangeNameActivity.class);
                startActivityForResult(intent, CHANGE_NAME);
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = new Intent(getContext(), MainActivity.class);
                signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signIn);
                Common.isLoadingUserFirstTime = true;
                Common.currentUser = null;
                Common.deliveryAddress = null;
                Common.payment = null;
            }
        });

        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddressActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHANGE_NAME && resultCode == RESULT_OK)
            txtName.setText(Common.currentUser.getName());
    }

}