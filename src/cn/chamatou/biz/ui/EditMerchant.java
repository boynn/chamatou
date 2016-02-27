package cn.chamatou.biz.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.AppException;
import cn.chamatou.biz.R;
import cn.chamatou.biz.adapter.GridViewAdapter;
import cn.chamatou.biz.async.AsyncUtil;
import cn.chamatou.biz.async.Callback;
import cn.chamatou.biz.async.Result;
import cn.chamatou.biz.bean.Merchant;
import cn.chamatou.biz.common.DensityUtils;
import cn.chamatou.biz.common.ImageLoaderUtil;
import cn.chamatou.biz.common.ImageUtils;
import cn.chamatou.biz.common.UIHelper;
import cn.chamatou.biz.data.StoreData;
import cn.chamatou.biz.widget.CommonDialog;
import cn.chamatou.biz.widget.DialogUtil;

import com.lidroid.xutils.HttpUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 新建茶文
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2016-2-21
 */
public class EditMerchant extends BasePicCutActivity implements
OnClickListener{
	// 公共intent对象
    protected Intent intent;
    // 公共budle对象
    protected Bundle bundle;

    private Merchant memerchant;
	
    private GridViewAdapter adapter;
    private ProgressDialog mProgress;
    private EditText name,owner,mgr,intro,srv,ttype;
	//private String gender;
	private ImageView back_btn,head_image;
	private ImageButton getPic,getPhoto;
	private Button ret,confirm;
	private String headPath;
	private View ivPics,photolayout,picslayout;
	GetPhotosUtils getPhotosUtils;
	private List<Bitmap> listBitmaps = new ArrayList<Bitmap>();
	private Bitmap photo;
	private AppContext appContext;//全局Context
	protected HttpUtils httpUtil = new HttpUtils();
	private LinearLayout photo_linearlayout;
	private int finishUploadPicNum=0;
	private InputMethodManager imm;
	private List<String> pics = new ArrayList<String>();
	private CommonDialog waitDialog;
	//private Handler mymerchantHandler;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = EditMerchant.this;
		appContext = (AppContext)getApplication();
		setContentView(R.layout.edit_role3);
		super.onCreate(savedInstanceState);
		InitMyMerchant();
		//initAType();
		initGetPhotosUtils();
		System.out.println("rfsss");
    	
	}
	
	//初始化视图控件
	@Override
	public	void initView()
    {    	
    	imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);    	
    	photo_linearlayout=(LinearLayout)findViewById(R.id.photo_linearlayout); 
    	name = (EditText)findViewById(R.id.role_name);
    	owner = (EditText)findViewById(R.id.role_owner);
    	mgr = (EditText)findViewById(R.id.role_mgr);
    	//addr = (EditText)findViewById(R.id.role_addr);
    	srv = (EditText)findViewById(R.id.role_srv);
    	intro = (EditText)findViewById(R.id.role_intro);
    	ttype=(EditText)findViewById(R.id.role_type);
    	ivPics=findViewById(R.id.rl_edit_pic);
    	head_image = (ImageView) findViewById(R.id.head);
    	back_btn=(ImageView)findViewById(R.id.btn_back);// 返回
    	ret=(Button)findViewById(R.id.returnBtn);
    	confirm=(Button)findViewById(R.id.confirmBtn);
    	photolayout=findViewById(R.id.photolayout);
    	picslayout=findViewById(R.id.picsLayout);
    	ret.setOnClickListener(this);
    	confirm.setOnClickListener(this);
    	head_image.setOnClickListener(this);
    	ivPics.setOnClickListener(this);
    	
    }
	private void InitMyMerchant() {
		final Handler mymerchantHandler = new Handler(){
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					memerchant = (Merchant)msg.obj;
					setView();					
				} else if (msg.what == 0) {
					UIHelper.ToastMessage(EditMerchant.this,
							R.string.msg_load_is_null);
				} else if (msg.what == -1 && msg.obj != null) {
					((AppException) msg.obj).makeToast(EditMerchant.this);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();
				try {
					Merchant minfo= appContext.getMyMerchant();				
					msg.what = 1;
					msg.obj = minfo;
	            } catch (AppException e) {
	            	e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }
				mymerchantHandler.sendMessage(msg);
			}
		}.start();
		
	}
	//初始化视图控件
    private void setView()
    {    	
    	name.setText(memerchant.getName());
    	owner.setText(memerchant.getOwner());
    	mgr.setText(memerchant.getMgr());
    	//addr.setText(memerchant.getAddress());
    	srv.setText(memerchant.getSrv());
    	intro.setText(memerchant.getDescription());
    	ttype.setText(memerchant.getTtype());
    	ImageLoader.getInstance().displayImage(memerchant.getHead(),
				head_image, ImageLoaderUtil.createNormalOption());
    }
	
	private View.OnClickListener takePicListener = new View.OnClickListener() {
		public void onClick(View v) {
			getPic.setVisibility(View.VISIBLE);
			getPhoto.setVisibility(View.VISIBLE);
			head_image.setImageDrawable(null);
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
						destoryImage();
						headPath=res;
						head_image.setTag(res);
						updatemerchant();						
					}
					else {
						((AppException)msg.obj).makeToast(EditMerchant.this);
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
		}
	 
	private void uploadAllPics() {
		int size = listBitmaps.size();
		//循环上传
		for(int i=0;i<size;i++){
			final int indext = i;
			AsyncUtil.goAsync(new Callable<String>() {

				@Override
				public String call() throws Exception {
					//获取图片
					Bitmap bmpCompressed = Bitmap.createScaledBitmap(listBitmaps.get(indext), 640, 480, true);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bmpCompressed.compress(CompressFormat.JPEG, 100, bos);
					byte[] data = bos.toByteArray();

					//上传图片
					return appContext.uploadData(data);
				}
			}, new Callback<String>() {
				@Override
				public void onHandle(String result) {
						String url = result;
						pics.add(url);
					finishUploadPicNum++;					
				}
			});
		}
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
			head_image.setImageDrawable(new BitmapDrawable(photo));
			//headlayout.setVisibility(View.GONE);
			//piclayout.setVisibility(View.VISIBLE);
		}
	}
	
	private void addPicToView(Intent picdata) {
		System.out.println("rfs3ss");
    	Bundle extras = picdata.getExtras();
		if (extras != null) {
			System.out.println("rfs3dss");
	    	Bitmap pic = extras.getParcelable("data");
			listBitmaps.add(pic);
		//	listBitmaps.add(photo);
			//隐藏
			if (picslayout.getVisibility() == View.GONE) {
				picslayout.setVisibility(View.VISIBLE);
				photolayout.setVisibility(View.GONE);
			}else {
				photolayout.setVisibility(View.GONE);
			}
			//动态添加ImageView

			ImageView imageView = new ImageView(EditMerchant.this);
			imageView.setLayoutParams(new LayoutParams(DensityUtils.dipTopx(EditMerchant.this, 60), DensityUtils.dipTopx(EditMerchant.this, 60)));
			imageView.setPadding(5, 5, 5, 5);
			imageView.setScaleType(ScaleType.FIT_CENTER);
			photo_linearlayout.addView(imageView);
			//把photo設置給imageview
			imageView.setImageDrawable(new BitmapDrawable(pic));
			
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
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(requestCode<4)
    	{
    		getPhotosUtils.onActivityResult(requestCode, resultCode, data);
    		System.out.println("rfsss");
	    	if (data != null) 
	        	addPicToView(data);
    	}
    	else
    		super.onActivityResult(requestCode, resultCode, data);    	
        
    }

    public void onClick(View v)
    {
    	switch (v.getId())
        {
            case R.id.head:// 头像
            	ShowPickDialog();
				break;
            case R.id.rl_edit_pic:// 选择多张图片
            	if (photolayout.getVisibility() == View.GONE) {
            		photolayout.setVisibility(View.VISIBLE);
 					picslayout.setVisibility(View.GONE);
 					imm.hideSoftInputFromWindow(name.getWindowToken(),0);
 				} else {
 					photolayout.setVisibility(View.GONE);
 					picslayout.setVisibility(View.GONE);
 				}
                break;
            case R.id.btn_back:// 返回
    		case R.id.returnBtn:// 返回
    			EditMerchant.this.finish();
                break;
            case R.id.confirmBtn:
            	updatemerchant();
                break;
        }
    }
    public void updatemerchant(){
    	if(photo!=null)
    	{
    		uploadPhoto();
    		return;
    	}
    	memerchant.setName(name.getText().toString());
    	memerchant.setOwner(owner.getText().toString());
    	memerchant.setMgr(mgr.getText().toString());
    	memerchant.setTtype(ttype.getText().toString());
    	memerchant.setSrv(srv.getText().toString());
    	//memerchant.setAddress(addr.getText().toString());
    	memerchant.setDescription(intro.getText().toString());
    	StoreData.updateMerchant(new Callback<String>() {
			@Override
			public void onHandle(String param) {
				// TODO Auto-generated method stub
					try {
						JSONObject object = new JSONObject(param);
						int error = object.getInt("error");
						if (error == 0) {
							name.getText().clear();
							intro.getText().clear();
							//waitDialog.dismiss();
							finish();
							//AppContext.self.setIsR1(true);
							Toast.makeText(EditMerchant.this, "更新成功", Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(EditMerchant.this, "更新失败,请重新操作", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		},memerchant,headPath);
    }
    // 删除图片提示框
    public void showUpdateDialog(final AlertDialog dlg, final int item)
    {
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.delete_picture);

        TextView message = (TextView) window
                .findViewById(R.id.tv_delete_picture_message);
        message.setText("亲，您确认要删除么？");
        Button cancel = (Button) window
                .findViewById(R.id.btn_delete_picture_cancel);
        Button sure = (Button) window
                .findViewById(R.id.btn_delete_picture_sure);
        cancel.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                dlg.dismiss();
            }
        });

        sure.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                pics.remove(item);
                adapter.notifyDataSetChanged();
                dlg.dismiss();
            }
        });
    }

	@Override
	public void initListener() {
		// TODO 自动生成的方法存根
		
	}

	

	@Override
	public String getTitleStr() {
		// TODO 自动生成的方法存根
		return "店铺管理";
	}
	
	void ShowPickDialog() {
		new AlertDialog.Builder(EditMerchant.this).setTitle("设置头像...")
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(intent, 1);

					}
				}).setPositiveButton("拍照", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						// 下面这句指定调用相机拍照后的照片存储的路径
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "header.jpg")));
						startActivityForResult(intent, 2);
					}
				}).show();
	}

	@Override
	public int getHeaderImageViewId() {
		// TODO 自动生成的方法存根
		return R.id.head;
	}

	@Override
	public void uploadImage(final Drawable drawable) {

		waitDialog = DialogUtil.waitingNew(EditMerchant.this);
		waitDialog.show();
		// 上传图片
		AsyncUtil.goAsync(new Callable<Result<String>>() {

			@Override
			public Result<String> call() throws Exception {
				return StoreData.upload(ImageUtils.drawableToByte(drawable));
			}
		}, new Callback<Result<String>>() {

			@Override
			public void onHandle(Result<String> result) {
				
				if (result.getError() == 0) {
					String url = result.getData();																	
					memerchant.setHead(url);
					waitDialog.dismiss();					
				} else {
					waitDialog.dismiss();
					Toast.makeText(EditMerchant.this, "上传头像失败：" + result.getErrorMsg(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	
}
