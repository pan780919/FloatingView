package jp.co.recruit_lifestyle.sample;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jack.pan on 2017/4/20.
 */

public class GtSharedPreferences {
    //是否第一次使用
    public  static final  String NAME = "MySharedPrefernces";
    public static final String KEY_FIRST_USED = "isFirstUsed";
    public static void saveIsFirstUsed(Context context,boolean isopen) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_FIRST_USED, isopen).commit();
    }

    public static boolean getIsFirstUsed(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getBoolean(KEY_FIRST_USED, false);
    }


}
