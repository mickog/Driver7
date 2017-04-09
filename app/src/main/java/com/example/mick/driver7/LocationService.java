package com.example.mick.driver7;

import android.*;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static com.google.android.gms.wearable.DataMap.TAG;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    private GoogleApiClient mLocationClient;
    ProfileActivity activity;

    private Location mCurrentLocation;
    LocationRequest mLocationRequest;

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new GoogleApiClient.Builder(LocationService.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(LocationService.this)
                .addOnConnectionFailedListener(LocationService.this)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setFastestInterval(1000);
        mLocationClient.connect();

        // getting the static instance of activity
        activity = ProfileActivity.instance;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"SERVICE STOPPED",Toast.LENGTH_LONG).show();
        mLocationClient.disconnect();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean flag=false;
        flag =intent.getBooleanExtra("flag",false);
//        String s = intent.getStringExtra("KEY1");

        try {
            if (flag==true) {
                double geoLat = intent.getDoubleExtra("geoLat", 0.0);
                double geoLon = intent.getDoubleExtra("geoLon", 0.0);
                Toast.makeText(this, "Lat and Lon are " + geoLat + " " + geoLon, Toast.LENGTH_LONG).show();
                startGeofence(geoLat,geoLon);


            }
        }catch (Exception e)
        {
            Toast.makeText(this, "Exception in service is  "+e, Toast.LENGTH_SHORT).show();

        }

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

/************************************************************************************************************/
    /****************************NEW GEOFENCE METHODS****************************************///

    // ******************Create a Intent send by the notification***************************************/
    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent( context, ProfileActivity.class );
        intent.putExtra( NOTIFICATION_MSG, msg );

        return intent;
    }



    // Start Geofence creation process
    private void startGeofence(double lat, double lon) {
        LatLng home = new LatLng(lat,lon);
        Geofence geofence = createGeofence(home , 100 );
//        Toast.makeText(getBaseContext(), "sending geofences from service activity  ", Toast.LENGTH_SHORT).show();

        Log.i(TAG, "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG is "+geofence.toString());

        GeofencingRequest geofenceRequest = createGeofenceRequest( geofence );
        Log.i(TAG, "HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH is "+geofenceRequest.toString());

        addGeofence( geofenceRequest );
        Log.i(TAG, "startGeofence()");

    }


    /****************************Create GEOFENCE METHODS****************************************///

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest( Geofence geofence ) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence( geofence )
                .build();
    }
    /****************************Create GEOFENCE METHOD****************************************///
    private static final String GEOFENCE_REQ_ID = "Delivery Destination";

    private Geofence createGeofence( LatLng latLng, float radius ) {
        Log.d(TAG, "createGeofence---------------------->");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion( latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration( 1000000 )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
    }

    // *********************Add the created GeofenceRequest to the device's monitoring list**********/
    private void addGeofence(GeofencingRequest request) {
        if (checkPermission())
            Log.d(TAG, "addGeofenceAAAAAAAAAAAAAAAAAaBBBBBBBBBBBBBBBBBB" +checkPermission());

        LocationServices.GeofencingApi.addGeofences(
                    mLocationClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }
/*****************************check permissions***************************************************/
    private boolean checkPermission() {
        boolean perm = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
        Log.d(TAG, "checkPermission() isSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSs "+perm);
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }
    /*****************************create the pending intent***************************************************/

    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;
    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null ) {
            Log.d(TAG, "createGeofencePendingIntent is not null it is "+geoFencePendingIntent.toString());

            return geoFencePendingIntent;
        }

        Intent intent = new Intent( this, GeofenceTransitionService.class);
        Log.d(TAG, "returning intent "+intent.toString());

        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }
    /*****************************create the on result message***************************************************/

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if ( status.isSuccess() ) {
            Log.d(TAG, "something was succesfull ");

//            saveGeofence();
//            drawGeofence();
        } else {
            // inform about fail
        }
    }






}