package zfani.assaf.jobim.Utils;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import zfani.assaf.jobim.Activities.HomePage;

public class GPSTracker implements LocationListener {

    public static Location location;
    private Activity activity;

    public GPSTracker(Activity activity) {

        this.activity = activity;

        initLocation();
    }

    public static LatLng createLatLng(Location location) {

        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static String getAddressFromLatLng(Activity activity, LatLng latLng, Location location) {

        try {

            Address foundAddress = new Geocoder(activity).getFromLocation(latLng != null ? latLng.latitude : location.getLatitude(),
                    latLng != null ? latLng.longitude : location.getLongitude(), 1).get(0);

            return foundAddress.getAddressLine(0) + ", " + foundAddress.getLocality();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getDistanceFromAddress(Activity activity, String address) {

        LatLng latLng = getLatLngFromAddress(activity, address);

        if (latLng != null) {

            Location loc = new Location("");

            loc.setLatitude(latLng.latitude);
            loc.setLongitude(latLng.longitude);

            return (int) location.distanceTo(loc);
        }

        return 0;
    }

    public static LatLng getLatLngFromAddress(final Activity activity, final String address) {

        try {

            Address foundAddress = new Geocoder(activity).getFromLocationName(address, 1).get(0);

            return new LatLng(foundAddress.getLatitude(), foundAddress.getLongitude());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void initLocation() {

        LocationManager locationManager = (LocationManager) activity.getSystemService(Activity.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isNetworkEnabled) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (isGPSEnabled) {

            if (location == null) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        GPSTracker.location = location;

        ((HomePage) activity).allJobsFragment.onRefresh();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}