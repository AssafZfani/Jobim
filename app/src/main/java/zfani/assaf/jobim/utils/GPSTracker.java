package zfani.assaf.jobim.utils;

import android.app.Activity;
import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import androidx.annotation.Nullable;
import im.delight.android.location.SimpleLocation;

public class GPSTracker {

    public static SimpleLocation location;

    public GPSTracker(Activity activity) {
        location = new SimpleLocation(activity);
    }

    public static LatLng createLatLng(SimpleLocation location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static int getDistanceFromAddress(Application application, String address) {
        LatLng latLng = getLatLngFromAddress(application, address);
        if (latLng != null) {
            Location loc1 = new Location("");
            loc1.setLatitude(latLng.latitude);
            loc1.setLongitude(latLng.longitude);
            Location loc2 = new Location("");
            loc2.setLatitude(location.getLatitude());
            loc2.setLongitude(location.getLongitude());
            return (int) loc1.distanceTo(loc2);
        }
        return 0;
    }

    @Nullable
    public static LatLng getLatLngFromAddress(Application application, String address) {
        if (address == null || address.isEmpty()) {
            return null;
        }
        List<Address> addressList;
        try {
            addressList = new Geocoder(application).getFromLocationName(address, 1);
        } catch (IOException e) {
            return null;
        }
        if (addressList != null && !addressList.isEmpty()) {
            Address foundAddress = addressList.get(0);
            return new LatLng(foundAddress.getLatitude(), foundAddress.getLongitude());
        }
        return null;
    }

    @Nullable
    public static String getAddressFromLatLng(Activity activity, @Nullable LatLng latLng) {
        List<Address> addressList;
        try {
            addressList = new Geocoder(activity).getFromLocation(latLng != null ? latLng.latitude : location.getLatitude(), latLng != null ? latLng.longitude : location.getLongitude(), 1);
        } catch (IOException e) {
            return null;
        }
        if (addressList != null && !addressList.isEmpty()) {
            Address foundAddress = addressList.get(0);
            return foundAddress.getAddressLine(0) + ", " + foundAddress.getLocality();
        }
        return null;
    }
}