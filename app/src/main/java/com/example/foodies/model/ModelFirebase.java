package com.example.foodies.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.foodies.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = myAuth.getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings
                .Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    /* ****************************** interfaces ****************************** */

    public interface GetAllPostsListener {
        void onComplete(List<Post> list);
    }

    /*
     *
     *
     */

    /* ****************************** Posts Functions ****************************** */

    public void addPost(Post post, String userEmail, Model.AddPostListener listener) {
        Map<String, Object> json = post.toJson();

        db.collection(Post.COLLECTION_NAME)
                .document(post.getId())
                .set(json)
                .addOnSuccessListener(unused -> {

                    db.collection(User.COLLECTION_NAME)
                            .document(userEmail)
                            .get()
                            .addOnCompleteListener(task -> {

                                if (task.isSuccessful() & task.getResult() != null) {

                                    User myUser = User.create(task.getResult().getData());
                                    myUser.getPostList().add(post.getId());

                                    int counter = Integer.parseInt(task.getResult().getData().get("counter").toString()) + 1;

                                    Map<String, Object> updateJson = myUser.toJson();

                                    updateJson.put("counter", counter);

                                    db.collection(User.COLLECTION_NAME)
                                            .document(userEmail).set(updateJson, SetOptions.merge())
                                            .addOnSuccessListener(command -> listener.onComplete())
                                            .addOnFailureListener(command -> listener.onComplete());
                                }
                            });
                }).addOnFailureListener(e -> listener.onComplete());
    }

    /* -------------------------------------------------------------------------- */

    public void editPost(Post post, Model.EditPostListener listener) {
        Map<String, Object> json = post.toJson();

        db.collection(Post.COLLECTION_NAME)
                .document(post.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    /* -------------------------------------------------------------------------- */

    public void deletePost(Post post, Model.DeletePostListener listener) {

        post.setDisplay(false);
        Map<String, Object> json = post.toJson();

        db.collection(Post.COLLECTION_NAME)
                .document(post.getId())
                .set(json)
                .addOnCompleteListener(command -> {

                    db.collection(User.COLLECTION_NAME)
                            .document(post.getUserEmail())
                            .get().addOnCompleteListener(command1 -> {

                        User myUser = User.create(command1.getResult().getData());
                        myUser.getPostList().remove(post.getId());

                        Map<String, Object> userJson = myUser.toJson();
                        userJson.put("counter", command1.getResult().getData().get("counter"));

                        db.collection(User.COLLECTION_NAME)
                                .document(post.getUserEmail())
                                .set(userJson).addOnCompleteListener(command2 -> listener.onComplete());
                    });
                });
    }

    /* -------------------------------------------------------------------------- */

    public void getAllPosts(Long lastUpdateDate, GetAllPostsListener listener) {

        db.collection(Post.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate, 0))
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

    public void getNextPostId(String userEmail, Model.GetNextPostIdListener listener) {

        db.collection(User.COLLECTION_NAME)
                .document(userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() & task.getResult() != null) {

                        int counter = Integer.parseInt(task.getResult().getData().get("counter").toString()) + 1;
                        StringBuilder idToSend = new StringBuilder();
                        idToSend.append(userEmail).append('_').append(counter);

                        listener.onComplete(idToSend.toString());

                    }
                });
    }

    /*
     *
     *
     */

    /* ****************************** Users Functions ****************************** */

    // SignUp
    public void addNewUser(User user, String email, String password, Model.GetAuthListener listener) {

        myAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Map<String, Object> json = user.toJson();
                        json.put("counter", 0);

                        db.collection(User.COLLECTION_NAME)
                                .document(user.getEmail())
                                .set(json)
                                .addOnSuccessListener(success -> listener.onComplete(true))
                                .addOnFailureListener(failure -> {
                                    Toast.makeText(MyApplication.getContext(), "Internet connection problem, please try again later", Toast.LENGTH_SHORT).show();
                                });
                    }
                    else{
                        listener.onComplete(false);
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
                        listener.onComplete(null);
                    }
                });
    }

    /* -------------------------------------------------------------------------- */

    // User Collection
    public void addUserDetails(User user, Model.AddUserDetailsListener listener) {
        Map<String, Object> json = user.toJson();

        db.collection(User.COLLECTION_NAME).document(user.getEmail())
                .get().addOnCompleteListener(task -> {

            if (task.getResult().getData().get("counter") != null) {
                json.put("counter", task.getResult().getData().get("counter"));
            } else {
                json.put("counter", 0);
            }

            db.collection(User.COLLECTION_NAME)
                    .document(user.getEmail())
                    .set(json)
                    .addOnSuccessListener(unused -> listener.onComplete())
                    .addOnFailureListener(e -> listener.onComplete());

        });
    }

    /* -------------------------------------------------------------------------- */

    public boolean isSignedIn() {
        return (currentUser != null);
    }

    /* -------------------------------------------------------------------------- */

    public String getLastUserEmail() {
        return currentUser.getEmail();
    }

    /* -------------------------------------------------------------------------- */

    public void UserLogout() {
        myAuth.signOut();
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

    /*
     *
     *
     */

    /*************************************** FirebaseStorage **************************************************/


    public void saveImage(Bitmap imageBitmap, String imageName, String storageName, Model.SaveImageListener listener) {

        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child(storageName + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnSuccessListener(taskSnapshot -> {
                    imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    });
                });
    }


}