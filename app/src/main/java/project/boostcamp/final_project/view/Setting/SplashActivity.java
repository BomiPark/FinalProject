package project.boostcamp.final_project.view.Setting;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import project.boostcamp.final_project.R;
import project.boostcamp.final_project.view.TodoItem.MainActivity;
import project.boostcamp.final_project.util.BindingService;
import project.boostcamp.final_project.util.SharedPreferencesService;

import static project.boostcamp.final_project.util.SharedPreferencesService.IS_ALARM;
import static project.boostcamp.final_project.util.SharedPreferencesService.IS_BOUND;
import static project.boostcamp.final_project.util.SharedPreferencesService.IS_SETTING;

public class SplashActivity extends BaseActivity {

    private BindingService bindingService;
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
            bindingService = BindingService.getInstance(getApplicationContext());
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
        }, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
