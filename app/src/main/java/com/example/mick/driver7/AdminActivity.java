package com.example.mick.driver7;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.R.id.list;

/**
 * Created by Mick on 22/02/2017.
 */

public class AdminActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private TextView textViewPersons;
    Button b;
    ListView listV;
    Driver[] items = new Driver[2];

    final List<Driver> driverList = new ArrayList<Driver>();
    ListView listView1;
    List<String> array = new ArrayList<String>();

    /**************************** On Create Method for when class is creates************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //set the context sp we can use firebase
        Firebase.setAndroidContext(this);
        textViewPersons = (TextView) findViewById(R.id.adminViewPerson);
        listView1 = (ListView) findViewById(R.id.lv);
        listView1.setOnItemClickListener(this);

        b = (Button)findViewById(R.id.adminButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListView();
                Toast.makeText(getApplicationContext(),"in here",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showListView() {

        //Creating firebase object
        Firebase ref = new Firebase(Config.FIREBASE_URL);

        //Storing values to firebase under the reference Driver
//        ref.child("Driver2").push().setValue(d);
//        ref.child("Driver3").setValue(d);

        //adding a value event listener so if data in database changes it does in textview also not needed at the minute
        ref.child("Driver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Driver d = postSnapshot.getValue(Driver.class);
                    String details = "Name : " + d.getName() + "\nLatitude : " + d.getLat() + "\nLongitude : " + d.getLon();
//                    Toast.makeText(getApplicationContext(),"Drivers name is "+d.getName(),Toast.LENGTH_SHORT).show();
                    textViewPersons.setText(details);
                    driverList.add(d);

                    ArrayAdapter adapter = new ArrayAdapter(AdminActivity.this,android.R.layout.simple_list_item_1,driverList);

                    listView1.setAdapter(adapter);

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
        Toast.makeText(this,"Clicked Driver "+d.getName()+" Lat is "+d.getLat()+" Lon is"+d.getLon(), Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(view.getContext(),MapA.class);
        intent.putExtra("text", "text");
        intent.putExtra("city", "city");
        startActivity(intent);

    }
}