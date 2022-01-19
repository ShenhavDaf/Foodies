package com.example.foodies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.foodies.model.Model;
import com.example.foodies.model.Post;
import com.example.foodies.model.User;

import java.util.List;

public class EditProfileFragment extends Fragment {

    EditText fullName,email, password, verify, city;
    ImageView img;
    Button saveBtn;

    String currentUserEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        currentUserEmail = EditProfileFragmentArgs.fromBundle(getArguments()).getUserEmail();
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        fullName = view.findViewById(R.id.editprofile_fullname_et);
        city = view.findViewById(R.id.editprofile_city_et);
        saveBtn = view.findViewById(R.id.editprofile_save_btn);

        Model.instance.getUserByEmail(currentUserEmail, user -> {

            fullName.setText(user.getFullName());
            city.setText(user.getCity());

        //TODO            dishImg.setText(post1.getImage());

        });

        saveBtn.setOnClickListener(v -> save(view));


        return view;
    }

    public void save(View view) {

        Model.instance.getUserByEmail(currentUserEmail, user -> {

            String name = fullName.getText().toString();
            String mycity = city.getText().toString();

            List<String> list = user.getPostList();

            //TODO:userID, img
            String img = "myImg";

            User newUser = new User( currentUserEmail, name, mycity, img, list);


            /* ------------------------------------ Navigation ------------------------------------ */

            //TODO: make a function in modelFirebase of updateData (change the addPost to updatePost)


            Model.instance.addUserDetails(newUser, () -> {
                Navigation.findNavController(view)
                        .navigate(EditProfileFragmentDirections
                                .actionGlobalProfileFragment(currentUserEmail));

            });


        });


    }
}