package project.boostcamp.final_project.UI.Setting;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.TodoItem.MainActivity;
import project.boostcamp.final_project.Util.BindingService;
import project.boostcamp.final_project.Util.SharedPreferencesService;

import static project.boostcamp.final_project.Util.SharedPreferencesService.IS_ALARM;
import static project.boostcamp.final_project.Util.SharedPreferencesService.IS_BOUND;
import static project.boostcamp.final_project.Util.SharedPreferencesService.IS_SETTING;

public class SplashActivity extends AppCompatActivity {

    public static BindingService bindingService;
    private Intent intent;
    private boolean isBound, isSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferencesService.getInstance().load(getApplicationContext());
        SharedPreferencesService.getInstance().setPrefData(IS_BOUND, false);

        isSetting = SharedPreferencesService.getInstance().getPrefBooleanData(IS_SETTING);


        if(!BindingService.isBound) {
            bindingService = BindingService.getInstance(this);//todo 체크
        }

        if(!isSetting){
            SharedPreferencesService.getInstance().setPrefData(IS_ALARM, true);
            intent = new Intent(this, PermissionActivity.class);}
        else{
            intent = new Intent(this, MainActivity.class);
        }

        isBound = true;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
