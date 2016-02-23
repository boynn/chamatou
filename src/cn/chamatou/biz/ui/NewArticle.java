package cn.chamatou.biz.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.AppException;
import cn.chamatou.biz.R;
import cn.chamatou.biz.api.ApiClient;
import cn.chamatou.biz.bean.Article;
import cn.chamatou.biz.bean.CityZone;
import cn.chamatou.biz.bean.Goods;
import cn.chamatou.biz.bean.Option;
import cn.chamatou.biz.bean.URLs;
import cn.chamatou.biz.common.JSONHelper;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIHelper;

/**
 * 新建茶文
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class NewArticle extends Activity{

	private ImageView mBack;
	private EditText mTitle,mCont;//,mSort;
	TextView mType;
	private Button mConfirm;//,mCancel;
    private ProgressDialog mProgress;
	private InputMethodManager imm;
	private Article article;
	private String title,content,atype,gimg;
	private ImageView default_image;
	private ImageButton getPic,getPhoto;
	private Button bt_camera;
	GetPhotosUtils getPhotosUtils;
	private String saveDir = Environment.getExternalStorageDirectory()
			.getPath() + "/temp_image";
	private Bitmap photo;
	private AppContext appContext;//全局Context
	protected HttpUtils httpUtil = new HttpUtils();
	private Handler mSortHandler;
	int type;
	protected ArrayAdapter<Option> sortAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_article);
		appContext = (AppContext)getApplication();
		Bundle bundle = getIntent().getExtras();
		type = bundle.getInt("type");
		article = new Article();
		this.initView();
		System.out.println("ddg3");
		//initAType();
		initGetPhotosUtils();
		
	}
	
    //初始化视图控件
    private void initView()
    {    	
    	//sort=1;busi=1;source=1;
    	imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);    	
    	mBack = (ImageView)findViewById(R.id.btn_back);
    	mConfirm = (Button)findViewById(R.id.finish_btn);
    	mConfirm.setVisibility(View.VISIBLE);
    	((TextView) findViewById(R.id.title)).setText("编辑文章");
    	mTitle = (EditText)findViewById(R.id.article_title);
    	mCont = (EditText)findViewById(R.id.article_cont);
    	getPic=(ImageButton)findViewById(R.id.image_tupian);
    	getPhoto=(ImageButton)findViewById(R.id.image_paizhao);
    	System.out.println("ddg");
		
    	default_image = (ImageView) findViewById(R.id.default_image);
		bt_camera = (Button) findViewById(R.id.bt_camera);
		
		bt_camera.setOnClickListener(takePicListener);
		mBack.setOnClickListener(UIHelper.finish(this));
    	//mCancel.setOnClickListener(UIHelper.finish(this));
    	mConfirm.setOnClickListener(createClickListener);
    	mType=(TextView)findViewById(R.id.article_type);  
    	switch (type) {
		case 1:
			mType.setText("茶典故");
			break;
		case 2:
			mType.setText("茶艺");
			break;
		case 3:
			mType.setText("茶养生");
			break;
		case 4:
			mType.setText("茶段子");
			break;
		default:
			mType.setText("茶典故");
			type=1;
			break;
    	}
//    	mType.setOnItemSelectedListener(new OnItemSelectedListener(){
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				  String ntype = ((Option) mType.getSelectedItem()).GetId();  
//				  if(!ntype.equals(atype)){
//					  atype=ntype;					  
//				  }
//				   
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO 自动生成的方法存根
//				
//			}
//    	});
    	
    	File savePath = new File(saveDir);
		if (!savePath.exists()) {
			savePath.mkdirs();
		}
    }
    	
 	private View.OnClickListener createClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			//隐藏软键盘
			if(photo!=null)
				uploadPhoto();
			else saveArticle();
		}
	};
	
//	private void initAType() {
//		// TODO 自动生成的方法存根
//				mSortHandler = new Handler() {
//					public void handleMessage(Message msg) {
//						if (msg.what == 1) {
//							@SuppressWarnings("unchecked")
//							List<Option> tList = (List<Option>)msg.obj;
//							if (tList != null && tList.size() != 0) {
//								typeList = tList;
//							}
//							sortAdapter = new ArrayAdapter<Option>(NewArticle.this,
//									android.R.layout. simple_spinner_item, typeList);
//							sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item );
//							mType.setAdapter(sortAdapter);
//							mType.setPrompt("茶文分类");
//							for(int i=0;i<mType.getCount();i++)
//					    	{
//					    		if(((Option) mType.getItemAtPosition(i)).GetId().equals(atype))  
//								{
//					    			mType.setSelection(i);
//					    			break;
//								}
//					    	} 
//											
//						} else if (msg.what == 0) {
//							UIHelper.ToastMessage(NewArticle.this,
//									R.string.msg_load_is_null);
//						} else if (msg.what == -1 && msg.obj != null) {
//							((AppException) msg.obj).makeToast(NewArticle.this);
//						}
//					}
//				};
//				new Thread(){
//					public void run() {
//						Message msg = new Message();
//						try {
//							List<Option> minfo= appContext.getAtype();				
//							msg.what = 1;
//							msg.obj = minfo;
//			            } catch (AppException e) {
//			            	e.printStackTrace();
//			            	msg.what = -1;
//			            	msg.obj = e;
//			            }
//						mSortHandler.sendMessage(msg);
//					}
//				}.start();				
//	}

	public void saveArticle(){
		//imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
		
		title = mTitle.getText().toString();
		if(StringUtils.isEmpty(title)){
			UIHelper.ToastMessage(this, "请输入文章标题");
			return;
		}
		
		content = mCont.getText().toString();
		if(StringUtils.isEmpty(title)){
			UIHelper.ToastMessage(this, "请输入文章内容");
			return;
		}
		mProgress = ProgressDialog.show(this, null, "处理中···",true,true); 
		article.setId(0);
		article.setTitle(title);
		article.setContent(content);
		article.setAtype(type);
		if(gimg!=null)
			article.setDefault_image(gimg);
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				if(mProgress!=null)mProgress.dismiss();
				if(msg.what == 1){
					String res = (String)msg.obj;
					try {
						JSONObject ojson= new JSONObject(res);
						new AlertDialog.Builder(NewArticle.this)
						 .setTitle("提示") 
						 .setMessage("文章"+ojson.getString("errormsg"))
						  .setPositiveButton("确定", null) 
						  .show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else {
					((AppException)msg.obj).makeToast(NewArticle.this);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();					
				String res = null;
				try {
					res = appContext.saveArticle(article);
					msg.what = 1;
					msg.obj = res;
				} catch (AppException e) {
	            	e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
	            }
				handler.sendMessage(msg);
				try {
					JSONObject ojson= new JSONObject(res);
					UIHelper.sendBroadCastGoods(NewArticle.this);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		//UIHelper.showGoodsDetail(appContext, goods);			
	}
	private View.OnClickListener takePicListener = new View.OnClickListener() {
		public void onClick(View v) {
			getPic.setVisibility(View.VISIBLE);
			getPhoto.setVisibility(View.VISIBLE);
			default_image.setImageDrawable(null);
		}
	};
	/**
	 * 初始化图片选择模块
	 */
	private void initGetPhotosUtils() {
		getPhotosUtils = new GetPhotosUtils() {
			@Override
			public void uploadImage(Drawable drawable) {
			}

			@Override
			public int[] getImageBtnId() {
				return new int[]{R.id.image_tupian,R.id.image_paizhao};
			}

			@Override
			public int[] getImageBtnType() {
				return new int[]{1,2};
			}
		};
		getPhotosUtils.onCreate(this);
	}
	public void uploadPhoto() {
			//imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
			mProgress = ProgressDialog.show(this, null, "处理中···",true,true); 
			
			final Handler handler = new Handler(){
				public void handleMessage(Message msg) {
					if(mProgress!=null)mProgress.dismiss();
					if(msg.what == 1){
						String res = (String)msg.obj;
							//JSONObject ojson= new JSONObject(res);
						default_image.setTag(res);
							gimg=res;
							saveArticle();
					}
					else {
						((AppException)msg.obj).makeToast(NewArticle.this);
					}
				}
			};
			new Thread(){
				public void run() {
					Message msg = new Message();					
					String res = null;
					try {
						Bitmap bmpCompressed = Bitmap.createScaledBitmap(photo, 640, 480, true);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						bmpCompressed.compress(CompressFormat.JPEG, 100, bos);
						byte[] data = bos.toByteArray();
						res = appContext.uploadData(data);
						msg.what = 1;
						msg.obj = res;
					} catch (AppException e) {
		            	e.printStackTrace();
						msg.what = -1;
						msg.obj = e;
		            }
					handler.sendMessage(msg);					
				}
			}.start();
			//UIHelper.showGoodsDetail(appContext, goods);			
		}
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{   
		getPhotosUtils.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 3) {
			if (data != null) {
				setPicToView(data);
			}
		} 
		super.onActivityResult(requestCode, resultCode, data);			 
	}
	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			default_image.setImageDrawable(new BitmapDrawable(photo));
		}
	}
	@Override
	protected void onDestroy() {
		destoryImage();
		super.onDestroy();
	}

	private void destoryImage() {
		if (photo != null) {
			photo.recycle();
			photo = null;
		}
	}
	
	

}
