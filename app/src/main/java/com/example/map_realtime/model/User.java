package com.example.map_realtime.model;

import com.example.map_realtime.Utils.GeneralUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by logonrm on 01/08/2017.
 */

@IgnoreExtraProperties
public class User implements ClusterItem {

    private String token;
    private Double longitude;
    private Double latitude;
    private Long  date ;



    public User() {

    }

    public User(String token, Double latitude, Double longitude, Long date) {
        this.token = token;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public User( Double latitude, Double longitude, Long date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    @Override
    public LatLng getPosition() {
        return   new LatLng(getLatitude(),getLongitude());
    }

    @Override
    public String getTitle() {
        return GeneralUtil.getLastChars(getToken());
    }

    @Override
    public String getSnippet() {
        return getTitle();
    }
}
