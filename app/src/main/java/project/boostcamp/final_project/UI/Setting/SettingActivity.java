package project.boostcamp.final_project.UI.Setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Util.GeofencingService;
import project.boostcamp.final_project.Util.GeofencingService.GeoBinder;
import project.boostcamp.final_project.Util.SharedPreferencesService;

import static project.boostcamp.final_project.R.id.back;
import static project.boostcamp.final_project.Util.SharedPreferencesService.IS_BOUND;

public class SettingActivity extends AppCompatActivity {

    Intent intent;
    GeoBinder geoBinder;
    public static GeofencingService geofencingService;
    public static boolean isBound; // shared에저장해야할듯!!

    Button on, off;
    ImageView back;
    Switch swich;
    SeekBar radiusBar;
    TextView radiusValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        intent = new Intent(this, GeofencingService.class);
        on = (Button)findViewById(R.id.on);
        off = (Button)findViewById(R.id.off);
        back=(ImageView)findViewById(R.id.back);
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
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusValue.setText(progress + " M");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        SharedPreferencesService.getInstance().load(getApplicationContext());

        if(!SharedPreferencesService.getInstance().getPrefBooleanData(IS_BOUND)) {
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            Toast.makeText(SettingActivity.this, "Service Connected", Toast.LENGTH_LONG).show();

            geoBinder = (GeoBinder) service;
            geofencingService = geoBinder.getService();
            SharedPreferencesService.getInstance().setPrefData(IS_BOUND, true);
            isBound = true;

        }


        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            geofencingService = null;
            geoBinder = null;
            SharedPreferencesService.getInstance().setPrefData(IS_BOUND, false);
        }
    };

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
            }
        }
    };

    void setAlarmOn(boolean on){
        if(on == true){
            swich.setChecked(true);
            Toast.makeText(getApplicationContext(), "service start", Toast.LENGTH_LONG).show();
            setTextColor(true);
            if(isEmpty() != true && geofencingService != null) {
                geofencingService.startGeofence();
                Log.e("setting135", geofencingService + "");
            }
        }
        else{
            swich.setChecked(false);
            Toast.makeText(getApplicationContext(), "service stop", Toast.LENGTH_LONG).show();
            geofencingService.stopGeofence();//
            setTextColor(false);
            isBound = false;
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

    boolean isEmpty(){

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        Realm realm = Realm.getDefaultInstance();
        if(realm.where(TodoItem.class).findAll().size() == 0)
            return true;
        else
            return false;
    }

}