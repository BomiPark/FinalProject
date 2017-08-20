package project.boostcamp.final_project.Util;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.TodoItem.ItemDetailActivity;

import static project.boostcamp.final_project.Util.SharedPreferencesService.IS_ALARM;

// 로케이션 서비스로부터 지오펜스 전환 이벤트를 받고 이에 관한 전환 처리. 그 결과로서 notification 반환
public class GeofenceService extends IntentService {

    private Realm realm;

    public final static String TAG = "GeofenceService";

    public GeofenceService(){
        super(TAG);
    }

    public GeofenceService(String name) {
        super(name);
    }

    private String getGeofenceTransitionDetails( // 이벤트 발생 상태 하나의 문자열로 변환
                                                 List<Geofence> triggeringGeofences) { //발생된 지오펜스

        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }

        return triggeringGeofencesIdsList.get(0).toString();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        int geofenceTransition = geofencingEvent.getGeofenceTransition(); // 전환 타입을 얻는다.

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ) {

            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences(); // 이벤트 발생한 지오펜스 획득. 하나의 이벤트가 여러개의 지오펜스와 관련되어있을 수도 있다

            String geofenceTransitionDetails = getGeofenceTransitionDetails(triggeringGeofences); // 상태 하나의 문자열로 변환

            int position = Integer.parseInt(geofenceTransitionDetails);

            sendNotification(position);
        }
    }

    private void sendNotification(int notificationDetails) {

        Intent notificationIntent = new Intent(getApplicationContext(), ItemDetailActivity.class);

        Log.e("GeofenceService", "sendNotification" + notificationDetails);
        notificationIntent.putExtra("id", notificationDetails);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(ItemDetailActivity.class);

        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        realm= RealmHelper.getInstance(getApplicationContext());

        String todo = null;
        if( realm.where(TodoItem.class).equalTo("id", notificationDetails).findFirst() != null) {
            todo = " '" + realm.where(TodoItem.class).equalTo("id", notificationDetails).findFirst().getTodo() + "'를 수행할 장소입니다. ";

            RemoteViews customView = new RemoteViews((getApplicationContext()).getPackageName(), R.layout.item_notification);
            customView.setTextViewText(R.id.noti_text, todo);
            builder.setSmallIcon(R.drawable.app_icon)   // 메시지 내용 동적으로 변경 고려
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.app_icon))
                    .setColor(Color.RED)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setVibrate(new long[]{1000, 1000, 200, 200})
                    .setLights(0xff00ff00, 500, 500)
                    .setContent(customView)
                    .setContentIntent(notificationPendingIntent);

            builder.setAutoCancel(true);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            SharedPreferencesService.getInstance().load(getApplicationContext());
            if(SharedPreferencesService.getInstance().getPrefBooleanData(IS_ALARM))
                mNotificationManager.notify(0, builder.build());
        }
    }

}
