package project.boostcamp.final_project.View;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import project.boostcamp.final_project.Interface.FragmentChangeListener;
import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Model.SearchItem;
import project.boostcamp.final_project.R;

import static android.R.attr.id;

public class NewItemActivity extends AppCompatActivity implements FragmentChangeListener {

    FrameLayout container;
    FragmentManager fragmentManager;

    NewItemDetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        init();
    }

    public static Fragment newInstance(int now, int to){

        Fragment fragment;
        if(now == Constant.DETAIL && to == Constant.SEARCH) {
            fragment = new NewItemSearchFragment();
        }
        else
            fragment = new NewItemSearchFragment();

        return fragment;
    }

    void init(){
        container = (FrameLayout)findViewById(R.id.container);
        fragmentManager = getSupportFragmentManager();

        detailFragment = new NewItemDetailFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();

    }

    @Override
    public void changeFragment(int now, int to, SearchItem item) {

        if(now == Constant.DETAIL){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,newInstance(now, to)).addToBackStack(null).commit(); // detail 저장
        }
        else if(now == Constant.SEARCH && to == Constant.DETAIL) {
            getSupportFragmentManager().popBackStack();
            Toast.makeText(this, " " + item.getTitle() , Toast.LENGTH_LONG).show();
        }
    }
}
