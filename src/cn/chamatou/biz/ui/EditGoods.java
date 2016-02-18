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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.AppException;
import cn.chamatou.biz.R;
import cn.chamatou.biz.api.ApiClient;
import cn.chamatou.biz.bean.CityZone;
import cn.chamatou.biz.bean.Goods;
import cn.chamatou.biz.bean.Option;
import cn.chamatou.biz.bean.URLs;
import cn.chamatou.biz.common.ImageLoaderUtil;
import cn.chamatou.biz.common.JSONHelper;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIHelper;

/**
 * 新建商品
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class EditGoods extends Activity{

	private ImageView mBack;
	private EditText mGname,mUnit,mPrice,mDescr;
	private Button mConfirm;
    private ProgressDialog mProgress;
	private InputMethodManager imm;
	private Goods goods;
	private String gname,unit,price,descr,gtype,gimg;
	private ImageButton getPic,getPhoto;
	private ImageView goods_image;
	private Button bt_camera;
	CheckBox ckDs,ckSt;
	GetPhotosUtils getPhotosUtils;
	private String saveDir = Environment.getExternalStorageDirectory()
			.getPath() + "/temp_image";
	//private Button bt_upload;
	private Bitmap photo;
	private Context context;
	private AppContext appContext;//全局Context
	protected HttpUtils httpUtil = new HttpUtils();
	protected List<CityZone> zoneList;
	protected ArrayList<String> cityList;
	protected ArrayAdapter<CityZone> provAdapter,cityAdapter,zoneAdapter;
	protected List<Option> selectList;
	protected ArrayAdapter<Option> sortAdapter,busiAdapter,sourceAdapter,degreeAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = EditGoods.this;
		appContext = (AppContext)getApplication();
		Bundle bundle = getIntent().getExtras();
		goods = (Goods) bundle.getSerializable("EDIT_GOODS");
		setContentView(R.layout.new_goods);
		if (goods == null) {
			UIHelper.ToastMessage(context, "商品有误", Toast.LENGTH_SHORT);
			finish();
			return;
		}
		super.onCreate(savedInstanceState);
		setView();
		initGetPhotosUtils();		
		
	}
	
	
	//初始化视图控件
    private void setView()
    {    	
    	//sort=1;busi=1;source=1;
    	imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);    	
    	mBack = (ImageView)findViewById(R.id.btn_back);
    	mConfirm = (Button)findViewById(R.id.finish_btn);
    	mConfirm.setVisibility(View.VISIBLE);
    	//mDel = (Button)findViewById(R.id.goods_del);
    	//mCancel = (Button)findViewById(R.id.goods_returnBtn);
    	mGname = (EditText)findViewById(R.id.new_goods_name);
    	mUnit = (EditText)findViewById(R.id.new_goods_unit);
    	mPrice = (EditText)findViewById(R.id.new_goods_price);
    	mDescr = (EditText)findViewById(R.id.new_goods_descr);
    	getPic=(ImageButton)findViewById(R.id.image_tupian);
    	getPhoto=(ImageButton)findViewById(R.id.image_paizhao);
    	((TextView)findViewById(R.id.title)).setText("编辑商品");
    	//mDel.setVisibility(View.VISIBLE);
    	ckSt = (CheckBox)this.findViewById(R.id.checkBox2);
    	//绑定监听器
    	ckSt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	goods.setState(0);; 
                }else{ 
                	goods.setState(1); 
                } 
            } 
        }); 
    	ckSt.setChecked((goods.getState()==0));
    	ckDs = (CheckBox)this.findViewById(R.id.checkBox1);
    	//绑定监听器
    	ckDs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	goods.setDiscount(1);; 
                }else{ 
                	goods.setDiscount(0); 
                } 
            } 
        }); 
    	ckDs.setChecked((goods.getDiscount()==1));
        mGname.setText(goods.getGoods_name());
    	mUnit.setText(goods.getUnit());
    	mPrice.setText(""+goods.getPrice());
    	mDescr.setText(goods.getDescription());
    	goods_image = (ImageView) findViewById(R.id.goods_image);
		ImageLoader.getInstance().displayImage(goods.getDefault_image(),
    			goods_image, ImageLoaderUtil.createNormalOption());
		//.setText(goods);
    	bt_camera = (Button) findViewById(R.id.bt_camera);

		bt_camera.setOnClickListener(takePicListener);
		//bt_upload = (Button) findViewById(R.id.bt_upload);

		//bt_upload.setOnClickListener(uploadListener);
		
    	mBack.setOnClickListener(UIHelper.finish(this));
    	//mCancel.setOnClickListener(UIHelper.finish(this));
    	mConfirm.setOnClickListener(createClickListener);
    	//mDel.setOnClickListener(delClickListener);
		
    	
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
		
		//goods = new Goods();
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
						new AlertDialog.Builder(EditGoods.this)
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
					((AppException)msg.obj).makeToast(EditGoods.this);
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
					UIHelper.sendBroadCastGoods(EditGoods.this);
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
						((AppException)msg.obj).makeToast(EditGoods.this);
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
	private View.OnClickListener delClickListener = new View.OnClickListener(){
		public void onClick(View v) {
			AppContext appContent=(AppContext) v.getContext().getApplicationContext();
			try {
				String res = appContent.delGoods(goods.getGoodsid());
				JSONObject ojson = new JSONObject(res);
				UIHelper.ToastMessage(v.getContext(), ojson.getString("Msg"));	
				v.setVisibility(View.GONE);
			} catch (AppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	};
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
