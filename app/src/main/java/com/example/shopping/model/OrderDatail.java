package com.example.shopping.model;

public class OrderDatail {
    int orderid;
    String idproduct;
    String price;
    String amount;

    public OrderDatail(int orderid, String idproduct, String price, String amount) {
        this.orderid = orderid;
        this.idproduct = idproduct;
        this.price = price;
        this.amount = amount;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(String idproduct) {
        this.idproduct = idproduct;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
