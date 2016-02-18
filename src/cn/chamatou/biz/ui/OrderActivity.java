package cn.chamatou.biz.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.AppException;
import cn.chamatou.biz.R;
import cn.chamatou.biz.adapter.ListViewOrderAdapter;
import cn.chamatou.biz.bean.Notice;
import cn.chamatou.biz.bean.Order;
import cn.chamatou.biz.bean.OrderList;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIHelper;
import cn.chamatou.biz.widget.PullToRefreshListView;

public class OrderActivity extends BaseWithTitleActivity {
	private PullToRefreshListView listView;
	private ArrayList<Order> listDatas=new ArrayList<Order>();;
	private ListViewOrderAdapter listAdapter;
	private Handler lvOrderHandler;
	private int lvOrderSumData;
	private View lvOrder_footer;
	private TextView lvOrder_foot_more;
	private ProgressBar lvOrder_foot_progress;
	private AppContext appContext;// 全局Context
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.frame_order);
		super.onCreate(savedInstanceState);
		appContext = (AppContext) getApplication();
		lvOrderHandler = this.getLvHandler(listView, listAdapter, lvOrder_foot_more, lvOrder_foot_progress, AppContext.PAGE_SIZE);  
        loadLvOrderData(lvOrderHandler, 0, UIHelper.LISTVIEW_ACTION_INIT);
		

	}

	public void initView() {
		// order_new = (Button)findViewById(R.id.frame_order_head_new);
		// order_new.setOnClickListener(newOrderClickListener);
		listAdapter = new ListViewOrderAdapter(context, listDatas);
		lvOrder_footer = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		lvOrder_foot_more = (TextView) lvOrder_footer
				.findViewById(R.id.listview_foot_more);
		lvOrder_foot_progress = (ProgressBar) lvOrder_footer
				.findViewById(R.id.listview_foot_progress);
		listView = (PullToRefreshListView) findViewById(R.id.frame_order_list);
		listView.addFooterView(lvOrder_footer);// 添加底部视图 必须在setAdapter前
		listView.setAdapter(listAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

	     	@Override
  	   public void onItemClick(AdapterView<?> parent, View v, int position,
  	     long id) {
  		  Integer orderid = listDatas.get(position-1).getId();
  		  UIHelper.showOrderDetail(v.getContext(), orderid);
  	   }

  	  });
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				listView.onScrollStateChanged(view, scrollState);
				
				//数据为空--不用继续下面代码了
				if(listDatas.isEmpty()) return;
				
				//判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if(view.getPositionForView(lvOrder_footer) == view.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}
				
				int lvDataState = StringUtils.toInt(listView.getTag());
				if(scrollEnd && lvDataState==UIHelper.LISTVIEW_DATA_MORE)
				{
					lvOrder_foot_more.setText(R.string.load_ing);
					lvOrder_foot_progress.setVisibility(View.VISIBLE);
					loadLvOrderData(lvOrderHandler,getOrderMinId(listDatas), UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				listView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		});
		listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
         public void onRefresh() {
         	loadLvOrderData(lvOrderHandler, 0, UIHelper.LISTVIEW_ACTION_REFRESH);
         }
     });		
	}

	private void loadLvOrderData(final Handler handler,final int minID,final int action) {
		// mHeadProgress.setVisibility(ProgressBar.VISIBLE);
		if(minID==0)listDatas.clear();
		new Thread(){
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if(action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				try {
					OrderList list = appContext.getOrderList(minID, 1);				
					msg.what = list.getPageSize();
					msg.obj = list;
	            } catch (AppException e) {
	            	e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_ORDER;
				handler.sendMessage(msg);
			}
		}.start();
	}

	public void initListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, OrderDetail.class);
				Order st = (Order) parent.getItemAtPosition(position);
				intent.putExtra("orderId", st.getId());
				startActivity(intent);
			}
		});

	}


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
					((AppException) msg.obj).makeToast(OrderActivity.this);
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
			OrderList cllist = (OrderList) obj;
			// orderMinID = what;
			if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
				if (listDatas.size() > 0) {
					for (Order order1 : cllist.getOrderlist()) {
						boolean b = false;
						for (Order order2 : listDatas) {
							if (order1.getId() == order2.getId()) {
								b = true;
								break;
							}
						}
						if (!b) {
							listDatas.add(order1);
						}
					}
				} else {
					listDatas.addAll(cllist.getOrderlist());
				}
			} else {
				listDatas.clear();// 先清除原有数据
				listDatas.addAll(cllist.getOrderlist());
			}
		case UIHelper.LISTVIEW_ACTION_SCROLL:
			OrderList glist = (OrderList) obj;
			lvOrderSumData += what;
			if (listDatas.size() > 0) {
				for (Order order1 : glist.getOrderlist()) {
					boolean b = false;
					for (Order order2 : listDatas) {
						if (order1.getId() == order2.getId()) {
							b = true;
							break;
						}
					}
					if (!b)
						listDatas.add(order1);
				}
			} else {
				listDatas.addAll(glist.getOrderlist());
			}
			break;

		}
		return notice;
	}
	private int getOrderMinId(List<Order> datas) {
		if (datas.size() > 0) {
			return (int) datas.get(datas.size() - 1).getId();
		} else {
			return (int) Long.MAX_VALUE;
		}
	}
	@Override
	public String getTitleStr() {
		return "订单";
	}

}
