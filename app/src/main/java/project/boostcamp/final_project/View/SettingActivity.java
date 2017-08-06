package project.boostcamp.final_project.View;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import project.boostcamp.final_project.GeoFencing.GeofenceService;
import project.boostcamp.final_project.Model.Constant;
import project.boostcamp.final_project.R;

public class SettingActivity extends AppCompatActivity implements OnCompleteListener<Void> {

    @Override
    public void onComplete(@NonNull Task<Void> task) {

    }

    private enum PendingGeofenceTask { // 지오펜스의 현재 상황
        ADD, REMOVE, NONE
    }

    private GeofencingClient mGeofencingClient; // Geofencing API 에 접근하게 해준다
    private ArrayList<Geofence> mGeofenceList; // 알람 울리게 관리하는 지역 리스트
    private PendingIntent mGeofencePendingIntent; // 지오펜스를 요청하거나 삭제한 경우 사용

    private PendingGeofenceTask mPendingGeofenceTask = PendingGeofenceTask.NONE; // 지오펜싱 태스크의 현재 상황을 설정 할 수 있다. 기본 세팅은 NONE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
    }

    void init(){
        mGeofenceList = new ArrayList<>(); // 리스트 초기화
        mGeofencePendingIntent = null; //

        setGeofenceList();
        mGeofencingClient = LocationServices.getGeofencingClient(this);

    }

    private void performPendingGeofenceTask() {  //     * Performs the geofencing task that was pending until location permission was granted.
        if (mPendingGeofenceTask == PendingGeofenceTask.ADD) {
            addGeofences();
        } else if (mPendingGeofenceTask == PendingGeofenceTask.REMOVE) {
            removeGeofences();
        }
    }

    @SuppressWarnings("MissingPermission")
    private void addGeofences() { //사용자가 요청을 허락한 경우에만 사용 가능해서 퍼미션 요구함

        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnCompleteListener(this);
    }

    private PendingIntent getGeofencePendingIntent() { // 지오펜스 전환을 할 수 있는 인텐트 서비스에게 펜딩인텐트를 넘기며, 로케이션 서비스 관련된 애가 이 안에 있는 듯
        if (mGeofencePendingIntent != null) { // 펜딩인텐트 있는 경우 재사용
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceService.class); //

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT); //FLAG_UPDATE_CURRENT 를 사용해서 지오펜스를 설정할 때나 해제할때나 같은 펜딩인텐틀ㄹ 사용한다 .
    }

    public void removeGeofencesButtonHandler(View view) { // 지오펜스 제거하면 후에 지오펜스에 들어가거나 나와도 이제 알람이 울리지 않음

        mPendingGeofenceTask = PendingGeofenceTask.REMOVE;

        removeGeofences();
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }

    private void removeGeofences() { //이것두 사용자가 퍼미션을 허락해야 사용 가능

        mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(this);
    }

    void setGeofenceList(){

        mGeofenceList.add(new Geofence.Builder()
                .setRequestId("test1") //지오펜스 구분하기 위한 키값 설정

                .setCircularRegion( // 지오펜스 근처에 영역 지정
                        37.4017670,
                        127.1094800,
                        Constant.GEOFENCE_RADIUS_IN_METERS
                )

                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | // 전환 타입 지정, 우리는 들어오고 나가는거만 체크할거야
                        Geofence.GEOFENCE_TRANSITION_EXIT)

                .build()); //지오펜스 생성

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.start :
                Toast.makeText(SettingActivity.this, "start", Toast.LENGTH_LONG).show();

                break;
            case R.id.stop :
                Toast.makeText(SettingActivity.this, "stop", Toast.LENGTH_LONG).show();

                break;
        }
    }
}
