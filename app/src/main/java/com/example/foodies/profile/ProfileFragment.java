package com.example.foodies.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.foodies.R;
import com.example.foodies.home.HomePageFragmentDirections;
import com.example.foodies.model.Model;
import com.example.foodies.model.Post;
import com.example.foodies.model.User;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private final static String SOURCE_PAGE = "profilepage";
    ProfileViewModel viewModel;
    MyAdapter adapter;

    ImageButton editProfileBtn;
    TextView fullNameTv, cityTv;
    ImageView image;

    User currUserFromModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        currUserFromModel = Model.instance.getCurrentUserModel();

        /* ********************************** View Items ********************************** */

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fullNameTv = view.findViewById(R.id.profile_fullname_tv);
        cityTv = view.findViewById(R.id.profile_city_tv);
        image = view.findViewById(R.id.profile_img);

        if (!currUserFromModel.getImage().equals("myImg")) {
            Picasso.get()
                    .load(currUserFromModel.getImage())
                    .into(image);
        }

        editProfileBtn = view.findViewById(R.id.profile_editprofile_btn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile(v);
            }
        });

        if (viewModel.getData().getValue() != null) {
            for (int i = 0; i < viewModel.getData().getValue().size(); i++) {
                System.out.println(viewModel.getData().getValue().get(i).getDishName());
            }
        }

        fullNameTv.setText(Model.instance.getCurrentUserModel().getFullName());
        cityTv.setText(Model.instance.getCurrentUserModel().getCity());

        /* ***************************** Post List - Recycler View ***************************** */

        RecyclerView list = view.findViewById(R.id.profile_postslist_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String postId = viewModel.getData().getValue().get(position).getId();
                Navigation.findNavController(v)
                        .navigate(ProfileFragmentDirections
                                .actionProfileFragmentToEditPostFragment(postId, SOURCE_PAGE));
            }
        });

        setHasOptionsMenu(true);
        viewModel.getData().observe(getViewLifecycleOwner(), posts -> refresh());

        return view;
    }

    /* ********************************* Function/ Navigation ********************************* */

    private void editProfile(View view) {
        Navigation.findNavController(view)
                .navigate(ProfileFragmentDirections
                        .actionProfileFragmentToEditProfileFragment());
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    /* ********************************* My View Holder ********************************* */

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dishName, description;
        RatingBar rateStar;
        ImageView userImage, dishImage;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            userImage = itemView.findViewById(R.id.listrow_avatar_imv);
            dishName = itemView.findViewById(R.id.listrow_username_tv);
            description = itemView.findViewById(R.id.listrow_description_tv);
            dishImage = itemView.findViewById(R.id.listrow_post_img);
            rateStar = itemView.findViewById(R.id.listrow_ratingBar);
            rateStar.setClickable(false);
            rateStar.setEnabled(false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v, pos);
                }
            });
        }

        public void myBind(Post post) {

            if (Model.instance.getCurrentUserModel().getImage() != "myImg") {
                Picasso.get()
                        .load(Model.instance.getCurrentUserModel().getImage())
                        .into(userImage);
            }

            dishName.setText(post.getDishName());
            description.setText(post.getDescription());

            if (post.getImage().equals("myImg")) {
                Picasso.get()
                        .load("https://t3.ftcdn.net/jpg/04/34/72/82/240_F_434728286_OWQQvAFoXZLdGHlObozsolNeuSxhpr84.jpg")
                        .into(dishImage);
            } else {
                Picasso.get()
                        .load(post.getImage())
                        .into(dishImage);
            }

            rateStar.setRating(Integer.parseInt(post.getRate()));
        }
    }

    /* ********************************* Adapter ********************************* */

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            
            View view = getLayoutInflater().inflate(R.layout.post_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post post = viewModel.getData().getValue().get(position);
            holder.myBind(post);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getData().getValue() == null) {
                return 0;
            }
            return viewModel.getData().getValue().size();
        }
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