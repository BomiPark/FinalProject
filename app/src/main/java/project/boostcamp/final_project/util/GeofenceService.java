package project.boostcamp.final_project.util;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import project.boostcamp.final_project.model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.view.TodoItem.ItemDetailActivity;

import static project.boostcamp.final_project.util.SharedPreferencesService.IS_ALARM;

// 로케이션 서비스로부터 지오펜스 전환 이벤트를 받고 이에 관한 전환 처리. 그 결과로서 notification 반환
public class GeofenceService extends IntentService {

    private Realm realm;

    public final static String TAG = "GeofenceService";

    public GeofenceService() {
        super(TAG);
    }

    public GeofenceService(String name) {
        super(name);
    }

    private String getGeofenceTransitionDetails(List<Geofence> triggeringGeofences) { //발생된 지오펜스 리스트

        if (realm == null)
            realm = RealmHelper.getInstance(getApplicationContext());

        if (triggeringGeofences != null) {
            ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
            for (Geofence geofence : triggeringGeofences) {
                triggeringGeofencesIdsList.add(geofence.getRequestId());

                if (realm.where(TodoItem.class).equalTo("id", Integer.parseInt(geofence.getRequestId())).equalTo("isCompleted", false).findFirst() != null)
                    return geofence.getRequestId();
            }

            return triggeringGeofencesIdsList.get(0);
        }
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        int position;

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences(); // 이벤트 발생한 지오펜스 획득. 하나의 이벤트가 여러개의 지오펜스와 관련되어있을 수도 있다

        String geofenceTransitionDetails = getGeofenceTransitionDetails(triggeringGeofences); // 상태 하나의 문자열로 변환

        if (geofenceTransitionDetails != null) {
            position = Integer.parseInt(geofenceTransitionDetails);
            sendNotification(position);
        }
    }

    private void sendNotification(int position) {

        Intent notificationIntent = new Intent(getApplicationContext(), ItemDetailActivity.class);

        notificationIntent.putExtra("id", position);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ItemDetailActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        String todo = null;
        if (realm.where(TodoItem.class).equalTo("id", position).equalTo("isCompleted", false).findFirst() != null) {
            todo = " '" + realm.where(TodoItem.class).equalTo("id", position).findFirst().getTodo() + "'를 수행할 장소입니다. ";

            Log.e(TAG, position + ", 알람내용 = " + todo);

            builder.setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(todo)
                    .setSmallIcon(R.drawable.app_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setVibrate(new long[]{1000, 1000, 200, 200})
                    .setLights(0xff00ff00, 500, 500)
                    .setContentIntent(notificationPendingIntent);

            builder.setAutoCancel(true);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            SharedPreferencesService.getInstance().load(getApplicationContext());

            boolean isAlarm = SharedPreferencesService.getInstance().getPrefBooleanData(IS_ALARM);
            if (isAlarm)
                mNotificationManager.notify(0, builder.build());

        }
    }

}
