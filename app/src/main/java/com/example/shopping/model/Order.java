package com.example.shopping.model;

import android.content.ClipData;

import java.util.List;

public class Order {
    int id;
    String iduser;
    String address;
    String email;
    int amount;
    String totalprice;
    int orderStatus;
    List<ClipData.Item> items;

    public Order(int id, String iduser, String address, String email, int amount, String totalprice, int orderStatus) {
        this.id = id;
        this.iduser = iduser;
        this.address = address;
        this.email = email;
        this.amount = amount;
        this.totalprice = totalprice;
        this.orderStatus = orderStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<ClipData.Item> getItems() {
        return items;
    }

    public void setItems(List<ClipData.Item> items) {
        this.items = items;
    }
}
