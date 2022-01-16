package com.example.foodies.model;

public class AuthUser {

    /* ****************************** Data Members ****************************** */

    String email = "";
    String password = "";

    /* ****************************** Constructors ****************************** */

    public AuthUser() {
    }

    public AuthUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /* ****************************** Getters & Setters ****************************** */

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*------------------------------------------------------*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
