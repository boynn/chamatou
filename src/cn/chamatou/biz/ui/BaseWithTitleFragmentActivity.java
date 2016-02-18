package cn.chamatou.biz.ui;

import com.eteamsun.gather.base.BaseFragmentGatherActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.chamatou.biz.R;

public abstract class BaseWithTitleFragmentActivity extends BaseFragmentGatherActivity {
	public Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		findViewById(R.id.title).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		initView();
		initListener();

		((TextView) findViewById(R.id.title)).setText(getTitleStr());
	}

	public abstract void initListener();

	public abstract void initView();

	public abstract String getTitleStr();

	public <T> void gotoActivity(Context context, Class<T> className) {
		Intent intent = new Intent(context, className);
		context.startActivity(intent);
	}
}
