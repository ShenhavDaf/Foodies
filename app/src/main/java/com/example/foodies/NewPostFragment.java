package com.example.foodies;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.foodies.model.Model;
import com.example.foodies.model.Post;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NewPostFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    List<Post> data;
    EditText dishName, restaurent, address, description, review;
    Spinner category, rate;
    Button postBtn;
    ImageView image;
    String currUserEmail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* *************************************** View Items *************************************** */

        currUserEmail = NewPostFragmentArgs.fromBundle(getArguments()).getUserEmail();

        View view = inflater.inflate(R.layout.fragment_new_post, container, false);

        dishName = view.findViewById(R.id.newpost_dishname_et);
        restaurent = view.findViewById(R.id.newpost_restaurant_et);
        address = view.findViewById(R.id.newpost_address_et);
        category = view.findViewById(R.id.newpost_category_spinner);

        // Category spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter
                .createFromResource(this.getContext(),R.array.postCategories,
                        android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);


        description = view.findViewById(R.id.newpost_desc_et);
        review = view.findViewById(R.id.newpost_review_et);
        image = view.findViewById(R.id.newpost_img);
        rate = view.findViewById(R.id.newpost_rate_spinner);

        // Rate spinner
        ArrayAdapter<CharSequence> rateAdapter = ArrayAdapter
                .createFromResource(this.getContext(),R.array.postRating,
                        android.R.layout.simple_spinner_item);
        rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rate.setAdapter(rateAdapter);


        /* ------------------------------------ Button ------------------------------------ */

        postBtn = view.findViewById(R.id.newpost_post_btn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(); //TODO: userID
            }
        });

        return view;
    }

    /* *************************************** Functions *************************************** */

    private void save() {
        // save on firebase
        postBtn.setEnabled(false);

        String name = dishName.getText().toString();
        String res = restaurent.getText().toString();
        String addr = address.getText().toString();
        String categor = category.getSelectedItem().toString();
        String desc = description.getText().toString();
        String rev = review.getText().toString();
        String rateing = rate.getSelectedItem().toString();;

        //TODO: img, userid
        String img = "myImg";

        /* ------------------------------------ Navigation ------------------------------------ */

        Model.instance.getNextPostId(currUserEmail, nextId -> {
            Post newPost = new Post(nextId + "", name, res, addr, categor, desc, rev, img, rateing, currUserEmail);
            Model.instance.addPost(newPost,currUserEmail, () -> {
                Navigation.findNavController(dishName).navigateUp();
            });
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