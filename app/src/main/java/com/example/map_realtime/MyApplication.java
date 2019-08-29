package com.example.map_realtime;

import android.app.Application;
import android.content.ContextWrapper;

import com.google.firebase.database.FirebaseDatabase;

import modules.general.model.shareddata.Prefs;

/**
 * Created by Net15 on 13/12/2016.
 */
public class MyApplication extends Application {

    public MyApplication() {

    }


    @Override
    public void onCreate() {
        super.onCreate();

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(false)
                .build();

        // for data persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }


}
