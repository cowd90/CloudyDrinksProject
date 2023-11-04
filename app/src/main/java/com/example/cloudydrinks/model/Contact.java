package com.example.cloudydrinks.model;

import java.io.Serializable;

public class Contact implements Serializable {
    private String fullName, phoneNo, city, district, ward, street;

    public Contact() {
    }

    public Contact(String fullName, String phoneNo, String city, String district, String ward, String street) {
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.street = street;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "fullName='" + fullName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", ward='" + ward + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
