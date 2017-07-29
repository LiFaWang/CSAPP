package net.huansi.csapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class SpUtils {
    public static void saveMobileNo(Context context, String mobileNo){
        SharedPreferences sp = context.getSharedPreferences("MobileNo",context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("ID",mobileNo);
        edit.commit();
    }
    public static String getMobileNo(Context context){
        SharedPreferences sp = context.getSharedPreferences("MobileNo",context.MODE_PRIVATE);
        String id = sp.getString("ID", "");
        return id;
    }
    /**
     * 保存数据
     * @param context
     * @param key 键
     * @param value 值
     */
    public static void saveSpData(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences("spData",context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.commit();
    }

    /**
     * 通过key获取值
     * @param context
     * @param key   键
     * @param defaultValue 默认值
     * @return
     */
    public static String getSpData(Context context,String key,String defaultValue){
        SharedPreferences sp = context.getSharedPreferences("spData",context.MODE_PRIVATE);
        String value = sp.getString(key, defaultValue);
        return value;
    }
}
