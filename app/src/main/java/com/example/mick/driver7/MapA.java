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

import java.io.IOException;
import java.io.InputStream;

public class MapA extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener  {

    String text;
    double lngMy;
    double latMy;
    String city = "";
    MapFragment mf;
    GoogleMap map;


    public String setLoc() {
        InputStream is;

        String text = "";
        try {
            is = getAssets().open("loc.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer);


        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return text;


    }

    public void showMap() {

        mf = (MapFragment) getFragmentManager().findFragmentById(R.id.the_map);
        if (mf == null) {
            // Frag isn't in layout so start axctivity
            // so start MapActivity (Activity B)
            // and pass it the info about the selected item
            text = setLoc();
            Intent intent = new Intent(this, MapActivity.class);

            intent.putExtra("text", text);
            intent.putExtra("city", city);

            startActivity(intent);
        } else {
            // CityMApFragment (Fragment B) is in the layout (tablet layout)
            mf.getMapAsync(this);

        }
    }

    @Override
    public void onMapReady(GoogleMap map) {    // map is loaded but not laid out yet

        this.map = map;
//        map.clear();
        // code to run when the map has loaded
        text = setLoc();
        String[] separated = text.split(" ");
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
        double lat = 0, lon=0;
//        if (city.equals(separated[0])) {
//
//            lat = Double.parseDouble(separated[1]);
//            lon = Double.parseDouble(separated[2]);
//        } else if (city.equals(separated[3])) {
//            lat = Double.parseDouble(separated[4]);
//            lon = Double.parseDouble(separated[5]);
//        } else if (city.equals("myLocation")) {
//            lat = 0;
//            lon = 0;
////            map.setMyLocationEnabled(true);
//            Location myLocation = map.getMyLocation();
//
//
//        }  else {
//            lat = 0;
//            lon = 0;
//        }
        if (!city.equals("My Location")) {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .title("")
            );

            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
            map.moveCamera(CameraUpdateFactory.zoomTo(10));

        }
        if (city.equals("My Location")) {

            map.setMyLocationEnabled(true);
            Location myLocation = map.getMyLocation();
            map.setOnMyLocationChangeListener(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_city_map, menu);
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

    public void setCity(String itemAtPosition) {

        this.city = itemAtPosition;
        Toast.makeText(getApplicationContext(),"city is "+this.city,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMyLocationChange(Location location) {
        latMy = location.getLatitude();
        lngMy = location.getLongitude();
        map.addMarker(new MarkerOptions()
                .position(new LatLng(latMy, lngMy))
                .title("")
        );
        Toast.makeText(getApplicationContext(),"Co or are "+latMy+" and "+lngMy, Toast.LENGTH_LONG).show();

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latMy, lngMy)));
        map.moveCamera(CameraUpdateFactory.zoomTo(100));
        map.setOnMyLocationChangeListener(null);
    }
}
