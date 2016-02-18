package cn.chamatou.biz.common;

import cn.chamatou.biz.ui.LoginDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;


/**
 * @author shiner
 */
public class AccountTool extends XPreference {
	public AccountTool(Context mContext) {
		super("account", mContext);
	}

	public boolean isLogin() {
		return !TextUtils.isEmpty(getString("loginUid", null));
	}

	public String getId() {
		return getString("loginUid", "");
	}

	public void setId(String id) {
		setString("loginUid", id);
	}

	public void goLogin(Context ctx) {
		goLogin(ctx, null);
	}

	public void goLogin(Context ctx, Intent refererIntent) {
		Intent intent = new Intent(ctx, LoginDialog.class);
		if (!(ctx instanceof Activity)) {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		if (refererIntent != null) {
			intent.putExtra("referer", refererIntent);
		}
		ctx.startActivity(intent);
	}
}
