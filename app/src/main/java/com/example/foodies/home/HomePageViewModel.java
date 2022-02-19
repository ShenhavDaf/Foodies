package com.example.foodies.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodies.model.Model;
import com.example.foodies.model.Post;

import java.util.List;

public class HomePageViewModel extends ViewModel {
    LiveData<List<Post>> data;

    public HomePageViewModel() {
        this.data = Model.instance.getAllPosts();
    }

    public LiveData<List<Post>> getData() {
        return data;
    }

}
