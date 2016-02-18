package cn.chamatou.biz.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import cn.chamatou.biz.api.ApiClient;
import cn.chamatou.biz.async.Result;
import cn.chamatou.biz.bean.User;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIHelper;

/**
 * 用户登录对话框
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class LoginDialog extends Activity{
	
	//private ViewSwitcher mViewSwitcher;
	//private ImageButton btn_close;
	private Button btn_login,mReg;
	private AutoCompleteTextView mAccount;
	private EditText mPwd;
	//private TextView ;
	//private AnimationDrawable loadingAnimation;
	//sprivate View loginLoading;
	//private CheckBox chb_rememberMe;
	private int curLoginType;
	private InputMethodManager imm;
	
	public final static int LOGIN_OTHER = 0x00;
	public final static int LOGIN_MAIN = 0x01;
	public final static int LOGIN_SETTING = 0x02;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog);
        
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        
        curLoginType = getIntent().getIntExtra("LOGINTYPE", LOGIN_OTHER);
        
        //mViewSwitcher = (ViewSwitcher)findViewById(R.id.logindialog_view_switcher);       
        //loginLoading = (View)findViewById(R.id.login_loading);
        mAccount = (AutoCompleteTextView)findViewById(R.id.login_account);
        mPwd = (EditText)findViewById(R.id.login_password);
        mReg = (Button)findViewById(R.id.register);
        //chb_rememberMe = (CheckBox)findViewById(R.id.login_checkbox_rememberMe);
         //btn_close = (ImageButton)findViewById(R.id.login_close_button);
        //btn_close.setOnClickListener(UIHelper.finish(this));        
        btn_login = (Button)findViewById(R.id.login_btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//隐藏软键盘
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
				
				String account = mAccount.getText().toString();
				String pwd = mPwd.getText().toString();
				//boolean isRememberMe = chb_rememberMe.isChecked();
				//判断输入
				if(StringUtils.isEmpty(account)){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_login_email_null));
					return;
				}
				if(StringUtils.isEmpty(pwd)){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_login_pwd_null));
					return;
				}
				
		        //btn_close.setVisibility(View.GONE);
		        //loadingAnimation = (AnimationDrawable)loginLoading.getBackground();
		        //loadingAnimation.start();
		        //mViewSwitcher.showNext();
		        
		        login(account, pwd, true);
			}
		});
        mReg.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(LoginDialog.this, RegDialog.class);
        		startActivityForResult(intent,1);
        	}
        });
         //是否显示登录信息
        AppContext ac = (AppContext)getApplication();
        User user = ac.getLoginInfo();
        if(user==null || !user.isRememberMe()) return;
        if(!StringUtils.isEmpty(user.getAccount())){
        	mAccount.setText(user.getAccount());
        	mAccount.selectAll();
        	//chb_rememberMe.setChecked(user.isRememberMe());
        }
        if(!StringUtils.isEmpty(user.getPwd())){
        	mPwd.setText(user.getPwd());
        }
    }
    
    //登录验证
    private void login(final String account, final String pwd, final boolean isRememberMe) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what == 1){
					User user = (User)msg.obj;
					if(user != null){
						//清空原先cookie
						ApiClient.cleanCookie();
						//提示登陆成功
						UIHelper.ToastMessage(LoginDialog.this, R.string.msg_login_success);
						if(curLoginType == LOGIN_OTHER){
							//跳转--加载用户动态
							Intent intent = new Intent(LoginDialog.this, Main.class);
							intent.putExtra("LOGIN", true);
							startActivity(intent);
						}else if(curLoginType == LOGIN_SETTING){
							//跳转--用户设置页面
							Intent intent = new Intent(LoginDialog.this, Setting.class);
							intent.putExtra("LOGIN", true);
							startActivity(intent);
						}
						finish();
					}
				}else if(msg.what == 0){
					//mViewSwitcher.showPrevious();
					//btn_close.setVisibility(View.VISIBLE);
					UIHelper.ToastMessage(LoginDialog.this, getString(R.string.msg_login_fail)+msg.obj);
				}else if(msg.what == -1){
					//mViewSwitcher.showPrevious();
					//btn_close.setVisibility(View.VISIBLE);
					((AppException)msg.obj).makeToast(LoginDialog.this);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg =new Message();
				try {
					AppContext ac = (AppContext)getApplication(); 
	                User user = ac.loginVerify(account, pwd);
	                user.setAccount(account);
	                user.setPwd(pwd);
	                user.setRememberMe(isRememberMe);
	                Result res = user.getValidate();
	                if(res.ok()){
	                	ac.saveLoginInfo(user);//保存登录信息
	                	msg.what = 1;//成功
	                	msg.obj = user;
	                }else{
	                	ac.cleanLoginInfo();//清除登录信息
	                	msg.what = 0;//失败
	                	msg.obj = res.getErrorMsg();
	                }
	            } catch (AppException e) {
	            	e.printStackTrace();
			    	msg.what = -1;
			    	msg.obj = e;
	            }
				handler.sendMessage(msg);
			}
		}.start();
    }
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        // TODO Auto-generated method stub  
        super.onActivityResult(requestCode, resultCode, data);  
        if(resultCode==2){  
            if(requestCode==1){  
                String mobile = data.getStringExtra("mobile");//接收返回数据  
                String pwd = data.getStringExtra("pwd");//接收返回数据
                System.out.println("mobile"+mobile);
                mAccount.setText(String.valueOf(mobile));  
                mPwd.setText(String.valueOf(pwd));
            }  
        }  
          
    }  
}
