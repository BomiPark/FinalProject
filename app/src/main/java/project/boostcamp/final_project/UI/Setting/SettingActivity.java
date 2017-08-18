package project.boostcamp.final_project.UI.Setting;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

import io.realm.Realm;
import io.realm.RealmResults;
import project.boostcamp.final_project.Model.FolderItem;
import project.boostcamp.final_project.Model.Dto.PojoFolderItem;
import project.boostcamp.final_project.Model.Dto.PojoTodoItem;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Util.RealmHelper;
import project.boostcamp.final_project.Util.SharedPreferencesService;

import static project.boostcamp.final_project.Util.SharedPreferencesService.EMAIL;


public class SettingActivity extends AppCompatActivity {

    private Button on, off;
    private ImageView back, ok;
    private Switch swich;
    private SeekBar radiusBar;
    private TextView radiusValue;

    private boolean isAlarm;
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

    void setView() { //todo 세팅

    }

    void saveStatus(){ //todo 알람 여부 반경

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
                case R.id.back :
                    finish();
                    break;
                case R.id.ok :
                    saveStatus();
                    break;
            }
        }
    };

    void setAlarmOn(boolean on){
        if(on == true){ //todo 추가추가
            swich.setChecked(true);
            setTextColor(true);

        }
        else{
            swich.setChecked(false);
            setTextColor(false);
        }
    }

    void setTextColor(boolean alarmOn){
        if(alarmOn == true){
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
            case R.id.to_login :
                startActivity(new Intent(this, PermissionActivity.class));
                break;
            case R.id.to_backup : // 백업
                Toast.makeText(getApplicationContext(), "backup", Toast.LENGTH_LONG).show();

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

                break;
            case R.id.get_data :

                Log.e("uid", SharedPreferencesService.getInstance().getPrefStringData(EMAIL));

                realm.beginTransaction();

                realm.deleteAll();

                databaseRef.child(SharedPreferencesService.getInstance().getPrefStringData(EMAIL)).child("todo").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                    pojoTodoItem = messageSnapshot.getValue(PojoTodoItem.class);
                                    todoItem = PojoTodoItem.toRealm(pojoTodoItem);
                                    realm.copyToRealm(todoItem);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                databaseRef.child(SharedPreferencesService.getInstance().getPrefStringData(EMAIL)).child("folder").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                    pojoFolderItem = messageSnapshot.getValue(PojoFolderItem.class);
                                    folderItem = PojoFolderItem.toRealm(pojoFolderItem);
                                    realm.copyToRealm(folderItem);

                                }
                                realm.commitTransaction();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                break;

        }
    }

    void init(){

        on = (Button)findViewById(R.id.on);
        off = (Button)findViewById(R.id.off);
        back=(ImageView)findViewById(R.id.back);
        ok = (ImageView)findViewById(R.id.ok);
        ok.setOnClickListener(clickListener);
        on.setOnClickListener(clickListener);
        off.setOnClickListener(clickListener);
        back.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swich = (Switch)findViewById(R.id.switchBar);
        radiusBar = (SeekBar)findViewById(R.id.radiusBar);
        radiusValue = (TextView)findViewById(R.id.radiusValue);

        SharedPreferencesService.getInstance().load(getApplicationContext());

        setView();

        realm = RealmHelper.getInstance(this);
        databaseRef = FirebaseDatabase.getInstance().getReference();

        swich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    setAlarmOn(true);
                }
                else{
                    setAlarmOn(false);
                }
            }
        });

        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //todo 채우기
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { //todo 변화 없는 경우 반영
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
}