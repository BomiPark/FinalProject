package project.boostcamp.final_project.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesService {

    private static final String SETTING = "setting";
    public static final String IS_SETTING = "isSetting"; // 앱 처음 설치한 것인지 확인
    public static final String IS_ALARM = "isAlarm";
    public static final String PROP_NAME = "prop_name";
    public static final String PROP_IMG = "prop_img";
    public static final String RADIUS = "radius";
    public static final String IS_BOUND = "is_bound";
    public static final String EMAIL = "email";

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

    public String getPrefStringData(String key){
        return pref.getString(key, null);
    }

    public int getPrefIntData(String key){
        return pref.getInt(key, 1);
    }

    public boolean getPrefBooleanData(String key){
        return pref.getBoolean(key, false);
    }

    public void setPrefData(String key, boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void setPrefStringData(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setPrefIntData(String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
