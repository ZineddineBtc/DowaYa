package com.example.dowaya.models;

public class Medicine {

    private String id, name, dose, photo, priceRange, description,
            searchHistoryTime, requestTime, postTime, postAddress;

    public Medicine(){}

    public Medicine(String name) {
        this.name = name;
    }

    public Medicine(String name, String description, String priceRange) {
        this.name = name;
        this.description = description;
        this.priceRange = priceRange;
    }

    public Medicine(String id, String name, String description, String priceRange) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchHistoryTime() {
        return searchHistoryTime;
    }

    public void setSearchHistoryTime(String searchHistoryTime) {
        this.searchHistoryTime = searchHistoryTime;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }
}
