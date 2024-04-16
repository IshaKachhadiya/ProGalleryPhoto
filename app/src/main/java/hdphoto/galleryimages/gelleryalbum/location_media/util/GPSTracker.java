package hdphoto.galleryimages.gelleryalbum.location_media.util;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import hdphoto.galleryimages.gelleryalbum.R;


public class GPSTracker extends Service implements LocationListener {
    private static final long MIN_TIME_BW_UPDATES = 60000;
    private static String TAG = "hdphoto.galleryimages.gelleryalbum.util.GPSTracker";
    double latitude;
    Location location;
    protected LocationManager locationManager;
    double longitude;
    private final Context mContext;
    private String provider_info;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean isGPSTrackingEnabled = false;
    int geocoderMaxResults = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String str) {
    }

    @Override
    public void onProviderEnabled(String str) {
    }

    @Override
    public void onStatusChanged(String str, int i, Bundle bundle) {
    }

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public void getLocation() {
        try {
            LocationManager locationManager = (LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE);
            this.locationManager = locationManager;
            this.isGPSEnabled = locationManager.isProviderEnabled("gps");
            boolean isProviderEnabled = this.locationManager.isProviderEnabled("network");
            this.isNetworkEnabled = isProviderEnabled;
            if (this.isGPSEnabled) {
                this.isGPSTrackingEnabled = true;
                Log.d(TAG, "Application use GPS Service");
                this.provider_info = "gps";
            } else if (isProviderEnabled) {
                this.isGPSTrackingEnabled = true;
                Log.d(TAG, "Application use Network State to get GPS coordinates");
                this.provider_info = "network";
            }
            if (this.provider_info.isEmpty()) {
                return;
            }
            if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                this.locationManager.requestLocationUpdates(this.provider_info, MIN_TIME_BW_UPDATES, 10.0f, this);
                LocationManager locationManager2 = this.locationManager;
                if (locationManager2 != null) {
                    this.location = locationManager2.getLastKnownLocation(this.provider_info);
                    updateGPSCoordinates();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Impossible to connect to LocationManager", e);
        }
    }

    public void updateGPSCoordinates() {
        Location location = this.location;
        if (location != null) {
            this.latitude = location.getLatitude();
            this.longitude = this.location.getLongitude();
        }
    }

    public double getLatitude() {
        Location location = this.location;
        if (location != null) {
            this.latitude = location.getLatitude();
        }
        return this.latitude;
    }
    public List<Address> getGeocoderAddress(Context context) {
        if (this.location != null) {
            try {
                return new Geocoder(context, Locale.ENGLISH).getFromLocation(this.latitude, this.longitude, this.geocoderMaxResults);
            } catch (IOException e) {
                Log.e(TAG, "Impossible to connect to Geocoder", e);
                return null;
            }
        }
        return null;
    }
}
