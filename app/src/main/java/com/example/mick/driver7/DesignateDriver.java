package com.example.mick.driver7;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DesignateDriver extends AppCompatActivity implements AdapterView.OnItemClickListener {

    final List<Customer> custList = new ArrayList<Customer>();
    ListView listView;
    ArrayList<String> addressList = new ArrayList<String>();
    ArrayList<String> arrayNames;
    ArrayAdapter adapter;
    //Creating firebase object
    Firebase ref = new Firebase(Config.FIREBASE_URL);

    private ListView listView1;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    ArrayList<String> optionList = new ArrayList<String>();
    ArrayAdapter adapter1;
    FirebaseAuth firebaseAuth;
    ImageView iv;
    ImageView iv2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designate_driver);
        Intent I = getIntent();

        arrayNames = I.getStringArrayListExtra("arrayNames");

        Firebase.setAndroidContext(this);
        listView = (ListView) findViewById(R.id.lvCust);
        iv = (ImageView)findViewById(R.id.imageView6);
        iv2 = (ImageView)findViewById(R.id.imageView7);
        listView.setOnItemClickListener(this);

        fillCustomerList();
        optionList.add("HOME");
        optionList.add("VIEW DRIVERS");
        optionList.add("DESIGNATE A DRIVER");
        optionList.add("LOG OUT");

        listView1 = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionList);
        listView1.setAdapter(mAdapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Intent intent = new Intent(DesignateDriver.this, AdminActivity.class);
                    startActivity(intent);
                }

                if (position == 1) {
                    Intent intent = new Intent(DesignateDriver.this, MapA.class);
                    intent.putStringArrayListExtra("arrayNames", arrayNames);
//                intent.putStringArrayListExtra("arrayLat", arrayLat);
//                intent.putStringArrayListExtra("arrayLon", arrayLon);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(DesignateDriver.this, DesignateDriver.class);
                    intent.putStringArrayListExtra("arrayNames", arrayNames);
                    startActivity(intent);

                } else if (position == 3) {
                    Toast.makeText(DesignateDriver.this, "GOODBYE", Toast.LENGTH_SHORT).show();
                    firebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(DesignateDriver.this, LoginActivity.class);
                    startActivity(intent);



                }
            }


        });
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    /*************************************new stuff for oncreate******************************************************************/
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillCustomerList() {

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

                    addressList.add(c.getAddress());


                }
                adapter = new ArrayAdapter(DesignateDriver.this,android.R.layout.simple_list_item_1,addressList);

                listView.setAdapter(adapter);

            }

            /************had to implement this method****************/
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());

            }
        });
    }

    String chosenAddress;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

         if(listView.getAdapter()==adapter)
        {
            chosenAddress = addressList.get(position);
            chooseDriver(custList.get(position));
        }

        else {

            Toast.makeText(this,"Driver is "+arrayNames.get(position)+" and customer address is "+chosenAddress,Toast.LENGTH_SHORT ).show();

            ref.child("Driver").child(arrayNames.get(position)).child("job").setValue(chosenAddress);

        }
    }

    private void chooseDriver(Customer customer) {
        iv.setImageResource(R.drawable.user);
        iv2.setImageResource(R.drawable.driv);
        adapter1 = new ArrayAdapter(DesignateDriver.this,android.R.layout.simple_list_item_1,arrayNames);
        listView.setAdapter(adapter1);


    }


}
