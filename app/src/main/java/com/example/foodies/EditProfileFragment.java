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

    EditText fullName, city;
    ImageView img;
    Button saveBtn;

    User currUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        currUser = Model.instance.getCurrentUserModel();

        /**************************************/

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        fullName = view.findViewById(R.id.editprofile_fullname_et);
        city = view.findViewById(R.id.editprofile_city_et);
        saveBtn = view.findViewById(R.id.editprofile_save_btn);


        fullName.setText(currUser.getFullName());
        city.setText(currUser.getCity());

        //TODO            dishImg.setText(currUser.getImg);


        saveBtn.setOnClickListener(v -> save(view));


        return view;
    }

    public void save(View view) {

        String name = fullName.getText().toString();
        String mycity = city.getText().toString();

        //TODO:userID, img
        String img = "myImg";

        List<String> list = currUser.getPostList();

        User newUser = new User(currUser.getEmail(), name, mycity, img, list);

        /* ------------------------------------ Navigation ------------------------------------ */

        Model.instance.addUserDetails(newUser, () -> {
            Navigation.findNavController(view)
                    .navigate(EditProfileFragmentDirections.actionGlobalProfileFragment());
        });
    }
}