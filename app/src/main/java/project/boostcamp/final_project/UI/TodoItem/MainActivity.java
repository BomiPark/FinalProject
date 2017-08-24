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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;
import project.boostcamp.final_project.Adapter.FolderItemAdapter;
import project.boostcamp.final_project.Interface.RecyclerItemClickListener;
import project.boostcamp.final_project.Adapter.TodoItemAdapter;
import project.boostcamp.final_project.Interface.TodoCheckClickListener;
import project.boostcamp.final_project.Interface.TodoMainClickListener;
import project.boostcamp.final_project.Model.FolderItem;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.Setting.BaseActivity;
import project.boostcamp.final_project.UI.Setting.ProfileActivity;
import project.boostcamp.final_project.UI.Setting.SettingActivity;
import project.boostcamp.final_project.UI.NewItem.NewItemActivity;
import project.boostcamp.final_project.Util.BindingService;
import project.boostcamp.final_project.Util.GeofencingService;
import project.boostcamp.final_project.Util.RealmHelper;
import project.boostcamp.final_project.Util.SharedPreferencesService;

import static project.boostcamp.final_project.Util.SharedPreferencesService.PROP_IMG;
import static project.boostcamp.final_project.Util.SharedPreferencesService.PROP_NAME;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    private RealmResults<TodoItem> itemList;
    private RealmResults<FolderItem> folderList;
    private List<String> spinnerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Spinner spinner;

    private TodoItemAdapter todoItemAdapter;
    private ArrayAdapter<String> spinnerAdapter;
    private AlertDialog.Builder dialog;

    private DrawerLayout drawer;
    private RecyclerView drawer_list;
    private FolderItemAdapter folderItemAdapter;

    private ImageView writeIcon, nav_img; // navigation drawer 관련 뷰
    private TextView nav_name;

    private Realm realm;
    private TodoItem todo = new TodoItem();
    private FolderItem folder = new FolderItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.gray));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewItemActivity.class);
                intent.putExtra("id", -1);
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

        Toasty.Config.getInstance()
                .setSuccessColor(getResources().getColor(R.color.blue)) // optional
                .apply(); // required

    }

    @Override
    public void onResume() {
        super.onResume();
        setProfile();
        updateData();

    }

    void init() {

        initData();
        dialog = new AlertDialog.Builder(MainActivity.this);
        drawer_list = (RecyclerView) findViewById(R.id.drawer_list);
        nav_name = (TextView) findViewById(R.id.nav_name);
        nav_img = (ImageView) findViewById(R.id.nav_img);
        writeIcon = (ImageView) findViewById(R.id.writeIcon);
        spinner = (Spinner) findViewById(R.id.main_spinner);

        writeIcon.setOnClickListener(clickListener);
        nav_name.setOnClickListener(clickListener);
        nav_img.setOnClickListener(clickListener);

        setSpinner();

        setFolderItemList();

        setProfile();

        setTodoItemList();
    }

    void setSpinner() {

        spinner.setOnItemSelectedListener(this);

        spinnerList = FolderItem.getFolderList(realm.where(FolderItem.class).findAll().sort("id"));

        spinnerList.add(0, getResources().getString(R.string.all));
        spinnerList.add(getResources().getString(R.string.completed_no));
        spinnerList.add(getResources().getString(R.string.completed_ok));

        spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, spinnerList);

        spinner.setAdapter(spinnerAdapter);

    }

    void setFolderItemList() {
        folderItemAdapter = new FolderItemAdapter(this, folderList, R.layout.item_folder);
        drawer_list.setAdapter(folderItemAdapter);
        drawer_list.setLayoutManager(new LinearLayoutManager(this));
        drawer_list.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, drawer_list, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                if (position == 0)
                    itemList = realm.where(TodoItem.class).findAll().sort("id");
                else {
                    itemList = realm.where(TodoItem.class).equalTo("folder", spinnerList.get(position)).findAll().sort("id");
                }

                spinner.setSelection(position + 1);
                setTodoItemList();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                removeFolderDialogBox(position);
                setSpinner();
            }
        }));

    }

    void setProfile() {

        SharedPreferencesService.getInstance().load(getApplicationContext());

        int prop_img = SharedPreferencesService.getInstance().getPrefIntData(PROP_IMG);
        nav_img.setImageResource(prop_img);
        String prop_name = SharedPreferencesService.getInstance().getPrefStringData(PROP_NAME);
        if (prop_name != null)
            nav_name.setText(prop_name);
    }

    void setTodoItemList() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        todoItemAdapter = new TodoItemAdapter(this, itemList, R.layout.item_todo, new TodoMainClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                Intent intent = new Intent(MainActivity.this, ItemDetailActivity.class); // 선택한애정보가지고갈거야
                intent.putExtra("id", itemList.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, final int position) {
                removeItemDialogBox(position);
            }
        }, new TodoCheckClickListener() {
            @Override
            public void onClick(final int position) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        todo = realm.where(TodoItem.class).equalTo("id", itemList.get(position).getId()).findFirst();
                        todo.setCompleted(!todo.isCompleted());
                        todoItemAdapter.notifyDataSetChanged();
                    }
                });
                if(!todo.isCompleted())
                    BindingService.getInstance(getApplicationContext()).upDateService();
            }
        });
        recyclerView.setAdapter(todoItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void initData() {

        realm = RealmHelper.getInstance(this);

        itemList = realm.where(TodoItem.class).findAll().sort("id");
        folderList = realm.where(FolderItem.class).findAll().sort("id");

    }

    void updateData() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                itemList = null;
                itemList = realm.where(TodoItem.class).findAll().sort("id");
            }
        });

        todoItemAdapter.notifyDataSetChanged();
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

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void removeItemDialogBox(final int position) {
        dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(R.string.dialog_title).setMessage(" \n'" + itemList.get(position).getTodo() + "' 를 삭제하시겠습니까")
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
                        BindingService.getInstance(getApplicationContext()).upDateService();
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

    void removeFolderDialogBox(final int position) {
        dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(R.string.dialog_folder).setMessage(" \n'" + folderList.get(position).getFolder() + "' 를 삭제하시겠습니까")
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {

                                FolderItem item = folderList.get(position);
                                item.deleteFromRealm();
                            }
                        });
                        folderItemAdapter.notifyDataSetChanged();
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

    void saveFolder(String folderName) {

        if (!RealmHelper.isSmameName(folderName)) {

            realm.beginTransaction();
            folder.setId(RealmHelper.getNextFolderId());
            folder.setFolder(folderName);
            realm.copyToRealm(folder);

            folderList = realm.where(FolderItem.class).findAll().sort("id");
            folderItemAdapter.notifyDataSetChanged();

            realm.commitTransaction();

            Toast.makeText(getApplicationContext(), R.string.saved, Toast.LENGTH_SHORT).show();
            setSpinner();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.same_folder), Toast.LENGTH_SHORT).show();
        }
    }

    View.OnClickListener clickListener = new ImageView.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.writeIcon:
                    OpenFolderDialogBox();
                    break;
                default:
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    break;
            }
        }
    };


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0)
            itemList = realm.where(TodoItem.class).findAll().sort("id");
        else if (spinnerList.get(position).equals(getResources().getString(R.string.completed_no)))
            itemList = realm.where(TodoItem.class).equalTo("isCompleted", false).findAll().sort("id");
        else if (spinnerList.get(position).equals(getResources().getString(R.string.completed_ok)))
            itemList = realm.where(TodoItem.class).equalTo("isCompleted", true).findAll().sort("id");
        else {
            itemList = realm.where(TodoItem.class).equalTo("folder", spinnerList.get(position)).findAll().sort("id");
        }

        setTodoItemList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        itemList = realm.where(TodoItem.class).findAll();
        todoItemAdapter.notifyDataSetChanged();

    }
}
