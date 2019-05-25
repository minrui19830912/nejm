package com.android.nejm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.nejm.MyApplication;


/**
 * 对SharedPreference文件中的各种类型的数据进行存取操作
 *
 */
public class SPUtils {

    private static SharedPreferences sp;
    private static final String PREF_USER = "user_preference";
    private static final String PREF_USER_CODE = "user_code";
    private static final String PREF_USER_PASS = "user_pass";
    private static final String POI_TIME = "poi_time";
    private static final String MSG_UID = "msg_uid";

    public static String[] leading_ins = new String[]{
            "在您附近的%s正在即时优惠，快去看看吧",
            "离您不远的%s惊现大力度促销商品，赶紧的",
//            "近在咫尺的%s满199减100活动正在火热进行中",
//            "您路过的%s全场5折火爆上演，快去快去",
//            "就在身边的%s冬季新品上市，赶紧体验一下吧"
    };
    public static String DEVICE = "device";

    private static void init(Context context) {
        if (sp == null) {
            sp = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }


    public static SharedPreferences getSharedPreference() {
        return MyApplication.getApplication().getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);
    }


    /**
     */
    public static void setUserCode(String userCode) {
        putStringPreference(PREF_USER_CODE, userCode);
    }


    public static String getUserCode() {
        return getStringPreference(PREF_USER_CODE, null);
    }

    public static void setMsgUid(String msgUid) {
        putStringPreference(MSG_UID, msgUid);
    }


    public static String getMsgUid() {
        return getStringPreference(MSG_UID, "-1");
    }

    public static void setPoiTime(String poiTime) {
        putStringPreference(POI_TIME, poiTime);
    }


    public static String getPoiTime() {
        return getStringPreference(POI_TIME, "0");
    }

    /**
     */
    public static void setUserPass(String userCode) {
        putStringPreference(PREF_USER_PASS, userCode);
    }


    public static String getUserPass() {
        return getStringPreference(PREF_USER_PASS, null);
    }


    public static void putStringPreference(String key, String value) {
        SharedPreferences preferences = getSharedPreference();
        preferences.edit().putString(key, value).apply();
    }

    public static String getStringPreference(String key, String defaultValue) {
        SharedPreferences preferences = getSharedPreference();
        return preferences.getString(key, defaultValue);
    }

    public static void putLongPreference(String key, long value) {
        SharedPreferences preferences = getSharedPreference();
        preferences.edit().putLong(key, value).apply();
    }

    public static long getLongPreference(String key, long defaultValue) {
        SharedPreferences preferences = getSharedPreference();
        return preferences.getLong(key, defaultValue);
    }

    public static void putBooleanPreference(String key, boolean value) {
        SharedPreferences preferences = getSharedPreference();
        preferences.edit().putBoolean(key, value).apply();
    }

    public static boolean getBooleanPreference(String key, boolean defaultValue) {
        SharedPreferences preferences = getSharedPreference();
        return preferences.getBoolean(key, defaultValue);
    }

    public static void setSharedIntData(Context context, String key, int value) {
        if (sp == null) {
            init(context);
        }
        sp.edit().putInt(key, value).commit();
    }

    public static int getSharedIntData(Context context, String key) {
        if (sp == null) {
            init(context);
        }
        return sp.getInt(key, 0);
    }

    public static void setSharedlongData(Context context, String key, long value) {
        if (sp == null) {
            init(context);
        }
        sp.edit().putLong(key, value).commit();
    }

    public static long getSharedlongData(Context context, String key) {
        if (sp == null) {
            init(context);
        }
        return sp.getLong(key, 0l);
    }

    public static void setSharedFloatData(Context context, String key,
                                          float value) {
        if (sp == null) {
            init(context);
        }
        sp.edit().putFloat(key, value).commit();
    }

    public static Float getSharedFloatData(Context context, String key) {
        if (sp == null) {
            init(context);
        }
        return sp.getFloat(key, 0f);
    }

    public static void setSharedBooleanData(Context context, String key,
                                            boolean value) {
        if (sp == null) {
            init(context);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    public static Boolean getSharedBooleanData(Context context, String key) {
        if (sp == null) {
            init(context);
        }
        return sp.getBoolean(key, false);
    }

    public static void setSharedStringData(Context context, String key, String value) {
        if (sp == null) {
            init(context);
        }
        sp.edit().putString(key, value).commit();
    }

    public static String getSharedStringData(Context context, String key) {
        if (sp == null) {
            init(context);
        }
        return sp.getString(key, "");
    }

}