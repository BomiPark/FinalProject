package project.boostcamp.final_project.UI.NewItem;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Util.LocationService;
import project.boostcamp.final_project.Util.RealmHelper;

import static android.media.CamcorderProfile.get;
import static project.boostcamp.final_project.Model.Constant.SAVE;
import static project.boostcamp.final_project.Util.BindingService.geofencingService;

public class NewItemActivity extends AppCompatActivity implements FragmentChangeListener {

    FrameLayout container;
    FragmentManager fragmentManager;
    NewItemDetailFragment detailFragment;
    LocationService locationService;

    Realm realm;
    TodoItem todoItem;

    int STATUS = Constant.DETAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        init();
    }

    public static Fragment newInstance(int now, int to){

        Fragment fragment = null;

        if(now == Constant.DETAIL && to == Constant.SEARCH) {
            fragment = new NewItemSearchFragment();
        }
        else if(now == Constant.DETAIL && to == Constant.MAP) {
            fragment = new NewItemMapFragment();
        }
        return fragment;
    }

    void init(){

        container = (FrameLayout)findViewById(R.id.container);
        fragmentManager = getSupportFragmentManager();
        todoItem = new TodoItem();
        locationService = new LocationService(this);

        STATUS = Constant.DETAIL;
        detailFragment = new NewItemDetailFragment();

        realm = RealmHelper.getInstance(this);

        Log.e("newItem", getIntent().getExtras().getInt("id")+"" ); //todo 로그

        if( getIntent().getExtras().getInt("id") != -1)
            todoItem = realm.where(TodoItem.class).equalTo("id", getIntent().getExtras().getInt("id")).findFirst();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        locationService.stopLocationService();
    }

    @Override
    public void changeFragment(int now, int to, TodoItem item) {

        if(now == Constant.DETAIL && to != Constant.END && to != Constant.SAVE){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,newInstance(now, to)).addToBackStack(null).commit(); // detail 저장
        } else if(to == Constant.DETAIL && item == null) {
            getSupportFragmentManager().popBackStack();
        } else if( to == Constant.DETAIL) {
            getSupportFragmentManager().popBackStack();
            todoItem = item;
        } else if(to == Constant.END){ // 아이템 저장안하고 종료하는 경우
            finish();
        } else if(to == SAVE){
            todoItem = item;
            setData(item);
            if(geofencingService != null)
                geofencingService.updateGeofence();
            finish();
        }
    }

    @Override
    public int setStatus(int now) {
        STATUS = now;
        return STATUS;
    }

    void setData(TodoItem item){ //todo 그냥 저장하는 경우와 수정하는 경우 구분하여 처리하기

        realm.beginTransaction();

        if(item.getId()  == 0) {
            item.setId(RealmHelper.getNextTodoId());
            realm.copyToRealm(item);
        }

        realm.commitTransaction();

    }


    @Override
    public TodoItem getCurrentItem() {
        return todoItem;
    }

    @Override
    public LatLng getCurrentLocation() {
        return locationService.getLocation();
    }

}
