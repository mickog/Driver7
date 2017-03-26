package com.example.mick.driver7;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    String name = "";
    MapFragment mf;
    GoogleMap map;
    ArrayList<String> arrayNames;
    ArrayList<String> arrayLat;
    ArrayList<String> arrayLon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent I = getIntent();
        arrayNames = I.getStringArrayListExtra("arrayNames");
        arrayLat = I.getStringArrayListExtra("arrayLat");
        arrayLon = I.getStringArrayListExtra("arrayLon");
        name = I.getStringExtra("name");
        mf = (MapFragment) getFragmentManager().findFragmentById(R.id.the_map);
        mf.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {    // map is loaded but not laid out yet
        this.map = map;
        map.clear();
        // code to run when the map has loaded
        double lat, lon;

        for (int i = 0; i < arrayNames.size(); i++) {

            if (name.equals(arrayNames.get(i))) {
                lat = Double.parseDouble(arrayLat.get(i));
                lon = Double.parseDouble(arrayLon.get(i));
                Toast.makeText(getApplicationContext(), "That name was " + name, Toast.LENGTH_SHORT).show();
                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
                map.moveCamera(CameraUpdateFactory.zoomTo(10));
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lon))
                        .title("")
                );

            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }

}
