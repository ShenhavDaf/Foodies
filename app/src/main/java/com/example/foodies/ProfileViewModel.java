package com.example.foodies;

import androidx.lifecycle.ViewModel;

import com.example.foodies.model.Post;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    List<Post> data;

    public List<Post> getData() {
        return data;
    }

    public void setData(List<Post> data) {
        this.data = data;
    }
}
