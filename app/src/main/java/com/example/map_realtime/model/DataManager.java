package com.example.map_realtime.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import modules.general.firebase.RealTime.listeners.FireBaseRtCallBack;
import modules.general.firebase.RealTime.listeners.IFireBaseRtCallBack;


public class DataManager implements IFireBaseRtCallBack {

    private static DataManager INSTANCE ;
    private  static FireBaseRtCallBack fireBaseRtCallBack ;
    private static Context  context ;

    public static DataManager getInstance(Context context)
    {
        if(INSTANCE==null)
        {
            INSTANCE=new DataManager(context);
        }
        return INSTANCE ;
    }

    private DataManager(Context context)
    {
        DataManager.context = context ;
        fireBaseRtCallBack =  FireBaseRtCallBack.getInstance(context);
    }

    public void setSyncListener(IFireBaseRtCallBack iFireBaseRtCallBack) {
        fireBaseRtCallBack .setiFireBaseRtCallBack(iFireBaseRtCallBack);
    }

    public   void attachDatabaseReadListener( DatabaseReference databaseReference  )
    {
        fireBaseRtCallBack.attachDatabaseReadListener(databaseReference);
    }

    public   void detachDatabaseReadListener( DatabaseReference databaseReference  )
    {
        fireBaseRtCallBack.detachDatabaseReadListener(databaseReference);
    }




    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }
}
