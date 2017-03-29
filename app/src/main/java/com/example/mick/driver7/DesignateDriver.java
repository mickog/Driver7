package com.example.mick.driver7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DesignateDriver extends AppCompatActivity implements AdapterView.OnItemClickListener {

    final List<Customer> custList = new ArrayList<Customer>();
    ListView listView;
    ArrayList<String> addressList = new ArrayList<String>();
    ArrayList<String> arrayNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designate_driver);
        Intent I = getIntent();

        arrayNames = I.getStringArrayListExtra("arrayNames");

        Firebase.setAndroidContext(this);
        listView = (ListView) findViewById(R.id.lvCust);

        listView.setOnItemClickListener(this);

        fillCustomerList();
    }

    private void fillCustomerList() {

        //Creating firebase object
        Firebase ref = new Firebase(Config.FIREBASE_URL);

        //Storing values to firebase under the reference Driver
//        ref.child("Driver2").push().setValue(d);
//        ref.child("Driver3").setValue(d);


        //adding a value event listener so if data in database changes it does in textview also not needed at the minute
        ref.child("AddressTable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                addressList.clear();
                custList.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Customer c = postSnapshot.getValue(Customer.class);

                    System.out.println("Customer Details -------------------> "+c.getName()+" "+c.getAddress()+" "+c.getId());
                    custList.add(c);

                    addressList.add(c.getName()+"\n"+c.getAddress());


                }
                ArrayAdapter adapter = new ArrayAdapter(DesignateDriver.this,android.R.layout.simple_list_item_1,addressList);

                listView.setAdapter(adapter);

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

        chooseDriver(custList.get(position));
    }

    private void chooseDriver(Customer customer) {
        ArrayAdapter adapter1 = new ArrayAdapter(DesignateDriver.this,android.R.layout.simple_list_item_1,arrayNames);

        listView.setAdapter(adapter1);
    }


}
