package cn.chamatou.biz.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.AppException;
import cn.chamatou.biz.R;
import cn.chamatou.biz.async.Result;
import cn.chamatou.biz.bean.URLs;
import cn.chamatou.biz.bean.User;
import cn.chamatou.biz.common.HttpUtil;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIHelper;
import cn.chamatou.biz.common.UiTool;
import cn.chamatou.biz.widget.DialogUtil;

/**
 * 商家注册对话框
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class RegDialog extends Activity{
	
	private ViewSwitcher mViewSwitcher;
	private ImageButton btn_close;
	private Button btn_reg,vcodeBtn;
	private AutoCompleteTextView mName,mTel,mPwd;
	private EditText vcodeEditText;
	private TextView mReg;
	private AnimationDrawable loadingAnimation;
	private View loginLoading;
	private CheckBox chb_agree;
	private InputMethodManager imm;
	private boolean isquit;
	public final static int LOGIN_OTHER = 0x00;
	public final static int LOGIN_MAIN = 0x01;
	public final static int LOGIN_SETTING = 0x02;
	public Context context;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
		setContentView(R.layout.reg_dialog);
        
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        
        mViewSwitcher = (ViewSwitcher)findViewById(R.id.logindialog_view_switcher);       
        loginLoading = (View)findViewById(R.id.login_loading);
        mName = (AutoCompleteTextView)findViewById(R.id.reg_name);
        vcodeEditText = (EditText) findViewById(R.id.vcode);
        mTel = (AutoCompleteTextView)findViewById(R.id.reg_tel);
        mPwd = (AutoCompleteTextView)findViewById(R.id.reg_pwd);
        vcodeBtn = (Button)findViewById(R.id.vcode_btn);
        mReg = (TextView)findViewById(R.id.agreement);
        chb_agree = (CheckBox)findViewById(R.id.login_checkbox_agree);
        
        btn_close = (ImageButton)findViewById(R.id.login_close_button);
        btn_close.setOnClickListener(UIHelper.finish(this));        
        vcodeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String phone = mTel.getText().toString().trim();
				if (phone.length() != 11) {
					DialogUtil.alert(R.drawable.dialog_error,
							RegDialog.this, "错误", "请输入正确的手机号码");
				} else {
					vcodeBtn.setEnabled(false);
					new Thread() {
						private Handler vcodeHandler = new Handler() {
							public void handleMessage(Message msg) {
								if (msg.arg1 == 0) {
									vcodeBtn.setText("重新获取\n验证码");
									vcodeBtn.setEnabled(true);
								} else if (msg.arg1 == 100) {
									vcodeBtn.setText("网络异常\n请设置");
									vcodeBtn.setEnabled(false);
									DialogUtil.alert(R.drawable.dialog_error,
											RegDialog.this, "错误", "网络异常");

								} else {
									if (vcodeBtn.getText() != "网络异常\n请设置") {
										vcodeBtn.setText("重新获取\n" + msg.arg1
												+ "s");
									}
								}
							}
						};

						public void run() {
							try {
								new Thread() {
									public void run() {
										for (int i = 90; i >= 0; i--) {

											if (!isquit) {
												SystemClock.sleep(1000);
												Message message = new Message();
												message.arg1 = i;
												vcodeHandler
														.sendMessage(message);
											}

										}
									}
								}.start();

								Long timestamp = Calendar.getInstance()
										.getTimeInMillis();
								List<NameValuePair> paramList = new ArrayList<NameValuePair>();

								paramList.add(new BasicNameValuePair("mobile",
										phone));
								paramList.add(new BasicNameValuePair(
										"timestamp", timestamp.toString()));
								paramList.add(new BasicNameValuePair("sign",
										UiTool.sign(timestamp, phone)));
								HttpUtil.post(URLs.URL_API_HOST
										+ "Store/regSms", paramList);
								Log.e("content", "fvf  ==========" + paramList);
								// System.out.println("fvf  =========="+paramList);

							} catch (Throwable e) {
								e.printStackTrace();
								Message msg = new Message();
								msg.arg1 = 100;
								isquit = true;
								vcodeHandler.sendMessage(msg);

							}
						}
					}.start();
				}
			}
		});

        btn_reg = (Button)findViewById(R.id.reg_btn_commit);
        btn_reg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//隐藏软键盘
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
				
				String sname = mName.getText().toString();
				String tel = mTel.getText().toString();
				String pwd = mPwd.getText().toString();
				final String vcode = vcodeEditText.getText().toString().trim();
				boolean isAgree = chb_agree.isChecked();

				if(StringUtils.isEmpty(sname)){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_reg_name_null));
					return;
				}
				if(StringUtils.isEmpty(tel)){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_reg_tel_null));
					return;
				}
				if(!isAgree){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_reg_agree_tip));
					return;
				}
				if (tel.length() != 11) {
					DialogUtil.alert(R.drawable.dialog_error,
							RegDialog.this, "错误", "请输入正确的手机号码");
				} else if (vcode.equals("")) {
					DialogUtil.alert(R.drawable.dialog_error,
							RegDialog.this, "错误", "验证码不能为空");
				} else {
					btn_reg.setEnabled(false);
					register(sname, tel,pwd,vcode);
				}
				
//		        btn_close.setVisibility(View.GONE);
//		        loadingAnimation = (AnimationDrawable)loginLoading.getBackground();
//		        loadingAnimation.start();
//		        mViewSwitcher.showNext();
//		        
		        
			}
		});
       mReg.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_reg_agree_tip));
				
        	}
        });
        //是否显示登录信息        
       //System.out.println("dfsdfd");
       
    }
    
    //登录验证
    private void register(final String sname, final String tel,final String pwd,final String vcode) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what == 0){
						//提示注册成功
						UIHelper.ToastMessage(RegDialog.this, R.string.msg_reg_success);
						 Intent intent = new Intent();  
			                intent.putExtra("mobile", tel);  
			                intent.putExtra("pwd", pwd);  
			                setResult(2,intent);  			                
			                finish();
					
				}else if(msg.what == 1){
					mViewSwitcher.showPrevious();
					btn_close.setVisibility(View.VISIBLE);
					UIHelper.ToastMessage(RegDialog.this, msg.obj.toString());
				}else if(msg.what == -1){
					mViewSwitcher.showPrevious();
					btn_close.setVisibility(View.VISIBLE);
					((AppException)msg.obj).makeToast(RegDialog.this);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg =new Message();
				try {
					AppContext ac = (AppContext)getApplication(); 
	                String ret = ac.storeReg(sname,tel,pwd,vcode);
	                JSONObject json = new JSONObject(StringUtils.removeBOM(ret));   
	    			Result res = new Result();
	    			res.setError(json.getInt("error"));
	    			res.setErrorMsg(json.getString("errormsg"));
	    			if(res.ok()){
	                	msg.what = 0;//成功
	                	msg.obj = res.getErrorMsg();
	                }else{
	                	msg.what = 1;//失败
	                	msg.obj = res.getErrorMsg();
	                }
	            } catch (AppException e) {
	            	e.printStackTrace();
			    	msg.what = -1;
			    	msg.obj = e;
	            } catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}.start();
    }
}
