package com.example.eatitapp.Model;

public class Address {
    String addressID;
    String name;
    String detail;
    String extraInformation;
    Boolean isSelected;

    String UserID;
    public Address() {
    }

    public Address(String name, String detail, String extraInformation, Boolean isSelected) {
        this.name = name;
        this.detail = detail;
        this.extraInformation = extraInformation;
        this.isSelected = isSelected;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getExtraInformation() {
        return extraInformation;
    }

    public void setExtraInformation(String extraInformation) {
        this.extraInformation = extraInformation;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
