package com.example.mick.driver7;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

/**
 * Created by Mick on 22/02/2017.
 */

public class AdminActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    final List<Driver> driverList = new ArrayList<Driver>();
    ArrayList<String> arrayNames;
    ArrayList<String> arrayLat;
    ArrayList<String> arrayLon;
    ArrayList<String> optionList = new ArrayList<String>();
    private ListView listView;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    FirebaseAuth firebaseAuth;

    /**************************** On Create Method for when class is creates************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_admin);
        //set the context sp we can use firebase
        Firebase.setAndroidContext(this);
        ImageView iv = (ImageView)findViewById(R.id.compass);
        iv.setImageResource(R.drawable.compass);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity.this.viewDrivers();

            }
        });

        ImageView iv2 = (ImageView)findViewById(R.id.car);
        iv2.setImageResource(R.drawable.car);

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity.this.designateDriver();

            }
        });

        ImageView iv3 = (ImageView)findViewById(R.id.exit);
        iv3.setImageResource(R.drawable.exit);
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity.this.exitApplication();
            }
        });


        optionList.add("HOME");
        optionList.add("VIEW DRIVERS");
        optionList.add("DESIGNATE A DRIVER");
        optionList.add("LOG OUT");


        fillDriverList();
        listView = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionList);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(this);
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
    /*************************************new stuff for oncreate******************************************************************/

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

    /*************************************old stuff needed after this**********************************************************/

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
try {
    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

        Driver d = postSnapshot.getValue(Driver.class);
//                    Toast.makeText(AdminActivity.this,"data snapshot Drivers name is "+d.getName(),Toast.LENGTH_SHORT).show();
//                    textViewPersons.setText(details);

        System.out.println("data snapshot Drivers name is -------------------> " + d.getName());
        driverList.add(d);
        arrayNames.add(d.getName());
        if (d.getJobStatus().equals("Arrived at Job")) {
            ref.child("Driver").child(d.getName()).child("jobStatus").setValue("Completing transaction");
            Toast.makeText(AdminActivity.this, d.getName() + " has JUST just reached " + d.getJob() + " after" + Math.abs((d.getJobFinished() - d.getJobStarted()) / 60) + " minutes", Toast.LENGTH_LONG).show();
//                        d.setJobStatus(jobStatusWayBack);
        }
        if (d.getJobStatus().equals("On The Way Back")) {
            ref.child("Driver").child(d.getName()).child("jobStatus").setValue("ComingHome");

            Toast.makeText(AdminActivity.this, d.getName() + " is on the way back it took" + Math.abs((d.getJobFinished() - d.getJobStarted()) / 60) + " minutes", Toast.LENGTH_LONG).show();
//                        d.setJobStatus(jobStatusWayBack);
        }
        try {
            arrayLat.add(Double.toString(d.getLat()));
            arrayLon.add(Double.toString(d.getLon()));
//                    ArrayAdapter adapter = new ArrayAdapter(AdminActivity.this,android.R.layout.simple_list_item_1,driverList);
//                    listView1.setAdapter(adapter);

        } catch (Exception e) {
            System.out.println("no coordinates to add");
        }
    }
}catch(Exception e){System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOO "+e);}

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


        if(position == 1) {
            viewDrivers();

        }
        else if(position == 2) {
            designateDriver();


        }
        else if(position == 3) {
//            Toast.makeText(this,"GOODBYE",Toast.LENGTH_SHORT).show();
//            firebaseAuth.getInstance().signOut();
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
              exitApplication();
        }

    }

    private void viewDrivers() {
        Intent intent = new Intent(this, MapA.class);
        intent.putStringArrayListExtra("arrayNames", arrayNames);
        intent.putStringArrayListExtra("arrayLat", arrayLat);
        intent.putStringArrayListExtra("arrayLon", arrayLon);
        startActivity(intent);
    }

    private void designateDriver() {
        Intent intent = new Intent(this, DesignateDriver.class);
        intent.putStringArrayListExtra("arrayNames", arrayNames);
        startActivity(intent);
    }

    public void getCo(SnapShotListener listener) {
        fillDriverList(listener);

    }
    public void fillDriverList(final SnapShotListener listener) {
//try {
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
                listener.onListFilled(arrayLat, arrayLon);
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
//}catch(Exception e){System.out.println("BBBBBBBBBBBBBBBBBBBBBBB "+e);}
    }
    public void exitApplication()
    {
        Toast.makeText(this,"GOODBYE",Toast.LENGTH_SHORT).show();
        firebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}