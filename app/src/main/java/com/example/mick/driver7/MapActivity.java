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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {

    double lngMy;
    double latMy;
    String text;
    String city = "";
    MapFragment mf;
    GoogleMap map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent I = getIntent();
        text = I.getStringExtra("text");
        city = I.getStringExtra("city");

        mf = (MapFragment) getFragmentManager().findFragmentById(R.id.the_map);
        mf.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {    // map is loaded but not laid out yet
        this.map = map;
//        map.clear();
        // code to run when the map has loaded
        String[] separated = text.split(" ");
        double lat, lon;
//        if (city.equals(separated[0])) {
////            Toast.makeText(this, city+" getting into dublin in map activity",Toast.LENGTH_LONG ).show();
//
//            lat = Double.parseDouble(separated[1]);
//
//            lon = Double.parseDouble(separated[2]);
//        } else if (city.equals(separated[3])) {
//            lat = Double.parseDouble(separated[4]);
//            lon = Double.parseDouble(separated[5]);
//        }
//        else {
//            lat = 0;
//            lon = 0;
//        }
        if (!city.equals("My Location")) {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(53.4013419, -6.408903))
                    .title("")
            );

            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(53.4013419, -6.408903)));
            map.moveCamera(CameraUpdateFactory.zoomTo(10));

        }
        if (city.equals("My Location")) {

            map.setMyLocationEnabled(true);
            Location myLocation = map.getMyLocation();
            map.setOnMyLocationChangeListener(this);
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

    @Override
    public void onMyLocationChange(Location location) {
        latMy = location.getLatitude();
        lngMy = location.getLongitude();

        map.addMarker(new MarkerOptions()
                .position(new LatLng(latMy, lngMy))
                .title("")
        );
        Toast.makeText(getApplicationContext(),"Co or are "+latMy+" and "+lngMy,Toast.LENGTH_LONG).show();
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latMy, lngMy)));
        map.moveCamera(CameraUpdateFactory.zoomTo(100));
        map.setOnMyLocationChangeListener(null);

    }
}
