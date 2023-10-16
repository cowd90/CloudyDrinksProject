package com.example.cloudydrinks.domain;

public class FoodDomain {
    private int category_id;
    private int product_id;
    private String product_name;
    private String product_img_url;
    private String product_description;
    private int product_price;

    public FoodDomain() {
    }

    public FoodDomain(int category_id, int product_id, String product_name, String product_img_url, String product_description, int product_price) {
        this.category_id = category_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_img_url = product_img_url;
        this.product_description = product_description;
        this.product_price = product_price;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
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

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }
}
