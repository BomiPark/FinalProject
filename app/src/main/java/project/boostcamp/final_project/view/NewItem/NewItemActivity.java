package project.boostcamp.final_project.view.NewItem;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.maps.model.LatLng;

import io.realm.Realm;
import project.boostcamp.final_project.listener.FragmentChangeListener;
import project.boostcamp.final_project.model.Constant;
import project.boostcamp.final_project.model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.view.Setting.BaseActivity;
import project.boostcamp.final_project.util.BindingService;
import project.boostcamp.final_project.util.LocationService;
import project.boostcamp.final_project.util.RealmHelper;

import static project.boostcamp.final_project.model.Constant.SAVE;

public class NewItemActivity extends BaseActivity implements FragmentChangeListener {

    private FrameLayout container;
    private FragmentManager fragmentManager;
    private NewItemDetailFragment detailFragment;
    private LocationService locationService;

    private Realm realm;
    private TodoItem todoItem;

    private int STATUS = Constant.DETAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        init();
    }

    void init(){

        container = (FrameLayout)findViewById(R.id.container);
        fragmentManager = getSupportFragmentManager();
        todoItem = new TodoItem();
        locationService = new LocationService(this);

        STATUS = Constant.DETAIL;
        detailFragment = new NewItemDetailFragment();

        realm = RealmHelper.getInstance(this);

        if( getIntent().getExtras().getInt("id") != -1)
            todoItem = realm.where(TodoItem.class).equalTo("id", getIntent().getExtras().getInt("id")).findFirst();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        RealmHelper.getInstance(this).close();
        locationService.stopLocationService();
    }

    @Override
    public void changeFragment(int now, int to, TodoItem item) {

        if(now == Constant.DETAIL && to != Constant.END && to != Constant.SAVE){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, NewItemBaseFragment.newInstance(now, to)).addToBackStack(null).commit(); // detail 저장
        } else if(to == Constant.DETAIL && item == null) {
            getSupportFragmentManager().popBackStack();
        } else if(to == Constant.DETAIL) {
            getSupportFragmentManager().popBackStack();
            realm.beginTransaction();
            todoItem.setInfo(item);
            realm.commitTransaction();
        } else if(to == Constant.END){ // 아이템 저장안하고 종료하는 경우
            finish();
        } else if(to == SAVE){
            realm.beginTransaction();
            todoItem.setInfo(item);
            realm.commitTransaction();
            setData(item);
            BindingService.getInstance(getApplicationContext()).upDateService(); //todo set
            finish();
        }
    }

    @Override
    public int setStatus(int now) {
        STATUS = now;
        return STATUS;
    }

    @Override
    public int getStatus() {
        return STATUS;
    }

    void setData(TodoItem item){

        realm.beginTransaction();

        if(item.getId() == -1) { // 아이템을 새로 생성하는 경우 전달된 id는 -1
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (getStatus()){
            case Constant.DETAIL :
                changeFragment(Constant.DETAIL, Constant.END, null);
                break;
            case Constant.MAP :
                changeFragment(Constant.MAP, Constant.DETAIL, null);
                break;
            case Constant.SEARCH :
                changeFragment(Constant.SEARCH, Constant.DETAIL, null);
        }

        return super.onOptionsItemSelected(item);
    }

}
