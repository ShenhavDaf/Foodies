package com.example.foodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.foodies.login.NewLoginActivity;
import com.example.foodies.model.Model;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Model.instance.executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (Model.instance.isSignedIn()) {

                    String email = Model.instance.getLastUserEmail();
                    Model.instance.getUserByEmail(email, lastUser -> {
                        Model.instance.setCurrentUserModel(lastUser);
                        Model.instance.mainThread.post(() -> {
                            toHomeActivity();
                        });
                    });

                } else {
                    Model.instance.mainThread.post(() -> {
                        toLoginActivity();
                    });
                }
            }
        });
    }

    private void toLoginActivity() {
        startActivity(new Intent(this, NewLoginActivity.class));
        finish();
    }

    private void toHomeActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}