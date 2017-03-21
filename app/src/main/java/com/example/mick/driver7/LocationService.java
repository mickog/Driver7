package com.example.mick.driver7;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mLocationClient;
    ProfileActivity activity;

    private Location mCurrentLocation;
    LocationRequest mLocationRequest;
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"SERVICE STOPPED",Toast.LENGTH_LONG).show();
        mLocationClient.disconnect();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO do something useful
        Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();
        mLocationClient = new GoogleApiClient.Builder(LocationService.this)
                .addApi(LocationServices.API).addConnectionCallbacks(LocationService.this)
                .addOnConnectionFailedListener(LocationService.this).build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setFastestInterval(1000);
        mLocationClient.connect();

        // getting the static instance of activity
         activity = ProfileActivity.instance;

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        mCurrentLocation = location;
        if (activity != null) {
            // we are calling here activity's method
            activity.receiveCo(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
        }
//        Toast.makeText(this, "IN LOCATION CHANGED"+mCurrentLocation.getLatitude() +", "+ mCurrentLocation.getLatitude(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        //if(servicesConnected()) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);
        //}

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}