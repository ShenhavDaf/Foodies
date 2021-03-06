package com.example.foodies.post;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.foodies.R;
import com.example.foodies.home.HomePageFragmentDirections;
import com.example.foodies.model.Model;
import com.example.foodies.model.Post;
import com.squareup.picasso.Picasso;

public class PostPageFragment extends Fragment {

    TextView dishName, restaurant, address, category, description, review;
    RatingBar rate;
    ImageButton editPostBtn;
    ImageView dishImage;
    String postId, sourcePage, authorEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        postId = PostPageFragmentArgs.fromBundle(getArguments()).getPostId();
        sourcePage = PostPageFragmentArgs.fromBundle(getArguments()).getSourcePage();

        /* ********************************* View Items ********************************* */

        View view = inflater.inflate(R.layout.fragment_post_page, container, false);

        dishName = view.findViewById(R.id.postpage_dishname_tv);
        restaurant = view.findViewById(R.id.postpage_restaurent_tv);
        address = view.findViewById(R.id.postpage_address_tv);
        category = view.findViewById(R.id.postpage_category_tv);
        description = view.findViewById(R.id.postpage_description_tv);
        review = view.findViewById(R.id.postpage_review_tv);
        rate = view.findViewById(R.id.postpage_rate_tv);
        dishImage = view.findViewById(R.id.postpage_dishimage_imv);

        dishName.setEnabled(false);
        restaurant.setEnabled(false);
        address.setEnabled(false);
        category.setEnabled(false);
        description.setEnabled(false);
        review.setEnabled(false);
        rate.setEnabled(false);

        editPostBtn = view.findViewById(R.id.postpage_editpost_btn);
        editPostBtn.setVisibility(View.GONE);

        Post post = Model.instance.getPostByIdLocalDB(postId);

        if (post.getImage().equals("myImg")) {
            Picasso.get()
                    .load("https://t3.ftcdn.net/jpg/04/34/72/82/240_F_434728286_OWQQvAFoXZLdGHlObozsolNeuSxhpr84.jpg")
                    .into(dishImage);
        } else {
            Picasso.get()
                    .load(post.getImage())
                    .into(dishImage);
        }

        dishName.setText(post.getDishName());
        restaurant.setText(post.getRestaurant());
        address.setText(post.getAddress());
        category.setText(post.getCategory());
        description.setText(post.getDescription());
        review.setText(post.getReview());
        rate.setRating(Integer.parseInt(post.getRate()));

        authorEmail = post.getUserEmail();
        if (authorEmail.equals(Model.instance.getCurrentUserModel().getEmail())) {
            editPostBtn.setVisibility(View.VISIBLE);
            editPostBtn.setOnClickListener(v -> editPost(view, postId));
        }

        setHasOptionsMenu(true);
        return view;
    }

    /* ********************************* Function/ Navigation ********************************* */

    private void editPost(View view, String postId) {
        Navigation.findNavController(view)
                .navigate(PostPageFragmentDirections
                        .actionPostPageFragmentToEditPostFragment(postId, sourcePage));
    }


    /* ********************************* Function/ Menu ********************************* */


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.bottom_nav_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.NewPostFragment) {
            NavHostFragment.findNavController(this).navigate(HomePageFragmentDirections.actionGlobalNewPostFragment());
            return true;
        }
        if (item.getItemId() == R.id.ProfileFragment) {
            NavHostFragment.findNavController(this).navigate(HomePageFragmentDirections.actionGlobalProfileFragment());
            return true;
        } else if (item.getItemId() == R.id.HomePageFragment) {
            NavHostFragment.findNavController(this).navigate(HomePageFragmentDirections.actionGlobalHomePage());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}