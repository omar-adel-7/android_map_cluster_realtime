package com.example.map_realtime.ui;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.map_realtime.Cluster.MyClusterItemRenderer;
import com.example.map_realtime.GetMyLocationInBackGround.GetMyLocationWorker;
import com.example.map_realtime.R;
import com.example.map_realtime.Utils.GeneralUtil;
import com.example.map_realtime.model.User;
import com.example.map_realtime.ui.presenter.IMapControlFragmentContract;
import com.example.map_realtime.ui.presenter.MapControlFragmentPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.location_map.fragments.BaseMapControlFragment;
import com.location_map.model.CustomAddress;
import com.location_map.utils.GetLocationHelper;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import modules.general.model.shareddata.Prefs;

import static com.example.map_realtime.Utils.Constants.FIREBASE_TOKEN_KEY;
import static com.example.map_realtime.Utils.Constants.LocationInBackgroundPeriodicTag;

/**
 * Created by Net22 on 11/13/2017.
 */

public class MapControlFragment extends BaseMapControlFragment<MapControlFragmentPresenter> implements

        IMapControlFragmentContract.IMapControlFragmentContractView
        , ClusterManager.OnClusterClickListener<User>

//       , ClusterManager.OnClusterInfoWindowClickListener<User>

        , ClusterManager.OnClusterItemClickListener<User>, ClusterManager.OnClusterItemInfoWindowClickListener<User> {

    PeriodicWorkRequest getMyLocationWorkRequest;

    public LatLng myLocation;
    public User myUser;

    public ClusterManager<User> mClusterManager;

    @Override
    public int getLayoutResource() {
        return R.layout.frg_map_control;
    }

    @Override
    public int getMapId() {
        return R.id.map;
    }

    @Override
    public void configureUI() {

        getLocationInBackground();
    }


    @Override
    public MapControlFragmentPresenter injectDependencies() {
        return new MapControlFragmentPresenter(getContainerActivity(), this);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
//        if(getMapControlFragmentPresenter().getDatabaseReferenceMyLocation()!=null)
//        {
//            getMapControlFragmentPresenter().getDatabaseReferenceMyLocation().removeValue();
//        }
        super.onStop();

        getMapControlFragmentPresenter().removeChildListener();

    }

    public MapControlFragmentPresenter getMapControlFragmentPresenter() {
        return ((MapControlFragmentPresenter) this.getPresenter());
    }

    @Override
    public void prepareMapClicks() {

        mClusterManager = new ClusterManager<>(getContainerActivity(), mGoogleMap);

        mGoogleMap.setOnCameraIdleListener(mClusterManager);


        //method 1
        mGoogleMap.setOnMarkerClickListener(mClusterManager);
        //method 2 this disables onClusterItemClick and  onClusterClick and onClusterItemInfoWindowClick
//        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                MapControlFragment.this.onMarkerClick(marker);
//                return false;
//            }
//        });

        mGoogleMap.setOnInfoWindowClickListener(mClusterManager);

        mGoogleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());


        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                CustomAddress customAddress = GetLocationHelper.getAddress(getContainerActivity(), latLng);
                Toast.makeText(getContainerActivity(), customAddress.getCityName(), Toast.LENGTH_LONG).show();
            }
        });

        // mGoogleMap.setOnInfoWindowClickListener(mClusterManager);


        doWork();
    }


    @Override
    public void doWork() {
        mClusterManager.setOnClusterItemClickListener(this);//setOnMarkerClickListener
        mClusterManager.setOnClusterClickListener(this);

        mClusterManager.setOnClusterItemInfoWindowClickListener(this);//setOnInfoWindowClickListener
//        mClusterManager.setOnClusterInfoWindowClickListener(this);

        mClusterManager.setRenderer(new MyClusterItemRenderer(getContainerActivity(), mGoogleMap, mClusterManager));

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                new CustomInfoWindowAdapter(getLayoutInflater()));

        super.doWork();
    }

    @Override
    public void gotLocationChangedBase(Location newLocation) {
        if (isAdded()) {
            LatLng latLng = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
            myLocation = latLng;
            if (myUser != null) {
                mClusterManager.removeItem(myUser);
            }
        }
    }

    @Override
    public void gotLocationChanged(Location newLocation) {
        if (isAdded()) {
            //Log.e("gotLocationChanged Longitude:", "" + String.valueOf(newLocation.getLongitude()));
            //  Log.e("gotLocationChanged Latitude:", String.valueOf(newLocation.getLatitude()));

            User userObject = new User(Prefs.getString(FIREBASE_TOKEN_KEY), newLocation.getLatitude(), newLocation.getLongitude(),
                    new Date().getTime());
            myUser = userObject;
            mClusterManager.addItem(myUser);
            mClusterManager.cluster();
            getMapControlFragmentPresenter().attachChildListener();
            getMapControlFragmentPresenter().getDatabaseReferenceMyLocation().setValue(myUser);
        }

    }


    private void getLocationInBackground() {
        getMyLocationWorkRequest = new PeriodicWorkRequest.Builder(GetMyLocationWorker.class, 1, TimeUnit.HOURS)
                .addTag(LocationInBackgroundPeriodicTag)
                .build();
        WorkManager.getInstance().enqueue(getMyLocationWorkRequest);
    }


    @Override
    public boolean onClusterItemClick(User item) {
        // Does nothing, but you could go into the user's profile page, for example.
        Log.e("onClusterItemClick ", "onClusterItemClick");

        CustomAddress customAddress = GetLocationHelper.getAddress(getContainerActivity(), item.getPosition());
        if (item.getDate() != null) {
            String dateText = GeneralUtil.convertDateFromLongToStr(item.getDate());
            Toast.makeText(getContainerActivity(), " " + getString(R.string.marker_clicked) + " "
                    + customAddress.getCityName() + " "
                    + GeneralUtil.getLastChars(
                    (item.getToken())) + " " + dateText, Toast.LENGTH_LONG).show();
        }

        return false;
    }

    @Override
    public boolean onClusterClick(Cluster<User> cluster) {
        Log.e("onClusterClick ", "onClusterClick");
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().getTitle();
        Toast.makeText(getContainerActivity(), cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
//            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(User item) {
        // Does nothing, but you could go into the user's profile page, for example.
        Log.e("onClusterItemInfoWindowClick ", "onClusterItemInfoWindowClick");
        CustomAddress customAddress = GetLocationHelper.getAddress(getContainerActivity(), item.getPosition());
        String dateText = GeneralUtil.convertDateFromLongToStr(item.getDate());
        Toast.makeText(getContainerActivity(), " " + getString(R.string.info_window_clicked) + " "
                + customAddress.getCityName() + " "
                + GeneralUtil.getLastChars(
                (item.getToken())) + " " + dateText, Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void onClusterInfoWindowClick(Cluster<User> cluster) {
//        // Does nothing, but you could go to a list of the users.
//        Log.e("onClusterInfoWindowClick","onClusterInfoWindowClick");
//        Log.e("onClusterInfoWindowClick size",cluster.getSize()+"");
//    }


    public void maxRadiusChanged() {
        for (int i = getMapControlFragmentPresenter().getUserArray().size() - 1; i >= 0; i--) {
            mClusterManager.removeItem(getMapControlFragmentPresenter().getUserArray().get(i));
        }
        mClusterManager.cluster();
        getMapControlFragmentPresenter().getUserArray().clear();
        getMapControlFragmentPresenter().removeChildListener();
        getMapControlFragmentPresenter().attachChildListener();
    }

}
