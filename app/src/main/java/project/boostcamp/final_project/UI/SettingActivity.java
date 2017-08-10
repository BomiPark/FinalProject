package project.boostcamp.final_project.UI;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Util.GeofencingService;
import project.boostcamp.final_project.Util.GeofencingService.GeoBinder;


public class SettingActivity extends AppCompatActivity {

    Intent intent;
    GeoBinder geoBinder;
    GeofencingService geofencingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        intent = new Intent(this, GeofencingService.class);
        unbindService(connection); //todo 이러면안될거같은뎅 ㅠㅠ
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            geoBinder = (GeoBinder) service;
            geofencingService = geoBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public void onClick(View view) {
        geofencingService.startGeofence();
    } //todo 이거바로시작하면강제종료

}