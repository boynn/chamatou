package cn.chamatou.biz.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.R;
import cn.chamatou.biz.adapter.ArticleAdapter;
import cn.chamatou.biz.async.Callback;
import cn.chamatou.biz.bean.Article;
import cn.chamatou.biz.common.AccountTool;
import cn.chamatou.biz.common.LocalPreferences;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIUtils;
import cn.chamatou.biz.data.StoreData;
import cn.chamatou.biz.widget.ScrollViewWithListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ArticleFragment extends Fragment{
	private Context context;
	private View view;
	//private ScrollViewWithListView title_listView;
	private ScrollViewWithListView comm_num_listView;
	//private BBSTitleAdapter bbsTitleAdapter;
	private ArticleAdapter commAdapter;
	public static int at_id;//评论和赞的帖子id
	//private ImageButton bbs_fatie;
	private InputMethodManager imm;
	//private int currentPage = 0;
	boolean isfatie = true;
	private static int user_state = -1;// 判断点赞状态
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.article_fragment, null);
		context = getActivity();
		initView();
		return view;
	}
	
	// 初始化控件
	public void initView() {
		//bbs_fatie = (ImageButton) view.findViewById(R.id.bbs_fatie);
		
//		title_listView = (ScrollViewWithListView) view.findViewById(R.id.title_listView);
//		bbsTitleAdapter = new BBSTitleAdapter(context,
//				new ArrayList<BBSPostBean>());
//		title_listView.setAdapter(bbsTitleAdapter);

		comm_num_listView = (ScrollViewWithListView) view.findViewById(R.id.comm_num_listView);
		commAdapter = new ArticleAdapter(context, new ArrayList<Article>());
		comm_num_listView.setAdapter(commAdapter);

	}
	@Override
	public void onResume() {
		getBBSPost();
		super.onResume();
	}

	// 获取圈子帖子
	private void getBBSPost() {
		String cache = LocalPreferences.getInstance(context).getCacheString(
				"ArticleFragtment:getBBSPost");
		if (!StringUtils.isEmpty(cache)) {
			Type type = new TypeToken<List<Article>>() {
			}.getType();
			Gson gson = new Gson();
			List<Article> data = gson.fromJson(cache, type);
			//bbsTitleAdapter.setListdata(data);
			commAdapter.notifyDataSetChanged();
			commAdapter.setListdata(data);
			commAdapter.notifyDataSetChanged();
		}

		StoreData.getMyArticle(new Callback<String>() {
			@Override
			public void onHandle(String param) {
				UIUtils.doS(context, param, new Callback<String>() {
					@Override
					public void onHandle(String param) {
						// TODO Auto-generated method stub
						Type type = new TypeToken<List<Article>>() {
						}.getType();
						Gson gson = new Gson();
						List<Article> data = gson.fromJson(param, type);
						//bbsTitleAdapter.setListdata(data);
						//bbsTitleAdapter.notifyDataSetChanged();
						commAdapter.setListdata(data);
						commAdapter.notifyDataSetChanged();
					}
				});
			}
		},AppContext.self.getLoginUid(),1);
	}

	// 防止乱pageview乱滚动
	private OnTouchListener forbidenScroll() {
		return new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		};
	}

	public static int getUser_state() {
		return user_state;
	}
	
}
