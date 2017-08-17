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

import io.realm.RealmResults;
import project.boostcamp.final_project.Model.PojoTodoItem;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.Model.User;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Util.RealmHelper;


public class SettingActivity extends AppCompatActivity {

    Button on, off;
    ImageView back, ok;
    Switch swich;
    SeekBar radiusBar;
    TextView radiusValue;

    boolean isAlarm;
    int radius;

    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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

        setView();

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
            case R.id.to_backup :
                Toast.makeText(getApplicationContext(), "backup", Toast.LENGTH_LONG).show();

                RealmResults<TodoItem> realmResults = RealmHelper.getInstance(this).where(TodoItem.class).findAll();
                List<PojoTodoItem> todoList = ImmutableList.copyOf(Collections2.transform(realmResults, new Function<TodoItem, PojoTodoItem>() {
                    @Nullable
                    @Override
                    public PojoTodoItem apply(@Nullable TodoItem input) {
                        return TodoItem.toPojo(input);
                    }
                }));
                databaseRef.child("test").setValue(todoList); // todo 사용자 이메일로 바꾸기!!!!!
                break;
            case R.id.get_data :
                databaseRef.child("test").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Log.e("받아온 데이터 확인", dataSnapshot.toString());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                                Log.e("받아온 데이터 확인","fail");
                            }
                        });
                break;

        }
    }
}