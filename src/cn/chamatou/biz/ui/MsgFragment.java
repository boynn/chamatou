package cn.chamatou.biz.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import cn.chamatou.biz.R;

public class MsgFragment extends Fragment{
	private Main context;
	private View view;
	private RadioGroup rg_club;//radioGroup
	private RadioButton rb_our_club;//本区圈子
	private RadioButton rb_hot_club;//最热圈子
	private RadioButton rb_mine_club;//我的圈子
	RadioButton clubbutton[] = null;
	private ViewPager club_viewpager;//viewpager
	private ArrayList<Fragment> fragmentsList;//viewpager里面fragment列表
	
	@Override
	 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.msg_activity, container, false);
		context = (Main) getActivity();
		initView();
		initListener();
		return view;
	}
	 
	public void initView(){
			((TextView)view.findViewById(R.id.title)).setText("消息");

	}
	
	

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

	//监听
	public void initListener() {
		
		
		view.findViewById(R.id.title).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				context.home.performClick();
			}
		});
	}
	
	
	// viewpager里面的frgment
	public void initClubViewPager() {
//		fragmentsList = new ArrayList<Fragment>();
//		fragmentsList.add(0,new OurClubFragment());
//		fragmentsList.add(1,new MostHotClubFragment());
//		fragmentsList.add(2,new MyClubFragment());
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	
}
