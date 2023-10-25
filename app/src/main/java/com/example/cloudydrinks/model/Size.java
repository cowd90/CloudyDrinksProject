package com.example.cloudydrinks.model;

public class Size {
    private int size_id;
    private String size_name;
    private int upsize_price;

    public Size() {
    }

    public Size(int size_id, String size_name, int upsize_price) {
        this.size_id = size_id;
        this.size_name = size_name;
        this.upsize_price = upsize_price;
    }

    public int getSize_id() {
        return size_id;
    }

    public void setSize_id(int size_id) {
        this.size_id = size_id;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public int getUpsize_price() {
        return upsize_price;
    }

    public void setUpsize_price(int upsize_price) {
        this.upsize_price = upsize_price;
    }

    @Override
    public String toString() {
        return "Size{" +
                "size_id=" + size_id +
                ", size_name='" + size_name + '\'' +
                ", upsize_price=" + upsize_price +
                '}';
    }
}
