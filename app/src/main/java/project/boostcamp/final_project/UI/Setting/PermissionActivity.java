package project.boostcamp.final_project.UI.Setting;

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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import project.boostcamp.final_project.BuildConfig;
import project.boostcamp.final_project.Model.FolderItem;
import project.boostcamp.final_project.Model.User;
import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.TodoItem.MainActivity;
import project.boostcamp.final_project.Util.RealmHelper;
import project.boostcamp.final_project.Util.SharedPreferencesService;

import static project.boostcamp.final_project.R.string.settings;
import static project.boostcamp.final_project.Util.SharedPreferencesService.EMAIL;
import static project.boostcamp.final_project.Util.SharedPreferencesService.IS_SETTING;

//https://developer.android.com/training/permissions/requesting.html?hl=ko 디벨로퍼 문서
// 앱 설치 시 거절-> 세팅창으로 이동, 거절 후 다시 어플리케이션 시작-> 확인 누르면 퍼미션 창 다시 뜬다
public class PermissionActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private Realm realm;
    private List<String> folderList;
    private FolderItem folder;

    private EditText edit_email, edit_pwd;

    private DatabaseReference databaseRef;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        SharedPreferencesService.getInstance().load(getApplicationContext());
        setDefaultFolder();

        auth = FirebaseAuth.getInstance(); //FirebaseAuth의 인스턴스 가져옴
        databaseRef = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        edit_email = (EditText) findViewById(R.id.email);
        edit_pwd = (EditText) findViewById(R.id.pwd);
    }

    public void onClick(View view){

        String email = edit_email.getText().toString();
        String pwd = edit_pwd.getText().toString();

        switch(view.getId()){
            case R.id.signUp :
                signUp(email, pwd);
                break;
            case R.id.signIn :
                signIn(email, pwd);
                break;

        }
    }

    public void signUp(String email, String pwd){

        auth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(PermissionActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser(); // signUp 성공한 유저는 데이터베이스에 저장
                            User userModel = new User(user.getEmail());
                            userModel.setPwd(edit_pwd.getText().toString());
                            databaseRef.child("users").child(user.getUid()).setValue(userModel);
                            SharedPreferencesService.getInstance().setPrefStringData(EMAIL,user.getUid());
                            Toast.makeText(PermissionActivity.this, "signUp ok", Toast.LENGTH_SHORT).show();
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(PermissionActivity.this, "signUp failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void signIn(String email, String pwd){
        auth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PermissionActivity.this, "signIn ok ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PermissionActivity.this, MainActivity.class));

                        }
                        else {
                            Toast.makeText(PermissionActivity.this, "signIn failed ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();

        auth.addAuthStateListener(mAuthListener);

        if (!checkPermissions()) {
            requestPermissions(); // 퍼미션 요청
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
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
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { //승인한 경우

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

    void setDefaultFolder(){

        getFolderList();

        realm = RealmHelper.getInstance(this);
        realm.beginTransaction();
        for(int i=0; i< folderList.size(); i++) {
            folder = realm.createObject(FolderItem.class);
            folder.setId(i);
            folder.setFolder(folderList.get(i).toString());
            realm.copyToRealm(folder);
        }
        realm.commitTransaction();
    }

    void getFolderList(){
        folderList = new ArrayList<>();
        folderList.add(getResources().getString(R.string.folder_default0));
        folderList.add(getResources().getString(R.string.folder_default1));
        folderList.add(getResources().getString(R.string.folder_default2));
        folderList.add(getResources().getString(R.string.folder_default3));
    }

}