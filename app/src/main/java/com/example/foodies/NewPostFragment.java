package com.example.foodies;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    ProgressBar progressBar;
    Bitmap imageBitmap;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        currUserEmail = Model.instance.getCurrentUserModel().getEmail();

        /* ********************************* View Items ********************************* */

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

        progressBar = view.findViewById(R.id.newpost_progressBar);
        progressBar.setVisibility(View.GONE);

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

    private void OpenGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_PICK);
    }

    private void OpenCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                image.setImageBitmap(imageBitmap);

            }
        } else if (requestCode == REQUEST_IMAGE_PICK) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    image.setImageBitmap(imageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                    System.out.println("failed to get to the photo");
                }
            }
        }
    }

    /* ********************************* Functions ********************************* */

    private void myNavigation(Post newPost) {
        Model.instance.addPost(newPost, currUserEmail, (isSuccess) -> {

            if(isSuccess) {
                List<String> l = Model.instance.getCurrentUserModel().getPostList();
                l.add(newPost.getId());
                Model.instance.getCurrentUserModel().setPostList(l);
                Navigation.findNavController(dishName).navigateUp();
            }
            else{
                progressBar.setVisibility(View.GONE);
                postBtn.setEnabled(true);
                Toast.makeText(MyApplication.getContext(), "Internet connection problem, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void save() {
        String name = dishName.getText().toString();
        String res = restaurant.getText().toString();
        String addr = address.getText().toString();
        String categor = category.getSelectedItem().toString();
        String desc = description.getText().toString();
        String rev = review.getText().toString();
        String rateing = rate.getSelectedItem().toString();
        String img = "myImg";


        if (name.isEmpty()) {
            dishName.setError("Please enter the name of the dish");
            dishName.requestFocus();
            return;
        }
        if (res.isEmpty()) {
            restaurant.setError("Please enter the name of the restaurant");
            restaurant.requestFocus();
            return;
        }
        if (addr.isEmpty()) {
            address.setError("Please enter restaurant address");
            address.requestFocus();
            return;
        }
        if (desc.isEmpty()) {
            description.setError("Please enter dish description");
            description.requestFocus();
            return;
        }
        if (rev.isEmpty()) {
            review.setError("Please enter your review");
            review.requestFocus();
            return;
        }

        postBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        /* ------------------------------------ Navigation ------------------------------------ */

        Model.instance.getNextPostId(currUserEmail, nextId -> {

            Post newPost = new Post(nextId, name, res, addr, categor, desc, rev, img, rateing, currUserEmail, true);
            if (imageBitmap != null) {

                Model.instance.setImage(imageBitmap, nextId + ".jpg", "/posts_images/", url -> {
                    newPost.setImage(url);
                    myNavigation(newPost);
                });
            } else {
                myNavigation(newPost);
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