package com.example.foodies.model;

import java.util.LinkedList;
import java.util.List;

public class Model {

    /* ****************************** Data Members ****************************** */
    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();

    /* ****************************** Default Constructor ****************************** */
    private Model() {
    }

    /* ******************** Listeners & calling to ModelFirebase ******************** */

    public interface GetAllPostsListener {
        void onComplete(List<Post> list);
    }

    public void getAllPosts(GetAllPostsListener listener) {
        modelFirebase.getAllPosts(listener);
    }

    /* ----------------------------------------------------- */

    public interface AddPostListener {
        void onComplete();
    }

    public void addPost(Post post, String userEmail, AddPostListener listener) {
        modelFirebase.addPost(post, userEmail, listener);
    }

    /* ----------------------------------------------------- */

    public interface GetPostByIdListener {
        void onComplete(Post post);
    }

    public Post getPostById(String postId, GetPostByIdListener listener) {
        modelFirebase.getPostById(postId, listener);
        return null;
    }

    /* ----------------------------------------------------- */

    public interface GetPostsListSizeListener {
        void onComplete(int size);
    }

    public void getPostsListSize(GetPostsListSizeListener listener) {
        modelFirebase.getPostsListSize(listener);
    }

    /* ----------------------------------------------------- */
    public interface GetNextPostIdListener {
        void onComplete(String lastId);
    }

    public void getNextPostId(String userEmail, GetNextPostIdListener listener) {
        modelFirebase.getNextPostId(userEmail, listener);
    }

    /* ----------------------------------------------------- */

    public interface GetAuthListener {
        void onComplete();
    }

    public void addNewUser(String email, String password, GetAuthListener listener) {
        modelFirebase.addNewUser(email, password, listener);
    }

    /* ----------------------------------------------------- */

    public interface UserLoginListener {
        void onComplete(String userID);
    }

    public Post UserLogin(String email, String password, UserLoginListener listener) {
        modelFirebase.UserLogin(email, password, listener);
        return null;
    }

    /* ----------------------------------------------------- */

    public interface AddUserDetailsListener {
        void onComplete();
    }

    public void addUserDetails(User user, AddUserDetailsListener listener) {
        modelFirebase.addUserDetails(user, listener);
    }

    /* ----------------------------------------------------- */

    public interface GetUserByEmailListener {
        void onComplete(User user);
    }

    public void getUserByEmail(String email, GetUserByEmailListener listener) {
        modelFirebase.getUserByEmail(email, listener);
    }

    /* ----------------------------------------------------- */

    public interface GetUserPostsListener {
        void onComplete(List<Post> list);
    }

    public void getUserPosts(User user, GetUserPostsListener listener) {
        modelFirebase.getUserPosts(user, listener);
    }


}
