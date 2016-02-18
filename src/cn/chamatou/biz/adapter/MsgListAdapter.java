package cn.chamatou.biz.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.chamatou.biz.R;
import cn.chamatou.biz.bean.MsgBean;
import cn.chamatou.biz.bean.MsgListItemBean;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.ui.NewGoods;

public class MsgListAdapter extends BaseAdapter {

	private List<MsgListItemBean> rowList;
	private LayoutInflater inflater;
	private Context context;

	public MsgListAdapter(final Context context,
			List<MsgListItemBean> list) {
		inflater = LayoutInflater.from(context);
		rowList = list;
		this.context = context;
	}

	public void clear() {
		rowList.clear();
	}

	@Override
	public int getCount() {
		return rowList.size();
	}

	@Override
	public Object getItem(int position) {
		return rowList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.msg_list_item, parent,
					false);

			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.unReadCount = (TextView) convertView
					.findViewById(R.id.unReadCount);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MsgBean msg = rowList.get(position).getLastestMsg();
		holder.title.setText(msg.getTitle());
		holder.content.setText(msg.getContent());
		holder.time.setText(StringUtils.friendly_time(msg.getTime()));
		holder.image.setImageResource(rowList.get(position).getIcon());

		int count = rowList.get(position).getUnReadCount();
		if (count > 0) {
			holder.unReadCount.setVisibility(View.VISIBLE);
			holder.unReadCount.setText("" + count);
		} else {
			holder.unReadCount.setVisibility(View.GONE);
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (position == 0) {
					Intent intent = new Intent(context,
							NewGoods.class);
					intent.putExtra("type", position + "");
					context.startActivity(intent);
				} else {
//					Intent intent = new Intent(context,
//							MsgInfoActivity1.class);
//					intent.putExtra("type", position + "");
//					context.startActivity(intent);
				}
			}
		});

		return convertView;
	}

	class ViewHolder {
		public ImageView image;
		public TextView title;
		public TextView content;
		public TextView time;
		public TextView unReadCount;
	}
}
