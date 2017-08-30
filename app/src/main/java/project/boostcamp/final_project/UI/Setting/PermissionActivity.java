package project.boostcamp.final_project.UI.Setting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
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
import static project.boostcamp.final_project.Util.SharedPreferencesService.PROP_IMG;
import static project.boostcamp.final_project.Util.SharedPreferencesService.RADIUS;

public class PermissionActivity extends BaseActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private Realm realm;
    private List<String> folderList;
    private FolderItem folder;

    private SignInButton sign_in_button;

    private DatabaseReference databaseRef;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private ProgressBar login_progress;
    public static final int RC_GOOGLE_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        SharedPreferencesService.getInstance().load(getApplicationContext());

        auth = FirebaseAuth.getInstance(); //FirebaseAuth의 인스턴스 가져옴
        databaseRef = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        sign_in_button = (SignInButton)findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(clickListener);
        login_progress = (ProgressBar)findViewById(R.id.login_progress);
        login_progress.setVisibility(View.INVISIBLE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SharedPreferencesService.getInstance().setPrefData(IS_SETTING, true); //shared 세팅
        SharedPreferencesService.getInstance().setPrefIntData(PROP_IMG, R.drawable.prop_img);
        SharedPreferencesService.getInstance().setPrefIntData(RADIUS, 1000);
    }

    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
        }
    };


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
        Snackbar snackbar;

        snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE) // 뜨는 시간 설정-> 계속 뜸
                .setAction(getString(actionStringId), listener); // 클릭리스너

        View snackView = snackbar.getView();
        snackView.setBackgroundColor(getResources().getColor(R.color.blue));

        snackbar.show();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == RC_GOOGLE_SIGN_IN ) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if ( result.isSuccess() ) {

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else {
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        login_progress.setVisibility(View.VISIBLE);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            databaseRef.child("users").child(user.getUid()).setValue(new User(user.getEmail()));
                            SharedPreferencesService.getInstance().setPrefStringData(EMAIL,user.getUid());
                        } else {
                            Toast.makeText(PermissionActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        setDefaultFolder();

                        startActivity(new Intent(PermissionActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }
}