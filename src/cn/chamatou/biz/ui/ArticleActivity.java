package cn.chamatou.biz.ui;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.chamatou.biz.R;
import cn.chamatou.biz.adapter.ArticleViewPagerAdapter;
import cn.chamatou.biz.common.UIHelper;

public class ArticleActivity extends BaseWithTitleFragmentActivity{
	private Button btn_new;	
	private RadioGroup rg_article;//radioGroup
	private RadioButton rb_story;//茶典故
	private RadioButton rb_skill;//茶艺
	private RadioButton rb_health;//茶养生
	private RadioButton rb_joke;//茶段子
	RadioButton rbutton[] = null;
	private ViewPager rt_viewpager;//viewpager
	private ArrayList<Fragment> fragmentsList;//viewpager里面fragment列表
	private ArticleViewPagerAdapter rtViewPagerAdapter;
	
	//private ViewFlipper flipper;
	public Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.article_activity);
		initClubViewPager();
		context = ArticleActivity.this;
		super.onCreate(savedInstanceState);

	}



	//初始化控件
	@Override
	public void initView() {
		btn_new = (Button)findViewById(R.id.finish_btn);
		btn_new.setText("发文");
		btn_new.setVisibility(View.VISIBLE);
		btn_new.setOnClickListener(newArticleClickListener);
		rg_article = (RadioGroup) findViewById(R.id.rg_rt);
		rb_story = (RadioButton) findViewById(R.id.rb_story);
		rb_skill = (RadioButton) findViewById(R.id.rb_skill);
		rb_health = (RadioButton) findViewById(R.id.rb_health);
		rb_joke = (RadioButton) findViewById(R.id.rb_joke);
		rt_viewpager = (ViewPager) findViewById(R.id.rt_viewpager);
		rt_viewpager.setOnPageChangeListener(new ViewPagerListener());
		rtViewPagerAdapter = new ArticleViewPagerAdapter(getSupportFragmentManager(), fragmentsList);
		rt_viewpager.setAdapter(rtViewPagerAdapter);
		
		//flipper = (ViewFlipper)findViewById(R.id.f1);

	}
	private View.OnClickListener newArticleClickListener = new View.OnClickListener(){
		public void onClick(View v) {
			UIHelper.showNewArticle(v.getContext());
		}		
	};
	//监听
	@Override
	public void initListener() {
		
		rbutton = new RadioButton[] {rb_story, rb_skill,rb_health,rb_joke};
		rg_article.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_story://讨论区
					rb_story.setBackgroundResource(R.drawable.tab41);
					rt_viewpager.setCurrentItem(0);
					break;
				case R.id.rb_skill://活动区
					rg_article.setBackgroundResource(R.drawable.tab42);
					rt_viewpager.setCurrentItem(1);
					break;
				case R.id.rb_health://委员会
					rg_article.setBackgroundResource(R.drawable.tab43);
					rt_viewpager.setCurrentItem(2);
					break;
				case R.id.rb_joke://委员会
					rg_article.setBackgroundResource(R.drawable.tab44);
					rt_viewpager.setCurrentItem(3);
					break;
				}
			}
		});
	}
	
	// viewpager 滑动监听
	public class ViewPagerListener implements OnPageChangeListener {
		RadioButton radiobutton[] = new RadioButton[] { rb_story, rb_skill,rb_health,rb_joke};
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int arg0) {
			radiobutton[arg0].setChecked(true);
		}
	}

	// viewpager里面的frgment
	public void initClubViewPager() {
		fragmentsList = new ArrayList<Fragment>();
		fragmentsList.add(0,new ArticleFragment());
		//fragmentsList.add(1,new BBSHuodongFragment());
		//fragmentsList.add(2,new MyClubFragment());
		//fragmentsList.add(0,new BBSTalkFragment());
		//fragmentsList.add(1,new BBSEventFragment());
		//fragmentsList.add(2,new BBSCommitteeFragment());
		//fragmentsList.add(3,new BBSVoteFragment());
	}
	
		
	@Override
	public String getTitleStr() {
		return "我的茶文";
	}
}
