package cn.chamatou.biz.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

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
import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.R;
import cn.chamatou.biz.adapter.ArticleAdapter;
import cn.chamatou.biz.adapter.MsgAdapter;
import cn.chamatou.biz.async.Callback;
import cn.chamatou.biz.bean.Article;
import cn.chamatou.biz.bean.MsgBean;
import cn.chamatou.biz.common.LocalPreferences;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIUtils;
import cn.chamatou.biz.data.StoreData;
import cn.chamatou.biz.widget.ScrollViewWithListView;

public class MsgFragment extends Fragment{
	private Main context;
	private View view;
	private ScrollViewWithListView msg_listView;
	private MsgAdapter msgAdapter;
	
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
			msg_listView = (ScrollViewWithListView) view.findViewById(R.id.msg_listView);
			msgAdapter = new MsgAdapter(context, new ArrayList<MsgBean>());
			msg_listView.setAdapter(msgAdapter);

	}
	
	@Override
	public void onResume() {
		getMessage(0);
		super.onResume();
	}

	// 获取圈子帖子
	private void getMessage(int page) {
		String cache = LocalPreferences.getInstance(context).getCacheString(
				"MsgFragtment:getMessage");
		if (!StringUtils.isEmpty(cache)) {
			Type type = new TypeToken<List<MsgBean>>() {
			}.getType();
			Gson gson = new Gson();
			List<MsgBean> data = gson.fromJson(cache, type);
			//bbsTitleAdapter.setListdata(data);
			msgAdapter.notifyDataSetChanged();
			msgAdapter.setListdata(data);
			msgAdapter.notifyDataSetChanged();
		}

		StoreData.getMessage(new Callback<String>() {
			@Override
			public void onHandle(String param) {
				UIUtils.doS(context, param, new Callback<String>() {
					@Override
					public void onHandle(String param) {
						// TODO Auto-generated method stub
						Type type = new TypeToken<List<MsgBean>>() {
						}.getType();
						System.out.println("msg:" + param);
						Gson gson = new Gson();
						try{
						List<MsgBean> data = gson.fromJson(param, type);
						msgAdapter.setListdata(data);
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
							
						}
						msgAdapter.notifyDataSetChanged();
					}
				});
			}
		},AppContext.self.getLoginUid(),0,page);
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
	
	
	
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	
}
