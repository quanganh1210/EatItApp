package com.example.eatitapp.Adapter;

//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;

import static androidx.core.content.ContextCompat.getDrawable;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Common.Common;
import com.example.eatitapp.EditOrderActivity;
import com.example.eatitapp.Model.Order;
import com.example.eatitapp.Model.OrderDetail;
import com.example.eatitapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {
    ArrayList<Order> lstData;
    ArrayList<OrderDetail> lstOrderDetail;
    Context context;

    public OrderRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = lstData.get(position);
        holder.txtOrderID.setText(order.getOrderID());
        holder.txtDate.setText(order.getOrderDate());
        holder.txtPrice.setText(Float.toString(order.getTotalPrice()));

        holder.btnEditOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditOrderActivity.class);
                intent.putExtra("OrderID", order.getOrderID());
                context.startActivity(intent);
            }
        });
        holder.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.cancel_order_dialog);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog.getWindow().setBackgroundDrawable(getDrawable(context,R.drawable.dialog_background));
                }
                //int width = (int)(getResources(context.getTheme()).getDisplayMetrics().widthPixels*0.90);
                dialog.getWindow().setLayout(1000, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                Button btnYes = dialog.findViewById(R.id.btnYes);
                Button btnNo = dialog.findViewById(R.id.btnNo);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference tbOrderDetail = FirebaseDatabase.getInstance().getReference("OrderDetail");
                        tbOrderDetail.orderByChild("orderID").equalTo(order.getOrderID()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                lstOrderDetail = new ArrayList<>();
                                for(DataSnapshot data: snapshot.getChildren()) {
                                    String orderDetailID = data.getKey();
                                    tbOrderDetail.child(orderDetailID).removeValue();
                                }
                                DatabaseReference tbOrder = FirebaseDatabase.getInstance().getReference("Order");
                                tbOrder.child(order.getOrderID()).removeValue();
                                if(order.getPaymentID().equals("2")) {
                                    Common.currentUser.setEatItCoin(Common.currentUser.getEatItCoin() + order.getTotalPrice());
                                    DatabaseReference tbUser = FirebaseDatabase.getInstance().getReference("User");
                                    tbUser.child(Common.currentUser.getPhone()).setValue(Common.currentUser);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Toast.makeText(context, "Your order has been canceled", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        });


    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public void setLstData(ArrayList<Order> lstData) {
        this.lstData = lstData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderID;
        TextView txtDate;
        TextView txtPrice;
        CardView parent;
        ImageView btnEditOrder;
        ImageView btnCancelOrder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderID = itemView.findViewById(R.id.txtOrderID);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            parent = itemView.findViewById(R.id.parent);
            btnEditOrder = itemView.findViewById(R.id.btnEditOrder);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
        }
    }
}
