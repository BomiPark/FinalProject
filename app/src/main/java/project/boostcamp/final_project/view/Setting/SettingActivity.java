package project.boostcamp.final_project.view.Setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

import javax.annotation.Nullable;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;
import project.boostcamp.final_project.model.FolderItem;
import project.boostcamp.final_project.model.Dto.PojoFolderItem;
import project.boostcamp.final_project.model.Dto.PojoTodoItem;
import project.boostcamp.final_project.model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.util.BindingService;
import project.boostcamp.final_project.util.RealmHelper;
import project.boostcamp.final_project.util.SharedPreferencesService;

import static project.boostcamp.final_project.util.SharedPreferencesService.EMAIL;
import static project.boostcamp.final_project.util.SharedPreferencesService.IS_ALARM;
import static project.boostcamp.final_project.util.SharedPreferencesService.RADIUS;

public class SettingActivity extends BaseActivity {

    private Button on, off, btn_license;
    private Switch swich;
    private SeekBar radiusBar;
    private TextView radiusValue;

    private AlertDialog.Builder dialog;

    private boolean alarm;
    private int radius;

    private Realm realm;
    private RealmResults<TodoItem> todoRealmResults;
    private RealmResults<FolderItem> folderRealmResults;
    private DatabaseReference databaseRef;
    private TodoItem todoItem = new TodoItem();
    private FolderItem folderItem = new FolderItem();
    private PojoTodoItem pojoTodoItem;
    private PojoFolderItem pojoFolderItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();

    }

    protected void onDestroy(){
        super.onDestroy();
        saveStatus();
    }

    void setView() {

        alarm = SharedPreferencesService.getInstance().getPrefBooleanData(IS_ALARM);

        if(alarm) {
            on.setTextColor(getResources().getColor(R.color.click_back));
            off.setTextColor(getResources().getColor(R.color.gray));
            swich.setChecked(true);
        }
        else{
            on.setTextColor(getResources().getColor(R.color.gray));
            off.setTextColor(getResources().getColor(R.color.click_back));
            swich.setChecked(false);
        }

        radius = SharedPreferencesService.getInstance().getPrefIntData(RADIUS);
        if(radius != 0)
            radiusValue.setText(radius + "M");

    }

    void saveStatus(){

        SharedPreferencesService.getInstance().setPrefData(IS_ALARM, alarm);
        SharedPreferencesService.getInstance().setPrefIntData(RADIUS, radius);

        Toasty.info(getApplicationContext(), getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();

        if(alarm)
            BindingService.getInstance(getApplicationContext()).startService();
        else
            BindingService.getInstance(getApplicationContext()).stopService();
    }

    View.OnClickListener clickListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.on :
                    setAlarmOn(true);
                    break;
                case R.id.off :
                    setAlarmOn(false);
                    break;
                case R.id.btn_license :
                    startActivity(new Intent(SettingActivity.this, LicenseActivity.class));
                    break;
            }
        }
    };

    void setAlarmOn(boolean on){

        alarm = on;
        if(on){
            swich.setChecked(true);
            setTextColor(true);
        }
        else{
            swich.setChecked(false);
            setTextColor(false);
        }
    }

    void setTextColor(boolean alarmOn){
        if(alarmOn){
            on.setTextColor(getResources().getColor(R.color.click_back));
            off.setTextColor(getResources().getColor(R.color.gray));
        }
        else{
            on.setTextColor(getResources().getColor(R.color.gray));
            off.setTextColor(getResources().getColor(R.color.click_back));
        }
    }

    public void settingClick(View view){
        switch(view.getId()){

            case R.id.to_backup : // 백업

                backupDialogBox();

                break;
            case R.id.get_data :

                restoreDialogBox();

                break;

        }
    }

    void backupDialogBox(){
        dialog = new AlertDialog.Builder(SettingActivity.this);
        dialog.setMessage(R.string.backup_message)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        backupData();

                    }
                })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), R.string.canceled, Toast.LENGTH_LONG).show();
                            }
                        });
        AlertDialog dialogCreate = dialog.create();
        dialogCreate.show();
    }

    void restoreDialogBox(){
        dialog = new AlertDialog.Builder(SettingActivity.this);
        dialog.setMessage(R.string.restore_message)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        restoreData();

                    }
                })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),R.string.canceled, Toast.LENGTH_LONG).show();
                            }
                        });
        AlertDialog dialogCreate = dialog.create();
        dialogCreate.show();
    }


    void backupData(){
        todoRealmResults = RealmHelper.getInstance(this).where(TodoItem.class).findAll();
        List<PojoTodoItem> todoList = ImmutableList.copyOf(Collections2.transform(todoRealmResults, new Function<TodoItem, PojoTodoItem>() {
            @Nullable
            @Override
            public PojoTodoItem apply(@Nullable TodoItem input) {
                return TodoItem.toPojo(input);
            }
        }));

        folderRealmResults = RealmHelper.getInstance(this).where(FolderItem.class).findAll();
        List<PojoFolderItem> folderList = ImmutableList.copyOf(Collections2.transform(folderRealmResults, new Function<FolderItem, PojoFolderItem>() {
            @Nullable
            @Override
            public PojoFolderItem apply(@Nullable FolderItem input) {
                return FolderItem.toPojo(input);
            }
        }));

        databaseRef.child(SharedPreferencesService.getInstance().getPrefStringData(EMAIL)).child("todo").setValue(todoList);
        databaseRef.child(SharedPreferencesService.getInstance().getPrefStringData(EMAIL)).child("folder").setValue(folderList);

        Toasty.info(getApplicationContext(), getResources().getString(R.string.backup), Toast.LENGTH_LONG).show();
    }

    void restoreData(){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();


        databaseRef.child(SharedPreferencesService.getInstance().getPrefStringData(EMAIL)).child("todo").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        realm.beginTransaction();

                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                            pojoTodoItem = messageSnapshot.getValue(PojoTodoItem.class);
                            todoItem = PojoTodoItem.toRealm(pojoTodoItem);
                            realm.copyToRealm(todoItem);
                        }

                        realm.commitTransaction();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        databaseRef.child(SharedPreferencesService.getInstance().getPrefStringData(EMAIL)).child("folder").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        realm.beginTransaction();

                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                            pojoFolderItem = messageSnapshot.getValue(PojoFolderItem.class);
                            folderItem = PojoFolderItem.toRealm(pojoFolderItem);
                            realm.copyToRealm(folderItem);

                        }
                        realm.commitTransaction();
                        Toasty.info(getApplicationContext(), getResources().getString(R.string.restore), Toast.LENGTH_LONG).show();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    void init(){

        on = (Button)findViewById(R.id.on);
        off = (Button)findViewById(R.id.off);
        btn_license = (Button)findViewById(R.id.btn_license);
        on.setOnClickListener(clickListener);
        off.setOnClickListener(clickListener);
        btn_license.setOnClickListener(clickListener);
        swich = (Switch)findViewById(R.id.switchBar);
        radiusBar = (SeekBar)findViewById(R.id.radiusBar);
        radiusValue = (TextView)findViewById(R.id.radiusValue);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.gray));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferencesService.getInstance().load(getApplicationContext());

        setView();

        realm = RealmHelper.getInstance(this);
        databaseRef = FirebaseDatabase.getInstance().getReference();

        swich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setAlarmOn(true);
                }
                else{
                    setAlarmOn(false);
                }
            }
        });

        radiusBar.setProgress(SharedPreferencesService.getInstance().getPrefIntData(RADIUS));

        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0)
                    radiusValue.setText(1 + " M");
                radiusValue.setText(progress + " M");
                radius = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        saveStatus();
        finish();

        return super.onOptionsItemSelected(item);
    }

}