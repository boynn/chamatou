package cn.chamatou.biz.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.chamatou.biz.R;
import cn.chamatou.biz.bean.GridFuncBean;

public class FuncGridViewAdapter extends BaseAdapter {
	private Context context;
	private List<GridFuncBean> list;
	private LayoutInflater inflater;
	public FuncGridViewAdapter(final Context context,List<GridFuncBean> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		if(list == null){
			this.list = new ArrayList<GridFuncBean>();
		}else{
			this.list = list;
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}
	
	public void setList(List<GridFuncBean> list) {
		this.list = list;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.func_gridview_item, parent,
					false);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.text = (TextView) convertView.findViewById(R.id.textView);
			holder.itemLayout = convertView.findViewById(R.id.bg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		GridFuncBean item = list.get(position);
		
		holder.isOpenedToUser =true;
		holder.index = item.getIndex();
		holder.position = position;
		holder.itemLayout.setBackgroundResource(item.getBackground());
		holder.img.setImageResource(item.getImg());
		holder.text.setText(item.getTitle());
		
		
		return convertView;
	}

	public class ViewHolder {
		public int index;
		public int position;
		public View itemLayout;
		public ImageView img;
		public TextView text;
		public boolean isOpenedToUser;//true表示该模块对用户开放
	}

}
