package cn.chamatou.biz.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.R;
import cn.chamatou.biz.adapter.GoodsListAdapter;
import cn.chamatou.biz.async.AsyncUtil;
import cn.chamatou.biz.async.Callback;
import cn.chamatou.biz.async.Result;
import cn.chamatou.biz.bean.Goods;
import cn.chamatou.biz.data.StoreData;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class GoodsActivity extends BaseWithTitleActivity  implements
OnClickListener, OnItemClickListener{
	private Button btn_new;
	private PullToRefreshListView listView;
	private View emptyView;
	private View progressView;
	private View reloadBtn;	
	private ArrayList<Goods> listDatas=new ArrayList<Goods>();
	private GoodsListAdapter listAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.goods);
		super.onCreate(savedInstanceState);
		getListdata();
	}
	private boolean isEnd = false;
	private boolean loading = false;

	public void initView() {
		btn_new = (Button)findViewById(R.id.finish_btn);
		btn_new.setText("添加新活动");
		btn_new.setVisibility(View.VISIBLE);
		btn_new.setOnClickListener(newGoodsClickListener);
		

		listView = (PullToRefreshListView) findViewById(R.id.listView);
		progressView = LayoutInflater.from(context).inflate(
				R.layout.list_footer_progress, null);
		listView.getRefreshableView().addFooterView(progressView, null, false);
		listView.setMode(Mode.DISABLED);
		listDatas = new ArrayList<Goods>();
		listAdapter = new GoodsListAdapter(context, listDatas);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);

		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			public void onLastItemVisible() {
				if (loading || isEnd) {
					return;
				}
				getListdata();
			}
		});

		emptyView = findViewById(R.id.list_empty);

		reloadBtn = findViewById(R.id.list_reload);
		reloadBtn.setOnClickListener(this);
		dismissProgress(false, 0);

	}

	public void initListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			 @Override
	     	   public void onItemClick(AdapterView<?> parent, View v, int position,
	     	     long id) {
	     		  Goods eevent = listDatas.get(position-1);
	     		 //System.out.println("ddL"+eventid);
	    		 //UIHelper.EditGoods(v.getContext(), eevent);
	     	   }
	     	
		});

	}

	private View.OnClickListener newGoodsClickListener = new View.OnClickListener(){
		public void onClick(View v) {
			//UIHelper.showNewGoods(v.getContext());
		}		
	};
	private void getListdata() {
		showProgress();
		AsyncUtil.goAsync(new Callable<Result<List<Goods>>>() {

			@Override
			public Result<List<Goods>> call() throws Exception {
				return StoreData.getGoods(""+AppContext.self.getLoginUid(),getMinId(listDatas)+"");
			}
		}, new Callback<Result<List<Goods>>>() {

			@Override
			public void onHandle(Result<List<Goods>> result) {
				if (result.ok()) {
					if (result.getData().isEmpty()
							&& listAdapter.getCount() != 0) {
						//UIUtils.toast(context, "已经是最后一页了", Toast.LENGTH_SHORT);
						isEnd = true;
					} else {
						listDatas.addAll(result.getData());
						listAdapter.notifyDataSetChanged();
					}
					dismissProgress(false, listAdapter.getCount());
				} else {
					//UIUtils.toastE(context, result.getErrorMsg(),
					//		Toast.LENGTH_SHORT);
					dismissProgress(true, listAdapter.getCount());
				}
			}
			

		});
	}

	private void showProgress() {
		loading = true;
		progressView.setVisibility(View.VISIBLE);
		reloadBtn.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
	}

	protected void dismissProgress(boolean hasError, int count) {
		loading = false;
		progressView.setVisibility(View.GONE);
		if (hasError) {
			reloadBtn.setVisibility(View.VISIBLE);
		} else {
			reloadBtn.setVisibility(View.GONE);
			emptyView.setVisibility(count > 0 ? View.GONE : View.VISIBLE);
		}
	}


	private Long getMinId(List<Goods> datas) {
		if (datas.size() > 0) {
			return (long) datas.get(datas.size() - 1).getGoodsid();
		} else {
			return Long.MAX_VALUE;
		}
	}

	@Override
	public String getTitleStr() {
		return "本店商品";
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		
	}

}
