package cn.chamatou.biz.common;

import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickAction;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.AppException;
import cn.chamatou.biz.AppManager;
import cn.chamatou.biz.R;
import cn.chamatou.biz.api.ApiClient;
import cn.chamatou.biz.bean.Contact;
import cn.chamatou.biz.bean.Dealer;
import cn.chamatou.biz.bean.Goods;
import cn.chamatou.biz.bean.Interact;
import cn.chamatou.biz.bean.JoinApp;
import cn.chamatou.biz.bean.Notice;
import cn.chamatou.biz.bean.Order;
import cn.chamatou.biz.bean.Remind;
import cn.chamatou.biz.bean.Transfer;
import cn.chamatou.biz.bean.URLs;
import cn.chamatou.biz.ui.About;
import cn.chamatou.biz.ui.ConfirmPwd;
import cn.chamatou.biz.ui.EditGoods;
import cn.chamatou.biz.ui.EditStore;
import cn.chamatou.biz.ui.ImageDialog;
import cn.chamatou.biz.ui.ImageZoomDialog;
import cn.chamatou.biz.ui.LoginDialog;
import cn.chamatou.biz.ui.Main;
import cn.chamatou.biz.ui.NewArticle;
import cn.chamatou.biz.ui.NewGoods;
import cn.chamatou.biz.ui.NoticeDetail;
import cn.chamatou.biz.ui.OrderDetail;
import cn.chamatou.biz.ui.Setting;
import cn.chamatou.biz.ui.SettingPwd;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class UIHelper {

	public final static int LISTVIEW_ACTION_INIT = 0x01;
	public final static int LISTVIEW_ACTION_REFRESH = 0x02;
	public final static int LISTVIEW_ACTION_SCROLL = 0x03;
	public final static int LISTVIEW_ACTION_CHANGE_TYPE = 0x04;
	
	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;
	
	public final static int LISTVIEW_DATATYPE_GOODS = 0x01;
	public final static int LISTVIEW_DATATYPE_ORDER = 0x02;
	
	public final static int REQUEST_CODE_FOR_RESULT = 0x01;
	public final static int REQUEST_CODE_FOR_REPLY = 0x02;
	
	/** 全局web样式 */
	public final static String WEB_STYLE = "<style>* {font-size:16px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;} " +
			"img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} " +
			"pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;} " +
			"a.tag {font-size:15px;text-decoration:none;background-color:#bbd6f3;border-bottom:2px solid #3E6D8E;border-right:2px solid #7F9FB6;color:#284a7b;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;}</style>";
	/**
	 * 显示首页
	 * @param activity
	 */
	public static void showHome(Activity activity)
	{
		Intent intent = new Intent(activity,Main.class);
		activity.startActivity(intent);
		activity.finish();
	}
	
	/**
	 * 显示登录页面
	 * @param activity
	 */
	public static void showLoginDialog(Context context)
	{
		Intent intent = new Intent(context,LoginDialog.class);
		if(context instanceof Main)
			intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_MAIN);
		else if(context instanceof Setting)
			intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_SETTING);
		else
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	/**
	 * 验证密码页面
	 * @param activity
	 */
	public static void showSetPwdDialog(Context context)
	{
		Intent intent = new Intent(context,SettingPwd.class);
		context.startActivity(intent);
	}
	public static void showNoticeDetail(Context context, int notice_id)
	{
		Intent intent = new Intent(context,NoticeDetail.class);
		intent.putExtra("Id", notice_id);
		context.startActivity(intent);
	}
	/**
	 * 设置密码页面
	 * @param activity
	 */
	public static void showConfirmDialog(Context context)
	{
		Intent intent = new Intent(context,ConfirmPwd.class);
		context.startActivity(intent);
	}
	
	
	public static void showOrderDetail(Context context, Integer odid) {
		Intent intent = new Intent(context, OrderDetail.class);
		intent.putExtra("orderId", odid);
		context.startActivity(intent);
	}
	
	
	
	/**
	 * 商品操作选择框
	 * @param context
	 * @param thread
	 */
	public static void showGoodsOptionDialog(final Context context,final Thread thread)
	{
		new AlertDialog.Builder(context)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(thread != null)
					thread.start();
				else
					ToastMessage(context, R.string.msg_noaccess_delete);
				dialog.dismiss();
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.create().show();
	}
	
	
	/**
	 * 显示图片对话框
	 * @param context
	 * @param imgUrl
	 */
	public static void showImageDialog(Context context, String imgUrl)
	{
		Intent intent = new Intent(context, ImageDialog.class);
		intent.putExtra("img_url", imgUrl);
		context.startActivity(intent);
	}
	public static void showImageZoomDialog(Context context, String imgUrl)
	{
		Intent intent = new Intent(context, ImageZoomDialog.class);
		intent.putExtra("img_url", imgUrl);
		context.startActivity(intent);
	}
	
	/**
	 * 显示系统设置界面
	 * @param context
	 */
	public static void showSetting(Context context)
	{
		Intent intent = new Intent(context, Setting.class);
		context.startActivity(intent);
	}	
	
	
	/**
	 * 加载显示图片
	 * @param imgFace
	 * @param faceURL
	 * @param errMsg
	 */
	public static void showLoadImage(final ImageView imgView,final String imgURL,final String errMsg)
	{
		//读取本地图片
		if(StringUtils.isEmpty(imgURL) || imgURL.endsWith("portrait.gif")){
			Bitmap bmp = BitmapFactory.decodeResource(imgView.getResources(), R.drawable.widget_dface);
			imgView.setImageBitmap(bmp);
			return;
		}
		
		//是否有缓存图片
    	final String filename = FileUtils.getFileName(imgURL);
    	//Environment.getExternalStorageDirectory();返回/sdcard
    	String filepath = imgView.getContext().getFilesDir() + File.separator + filename;
		File file = new File(filepath);
		if(file.exists()){
			Bitmap bmp = ImageUtils.getBitmap(imgView.getContext(), filename);
			imgView.setImageBitmap(bmp);
			return;
    	}
		
		//从网络获取&写入图片缓存
		String _errMsg = imgView.getContext().getString(R.string.msg_load_image_fail);
		if(!StringUtils.isEmpty(errMsg))
			_errMsg = errMsg;
		final String ErrMsg = _errMsg;
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what==1 && msg.obj != null){
					imgView.setImageBitmap((Bitmap)msg.obj);
					try {
                    	//写图片缓存
						ImageUtils.saveImage(imgView.getContext(), filename, (Bitmap)msg.obj);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					ToastMessage(imgView.getContext(), ErrMsg);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();
				try {
					Bitmap bmp = ApiClient.getNetBitmap((AppContext) imgView.getContext().getApplicationContext(), imgURL);
					msg.what = 1;
					msg.obj = bmp;
				} catch (AppException e) {
					e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	/**
	 * url跳转
	 * @param context
	 * @param url
	 */
	public static void showUrlRedirect(Context context, String url){
		URLs urls = URLs.parseURL(url);
		if(urls != null){
			showLinkRedirect(context, urls.getObjType(), urls.getObjId(), urls.getObjKey());
		}else{
			openBrowser(context, url);
		}
	}
	
	public static void showLinkRedirect(Context context, int objType, int objId, String objKey){
		switch (objType) {
			case URLs.URL_OBJ_TYPE_BLOG:
				break;
			case URLs.URL_OBJ_TYPE_OTHER:
				openBrowser(context, objKey);
				break;
		}
	}
	
	/**
	 * 打开浏览器
	 * @param context
	 * @param url
	 */
	public static void openBrowser(Context context, String url){
		try {
			Uri uri = Uri.parse(url);  
			Intent it = new Intent(Intent.ACTION_VIEW, uri);  
			context.startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			ToastMessage(context, "无法浏览此网页", 500);
		} 
	}
		
	/**
	 * 获取webviewClient对象
	 * @return
	 */
	public static WebViewClient getWebViewClient(){
		return new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view,String url) {
				showUrlRedirect(view.getContext(), url);
				return true;
			}
		};
	}
	
	/**
	 * 获取TextWatcher对象
	 * @param context
	 * @param tmlKey
	 * @return
	 */
	public static TextWatcher getTextWatcher(final Activity context, final String temlKey) {
		return new TextWatcher() {		
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//保存当前EditText正在编辑的内容
				((AppContext)context.getApplication()).setProperty(temlKey, s.toString());
			}		
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}		
			public void afterTextChanged(Editable s) {}
		};
	}
	
	/**
	 * 编辑器显示保存的草稿
	 * @param context
	 * @param editer
	 * @param temlKey
	 */
	public static void showTempEditContent(Activity context, EditText editer, String temlKey) {
		String tempContent = ((AppContext)context.getApplication()).getProperty(temlKey);
		if(!StringUtils.isEmpty(tempContent)) {
			editer.setText(tempContent);
			editer.setSelection(tempContent.length());//设置光标位置
		}
	}
	

	/**
	 * 发送通知广播
	 * @param context
	 * @param notice
	 */
	public static void sendBroadCast(Context context, Notice notice){
		if(!((AppContext)context.getApplicationContext()).isLogin() || notice==null) return;
		Intent intent = new Intent("cn.chamatou.biz.action.APPWIDGET_UPDATE"); 
		context.sendBroadcast(intent);
	}
	
	/**
	 * 发送广播-新建采购订单
	 * @param context
	 * @param notice
	 */
	public static void sendBroadCastOrder(Context context, int what, String res, Order order){
		//if(res==null && porder==null) return;
		Intent intent = new Intent("cn.chamatou.biz.action.NEW_INFO"); 
		intent.putExtra("INFO_TYPE", 1);
		intent.putExtra("ORDER", order);
		context.sendBroadcast(intent);
	}
	
	public static void sendBroadCastContact(Context context, int what,
			String res, Contact contact) {
		Intent intent = new Intent("cn.chamatou.biz.action.NEW_INFO"); 
		intent.putExtra("INFO_TYPE", 3);
		intent.putExtra("CONTACT", contact);
		context.sendBroadcast(intent);
	}
	public static void sendBroadCastInteract(Context context, int what,
			String res, Interact interact) {
		Intent intent = new Intent("cn.chamatou.biz.action.NEW_INFO"); 
		intent.putExtra("INFO_TYPE", 3);
		intent.putExtra("CONTACT", interact);
		context.sendBroadcast(intent);
	}
	public static void sendBroadCastRemind(Context context, int what,
			String res, Remind remind) {
		Intent intent = new Intent("cn.chamatou.biz.action.NEW_INFO"); 
		intent.putExtra("INFO_TYPE", 3);
		intent.putExtra("CONTACT", remind);
		context.sendBroadcast(intent);
	}
	/**
	 * 发送广播-新建会员
	 * @param context
	 * @param notice
	 */
	public static void sendBroadCastDealer(Context context, int what, String res, Dealer dealer){
		//if(res==null && dealer==null) return;
		Intent intent = new Intent("cn.chamatou.biz.action.NEW_INFO"); 
		intent.putExtra("INFO_TYPE", 4);
		intent.putExtra("DEALER", dealer);
		context.sendBroadcast(intent);
	}
	public static void sendBroadCastJoinApp(Context context, int what, String res, JoinApp joinapp){
		//if(res==null && dealer==null) return;
		Intent intent = new Intent("cn.chamatou.biz.action.NEW_INFO"); 
		intent.putExtra("INFO_TYPE", 7);
		intent.putExtra("JOINAPP", joinapp);
		context.sendBroadcast(intent);
	}
	public static void sendBroadCastTransfer(Context context, int what, String res, Transfer transfer){
		//if(res==null && dealer==null) return;
		Intent intent = new Intent("cn.chamatou.biz.action.NEW_INFO"); 
		intent.putExtra("INFO_TYPE", 6);
		intent.putExtra("TRANSFER", transfer);
		context.sendBroadcast(intent);
	}
	public static void sendBroadCastGoods(Context context){
		//if(res==null && client==null) return;
		Intent intent = new Intent("cn.chamatou.biz.action.NEW_INFO"); 
		intent.putExtra("INFO_TYPE", 1);
		context.sendBroadcast(intent);
	}
	/**
	 * 组合动态的动作文本
	 * @param objecttype
	 * @param objectcatalog
	 * @param objecttitle
	 * @return
	 */
	@SuppressLint("NewApi")
	public static SpannableString parseActiveAction(String author,int objecttype,int objectcatalog,String objecttitle){
		String title = "";
		int start = 0;
		int end = 0;
		if(objecttype==100){
			title = "更新了动态";
		}
		title = author + " " + title;
		SpannableString sp = new SpannableString(title);
		//设置用户名字体大小、加粗、高亮 
		sp.setSpan(new AbsoluteSizeSpan(14,true), 0, author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 0, author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置标题字体大小、高亮 
        if(!StringUtils.isEmpty(objecttitle)){
        	start = title.indexOf(objecttitle);
			if(objecttitle.length()>0 && start>0){
				end = start + objecttitle.length();  
				sp.setSpan(new AbsoluteSizeSpan(14,true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
        }
        return sp;
	}
	
	/**
	 * 组合动态的回复文本
	 * @param name
	 * @param body
	 * @return
	 */
	public static SpannableString parseActiveReply(String name,String body){
		SpannableString sp = new SpannableString(name+"："+body);
		//设置用户名字体加粗、高亮 
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
	}
	
	/**
	 * 组合消息文本
	 * @param name
	 * @param body
	 * @return
	 */
	public static SpannableString parseMessageSpan(String name,String body,String action){
		SpannableString sp = null;
		int start = 0;
		int end = 0;
		if(StringUtils.isEmpty(action)){
			sp = new SpannableString(name + "：" + body);
			end = name.length();
		}else{
			sp = new SpannableString(action + name + "：" + body);
			start = action.length();
			end = start + name.length();
		}
		//设置用户名字体加粗、高亮 
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
	}
	
	/**
	 * 弹出Toast消息
	 * @param msg
	 */
	public static void ToastMessage(Context cont,String msg)
	{
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	public static void ToastMessage(Context cont,int msg)
	{
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	public static void ToastMessage(Context cont,String msg,int time)
	{
		Toast.makeText(cont, msg, time).show();
	}
	
	/**
	 * 点击返回监听事件
	 * @param activity
	 * @return
	 */
	public static View.OnClickListener finish(final Activity activity)
	{
		return new View.OnClickListener() {
			public void onClick(View v) {
				activity.finish();
			}
		};
	}	
	
	/**
	 * 显示关于我们
	 * @param context
	 */
	public static void showAbout(Context context)
	{
		Intent intent = new Intent(context, About.class);
		context.startActivity(intent);
	}
	
	public static void editStore(Context context) {
		Intent intent = new Intent(context, EditStore.class);
		((Activity) context).startActivityForResult(intent, 1);
	}

	public static void newGoods(Context context)
	{
		Intent intent = new Intent(context, NewGoods.class);
		//context.startActivity(intent);
		((Activity) context).startActivityForResult(intent, 1);
	}
	public static void EditGoods(Context context, Goods goods) {
		Intent intent = new Intent(context, EditGoods.class);
		Bundle mBundle = new Bundle();  
		mBundle.putSerializable("EDIT_GOODS",goods);  
		intent.putExtras(mBundle);
		((Activity) context).startActivityForResult(intent, 1);
	}
	public static void showNewGoods(Context context)
	{
		Intent intent = new Intent(context, NewGoods.class);
		//context.startActivity(intent);
		((Activity) context).startActivityForResult(intent, 1);
	}
	public static void showNewArticle(Context context)
	{
		Intent intent = new Intent(context, NewArticle.class);
		//context.startActivity(intent);
		((Activity) context).startActivityForResult(intent, 1);
	}
	
	public static void hideSoftInput(Context ctx, View paramEditText) {
		((InputMethodManager) ctx
				.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
	}

	/**
	 * 用户登录或注销
	 * @param activity
	 */
	public static void loginOrLogout(Activity activity)
	{
		AppContext ac = (AppContext)activity.getApplication();
		if(ac.isLogin()){
			ac.Logout();
			ToastMessage(activity, "已退出登录");
		}else{
			showLoginDialog(activity);
		}
	}
	
	/**
	 * 文章是否加载图片显示
	 * @param activity
	 */
	public static void changeSettingIsLoadImage(Activity activity)
	{
		AppContext ac = (AppContext)activity.getApplication();
		if(ac.isLoadImage()){
			ac.setConfigLoadimage(false);
			ToastMessage(activity, "已设置文章不加载图片");
		}else{
			ac.setConfigLoadimage(true);
			ToastMessage(activity, "已设置文章加载图片");
		}
	}
	public static void changeSettingIsLoadImage(Activity activity,boolean b)
	{
		AppContext ac = (AppContext)activity.getApplication();
		ac.setConfigLoadimage(b);
	}
	
	/**
	 * 清除app缓存
	 * @param activity
	 */
	public static void clearAppCache(Activity activity)
	{
		final AppContext ac = (AppContext)activity.getApplication();
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what==1){
					ToastMessage(ac, "缓存清除成功");
				}else{
					ToastMessage(ac, "缓存清除失败");
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();
				try {				
					ac.clearAppCache();
					msg.what = 1;
				} catch (Exception e) {
					e.printStackTrace();
	            	msg.what = -1;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	/**
	 * 退出程序
	 * @param cont
	 */
	public static void Exit(final Context cont)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_menu_surelogout);
		builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//退出
				AppManager.getAppManager().AppExit(cont);
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}





}
