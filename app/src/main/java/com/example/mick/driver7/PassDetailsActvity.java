package com.example.mick.driver7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PassDetailsActvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_details_actvity);
    }

   public String getMyData()
   {
       return "hello world";
   }
}
