package project.boostcamp.final_project.listener;

import com.google.android.gms.maps.model.LatLng;

import project.boostcamp.final_project.model.TodoItem;


public interface FragmentChangeListener {

    void changeFragment(int now, int to, TodoItem item);

    int setStatus(int now);

    int getStatus();

    TodoItem getCurrentItem();

    LatLng getCurrentLocation();


}
