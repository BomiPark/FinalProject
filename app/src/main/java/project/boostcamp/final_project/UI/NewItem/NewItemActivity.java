package project.boostcamp.final_project.UI.NewItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.gms.maps.model.LatLng;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Util.LocationService;

import static project.boostcamp.final_project.Model.Constant.SAVE;

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
        } else if( to == Constant.DETAIL && item != null) {
            getSupportFragmentManager().popBackStack();
            todoItem = item;
        } else if(to == Constant.END){ // 아이템 저장안하고 종료하는 경우
            finish();
        } else if(to == SAVE){
            todoItem = item;
            setData(item);
            finish();
        }
    }

    @Override
    public int setStatus(int now) {
        STATUS = now;
        return STATUS;
    }

    void setData(TodoItem item){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();
        int nextID =0;

        if(realm.where(TodoItem.class).findAll().size() > 0)
            nextID = realm.where(TodoItem.class).findAll().last().getId() + 1; // 가장 마지막에 저장된 id 값

        realm.beginTransaction();
        item.setId(nextID);
        realm.copyToRealm(item);

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
