package com.example.foodies.login;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.foodies.MainActivity;
import com.example.foodies.MyApplication;
import com.example.foodies.R;
import com.example.foodies.model.Model;
import com.example.foodies.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SignUpFragment extends Fragment {

    Button joinBtn;
    EditText fullNameEt, emailEt, passwordEt, verifyEt, cityEt;
    ImageButton galleryBtn, cameraBtn;
    ImageView image;
    ProgressBar progressBar;
    Bitmap imageBitmap;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;


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

        joinBtn.setOnClickListener(v -> Join());

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
                    Toast.makeText(getContext(), "Error uploading image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();
            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            addPicureToGallery(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPicureToGallery(File imageFile){
        //add the picture to the gallery so we dont need to manage the cache size
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        MyApplication.getContext().sendBroadcast(mediaScanIntent);
    }

    private String getLocalImageFileName(String url) {
        String name = URLUtil.guessFileName(url, null, null);
        return name;
    }

    /* ************************************** Function ************************************** */

    private void myNavigation(User newUserDetails, String email, String password) {
        Model.instance.addNewUser(newUserDetails, email, password, (isSuccess) -> {

            if (isSuccess == true) {
                String msg = "Welcome to Foodies ♥";
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

                AlertDialog alert = builder.create();
                alert.setTitle("\n" + msg + "\n");
                alert.show();
                Model.instance.setCurrentUserModel(newUserDetails);
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            } else {

                String msg = "Email is already exist in our system. \n Please enter another email";
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setNegativeButton("OK", (dialog, which) -> dialog.cancel());

                AlertDialog alert = builder.create();
                alert.setTitle("\n" + msg + "\n");
                alert.show();

                progressBar.setVisibility(View.GONE);
                joinBtn.setEnabled(true);

            }
        });
    }


    private void Join() {
        String fullname = fullNameEt.getText().toString();
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String verify = verifyEt.getText().toString();
        String city = cityEt.getText().toString();
        String image = "myImg";

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
                saveImageToFile(imageBitmap, getLocalImageFileName(url));
                myNavigation(newUserDetails, email, password);
            });
        } else {
            myNavigation(newUserDetails, email, password);
        }
    }
}