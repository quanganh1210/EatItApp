package com.example.eatitapp.Model;

public class OrderDetail {
    private String foodID;
    private String orderID;
    private int quantity;
    private float unitSellingPrice;
    private float discount;
    private String orderDetailID;

    public OrderDetail() {
    }

    public OrderDetail(String foodID, int quantity, float unitSellingPrice, float discount) {
        this.foodID = foodID;
        this.quantity = quantity;
        this.unitSellingPrice = unitSellingPrice;
        this.discount = discount;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getUnitSellingPrice() {
        return unitSellingPrice;
    }

    public void setUnitSellingPrice(float unitSellingPrice) {
        this.unitSellingPrice = unitSellingPrice;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getOrderDetailID() {
        return orderDetailID;
    }

    public void setOrderDetailID(String orderDetailID) {
        this.orderDetailID = orderDetailID;
    }
}
