package cn.chamatou.biz.ui;

import org.json.JSONException;
import org.json.JSONObject;

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
import cn.chamatou.biz.AppException;
import cn.chamatou.biz.R;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIHelper;

/**
 * 设置密码对话框
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class SettingPwd extends Activity{
	
	private ViewSwitcher mViewSwitcher;
	private ImageButton btn_close;
	private Button btn_login;
	private EditText oPwd;
	private EditText nPwd;
	private InputMethodManager imm;
	
	public final static int LOGIN_OTHER = 0x00;
	public final static int LOGIN_MAIN = 0x01;
	public final static int LOGIN_SETTING = 0x02;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingpwd_dialog);
        
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        
        
        mViewSwitcher = (ViewSwitcher)findViewById(R.id.logindialog_view_switcher);       
        oPwd = (EditText)findViewById(R.id.old_password);
        nPwd = (EditText)findViewById(R.id.new_password);
        
        btn_close = (ImageButton)findViewById(R.id.confirm_close_button);
        btn_close.setOnClickListener(UIHelper.finish(this));        
        
        btn_login = (Button)findViewById(R.id.pwd_btn_confirm);
        btn_login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//隐藏软键盘
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
				
				String opwd = oPwd.getText().toString();
				String npwd = nPwd.getText().toString();
				//判断输入
				if(StringUtils.isEmpty(opwd)||StringUtils.isEmpty(npwd)){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_login_pwd_null));
					return;
				}
				
		        btn_close.setVisibility(View.GONE);
		        mViewSwitcher.showNext();
		        
		        changPwd(opwd,npwd);
			}
		});
        
    }
    
    //登录验证
    private void changPwd(final String opwd,final String npwd) {		
    	AppContext ac = (AppContext)getApplication();
    	String res;
		try {
			res = ac.changPwd(opwd,npwd);
			JSONObject ojson = new JSONObject(res);
			finish();
			UIHelper.ToastMessage(SettingPwd.this, ojson.getString("errormsg"));
		} catch (AppException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
    }
}
