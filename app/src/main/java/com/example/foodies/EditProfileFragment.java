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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.foodies.model.Model;
import com.example.foodies.model.User;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

public class EditProfileFragment extends Fragment {

    EditText fullName, city;
    Button saveBtn;
    ImageView profileImage;
    ImageButton galleryBtn, cameraBtn;
    ProgressBar progressBar;
    Bitmap imageBitmap;

    User currUser;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        currUser = Model.instance.getCurrentUserModel();

        /* ********************************* View Items ********************************* */

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        fullName = view.findViewById(R.id.editprofile_fullname_et);
        city = view.findViewById(R.id.editprofile_city_et);
        profileImage = view.findViewById(R.id.editprofile_img);
        saveBtn = view.findViewById(R.id.editprofile_save_btn);
        galleryBtn = view.findViewById(R.id.editprofile_gallery_btn);
        cameraBtn = view.findViewById(R.id.editprofile_camera_btn);
        progressBar = view.findViewById(R.id.editprofile_progressBar);
        progressBar.setVisibility(View.GONE);

        fullName.setText(currUser.getFullName());
        city.setText(currUser.getCity());

        if (!currUser.getImage().equals("myImg")) {
            Picasso.get()
                    .load(currUser.getImage())
                    .into(profileImage);
        }

        saveBtn.setOnClickListener(v -> save(view));

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

    /* ***************************** Image Upload ***************************** */

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                profileImage.setImageBitmap(imageBitmap);

            }
        } else if (requestCode == REQUEST_IMAGE_PICK) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    profileImage.setImageBitmap(imageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                    System.out.println("failed to get to the photo");
                }
            }
        }
    }

    /* ******************************************************************** */

    public void save(View view) {
        progressBar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);

        String name = fullName.getText().toString();
        String mycity = city.getText().toString();
        String img = currUser.getImage();

        List<String> list = currUser.getPostList();
        User newUser = new User(currUser.getEmail(), name, mycity, img, list);

        if (imageBitmap != null) {
            Model.instance.setImage(imageBitmap, currUser.getEmail() + ".jpg", "/users_avatars/", url -> {
                newUser.setImage(url);
                changeUserAndNavigate(newUser, view);
            });
        } else {
            changeUserAndNavigate(newUser, view);
        }
    }

    public void changeUserAndNavigate(User newUser, View view) {
        Model.instance.addUserDetails(newUser, (isSuccess) -> {

            if (isSuccess) {
                Model.instance.getCurrentUserModel().setFullName(newUser.getFullName());
                Model.instance.getCurrentUserModel().setCity(newUser.getCity());
                Model.instance.getCurrentUserModel().setImage(newUser.getImage());
                Navigation.findNavController(view)
                        .navigate(EditProfileFragmentDirections.actionGlobalProfileFragment());
            } else {
                progressBar.setVisibility(View.GONE);
                saveBtn.setEnabled(true);
                Toast.makeText(MyApplication.getContext(), "Internet connection problem, please try again later", Toast.LENGTH_SHORT).show();
            }

        });
    }
}