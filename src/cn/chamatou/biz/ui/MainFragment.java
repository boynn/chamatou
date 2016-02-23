package cn.chamatou.biz.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.Const;
import cn.chamatou.biz.R;
import cn.chamatou.biz.adapter.DataGridViewAdapter;
import cn.chamatou.biz.adapter.FuncGridViewAdapter;
import cn.chamatou.biz.adapter.FuncGridViewAdapter.ViewHolder;
import cn.chamatou.biz.async.AsyncUtil;
import cn.chamatou.biz.async.Callback;
import cn.chamatou.biz.async.Result;
import cn.chamatou.biz.bean.GridDataBean;
import cn.chamatou.biz.bean.GridFuncBean;
import cn.chamatou.biz.bean.MyInfo;
import cn.chamatou.biz.common.AccountTool;
import cn.chamatou.biz.data.StoreData;
import cn.chamatou.biz.widget.DialogUtil;
import cn.chamatou.biz.widget.draggrid.ChannelItem;

import com.nostra13.universalimageloader.core.ImageLoader;

public class MainFragment extends Fragment implements OnClickListener {
	private View view;
	private Context context;
	private ImageView simg;
	private GridView datagridView, funcgridView;
	private LinearLayout la1,la2,la3;
	private FuncGridViewAdapter funcGridViewAdapter;
	private DataGridViewAdapter dataGridViewAdapter;
	private boolean isShowAllGridViewItem = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.main_fragment, null);
		context = getActivity();
		initView();
		//initListener();
		return view;
	}

	private void initListener() {
		datagridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startData(arg1);
			}

		});
		funcgridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 startChannel(arg1);
			}

		});

	}

	private void initView() {
		datagridView = (GridView) view.findViewById(R.id.datagrid);
		funcgridView = (GridView) view.findViewById(R.id.funcgrid);
		simg= (ImageView)view.findViewById(R.id.storelogo);
		la1= (LinearLayout)view.findViewById(R.id.apply1);
		la2= (LinearLayout)view.findViewById(R.id.apply2);
		la3= (LinearLayout)view.findViewById(R.id.apply3);
		
    	
		initListener();

	}

	@Override
	public void onStart() {

		// 刷新频道数据
		dataGridViewAdapter = new DataGridViewAdapter(context,
				initGridViewData());
		datagridView.setAdapter(dataGridViewAdapter);
		dataGridViewAdapter.notifyDataSetChanged();

		funcGridViewAdapter = new FuncGridViewAdapter(context,
				initGridViewFuncData());
		funcgridView.setAdapter(funcGridViewAdapter);
		funcGridViewAdapter.notifyDataSetChanged();
		if(AppContext.self.isR1())	la1.setVisibility(View.GONE);
		if(AppContext.self.isR2())	la2.setVisibility(View.GONE);
		if(AppContext.self.isR3())	la3.setVisibility(View.GONE);
		// banner.startTimer();
		// banner.loadBannerData();
		initMyInfo();
		super.onStart();
	}

	@Override
	public void onStop() {
		// banner.stopTimer();
		super.onStop();
	}

	@Override
	public void onClick(View v) {

	}

	public List<GridDataBean> initGridViewData() {
		List<GridDataBean> list = new ArrayList<GridDataBean>();

		try {
			SharedPreferences sharedPreferences = new AccountTool(getActivity())
					.getPreferences();
			JSONArray datas = new JSONArray(sharedPreferences.getString(
					"datas", AppContext.self.getMydatas()));
			int size = datas.length();
			if (!isShowAllGridViewItem) {
				if (size >= 6) {
					size = 5;
				}
			}
			//Const.defaultUserDataMap
			for (int i = 0; i < size; i++) {
				int index = datas.getInt(i);
				for (ChannelItem channelItem : Const.defaultUserDataMap.values()) {
					if(index==channelItem.getId()){
						list.add(new GridDataBean(index, channelItem.getName(), R.drawable.btn_grid,"0"));
						break;
					}
				}
			}
			
			list.add(new GridDataBean(20, "添加数据", R.drawable.btn_grid, "+"));
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<GridFuncBean> initGridViewFuncData() {
		List<GridFuncBean> list = new ArrayList<GridFuncBean>();
		try {
			//SharedPreferences sharedPreferences = new AccountTool(getActivity()).getPreferences();
			JSONArray funcs = new JSONArray(AppContext.self.getMyfuncs());
			int size = funcs.length();
//			if (!isShowAllGridViewItem) {
//				if (size >= 20) {
//					size = 7;
//				}
//			}

			for (int i = 0; i < size; i++) {
				int index = funcs.getInt(i);
				for (ChannelItem channelItem : Const.defaultUserChannelMap.values()) {
					if(index==channelItem.getId()){
						list.add(new GridFuncBean(index, channelItem.getName(), R.drawable.btn_grid,channelItem.getIconId()));
						break;
					}
				}
			}
//			list.add(new GridFuncBean(20, "添加功能", R.drawable.btn_grid,
//					R.drawable.add));
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return list;
	}
	
    private void initMyInfo() {
    	
    	//view.findViewById(R.id.list_progress).setVisibility(View.VISIBLE);
		AsyncUtil.goAsync(new Callable<Result<MyInfo>>() {

			@Override
			public Result<MyInfo> call() throws Exception {
				return StoreData.getMyInfo(AppContext.self.getLoginUid());
			}
		}, new Callback<Result<MyInfo>>() {

			@Override
			public void onHandle(Result<MyInfo> param) {
				//view.findViewById(R.id.list_progress).setVisibility(View.GONE);
				if (param.ok()) {
					ImageLoader.getInstance().displayImage(param.getData().getHead(),
							simg, cn.chamatou.biz.common.ImageLoaderUtil.createNormalOption());
					
					
				}
				
			}
		});
		
	}
    private void startChannel(View view) {
		AccountTool account = new AccountTool(context);

		FuncGridViewAdapter.ViewHolder holder = (ViewHolder) view.getTag();
		if (!holder.isOpenedToUser) {
			DialogUtil.alert(R.drawable.widget_dialog_icon, context, "提示",
					"该模块暂未开放");
			return;
		}

		switch (holder.index) {
		
		case 1:// 我的文章
			Intent intent1 = new Intent(context, ArticleActivity.class);
			startActivity(intent1);			
			break;
		case 2:// 我的活动
			Intent intent2 = new Intent(context, ArticleActivity.class);
			startActivity(intent2);			
			break;
		case 3:// 个人中心
			Intent intent3 = new Intent(context, EditPerson.class);
			startActivity(intent3);			
			break;
		case 4:// 茶云联供
			Intent intent4 = new Intent(context, EditStore.class);
			startActivity(intent4);			
			break;
		case 5:// 优惠活动

			//Intent intentlf = new Intent(context, BBSActivity.class);
			//startActivity(intentlf);
			break;
		case 6://数据分析
			break;
		case 7:// 门店管理
			Intent intent7 = new Intent(context, EditStore.class);
			startActivity(intent7);			
			break;
		case 8:// 店铺管理
			Intent intent8 = new Intent(context, EditMerchant.class);
			startActivity(intent8);			
			break;
		case 9:// 我的订单
			Intent intent9 = new Intent(context, OrderActivity.class);
			startActivity(intent9);
			 
			break;
		case 10:// 发布商品
			Intent intent10 = new Intent(context, GoodsActivity.class);
			startActivity(intent10);
			break;
		case 11://促销管理
			break;
		case 12://收入管理
			break;
		
				
		case 20:// 更多
			startActivity(new Intent(context, ChannelActivity.class));
		}
	}	
    
    private void startData(View view) {

		DataGridViewAdapter.ViewHolder holder = (cn.chamatou.biz.adapter.DataGridViewAdapter.ViewHolder) view.getTag();
		if (!holder.isOpenedToUser) {
			DialogUtil.alert(R.drawable.widget_dialog_icon, context, "提示",
					"该数据暂未开放");
			return;
		}

		switch (holder.index) {

		case 1:// 我的

			break;
		case 2:// 我的
			break;
		case 3:// 我的
			 
			break;
		case 4:// 我的
			break;

		case 5:// 我的

			//Intent intentlf = new Intent(context, BBSActivity.class);
			//startActivity(intentlf);
			break;
				
		case 20:// 更多
			startActivity(new Intent(context, UserDataActivity.class));
		}
	}	
}
