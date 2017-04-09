package com.example.mick.driver7;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mick on 22/02/2017.
 */

public class AdminActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private TextView textViewPersons;

    Driver[] items = new Driver[2];

    final List<Driver> driverList = new ArrayList<Driver>();
    ListView listView1;
    ArrayList<String> arrayNames;
    ArrayList<String> arrayLat;
    ArrayList<String> arrayLon;
    ArrayList<String> optionList = new ArrayList<String>();


    /**************************** On Create Method for when class is creates************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //set the context sp we can use firebase
        Firebase.setAndroidContext(this);
//        textViewPersons = (TextView) findViewById(R.id.adminViewPerson);
        optionList.add("VIEW DRIVERS");
        optionList.add("DESIGNATE A DRIVER");
        optionList.add("LOG OUT");
        listView1 = (ListView) findViewById(R.id.lv);
        ArrayAdapter adapter = new ArrayAdapter(AdminActivity.this, android.R.layout.simple_list_item_1, optionList);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(this);
        fillDriverList();


    }

    public void fillDriverList() {

        //Creating firebase object
        final Firebase ref = new Firebase(Config.FIREBASE_URL);

        //Storing values to firebase under the reference Driver
//        ref.child("Driver2").push().setValue(d);
//        ref.child("Driver3").setValue(d);


        String[]names;
//        final String jobStatusWayBack="ON WAY BACK";
        //adding a value event listener so if data in database changes it does in textview also not needed at the minute
        ref.child("Driver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
//               Toast.makeText(AdminActivity.this,"job status is "+ snapshot.child("jobStatus").getValue().toString(),Toast.LENGTH_LONG).show();
                arrayNames = new ArrayList<String>();
                arrayLat = new ArrayList<String>();
                arrayLon = new ArrayList<String>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Driver d = postSnapshot.getValue(Driver.class);
//                    Toast.makeText(AdminActivity.this,"data snapshot Drivers name is "+d.getName(),Toast.LENGTH_SHORT).show();
//                    textViewPersons.setText(details);
                    System.out.println("data snapshot Drivers name is -------------------> "+d.getName());
                    driverList.add(d);
                    arrayNames.add(d.getName());
                    if(d.getJobStatus().equals("Arrived at Job"))
                    {
                        ref.child("Driver").child(d.getName()).child("jobStatus").setValue("Completing transaction");
                       Toast.makeText(AdminActivity.this,d.getName()+" has JUST just reached "+d.getJob()+" after"+(d.getJobFinished()-d.getJobStarted())/1000*60+" minutes",Toast.LENGTH_LONG).show();
//                        d.setJobStatus(jobStatusWayBack);
                    }
                    if(d.getJobStatus().equals("On The Way Back"))
                    {
                        ref.child("Driver").child(d.getName()).child("jobStatus").setValue("ComingHome");

                        Toast.makeText(AdminActivity.this,d.getName()+" is on the way back it took"+(d.getJobFinished()-d.getJobStarted())/1000/60+" minutes",Toast.LENGTH_LONG).show();
//                        d.setJobStatus(jobStatusWayBack);
                    }
                    try{
                        arrayLat.add(Double.toString(d.getLat()));
                        arrayLon.add(Double.toString(d.getLon()));
//                    ArrayAdapter adapter = new ArrayAdapter(AdminActivity.this,android.R.layout.simple_list_item_1,driverList);
//                    listView1.setAdapter(adapter);

                    }catch(Exception e)
                    {
                        System.out.println("no coordinates to add");
                    }
                }

            }

            /************had to implement this method****************/
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());

            }
        });



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Driver d = driverList.get(position);
//        Toast.makeText(this,"Clicked Driver "+d.getName()+" Lat is "+d.getLat()+" Lon is"+d.getLon(), Toast.LENGTH_SHORT).show();


        if(position == 0) {
            Intent intent = new Intent(this, MapA.class);
            intent.putStringArrayListExtra("arrayNames", arrayNames);
            intent.putStringArrayListExtra("arrayLat", arrayLat);
            intent.putStringArrayListExtra("arrayLon", arrayLon);
            startActivity(intent);
        }
        else if(position == 1) {
            Intent intent = new Intent(this, DesignateDriver.class);
            intent.putStringArrayListExtra("arrayNames", arrayNames);
            startActivity(intent);
//            Toast.makeText(this,"DESIGNATE A DRIVER ACTIVITY",Toast.LENGTH_SHORT).show();

        }
        else if(position == 2) {
            Toast.makeText(this,"GOODBYE",Toast.LENGTH_SHORT).show();
            System.exit(0);

        }

    }

    public void getCo(SnapShotListener listener) {
        fillDriverList(listener);

    }
    public void fillDriverList(final SnapShotListener listener) {

        //Creating firebase object
        Firebase ref = new Firebase(Config.FIREBASE_URL);

        ref.child("Driver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                arrayLat = new ArrayList<String>();
                arrayLon = new ArrayList<String>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Driver d = postSnapshot.getValue(Driver.class);

                    arrayLat.add(Double.toString(d.getLat()));
                    arrayLon.add(Double.toString(d.getLon()));


                }
                if (listener != null) {
                    listener.onListFilled(arrayLat,arrayLon);
                }

            }

            /************
             * had to implement this method
             ****************/
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

}