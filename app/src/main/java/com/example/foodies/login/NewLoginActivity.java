package com.example.foodies.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.foodies.R;

public class NewLoginActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    NavController navCtl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        mAppBarConfiguration = new AppBarConfiguration.Builder().build();

        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.login_navhost);
        navCtl = navHost.getNavController();

        NavigationUI.setupActionBarWithNavController(this,navCtl);

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.main_navhost);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)|| super.onSupportNavigateUp();
    }
}