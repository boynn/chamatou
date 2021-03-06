package cn.chamatou.biz.adapter;

import java.util.List;

import org.apache.commons.httpclient.util.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.chamatou.biz.R;
import cn.chamatou.biz.bean.Article;
import cn.chamatou.biz.common.StringUtils;

public class ArticleAdapter extends BaseAdapter {
	private static ArticleAdapter self;
	private static int choosedPostition = 0;
	private Context context;
	private LayoutInflater inflater;
	private List<Article> listdata;
	public ArticleAdapter(Context context, List<Article> listdata) {
		self = this;
		this.context = context;
		this.listdata = listdata;
	}

	@Override
	public int getCount() {
		return listdata.size();
	}

	public void setListdata(List<Article> listdata) {
		this.listdata = listdata;
	}

	public List<Article> getListdata() {
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.article_item, null);
			holder = new ViewHolder();
			holder.tv_comm = (TextView) convertView.findViewById(R.id.tv_comm);
			holder.tv_comm_time = (TextView) convertView
					.findViewById(R.id.tv_comm_time);
			holder.tv_textview1 = (TextView) convertView
					.findViewById(R.id.tv_textview1);
			holder.tv_textview2 = (TextView) convertView
					.findViewById(R.id.tv_textview2);
			holder.textview_zan_num = (TextView) convertView
					.findViewById(R.id.textview_zan_num);
			holder.comm_num = (TextView) convertView
					.findViewById(R.id.comm_num);
						convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final Article bean = listdata.get(position);
		//holder.tv_comm.setText(bean.getTitle());
		holder.tv_textview1.setText(bean.getTitle());
		holder.tv_comm_time.setText(StringUtils.friendly_time(bean.getCreateTime()));
		holder.tv_textview2.setText(bean.getContent());
		holder.textview_zan_num.setText(bean.getPraise() + "人喜欢");
		
		notifyDataSetChanged();
		holder.comm_num.setText(bean.getFollow() + "跟帖");

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(context, ArticleDetailActivity.class);
//				intent.putExtra("id", listdata.get(position).getId());
//				intent.putExtra("bean", listdata.get(position));
//				choosedPostition = position;
				// intent.putExtra("state", state);//为1的时候才可以点赞
//				context.startActivity(intent);
			}
		});
//		holder.imagebtn_zan.setClickable(true);
//		holder.imagebutton_comm.setClickable(true);
//		holder.imagebutton_comm.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				((ArticleDetailActivity) context).findViewById(R.id.bottomLayout)
//						.setVisibility(View.VISIBLE);
//				ArticleDetailActivity.id = listdata.get(position).getId();
//			}
//		});
//		holder.imagebtn_zan.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				try {
//					BBSDataUtil.getZanInfo(new Callback<String>() {
//						@Override
//						public void onHandle(String param) {
//							// TODO Auto-generated method stub
//							UIUtils.doS(context, param, new Callback<String>() {
//								@Override
//								public void onHandle(String param) {
//									// TODO Auto-generated method stub
//									try {
//										JSONArray object = new JSONArray(param);
//										// int error = object.getInt("error");
//										// int praise = object.getInt("praise");
//										if (object.length() > 0) {
//											JSONObject obj = object
//													.getJSONObject(0);
//											int praise = obj.getInt("praise");
//											holder.textview_zan_num
//													.setText(praise + "");
//											bean.setPraise(praise);
//											notifyDataSetChanged();
//										}
//									} catch (Exception e) {
//										// TODO: handle exception
//										e.printStackTrace();
//									}
//								}
//							});
//							try {
//								JSONObject jsonObject = new JSONObject(param);
//								int error = jsonObject.getInt("error");
//								if (error == 1) {
//									Toast.makeText(context, "您已经点过赞了",
//											Toast.LENGTH_SHORT).show();
//								}
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//
//						}
//					}, bean.getId());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}

	//		}
	//	});
		return convertView;
	}

	class ViewHolder {
		private TextView tv_comm;
		private TextView tv_comm_time;
		private TextView tv_textview1;
		private TextView tv_textview2;

		private TextView textview_zan_num;
		private TextView comm_num;
		
	}

	public static ArticleAdapter getSelf() {
		return self;
	}

	public static int getChoosedPostition() {
		return choosedPostition;
	}
}
