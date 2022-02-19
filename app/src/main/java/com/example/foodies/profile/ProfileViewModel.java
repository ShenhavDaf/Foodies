package com.example.foodies.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodies.model.Model;
import com.example.foodies.model.Post;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    LiveData<List<Post>> data;

    public ProfileViewModel() {
        this.data = Model.instance.getUserPosts();
    }

    public LiveData<List<Post>> getData() {
        return data;
    }
}