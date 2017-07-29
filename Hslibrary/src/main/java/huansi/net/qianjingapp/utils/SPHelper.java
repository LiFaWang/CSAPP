package huansi.net.qianjingapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPHelper {

	public static final String POPULAR="流行趋势";
	public static final String JOB="行业信息";
	public static final String TRAIN="培训资料";
	public static final String SPREAD="推广引导";


	public static void saveLocalData(Context context, String key, Object value,String  className) {
		SharedPreferences preferences = context.getSharedPreferences("CCApp", Activity.MODE_PRIVATE);
		Editor editor = preferences.edit();

		if (className.equalsIgnoreCase(Integer.class.getName())) {
			editor.putInt(key, (Integer) value);
		} else if (className.equalsIgnoreCase(String.class.getName())) {
			editor.putString(key, value.toString());
		} else if (className.equalsIgnoreCase(Boolean.class.getName())) {
			editor.putBoolean(key, (Boolean) value);
		} else if (className.equalsIgnoreCase(Float.class.getName())) {
			editor.putFloat(key, (Float) value);
		} else if (className.equalsIgnoreCase(Long.class.getName())) {
			editor.putLong(key, (Long) value);
		}
		editor.commit();
	}

	public static Object getLocalData(Context context, String key,String  className,Object defaultValue) {
		SharedPreferences preferences = context.getSharedPreferences("CCApp", Activity.MODE_PRIVATE);
		if(className.equalsIgnoreCase(Integer.class.getName())) return preferences.getInt(key, (Integer) defaultValue);
		if(className.equalsIgnoreCase(String.class.getName())) return preferences.getString(key, defaultValue.toString());
		if(className.equalsIgnoreCase(Boolean.class.getName())) return preferences.getBoolean(key, (Boolean) defaultValue);
		if(className.equalsIgnoreCase(Float.class.getName())) return preferences.getFloat(key, (Float) defaultValue);
		if(className.equalsIgnoreCase(Long.class.getName())) return preferences.getLong(key, (Long) defaultValue);
        return "";
	}
}
