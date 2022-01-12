package com.example.foodies;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;



public class SignUpFragment extends Fragment {

    Button join;
    EditText fullNameEt, emailEt, passwordEt, verifyEt,  cityEt, birthdayEt;
    // TODO: add image, add verify


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        // Inflate the layout for this fragment
        fullNameEt = view.findViewById(R.id.signin_name_et);
        emailEt = view.findViewById(R.id.signin_email_et);
        passwordEt = view.findViewById(R.id.signin_password_et);
        verifyEt = view.findViewById(R.id.signin_verify_et);
        cityEt = view.findViewById(R.id.signin_city_et);
        birthdayEt = view.findViewById(R.id.signin_birthdate_et);

        join = view.findViewById(R.id.signin_join_btn);
        join.setOnClickListener(v -> Join(view));
        return view;
    }

    private void Join(View view){
        System.out.println("join button was clicked");

        String fullname = fullNameEt.getText().toString();
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String verify = verifyEt.getText().toString();
        String city = cityEt.getText().toString();
        String birthday = birthdayEt.getText().toString();

        if(fullname.isEmpty()){
            fullNameEt.setError("Please enter your full name");
            fullNameEt.requestFocus();
            return;
        }


        if(city.isEmpty()){
            cityEt.setError("Please enter your city");
            cityEt.requestFocus();
            return;
        }

        if(birthday.isEmpty()){
            birthdayEt.setError("Please enter your birthday");
            birthdayEt.requestFocus();
            return;
        }
//        if(!Patterns.DATE.matcher(birthday).matches()){
//            birthday.setError("Please provide valid email");
//            birthday.requestFocus();
//            return;
//        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEt.setError("Please provide valid email");
            emailEt.requestFocus();
            return;
        }
        if(email.isEmpty()){
            emailEt.setError("Please enter your Email");
            emailEt.requestFocus();
            return;
        }

        if(password.length() < 5){
            passwordEt.setError("Password length should be at least 5 characters");
            passwordEt.requestFocus();
            return;
        }
        if(!verify.equals(password)){
            verifyEt.setError("Wrong password");
            verifyEt.requestFocus();
            return;
        }

        Navigation.findNavController(view).navigate(R.id.action_global_homePage);
    }
}