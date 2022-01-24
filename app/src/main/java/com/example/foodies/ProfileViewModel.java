package com.example.foodies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodies.model.Model;
import com.example.foodies.model.Post;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends ViewModel {

    LiveData<List<Post>> data;


    public ProfileViewModel() {
        this.data =
                Model.instance.getUserPostsLocalDB(
                        Model.instance.getCurrentUserModel().getPostList());


    }

    public LiveData<List<Post>> getData() {
        return data;
    }
}