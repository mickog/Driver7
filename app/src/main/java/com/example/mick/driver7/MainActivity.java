package com.example.mick.driver7;

/**
 * Created by Mick on 13/02/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MultiDex.install(this);
    }
    public void btnRegistration_Click(View v) {
        Intent i = new Intent(com.example.mick.driver7.MainActivity.this, RegistrationActivity.class);
        startActivity(i);
    }
    public void btnLogin_Click(View v) {
        Intent i = new Intent(com.example.mick.driver7.MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
}

