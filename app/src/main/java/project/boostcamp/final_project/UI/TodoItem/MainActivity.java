package project.boostcamp.final_project.UI.TodoItem;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import project.boostcamp.final_project.Adapter.FolderItemAdapter;
import project.boostcamp.final_project.Interface.RecyclerItemClickListener;
import project.boostcamp.final_project.Adapter.TodoItemAdapter;
import project.boostcamp.final_project.Model.FolderItem;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.Setting.SettingActivity;
import project.boostcamp.final_project.UI.NewItem.NewItemActivity;

import static project.boostcamp.final_project.UI.Setting.SettingActivity.geofencingService;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RealmResults<TodoItem> itemList;
    RealmResults<FolderItem> folderList;
    RecyclerView recyclerView;
    TodoItemAdapter todoItemAdapter;
    AlertDialog.Builder dialog;

    DrawerLayout drawer;
    RecyclerView drawer_list;
    FolderItemAdapter folderItemAdapter;

    ImageView writeIcon;

    Realm realm;
    FolderItem folder = new FolderItem();

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

        initData();
        dialog = new AlertDialog.Builder(MainActivity.this);
        drawer_list = (RecyclerView) findViewById(R.id.drawer_list);
        writeIcon = (ImageView)findViewById(R.id.writeIcon);

        writeIcon.setOnClickListener(clickListener);

        setFolderItemList();

        setTodoItemList();
    }

    void setFolderItemList(){
        folderItemAdapter = new FolderItemAdapter(this, folderList, R.layout.item_folder);
        drawer_list.setAdapter(folderItemAdapter);
        drawer_list.setLayoutManager(new LinearLayoutManager(this));
        drawer_list.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, drawer_list, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout); //todo 폴더리스트로이동
                drawer.closeDrawer(GravityCompat.START);
                Toast.makeText(MainActivity.this, folderList.get(position).getFolder()+"리스트로이동, 아직구현x" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, FolderItemActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

    }

    void setTodoItemList(){
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        todoItemAdapter = new TodoItemAdapter(this, itemList, R.layout.item_todo);
        recyclerView.setAdapter(todoItemAdapter);
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
                removeItemDialogBox(position);
            }
        }));
    }

    void initData(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();

        itemList = realm.where(TodoItem.class).equalTo("isCompleted", false).findAll();
        folderList = realm.where(FolderItem.class).findAll();

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
                todoItemAdapter.notifyDataSetChanged();
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

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void removeItemDialogBox(final int position){
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
                        todoItemAdapter.notifyDataSetChanged();
                        if(geofencingService != null)
                            geofencingService.updateGeofence();
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

    void OpenFolderDialogBox() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_add_folder, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add New FolderItem");
        alert.setView(promptView);

        final EditText input = (EditText) promptView.findViewById(R.id.add_folder);

        input.requestFocus();
        input.setTextColor(Color.BLACK);

        alert.setPositiveButton(R.string.ok_eng, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String newFolderText = input.getText().toString();
                        if (newFolderText.equals("")) {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.input_folder), Toast.LENGTH_LONG).show();
                            OpenFolderDialogBox();
                        } else {
                            saveFolder(newFolderText);
                            Toast.makeText(getApplicationContext(), R.string.saved, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        alert.setNegativeButton(R.string.cancel_eng,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getApplicationContext(),
                                "CANCEL", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alertDialog = alert.create();

        alertDialog.show();

    }

    void saveFolder(String folderName){

        int nextID =0;

        if(realm.where(FolderItem.class).findAll().size() > 0)
            nextID = realm.where(FolderItem.class).findAll().last().getId() + 1; // 가장 마지막에 저장된 id 값

        realm.beginTransaction();
        folder.setId(nextID);
        folder.setFolder(folderName);
        realm.copyToRealm(folder);

        realm.commitTransaction();

        folderList= realm.where(FolderItem.class).findAll();
        folderItemAdapter.notifyDataSetChanged();
    }

    View.OnClickListener clickListener = new ImageView.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.writeIcon :
                    OpenFolderDialogBox();
                    break;
            }
        }
    };


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
