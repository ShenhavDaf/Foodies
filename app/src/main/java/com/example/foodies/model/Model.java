package com.example.foodies.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

//    List<Post> data = new LinkedList<Post>();
    ModelFirebase modelFirebase = new ModelFirebase();


    private Model(){
//        for(int i=0;i<100;i++){
//            Post s = new Post(""+i, "dishName","restaurant", "address", "category", "description", "review", null, 5, "userId");
//            data.add(s);
//        }
    }

//    public List<Post> getAllPosts(){
//        return data;
//    }

//
//    public void addPost(Post post){
//        data.add(post);
//    }
//
//    public Post getPostById(String postId) {
//        for (Post p:data
//        ) {
//            if (p.getId().equals(postId)){
//                return p;
//            }
//        }
//        return null;
//    }

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

}
