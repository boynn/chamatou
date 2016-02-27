package cn.chamatou.biz.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import cn.chamatou.biz.R;

/**
 * 应用程序首页
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2016-1-21
 */
public class Main extends FragmentActivity {

	public RadioButton home;
	private static FragmentManager fMgr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//获取FragmentManager实例
		fMgr = getSupportFragmentManager();
		initView();
		initFragment();	
		initListener();
	}
	/**
	 * 初始化首个Fragment
	 */
	private void initFragment() {
		FragmentTransaction ft = fMgr.beginTransaction();
		MainFragment mainFragment = new MainFragment();
		ft.replace(R.id.mainview, mainFragment, "mainFragment");
		ft.addToBackStack("mainFragment");
		ft.commit();		
	}
	public void initView(){
		home=(RadioButton) findViewById(R.id.main_footbar_home);			
	}


	/**
	 * 处理底部点击事件
	 */
	public void initListener() {
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(fMgr.findFragmentByTag("mainFragment")!=null && fMgr.findFragmentByTag("mainFragment").isVisible()) {
					return;
				}
				popAllFragmentsExceptTheBottomOne();

			}
			
		});
		findViewById(R.id.main_footbar_msg).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft = fMgr.beginTransaction();
				ft.hide(fMgr.findFragmentByTag("mainFragment"));
				MsgFragment sf = new MsgFragment();
				ft.replace(R.id.mainview, sf, "msgFragment");
				ft.addToBackStack("msgFragment");
				ft.commit();
			}
		});
		findViewById(R.id.main_footbar_myinfo).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft = fMgr.beginTransaction();
				ft.hide(fMgr.findFragmentByTag("mainFragment"));
				MyFragment sf = new MyFragment();
				ft.add(R.id.mainview, sf, "myFragment");
				ft.addToBackStack("myFragment");
				ft.commit();
			}

		});
	}
	
	/**
	 * 从back stack弹出所有的fragment，保留首页的那个
	 */
	public static void popAllFragmentsExceptTheBottomOne() {
		for (int i = 0, count = fMgr.getBackStackEntryCount() - 1; i < count; i++) {
			fMgr.popBackStack();
		}
	}
	
//	private void initFrameButton()
//    {
//    	
//    }    
//   

}
