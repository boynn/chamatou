package cn.chamatou.biz.ui;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.AppException;
import cn.chamatou.biz.R;
import cn.chamatou.biz.adapter.ListViewGoodsAdapter;
import cn.chamatou.biz.bean.Goods;
import cn.chamatou.biz.bean.GoodsList;
import cn.chamatou.biz.bean.Notice;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIHelper;
import cn.chamatou.biz.widget.PullToRefreshListView;

public class GoodsActivity extends BaseWithTitleActivity {
	private Button btn_new;	
	private PullToRefreshListView listView;
	private ArrayList<Goods> listDatas=new ArrayList<Goods>();
	private ListViewGoodsAdapter listAdapter;
	private Handler lvGoodsHandler;
	private int lvGoodsSumData;
	private View lvGoods_footer;
	private TextView lvGoods_foot_more;
	private ProgressBar lvGoods_foot_progress;
	private AppContext appContext;// 全局Context
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.goods);
		super.onCreate(savedInstanceState);
		appContext = (AppContext) getApplication();
		lvGoodsHandler = this.getLvHandler(listView, listAdapter, lvGoods_foot_more, lvGoods_foot_progress, AppContext.PAGE_SIZE);  
		loadLvGoodsData(lvGoodsHandler, 0, UIHelper.LISTVIEW_ACTION_INIT);
		

	}

	private void loadLvGoodsData(final Handler handler, final int page,
			final int action) {
		// mHeadProgress.setVisibility(ProgressBar.VISIBLE);
		System.out.println("take goods403.........");
		if (page == 0)
			listDatas.clear();
		System.out.println("take goods4x3.........");
		new Thread() {
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				GoodsList list;
				try {
					list = appContext.getGoodsList(page, isRefresh);
					System.out.println("refresh goods.........");
					msg.what = list.getPageSize();
					msg.obj = list;
					msg.arg1 = action;
					msg.arg2 = UIHelper.LISTVIEW_DATATYPE_GOODS;
					handler.sendMessage(msg);
				} catch (AppException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
		}.start();
	}

	public void initListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			 @Override
	     	   public void onItemClick(AdapterView<?> parent, View v, int position,
	     	     long id) {
	     		  Goods egoods = listDatas.get(position-1);
	     		 //System.out.println("ddL"+goodsid);
	    		 UIHelper.EditGoods(v.getContext(), egoods);
	     	   }
	     	
		});

	}

	public void initView() {
		btn_new = (Button)findViewById(R.id.finish_btn);
		btn_new.setText("新增");
		btn_new.setVisibility(View.VISIBLE);
		btn_new.setOnClickListener(newGoodsClickListener);
		listAdapter = new ListViewGoodsAdapter(this, listDatas,
				R.layout.goods_listitem);
		lvGoods_footer = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		lvGoods_foot_more = (TextView) lvGoods_footer
				.findViewById(R.id.listview_foot_more);
		lvGoods_foot_progress = (ProgressBar) lvGoods_footer
				.findViewById(R.id.listview_foot_progress);
		listView = (PullToRefreshListView) findViewById(R.id.my_goods_list);
		listView.addFooterView(lvGoods_footer);// 添加底部视图 必须在setAdapter前
		try {
			listView.setAdapter(listAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				listView.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (listDatas.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvGoods_footer) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(listView.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
					lvGoods_foot_more.setText(R.string.load_ing);
					lvGoods_foot_progress.setVisibility(View.VISIBLE);
					int pageIndex = lvGoodsSumData / AppContext.PAGE_SIZE;
					loadLvGoodsData(lvGoodsHandler, pageIndex,
							UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				listView.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View v,
					int position, long id) {
				final Goods egoods = listDatas.get(position - 1);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						GoodsActivity.this);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setTitle(R.string.cleargoods);
				builder.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								try {
									String res =appContext.delGoods(egoods.getGoodsid());
									JSONObject ojson = new JSONObject(res);
									if (ojson.getInt("error") == 0)
										loadLvGoodsData(
												lvGoodsHandler,
												0,
												UIHelper.LISTVIEW_ACTION_REFRESH);
									// v.setVisibility(View.GONE);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (AppException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}

							}
						});
				builder.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.show();
				return true;
			}

		});

		listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				loadLvGoodsData(lvGoodsHandler, 0,
						UIHelper.LISTVIEW_ACTION_REFRESH);
			}
		});
		
	}
	private View.OnClickListener newGoodsClickListener = new View.OnClickListener(){
		public void onClick(View v) {
			UIHelper.showNewGoods(v.getContext());
		}		
	};

	private Handler getLvHandler(final PullToRefreshListView lv,
			final BaseAdapter adapter, final TextView more,
			final ProgressBar progress, final int pageSize) {
		return new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what >= 0) {
					// listview数据处理
					Notice notice = handleLvData(msg.what, msg.obj, msg.arg2,
							msg.arg1);
					if (msg.what < pageSize) {
						lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_full);
					} else if (msg.what == pageSize) {
						lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_more);

						// 特殊处理-不能翻页
					}
					// 发送通知广播
					if (notice != null) {
						UIHelper.sendBroadCast(lv.getContext(), notice);
					}

				} else if (msg.what == -1) {
					// 有异常--显示加载出错 & 弹出错误消息
					lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
					more.setText(R.string.load_error);
					((AppException) msg.obj).makeToast(GoodsActivity.this);
				}
				if (adapter.getCount() == 0) {
					lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
					more.setText(R.string.load_empty);
				}
				progress.setVisibility(ProgressBar.GONE);
				if (msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH) {
					lv.onRefreshComplete(getString(R.string.pull_to_refresh_update)
							+ new Date().toLocaleString());
					lv.setSelection(0);
				} else if (msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_TYPE) {
					lv.onRefreshComplete();
					lv.setSelection(0);
				}
			}
		};
	}

	private Notice handleLvData(int what, Object obj, int objtype,
			int actiontype) {
		Notice notice = null;
		System.out.println("数量" + what + "action:" + actiontype + "objtype"
				+ objtype);
		switch (actiontype) {
		case UIHelper.LISTVIEW_ACTION_INIT:
		case UIHelper.LISTVIEW_ACTION_REFRESH:
			GoodsList cllist = (GoodsList) obj;
			// goodsMinID = what;
			if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
				if (listDatas.size() > 0) {
					for (Goods goods1 : cllist.getGoodslist()) {
						boolean b = false;
						for (Goods goods2 : listDatas) {
							if (goods1.getGoodsid() == goods2.getGoodsid()) {
								b = true;
								break;
							}
						}
						if (!b) {
							listDatas.add(goods1);
						}
					}
				} else {
					listDatas.addAll(cllist.getGoodslist());
				}
			} else {
				listDatas.clear();// 先清除原有数据
				listDatas.addAll(cllist.getGoodslist());
			}
		case UIHelper.LISTVIEW_ACTION_SCROLL:
			GoodsList glist = (GoodsList) obj;
			lvGoodsSumData += what;
			if (listDatas.size() > 0) {
				for (Goods goods1 : glist.getGoodslist()) {
					boolean b = false;
					for (Goods goods2 : listDatas) {
						if (goods1.getGoodsid() == goods2.getGoodsid()) {
							b = true;
							break;
						}
					}
					if (!b)
						listDatas.add(goods1);
				}
			} else {
				listDatas.addAll(glist.getGoodslist());
			}
			break;

		}
		return notice;
	}

	@Override
	public String getTitleStr() {
		return "本店商品";
	}

}
