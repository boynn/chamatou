package cn.chamatou.biz.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.chamatou.biz.Const;
import cn.chamatou.biz.R;
import cn.chamatou.biz.common.AccountTool;
import cn.chamatou.biz.common.LocalPreferences;
import cn.chamatou.biz.widget.draggrid.ChannelItem;
import cn.chamatou.biz.widget.draggrid.DragAdapter;
import cn.chamatou.biz.widget.draggrid.DragGrid;
import cn.chamatou.biz.widget.draggrid.OtherAdapter;
import cn.chamatou.biz.widget.draggrid.OtherGridView;

/**
 * 首页功能管理
 */
public class ChannelActivity extends BaseWithTitleActivity implements OnItemClickListener {
	public Context context;
	public static String TAG = "ChannelActivity";
	/** 用户栏目的GRIDVIEW */
	private DragGrid userGridView;
	/** 其它栏目的GRIDVIEWa */
	private OtherGridView otherGridView;
	/** 用户栏目对应的适配器，可以拖动 */
	DragAdapter userAdapter;
	/** 其它栏目对应的适配器 */
	OtherAdapter otherAdapter;
	/** 其它栏目列表 */
	List<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
	/** 用户栏目列表 */
	List<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
	boolean isMove = false;

	private View finishButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context=this;
		setContentView(R.layout.channel);
		isNeedShowNewUserIndex();
		super.onCreate(savedInstanceState);
		initView();
	}

	
	
	public void isNeedShowNewUserIndex() {
		int time = LocalPreferences.getInstance(context).getCacheInt(
				"ChannelActivity");
			LocalPreferences.getInstance(context).saveCahceInt(
					"ChannelActivity", 1);		
	}
	
	public void initView() {
		try {
			finishButton = findViewById(R.id.finish_btn);
			finishButton.setVisibility(View.VISIBLE);
			finishButton.setOnClickListener(new OnFinishClickListener());

			userGridView = (DragGrid) findViewById(R.id.userGridView);
			otherGridView = (OtherGridView) findViewById(R.id.otherGridView);

			userChannelList = new ArrayList<ChannelItem>();
			otherChannelList = new ArrayList<ChannelItem>();
			SharedPreferences sharedPreferences = new AccountTool(ChannelActivity.this).getPreferences();
			String menusStr = sharedPreferences.getString("funcs", Const.funcs.toString());
			JSONArray rows = new JSONArray(menusStr);
			HashSet<Integer> idSet = new HashSet<Integer>();
			for (int i = 0; i < rows.length(); i++) {
				Integer key = rows.getInt(i);
				idSet.add(key);
				if (Const.defaultUserChannelMap.containsKey(key)) {
					userChannelList.add(Const.defaultUserChannelMap.get(key));
				}
			}
			for (ChannelItem channelItem : Const.defaultUserChannelMap.values()) {
				if (!idSet.contains(channelItem.getId())) {
					otherChannelList.add(channelItem);
				}
			}

			// 初始化数据
			userAdapter = new DragAdapter(this, userChannelList);
			userGridView.setAdapter(userAdapter);
			otherAdapter = new OtherAdapter(this, otherChannelList);
			otherGridView.setAdapter(otherAdapter);
			// 设置GRIDVIEW的ITEM的点击监听
			otherGridView.setOnItemClickListener(this);
			userGridView.setOnItemClickListener(this);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/** GRIDVIEW对应的ITEM点击监听接口 */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
		// 如果点击的时候，之前动画还没结束，那么就让点击事件无效
		if (isMove) {
			return;
		}
		switch (parent.getId()) {
		case R.id.userGridView:
		// position为 0，1 的不可以进行任何操作
		// if (position != 0 && position != 1)
		{
			final ImageView moveImageView = getView(view);
			if (moveImageView != null) {
				TextView newTextView = (TextView) view.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final ChannelItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);// 获取点击的频道内容
				otherAdapter.setVisible(false);
				// 添加到最后一个
				otherAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// 获取终点的坐标
							otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
							MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
							userAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
		}
			break;
		case R.id.otherGridView:
			final ImageView moveImageView = getView(view);
			if (moveImageView != null) {
				TextView newTextView = (TextView) view.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final ChannelItem channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
				userAdapter.setVisible(false);
				// 添加到最后一个
				userAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// 获取终点的坐标
							userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
							MoveAnim(moveImageView, startLocation, endLocation, channel, otherGridView);
							otherAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 点击ITEM移动动画
	 * 
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelItem moveChannel, final GridView clickGridView) {
		int[] initLocation = new int[2];
		// 获取传递过来的VIEW的坐标
		moveView.getLocationInWindow(initLocation);
		// 得到要移动的VIEW,并放入对应的容器中
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
		// 创建移动动画
		TranslateAnimation moveAnimation = new TranslateAnimation(startLocation[0], endLocation[0], startLocation[1], endLocation[1]);
		moveAnimation.setDuration(300L);// 动画时间
		// 动画配置
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				isMove = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				moveViewGroup.removeView(mMoveView);
				// instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
				if (clickGridView instanceof DragGrid) {
					otherAdapter.setVisible(true);
					otherAdapter.notifyDataSetChanged();
					userAdapter.remove();
				} else {
					userAdapter.setVisible(true);
					userAdapter.notifyDataSetChanged();
					otherAdapter.remove();
				}
				isMove = false;
			}
		});
	}

	/**
	 * 获取移动的VIEW，放入对应ViewGroup布局容器
	 * 
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}

	/**
	 * 创建移动的ITEM对应的ViewGroup布局容器
	 */
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}

	/**
	 * 获取点击的Item的对应View，
	 * 
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}

	private class OnFinishClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			List<ChannelItem> items = userAdapter.getChannnelLst();

			JSONArray rows = new JSONArray();
			for (ChannelItem channelItem : items) {
				rows.put(channelItem.getId());
			}
			SharedPreferences sharedPreferences = new AccountTool(ChannelActivity.this).getPreferences();

			Editor editor = sharedPreferences.edit();
			editor.putString("funcs", rows.toString());
			editor.commit();

			finish();
		}
	}

	@Override
	public void initListener() {
		// TODO 自动生成的方法存根
		
	}



	@Override
	public String getTitleStr() {
		// TODO 自动生成的方法存根
		return "功能面板";
	}

}
