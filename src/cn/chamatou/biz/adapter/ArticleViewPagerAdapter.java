package cn.chamatou.biz.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ArticleViewPagerAdapter extends FragmentPagerAdapter{
	private ArrayList<Fragment> fragmentsList;

	public ArticleViewPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public ArticleViewPagerAdapter(FragmentManager fm,ArrayList<Fragment> fragmentsList) {
		super(fm);
		this.fragmentsList = fragmentsList;
	}

	@Override
	public int getCount() {
		return fragmentsList.size();
	}


	@Override
	public Fragment getItem(int arg0) {

		return fragmentsList.get(arg0);
	}


	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}



}
