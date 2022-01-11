package com.example.foodies.model;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import java.util.List;

public class User {

    @NonNull
    @PrimaryKey
    String id = "";

    String email= "";
    String password =  "";
    String fullName = "";
    String city = "";
    ImageView image;
    List<Post> postList;

    public User(String email, String password, String fullName, String city, ImageView image, List<Post> postList) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.city = city;
        this.image = image;
        this.postList = postList;
    }

    public User() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}