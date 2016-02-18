package cn.chamatou.biz.ui;

import java.io.File;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.R;
import cn.chamatou.biz.common.FileUtils;
import cn.chamatou.biz.common.MethodsCompat;
import cn.chamatou.biz.common.UIHelper;
import cn.chamatou.biz.common.UpdateManager;

public class Setting extends PreferenceActivity{
	
	SharedPreferences mPreferences;
	Preference account;
	Preference myinfo;
	Preference cache;
	Preference feedback;
	Preference update;
	Preference about;
	CheckBoxPreference httpslogin;
	CheckBoxPreference loadimage;
	CheckBoxPreference scroll;
	CheckBoxPreference voice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//设置显示Preferences
		addPreferencesFromResource(R.xml.preferences);
		//获得SharedPreferences
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);		
		
		ListView localListView = getListView();
		localListView.setBackgroundColor(0);
		localListView.setCacheColorHint(0);
		((ViewGroup)localListView.getParent()).removeView(localListView);
		ViewGroup localViewGroup = (ViewGroup)getLayoutInflater().inflate(R.layout.setting, null);
		((ViewGroup)localViewGroup.findViewById(R.id.setting_content)).addView(localListView, -1, -1);
		setContentView(localViewGroup);
	      
	    
		final AppContext ac = (AppContext)getApplication();
		
	
		//我的资料
		myinfo = (Preference)findPreference("myinfo");
		myinfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.showSetPwdDialog(Setting.this);
				return true;
			}
		});
		
		//https登录
		httpslogin = (CheckBoxPreference)findPreference("httpslogin");
		httpslogin.setChecked(ac.isHttpsLogin());
		if(ac.isHttpsLogin()){
			httpslogin.setSummary("可显示内部信息");
		}else{
			httpslogin.setSummary("不显示内部信息");
		}
		httpslogin.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				if(httpslogin.isChecked()){
					UIHelper.showConfirmDialog(Setting.this);
					httpslogin.setSummary("可显示内部信息");
				}else{
					ac.setConfigHttpsLogin(false);
					httpslogin.setSummary("不显示内部信息");
				}
				UIHelper.sendBroadCastGoods(Setting.this);
				return true;
			}
		});
		
		//加载图片loadimage
		loadimage = (CheckBoxPreference)findPreference("loadimage");
		loadimage.setChecked(ac.isLoadImage());
		if(ac.isLoadImage()){
			loadimage.setSummary("页面加载图片 (默认在WIFI网络下加载图片)");
		}else{
			loadimage.setSummary("页面不加载图片 (默认在WIFI网络下加载图片)");
		}
		loadimage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.changeSettingIsLoadImage(Setting.this,loadimage.isChecked());
				if(loadimage.isChecked()){
					loadimage.setSummary("页面加载图片 (默认在WIFI网络下加载图片)");
				}else{
					loadimage.setSummary("页面不加载图片 (默认在WIFI网络下加载图片)");
				}
				return true;
			}
		});
		
		//左右滑动
		scroll = (CheckBoxPreference)findPreference("scroll");
		scroll.setChecked(ac.isScroll());
		if(ac.isScroll()){
			scroll.setSummary("已启用左右滑动");
		}else{
			scroll.setSummary("已关闭左右滑动");
		}
		scroll.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				ac.setConfigScroll(scroll.isChecked());
				if(scroll.isChecked()){
					scroll.setSummary("已启用左右滑动");
				}else{
					scroll.setSummary("已关闭左右滑动");
				}
				return true;
			}
		});
		
		//提示声音
		voice = (CheckBoxPreference)findPreference("voice");
		voice.setChecked(ac.isVoice());
		if(ac.isVoice()){
			voice.setSummary("已开启提示声音");
		}else{
			voice.setSummary("已关闭提示声音");
		}
		voice.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				ac.setConfigVoice(voice.isChecked());
				if(voice.isChecked()){
					voice.setSummary("已开启提示声音");
				}else{
					voice.setSummary("已关闭提示声音");
				}
				return true;
			}
		});
		
		//计算缓存大小		
		long fileSize = 0;
		String cacheSize = "0KB";		
		File filesDir = getFilesDir();
		File cacheDir = getCacheDir();
		
		fileSize += FileUtils.getDirSize(filesDir);
		fileSize += FileUtils.getDirSize(cacheDir);		
		//2.2版本才有将应用缓存转移到sd卡的功能
		if(AppContext.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)){
			File externalCacheDir = MethodsCompat.getExternalCacheDir(this);
			fileSize += FileUtils.getDirSize(externalCacheDir);
		}		
		if(fileSize > 0)
			cacheSize = FileUtils.formatFileSize(fileSize);
		
		//清除缓存
		cache = (Preference)findPreference("cache");
		cache.setSummary(cacheSize);
		cache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.clearAppCache(Setting.this);
				cache.setSummary("0KB");
				return true;
			}
		});
		
		
		//版本更新
		update = (Preference)findPreference("update");
		update.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UpdateManager.getUpdateManager().checkAppUpdate(Setting.this, true);
				return true;
			}
		});
		
		//关于我们
		about = (Preference)findPreference("about");
		about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.showAbout(Setting.this);
				return true;
			}
		});
		
	}
	public void back(View paramView)
	{
		finish();
	}
	
}
