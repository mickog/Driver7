package com.example.mick.driver7;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import java.util.List;
import java.util.Timer;


public class GeofenceTransitionService extends IntentService {

    private static final String TAG = GeofenceTransitionService.class.getSimpleName();

    public static final int GEOFENCE_NOTIFICATION_ID = 0;

    public GeofenceTransitionService() {
        super(TAG);
    }
    Firebase ref = new Firebase(Config.FIREBASE_URL);
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String username = user.getDisplayName();

    //handle the intent
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent1 = new Intent("android.intent.category.LAUNCHER");
        intent1.setClassName("com.example.mick.driver7", "com.example.mick.driver7.ProfileActivity");
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
        //just printing for debugging purposes
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        // Handling errors
        if ( geofencingEvent.hasError() ) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode() );
            Log.e( TAG, errorMsg );
            return;
        }

        //this int determines the type of geofence
        int geoFenceTransition = geofencingEvent.getGeofenceTransition();
        // Check if the transition type is of interest
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ) {
            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            //this will call the method
            String geofenceTransitionDetails = getDetailsToDisplay(geoFenceTransition, triggeringGeofences );
            // Send notification details as a String
            sendNotification( geofenceTransitionDetails );
        }
    }


    String jobAddress;
    private String getDetailsToDisplay(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        System.out.println( "TEST2");

        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for ( Geofence geofence : triggeringGeofences ) {
            triggeringGeofencesList.add( geofence.getRequestId() );
        }

        String status = null;
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ) {
            ref.child("Driver").child(username).child("job").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snap) {
                    jobAddress =  snap.getValue().toString();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }

            });
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            DataCollection dcc = new DataCollection();
            dcc.setDriver(username);
            dcc.setJob(jobAddress);
            dcc.setTime(ts);
            ref.child("dataCollection").push().setValue(dcc);
            status = "Entering ";
            ref.child("Driver").child(username).child("jobStatus").setValue("Arrived at Job");

            //update firebase table based on the driver reaching or leaving the destination


            ref.child("Driver").child(username).child("jobFinished").setValue(ts);


        }
        else if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ) {
            status = "Exiting ";
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();

            ref.child("Driver").child(username).child("jobFinished").setValue(ts);
            ref.child("Driver").child(username).child("jobStatus").setValue("On The Way Back");
//            String jobAddress =  ref.child("Driver").child(username).child("job").getKey();
//            ref.child("dataCollection").push().setValue(username,jobAddress);

        }
        return status + TextUtils.join( ", ", triggeringGeofencesList);
    }

    private void sendNotification( String msg ) {
        System.out.println( "TEST3");
        Log.i(TAG, "sendNotification: " + msg );

        // Intent to start the Activity
        Intent notificationIntent = LocationService.makeNotificationIntent(
                getApplicationContext(), msg
        );

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ProfileActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        // Creating and sending Notification
        NotificationManager notificatioMng =
                (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificatioMng.notify(
                GEOFENCE_NOTIFICATION_ID,
                createNotification(msg, notificationPendingIntent));

    }

    // Create notification
    private Notification createNotification(String msg, PendingIntent notificationPendingIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_action_location)
                .setColor(Color.RED)
                .setContentTitle(msg)
                .setContentText("Entering Geofence")
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        return notificationBuilder.build();
    }


    private static String getErrorString(int errorCode) {
        System.out.println( "TEST5");

        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }
}
