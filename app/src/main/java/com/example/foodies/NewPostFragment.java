package com.example.foodies;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.foodies.model.Model;
import com.example.foodies.model.Post;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class NewPostFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    EditText dishName, restaurant, address, description, review;
    Spinner category, rate;
    Button postBtn;
    ImageView image;
    ImageButton cameraBtn, galleryBtn;
    String currUserEmail;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* ************* View Items ************* */

        currUserEmail = Model.instance.getCurrentUserModel().getEmail();

        View view = inflater.inflate(R.layout.fragment_new_post, container, false);

        dishName = view.findViewById(R.id.newpost_dishname_et);
        restaurant = view.findViewById(R.id.newpost_restaurant_et);
        address = view.findViewById(R.id.newpost_address_et);
        category = view.findViewById(R.id.newpost_category_spinner);

        // Category spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter
                .createFromResource(this.getContext(), R.array.postCategories,
                        android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);


        description = view.findViewById(R.id.newpost_desc_et);
        review = view.findViewById(R.id.newpost_review_et);
        image = view.findViewById(R.id.newpost_img);
        rate = view.findViewById(R.id.newpost_rate_spinner);

        // Rate spinner
        ArrayAdapter<CharSequence> rateAdapter = ArrayAdapter
                .createFromResource(this.getContext(), R.array.postRating,
                        android.R.layout.simple_spinner_item);
        rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rate.setAdapter(rateAdapter);


        /* ------------------------------------ Button ------------------------------------ */

        postBtn = view.findViewById(R.id.newpost_post_btn);
        cameraBtn = view.findViewById(R.id.newpost_camera_btn);
        galleryBtn = view.findViewById(R.id.newpost_gallery_btn);

        postBtn.setOnClickListener(v -> {
            save();
        });

        cameraBtn.setOnClickListener(v -> {
            OpenCamera();
        });

        galleryBtn.setOnClickListener(v -> {
            OpenGallery();
        });



        return view;
    }


    private void OpenGallery(){

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent,  REQUEST_IMAGE_PICK);
    }

    private void OpenCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    Bitmap imageBitmap;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                //not in interface
                image.setImageBitmap(imageBitmap);

            }
        }
        else if(requestCode == REQUEST_IMAGE_PICK){
            if(resultCode == RESULT_OK){
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    //not in interface
                    image.setImageBitmap(imageBitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Failed", Toast.LENGTH_LONG).show();
                    System.out.println("failed to get to the photo");
                }
            }
        }
    }

    /* ************* Functions ************* */

    private void save() {
        // save on firebase
        postBtn.setEnabled(false);

        String name = dishName.getText().toString();
        String res = restaurant.getText().toString();
        String addr = address.getText().toString();
        String categor = category.getSelectedItem().toString();
        String desc = description.getText().toString();
        String rev = review.getText().toString();
        String rateing = rate.getSelectedItem().toString();

//
//        if (name.isEmpty()) {
//            dishName.setError("Please enter the name of the dish");
//            dishName.requestFocus();
//            return;
//        }
//        if (res.isEmpty()) {
//            restaurant.setError("Please enter the name of the restaurant");
//            restaurant.requestFocus();
//            return;
//        }
//        if (addr.isEmpty()) {
//            address.setError("Please enter restaurant address");
//            address.requestFocus();
//            return;
//        }
//        if (rev.isEmpty()) {
//            review.setError("Please enter restaurant address");
//            review.requestFocus();
//            return;
//        }

        //TODO: category and rating

        //TODO: img, userid



        /* ------------------------------------ Navigation ------------------------------------ */

        Model.instance.getNextPostId(currUserEmail, nextId -> {

            if(imageBitmap != null){

                Model.instance.setImage(imageBitmap, nextId + ".jpg","/posts_images/", url -> {

                    Post newPost = new Post(nextId, name, res, addr, categor, desc, rev, url, rateing, currUserEmail, true);
                    Model.instance.addPost(newPost, currUserEmail, () -> {
//                            Model.instance.refreshPostsList();

                        List<String> l = Model.instance.getCurrentUserModel().getPostList();
                        l.add(newPost.getId());
                        Model.instance.getCurrentUserModel().setPostList(l);

                        //TODO: return to footer caller
                        Navigation.findNavController(dishName).navigateUp();
                    });
                });
            }
            else {


                // TODO: pop-up
            }







        });

    }

    /* ************* Spinner Functions ************* */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}