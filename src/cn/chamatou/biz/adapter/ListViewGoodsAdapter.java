package cn.chamatou.biz.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
//import com.boynn.community.common.ImageLoaderUtil;
import cn.chamatou.biz.R;
import cn.chamatou.biz.bean.Goods;
import cn.chamatou.biz.common.BitmapManager;
import cn.chamatou.biz.common.ImageLoaderUtil;
import cn.chamatou.biz.ui.Main;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 商品Adapter类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class ListViewGoodsAdapter extends BaseAdapter {
	//private Main 					context;//运行上下文
	private Context ctx;
	private List<Goods> 				listItems;//数据集合
	private LayoutInflater 				listContainer;//视图容器
	private int 						itemViewResource;//自定义项视图源
	private BitmapManager 				bmpManager;
	static class ListItemView{				//自定义控件集合  
			//public CheckBox ckbx;  
			public ImageView gimg;  
			public ImageView stimg;  
        	public TextView gname;  
        	public TextView price;  
		    public TextView gunit;
		    public TextView gneeds;
		    public TextView gtotal;
		    public TextView gtype;
	 }  

	/**
	 * 实例化Adapter
	 * @param main2
	 * @param data
	 * @param resource
	 */
	public ListViewGoodsAdapter(final Context context, List<Goods> data,int resource) {
		//this.context = context;			
		this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_dface_loading));
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
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//自定义视图
		ListItemView  listItemView = null;
		if (convertView == null) {
			//获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);
			
			listItemView = new ListItemView();
			//获取控件对象
			listItemView.gimg = (ImageView)convertView.findViewById(R.id.goods_list_thumbnail);
			listItemView.stimg = (ImageView)convertView.findViewById(R.id.goods_list_state);
			listItemView.gname = (TextView)convertView.findViewById(R.id.goods_list_gname);
			listItemView.gunit= (TextView)convertView.findViewById(R.id.goods_list_unit);
			listItemView.gtotal= (TextView)convertView.findViewById(R.id.goods_list_needs);
			listItemView.gneeds= (TextView)convertView.findViewById(R.id.goods_need);
			listItemView.price= (TextView)convertView.findViewById(R.id.goods_list_price);
			
			//设置控件集到convertView
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}
				
		//设置文字和图片
		Goods goods = listItems.get(position);
		listItemView.gname.setText(goods.getGoods_name());
		listItemView.gname.setTag(goods);//设置隐藏参数(实体类)
		if(goods.getTotal()>0){
			listItemView.gtotal.setText(""+goods.getTotal());
			listItemView.gneeds.setVisibility(View.VISIBLE);
		}
		listItemView.gunit.setText("/"+goods.getUnit());
		listItemView.price.setText("￥"+goods.getPrice());
		if(goods.getState()==0)
			listItemView.stimg.setVisibility(View.VISIBLE);
		else listItemView.stimg.setVisibility(View.GONE);
		ImageLoader.getInstance().displayImage(goods.getDefault_image(),
				listItemView.gimg, ImageLoaderUtil.createNormalOption());
		
		return convertView;
	}

}