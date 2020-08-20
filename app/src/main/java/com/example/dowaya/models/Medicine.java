package com.example.dowaya.models;

public class Medicine {
    private String name, priceRange, description;

    public Medicine(){}

    public Medicine(String name) {
        this.name = name;
    }

    public Medicine(String name, String description, String priceRange) {
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
}
