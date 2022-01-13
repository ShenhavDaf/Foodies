package com.example.foodies.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    ModelFirebase modelFirebase = new ModelFirebase();


    private Model(){
    }

    /* ----------------------------------------------------- */

    public interface GetAllPostsListener {
        void onComplete(List<Post> list);
    }

    public void getAllPosts(GetAllPostsListener listener){
        modelFirebase.getAllPosts(listener);
    }

    /* ----------------------------------------------------- */

    public interface AddPostListener {
        void onComplete();
    }

    public void addPost(Post post, AddPostListener listener){
        modelFirebase.addPost( post,  listener);
    }

    /* ----------------------------------------------------- */

    public interface GetPostByIdListener {
        void onComplete(Post post);
    }

    public Post getPostById(String postId, GetPostByIdListener listener) {
        modelFirebase.getPostById(postId,listener);
        return null;
    }

    /* ----------------------------------------------------- */

    public interface GetListSizeListener {
        void onComplete(int size);
    }

    public void getListSize(GetListSizeListener listener) {
        modelFirebase.getListSize(listener);
    }

    /* ----------------------------------------------------- */
    public interface GetNextPostIdListener {
        void onComplete(String lastId);
    }

    public void getNextPostId(GetNextPostIdListener listener) {
        modelFirebase.getNextPostId(listener);
    }

    /* ----------------------------------------------------- */

}
