package com.example.eatitapp.Model;

public class Payment {
    String paymentID;
    String Name;
    Boolean isSelected;

    public Payment() {
    }

    public Payment(String paymentID, String name, Boolean isSelected) {
        this.paymentID = paymentID;
        Name = name;
        this.isSelected = isSelected;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
