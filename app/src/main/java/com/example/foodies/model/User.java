package com.example.foodies.model;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    final public static String COLLECTION_NAME = "Users";

//    @NonNull
//    @PrimaryKey
//    String id = "";

    String email= "";
    String password =  "";
    String fullName = "";
    String city = "";
    String image;
    List<Post> postList;

    // TODO: img

    public User(String email, String fullName, String city, String image) {
        this.email = email;
//        this.password = password;
        this.fullName = fullName;
        this.city = city;
        this.image = image;
        this.postList = new ArrayList<>();
    }


    public User() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }


    public static User create(Map<String, Object> json) {

        String email = (String) json.get("email");
//        String password = (String) json.get("password");
        String fullName = (String) json.get("fullName");
        String city = (String) json.get("city");
        String image = (String) json.get("image");

        User user = new User(email, fullName, city, image);

        return user;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();

        json.put("email", email);
        json.put("fullName", fullName);
        json.put("city", city);
        json.put("image", image);

        return json;
    }

//    @NonNull
//    public String getId() {
//        return id;
//    }
//
//    public void setId(@NonNull String id) {
//        this.id = id;
//    }
}
