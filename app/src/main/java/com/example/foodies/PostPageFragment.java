package com.example.foodies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PostPageFragment extends Fragment {

    Button editPostBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_page, container, false);
        // Inflate the layout for this fragment

        editPostBtn = view.findViewById(R.id.postpage_editpost_btn);

        // TODO:
        // if the user is not the creator to the post, we need to set the visibility to GONE
        editPostBtn.setVisibility(View.VISIBLE); // GONE OR VISIBLE

        editPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPost(view);
            }
        });
        return view;
    }


    private void editPost(View view){

        System.out.println("edit post button eas clicked");
        Navigation.findNavController(view).navigate(R.id.action_postPageFragment_to_editPostFragment);

    }
}