package cn.chamatou.biz.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.chamatou.biz.R;
import cn.chamatou.biz.bean.Transfer;
import cn.chamatou.biz.ui.Main;

/**
 * 转账Adapter类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class ListViewTransferAdapter extends BaseAdapter {
	//private Main 					context;//运行上下文
	private List<Transfer> 				listItems;//数据集合
	private LayoutInflater 				listContainer;//视图容器
	private int 						itemViewResource;//自定义项视图源
	static class ListItemView{				//自定义控件集合  
			//public CheckBox ckbx;  
        	//public ImageView gimg;  
	        public TextView eaccount;  
		    public TextView inaccount;
		    public TextView acname;
		    public TextView amount;
		    public TextView transtime;  
		    public TextView descr;
		    
	 }  

	/**
	 * 实例化Adapter
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewTransferAdapter(Main context, List<Transfer> data,int resource) {
		//this.context = context;			
		this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		this.itemViewResource = resource;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//自定义视图
		ListItemView  listItemView = null;
		if (convertView == null) {
			//获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);
			
			listItemView = new ListItemView();
			//获取控件对象
			//listItemView.gimg = (ImageView)convertView.findViewById(R.id.transfer_list_gimg);
			//listItemView.eaccount = (TextView)convertView.findViewById(R.id.transfer_list_eaccount);
			listItemView.inaccount = (TextView)convertView.findViewById(R.id.transfer_list_inaccount);
			listItemView.acname= (TextView)convertView.findViewById(R.id.transfer_list_acname);
			listItemView.amount= (TextView)convertView.findViewById(R.id.transfer_list_amount);
			listItemView.transtime= (TextView)convertView.findViewById(R.id.transfer_list_date);
			listItemView.descr= (TextView)convertView.findViewById(R.id.transfer_list_descr);
			
			//设置控件集到convertView
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}
				
		//设置文字和图片
		Transfer transfer = listItems.get(position);
		//listItemView.eaccount.setText(transfer.getEaccount());
		listItemView.inaccount.setText(transfer.getInaccount());
		listItemView.acname.setTag(transfer);//设置隐藏参数(实体类)
		listItemView.acname.setText(transfer.getAcname());
		listItemView.amount.setText(transfer.getAmount());
		listItemView.descr.setText(transfer.getDescr());
		listItemView.transtime.setText(transfer.getTranstime());
		
		
		return convertView;
	}

}