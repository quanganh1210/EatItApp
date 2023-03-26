package com.example.eatitapp.Common;

import com.example.eatitapp.Model.Address;
import com.example.eatitapp.Model.OrderDetail;
import com.example.eatitapp.Model.Payment;
import com.example.eatitapp.Model.User;

import java.util.ArrayList;

public class Common {
    public static User currentUser;
    public static float TotalPrice;
    public static Address deliveryAddress;
    public static Payment payment;
    public static boolean isLoadingUserFirstTime = true;
    public static ArrayList<OrderDetail> lstOrderDetail;

}
