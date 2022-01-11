package com.example.foodies.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    private Model(){
        for(int i=0;i<100;i++){
            Post s = new Post(""+i, "dishName","restaurant", "address", "category", "description", "review", null, 5);
            data.add(s);
        }
    }

    List<Post> data = new LinkedList<Post>();

    public List<Post> getAllPosts(){
        return data;
    }

    public void addPost(Post post){
        data.add(post);
    }

    public Post getPostById(String postId) {
        for (Post p:data
        ) {
            if (p.getId().equals(postId)){
                return p;
            }
        }
        return null;
    }
}
