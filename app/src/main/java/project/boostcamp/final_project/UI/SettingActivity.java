package project.boostcamp.final_project.UI;

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
import android.widget.Toast;

import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.TodoItem.MainActivity;
import project.boostcamp.final_project.Util.GeofencingService;
import project.boostcamp.final_project.Util.GeofencingService.GeoBinder;
import project.boostcamp.final_project.Util.SharedPreferencesService;


public class SettingActivity extends AppCompatActivity {

    Intent intent;
    GeoBinder geoBinder;
    GeofencingService geofencingService;
    boolean isBound;

    Button start, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        intent = new Intent(this, GeofencingService.class);

        if(isBound == false) {
            bindService(intent, connection, Context.BIND_AUTO_CREATE); // 이걸 계속하는 게 문제인강
            isBound = true;
        }
        start = (Button)findViewById(R.id.start);
        start.setOnClickListener(clickListener);
        stop = (Button)findViewById(R.id.stop);
        stop.setOnClickListener(clickListener);


    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            Toast.makeText(SettingActivity.this, "Service Connected", Toast.LENGTH_LONG).show();

            geoBinder = (GeoBinder) service;
            geofencingService = geoBinder.getService();
            isBound = true;
        }



        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            geoBinder = null;
            isBound = false;
            geofencingService = null;
        }
    };

    View.OnClickListener clickListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.start :
                    Toast.makeText(getApplicationContext(), "service start", Toast.LENGTH_LONG).show();
                    geofencingService.startGeofence();
                    break;
                case R.id.stop :
                    Toast.makeText(getApplicationContext(), "service stop", Toast.LENGTH_LONG).show();
                    geofencingService.stopGeofence();//
                    //unbindService(connection);
                    isBound = false;
                    break;
            }
        }
    };

}