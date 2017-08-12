package project.boostcamp.final_project.UI.TodoItem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import project.boostcamp.final_project.Interface.RecyclerItemClickListener;
import project.boostcamp.final_project.Adapter.TodoItemAdapter;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.SettingActivity;
import project.boostcamp.final_project.UI.NewItem.NewItemActivity;
import project.boostcamp.final_project.Util.CheckDialog;

import static project.boostcamp.final_project.UI.SettingActivity.geofencingService;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RealmResults<TodoItem> itemList;
    RecyclerView recyclerView;
    TodoItemAdapter adapter;
    AlertDialog.Builder dialog;
    DrawerLayout drawer;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewItemActivity.class);
                startActivity(intent);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();

    }

    void init(){

        setData();
        dialog = new AlertDialog.Builder(MainActivity.this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        adapter = new TodoItemAdapter(this, itemList, R.layout.todo_item);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, ItemDetailActivity.class); // 선택한애정보가지고갈거야
                intent.putExtra("id", itemList.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(R.string.dialog_title).setMessage(itemList.get(position).getTodo())
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {

                                        TodoItem item = itemList.get(position);
                                        item.deleteFromRealm();
                                    }
                                });
                                adapter.notifyDataSetChanged();
                                // geofencingService.updateGeofence(); //todo 서비스 생성 위치 바꾸고 활성화
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // negative button logic
                                    }
                                });
                AlertDialog dialogCreate = dialog.create();
                dialogCreate.show();
            }
        }));
    }

    void setData(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();

        itemList = realm.where(TodoItem.class).equalTo("isCompleted", false).findAll();

    }

    @Override
    public void onResume(){
        super.onResume();
        updateData();
    }

    void updateData(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) { //todo 이렇게안해두되긴하는거같당!
                itemList = null;
                itemList = realm.where(TodoItem.class).equalTo("isCompleted", false).findAll();
                //geofencingService.updateGeofence();  //todo 서비스 생성 위치 바꾸고 활성화
                adapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
