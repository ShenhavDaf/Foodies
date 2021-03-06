package com.example.foodies.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.foodies.MyApplication;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    /* ****************************** Data Members ****************************** */
    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();

    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    User currentUserModel;

    public User getCurrentUserModel() {
        return currentUserModel;
    }

    public void setCurrentUserModel(User currentUserModel) {
        this.currentUserModel = currentUserModel;
    }


    /* ****************************** Image Upload ****************************** */

    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void setImage(Bitmap imageBitmap, String imageName, String storageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, storageName, listener);
    }

    /* ****************************** Loading State ****************************** */

    public enum LoadingState {
        loading, loaded
    }

    MutableLiveData<LoadingState> postsListLoadingState = new MutableLiveData<LoadingState>();

    public MutableLiveData<LoadingState> getPostsListLoadingState() {
        return postsListLoadingState;
    }

    /* ****************************** Default Constructor ****************************** */
    private Model() {
        postsListLoadingState.setValue(LoadingState.loaded);
    }

    /* ******************** Listeners & calling to ModelFirebase ******************** */

    MutableLiveData<List<Post>> allPostsList = new MutableLiveData<List<Post>>();

    public LiveData<List<Post>> getAllPosts() {
        if (allPostsList.getValue() == null) {
            refreshPostsList();
        }
        return allPostsList;
    }

    MutableLiveData<List<Post>> userPostsListLD = new MutableLiveData<List<Post>>();

    public LiveData<List<Post>> getUserPosts() {
        if (userPostsListLD.getValue() == null) {
            refreshPostsList();
        }

        return userPostsListLD;
    }

    /* ----------------------------------------------------- */

    public interface AddPostListener {
        void onComplete(Boolean isSuccess);
    }

    public void addPost(Post post, String userEmail, AddPostListener listener) {
        modelFirebase.addPost(post, userEmail, new AddPostListener() {
            @Override
            public void onComplete(Boolean isSuccess) {
                refreshPostsList();
                listener.onComplete(true);
            }
        });
    }

    /* ----------------------------------------------------- */

    public interface EditPostListener {
        void onComplete(Boolean isSuccess);
    }

    public void editPost(Post post, EditPostListener listener) {
        modelFirebase.editPost(post, listener);
    }

    /* ----------------------------------------------------- */

    public interface DeletePostListener {
        void onComplete(Boolean isSuccess);
    }

    public void deletePost(Post post, DeletePostListener listener) {
        modelFirebase.deletePost(post, listener);
    }

    /* ----------------------------------------------------- */

    public Post getPostByIdLocalDB(String postId) {

        for (Post p : allPostsList.getValue()) {
            if (p.getId().equals(postId)) {
                return p;
            }
        }

        return null;
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
        void onComplete(Boolean isSuccess);
    }

    public void addNewUser(User user, String email, String password, GetAuthListener listener) {
        modelFirebase.addNewUser(user, email, password, listener);
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
        void onComplete(Boolean isSuccess);
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

    public void refreshPostsList() {

        postsListLoadingState.setValue(LoadingState.loading);

        /*---------- get last local update date ----------*/
        Long lastUpdateDate = MyApplication.getContext()
                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong(Post.LAST_UPDATE, 0);

        /*---------- firebase - get all updates since localUpdateDate ----------*/
        modelFirebase.getAllPosts(lastUpdateDate, new ModelFirebase.GetAllPostsListener() {
            @Override
            public void onComplete(List<Post> list) {

                executor.execute(new Runnable() {
                    @Override
                    public void run() {

                        /*---------- add all records to local db ----------*/
                        Long lud = new Long(0);

                        for (Post post : list) {

                            if (post.getDisplay().booleanValue() == false) {
                                AppLocalDB.db.PostDao().delete(post);

                            } else if (post.getDisplay().booleanValue() == true) {
                                AppLocalDB.db.PostDao().insertAll(post);

                            }
                            if (lud < post.getUpdateDate()) {
                                lud = post.getUpdateDate();
                            }
                        }

                        /*---------- update last local update date ----------*/
                        MyApplication.getContext()
                                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                .edit().putLong(Post.LAST_UPDATE, lud).commit();

                        /*---------- return all data to caller ----------*/
                        List<Post> poList = AppLocalDB.db.PostDao().getAll();
                        Collections.reverse(poList);
                        allPostsList.postValue(poList);
                        postsListLoadingState.postValue(LoadingState.loaded);

                        /*---------- update current user's posts ----------*/

                        List<Post> userPostList = new ArrayList<>();

                        for (Post p : poList) {
                            if (currentUserModel.getPostList().contains(p.getId())) {
                                userPostList.add(p);
                            }
                        }
                        userPostsListLD.postValue(userPostList);
                    }
                });

            }
        });

    }


    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }

    public String getLastUserEmail(){
        return modelFirebase.getLastUserEmail();
    }

    public void UserLogout() {
        modelFirebase.UserLogout();
    }

}
