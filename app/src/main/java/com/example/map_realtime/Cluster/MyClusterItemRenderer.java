package com.example.map_realtime.Cluster;

import android.content.Context;
import android.util.Log;

import com.example.map_realtime.R;
import com.example.map_realtime.Utils.GeneralUtil;
import com.example.map_realtime.model.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.location_map.model.CustomAddress;
import com.location_map.utils.GetLocationHelper;

public class MyClusterItemRenderer extends DefaultClusterRenderer<User> {

    private Context context ;
    public MyClusterItemRenderer(Context context , GoogleMap googleMap , ClusterManager<User> clusterManager) {
        super(context.getApplicationContext(), googleMap, clusterManager);
        this.context=context;
    }

    @Override
    protected void onBeforeClusterItemRendered(User user, MarkerOptions markerOptions) {
        // Customize the marker here
        //super.onBeforeClusterItemRendered(myItem, markerOptions);
        Log.e("onBeforeClusterItemRendered","onBeforeClusterItemRendered");

        LatLng latLng = new LatLng(user.getLatitude(), user.getLongitude());
        CustomAddress customAddress = GetLocationHelper.getAddress(context,latLng);
        markerOptions.title(GeneralUtil.getLastChars(user.getToken())+" "+context.getString(R.string.at)
                +" "+customAddress.getCity());
        if (GeneralUtil.isMyUser( user)) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
        else
        {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        }

    }

    @Override
    protected void onBeforeClusterRendered(Cluster<User> cluster, MarkerOptions markerOptions) {
        // Customize the cluster here
      //  Log.e("onBeforeClusterRendered","onBeforeClusterRendered");

    }
    @Override
    protected void onClusterItemRendered(User clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        //here you have access to the marker itself
         Log.e("onClusterItemRendered","onClusterItemRendered");
        Log.e("onClusterItemRenderedUserTag",getMarker(clusterItem).getTitle());
     }

}