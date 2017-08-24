package project.boostcamp.final_project.UI.NewItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
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

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Util.LocationService;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource;

public class NewItemMapFragment extends Fragment {

    private static View view;
    private GoogleMap googleMap;
    private ImageView back, ok, toSearch;
    private Geocoder geoCoder;
    private PlaceAutocompleteFragment autocompleteFragment;

    private TodoItem item;
    private Marker marker;
    private MarkerOptions options;

    private FragmentChangeListener listener;
    private LatLng changedLatLng;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_new_item_map, container, false);
        } catch (InflateException e) {}

        Toasty.info(getActivity(), getResources().getString(R.string.move_marker), Toast.LENGTH_SHORT).show();

        autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setBoundsBias(new LatLngBounds( //todo 하드코딩
                new LatLng(33.2572573, 126.0331448),
                new LatLng(38.6084671, 129.3620022)
        ));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                setSearch(place);
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getContext(), "다른 장소를 입력해주세요", Toast.LENGTH_LONG).show();
            }
        });

        item = new TodoItem();
        geoCoder = new Geocoder(getContext());
        com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallback);

        back = (ImageView)view.findViewById(R.id.back);
        ok = (ImageView)view.findViewById(R.id.ok);
        changedLatLng = new LatLng(0,0);

        back.setOnClickListener(clickListener);
        ok.setOnClickListener(clickListener);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (FragmentChangeListener) context;
        listener.setStatus(Constant.MAP);

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
            googleMap.clear();

            LatLng loc = listener.getCurrentLocation();
            LatLng baseLatlng = new LatLng(loc.latitude, loc.longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(baseLatlng, 13));

            changedLatLng = baseLatlng; // 초기화

            options = new MarkerOptions();

            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_current)); // 마커 위치 todo 글씨 안보이는 것 처리
            options.position(baseLatlng);
            options.draggable(true);
            marker = googleMap.addMarker(options);

            googleMap.setOnMarkerDragListener(makerDragListener);
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

            googleMap.clear();

            changedLatLng = marker.getPosition();

            options.icon(fromResource(R.drawable.marker_select)); // 마커 위치
            options.position(changedLatLng);
            googleMap.addMarker(options);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(changedLatLng, 13));
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
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


    void setSearch(Place place){

        item.setAddress(place.getAddress().toString());
        move(place.getLatLng());

    }

    void move(LatLng latLng){

        if(latLng != null){

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

            marker.remove();
            options.position(latLng);
            options.icon(fromResource(R.drawable.marker_select));
            marker = googleMap.addMarker(options);

            changedLatLng = latLng;}
        else
            Toast.makeText(getContext(), "다른 장소를 입력해주세요", Toast.LENGTH_LONG).show();
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