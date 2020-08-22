package com.example.dowaya.models;

public class Medicine {
    private int id;
    private String name, photo, priceRange, description, historyTime, requestTime;

    public Medicine(){}

    public Medicine(String name) {
        this.name = name;
    }

    public Medicine(String name, String description, String priceRange) {
        this.name = name;
        this.description = description;
        this.priceRange = priceRange;
    }

    public Medicine(int id, String name, String description, String priceRange) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceRange = priceRange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHistoryTime() {
        return historyTime;
    }

    public void setHistoryTime(String historyTime) {
        this.historyTime = historyTime;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}
