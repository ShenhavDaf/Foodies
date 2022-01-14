package com.example.foodies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    Button editProfileBtn;
    TextView fullNameTv;
//    MyAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* ********************************** View Items ********************************** */
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fullNameTv = view.findViewById(R.id.profile_fullname_tv);

        editProfileBtn = view.findViewById(R.id.profile_editprofile_btn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile(v);
            }
        });


        /* ***************************** Post List - Recycler View ***************************** */

        RecyclerView list = view.findViewById(R.id.profile_postslist_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        list.setAdapter(adapter);
//
//        adapter.setOnItemClickListener(new HomePageFragment.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v,int position) {
////                String stId = viewModel.getData().getValue().get(position).getId();
//                  Navigation.findNavController(v).navigate(
//                        StudentListRvFragmentDirections.actionStudentListRvFragmentToStudentDetailsFragment(stId));
//
//                System.out.println("to the post page");
//            }
//        });


        return view;
    }


    /* ********************************* Function/ Navigation ********************************* */

    private void editProfile(View view) {
        System.out.println("edit profile was clicked");
        Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_editProfileFragment);
    }
}