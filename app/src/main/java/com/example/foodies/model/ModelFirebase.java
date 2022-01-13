package com.example.foodies.model;

import android.content.Context;
import android.net.wifi.hotspot2.pps.Credential;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    final public static String URL = "https://foodies-14955-default-rtdb.europe-west1.firebasedatabase.app";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth myAuth = FirebaseAuth.getInstance();



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

    public void addPost(Post post, Model.AddPostListener listener) {
        Map<String, Object> json = post.toJson();

        db.collection(Post.COLLECTION_NAME)
                .document(post.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
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

    public void getListSize(Model.GetListSizeListener listener) {
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

    public void getNextPostId(Model.GetNextPostIdListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    String lastId = "";
                    if (task.isSuccessful()) {
                        int size = task.getResult().getDocuments().size() - 1;
                        lastId = task.getResult().getDocuments().get(size).getId();
                        int i = Integer.parseInt(lastId);
                        i++;
                        StringBuilder builder = new StringBuilder();
                        builder.append(i);
                        lastId = builder.toString();
                    }
                    listener.onComplete(lastId);
                });
    }

    /* -------------------------------------------------------------------------- */

//    public void addNewUser(User user, Model.GetAuthListener listener) {
    public void addNewUser(String email, String password, Model.GetAuthListener listener) {


    myAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        AuthUser user = new AuthUser(email, password);

                        FirebaseDatabase.getInstance(URL).getReference("Users").
                                child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    System.out.println("user registered");
                                    listener.onComplete();
                                } else {
                                    //TODO: change the print
                                    System.out.println("user not register1");
                                }

                            }
                        });
                    } else {
                        //TODO: change the print
                        System.out.println("user not register2");
                    }
                });

    }

    /* -------------------------------------------------------------------------- */

    public void getUserId(String email, String password, Model.GetUserIdListener listener) {

        myAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        listener.onComplete(userId);
                    } else {
                        //TODO: change the print
                        System.out.println("user not register from 'getUserId' ");
                    }
                });
    }

    /* -------------------------------------------------------------------------- */


//    public void addUser(User user, Model.AddUserListener listener) {
//        Map<String, Object> json = user.toJson();
//
//        db.collection(User.COLLECTION_NAME)
//                .document(user.getEmail())
//                .set(json)
//                .addOnSuccessListener(unused -> listener.onComplete())
//                .addOnFailureListener(e -> listener.onComplete());
//    }
}