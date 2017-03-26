package com.example.mick.driver7;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
//    Button b;
    ListView listV;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //set the context sp we can use firebase
        Firebase.setAndroidContext(this);
//        textViewPersons = (TextView) findViewById(R.id.adminViewPerson);
        optionList.add("VIEW DRIVERS");
        optionList.add("LOG OUT");
        listView1 = (ListView) findViewById(R.id.lv);
        ArrayAdapter adapter = new ArrayAdapter(AdminActivity.this,android.R.layout.simple_list_item_1,optionList);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(this);

//        b = (Button)findViewById(R.id.adminButton);


                fillDriverList();
//                Toast.makeText(getApplicationContext(),"in here",Toast.LENGTH_LONG).show();


    }

    public void fillDriverList() {

        //Creating firebase object
        Firebase ref = new Firebase(Config.FIREBASE_URL);

        //Storing values to firebase under the reference Driver
//        ref.child("Driver2").push().setValue(d);
//        ref.child("Driver3").setValue(d);


        String[]names;
        //adding a value event listener so if data in database changes it does in textview also not needed at the minute
        ref.child("Driver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                arrayNames = new ArrayList<String>();
                arrayLat = new ArrayList<String>();
                arrayLon = new ArrayList<String>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Driver d = postSnapshot.getValue(Driver.class);
                    String details = "Name : " + d.getName() + "\nLatitude : " + d.getLat() + "\nLongitude : " + d.getLon();
//                    Toast.makeText(AdminActivity.this,"data snapshot Drivers name is "+d.getName(),Toast.LENGTH_SHORT).show();
//                    textViewPersons.setText(details);
                    System.out.println("data snapshot Drivers name is -------------------> "+d.getName());
                    driverList.add(d);
                    arrayNames.add(d.getName());
                    arrayLat.add(Double.toString(d.getLat()));
                    arrayLon.add(Double.toString(d.getLon()));
//                    ArrayAdapter adapter = new ArrayAdapter(AdminActivity.this,android.R.layout.simple_list_item_1,driverList);
//                    listView1.setAdapter(adapter);

                }

//                Driver d = driverList.get(0);
////                Toast.makeText(this,"Clicked Driver "+d.getName()+" Lat is "+d.getLat()+" Lon is"+d.getLon(), Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(AdminActivity.this,MapA.class);
//                intent.putStringArrayListExtra("arrayNames", arrayNames);
//                intent.putExtra("text", "text");
//                intent.putExtra("name", "name");
//                intent.putExtra("driver", d.getName());
//                intent.putExtra("lat", d.getLat());
//                intent.putExtra("lon", d.getLon());
//                startActivity(intent);


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

//        Toast.makeText(this,"Array names admin"+arrayNames.get(1),Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,MapA.class);
        intent.putStringArrayListExtra("arrayNames", arrayNames);
        intent.putStringArrayListExtra("arrayLat", arrayLat);
        intent.putStringArrayListExtra("arrayLon", arrayLon);
        startActivity(intent);

    }
    public interface SnapshotListener{
        void onListFilled(ArrayList<String> arrayLat);
        void onFailure();
    }
    public void getLat(int i, SnapShotListener listener) {
        fillDriverList(listener);
//        return arrayLat.get(i);
        //return"0.000";

    }

//    public String getLat(int i) {
//        fillDriverList();
//        arrayLat.get(1);
//        return"0.000";
//
//    }
    public String getLon(int i) {
//        fillDriverList();
//        return arrayLon.get(i);
        return"0.000";
    }

    public void fillDriverList(final SnapShotListener listener) {

        //Creating firebase object
        Firebase ref = new Firebase(Config.FIREBASE_URL);

        //adding a value event listener so if data in database changes it does in textview also not needed at the minute
        ref.child("Driver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                arrayLat = new ArrayList<String>();
                arrayLon = new ArrayList<String>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Driver d = postSnapshot.getValue(Driver.class);

                    System.out.println("data snapshot Drivers name is -------------------> " + d.getName());

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