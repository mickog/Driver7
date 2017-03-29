package com.example.mick.driver7;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapA extends AppCompatActivity implements OnMapReadyCallback {

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
    public ArrayList getMyData()
    {
        //method to receive data from here
        Intent I = getIntent();
        arrayNames = I.getStringArrayListExtra("arrayNames");
        arrayLat = I.getStringArrayListExtra("arrayLat");
        arrayLon = I.getStringArrayListExtra("arrayLon");
        name = arrayNames.get(1);
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


    }

    public void setDriverName(String itemAtPosition) {

        this.name = itemAtPosition;

    }

}
