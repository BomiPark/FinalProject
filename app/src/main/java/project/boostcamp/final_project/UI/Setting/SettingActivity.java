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

import static project.boostcamp.final_project.Util.BindingService.geofencingService;
import static project.boostcamp.final_project.Util.SharedPreferencesService.IS_BOUND;

public class SettingActivity extends AppCompatActivity {

    Button on, off;
    ImageView back;
    Switch swich;
    SeekBar radiusBar;
    TextView radiusValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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

}