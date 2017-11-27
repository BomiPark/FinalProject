package project.boostcamp.final_project.util;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

public class FontApplication extends Application{

    @Override
    public void onCreate(){
        super.onCreate();
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NanumBarunGothic.ttf"))
                .addBold(Typekit.createFromAsset(this, "NanumBarunGothicBold.ttf"))
                .addCustom1(Typekit.createFromAsset(this, "NanumGothic.ttf"));
    }
}
