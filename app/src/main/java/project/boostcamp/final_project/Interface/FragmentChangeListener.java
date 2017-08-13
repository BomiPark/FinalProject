package project.boostcamp.final_project.Interface;

import com.google.android.gms.maps.model.LatLng;

import project.boostcamp.final_project.Model.TodoItem;

/**
 * Created by qkrqh on 2017-07-20.
 */

public interface FragmentChangeListener {

    void changeFragment(int now, int to, TodoItem item);

    int setStatus(int now);

    TodoItem getCurrentItem();

    LatLng getCurrentLocation();


}
