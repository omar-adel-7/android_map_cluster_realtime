package com.example.map_realtime.Utils;

import android.location.Location;

import com.example.map_realtime.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;

import modules.general.model.shareddata.Prefs;

import static com.example.map_realtime.Utils.Constants.FIREBASE_TOKEN_KEY;
import static com.example.map_realtime.Utils.Constants.MAX_DISTANCE;
import static com.example.map_realtime.Utils.Constants.MAX_DISTANCE_KEY;

public class GeneralUtil {
    public static boolean isNearBy(double lat1, double long1, double lat2, double long2) {
        float distance = distanceBetween(lat1,long1,lat2,long2);
        if(distance<=Prefs.getFloat(MAX_DISTANCE_KEY,MAX_DISTANCE))
        {
            return true ;
        }
        return false ;
    }

    public static boolean isMyUser(  User userToCompare ) {
        if(Prefs.getString(FIREBASE_TOKEN_KEY).equals(userToCompare.getToken()))
        {
            return true ;
        }
        return false ;
    }


    public static float distanceBetween(double lat1, double long1, double lat2, double long2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, long1,
                lat2, long2, results);
        return results[0];

    }

    public static String getLastChars(String str)
    {
        if (str.length() == 3) {
            return str;
        } else if (str.length() > 3) {
            return str.substring(str.length() - 3);
        } else {
            return str;
         }
    }

    public static String convertDateFromLongToStr(long longDate)
    {
        Date date=new Date(longDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
         return simpleDateFormat.format(date);
    }


}
