package project.boostcamp.final_project.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import project.boostcamp.final_project.BuildConfig;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.TodoItem.MainActivity;
import project.boostcamp.final_project.Util.SharedPreferencesService;

import static project.boostcamp.final_project.R.string.settings;

//https://developer.android.com/training/permissions/requesting.html?hl=ko 디벨로퍼 문서
// 앱 설치 시 거절-> 세팅창으로 이동, 거절 후 다시 어플리케이션 시작-> 확인 누르면 퍼미션 창 다시 뜬다
public class PermissionActivity extends AppCompatActivity {

    private static final String TAG = "PermissionActivity";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public static final String SETTING = "isSetting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        SharedPreferencesService.getInstance().load(getApplicationContext()); //todo 이거스플래시에서하는게 맞는듯!

        boolean isSetting = SharedPreferencesService.getInstance().getPrefData(SETTING);

        if(isSetting == false)
            SharedPreferencesService.getInstance().setPrefData(SETTING, true);
        else{
            this.finish();
            startActivity(new Intent(this, MainActivity.class));} //todo 속도 너무 느린데 다른 방법 찾기

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions(); // 퍼미션 요청
        }
    }

    private boolean checkPermissions() { // 퍼미션 상태 체크
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() { // 퍼미션 요청
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) { // 거절+ 다시물어보지말라고했을때
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(PermissionActivity.this, // 퍼미션 요청
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(PermissionActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void showSnackbar(final String text) { //텍스트는 스낵바에 뜰 텍스트 나중에 사용하기!!!
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,  //1. 스낵바에뜰텍스트스트링인트값 2. 오른쪽뜨는 글씨 3. 클릭 리스너
                              View.OnClickListener listener) {
        Snackbar.make( //View snackView = snackbar.getView();snackView.setBackgroundColor(Color.parseColor("#FF0000")); 배경 색 변경
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE) // 뜨는 시간 설정-> 계속 뜸
                .setAction(getString(actionStringId), listener).show(); // 클릭리스너
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, // 퍼미션 요청 끝나고 반응
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//todo 여기서 지오펜싱 서비스 시작하기!
                finish();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                showSnackbar(R.string.permission_denied_explanation, settings,  // 퍼미션 요청 거절한 경우 스낵바를 통해 꼭 필요한 기능임을 알려주고 누르면 세팅창으로 갈 수 있다
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) { // 세팅창으로
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }
}
