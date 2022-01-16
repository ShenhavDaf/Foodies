package com.example.foodies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.foodies.model.Model;
import com.example.foodies.model.Post;

public class PostPageFragment extends Fragment {

    TextView dishName, restaurant, address, category, description, review;
    RatingBar rate;
    Button editPostBtn;
    String postId, sourcePage, currUserEmail;
    String postEmail;
    Post currentPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         postId = PostPageFragmentArgs.fromBundle(getArguments()).getPostId();
         sourcePage = PostPageFragmentArgs.fromBundle(getArguments()).getSourcePage();
         currUserEmail =  PostPageFragmentArgs.fromBundle(getArguments()).getUserEmail();
        /* ********************************* View Items ********************************* */

        View view = inflater.inflate(R.layout.fragment_post_page, container, false);

        dishName = view.findViewById(R.id.postpage_dishname_tv);
        restaurant = view.findViewById(R.id.postpage_restaurent_tv);
        address = view.findViewById(R.id.postpage_address_tv);
        category = view.findViewById(R.id.postpage_category_tv);
        description = view.findViewById(R.id.postpage_description_tv);
        review = view.findViewById(R.id.postpage_review_tv);
        rate = view.findViewById(R.id.postpage_rate_tv);

        description.setEnabled(false);
        review.setEnabled(false);
        rate.setEnabled(false);

        editPostBtn = view.findViewById(R.id.postpage_editpost_btn);
        editPostBtn.setVisibility(View.GONE); // GONE OR VISIBLE

        Model.instance.getPostById(postId, post -> {
            dishName.setText(post.getDishName());
            restaurant.setText(post.getRestaurant());
            address.setText(post.getAddress());
            category.setText(post.getCategory());
            description.setText(post.getDescription());
            review.setText(post.getReview());
            rate.setRating(Integer.parseInt(post.getRate()));
            postEmail = post.getUserEmail();
            if(postEmail.equals(currUserEmail)){
                editPostBtn.setVisibility(View.VISIBLE);
                editPostBtn.setOnClickListener(v -> editPost(view, postId));
            }// GONE OR VISIBLE
        });



        // TODO: by userId
        // if the user is not the creator to the post, we need to set the visibility to GONE
        System.out.println(currUserEmail);
        System.out.println(postEmail);





        return view;
    }

    /* ********************************* Function/ Navigation ********************************* */

    private void editPost(View view, String postId) {
        Navigation.findNavController(view)
                .navigate(PostPageFragmentDirections.actionPostPageFragmentToEditPostFragment(postId, sourcePage, currUserEmail));

    }
}