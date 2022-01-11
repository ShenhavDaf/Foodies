package com.example.foodies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.foodies.model.Model;
import com.example.foodies.model.Post;

public class NewPostFragment extends Fragment {

    EditText dishName, restaurent, address, description, review;
    //TODO: categoty, rate
    Button postBtn;
    ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_post, container, false);

        dishName = view.findViewById(R.id.newpost_dishname_et);
        restaurent = view.findViewById(R.id.newpost_restaurant_et);
        address = view.findViewById(R.id.newpost_address_et);
        description = view.findViewById(R.id.newpost_desc_et);
        review = view.findViewById(R.id.newpost_review_et);

        postBtn = view.findViewById(R.id.newpost_post_btn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save("1"); //TODO: userID
            }
        });


        return view;
    }

    private void save(String userID) {
        // save on firebase
        postBtn.setEnabled(false);

        String name = dishName.getText().toString();
        String res = restaurent.getText().toString();
        String addr = address.getText().toString();
        String desc = description.getText().toString();
        String rev = review.getText().toString();

        //TODO
        String categor = "myCategory";
        String img = "myImg";
        String rateing = "3";

        Post newPost = new Post("3", name, res, addr, categor, desc, rev, img, rateing, userID);

        Model.instance.addPost(newPost, () -> {
            Navigation.findNavController(dishName).navigateUp();
        });

    }


}