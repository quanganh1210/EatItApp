package com.example.eatitapp.Model;

public class User {
    private String phone;
    private String name;
    private String password;
    private String role;
    float eatItCoin;

    public User() {
    }

    public User(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getEatItCoin() {
        return eatItCoin;
    }

    public void setEatItCoin(float eatItCoin) {
        this.eatItCoin = eatItCoin;
    }
}
