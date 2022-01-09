package com.example.foodies;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class LogInFragment extends Fragment {

    Button loginBtn;
    TextView joinTv;
    Intent joinIntent, homePageIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        joinTv = view.findViewById(R.id.main_joinbtn_tv);
        joinTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { JoinUs();  }
        });


        loginBtn = view.findViewById(R.id.main_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { LogIn();  }
        });


        return view;
    }

    private void JoinUs() {
        
    }

    private void LogIn() {

    }
}