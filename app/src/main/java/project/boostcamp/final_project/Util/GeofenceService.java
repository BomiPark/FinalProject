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
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.TodoItem.ItemDetailActivity;

// 로케이션 서비스로부터 지오펜스 전환 이벤트를 받고 이에 관한 전환 처리. 그 결과로서 notification 반환
public class GeofenceService extends IntentService {

    public final static String TAG = "GeofenceService";

    public GeofenceService(){
        super(TAG);
    }

    public GeofenceService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        int geofenceTransition = geofencingEvent.getGeofenceTransition(); // 전환 타입을 얻는다.

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ) {

            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences(); // 이벤트 발생한 지오펜스 획득. 하나의 이벤트가 여러개의 지오펜스와 관련되어있을 수도 있다

            String geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition, triggeringGeofences); // 상태 하나의 문자열로 변환

            sendNotification(geofenceTransitionDetails);
        }
    }

    private String getGeofenceTransitionDetails( // 이벤트 발생 상태 하나의 문자열로 변환
            int geofenceTransition, // 지오펜스 전환 타입
            List<Geofence> triggeringGeofences) { //발생된 지오펜스

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }

        String triggeringGeofencesIdsString = TextUtils.join(", ",  triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    private void sendNotification(String notificationDetails) {

        Intent notificationIntent = new Intent(getApplicationContext(), ItemDetailActivity.class);

        notificationIntent.putExtra("id", 0);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(ItemDetailActivity.class);   //todo 지정한아이템세부항목으로가기

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        RemoteViews customView = new RemoteViews((getApplicationContext()).getPackageName(), R.layout.noti);

        builder.setSmallIcon(R.drawable.app_icon)   //노티 세팅 todo 커스텀으로수정~
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.app_icon))
                .setColor(Color.RED)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setVibrate(new long[]{1000, 1000, 200, 200})
                .setLights(0xff00ff00, 500, 500)
                //.setContentTitle(notificationDetails)
                //.setContentText(getString(R.string.geofence_transition_notification_text))
                .setContent(customView)
                .setContentIntent(notificationPendingIntent);

        builder.setAutoCancel(true);            // Dismiss notification once the user touches it.

        NotificationManager mNotificationManager =   //노티 매니저
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, builder.build()); //노티 날린다
    }

    private String getTransitionString(int transitionType) {    //전환 상태 제공  들어왔는지 나갔는지
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }
}
