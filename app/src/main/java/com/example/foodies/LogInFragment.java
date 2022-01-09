package com.example.foodies;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LogInFragment extends Fragment {

    Button loginBtn;
    TextView joinTv;
    EditText email, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        email = view.findViewById(R.id.login_email_et);
        password = view.findViewById(R.id.login_password_et);


        joinTv = view.findViewById(R.id.main_joinbtn_tv);
        joinTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinUs(v);
            }
        });


        loginBtn = view.findViewById(R.id.main_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogIn(v);
            }
        });


        return view;
    }

    private void JoinUs(View view) {
        Navigation.findNavController(view).navigate(R.id.action_logInFragment_to_signIn);
    }

    private void LogIn(View view) {

        if (email.getText().toString().equals("admin") || email.getText().toString().equals("Admin")) {
            if (password.getText().toString().equals("123")){
                Navigation.findNavController(view).navigate(R.id.action_logInFragment_to_homePage);
            }
        }
    }





}