package project.boostcamp.final_project.UI.Setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.j2objc.annotations.ObjectiveCName;

import project.boostcamp.final_project.R;
import project.boostcamp.final_project.Util.SharedPreferencesService;

import static project.boostcamp.final_project.Util.SharedPreferencesService.PROP_IMG;
import static project.boostcamp.final_project.Util.SharedPreferencesService.PROP_NAME;

public class ProfileActivity extends AppCompatActivity {

    ImageView prop_img, prop_img1, prop_img2, prop_img3, prop_img4, prop_img5;
    EditText prop_name;

    int save_img;
    int prop_img_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        setProfile();
    }

    void setProfile(){
        SharedPreferencesService.getInstance().load(getApplicationContext());

        prop_img_value = SharedPreferencesService.getInstance().getPrefIntData(PROP_IMG);
        String prop_name_value = SharedPreferencesService.getInstance().getPrefStringData(PROP_NAME);

        if(prop_img_value != 0){
            prop_img.setImageResource(prop_img_value);}
        if(prop_name_value != null){
            prop_name.setText(prop_name_value);
        }
    }

    void saveProfile(){

        if(!prop_name.getText().toString().equals(""))
            SharedPreferencesService.getInstance().setPrefStringData(PROP_NAME, prop_name.getText().toString());
        else
            SharedPreferencesService.getInstance().setPrefStringData(PROP_NAME, getResources().getString(R.string.name));
        SharedPreferencesService.getInstance().setPrefIntData(PROP_IMG, save_img);

    }

    public void onClick(View view){
        finish();
        saveProfile();
    }

    View.OnClickListener clickListener = new ImageView.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.prop_img1 :
                    save_img = R.drawable.prop_img1;
                    break;
                case R.id.prop_img2 :
                    save_img = R.drawable.prop_img2;
                    break;
                case R.id.prop_img3 :
                    save_img = R.drawable.prop_img3;
                    break;
                case R.id.prop_img4 :
                    save_img = R.drawable.prop_img4;
                    break;
                case R.id.prop_img5 :
                    save_img = R.drawable.prop_img5;
                    break;
            }
            prop_img.setImageResource(save_img);
        }
    };

    void init(){

        prop_name = (EditText)findViewById(R.id.prop_name);
        prop_img = (ImageView)findViewById(R.id.prop_image);
        prop_img1 = (ImageView)findViewById(R.id.prop_img1);
        prop_img2 = (ImageView)findViewById(R.id.prop_img2);
        prop_img3 = (ImageView)findViewById(R.id.prop_img3);
        prop_img4 = (ImageView)findViewById(R.id.prop_img4);
        prop_img5 = (ImageView)findViewById(R.id.prop_img5);

        prop_img1.setOnClickListener(clickListener);
        prop_img2.setOnClickListener(clickListener);
        prop_img3.setOnClickListener(clickListener);
        prop_img4.setOnClickListener(clickListener);
        prop_img5.setOnClickListener(clickListener);

        save_img = R.drawable.prop_img1;
    }

}
