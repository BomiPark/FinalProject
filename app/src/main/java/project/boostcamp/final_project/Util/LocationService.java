package project.boostcamp.final_project.Util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class LocationService {

    private LocationManager locManager;
    private Context context;
    LatLng currentLoc;

    public LocationService(Context context) {

        this.context = context;
        currentLoc = new LatLng(0,0);
        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        startLocationService();
        Log.e("location", "생성");
    }

    public void startLocationService() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location lastLocation = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (lastLocation != null) {
            Double latitude = lastLocation.getLatitude();
            Double longitude = lastLocation.getLongitude();
        }

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

    public void stopLocationService() {

        locManager.removeUpdates(locationListener);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            Log.e("locationService55", currentLoc.latitude+"");

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

        if(currentLoc != null)
            return currentLoc;
        else
            return new LatLng(0,0);
    }

}
