package cn.chamatou.biz.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import cn.chamatou.biz.R;
import cn.chamatou.biz.bean.OrderGoods;
import cn.chamatou.biz.common.ImageLoaderUtil;

public class OrderDetailGoodsAdapter extends BaseAdapter {
	private static OrderDetailGoodsAdapter self;
	private static int choosedPostition = 0;
	private Context context;
	private LayoutInflater inflater;
	private List<OrderGoods> listdata;
	
	public OrderDetailGoodsAdapter(Context context,
			ArrayList<OrderGoods> arrayList) {
		self = this;
		this.context = context;
		this.listdata = arrayList;
	}

	@Override
	public int getCount() {
		return listdata.size();
	}

	public void setListdata(List<OrderGoods> listdata) {
		this.listdata = listdata;
	}

	public List<OrderGoods> getListdata() {
		return listdata;
	}

	@Override
	public Object getItem(int position) {
		return listdata.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public int getQtyCount() {
		int qty=0;
		for(int i=0;i<listdata.size();i++){
			qty+=listdata.get(i).getQty();
		}
		return qty;
	}
	public double getSum() {
		double sum=0;
		for(int i=0;i<listdata.size();i++){
			sum+=listdata.get(i).getQty()*listdata.get(i).getPrice();
		}
		return sum;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.ordergoods_item,
					null);
			holder = new ViewHolder();
			holder.tv_goodsname = (TextView) convertView.findViewById(R.id.tv_goodsname);
			holder.tv_price = (TextView) convertView.findViewById(R.id.goods_price);
			holder.tv_qty = (TextView) convertView.findViewById(R.id.goods_qty);
			holder.gimg = (ImageView) convertView.findViewById(R.id.imageView1);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final OrderGoods goods = listdata.get(position);
		ImageLoader.getInstance().displayImage(goods.getThumbnail(),
				holder.gimg, ImageLoaderUtil.createNormalOption());
		holder.tv_goodsname.setText(goods.getGoodsname());
		String price = new DecimalFormat("0.0").format(goods.getPrice());
		if (price.endsWith(".0")) {
			price = price.substring(0, price.length() - 2);
		}
		holder.tv_price.setText("￥" + price);
		holder.tv_qty.setText("x" + goods.getQty());
		notifyDataSetChanged();
		
		
		return convertView;
	}

	class ViewHolder {
		private TextView tv_goodsname;
		private TextView tv_price;
		private TextView tv_qty;
		
		private ImageView gimg;
		

	}

	public static OrderDetailGoodsAdapter getSelf() {
		return self;
	}

	public static int getChoosedPostition() {
		return choosedPostition;
	}
}
