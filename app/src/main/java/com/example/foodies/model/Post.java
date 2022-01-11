package com.example.foodies.model;

import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public class Post {

    @NonNull
    @PrimaryKey
    String id = "";

    String dishName = "";
    String restaurant = "";
    String address =  "";
    String category = "";
    String description = "";
    String review = "";
    ImageView image;
    Integer rate = 0;




    public Post(){}
    public Post( String id, String dishName, String restaurant, String address, String category, String description, String review, ImageView image, Integer rate) {
        this.id = id;
        this.dishName = dishName;
        this.restaurant = restaurant;
        this.address =  address;
        this.category = category;
        this.description = description;
        this.review = review;
        this.image = image;
        this.rate = rate;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
