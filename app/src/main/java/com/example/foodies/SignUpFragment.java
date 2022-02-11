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
import android.util.Patterns;
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
import com.example.foodies.model.Post;
import com.example.foodies.model.User;

import java.io.InputStream;


public class SignUpFragment extends Fragment {

    Button joinBtn;
    EditText fullNameEt, emailEt, passwordEt, verifyEt, cityEt, imageEt;
    ImageButton galleryBtn, cameraBtn;
    ImageView image;
    ProgressBar progressBar;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;


    // TODO: add image

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* ********************************** View Items ********************************** */
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        fullNameEt = view.findViewById(R.id.signin_name_et);
        emailEt = view.findViewById(R.id.signin_email_et);
        passwordEt = view.findViewById(R.id.signin_password_et);
        verifyEt = view.findViewById(R.id.signin_verify_et);
        cityEt = view.findViewById(R.id.signin_city_et);
        image = view.findViewById(R.id.signin_image_img);
        progressBar = view.findViewById(R.id.signin_progressBar);
        progressBar.setVisibility(View.GONE);

        galleryBtn = view.findViewById(R.id.signin_gallery_btn);
        cameraBtn = view.findViewById(R.id.signin_camera_btn);
        joinBtn = view.findViewById(R.id.signin_join_btn);

        cameraBtn.setOnClickListener(v -> {
            OpenCamera();
        });

        galleryBtn.setOnClickListener(v -> {
            OpenGallery();
        });

        joinBtn.setOnClickListener(v -> Join(view));

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

    Bitmap imageBitmap;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                //not in interface
                image.setImageBitmap(imageBitmap);

            }
        } else if (requestCode == REQUEST_IMAGE_PICK) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    //not in interface
                    image.setImageBitmap(imageBitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                    System.out.println("failed to get to the photo");
                }
            }
        }
    }

    /* ************************************** Function ************************************** */

    private void myNavigation(View view, User newUserDetails, String email, String password) {
        Model.instance.addNewUser(newUserDetails, email, password, () -> {
            Model.instance.setCurrentUserModel(newUserDetails);
            Navigation.findNavController(view)
                    .navigate(SignUpFragmentDirections.actionGlobalHomePage());
        });
    }


    private void Join(View view) {
        System.out.println("join button was clicked");

        String fullname = fullNameEt.getText().toString();
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String verify = verifyEt.getText().toString();
        String city = cityEt.getText().toString();
//       TODO: String image = imageEt.getText().toString();
        String image = null;

        /* ********************************** Validations ********************************** */

        if (fullname.isEmpty()) {
            fullNameEt.setError("Please enter your full name");
            fullNameEt.requestFocus();
            return;
        }

        if (city.isEmpty()) {
            cityEt.setError("Please enter your city");
            cityEt.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("Please provide valid email");
            emailEt.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            emailEt.setError("Please enter your Email");
            emailEt.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEt.setError("Password length should be at least 6 characters");
            passwordEt.requestFocus();
            return;
        }
        if (!verify.equals(password)) {
            verifyEt.setError("Wrong password");
            verifyEt.requestFocus();
            return;
        }


        joinBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        /* ------------------------------------ Navigation ------------------------------------ */

        User newUserDetails = new User(email, fullname, city, image);

        if (imageBitmap != null) {
            Model.instance.setImage(imageBitmap, email + ".jpg", "/users_avatars/", url -> {
                newUserDetails.setImage(url);
                myNavigation(view, newUserDetails, email, password);
            });
        } else {
            myNavigation(view, newUserDetails, email, password);
        }
    }
}