package com.example.foodies;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodies.model.AppLocalDB;
import com.example.foodies.model.Model;
import com.example.foodies.model.Post;

public class EditPostFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    TextView dishName, restaurent, address, description, review;
    ImageView dishImg;
    Spinner category, rate;
    Button saveBtn, deleteBtn;

    String postId, sourcePage, currUserEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);

        postId = EditPostFragmentArgs.fromBundle(getArguments()).getPostId();
        sourcePage = PostPageFragmentArgs.fromBundle(getArguments()).getSourcePage();
        currUserEmail = PostPageFragmentArgs.fromBundle(getArguments()).getUserEmail();


        /* *************************************** View Items *************************************** */
        dishName = view.findViewById(R.id.editpost_dishname_et);
        restaurent = view.findViewById(R.id.editpost_restaurant_et);
        address = view.findViewById(R.id.editpost_address_et);
        category = view.findViewById(R.id.editpost_category_spinner);
        description = view.findViewById(R.id.editpost_description_et);
        review = view.findViewById(R.id.editpost_review_et);
        dishImg = view.findViewById(R.id.editpost_dishimg_img);
        rate = view.findViewById(R.id.editpost_rate_spinner);

        /* *************************************** Current Post *************************************** */

        Model.instance.getPostById(postId, post1 -> {

            dishName.setText(post1.getDishName());
            restaurent.setText(post1.getRestaurant());
            address.setText(post1.getAddress());

            ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter
                    .createFromResource(this.getContext(), R.array.postCategories, android.R.layout.simple_spinner_item);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category.setAdapter(categoryAdapter);
            if (post1.getCategory() != null) {
                int spinnerPosition = categoryAdapter.getPosition(post1.getCategory());
                category.setSelection(spinnerPosition);
            }

            description.setText(post1.getDescription());
            review.setText(post1.getReview());

//TODO            dishImg.setText(post1.getImage());

            ArrayAdapter<CharSequence> rateAdapter = ArrayAdapter
                    .createFromResource(this.getContext(), R.array.postRating, android.R.layout.simple_spinner_item);
            rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rate.setAdapter(rateAdapter);
            if (post1.getCategory() != null) {
                int spinnerPosition = rateAdapter.getPosition(post1.getRate());
                rate.setSelection(spinnerPosition);
            }

        });

        /*---------------------------------- Button --------------------------------------*/

        saveBtn = view.findViewById(R.id.editpost_save_btn);
        saveBtn.setOnClickListener(v -> save(postId, v));


        deleteBtn = view.findViewById(R.id.editpost_delete_btn);
        deleteBtn.setOnClickListener(v -> delete(postId, v));

        return view;
    }


    /* *************************************** Functions *************************************** */

    private void save(String postID, View v) {
        Model.instance.getPostById(postID, post1 -> {

            String name = dishName.getText().toString();
            String res = restaurent.getText().toString();
            String addr = address.getText().toString();
            String categor = category.getSelectedItem().toString();
            String desc = description.getText().toString();
            String rev = review.getText().toString();
            String rateing = rate.getSelectedItem().toString();

            //TODO:userID, img
            String img = "myImg";

            Post newPost = new Post(postID, name, res, addr, categor, desc, rev, img, rateing, currUserEmail, true);

            /* ------------------------------------ Navigation ------------------------------------ */

            //TODO: make a function in modelFirebase of updateData (change the addPost to updatePost)

            Model.instance.editPost(newPost, () -> {
                Model.instance.refreshPostsList();

                if (sourcePage.equals("homepage")) {
                    Navigation.findNavController(v)
                            .navigate(EditPostFragmentDirections.actionGlobalHomePage(currUserEmail));
                } else if (sourcePage.equals("profilepage")) {
                    Navigation.findNavController(v)
                            .navigate(EditPostFragmentDirections.actionGlobalProfileFragment(currUserEmail));
                }
            });

//
//            Model.instance.addPost(newPost, currUserEmail, () -> {
//
//                Model.instance.refreshPostsList();
//
//                if (sourcePage.equals("homepage")) {
//                    Navigation.findNavController(v)
//                            .navigate(EditPostFragmentDirections.actionGlobalHomePage(currUserEmail));
//                } else if (sourcePage.equals("profilepage")) {
//                    Navigation.findNavController(v)
//                            .navigate(EditPostFragmentDirections.actionGlobalProfileFragment(currUserEmail));
//                }
//
//            });


//            if (sourcePage.equals("homepage")) {
//                Model.instance.addPost(newPost, currUserEmail, () -> {
//                    Navigation.findNavController(v)
//                            .navigate(EditPostFragmentDirections.actionGlobalHomePage(currUserEmail));
//
//                });
//            }
//            if (sourcePage.equals("profilepage")) {
//                Model.instance.addPost(newPost, currUserEmail, () -> {
//                    Navigation.findNavController(v)
//                            .navigate(EditPostFragmentDirections
//                                    .actionGlobalProfileFragment(currUserEmail));
//
//                });
//            }
        });

    }

    /*-------------------------------------------*/

    private void delete(String postID, View v) {
        System.out.println("********** Delete btn was clicked in editPage");

        Model.instance.getPostById(postID, post1 -> {

                    String name = dishName.getText().toString();
                    String res = restaurent.getText().toString();
                    String addr = address.getText().toString();
                    String categor = category.getSelectedItem().toString();
                    String desc = description.getText().toString();
                    String rev = review.getText().toString();
                    String rateing = rate.getSelectedItem().toString();

                    //TODO:userID, img
                    String img = "myImg";

                    Post newPost = new Post(postID, name, res, addr, categor, desc, rev, img, rateing, currUserEmail, false);

                    Model.instance.addPost(newPost, currUserEmail, () -> {

                        Model.instance.refreshPostsList();

                        Navigation.findNavController(v)
                                .navigate(EditPostFragmentDirections.actionGlobalHomePage(currUserEmail));
                    });


//        Model.instance.deletePostById(postId, () -> {
//            Navigation.findNavController(v)
//                    .navigate(EditPostFragmentDirections.actionGlobalHomePage(currUserEmail));
//        });

                });
    }

                /* *************************************** Spinner Functions *************************************** */
        @Override
        public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
            String item = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), item, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected (AdapterView < ? > parent){

        }
    }