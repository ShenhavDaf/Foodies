package com.example.foodies.model;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Post {

    /* ****************************** Data Members ****************************** */
    final public static String COLLECTION_NAME = "posts";
    final public static String LAST_UPDATE = "PostsLastUpdateDate";

    @NonNull
    @PrimaryKey
    String id = "";

    String dishName = "";
    String restaurant = "";
    String address = "";
    String category = "";
    String description = "";
    String review = "";
    String image = "";
    String rate = "0";
    String userEmail = "";
    String userFullName = "";// TODO: for post author in home page
    Long updateDate = new Long(0);

    Boolean display = true;

    /* ****************************** Constructors ****************************** */

    public Post() {
    }

    public Post(String id, String dishName, String restaurant, String address, String category, String description,
                String review, String image, String rate, String userEmail, Boolean display) {
        this.id = id;
        this.dishName = dishName;
        this.restaurant = restaurant;
        this.address = address;
        this.category = category;
        this.description = description;
        this.review = review;
        this.image = image;
        this.rate = rate;
        this.userEmail = userEmail;
        this.display = display;
    }

    /* ****************************** Getters & Setters ****************************** */
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    /*------------------------------------------------------*/

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    /*------------------------------------------------------*/

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    /*------------------------------------------------------*/

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /*------------------------------------------------------*/

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /*------------------------------------------------------*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*------------------------------------------------------*/

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    /*------------------------------------------------------*/

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /*------------------------------------------------------*/

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    /*------------------------------------------------------*/

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }

    /*------------------------------------------------------*/

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    /*------------------------------------------------------*/

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }


    /* ****************************** Functions ****************************** */

    public static Post create(Map<String, Object> json) {
        String id = (String) json.get("id").toString();
        String dishName = (String) json.get("dishName");
        String restaurant = (String) json.get("restaurant");
        String address = (String) json.get("address");
        String category = (String) json.get("category");
        String description = (String) json.get("description");
        String review = (String) json.get("review");
        String image = (String) json.get("image");
        String rate = (String) json.get("rate");
        String userEmail = (String) json.get("userEmail");
        Boolean display = (Boolean) json.get("display");

        Timestamp ts = (Timestamp) json.get("updateDate");
        Long updateDate = ts.getSeconds();

        Post post = new Post(id, dishName, restaurant, address, category, description,
                review, image, rate, userEmail, display);
        post.setUpdateDate(updateDate);

        return post;
    }

    /*------------------------------------------------------*/

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id", id);
        json.put("dishName", dishName);
        json.put("restaurant", restaurant);
        json.put("address", address);
        json.put("category", category);
        json.put("description", description);
        json.put("review", review);
        json.put("image", image);
        json.put("rate", rate);
        json.put("userEmail", userEmail);
        json.put("display", display);


        json.put("updateDate", FieldValue.serverTimestamp());


        return json;
    }

    /*------------------------------------------------------*/

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        StringBuilder builder = new StringBuilder();
        builder.append(day + "." + month + "." + year);
        String date = builder.toString();

        return date;
    }


}
