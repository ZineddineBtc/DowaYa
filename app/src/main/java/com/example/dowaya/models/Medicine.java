package com.example.dowaya.models;

public class Medicine {
    private int id;
    private String name, priceRange, description, time;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
