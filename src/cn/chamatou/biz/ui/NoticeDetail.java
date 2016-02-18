package cn.chamatou.biz.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.AppException;
import cn.chamatou.biz.R;
import cn.chamatou.biz.bean.Notice;
import cn.chamatou.biz.common.UIHelper;

/**
 *公告详情
 * 
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class NoticeDetail extends Activity {
    
	private ImageView mBack;
	private TextView mSubject;
    private TextView mDetail;
    private TextView mEname;
    private TextView mCdate;
    private Handler mHandler;
    private Notice noticeDetail;
    private int Id;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detail);
        Id = getIntent().getIntExtra("Id",0);  
        //初始化视图控件
        this.initView();
        //初始化控件数据
        this.initData();
     }    
     
    //初始化视图控件
    private void initView()
    {    	
		mBack = (ImageView)findViewById(R.id.notice_detail_back);
		//adapter = new GImageAdapter(this);  
		
    	mBack.setOnClickListener(UIHelper.finish(this));
    	mSubject = (TextView)findViewById(R.id.notice_detail_subject);
    	mDetail = (TextView)findViewById(R.id.notice_detail);
    	mEname = (TextView)findViewById(R.id.notice_detail_ename);
    	mCdate = (TextView)findViewById(R.id.notice_detail_cdate);
    	//AppContext ac = (AppContext)getApplication();
    	//if(!ac.isHttpsLogin()){mRprice.setVisibility(ImageView.GONE);mSprice.setVisibility(ImageView.GONE);}
		
    }    
 	
	
    //初始化控件数据
	private void initData()
	{		
		mHandler = new Handler()
		{
			public void handleMessage(Message msg) 
			{				
				if(msg.what == 1)
				{	
					
					mSubject.setText(noticeDetail.getSubject());
					mDetail.setText(noticeDetail.getDetail());
					mEname.setText(noticeDetail.getEname());
					mCdate.setText(noticeDetail.getCdate());
			    	

				}
				else if(msg.what == 0)
				{
					UIHelper.ToastMessage(NoticeDetail.this, R.string.msg_load_is_null);
				}
				else if(msg.what == -1 && msg.obj != null)
				{
					((AppException)msg.obj).makeToast(NoticeDetail.this);
				}				
			}
		};
		
		initData(Id, false);
	}
	
	private void initData(final int notice_id, final boolean isRefresh)
    {		
    	
		new Thread(){
			public void run() {
                Message msg = new Message();
				noticeDetail = ((AppContext)getApplication()).getNotice(notice_id, isRefresh);
				msg.what = (noticeDetail!=null && noticeDetail.getId()>0) ? 1 : 0;
				msg.obj = (noticeDetail!=null) ? noticeDetail: null;			
                mHandler.sendMessage(msg);
			}
		}.start();
    }
	
}
