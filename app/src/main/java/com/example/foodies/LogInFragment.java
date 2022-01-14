package com.example.foodies;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodies.model.Model;
import com.example.foodies.model.User;


public class LogInFragment extends Fragment {

    Button loginBtn;
    TextView joinTv;
    EditText emailEt, passwordEt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* ********************************** View Items ********************************** */

        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        emailEt = view.findViewById(R.id.login_email_et);
        passwordEt = view.findViewById(R.id.login_password_et);


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

    /* *************************************** Functions *************************************** */

    private void JoinUs(View view) {
        Navigation.findNavController(view).navigate(R.id.action_logInFragment_to_signIn);
    }

    private void LogIn(View view) {

        String localMail = emailEt.getText().toString().trim();
        String localPass = passwordEt.getText().toString().trim();

        /* *************************************** Validations *************************************** */

        if(!Patterns.EMAIL_ADDRESS.matcher(localMail).matches()){
            emailEt.setError("Please provide valid email");
            emailEt.requestFocus();
            return;
        }
        if(localMail.isEmpty()){
            emailEt.setError("Please enter your Email");
            emailEt.requestFocus();
            return;
        }

        if(localPass.length() < 6){
            passwordEt.setError("Password length should be at least 6 characters");
            passwordEt.requestFocus();
            return;
        }

        /* ------------------------------------ Navigation ------------------------------------ */

        Model.instance.UserLogin(localMail,localPass, userID -> {
            Navigation.findNavController(view).navigate(LogInFragmentDirections.actionGlobalHomePage(localMail));
        });

    }

}