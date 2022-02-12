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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodies.model.AppLocalDB;
import com.example.foodies.model.Model;
import com.example.foodies.model.Post;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

public class EditPostFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    TextView dishName, restaurent, address, description, review;
    ImageView dishImg;
    Spinner category, rate;
    Button saveBtn, deleteBtn;
    ImageButton cameraBtn, galleryBtn;

    String postId, sourcePage, currUserEmail;

    Post currPost;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        currUserEmail = Model.instance.getCurrentUserModel().getEmail();

        postId = EditPostFragmentArgs.fromBundle(getArguments()).getPostId();
        sourcePage = PostPageFragmentArgs.fromBundle(getArguments()).getSourcePage();

        /* *************************************** View Items *************************************** */
        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);

        dishName = view.findViewById(R.id.editpost_dishname_et);
        restaurent = view.findViewById(R.id.editpost_restaurant_et);
        address = view.findViewById(R.id.editpost_address_et);
        category = view.findViewById(R.id.editpost_category_spinner);
        description = view.findViewById(R.id.editpost_description_et);
        review = view.findViewById(R.id.editpost_review_et);
        dishImg = view.findViewById(R.id.editpost_dishimg_img);
        rate = view.findViewById(R.id.editpost_rate_spinner);

        /* *************************************** Current Post *************************************** */

        currPost = Model.instance.getPostByIdLocalDB(postId);


        dishName.setText(currPost.getDishName());
        restaurent.setText(currPost.getRestaurant());
        address.setText(currPost.getAddress());

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter
                .createFromResource(this.getContext(), R.array.postCategories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);
        if (currPost.getCategory() != null) {
            int spinnerPosition = categoryAdapter.getPosition(currPost.getCategory());
            category.setSelection(spinnerPosition);
        }

        description.setText(currPost.getDescription());
        review.setText(currPost.getReview());

        if (currPost.getImage() != null) {
            Picasso.get()
                    .load(currPost.getImage())
                    .into(dishImg);
        }

        ArrayAdapter<CharSequence> rateAdapter = ArrayAdapter
                .createFromResource(this.getContext(), R.array.postRating, android.R.layout.simple_spinner_item);
        rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rate.setAdapter(rateAdapter);
        if (currPost.getCategory() != null) {
            int spinnerPosition = rateAdapter.getPosition(currPost.getRate());
            rate.setSelection(spinnerPosition);
        }


        /*---------------------------------- Button --------------------------------------*/

        saveBtn = view.findViewById(R.id.editpost_save_btn);
        saveBtn.setOnClickListener(v -> save(postId, v));

        deleteBtn = view.findViewById(R.id.editpost_delete_btn);
        deleteBtn.setOnClickListener(v -> delete(postId, v));

        cameraBtn = view.findViewById(R.id.editpost_camera_btn);
        galleryBtn = view.findViewById(R.id.editpost_gallery_btn);

        cameraBtn.setOnClickListener(v -> {
            OpenCamera();
        });

        galleryBtn.setOnClickListener(v -> {
            OpenGallery();
        });


        return view;
    }


    /* *************************************** Functions *************************************** */

    private void OpenGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_PICK);
    }

    private void OpenCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    Bitmap imageBitmap;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                //not in interface
                dishImg.setImageBitmap(imageBitmap);

            }
        } else if (requestCode == REQUEST_IMAGE_PICK) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    //not in interface
                    dishImg.setImageBitmap(imageBitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                    System.out.println("failed to get to the photo");
                }
            }
        }
    }

    private void save(String postID, View v) {

        saveBtn.setEnabled(false);

        String name = dishName.getText().toString();
        String res = restaurent.getText().toString();
        String addr = address.getText().toString();
        String categor = category.getSelectedItem().toString();
        String desc = description.getText().toString();
        String rev = review.getText().toString();
        String rateing = rate.getSelectedItem().toString();

        String img = currPost.getImage();

        Post newPost = new Post(postID, name, res, addr, categor, desc, rev, img, rateing, currUserEmail, true);

        if (imageBitmap != null) {
            Model.instance.setImage(imageBitmap, postID + ".jpg", "/posts_images/", url -> {
                newPost.setImage(url);
                editNavigation(newPost, v);
            });
        } else {
            editNavigation(newPost, v);
        }

    }

    private void editNavigation(Post newPost, View v) {

        Model.instance.editPost(newPost, () -> {
            Model.instance.refreshPostsList();

            if (sourcePage.equals("homepage")) {
                Navigation.findNavController(v)
                        .navigate(EditPostFragmentDirections.actionGlobalHomePage());
            } else if (sourcePage.equals("profilepage")) {
                Navigation.findNavController(v)
                        .navigate(EditPostFragmentDirections.actionGlobalProfileFragment());
            }
        });
    }

    /*-------------------------------------------*/

    private void delete(String postID, View v) {
        System.out.println("********** Delete btn was clicked in editPage");

        Post newPost = Model.instance.getPostByIdLocalDB(postID);

        Model.instance.deletePost(newPost, () -> {
            Model.instance.refreshPostsList();

            List<String> l = Model.instance.getCurrentUserModel().getPostList();
            l.remove(postID);
            Model.instance.getCurrentUserModel().setPostList(l);

            if (sourcePage.equals("homepage")) {

                Navigation.findNavController(v)
                        .navigate(EditPostFragmentDirections.actionGlobalHomePage());
            } else if (sourcePage.equals("profilepage")) {

                Navigation.findNavController(v)
                        .navigate(EditPostFragmentDirections.actionGlobalProfileFragment());
            }

        });

    }


    /* *************************************** Spinner Functions *************************************** */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}