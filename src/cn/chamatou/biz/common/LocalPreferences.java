package cn.chamatou.biz.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 整个应用公用的sharedPreferences
 * @author Kaka
 *
 */
public class LocalPreferences {
	private static LocalPreferences instance = null;
	private SharedPreferences preferences;
	private final String PREFERENCE_SNAME = "cache";
	
	private LocalPreferences(Context context){
		preferences = context.getSharedPreferences(PREFERENCE_SNAME, 0);
	}
	
	public static LocalPreferences getInstance( Context context) {
		if(instance == null){
			instance = new LocalPreferences(context);
		}
		return instance;
	}
	
	public void saveCacheString(String key, String value){
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public String getCacheString(String key){
		String value = preferences.getString(key, "");
		return value;
	}
	
	public void saveCahceInt(String key ,int value){
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
 
	public int getCacheInt(String key){
		int value = preferences.getInt(key, 0);
		return value;
	}
	
	public void saveCahceLong(String key ,long value){
		Editor editor = preferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	public long getCacheLong(String key){
		long value = preferences.getLong(key, 0);
		return value;
	}
	
	public void clearCache(){
		preferences.edit().clear().commit();
	}

}
