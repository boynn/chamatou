package cn.chamatou.biz.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public abstract class CommonDialog extends Dialog {
	protected Context context;
	private Dialog self;

	public CommonDialog(Context context, int layout, int style) {
		super(context, style);
		setContentView(layout);
		this.context = context;
		self = this;
		Window window = getWindow();
		
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
	
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		initListener();
		if(!(context instanceof Activity)){
			getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		}
	}

	public abstract void initListener();
	
	public  void closeListener(){
		
	}

	private float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.density;
	}
	
	public void closeDialog(){
		self.dismiss();
		closeListener();
	}

	@Override
	public void onBackPressed() {
	}
	
	
}
