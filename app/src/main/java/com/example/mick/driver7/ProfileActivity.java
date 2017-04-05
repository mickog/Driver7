package com.example.mick.driver7;

/**
 * Created by Mick on 13/02/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    static ProfileActivity instance;
    private TextView tvEmail;
    private EditText editTextName;
    private EditText editLon;
    private EditText editLat;
    private TextView textViewPersons;
    private Button buttonSave;
    Button buttonStop;
    private String userId;

    /***************************On Create Method for when class is creates************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //create instance of this so I can reference it in the service(to send co-ordinates
        instance = this;

        //declare and initialise components
        tvEmail = (TextView) findViewById(R.id.tvEmailProfile);
        userId = getIntent().getExtras().getString("UserID");
        tvEmail.setText(getIntent().getExtras().getString("Email") + "\nUser ID is =  " + userId);
        buttonSave = (Button) findViewById(R.id.start);
        buttonStop = (Button) findViewById(R.id.stop);
        textViewPersons = (TextView) findViewById(R.id.textViewPersons);

        //set the context sp we can use firebase
        Firebase.setAndroidContext(this);

        //set on click listener for the stop button this will stop the service running
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(getBaseContext(),LocationService.class));

            }
        });

        //set on click listener for the start button to start the service which generates users co-ordinates
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(getBaseContext(),LocationService.class));
                //updatetFirebase("Method man",2.001, 3.8882);
            }

        });


        //Creating firebase object
        Firebase ref = new Firebase(Config.FIREBASE_URL);

        String[]names;
        //adding a value event listener so if data in database changes it does in textview also not needed at the minute
        ref.child("Driver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Driver d = postSnapshot.getValue(Driver.class);

                }

            }

            /************had to implement this method****************/
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());

            }
        });
            }
/***********************************Method for updating Firebase with the users new co-ordinates******************/

    public void updatetFirebase(String name,Double lat, Double lon) {

        //Creating firebase object
        Firebase ref = new Firebase(Config.FIREBASE_URL);

        //Creating driver object
        Driver d = new Driver();
        //Adding values
        d.setName(name);
        d.setLat(lat);
        d.setLon(lon);

        //Storing values to firebase under the reference Driver
//        ref.child("Driver2").push().setValue(d);
        ref.child("Driver").child("Driver2").setValue(d);


    }
/********************* Method to receive the co-ordinates and pass to the update firebase********************/
    public void receiveCo(Double x, Double y)
    {
        Toast.makeText(this, "RECEIVING "+x+" AND "+y, Toast.LENGTH_SHORT).show();
        updatetFirebase("MICK",x,y);

    }
    /********************* NEW GEOFENCE CODE****************************************************************/



}


