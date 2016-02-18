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

public class ArticleAdapter extends BaseAdapter {
	private static ArticleAdapter self;
	private static int choosedPostition = 0;
	private Context context;
	private LayoutInflater inflater;
	private List<Article> listdata;
	private PopupWindow popupWindow;

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
			holder.horizontalScrollView = (LinearLayout) convertView
					.findViewById(R.id.horizontalScrollView);
			holder.imagebtn_zan = (ImageButton) convertView
					.findViewById(R.id.imagebtn_zan);
			holder.textview_zan_num = (TextView) convertView
					.findViewById(R.id.textview_zan_num);
			holder.comm_num = (TextView) convertView
					.findViewById(R.id.comm_num);
			holder.frameayout_num = convertView
					.findViewById(R.id.frameayout_num);
			holder.tupian_num = (TextView) convertView
					.findViewById(R.id.tupian_num);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Article bean = listdata.get(position);
		//holder.tv_comm.setText(bean.getTitle());
		holder.tv_comm_time.setText(DateUtil.formatDate(bean.getCreateTime()));
		holder.tv_textview1.setText(bean.getTitle());
		holder.tv_textview2.setText(bean.getContent());
		holder.horizontalScrollView.removeAllViews();
		holder.textview_zan_num.setText(bean.getPraise() + "");
//		List<String> picList = bean.getPic1();
//		if (picList.size() > 0) {
//			holder.horizontalScrollView.setVisibility(View.VISIBLE);
//			for (String url : picList) {
//				if (!StringUtils.isBlank(url)) {
//					ImageView imageView = new ImageView(context);
//					imageView.setLayoutParams(new LayoutParams(DensityUtils
//							.dipTopx(context, 106), DensityUtils.dipTopx(
//							context, 106)));
//					imageView.setPadding(10, 10, 10, 10);
//					imageView.setScaleType(ScaleType.FIT_CENTER);
//					imageView.setScaleType(ScaleType.FIT_XY);
//					ImageLoader.getInstance().displayImage(url, imageView,
//							ImageLoaderUtil.createNormalOption());
//					holder.horizontalScrollView.addView(imageView);
//				}
//			}
//		} else {
//			holder.horizontalScrollView.setVisibility(View.GONE);
//		}

//		int Tupain_number = bean.getPic_count();
//		if (Tupain_number > 3) {
//			holder.frameayout_num.setVisibility(View.VISIBLE);
//			holder.tupian_num.setText("共" + Tupain_number + "张");
//		} else {
//			holder.frameayout_num.setVisibility(View.GONE);
//		}

//		holder.horizontalScrollView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// 显示图片列表
//				Intent intent = new Intent(context, ChaKanDaTuActivity.class);
//				intent.putExtra("bean", bean);
//				context.startActivity(intent);
//			}
//		});
		notifyDataSetChanged();
		holder.comm_num.setText(bean.getFollow() + "");

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
		private ImageView image_comm;
		private TextView tv_comm;
		private TextView tv_comm_time;
		private TextView tv_textview1;
		private TextView tv_textview2;

		private LinearLayout horizontalScrollView;
		private ImageView imageview_comm;
		private ImageButton imagebtn_zan;
		private TextView textview_zan_num;
		private ImageButton imagebutton_comm;
		private TextView comm_num;
		private View frameayout_num;
		private TextView tupian_num;

	}

	public static ArticleAdapter getSelf() {
		return self;
	}

	public static int getChoosedPostition() {
		return choosedPostition;
	}
}
