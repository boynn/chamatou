package cn.chamatou.biz.common;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.view.View;
import android.view.View.MeasureSpec;

import cn.chamatou.biz.bean.URLs;

public class UiTool {

	public static int dip2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int px2dip(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	public static String sign(long timestamp, String... params) {
		StringBuffer buf = new StringBuffer();
		for (String param : params) {
			buf.append(param);
		}
		buf.append(timestamp);
		buf.append("8iu*d7&i327^&%&)");
		return MD5.md5(buf.toString());
	}
	
	public static String signHuodong(long timestamp, String... params) {
		StringBuffer buf = new StringBuffer();
		for (String param : params) {
			buf.append(param);
		}
		buf.append(timestamp);
		buf.append("^Cov57dTt-jn*a%D");
		return MD5.md5(buf.toString());
	}

	public static boolean isAction(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public static File createFilePath() {
		return createFilePath(null);
	}

	public static File createFilePath(String fileName) {
		if (fileName == null) {
			fileName = UUID.randomUUID().toString();
		}
		File dir = new File(URLs.DIR_PATH);
		if (!dir.exists()) {
			dir.getPath();
			dir.mkdir();
		}
		File f = new File(URLs.DIR_PATH + fileName);
		return f;
	}

	public static int getFileSize() {
		File dir = new File(URLs.DIR_PATH);
		if (dir.exists()) {
			return dir.list().length;
		} else {
			return 0;
		}
	}

	public static int clearFiles() {
		File dir = new File(URLs.DIR_PATH);
		if (dir.exists()) {
			for (File f : dir.listFiles()) {
				f.delete();
			}
		}
		return 0;
	}

	public static String getToken(Context context) {
		return MD5.md5(context.getPackageName() + Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
	}

	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	public static String buildCode(String phoneNum, String deviceId) {
		String code = null;
		if (phoneNum == null || phoneNum.trim().equals("")) {
			return code;
		}
		// 当设备号为空值时，添加默认设备号
		if (deviceId == null || deviceId.trim().equals("")) {
			deviceId = "201410211038001";
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(phoneNum).append("&").append(deviceId);
		try {
			SecretKeySpec signingKey = new SecretKeySpec("6180a6B65160EfDA".getBytes("utf-8"), "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(buffer.toString().getBytes("utf-8"));
			code = new String(Base64.encode(rawHmac, Base64.DEFAULT), "utf-8");
			code = URLEncoder.encode(code, "utf-8");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return code;
	}
	
	
	/**
	 * 显示收到的消息
	 * 
	 * @param context
	 * @param className
	 * @param mNotificationManager
	 
	public static void showNotification(Context context, PushMsgBean bean) {
//		LayoutInflater layoutInflater = LayoutInflater.from(context);
//      	VIEW VIEW = LAYOUTINFLATER.INFLATE(R.LAYOUT.COMMON_ALERT, NULL);
//		LayoutInflater layoutInflater = LayoutInflater.from(context);
//		View view = layoutInflater.inflate(R.layout.common_alert, null);

		Intent notificationIntent = new Intent(App.self, WebViewActivity.class);
		notificationIntent.putExtra("url",  bean.getUrl());
		notificationIntent.putExtra("title", "消息内容");
		
		
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		notificationIntent.putExtra("url", bean.getUrl());
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		Notification notification = new Notification();
		// if (!StringUtils.isBlank(bean.getUrl())) {
		// notification.contentIntent = contentIntent;
		// }
		notification.icon = R.drawable.ic_launcher;
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.tickerText = bean.getTitle();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		//
		notification.setLatestEventInfo(context, context.getString(R.string.app_name),
				bean.getTitle(), contentIntent);
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify((int) (Math.random() * 100000), notification);
	}*/

	
	 
}
