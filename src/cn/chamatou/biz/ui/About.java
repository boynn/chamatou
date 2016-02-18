package cn.chamatou.biz.ui;

import cn.chamatou.biz.R;
import cn.chamatou.biz.common.UpdateManager;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 关于我们
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class About extends Activity{
	
	private TextView mVersion,txt;
	private Button mUpdate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		//获取客户端版本信息
        try { 
        	PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
        	mVersion = (TextView)findViewById(R.id.about_version);
        	txt = (TextView)findViewById(R.id.textView1);
    		mVersion.setText("版本："+info.versionName);
    		 txt.setTypeface( Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf"));
        } catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
        
        mUpdate = (Button)findViewById(R.id.about_update);
        mUpdate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				UpdateManager.getUpdateManager().checkAppUpdate(About.this, true);
			}
		});
	}
}
