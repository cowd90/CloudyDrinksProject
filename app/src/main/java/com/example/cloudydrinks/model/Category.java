package com.example.cloudydrinks.model;

import java.io.Serializable;

public class Category implements Serializable {
    private int category_id;
    private String category_name;
    private String category_img;

    public Category() {
    }

    public Category(int category_id, String category_name, String category_img) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_img = category_img;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_img() {
        return category_img;
    }

    public void setCategory_img(String category_img) {
        this.category_img = category_img;
    }

    @Override
    public String toString() {
        return "Category{" +
                "category_id=" + category_id +
                ", category_name='" + category_name + '\'' +
                ", category_img='" + category_img + '\'' +
                '}';
    }
}
