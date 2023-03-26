package com.example.eatitapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Adapter.OrderRecyclerAdapter;
import com.example.eatitapp.Common.Common;
import com.example.eatitapp.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class WaittingOrderFragment extends Fragment {
    RecyclerView recycler;
    ArrayList<Order> lstOrder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_waitting_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = view.findViewById(R.id.recycler_order);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));


        DatabaseReference tbOrder = FirebaseDatabase.getInstance().getReference("Order");
                tbOrder.orderByChild("status").equalTo("Waitting").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        lstOrder = new ArrayList<>();
                        for(DataSnapshot data: snapshot.getChildren()) {
                            Order order = data.getValue(Order.class);
                            if(order.getUserID().equals(Common.currentUser.getPhone())) {
                                order.setOrderID(data.getKey());
                                lstOrder.add(order);
                            }

                        }
                        OrderRecyclerAdapter adapter = new OrderRecyclerAdapter(getContext());
                        Collections.sort(lstOrder, new Comparator<Order>() {
                            @Override
                            public int compare(Order o1, Order o2) {
                                String dateInString = "Mar 23, 2023 5:44:19 PM";
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d,yyyy h:m:s a");
                                try {
                                    Date dateTime1 = dateFormat.parse(o1.getOrderDate());
                                    Date dateTime2 = dateFormat.parse(o2.getOrderDate());
                                    return dateTime2.compareTo(dateTime1);
                                }catch (ParseException e) {
                                    Toast.makeText(getContext(), "Khong ep kieu duoc", Toast.LENGTH_SHORT).show();
                                    return 0;
                                }

                            }
                        });
                        adapter.setLstData(lstOrder);
                        recycler.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}