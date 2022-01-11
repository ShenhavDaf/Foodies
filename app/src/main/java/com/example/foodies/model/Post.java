package com.example.foodies.model;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Post {

    final public static String COLLECTION_NAME = "posts";

    @NonNull
    @PrimaryKey
    String id = "";

    String dishName = "";
    String restaurant = "";
    String address = "";
    String category = "";
    String description = "";
    String review = "";
    String image = ""; // TODO...
    String rate = "0";
    String userId = "";


    public Post() {
    }

    public Post(String id, String dishName, String restaurant, String address, String category, String description,
                String review, String image, String rate, String userId) {
        this.id = id;
        this.dishName = dishName;
        this.restaurant = restaurant;
        this.address = address;
        this.category = category;
        this.description = description;
        this.review = review;
        this.image = image;
        this.rate = rate;
        this.userId = userId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }


    public static Post create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String dishName = (String) json.get("dishName");
        String restaurant = (String) json.get("restaurant");
        String address = (String) json.get("address");
        String category = (String) json.get("category");
        String description = (String) json.get("description");
        String review = (String) json.get("review");
        System.out.println("the rate is: " + json.get("rate"));
        String image = (String) json.get("image");
        String rate = (String) json.get("rate");
        String userId = (String) json.get("userId");


        Post post = new Post(id, dishName, restaurant, address, category, description, review, image, rate, userId);
        return post;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("dishName",dishName);
        json.put("restaurant",restaurant);
        json.put("category",category);
        json.put("description",description);
        json.put("review",review);
        json.put("image",image);
        json.put("rate",rate);
        json.put("userId",userId);

        return json;
    }


}
