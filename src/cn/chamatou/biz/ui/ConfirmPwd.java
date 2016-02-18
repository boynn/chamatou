package cn.chamatou.biz.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;

import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.R;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIHelper;

/**
 * 密码验证对话框
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class ConfirmPwd extends Activity{
	
	private ViewSwitcher mViewSwitcher;
	private ImageButton btn_close;
	private Button btn_login;
	private EditText mPwd;
	private InputMethodManager imm;
	
	public final static int LOGIN_OTHER = 0x00;
	public final static int LOGIN_MAIN = 0x01;
	public final static int LOGIN_SETTING = 0x02;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmpwd_dialog);
        
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        
        
        mViewSwitcher = (ViewSwitcher)findViewById(R.id.logindialog_view_switcher);       
        mPwd = (EditText)findViewById(R.id.confirm_password);
        
        btn_close = (ImageButton)findViewById(R.id.confirm_close_button);
        btn_close.setOnClickListener(UIHelper.finish(this));        
        
        btn_login = (Button)findViewById(R.id.pwd_btn_confirm);
        btn_login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//隐藏软键盘
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
				
				String pwd = mPwd.getText().toString();
				//判断输入
				if(StringUtils.isEmpty(pwd)){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_login_pwd_null));
					return;
				}
				
		        btn_close.setVisibility(View.GONE);
		        mViewSwitcher.showNext();
		        
		        checkPwd(pwd);
			}
		});
        
    }
    
    //登录验证
    private void checkPwd(final String pwd) {		
    	AppContext ac = (AppContext)getApplication();
    	if(ac.isInfoPwd(pwd)){
    		ac.setConfigHttpsLogin(true);
			Intent intent = new Intent(ConfirmPwd.this, Setting.class);
			startActivity(intent);
			finish();
		}else{
			ac.setConfigHttpsLogin(false);
			mViewSwitcher.showPrevious();
			btn_close.setVisibility(View.VISIBLE);
			UIHelper.ToastMessage(ConfirmPwd.this, getString(R.string.msg_pwd_fail));
		}
		
    }
}
