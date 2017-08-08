package project.boostcamp.final_project.Util;

import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class GeoCodingService { //todo 안쓰면 삭제하기

    Geocoder geoCoder;

    public GeoCodingService(Geocoder geo){
        geoCoder = geo;
    }

    public LatLng getLatLng(String address){
        List<Address> list = null;
        try {
            list = geoCoder.getFromLocationName(address, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
    }

    public String getAddress(LatLng latLng){
        List<Address> list = null;
        try {
            list = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.get(0).getAddressLine(0).toString();
    }
}
