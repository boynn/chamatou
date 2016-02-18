package cn.chamatou.biz.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.AppException;
import cn.chamatou.biz.R;
import cn.chamatou.biz.bean.Option;
import cn.chamatou.biz.bean.Store;
import cn.chamatou.biz.common.ImageLoaderUtil;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 新建商品
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class EditStore extends BaseWithTitleActivity{

	//private ImageView mBack;
	private EditText mSname,mAddr,mTitle,mDescr;
	//private TextView mComm;
	private Spinner mType;//,mSort;
	private View mConfirm;
    private ProgressDialog mProgress;
	private InputMethodManager imm;
	private Store mystore;
	private String name,addr,mobile,title,descr,gimg;
	private ImageButton getPic,getPhoto;
	private ImageView store_image;
	private Button bt_camera;
	GetPhotosUtils getPhotosUtils;
	private String saveDir = Environment.getExternalStorageDirectory()
			.getPath() + "/temp_image";
	private Handler myStoreHandler,mOptionHandler,mSortHandler;
	private Bitmap photo;
	private Context context;
	private AppContext appContext;//全局Context
	protected HttpUtils httpUtil = new HttpUtils();
	protected List<Option> typeList;
	protected ArrayAdapter<Option> typeAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = EditStore.this;
		appContext = (AppContext)getApplication();
		setContentView(R.layout.edit_store);
		super.onCreate(savedInstanceState);
		initView();
		InitMyStore();
		initGetPhotosUtils();		
		
	}
	
	private void InitMyStore() {
		// TODO 自动生成的方法存根
		myStoreHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					mystore = (Store)msg.obj;
					setView();					
				} else if (msg.what == 0) {
					UIHelper.ToastMessage(EditStore.this,
							R.string.msg_load_is_null);
				} else if (msg.what == -1 && msg.obj != null) {
					((AppException) msg.obj).makeToast(EditStore.this);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();
				try {
					Store minfo= appContext.getMyStore();				
					msg.what = 1;
					msg.obj = minfo;
	            } catch (AppException e) {
	            	e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }
				myStoreHandler.sendMessage(msg);
			}
		}.start();
		
	}
	
	
	@Override
	public void initView()
    {    	
    	imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);    	
    	//mBack = (ImageView)findViewById(R.id.edit_store_back);
    	mConfirm = findViewById(R.id.finish_btn);
    	mConfirm.setVisibility(View.VISIBLE);
		((TextView) mConfirm).setText("保存");
    	mSname = (EditText)findViewById(R.id.store_name);
    	mTitle = (EditText)findViewById(R.id.store_title);
    	mAddr = (EditText)findViewById(R.id.store_addr);
    	mDescr = (EditText)findViewById(R.id.store_descr);
    	getPic=(ImageButton)findViewById(R.id.image_tupian);
    	getPhoto=(ImageButton)findViewById(R.id.image_paizhao);
    	//((TextView)findViewById(R.id.title)).setText("店铺管理");
    	store_image = (ImageView) findViewById(R.id.store_image);
    	bt_camera = (Button) findViewById(R.id.bt_camera);

		bt_camera.setOnClickListener(takePicListener);
		
    	//mBack.setOnClickListener(UIHelper.finish(this));
    	//mCancel.setOnClickListener(UIHelper.finish(this));
    	mConfirm.setOnClickListener(createClickListener);
    	
    	
    	File savePath = new File(saveDir);
		if (!savePath.exists()) {
			savePath.mkdirs();
		}
		
    }
	//初始化视图控件
    private void setView()
    {    	
    	
    	mSname.setText(mystore.getStore_name());
    	mAddr.setText(mystore.getStore_addr());
    	mTitle.setText(mystore.getTitle());
    	mDescr.setText(mystore.getDescription());
    	ImageLoader.getInstance().displayImage(mystore.getHead(),
				store_image, ImageLoaderUtil.createNormalOption());
    	//loadStoreType(type);
    }
	
	private View.OnClickListener createClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			//隐藏软键盘
			if(photo!=null)
				uploadPhoto();
			else updateStore();
		}
	};
	public void updateStore(){
		//imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
		
		name = mSname.getText().toString();
		if(StringUtils.isEmpty(name)){
			UIHelper.ToastMessage(this, "请输入门店名称");
			return;
		}
		mystore.setStore_name(name);
		title = mTitle.getText().toString();
		if(StringUtils.isEmpty(title)){
			UIHelper.ToastMessage(this, "请输入门店签名");
			return;
		}
		mystore.setTitle(title);
		addr = mAddr.getText().toString();
		if(StringUtils.isEmpty(addr)){
			UIHelper.ToastMessage(this, "请输入门店地址");
			return;
		}
		mystore.setStore_addr(addr);
		mProgress = ProgressDialog.show(this, null, "处理中···",true,true); 
		descr = mDescr.getText().toString();
		mystore.setDescription(descr);
		if(gimg!=null)
			mystore.setHead(gimg);
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				if(mProgress!=null)mProgress.dismiss();
				if(msg.what == 1){
					String res = (String)msg.obj;
					try {
						JSONObject ojson= new JSONObject(res);
						new AlertDialog.Builder(EditStore.this).setTitle(ojson.getString("errormsg")).show();
						finish();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else {
					((AppException)msg.obj).makeToast(EditStore.this);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();					
				String res = null;
				try {
					res = appContext.saveStore(mystore);
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
					UIHelper.sendBroadCastGoods(EditStore.this);
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
			store_image.setImageDrawable(null);
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
							store_image.setTag(res);
							gimg=res;
							updateStore();
					}
					else {
						((AppException)msg.obj).makeToast(EditStore.this);
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
			store_image.setImageDrawable(new BitmapDrawable(photo));
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
	private static Gson createGson() {
		// return new GsonBuilder().registerTypeHierarchyAdapter(String.class,
		// new DateAdapter()).create();
		GsonBuilder b = new GsonBuilder();
		b.setDateFormat("yyyy-MM-dd HH:mm:ss");
		return b.create();
	}

	@Override
	public void initListener() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public String getTitleStr() {
		// TODO 自动生成的方法存根
		return "门店管理";
	}

}
