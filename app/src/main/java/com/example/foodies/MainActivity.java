package com.example.foodies;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Intent joinIntent, homePageIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinIntent = new Intent(this, SignIn.class);
        homePageIntent = new Intent(this, HomePage.class);

        Button joinBtn = findViewById(R.id.main_join_btn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { JoinUs();  }
        });

        Button loginBtn = findViewById(R.id.main_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { LogIn();  }
        });
    }
    private void JoinUs() {
        startActivity(joinIntent);
    }

    private void LogIn() {
        startActivity(homePageIntent);
    }
}