package cn.chamatou.biz.adapter;

import java.util.List;

import org.json.JSONObject;

import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.R;
import cn.chamatou.biz.bean.Notice;
import cn.chamatou.biz.bean.Notice;
import cn.chamatou.biz.common.BitmapManager;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIHelper;
import cn.chamatou.biz.ui.Main;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 动弹Adapter类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class ListViewNoticeAdapter extends BaseAdapter {
	private Main 					context;//运行上下文
	private List<Notice> 				listItems;//数据集合
	private LayoutInflater 				listContainer;//视图容器
	private int 						itemViewResource;//自定义项视图源
	static class ListItemView{				//自定义控件集合  
			public TextView subject;			
	 }  

	/**
	 * 实例化Adapter
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewNoticeAdapter(Main context, List<Notice> data,int resource) {
		this.context = context;			
		this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
	}
	
	public int getCount() {
		return listItems.size();
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
			listItemView.subject = (TextView)convertView.findViewById(R.id.notice_listitem_subject);
			//设置控件集到convertView
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}
				
		//设置文字和图片
		Notice notice = listItems.get(position);
		listItemView.subject.setText(notice.getSubject());
		listItemView.subject.setOnClickListener(noticeClickListener);
		listItemView.subject.setTag(notice.getId());
		return convertView;
	}
	
	private View.OnClickListener noticeClickListener = new View.OnClickListener(){
		public void onClick(View v) {
			Integer noticeid = (Integer)v.getTag();
			UIHelper.showNoticeDetail(v.getContext(), noticeid);
		}
	};
	

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return 0;
	}	

}