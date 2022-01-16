package com.example.foodies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.foodies.model.Model;
import com.example.foodies.model.Post;
import com.example.foodies.model.User;

import java.util.List;

public class HomePageFragment extends Fragment {

    List<Post> data;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    TextView userName;
    ImageButton profile, homePage, addPost;
    private final static String SOURCE_PAGE = "homepage";
    String currUserEmail;
//    User currentUserDetails;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // Current login user
        currUserEmail = HomePageFragmentArgs.fromBundle(getArguments()).getUserEmail();


        /* *********************************** Current user *********************************** */
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        userName = view.findViewById(R.id.home_user_name);

        Model.instance.getUserByEmail(currUserEmail, user -> {
            userName.setText(user.getFullName());
        });

        /* ***************************** Post List - Recycler View ***************************** */

        swipeRefresh = view.findViewById(R.id.postlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> refresh());

        RecyclerView list = view.findViewById(R.id.postlist_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String postId = data.get(position).getId();
                Navigation.findNavController(v).navigate(
                        HomePageFragmentDirections
                                .actionHomePageToPostPageFragment(postId, SOURCE_PAGE, currUserEmail));
            }
        });


        /* ************************************ Footer menu ************************************ */

        View footer = view.findViewById(R.id.home_footer);

        addPost = footer.findViewById(R.id.NewPostFragment);
        homePage = footer.findViewById(R.id.HomePageFragment);
        profile = footer.findViewById(R.id.ProfileFragment);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "add btn was clicked........");
                Navigation.findNavController(view)
                        .navigate(HomePageFragmentDirections
                                .actionGlobalNewPostFragment(currUserEmail));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "profile btn was clicked");
                Navigation.findNavController(view)
                        .navigate(HomePageFragmentDirections
                                .actionGlobalProfileFragment(currUserEmail));
            }
        });

//        homePage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("TAG", "homepage btn was clicked");
//                Navigation.findNavController(view).navigate(R.id.action_global_homePage);
//            }
//        });


        /* ************************************ menu ************************************ */

//        setHasOptionsMenu(true);
//        viewModel.getData().observe(getViewLifecycleOwner(), list1 -> refresh());
//        swipeRefresh.setRefreshing(Model.instance.getStudentListLoadingState().getValue() == Model.StudentListLoadingState.loading);
//        Model.instance.getStudentListLoadingState().observe(getViewLifecycleOwner(), studentListLoadingState -> {
//            if (studentListLoadingState == Model.StudentListLoadingState.loading){
//                swipeRefresh.setRefreshing(true);
//            }else{
//                swipeRefresh.setRefreshing(false);
//            }

//        });


        refresh();
        return view;

    }

    private void refresh() {
        swipeRefresh.setRefreshing(true);
        Model.instance.getAllPosts((list) -> {
            data = list;
            adapter.notifyDataSetChanged();
            swipeRefresh.setRefreshing(false);
        });
    }

    /* *************************************** Holder *************************************** */

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


    /* *************************************** Adapter *************************************** */

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


//            Model.instance.getUserByEmail(currUserEmail, user -> {
//                holder.userName.setText(user.getFullName());
//            });
//            Student student = viewModel.getData().getValue().get(position);
            Post post = data.get(position);

            //TODO: after authentication find user name & img
            Model.instance.getUserByEmail(post.getUserEmail(), user -> {
                holder.userName.setText(user.getFullName());
//                holder.userImage.setImageDrawable(user.getImage());
            });

            holder.description.setText(post.getDishName());

            //TODO: set at "Post" img to ImageView - now its String
//            holder.dishImage.setImageDrawable(post.getImage());

            holder.rateNum.setText(post.getRate());
            holder.rateStar.setRating(Integer.parseInt(post.getRate()));

        }

        @Override
        public int getItemCount() {
            if (data == null) {
                return 0;
            }
            return data.size();
        }
    }

    /* *************************************** Menu Functions *************************************** */

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.student_list_menu,menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.addStudentFragment){
//            Log.d("TAG","ADD...");
//            return true;
//        }else {
//            return super.onOptionsItemSelected(item);
//        }
//    }

}

