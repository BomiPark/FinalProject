package project.boostcamp.final_project.Util;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.Util.GeofencingService.GeoBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import static project.boostcamp.final_project.UI.TodoItem.MainActivity.bindingService;
import static project.boostcamp.final_project.Util.SharedPreferencesService.IS_BOUND;

public class BindingService {

    Intent intent;
    GeoBinder geoBinder;
    Context context;
    public static GeofencingService geofencingService;
    public static boolean isBound;

    public BindingService(Context context){

        this.context = context;
        intent = new Intent(context, GeofencingService.class);
        SharedPreferencesService.getInstance().load(context);

        if(!SharedPreferencesService.getInstance().getPrefBooleanData(IS_BOUND)) {  //todo  수정
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            Toast.makeText(context, "Service Connected", Toast.LENGTH_LONG).show();

            geoBinder = (GeoBinder) service;
            geofencingService = geoBinder.getService();
            SharedPreferencesService.getInstance().setPrefData(IS_BOUND, true);
            isBound = true;

            bindingService.startService();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            geofencingService = null;
            geoBinder = null;
            SharedPreferencesService.getInstance().setPrefData(IS_BOUND, false);
        }
    };

    public void startService(){

        if(isEmpty() != true && geofencingService != null) {
            Toast.makeText(context, "service start", Toast.LENGTH_LONG).show();
            geofencingService.startGeofence();
        }
    }

    public void stopService(){
        Toast.makeText(context, "service stop", Toast.LENGTH_LONG).show();
        geofencingService.stopGeofence();//
        isBound = false;
    }

    boolean isEmpty(){

        Realm realm = RealmHelper.getInstance(context);
        if(realm.where(TodoItem.class).findAll().size() == 0)
            return true;
        else
            return false;
    }
}
