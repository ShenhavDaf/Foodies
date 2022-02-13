package com.example.foodies.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

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

    final public static String URL = "https://foodies-14955-default-rtdb.europe-west1.firebasedatabase.app";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth myAuth = FirebaseAuth.getInstance();

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

                    System.out.println("Addition new post to firebase was success");

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

    public void editPost(Post post, Model.EditPostListener listener) {
        Map<String, Object> json = post.toJson();

        db.collection(Post.COLLECTION_NAME)
                .document(post.getId())
                .set(json)
                .addOnSuccessListener(unused -> {
                    System.out.println("Addition new post to firebase was success");
                    listener.onComplete();

                })

                .addOnFailureListener(e -> {
                    System.out.println("Addition new post to firebase was failed");
                    listener.onComplete();
                });
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
                                .set(userJson).addOnCompleteListener(command2 -> {
                            listener.onComplete();
                        });

                    });
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

        //realtime
        myAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Log.d("TAG", "create complete");
                        System.out.println("create complete");

                        Map<String, Object> json = user.toJson();
                        json.put("counter", 0);

                        db.collection(User.COLLECTION_NAME)
                                .document(user.getEmail())
                                .set(json)
                                .addOnSuccessListener(success -> listener.onComplete())
                                .addOnFailureListener(failure -> {
                                    System.out.println("failed to inside the user to userCollection");
                                    //TODO:
//                                            listener.onComplete();
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

        //check if user exist in system
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

        db.collection(User.COLLECTION_NAME).document(user.getEmail())
                .get().addOnCompleteListener(task -> {

            if (task.getResult().getData().get("counter") != null) {
                json.put("counter", task.getResult().getData().get("counter"));
            } else {
                json.put("counter", 0);
            }

            System.out.println("print from firebase ========= " + json.get("counter"));

            db.collection(User.COLLECTION_NAME)
                    .document(user.getEmail())
                    .set(json)
                    .addOnSuccessListener(unused -> {
                        listener.onComplete();
                    })
                    .addOnFailureListener(e -> listener.onComplete());

        });

    }

    /* -------------------------------------------------------------------------- */


    FirebaseUser currentUser = myAuth.getCurrentUser();

    public boolean isSignedIn() {
//        System.out.println("===================== currentUser = " + currentUser.getEmail());
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

    /* -------------------------------------------------------------------------- */

    //TODO: lastUpdateDate, whereGreaterThanOrEqualTo
    public void getUserPosts(User user, Model.GetUserPostsListener listener) {
        List<Post> list = new LinkedList<Post>();
        if (user.getPostList().size() > 0) {
            db.collection(Post.COLLECTION_NAME)
//                    .whereArrayContainsAny(FieldPath.documentId(), user.getPostList())
//                    .whereIn(FieldPath.documentId(), user.getPostList())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Post post = Post.create(doc.getData());
                                if (post != null) {
                                    if (user.getPostList().contains(post.getId())) {
                                        list.add(post);
                                    }
                                }
                            }
                        }
                        listener.onComplete(list);
                    });
        } else {
            listener.onComplete(list);
        }
    }

    /*
     *
     *
     */

    /*************************************** FirebaseStorage **************************************************/

    FirebaseStorage storage = FirebaseStorage.getInstance();

    public void saveImage(Bitmap imageBitmap, String imageName, String storageName, Model.SaveImageListener listener) {

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child(storageName + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            listener.onComplete(null);
        }).addOnSuccessListener(taskSnapshot -> {
            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Uri downloadUrl = uri;
                listener.onComplete(downloadUrl.toString());
            });

        });
    }
}