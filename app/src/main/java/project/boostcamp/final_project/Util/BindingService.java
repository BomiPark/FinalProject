package project.boostcamp.final_project.Util;

import io.realm.Realm;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.Util.GeofencingService.GeoBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import static project.boostcamp.final_project.UI.Setting.SplashActivity.bindingService;
import static project.boostcamp.final_project.Util.SharedPreferencesService.IS_BOUND;

public class BindingService {

    private Intent intent;
    private GeoBinder geoBinder;
    private Context context;
    public static GeofencingService geofencingService;
    public static boolean isBound;

    public BindingService(Context context){

        this.context = context;
        intent = new Intent(context, GeofencingService.class);
        SharedPreferencesService.getInstance().load(context);

        /*
        if(!SharedPreferencesService.getInstance().getPrefBooleanData(IS_BOUND)) {  //todo  수정
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        } */

        if(!isBound)
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    public void unbindService(){
        if (connection != null) {
            context.unbindService(connection);
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            SharedPreferencesService.getInstance().setPrefData(IS_BOUND, true);

            geoBinder = (GeoBinder) service;
            geofencingService = geoBinder.getService();

            if(!isBound) {  //todo  수정
                bindingService.startService();
            }

            isBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            geofencingService = null;
            geoBinder = null;
            //SharedPreferencesService.getInstance().setPrefData(IS_BOUND, false);
        }
    };

    public void startService(){

        if(!isEmpty() && geofencingService != null) {
            geofencingService.startGeofence();
        }
    }

    public void stopService(){
        geofencingService.stopGeofence();//
        isBound = false;
    }

    public void upDateService(){
        geofencingService.updateGeofence();
    }

    boolean isEmpty(){

        Realm realm = RealmHelper.getInstance(context);
        if(realm.where(TodoItem.class).equalTo("alarm", true).equalTo("isCompleted", false).findAll().size() == 0)
            return true;
        else
            return false;
    }
}
