package cn.chamatou.biz.adapter;

import java.util.List;

import org.apache.commons.httpclient.util.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.chamatou.biz.R;
import cn.chamatou.biz.bean.MsgBean;
import cn.chamatou.biz.common.StringUtils;

import com.nostra13.universalimageloader.core.ImageLoader;

public class MsgInfoListAdapter extends BaseAdapter {

	private List<MsgBean> rowList;
	private LayoutInflater inflater;
	private Context context;

	public MsgInfoListAdapter(final Context context, List<MsgBean> list) {
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

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MsgBean msg = rowList.get(position);
		holder.title.setText(msg.getSender());
		holder.content.setText(msg.getContent());
		holder.time.setText(StringUtils.friendly_time(msg.getTime()));
		if ("".equals(msg.getImgUrl()) || null == msg.getImgUrl()) {
			holder.image.setVisibility(View.GONE);
		} else {
			holder.image.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(msg.getImgUrl(),
					holder.image);
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(context,
				// NewsListClassActivity1.class);
				// intent.putExtra("type", position+"");
				// context.startActivity(intent);
			}
		});

		return convertView;
	}

	class ViewHolder {
		public ImageView image;
		public TextView title;
		public TextView content;
		public TextView time;
	}
}
