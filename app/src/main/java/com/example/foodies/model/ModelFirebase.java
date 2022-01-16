package com.example.foodies.model;

import android.content.Context;
import android.net.wifi.hotspot2.pps.Credential;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.foodies.MainActivity;
import com.example.foodies.SignUpFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    final public static String URL = "https://foodies-14955-default-rtdb.europe-west1.firebasedatabase.app";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth myAuth = FirebaseAuth.getInstance();


    /* ****************************** Posts Functions ****************************** */

    public void addPost(Post post, String userEmail, Model.AddPostListener listener) {
        Map<String, Object> json = post.toJson();

        db.collection(Post.COLLECTION_NAME)
                .document(post.getId())
                .set(json)
                .addOnSuccessListener(unused -> {

                    System.out.println("Addition new post to firebase was success");

                    db.collection(User.COLLECTION_NAME)
                            .document(userEmail)
                            .get()
                            .addOnCompleteListener(task -> {

                                if (task.isSuccessful() & task.getResult() != null) {

                                    User myUser = User.create(task.getResult().getData());
                                    myUser.getPostList().add(post.getId());

                                    Map<String, Object> updateJson = myUser.toJson();

                                    db.collection(User.COLLECTION_NAME)
                                            .document(userEmail).set(updateJson, SetOptions.merge())
                                            .addOnSuccessListener(command -> {

                                                System.out.println("Addition new post to firebase into user post list was success");
                                                listener.onComplete();

                                            }).addOnFailureListener(command -> {

                                        System.out.println("Addition new post to firebase into user post list was failed");
                                        listener.onComplete();
                                    });
                                }
                            });
                })

                .addOnFailureListener(e -> {
                    System.out.println("Addition new post to firebase was failed");
                    listener.onComplete();
                });
    }

    /* -------------------------------------------------------------------------- */

    public void getPostById(String postId, Model.GetPostByIdListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .document(postId)
                .get()
                .addOnCompleteListener(task -> {
                    Post post = null;
                    if (task.isSuccessful() & task.getResult() != null) {
                        post = Post.create(task.getResult().getData());
                    }
                    listener.onComplete(post);
                });
    }

    /* -------------------------------------------------------------------------- */

    public void getAllPosts(Model.GetAllPostsListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    List<Post> list = new LinkedList<Post>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Post post = Post.create(doc.getData());
                            if (post != null) {
                                list.add(post);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }
    /* -------------------------------------------------------------------------- */

    public void getPostsListSize(Model.GetPostsListSizeListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    int size = 0;
                    if (task.isSuccessful()) {
                        size = task.getResult().getDocuments().size();
                    }
                    listener.onComplete(size);
                });
    }

    /* -------------------------------------------------------------------------- */

    public void getNextPostId(String userEmail, Model.GetNextPostIdListener listener) {


        db.collection(User.COLLECTION_NAME)
                .document(userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() & task.getResult() != null) {

                        User myUser = User.create(task.getResult().getData());
                        int size = myUser.getPostList().size();
                        String lastLetters = null;

                        if (size == 0) {
                            lastLetters = "0";
                        } else if (size > 0) {

                            String lastPost = myUser.getPostList().get(size - 1);
                            StringBuilder email = new StringBuilder();
                            email.append(userEmail).append('_');
                            String[] array = lastPost.split(email.toString());
                            for (String s : array) {
                                lastLetters = s;
                            }
                        }

                        int nextNumber = Integer.parseInt(lastLetters);
                        nextNumber++;

                        StringBuilder idToSend = new StringBuilder();
                        idToSend.append(userEmail).append("_").append(nextNumber);

                        listener.onComplete(idToSend.toString());

                    }
                });
    }

    /* ****************************** Users Functions ****************************** */

    // SignUp
    public void addNewUser(String email, String password, Model.GetAuthListener listener) {
        myAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        AuthUser user = new AuthUser(email, password);

                        FirebaseDatabase.getInstance(URL).getReference("Users").
                                child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).
                                addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        System.out.println("user registered");
                                        listener.onComplete();
                                    } else {
                                        //TODO: change the print
                                        System.out.println("user not register1");
                                    }

                                });
                    } else {
                        //TODO: change the print
                        System.out.println("user not register2");
                    }
                });
    }

    /* -------------------------------------------------------------------------- */

    // Authentication
    public void UserLogin(String email, String password, Model.UserLoginListener listener) {
        myAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        listener.onComplete(userId);
                    } else {
                        //TODO: change the print
                        System.out.println("User name or password wrong!!");
                    }
                });
    }

    /* -------------------------------------------------------------------------- */

    // User Collection
    public void addUserDetails(User user, Model.AddUserDetailsListener listener) {
        Map<String, Object> json = user.toJson();

        db.collection(User.COLLECTION_NAME)
                .document(user.getEmail())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    /* -------------------------------------------------------------------------- */

    public void getUserByEmail(String email, Model.GetUserByEmailListener listener) {
        db.collection(User.COLLECTION_NAME)
                .document(email)
                .get()
                .addOnCompleteListener(task -> {
                    User user = null;
                    if (task.isSuccessful() & task.getResult() != null) {
                        user = User.create(task.getResult().getData());
                    }
                    listener.onComplete(user);
                });
    }

    /* -------------------------------------------------------------------------- */

    public void getUserPosts(User user, Model.GetUserPostsListener listener) {
        List<Post> list = new LinkedList<Post>();
        if (user.getPostList().size() > 0) {
            db.collection(Post.COLLECTION_NAME)
                    .whereIn(FieldPath.documentId(), user.getPostList())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Post post = Post.create(doc.getData());
                                if (post != null) {
                                    list.add(post);
                                }
                            }
                        }
                        listener.onComplete(list);
                    });
        } else {
            listener.onComplete(list);
        }
    }
}