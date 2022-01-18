package com.example.foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.foodies.model.Model;
import com.example.foodies.model.Post;

import java.util.List;

public class ProfileFragment extends Fragment {

    private final static String SOURCE_PAGE = "profilepage";
    ProfileViewModel viewModel;

    Button editProfileBtn;
    TextView fullNameTv;
    MyAdapter adapter;

    String currUserEmail;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* ********************************** View Items ********************************** */

        currUserEmail = ProfileFragmentArgs.fromBundle(getArguments()).getUserEmail();


        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fullNameTv = view.findViewById(R.id.profile_fullname_tv);

        editProfileBtn = view.findViewById(R.id.profile_editprofile_btn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile(v);
            }
        });

        Model.instance.getUserByEmail(currUserEmail, user -> {
            fullNameTv.setText(user.getFullName());
        });


        /* ***************************** Post List - Recycler View ***************************** */

        RecyclerView list = view.findViewById(R.id.profile_postslist_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String postId = viewModel.getData().get(position).getId();
                Navigation.findNavController(v)
                        .navigate(ProfileFragmentDirections
                                .actionProfileFragmentToEditPostFragment(postId, SOURCE_PAGE, currUserEmail));

                System.out.println("to the post page");
            }
        });

        refresh();
        return view;
    }


    /* ********************************* Function/ Navigation ********************************* */

    private void editProfile(View view) {
        System.out.println("edit profile was clicked");
        Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_editProfileFragment);
    }

    private void refresh() {
//        swipeRefresh.setRefreshing(true);
        Model.instance.getUserByEmail(currUserEmail, user -> {
            Model.instance.getUserPosts(user, (list) -> {
                if (list != null) {
                    viewModel.setData(list);
//                    data = list;
                    adapter.notifyDataSetChanged();
                    //            swipeRefresh.setRefreshing(false);
                }
            });
        });
    }

    /* ********************************* My View Holder ********************************* */

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, description, rateNum;
        RatingBar rateStar;
        ImageView userImage, dishImage;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            userImage = itemView.findViewById(R.id.listrow_avatar_imv);
            userName = itemView.findViewById(R.id.listrow_username_tv);
            description = itemView.findViewById(R.id.listrow_description_tv);
            dishImage = itemView.findViewById(R.id.listrow_post_img);
            rateNum = itemView.findViewById(R.id.listrow_rate_tv);
            rateStar = itemView.findViewById(R.id.listrow_ratingBar);
            rateStar.setEnabled(false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v, pos);
                }
            });
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
            Post post = viewModel.getData().get(position);

            //TODO: after authentication find user name & img
//            holder.userImage.setImageDrawable(post.getUserId().getImage);
//            holder.userName.setText(post.getUserId());

            holder.description.setText(post.getDishName());

            //TODO: set at "Post" img to ImageView - now its String
//            holder.dishImage.setImageDrawable(post.getImage());

            holder.rateNum.setText(post.getRate());
            holder.rateStar.setRating(Integer.parseInt(post.getRate()));

        }

        @Override
        public int getItemCount() {
            if (viewModel.getData() == null) {
                return 0;
            }
            return viewModel.getData().size();
        }
    }

}