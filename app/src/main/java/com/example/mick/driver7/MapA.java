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

    String latString;
    String lonString;
    String name = "";
    MapFragment mf;
    GoogleMap map;
    ArrayList<String> arrayNames;
    ArrayList<String> arrayLat;
    ArrayList<String> arrayLon;
    public ArrayList getMyData()
    {
        Intent I = getIntent();
        arrayNames = I.getStringArrayListExtra("arrayNames");
        arrayLat = I.getStringArrayListExtra("arrayLat");
        arrayLon = I.getStringArrayListExtra("arrayLon");
//        Toast.makeText(this,"Array names mapA "+arrayNames.get(1),Toast.LENGTH_SHORT).show();
        name = arrayNames.get(1);

        return arrayNames;
    }

    public void showMap() {
        mf = (MapFragment) getFragmentManager().findFragmentById(R.id.the_map);
        if (mf == null) {

            // Frag isn't in layout so start axctivity
            // so start MapActivity (Activity B)
            // and pass it the info about the selected item
            Intent intent = new Intent(this, MapActivity.class);
            intent.putStringArrayListExtra("arrayNames", arrayNames);
            intent.putStringArrayListExtra("arrayLat", arrayLat);
            intent.putStringArrayListExtra("arrayLon", arrayLon);
            intent.putExtra("name", name);
            startActivity(intent);
        } else {
            // CityMApFragment (Fragment B) is in the layout (tablet layout)
            mf.getMapAsync(this);

        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {    // map is loaded but not laid out yet

        this.map = map;
        map.clear();
        // code to run when the map has loaded
        double lat, lon;

        for (int i = 0; i < arrayNames.size(); i++) {

            if (name.equals(arrayNames.get(i))) {
                AdminActivity appState = new AdminActivity();
                final int finalI = i;
                appState.getLat(i, new SnapShotListener(){
                    public void onListFilled(ArrayList<String> arrayLat,ArrayList<String> arrayLon){
                        AdminActivity appState1 = new AdminActivity();
                        ArrayList<String> test = arrayLat;
                        ArrayList<String> test1 = arrayLon;
                        latString = test.get(finalI);
                        lonString = test1.get(finalI);

                        Toast.makeText(getApplicationContext(), "lat ok string is " + latString+" lon is "+lonString, Toast.LENGTH_LONG).show();
                        double lat = Double.parseDouble(latString);
                        double lon = Double.parseDouble(lonString);
                        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
                        map.moveCamera(CameraUpdateFactory.zoomTo(10));
                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lon))
                                .title("")
                        );
                    }
                    public void onFailure(){
                        //go crazy
                    }
                });
                Toast.makeText(getApplicationContext(), "lat string ok non an is " + latString, Toast.LENGTH_SHORT).show();

//                latString = appState.getLat(i);
//                lonString = appState.getLon(i);
//                lat = Double.parseDouble(latString);
//                lon = Double.parseDouble(lonString);
//                lat = Double.parseDouble(arrayLat.get(i));
//                lon = Double.parseDouble(arrayLon.get(i));
//                Toast.makeText(getApplicationContext(), "That name was " + name, Toast.LENGTH_SHORT).show();
//                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
//                map.moveCamera(CameraUpdateFactory.zoomTo(10));
//                map.addMarker(new MarkerOptions()
//                        .position(new LatLng(lat, lon))
//                        .title("")
//                );

            }
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

    public void setDriverName(String itemAtPosition) {

        this.name = itemAtPosition;

    }

}
