package com.example.map_realtime.firebase;

import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;

import modules.general.model.shareddata.Prefs;

import static com.example.map_realtime.Utils.Constants.FIREBASE_TOKEN_KEY;
import static com.example.map_realtime.Utils.Constants.FIREBASE_TOKEN_KEY_READY_EVENT;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = CustomFirebaseMessagingService.class.getName();


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String refreshedToken = s;
        Log.e(TAG, "Token Value: " + refreshedToken);

        Prefs.putString(FIREBASE_TOKEN_KEY, refreshedToken);

        Intent intent = new Intent(FIREBASE_TOKEN_KEY_READY_EVENT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);


    }


}
