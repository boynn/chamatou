package cn.chamatou.biz.ui;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.AppException;
import cn.chamatou.biz.R;
import cn.chamatou.biz.adapter.OrderDetailGoodsAdapter;
import cn.chamatou.biz.bean.Order;
import cn.chamatou.biz.bean.OrderGoods;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIHelper;
import cn.chamatou.biz.widget.ScrollViewWithListView;

public class OrderDetail extends Activity implements OnTouchListener {
	private Context context;
	private TextView tv_order_no,tv_order_resident,tv_addr,tv_order_time,tv_order_sum,tv_order_state,ship_state,tv_title;
	private Button confirm_order;	
	private Handler mHandler;
    private ScrollViewWithListView goods_listView;
    private OrderDetailGoodsAdapter commAdapter;
	private int oid;// 活动id，

	private Order order;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = OrderDetail.this;
		// 初始化数据
		Bundle bundle = getIntent().getExtras();
		oid = bundle.getInt("oid", 0);
		setContentView(R.layout.order_detail);
		if (oid == 0) {
			UIHelper.ToastMessage(context, "订单有误", Toast.LENGTH_SHORT);
			finish();
			return;
		}
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();
				
	}

	// 初始化控件
	public void initView() {
		tv_order_no = (TextView) findViewById(R.id.tv_orderno);
		tv_order_time = (TextView) findViewById(R.id.tv_ordertime);
		tv_order_resident = (TextView) findViewById(R.id.tv_resident);
		tv_addr = (TextView) findViewById(R.id.tv_addr);
		tv_order_sum = (TextView) findViewById(R.id.order_sum);
		tv_order_state = (TextView) findViewById(R.id.order_state);
		ship_state= (TextView) findViewById(R.id.ship_state);
		confirm_order=(Button) findViewById(R.id.confirm_order);
		tv_title = (TextView) findViewById(R.id.title);
		tv_title.setText("订单详情");
		goods_listView = (ScrollViewWithListView) findViewById(R.id.goods_listView);
		commAdapter = new OrderDetailGoodsAdapter(context, new ArrayList<OrderGoods>());
		goods_listView.setAdapter(commAdapter);
		
	}
	//初始化控件数据
		private void initData()
		{		
			mHandler = new Handler()
			{
				public void handleMessage(Message msg) 
				{				
					if(msg.what == 1)
					{							
						setView();
					}
					else if(msg.what == 0)
					{
						UIHelper.ToastMessage(context, R.string.msg_load_is_null);
					}
					else if(msg.what == -1 && msg.obj != null)
					{
						((AppException)msg.obj).makeToast(context);
					}				
				}
			};
			
			new Thread(){
				public void run() {
	                Message msg = new Message();
					try {
						order = ((AppContext)getApplication()).getOrder(oid, false);
					} catch (AppException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					msg.what = (order!=null && order.getId()>0) ? 1 : 0;
					msg.obj = (order!=null) ? order: null;			
	                mHandler.sendMessage(msg);
				}
			}.start();
	    }
		

	private void setView() {
		tv_order_no.setText("订单号："+order.getOrderno());
		tv_order_resident.setText(order.getResident_name()+" "+order.getMobile());
		tv_order_time.setText(StringUtils.friendly_time(order.getCreate_time()));
		tv_addr.setText(order.getAddr());
		String price = new DecimalFormat("0.0").format(order.getTotal());
		if (price.endsWith(".0")) {
			price = price.substring(0, price.length() - 2);
		}
		tv_order_sum.setText("￥" + price);
		tv_order_state.setText(order.getPayment()==1?"已付款":(order.getState()!=4?"到付":"已取消"));
		ship_state.setText(order.getState()==0?"待配送":(order.getState()==1?"待收货":(order.getState()==2?"待评价":"已完成")));
		if(order.getState()==0){
			confirm_order.setVisibility(View.VISIBLE);
		}
		//if(order.getState()==2)confirm_order.setVisibility(View.GONE);
		getOrderGoods(order.getOrderno());
	}


	// 获取订单商品
	private void getOrderGoods(final String orderno) {
		mHandler = new Handler()
		{
			public void handleMessage(Message msg) 
			{				
				if(msg.what == 1)
				{
					Type type = new TypeToken<List<OrderGoods>>() {
					}.getType();
					Gson gson = new Gson();
					List<OrderGoods> data = gson.fromJson((String)msg.obj, type);
					commAdapter.setListdata(data);
					commAdapter.notifyDataSetChanged();	
				}
				else if(msg.what == 0)
				{
					((AppException)msg.obj).makeToast(context);
				}		
			}
		};
		new Thread(){
			public void run() {
                Message msg = new Message();
				try {
					msg.what=1;
					msg.obj = ((AppContext)getApplication()).getOrderGoods(orderno, false);
				} catch (AppException e) {
					// TODO 自动生成的 catch 块
					msg.what=0;
					msg.obj=e;
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					msg.what=0;
					msg.obj=e;
				}
				mHandler.sendMessage(msg);
			}
		}.start();
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void initListener() {
		confirm_order.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(v.getContext()) 
				.setTitle("订单配送")
				.setMessage("确定吗？")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {				 
		            @Override
		            public void onClick(DialogInterface dialog, int i) {
		            	mHandler = new Handler()
		        		{
		        			public void handleMessage(Message msg) 
		        			{				
		        				if(msg.what == 0)
		        				{
		        					UIHelper.ToastMessage(context, (String)msg.obj);
		        					confirm_order.setVisibility(View.GONE);	
		        					ship_state.setVisibility(View.VISIBLE);
		        				}
		        				else if(msg.what == 1)
		        				{
		        					((AppException)msg.obj).makeToast(context);
		        				}		
		        			}
		        		};
		        		new Thread(){
		        			public void run() {
		                        Message msg = new Message();
		        				try {
		        					JSONObject ojson = new JSONObject(((AppContext)getApplication()).shipOrder(oid));
		        					msg.what=ojson.getInt("error");
		        					msg.obj = ojson.getString("errormsg");
		        				} catch (AppException e) {
		        					// TODO 自动生成的 catch 块
		        					msg.what=1;
		        					msg.obj=e;
		        				} catch (Exception e) {
		        					// TODO 自动生成的 catch 块
		        					msg.what=1;
		        					msg.obj=e;
		        				}
		        				mHandler.sendMessage(msg);
		        			}
		        		}.start();
		            }
		        })
				.setNegativeButton("否", null)
				.show();
			}
		});
		tv_title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 自动生成的方法存根
		return false;
	}
	
}
