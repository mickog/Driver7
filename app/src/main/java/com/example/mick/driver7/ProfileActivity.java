package com.example.mick.driver7;

/**
 * Created by Mick on 13/02/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    static ProfileActivity instance;
    private TextView tvEmail;
        private Button buttonSave;
    Button buttonStop;
    Button logout;
    private String userId;
    String username;
    FirebaseAuth firebaseAuth;


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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        username = user.getDisplayName();
        buttonSave = (Button) findViewById(R.id.start);
        buttonStop = (Button) findViewById(R.id.stop);
        logout = (Button) findViewById(R.id.logout);

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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getInstance().signOut();
                System.exit(0);

            }

        });
    }
/***********************************Method for updating Firebase with the users new co-ordinates******************/

String jobTemp="none";
    String job="none";
    String jobStatus="";
    boolean flag = false;

    public void updatetFirebase(Double lat, Double lon) {
        //Creating firebase object
        final Firebase ref = new Firebase(Config.FIREBASE_URL);

        //Creating driver object
        Driver d = new Driver();

        //Adding values
        d.setName(username);
        d.setLat(lat);
        d.setLon(lon);
        d.setJob(job);
        d.setJobStatus(jobStatus);
        //Storing values to firebase under the reference Driver
//        ref.child("Driver2").push().setValue(d);
        ref.child("Driver").child(username).setValue(d);


        //adding a value event listener so if data in database changes it does in textview also not needed at the minute
        ref.child("Driver").child(username).child("job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                job=snapshot.getValue().toString();
            }

            /************had to implement this method****************/
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());

            }
        });
//adding a value event listener so if data in database changes it does in textview also not needed at the minute
        ref.child("Driver").child(username).child("jobStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                jobStatus=snapshot.getValue().toString();
            }

            /************had to implement this method****************/
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());

            }
        });

        //if job changes at any stage
        if(!job.equals(jobTemp)&& !job.equals("none"))
                {
                    jobTemp=job;
                    flag=true;
//                    Toast.makeText(ProfileActivity.this, "JOB IS "+job, Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);

                    // set title
                    alertDialogBuilder.setTitle("Accept Delivery to \n"+job);

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Click yes to confirm!")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    ref.child("Driver").child(username).child("jobStatus").setValue("En Route");

                                    createGeofence(job);
                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                }

    }
    /********************* Method to create the geofence based on given job*************************************/

    private void createGeofence(String job)  {
//        String job = "27 brompton grove dublin 15";
        double latitude=0.0;
        double longitude=0.0;
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(job, 1);
            Address address = addresses.get(0);
            if (addresses.size() > 0) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
//                Toast.makeText(ProfileActivity.this, "JOB = "+job+"\ncoord =  "+latitude+" "+longitude, Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(ProfileActivity.this, "list is  empty", Toast.LENGTH_LONG).show();

            }
        }
        catch(Exception e)
        {
            Toast.makeText(ProfileActivity.this, "Exception geocoding is  "+e, Toast.LENGTH_LONG).show();

        }
        try {
//            Toast.makeText(ProfileActivity.this, "sending geofences from profile activity  ", Toast.LENGTH_LONG).show();

            startService(new Intent(getBaseContext(), LocationService.class));
            Intent i = new Intent(getBaseContext(), LocationService.class);
// potentially add data to the intent
            i.putExtra("flag", true);
            i.putExtra("geoLat", latitude);
            i.putExtra("geoLon", longitude);
            startService(i);
        }catch(Exception e)
            {
                Toast.makeText(ProfileActivity.this, "Exception from passing stuff is  "+e, Toast.LENGTH_LONG).show();

            }
    }

    /********************* Method to receive the co-ordinates and pass to the update firebase********************/
    public void receiveCo(Double x, Double y)
    {
//        Toast.makeText(this, "RECEIVING "+x+" AND "+y, Toast.LENGTH_SHORT).show();
        updatetFirebase(x,y);

    }
    /********************* NEW GEOFENCE CODE****************************************************************/



}


