package project.boostcamp.final_project.UI.NewItem;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Util.LocationListener;

public class NewItemMapFragment extends Fragment {

    GoogleMap googleMap;
    static View view;
    ImageView back, ok, toSearch;
    EditText editSearch;
    Geocoder geoCoder;
    LatLng latLng;

    TodoItem item;

    LocationListener locationListener;
    Marker marker;
    MarkerOptions options;

    FragmentChangeListener listener;

    private LatLng changedLatLng;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_new_item_map, container, false);
        } catch (InflateException e) {}


        locationListener = new LocationListener(getActivity());

        item = new TodoItem();
        geoCoder = new Geocoder(getContext());
        com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallback);

        back = (ImageView)view.findViewById(R.id.back);
        ok = (ImageView)view.findViewById(R.id.ok);
        editSearch = (EditText)view.findViewById(R.id.search);
        toSearch = (ImageView)view.findViewById(R.id.toSearch);
        changedLatLng = new LatLng(0,0);

        editSearch.setOnKeyListener(keyListener);
        back.setOnClickListener(clickListener);
        ok.setOnClickListener(clickListener);
        toSearch.setOnClickListener(clickListener);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (FragmentChangeListener) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
    }

    OnMapReadyCallback mapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap Map) {

            googleMap = Map;

            LatLng loc = locationListener.getLocation();
            LatLng baseLatlng = new LatLng(loc.latitude, loc.longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(baseLatlng, 12));

            changedLatLng = baseLatlng; // 초기화

            options = new MarkerOptions();
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            options.position(baseLatlng);
            marker = googleMap.addMarker(options);

            marker.setDraggable(true);
        }
    };

    GoogleMap.OnMarkerDragListener makerDragListener = new GoogleMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {
        }

        @Override
        public void onMarkerDrag(Marker marker) {
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {

            changedLatLng = marker.getPosition();

            marker.setDraggable(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(changedLatLng, 12));
        }
    };

    View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                setSearch();
                return true;
            }
            return false;
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.toSearch :
                    setSearch();
                    break;
                case R.id.back: // pop
                    listener.changeFragment(Constant.MAP, Constant.DETAIL, null);
                    break;
                case R.id.ok : // 마커 현재 위도 경도 가지고 주소 받아서 돌아가기
                    item.setLatitude(changedLatLng.latitude);
                    item.setLongitude(changedLatLng.longitude);
                    item.setAddress(getAddress(changedLatLng));
                    listener.changeFragment(Constant.MAP, Constant.DETAIL, item);
                    break;
            }
        }
    };

    void setSearch(){

        String query = editSearch.getText().toString();

        if(query != null) {
            move(query);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);    //hide keyboard
        }
        else
            Toast.makeText(getActivity(), "검색어를 입력해주세요", Toast.LENGTH_LONG).show(); //todo 이거 안먹는다.
    }

    void move(String query){
        latLng = getLatlng(query);
        if(latLng != null){

            marker.remove();
            options.position(latLng);
            marker = googleMap.addMarker(options);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            changedLatLng = latLng;}
        else
            Toast.makeText(getContext(), "다른 장소를 입력해주세요", Toast.LENGTH_LONG).show();
    }

    public LatLng getLatlng(String address){
        List<Address> list = null;
        try {
            list = geoCoder.getFromLocationName(address, 5);
            latLng = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
        } catch (Exception e) {

        }
        return latLng;
    }

    public String getAddress(LatLng latLng){
        List<Address> addressList = null;

        try {
            addressList = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressList != null) {
            for (Address address : addressList) {
                return  address.getAddressLine(0).toString();
            }
        }
        return null;
    }

}