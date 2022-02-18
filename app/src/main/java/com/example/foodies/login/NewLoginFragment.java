package com.example.foodies.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.foodies.MainActivity;
import com.example.foodies.R;
import com.example.foodies.model.Model;

public class NewLoginFragment extends Fragment {

    Button loginBtn;
    TextView joinTv;
    EditText emailEt, passwordEt, emailInputEt, passwordInputEt;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int layoutName;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutName = R.layout.fragment_new_login;
        }
        else{
            layoutName = R.layout.horizontal_fragment_log_in;
        }

        View view = inflater.inflate(R.layout.fragment_new_login, container, false);
        emailEt = view.findViewById(R.id.newlogin_email_et);
        passwordEt = view.findViewById(R.id.newlogin_password_et);
        emailInputEt = view.findViewById(R.id.newlogin_input_email_et);
        passwordInputEt = view.findViewById(R.id.newlogin_input_password_et);
        progressBar = view.findViewById(R.id.newlogin_progressBar);

        progressBar.setVisibility(View.GONE);

        joinTv = view.findViewById(R.id.newlogin_joinbtn_tv);
        joinTv.setOnClickListener(v -> JoinUs(v));

        loginBtn = view.findViewById(R.id.newlogin_login_btn);
        loginBtn.setOnClickListener(v -> LogIn(v));

        return view;
    }


    private void JoinUs(View view) {
        Navigation.findNavController(view).navigate(R.id.action_newLoginFragment_to_signUpFragment);
    }

    private void LogIn(View view) {

        progressBar.setVisibility(View.VISIBLE);
        loginBtn.setEnabled(false);

        String localInputIEmail = emailInputEt.getText().toString().trim();
        String localInputPassword = passwordInputEt.getText().toString().trim();

        /* *************************************** Validations *************************************** */

        if (!Patterns.EMAIL_ADDRESS.matcher(localInputIEmail).matches()) {
            emailInputEt.setError("Please provide valid email");
            emailInputEt.requestFocus();

            return;
        }
        if (localInputIEmail.isEmpty()) {
            emailInputEt.setError("Please enter your Email");
            emailInputEt.requestFocus();
            return;
        }

        if (localInputPassword.length() < 6) {
            passwordInputEt.setError("Password length should be at least 6 characters");
            passwordInputEt.requestFocus();
            return;
        }

        toHomeActivity(localInputIEmail, localInputPassword);
    }

    private void toHomeActivity(String localMail, String localPass) {

        Model.instance.UserLogin(localMail, localPass, userID -> {

            if(userID == null){
                progressBar.setVisibility(View.GONE);
                loginBtn.setEnabled(true);

                String msg = "Username or password incorrect!!\nPlease try again 😊";

                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setNegativeButton("OK", (dialog, which) -> dialog.cancel());

                AlertDialog alert = builder.create();
                alert.setTitle("Error");
                alert.setMessage("\n"+msg+"\n");
                alert.show();
            }
            else{
                loginBtn.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                Model.instance.getUserByEmail(localMail, user -> {
                    Model.instance.setCurrentUserModel(user);
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                });
            }
        });
    }
}