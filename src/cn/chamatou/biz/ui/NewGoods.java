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
import cn.chamatou.biz.bean.CityZone;
import cn.chamatou.biz.bean.Goods;
import cn.chamatou.biz.bean.Option;
import cn.chamatou.biz.bean.URLs;
import cn.chamatou.biz.common.JSONHelper;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIHelper;

/**
 * 新建商品
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class NewGoods extends Activity{

	private ImageView mBack;
	private EditText mGname,mUnit,mPrice,mDescr;
	private Button mConfirm;//,mCancel;
    private ProgressDialog mProgress;
	private InputMethodManager imm;
	private Goods goods;
	private String gname,unit,price,descr,gtype,gimg;
	private ImageView goods_image;
	private ImageButton getPic,getPhoto;
	private Button bt_camera;
	GetPhotosUtils getPhotosUtils;
	private String saveDir = Environment.getExternalStorageDirectory()
			.getPath() + "/temp_image";
	CheckBox ckDs,ckSt;
	private Bitmap photo;
	private AppContext appContext;//全局Context
	protected HttpUtils httpUtil = new HttpUtils();
	protected List<CityZone> zoneList;
	protected ArrayList<String> cityList;
	protected ArrayAdapter<CityZone> provAdapter,cityAdapter,zoneAdapter;
	protected List<Option> selectList;
	protected ArrayAdapter<Option> sortAdapter,busiAdapter,sourceAdapter,degreeAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_goods);
		appContext = (AppContext)getApplication();
		goods = new Goods();
		goods.setState(1);
		this.initView();
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
    	//mCancel = (Button)findViewById(R.id.goods_returnBtn);
    	mGname = (EditText)findViewById(R.id.new_goods_name);
    	mUnit = (EditText)findViewById(R.id.new_goods_unit);
    	mPrice = (EditText)findViewById(R.id.new_goods_price);
    	mDescr = (EditText)findViewById(R.id.new_goods_descr);
    	getPic=(ImageButton)findViewById(R.id.image_tupian);
    	getPhoto=(ImageButton)findViewById(R.id.image_paizhao);
    	
    	goods_image = (ImageView) findViewById(R.id.goods_image);
		bt_camera = (Button) findViewById(R.id.bt_camera);

		bt_camera.setOnClickListener(takePicListener);
		ckSt = (CheckBox)this.findViewById(R.id.checkBox2);
    	//绑定监听器
    	ckSt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	goods.setState(0);
                }else{ 
                	goods.setState(1); 
                } 
            } 
        }); 
    	ckDs = (CheckBox)this.findViewById(R.id.checkBox1);
    	//绑定监听器
    	ckDs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	goods.setDiscount(1); 
                }else{ 
                	goods.setDiscount(0); 
                } 
            } 
        }); 
    	mBack.setOnClickListener(UIHelper.finish(this));
    	//mCancel.setOnClickListener(UIHelper.finish(this));
    	mConfirm.setOnClickListener(createClickListener);
		
    	
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
			else saveGoods();
		}
	};
	public void saveGoods(){
		//imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
		
		gname = mGname.getText().toString();
		if(StringUtils.isEmpty(gname)){
			UIHelper.ToastMessage(this, "请输入商品名称");
			return;
		}
		unit = mUnit.getText().toString();
		if(StringUtils.isEmpty(unit)){
			UIHelper.ToastMessage(this, "请输入商品单位");
			return;
		}
		price = mPrice.getText().toString();
		if(StringUtils.isEmpty(price)){
			UIHelper.ToastMessage(this, "请输入商品价格");
			return;
		}
		
		mProgress = ProgressDialog.show(this, null, "处理中···",true,true); 
		descr = mDescr.getText().toString();
		
		goods.setGoodsid(0);
		goods.setGoods_name(gname);
		goods.setUnit(unit);
		goods.setPrice(StringUtils.toDouble(price));
		goods.setDescription(descr);
		
		if(gimg!=null)
			goods.setDefault_image(gimg);
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				if(mProgress!=null)mProgress.dismiss();
				if(msg.what == 1){
					String res = (String)msg.obj;
					try {
						JSONObject ojson= new JSONObject(res);
						new AlertDialog.Builder(NewGoods.this)
						 .setTitle("提示") 
						 .setMessage("商品信息"+ojson.getString("errormsg"))
						  .setPositiveButton("确定", null) 
						  .show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else {
					((AppException)msg.obj).makeToast(NewGoods.this);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();					
				String res = null;
				try {
					res = appContext.saveGoods(goods);
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
					UIHelper.sendBroadCastGoods(NewGoods.this);
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
			goods_image.setImageDrawable(null);
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
							goods_image.setTag(res);
							gimg=res;
							saveGoods();
					}
					else {
						((AppException)msg.obj).makeToast(NewGoods.this);
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
			goods_image.setImageDrawable(new BitmapDrawable(photo));
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
