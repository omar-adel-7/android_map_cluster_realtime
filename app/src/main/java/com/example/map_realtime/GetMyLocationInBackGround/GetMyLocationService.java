package com.example.map_realtime.GetMyLocationInBackGround;


import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.map_realtime.R;
import com.example.map_realtime.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.location_map.listeners.GetLocationListeners;
import com.location_map.utils.GetLocationHelper;

import java.util.Date;

import modules.general.model.shareddata.Prefs;
import modules.general.utils.NotificationUtils;

import static com.example.map_realtime.Utils.Constants.FIREBASE_TOKEN_KEY;
import static modules.general.utils.NotificationUtils.ANDROID_CHANNEL_ID;

public class GetMyLocationService extends Service implements GetLocationListeners {
    private static final String LOG_TAG = GetMyLocationService.class.getName();
    NotificationUtils notificationUtils;
    int notification_id = 56;

    GetLocationHelper getLocationHelper;


    DatabaseReference databaseReferenceMyLocation;
    private DatabaseReference mUsersReference;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationUtils = new NotificationUtils(this);
        Log.e(LOG_TAG, "Received Start Foreground Intent ");

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this
                , ANDROID_CHANNEL_ID)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.getting_your_loc_send_title))
                .setContentText(getString(R.string.getting_your_loc_send_text));
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
         Notification notification = notificationBuilder.build();

        notificationUtils.getManager().notify(notification_id, notification);

        startForeground(notification_id,
                notification);

        mUsersReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReferenceMyLocation = mUsersReference.child(Prefs.getString(FIREBASE_TOKEN_KEY));

        getMyLocation();


        return START_STICKY;
    }

    private void getMyLocation() {

        getLocationHelper = new GetLocationHelper(this, this);
        if (GetLocationHelper.checkGpsState(this)) {
            getLocationHelper.getMyLocation();
        } else {
            stop(false);
        }
    }


    @Override
    public void onLocationChanged(Location newLocation) {
      //  Log.e("service onLocationChanged  getLongitude: ", "" + newLocation.getLongitude());
      //  Log.e("service onLocationChangeddddd  getLatitude: ", "" + newLocation.getLatitude());

        //     Log.e("background service update  ", "firebase location ");
            User userObject = new User(Prefs.getString(FIREBASE_TOKEN_KEY), newLocation.getLatitude(), newLocation.getLongitude(), new Date().getTime());
            databaseReferenceMyLocation.setValue(userObject);
        stop(true);
    }

    private void stop(boolean hasLocation) {
        if (hasLocation) {
            getLocationHelper.stopLocationUpdates();
        }
        //Log.e(LOG_TAG, "Stop Foreground Intent");
        stopForeground(true);
        stopSelf();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
      //  Log.e(LOG_TAG, "In onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }
}