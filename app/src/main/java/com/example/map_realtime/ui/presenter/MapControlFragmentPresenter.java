package com.example.map_realtime.ui.presenter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.map_realtime.Utils.GeneralUtil;
import com.example.map_realtime.model.DataManager;
import com.example.map_realtime.model.User;
import com.example.map_realtime.ui.MapControlFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import modules.general.firebase.RealTime.listeners.IFireBaseRtCallBack;
import modules.general.model.shareddata.Prefs;

import static com.example.map_realtime.Utils.Constants.FIREBASE_TOKEN_KEY;


/**
 * Created by Net22 on 11/13/2017.
 */

public class MapControlFragmentPresenter
        implements IMapControlFragmentContract.IMapControlFragemntContractPresenter
  , IFireBaseRtCallBack  {
    private final Context mContext;
    IMapControlFragmentContract.IMapControlFragmentContractView mView;

    private ArrayList<User> mUserArray = new ArrayList<User>();

    public ArrayList<User> getUserArray() {
        return mUserArray;
    }

    private DatabaseReference mUsersReference;


    private DatabaseReference databaseReferenceMyLocation;

    public DatabaseReference getDatabaseReferenceMyLocation() {
        return databaseReferenceMyLocation;
    }


    public DatabaseReference getUsersReference() {
        return mUsersReference;
    }

    public MapControlFragmentPresenter(Context context, IMapControlFragmentContract.IMapControlFragmentContractView view) {
        mView = view;
        mContext = context;

        mUsersReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReferenceMyLocation = mUsersReference.child(Prefs.getString(FIREBASE_TOKEN_KEY));

    }

    @Override
    public void attachChildListener() {
        DataManager.getInstance(mContext).attachDatabaseReadListener(mUsersReference);
        DataManager.getInstance(mContext).setSyncListener(this);

    }
    @Override
    public void removeChildListener() {
        DataManager.getInstance(mContext).detachDatabaseReadListener(mUsersReference);
    }




    @Override
    public void onChildAdded(@NonNull DataSnapshot singleSnapshot, @Nullable String s) {
        userAdded(singleSnapshot);
        if (singleSnapshot.getRef().getParent().equals(mUsersReference)) {
            User user = singleSnapshot.getValue(User.class);
            //Log.e(getClass().getName(), "onChildAdded key:" + singleSnapshot.getKey());
          //  Log.e(getClass().getName(), "onChildAdded:" + user.getToken());
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot singleSnapshot, @Nullable String s) {
        userChanged(singleSnapshot);
        if (singleSnapshot.getRef().getParent().equals(mUsersReference)) {
            User user = singleSnapshot.getValue(User.class);
          //  Log.e(getClass().getName(), "onChildChanged key:" + singleSnapshot.getKey());
           // Log.e(getClass().getName(), "onChildChanged:" + user.getToken());
        }

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot singleSnapshot) {
        userDeleted(singleSnapshot);
        if (singleSnapshot.getRef().getParent().equals(mUsersReference)) {
            User user = singleSnapshot.getValue(User.class);
          // Log.e(getClass().getName(), "onChildRemoved key:" + singleSnapshot.getKey());
           // Log.e(getClass().getName(), "onChildRemoved:" + user.getToken());
        }

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot singleSnapshot, @Nullable String s) {
        if (singleSnapshot.getRef().getParent().equals(mUsersReference)) {
            // User user = singleSnapshot.getValue(User.class);
        }
    }


    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        //        Log.e(getClass().getName(), "DatabaseError:onCancelled"+ databaseError.toString());
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void userAdded(DataSnapshot singleSnapshot) {
        if (singleSnapshot.getRef().getParent().equals(mUsersReference)) {
            User user = singleSnapshot.getValue(User.class);
            if (((MapControlFragment) mView).myLocation != null) {
                if (!GeneralUtil.isMyUser( user)) {
                    if (GeneralUtil.isNearBy(((MapControlFragment) mView).myLocation.latitude,
                            ((MapControlFragment) mView).myLocation.longitude
                            , user.getLatitude(), user.getLongitude())) {
                         LatLng latLng = new LatLng(user.getLatitude(), user.getLongitude());
                         getUserArray().add(user);
                        ((MapControlFragment) mView).mClusterManager.addItem(user);
                        ((MapControlFragment) mView).mClusterManager.cluster();
                    }
                }
            }

        }

    }
//
    @Override
    public void userChanged(DataSnapshot singleSnapshot) {
        if (singleSnapshot.getRef().getParent().equals(mUsersReference)) {
            User user = singleSnapshot.getValue(User.class);
            if (((MapControlFragment) mView).myLocation != null) {
                if (!GeneralUtil.isMyUser( user)) {
                    for (int i = 0; i < getUserArray().size(); i++) {
                        if ((getUserArray().get(i).getToken()
                                .equals(user.getToken()))) {

                            if (GeneralUtil.isNearBy(((MapControlFragment) mView).myLocation.latitude,
                                    ((MapControlFragment) mView).myLocation.longitude
                                    , user.getLatitude(), user.getLongitude())) {
                                 ((MapControlFragment) mView).mClusterManager.removeItem(getUserArray().get(i));
                                 getUserArray().remove(i);
                                 getUserArray().add(user);
                                ((MapControlFragment) mView).mClusterManager.addItem(user);
                                ((MapControlFragment) mView).mClusterManager.cluster();

                            } else {
                                ((MapControlFragment) mView).mClusterManager.removeItem(getUserArray().get(i));
                                 getUserArray().remove(i);
                                ((MapControlFragment) mView).mClusterManager.cluster();
                            }

                            break;
                        }
                    }
                }
            }


        }
    }

    @Override
    public void userDeleted(DataSnapshot singleSnapshot) {
        if (singleSnapshot.getRef().getParent().equals(mUsersReference)) {
            User user = singleSnapshot.getValue(User.class);
            if (((MapControlFragment) mView).myLocation != null) {
                if (!GeneralUtil.isMyUser( user)) {
                    for (int i = 0; i < getUserArray().size(); i++) {
                        if ((( getUserArray().get(i)).getToken().equals(user.getToken()))) {
                            ((MapControlFragment) mView).mClusterManager.removeItem(getUserArray().get(i));
                             getUserArray().remove(i);
                            ((MapControlFragment) mView).mClusterManager.cluster();
                            break;
                        }
                    }
                }
            }

        }
    }


}