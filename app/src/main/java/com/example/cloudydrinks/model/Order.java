package com.example.cloudydrinks.model;

public class Order {
    private int product_id, product_price, quantity, totalPrice;
    private String product_name, size;
    private Contact contact;

    public Order() {
    }

    public Order(int product_id, int product_price, int quantity, int totalPrice, String product_name, String size, Contact contact) {
        this.product_id = product_id;
        this.product_price = product_price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.product_name = product_name;
        this.size = size;
        this.contact = contact;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
