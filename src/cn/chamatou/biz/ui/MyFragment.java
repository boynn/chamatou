package cn.chamatou.biz.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import cn.chamatou.biz.R;
import cn.chamatou.biz.common.AccountTool;


public class MyFragment extends Fragment {

	private View view;
	private Activity activity;
	BroadcastReceiver receiver;

	public static int MY_SHORT = 0;
	protected Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		activity = getActivity();
		super.onCreate(savedInstanceState);
		//accountTool = new AccountTool(activity.getBaseContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.my_center, container, false);
		//initNameView();
		//getPersonalMap();
		
		
		view.findViewById(R.id.me_data).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					Intent intent = new Intent(getActivity(), EditStore.class);
					getActivity().startActivity(intent);
			}
		});

		view.findViewById(R.id.me_order).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Intent intent = new Intent(getActivity(), OrderActivity.class);
				//startActivity(intent);				
			}
		});

		view.findViewById(R.id.about_btn).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), About.class);
				startActivity(intent);
			}
		});

		view.findViewById(R.id.ch_number).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), LoginDialog.class);
				String phone = PreferenceManager.getDefaultSharedPreferences(activity).getString("RESIDENT_MOBILE", "");
				intent.putExtra("phone", phone);
				startActivity(intent);
			}
		});
		view.findViewById(R.id.share).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					//ShareUtils.showShare(getActivity());
				
			}
		});

		view.findViewById(R.id.quit_btn).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AccountTool tool = new AccountTool(activity);
				tool.clearPreference();

				activity.finish();
			}
		});
		return view;
	}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		//initNameView();
		//getPersonalMap();
		super.onStart();
	}
	
}
