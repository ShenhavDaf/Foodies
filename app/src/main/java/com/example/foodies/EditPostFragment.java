package com.example.foodies;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.foodies.model.Model;
import com.example.foodies.model.Post;

public class EditPostFragment extends Fragment {

    TextView dishName, restaurent, address, description, review;
    ImageView dishImg;
    Spinner category, rate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);

        dishName = view.findViewById(R.id.editpost_dishname_et);
        restaurent = view.findViewById(R.id.editpost_restaurant_et);
        address = view.findViewById(R.id.editpost_address_et);
        category = view.findViewById(R.id.editpost_category_spinner);
        description = view.findViewById(R.id.editpost_description_et);
        review = view.findViewById(R.id.editpost_review_et);
        dishImg = view.findViewById(R.id.editpost_dishimg_img);
        rate = view.findViewById(R.id.editpost_rate_spinner);


        String postId = EditPostFragmentArgs.fromBundle(getArguments()).getPostId();

        Post post = Model.instance.getPostById(postId, post1 -> {

            dishName.setText(post1.getDishName());
            restaurent.setText(post1.getRestaurant());
            address.setText(post1.getAddress());

            ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.postCategories, android.R.layout.simple_spinner_item);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category.setAdapter(categoryAdapter);
            if (post1.getCategory() != null) {
                int spinnerPosition = categoryAdapter.getPosition(post1.getCategory());
                category.setSelection(spinnerPosition);
            }

            description.setText(post1.getDescription());
            review.setText(post1.getReview());

//TODO            dishImg.setText(post1.getImage());

            ArrayAdapter<CharSequence> rateAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.postRating, android.R.layout.simple_spinner_item);
            rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rate.setAdapter(rateAdapter);
            if (post1.getCategory() != null) {
                int spinnerPosition = rateAdapter.getPosition(post1.getRate());
                rate.setSelection(spinnerPosition);
            }

        });

        Button saveBtn = view.findViewById(R.id.editpost_save_btn);
        saveBtn.setOnClickListener(v -> save(post, v));

        return view;
    }

    private void save(Post post, View v) {

        //TODO
//        post.setDishName(dishName.getText().toString());
//        post.setRestaurant(restaurent.getText().toString());
//        post.setAddress(address.getText().toString());
////        post.setCategory(category.getText().toString());
//        post.setDescription(description.getText().toString());
//        post.setReview(review.getText().toString());
////        post.setRate(rate.getText().toString());

        Navigation.findNavController(v).navigate(EditPostFragmentDirections.actionEditPostFragmentToHomePage());

    }

}