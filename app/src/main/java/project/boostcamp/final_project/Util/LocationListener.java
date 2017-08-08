package project.boostcamp.final_project.Util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

public class LocationListener {

    private LocationManager locManager;
    private Context context;
    LatLng currentLoc;
    int set = 0;

    public LocationListener(Context context) {

        this.context = context;
        startLocationService();

    }

    private void startLocationService() {
        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location lastLocation = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (lastLocation != null) {
            Double latitude = lastLocation.getLatitude();
            Double longitude = lastLocation.getLongitude();
        }

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

    private void stopLocationService() {
        set = 0;

        locManager.removeUpdates(locationListener);
    }

    android.location.LocationListener locationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            set = 1;
            currentLoc = new LatLng(location.getLatitude(), location.getLongitude());

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
        @Override
        public void onProviderEnabled(String s) {

        }
        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public LatLng getLocation(){

        if(set == 1) {
            stopLocationService();
            return currentLoc;
        }        else
            return new LatLng(37.5804580, 126.8895390);
    }

}
