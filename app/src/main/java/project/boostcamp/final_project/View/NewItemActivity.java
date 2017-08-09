package project.boostcamp.final_project.View;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Model.SearchItem;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;

import static project.boostcamp.final_project.Model.Constant.SAVE;

public class NewItemActivity extends AppCompatActivity implements FragmentChangeListener {

    FrameLayout container;
    FragmentManager fragmentManager;
    NewItemDetailFragment detailFragment;

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

        STATUS = Constant.DETAIL;
        detailFragment = new NewItemDetailFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();

    }

    @Override
    public void changeFragment(int now, int to, TodoItem item) {

        if(now == Constant.DETAIL && to != Constant.END && to != Constant.SAVE){ //todo case 정리 하기
            getSupportFragmentManager().beginTransaction().replace(R.id.container,newInstance(now, to)).addToBackStack(null).commit(); // detail 저장
        } else if(to == Constant.DETAIL && item == null) {
            getSupportFragmentManager().popBackStack();
        } else if( to == Constant.DETAIL && item != null) {
            getSupportFragmentManager().popBackStack();
            todoItem = item;
        } else if(to == Constant.END){
            finish();
        } else if(to == SAVE){
            todoItem = item;
            setData(item);
            finish();
        }
    }

    void setData(TodoItem item){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.copyToRealm(item);

        realm.commitTransaction();

    }

    @Override
    public int setStatus(int now) {

        STATUS = now;

        return 0;
    }

    @Override
    public TodoItem getCurrentItem() {
        return todoItem;
    }

}
