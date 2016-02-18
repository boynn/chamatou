package cn.chamatou.biz.common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import cn.chamatou.biz.R;
import cn.chamatou.biz.async.Callback;

/**
 * @author shiner
 */
public class UIUtils {
	public static final int ANIMATION_FADE_IN_TIME = 250;
	public static SimpleDateFormat dateFormate = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateFormateEndmm = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	public static SimpleDateFormat dateFormateEnddd = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static Handler uiHandler = new Handler(Looper.getMainLooper());

	public static boolean isGoogleTV(Context context) {
		return context.getPackageManager().hasSystemFeature("com.google.android.tv");
	}

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasICS() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isHoneycombTablet(Context context) {
		return hasHoneycomb() && isTablet(context);
	}

	public static String toHexColor(int r, int g, int b) {
		return "#" + toBrowserHexValue(r) + toBrowserHexValue(g)
				+ toBrowserHexValue(b);
	}

	private static String toBrowserHexValue(int number) {
		StringBuilder builder = new StringBuilder(
				Integer.toHexString(number & 0xff));
		while (builder.length() < 2) {
			builder.append("0");
		}
		return builder.toString().toUpperCase();
	}

	public static void safeOpenLink(Context context, Intent linkIntent) {
		try {
			context.startActivity(linkIntent);
		} catch (Exception e) {
			Toast.makeText(context, "不能打开链接", Toast.LENGTH_SHORT).show();
		}
	}

	public static View getIndexView(AbsListView listView, int itemIndex) {
		View v = null;
		try {
			int visiblePosition = listView.getFirstVisiblePosition();
			int targetViewIndex = itemIndex - visiblePosition;
			if (targetViewIndex >= 0
					&& targetViewIndex < listView.getChildCount()) {
				v = listView.getChildAt(targetViewIndex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static void openImage(Context ctx, String fullName) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(fullName));
		intent.setDataAndType(uri, "image/*");
		safeOpenLink(ctx, intent);
	}

	public static void openPath(Context ctx, String path) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.setDataAndType(Uri.parse(path), "file/*");
		safeOpenLink(ctx, intent);
	}

	public static void hideSoftInput(Context ctx, View paramEditText) {
		((InputMethodManager) ctx
				.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
	}

	public static void showKeyBoard(final View paramEditText) {
		paramEditText.requestFocus();
		paramEditText.post(new Runnable() {
			@Override
			public void run() {
				((InputMethodManager) paramEditText.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE))
						.showSoftInput(paramEditText, 0);
			}
		});
	}

	public static DecimalFormat percentFormatePoint = new DecimalFormat("0.00");
	public static DecimalFormat percentFormate = new DecimalFormat("0");

	public static String showSize(long size) {
		String result = null;
		if (size / 1024 / 1024 > 0) {
			result = percentFormatePoint.format(((double) size) / 1024 / 1024)
					+ "Mb";
		} else if (size / 1024 > 0) {
			result = (size / 1024) + "Kb";
		} else {
			result = "0Kb";
		}
		return result;
	}

	public static String showSize(long size, boolean withPoint) {
		DecimalFormat format = percentFormatePoint;
		if (!withPoint) {
			format = percentFormate;
		}
		String result = null;
		if (size / 1024 / 1024 > 0) {
			result = format.format(((double) size) / 1024 / 1024) + "Mb";
		} else if (size / 1024 > 0) {
			result = (size / 1024) + "Kb";
		} else {
			result = "0Kb";
		}
		return result;
	}

	@SuppressLint("NewApi")
	public static String getMimeType(String fullName) {
		String type = null;
		int filenamePos = fullName.lastIndexOf('/');
		String filename = 0 <= filenamePos ? fullName
				.substring(filenamePos + 1) : fullName;
		String extension = null;
		if (!filename.isEmpty()) {
			int dotPos = filename.lastIndexOf('.');
			if (0 <= dotPos) {
				extension = filename.substring(dotPos + 1);
			}
		}
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}

	@SuppressLint("NewApi")
	public static void showPopUpWindow(Context ctx, View anchor, List<String> datas,
			final OnItemClickListener onItemClick) {
		ListView lv = new ListView(ctx);
		final PopupWindow mPopupWindow = new PopupWindow(lv, anchor.getWidth(),
				LayoutParams.WRAP_CONTENT);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(ctx
				.getResources()));
		mPopupWindow.setFocusable(true);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,
				R.layout.simple_list_item);
		adapter.addAll(datas);
		lv.setAdapter(adapter);
		lv.setCacheColorHint(Color.TRANSPARENT);
		lv.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onItemClick.onItemClick(parent, view, position, id);
				mPopupWindow.dismiss();
			}
		});
		mPopupWindow.showAsDropDown(anchor);
	}
	
	public static byte[] eccrypt(String info) throws NoSuchAlgorithmException {
		// 根据MD5算法生成MessageDigest对象
		MessageDigest md5 = MessageDigest.getInstance("SHA-1");
		byte[] srcBytes = info.getBytes();
		// 使用srcBytes更新摘要
		md5.update(srcBytes);
		// 完成哈希计算，得到result
		byte[] resultBytes = md5.digest();
		return resultBytes;
	}

	public static byte[] eccrypt(String info, String provider) throws UnsupportedEncodingException,
			InvalidKeyException, NoSuchAlgorithmException {
		SecretKeySpec signingKey = new SecretKeySpec(provider.getBytes("UTF-8"), "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signingKey);
		byte[] rawHmac = mac.doFinal(info.getBytes("UTF-8"));
		return rawHmac;
	}
	
	
	public static boolean isJSONObj(String resultStr){
		if(resultStr.startsWith("{")){
			 return true;
		} 
		return false;
	}
	
	public static String getErrorMsg(String resultStr){
		String returnStr = "";
		if(resultStr.startsWith("{")){
			JSONObject obj;
			try {
				obj = new JSONObject(resultStr);
				if(obj.has("errormsg")){
					returnStr = obj.getString("errormsg");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} 
		if(returnStr.equals("null") || StringUtils.isEmpty(returnStr)){
			returnStr = "未知错误";
		}
		return returnStr;
	}
	
	
	
	
	public static boolean isSucResult(String resultStr){
		try {
			JSONObject obj = new JSONObject(resultStr);
			int error = obj.getInt("error");
			if(error == 0){
				return true;
			}else{
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static boolean isJSONArray(String resultStr){
		if(resultStr.startsWith("[")){
			 return true;
		} 
		return false;
	}
	
	public static void doS(Context context,String param ,Callback<String> callback){
		if(UIUtils.isJSONObj(param)){
			if(UIUtils.isSucResult(param)){
			}else{
				String errorMsg  = UIUtils.getErrorMsg(param);
				if(context == null){
					
				}else{
//					Toast.makeText(context,errorMsg, Toast.LENGTH_SHORT).show();
				}
			}
		}else if(UIUtils.isJSONArray(param)){
			callback.onHandle(param);
		}
	}
	
}
