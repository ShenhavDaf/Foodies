package com.example.foodies.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


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
                        if(task.isSuccessful()){
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
                        if(task.isSuccessful()){
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

}

