package project.boostcamp.final_project.UI.Setting;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import project.boostcamp.final_project.R;
import project.boostcamp.final_project.UI.TodoItem.MainActivity;
import project.boostcamp.final_project.Util.SharedPreferencesService;

import static project.boostcamp.final_project.Util.SharedPreferencesService.IS_SETTING;

public class SplashActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferencesService.getInstance().load(getApplicationContext());
        boolean isSetting = SharedPreferencesService.getInstance().getPrefBooleanData(IS_SETTING);

        if(isSetting == false){
            SharedPreferencesService.getInstance().setPrefData(IS_SETTING, true);
            intent = new Intent(this, PermissionActivity.class);}
        else{
            intent = new Intent(this, MainActivity.class);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 500);
    }
}
