package com.example.foodies.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    /* ****************************** Data Members ****************************** */
    final public static String COLLECTION_NAME = "Users";

    String email = "";
    String fullName = "";
    String city = "";
    String image = "myImg";
    List<String> postList;

    /* ****************************** Constructors ****************************** */

    public User() {
    }

    public User(String email, String fullName, String city, String image, List<String> postList) {
        this.email = email;
        this.fullName = fullName;
        this.city = city;
        this.image = image;
        this.postList = postList;
    }

    // for firebase
    public User(String email, String fullName, String city, String image) {
        this.email = email;
        this.fullName = fullName;
        this.city = city;
        this.image = image;
        this.postList = new ArrayList<>();
    }

    /* ****************************** Getters & Setters ****************************** */

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*------------------------------------------------------*/

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /*------------------------------------------------------*/

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /*------------------------------------------------------*/

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /*------------------------------------------------------*/

    public List<String> getPostList() {
        return postList;
    }

    public void setPostList(List<String> postList) {
        this.postList = postList;
    }

    /*------------------------------------------------------*/


    /* ****************************** Functions ****************************** */

    public static User create(Map<String, Object> json) {
        String email = (String) json.get("email");
        String fullName = (String) json.get("fullName");
        String city = (String) json.get("city");
        String image = (String) json.get("image");
        List<String> postList = (List<String>) json.get("postList");

        User user = new User(email, fullName, city, image, postList);
        return user;
    }

    /*------------------------------------------------------*/

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();

        json.put("email", email);
        json.put("fullName", fullName);
        json.put("city", city);
        json.put("image", image);
        json.put("postList", postList);

        return json;
    }


}
