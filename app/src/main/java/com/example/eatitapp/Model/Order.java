package com.example.eatitapp.Model;

public class Order {
    String orderID;
    String orderDate;
    float totalPrice;
    String status;
    Float deliveryFee = 4f;
    String userID;
    String paymentID;
    String addressID;

    public Order() {
    }

    public Order(String orderDate, float totalPrice, String status,  String userID, String paymentID, String addressID) {
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.deliveryFee = deliveryFee;
        this.userID = userID;
        this.paymentID = paymentID;
        this.addressID = addressID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Float deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

}
