package com.example.cloudydrinks.model;

import java.io.Serializable;

public class CartModel implements Serializable {
    private int product_id, product_price, quantity, totalPrice;
    private String product_name, product_img_url, size;

    public CartModel() {
    }

    public CartModel(int product_id, int product_price, int quantity, int totalPrice, String product_name, String product_img_url) {
        this.product_id = product_id;
        this.product_price = product_price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.product_name = product_name;
        this.product_img_url = product_img_url;
    }

    public CartModel(int product_id, int product_price, int quantity, int totalPrice, String product_name, String product_img_url, String size) {
        this.product_id = product_id;
        this.product_price = product_price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.product_name = product_name;
        this.product_img_url = product_img_url;
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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

    public String getProduct_img_url() {
        return product_img_url;
    }

    public void setProduct_img_url(String product_img_url) {
        this.product_img_url = product_img_url;
    }

    @Override
    public String toString() {
        return "CartModel{" +
                "product_id=" + product_id +
                ", product_price=" + product_price +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", product_name='" + product_name + '\'' +
                ", product_img_url='" + product_img_url + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
