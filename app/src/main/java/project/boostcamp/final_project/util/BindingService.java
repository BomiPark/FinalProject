package project.boostcamp.final_project.util;

import io.realm.Realm;
import project.boostcamp.final_project.model.TodoItem;
import project.boostcamp.final_project.util.GeofencingService.GeoBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import static project.boostcamp.final_project.util.SharedPreferencesService.IS_BOUND;

public class BindingService {

    private static BindingService binding;
    private Intent intent;
    private GeoBinder geoBinder;
    private Context context;
    private static GeofencingService geofencingService;
    public static boolean isBound;
    private Realm realm;

    public static BindingService getInstance(Context context){

        if(binding == null){
            synchronized (BindingService.class) {
                if(binding == null)
                    binding = new BindingService(context);
            }
        }
        return binding;
    }

    private BindingService(Context context){

        isBound = true;
        intent = new Intent(context, GeofencingService.class);
        SharedPreferencesService.getInstance().load(context);
        realm = RealmHelper.getInstance(context);

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

            if(!isBound) {
                binding.startService();
            }

            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            geofencingService = null;
            geoBinder = null;
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
        if(geofencingService != null)
            geofencingService.updateGeofence();
    }

    private boolean isEmpty(){

        if(realm.where(TodoItem.class).equalTo("alarm", true).equalTo("isCompleted", false).findAll().size() == 0)
            return true;
        else
            return false;
    }
}
