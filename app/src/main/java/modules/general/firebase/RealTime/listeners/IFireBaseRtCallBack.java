package modules.general.firebase.RealTime.listeners;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public   interface IFireBaseRtCallBack {

        void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) ;

           void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) ;

           void onChildRemoved(@NonNull DataSnapshot dataSnapshot) ;

           void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) ;

           void onCancelled(@NonNull DatabaseError databaseError) ;

           void onDataChange(@NonNull DataSnapshot dataSnapshot) ;

 }
