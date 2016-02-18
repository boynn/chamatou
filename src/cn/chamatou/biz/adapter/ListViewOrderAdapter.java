package cn.chamatou.biz.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

//import com.boynn.community.common.ImageLoaderUtil;
import cn.chamatou.biz.R;
import cn.chamatou.biz.bean.Order;
import cn.chamatou.biz.common.BitmapManager;
import cn.chamatou.biz.common.DensityUtils;
import cn.chamatou.biz.common.ImageLoaderUtil;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.ui.Main;
import cn.chamatou.biz.ui.OrderDetail;

/**
 * 商品Adapter类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class ListViewOrderAdapter extends BaseAdapter {
	private Context 					context;//运行上下文
	private List<Order> 				listItems;//数据集合
	private LayoutInflater 				listContainer;//视图容器
	//private int 						itemViewResource;//自定义项视图源
	private BitmapManager 				bmpManager;
	class ViewHolder {
		private TextView tv_resident;
		private TextView tv_ordertime;
		private TextView tv_total;
		private TextView tv_addr;
		private TextView tv_pay;
		private TextView tv_state;
		private LinearLayout goodsscrollview;		

	}
	 

	/**
	 * 实例化Adapter
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewOrderAdapter(Context context, List<Order> data) {
		this.context = context;			
		this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		//this.itemViewResource = resource;
		this.listItems = data;		
	}
	
	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}
	   
	/**
	 * ListView Item设置
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			//获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.order_listitem,null);
			holder = new ViewHolder();
			holder.tv_resident = (TextView) convertView.findViewById(R.id.tv_resident);
			holder.tv_addr = (TextView) convertView.findViewById(R.id.tv_addr);
			holder.tv_ordertime = (TextView) convertView
					.findViewById(R.id.tv_ordertime);
			holder.tv_total = (TextView) convertView
					.findViewById(R.id.order_sum);
			holder.tv_pay = (TextView) convertView
					.findViewById(R.id.order_paid);
			holder.tv_state = (TextView) convertView
					.findViewById(R.id.order_state);
			holder.goodsscrollview=(LinearLayout)convertView.findViewById(R.id.goodspicview);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Order order = listItems.get(position);
		holder.tv_resident.setText(order.getResident_name()+" "+order.getMobile());
		holder.tv_ordertime.setText(StringUtils.friendly_time(order.getCreate_time()));
		holder.tv_addr.setText(order.getAddr());
		holder.tv_pay.setText(order.getPayment()==1?"已付款":(order.getState()!=4?"到付":"已取消"));	
		holder.tv_state.setText(order.getState()==0?"待配送":(order.getState()==1?"待收货":(order.getState()==2?"待评价":"已完成")));		
		List<String> picList = order.getPic();
		if (picList.size()>0) {
			holder.goodsscrollview.removeAllViews();
			Drawable dr = context.getResources().getDrawable(R.drawable.pic_border); 
			for (String url : picList) {
					if (!StringUtils.isEmpty(url)) {
					ImageView imageView = new ImageView(context);
					imageView.setLayoutParams(new LayoutParams(DensityUtils.dipTopx(context, 66), DensityUtils.dipTopx(context, 66)));
					imageView.setPadding(10, 10, 10, 10);
					imageView.setScaleType(ScaleType.FIT_CENTER);
					imageView.setScaleType(ScaleType.FIT_XY);
					imageView.setBackground(dr);
					ImageLoader.getInstance().displayImage(url, imageView,ImageLoaderUtil.createNormalOption());
					holder.goodsscrollview.addView(imageView);
					
				}
			}
		}
		String price = new DecimalFormat("0.0").format(order.getTotal());
		if (price.endsWith(".0")) {
			price = price.substring(0, price.length() - 2);
		}
		holder.tv_total.setText("￥" + price);
		notifyDataSetChanged();
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						OrderDetail.class);
				intent.putExtra("oid", listItems.get(position).getId());
				
				//intent.putExtra("state", state);//为1的时候才可以
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}

}