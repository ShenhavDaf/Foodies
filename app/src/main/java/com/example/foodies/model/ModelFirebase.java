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


    public void addPost(Post post, Model.AddPostListener listener) {
        Map<String, Object> json = post.toJson();

        db.collection(Post.COLLECTION_NAME)
                .document(post.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }


    public void getPostById(String postId, Model.GetPostByIdListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .document(postId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Post post = null;
                        if (task.isSuccessful() & task.getResult() != null) {
                            post = Post.create(task.getResult().getData());
                        }
                        listener.onComplete(post);
                    }
                });
    }


    public void getListSize(Model.GetListSizeListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int size = 0;
                        if (task.isSuccessful()) {
                            size = task.getResult().getDocuments().size();
                        }
                        listener.onComplete(size);
                    }
                });
    }


    public void getNextPostId(Model.GetNextPostIdListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                    }
                });
    }

    public void addNewUser(User user, Model.GetAuthListener listener) {

        myAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

//                    user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            FirebaseDatabase.getInstance("https://foodies-14955-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").
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
                    }
                });

    }

    public void getUserId(String email, String password, Model.GetUserIdListener listener) {

        System.out.println("email1 = "+email);
        System.out.println("password1 = "+password);

        myAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        System.out.println("email2 = "+email);
                        System.out.println("password2 = "+password);

                        if(task.isSuccessful()){

                            System.out.println("email3 = "+email);
                            System.out.println("password3 = "+password);

                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            listener.onComplete(userId);
                        }
                        else {
                            //TODO: change the print
                            System.out.println("user not register from 'getUserId' ");
                        }
                    }
                });

    }
}

