package com.example.dowaya.models;

public class Store {
    private String id, name, city, phone, address, time;

    public Store(){}

    public Store(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public Store(String name, String city, String phone, String address) {
        this.name = name;
        this.city = city;
        this.phone = phone;
        this.address = address;
    }

    public Store(String id, String name, String city, String phone, String address) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

