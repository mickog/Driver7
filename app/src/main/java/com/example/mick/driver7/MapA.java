package com.example.mick.driver7;

import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MapA extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    //declare and initialise variables

    boolean flag = true;    //flag boolean so camera doesn't keep zooming

    String latString;   //String for lat and lon because its easier to transfer
    String lonString;
    String name = "";   //String to see which driver selected
    MapFragment mf;     //map fragment to be used in landscape or portrair
    GoogleMap map;
    ArrayList<String> arrayNames;   //array list to initially transfer in the data
    ArrayList<String> arrayLat;
    ArrayList<String> arrayLon;

    ArrayList<String> optionList = new ArrayList<String>();
    private ListView listView1;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    FirebaseAuth firebaseAuth;
    public ArrayList getMyData()
    {
        //method to receive data from here
        Intent I = getIntent();
        arrayNames = I.getStringArrayListExtra("arrayNames");
        arrayLat = I.getStringArrayListExtra("arrayLat");
        arrayLon = I.getStringArrayListExtra("arrayLon");
        name = arrayNames.get(0);
        return arrayNames;
    }

    public void showMap() {
        mf = (MapFragment) getFragmentManager().findFragmentById(R.id.the_map);
        if (mf == null) {
            // If the map frag isnt in the view then we know we can start map activity
            // and pass it the arrays to be used on intial start up
            Intent intent = new Intent(this, MapActivity.class);
            intent.putStringArrayListExtra("arrayNames", arrayNames);
            intent.putStringArrayListExtra("arrayLat", arrayLat);
            intent.putStringArrayListExtra("arrayLon", arrayLon);
            intent.putExtra("name", name);
            startActivity(intent);
        } else {
            // or else we are using the landscape mode
            mf.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {    // map is loaded but not laid out yet

        this.map = map;
        //clear any of the markers and stuff off the map
        map.clear();
        // code to run when the map has loaded
        double lat, lon;

        //loop through the names array and see if any of them match off what was selected from list
        for (int i = 0; i < arrayNames.size(); i++) {

            //if the name from the list matches any of the names in the array that was passed into activity
            if (name.equals(arrayNames.get(i))) {
                //create a enw instance of AdminActiviy (because we can fetch co ordinates from here)
                AdminActivity appState = new AdminActivity();
                //i will need to be final because using it in inner class
                final int finalI = i;

                //call the method to get the coordinates, which takes the index and the new interface
                //because this all needs to be done async
                appState.getCo(new SnapShotListener(){
                    public void onListFilled(ArrayList<String> arrayLat,ArrayList<String> arrayLon){
//                        ArrayList<String> test = arrayLat;
//                        ArrayList<String> test1 = arrayLon;
                        latString = arrayLat.get(finalI);
                        lonString = arrayLon.get(finalI);
                        double lat = Double.parseDouble(latString);
                        double lon = Double.parseDouble(lonString);
                        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
                        LatLng toPosition = new LatLng(lat, lon);
                        if(flag) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(toPosition, 70));
                            flag=false;
                        }
                        map.clear();
                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lon))
                                .title("")
                        );
                    }
                    public void onFailure(){
                        //go crazy
                    }
                });
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        optionList.add("HOME");
        optionList.add("VIEW DRIVERS");
        optionList.add("DESIGNATE A DRIVER");
        optionList.add("LOG OUT");

        listView1 = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionList);
        listView1.setAdapter(mAdapter);

        listView1.setOnItemClickListener(this);
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

    public void setDriverName(String itemAtPosition) {

        this.name = itemAtPosition;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0) {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        }
                if(position == 1) {
        Intent intent = new Intent(this, MapA.class);
        intent.putStringArrayListExtra("arrayNames", arrayNames);
//                intent.putStringArrayListExtra("arrayLat", arrayLat);
//                intent.putStringArrayListExtra("arrayLon", arrayLon);
        startActivity(intent);
    }
            else if(position == 2) {
        Intent intent = new Intent(this, DesignateDriver.class);
        intent.putStringArrayListExtra("arrayNames", arrayNames);
        startActivity(intent);

    }
            else if(position == 3) {
        Toast.makeText(this,"GOODBYE",Toast.LENGTH_SHORT).show();
                    firebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);

    }


}
}
