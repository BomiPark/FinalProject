package project.boostcamp.final_project.Util;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.Model.TodoItem;

public class GeofencingService extends Service{

    public final IBinder binder = new GeoBinder();

    private enum PendingGeofenceTask { // 지오펜스의 현재 상태
        ADD, REMOVE, NONE
    }
    RealmResults<TodoItem> itemList;
    Realm realm;

    private GeofencingClient mGeofencingClient; // Geofencing API 에 접근하게 해준다
    private ArrayList<Geofence> mGeofenceList; // 알람 울리게 관리하는 지역 리스트
    private PendingIntent mGeofencePendingIntent; // 지오펜스를 요청하거나 삭제한 경우 사용

    private PendingGeofenceTask mPendingGeofenceTask = PendingGeofenceTask.NONE; // 지오펜싱 태스크의 현재 상황을 설정 할 수 있다. 기본 세팅은 NONE


    public GeofencingService() {
    }

    public void staartGeofence(){
        mPendingGeofenceTask = PendingGeofenceTask.ADD;
        addGeofences();
    }

    public void stopGeofence(){
        mPendingGeofenceTask = PendingGeofenceTask.REMOVE;
        removeGeofences();
    }

    @SuppressWarnings("MissingPermission")
    private void addGeofences() { //사용자가 요청을 허락한 경우에만 사용 가능해서 퍼미션 요구함

        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent());
               //.addOnCompleteListener(getApplication());
    }

    private void removeGeofences() { //이것두 사용자가 퍼미션을 허락해야 사용 가능

        mGeofencingClient.removeGeofences(getGeofencePendingIntent());
    }

    private PendingIntent getGeofencePendingIntent() { // 지오펜스 전환을 할 수 있는 인텐트 서비스에게 펜딩인텐트를 넘기며, 로케이션 서비스 관련된 애가 이 안에 있는 듯
        if (mGeofencePendingIntent != null) { // 펜딩인텐트 있는 경우 재사용
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceService.class); //

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT); //FLAG_UPDATE_CURRENT 를 사용해서 지오펜스를 설정할 때나 해제할때나 같은 펜딩인텐트를 사용한다 .
    }

    private GeofencingRequest getGeofencingRequest() { // 지오펜스 리퀘스트 빌드, 지오펜스 리스트 빌더에 넣구 초기화
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER); // 이미들어가있는경우노티티

        builder.addGeofences(mGeofenceList); // 지오펜싱 서비스에 지오펜스 리스트 감시하게 함

        return builder.build(); // 지오펜스 리퀘스트 리턴
    }

    @Override
    public IBinder onBind(Intent intent) { // 초기화 여기에서 할래

        mGeofenceList = new ArrayList<>(); // 리스트 초기화
        mGeofencePendingIntent = null; //
        mPendingGeofenceTask = PendingGeofenceTask.ADD;

        setGeofenceList();
        mGeofencingClient = LocationServices.getGeofencingClient(this);

        return binder;
    }

    void setGeofenceList(){ // 지오펜스 리스트 설정

        setData();
        for(TodoItem item : itemList){

            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(item.getTodo()) //지오펜스 구분하기 위한 키값 설정
                    .setCircularRegion( // 지오펜스 근처에 영역 지정
                            item.getLatitude(),
                            item.getLongitude(),
                            Constant.GEOFENCE_RADIUS_IN_METERS  //todo 이거 입력받아서 수정할수 있게 만들기
                    )

                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)  // 전환 타입 지정

                    .setExpirationDuration(Geofence.NEVER_EXPIRE)

                    .build()); //지오펜스 생성
        }
    }

    void setData(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();

        itemList = realm.where(TodoItem.class).findAll();

    }

    public class GeoBinder extends Binder {
        public GeofencingService getService(){
            return GeofencingService.this;
        }
    }
}