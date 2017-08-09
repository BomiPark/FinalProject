package project.boostcamp.final_project.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesService {

    private static final String SETTING = "setting";

    private static SharedPreferencesService sharedPreferencesManager;
    private SharedPreferences pref;

    public static SharedPreferencesService getInstance(){
        if (sharedPreferencesManager == null) {
            synchronized (SharedPreferencesService.class) {
                if (sharedPreferencesManager == null)
                    sharedPreferencesManager = new SharedPreferencesService();
            }
        }
        return sharedPreferencesManager;
    }

    private void getPref(Context cont) {
        if (pref == null) {
            pref = cont.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        }
    }

    public void load(Context context) {
        getPref(context);
    }

    public boolean getPrefData(String key){
        return pref.getBoolean(key, false);
    }

    public void setPrefData(String key, boolean value) {

        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
