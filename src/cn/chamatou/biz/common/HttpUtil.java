package cn.chamatou.biz.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;

public class HttpUtil {

	// 上传文件到媒体服务器
	// 返回文件下载URL
	public static String uploadFile(String server, File file) throws Exception {
		System.out.println(server + ":" + file.getPath());
		URL url = new URL(server);
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		HttpPost httpPost = new HttpPost(url.toURI());

		MultipartEntity m = new MultipartEntity();
		m.addPart("file", new FileBody(file));
		httpPost.setEntity(m);
		HttpResponse response = httpclient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception("HTTP请求异常");
		}
		String rst = EntityUtils.toString(response.getEntity(), "UTF-8");
		System.out.println(rst);
		return rst;
	}

	public static String post(String server, List<NameValuePair> paramList) throws Exception {
		System.out.println(server);
		for (NameValuePair nameValuePair : paramList) {
			Log.e("sendmsg---",nameValuePair.getName() + " = " + nameValuePair.getValue());
			//System.out.println(nameValuePair.getName() + " = " + nameValuePair.getValue());
		}

		URL url = new URL(server);
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		
		HttpPost httpPost = new HttpPost(url.toURI());
		
		if (paramList != null) {
			httpPost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
		}

		HttpResponse response = httpclient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception("HTTP请求异常");
		}
		String rst = EntityUtils.toString(response.getEntity(), "UTF-8");
		byte[] b = rst.getBytes("UTF-8");
		int n=0;
		if (b[0] == -17 && b[1] == -69 && b[2] == -65)n=3;
		if (b[3] == -17 && b[4] == -69 && b[5] == -65)n=6;
		if (b[6] == -17 && b[7] == -69 && b[8] == -65)n=9;
		String xx = new String(b,n,b.length-n,"UTF-8");
		return xx;//return rst;
	}

	public static JSONObject postJsonObject(String server, List<NameValuePair> paramList) throws Exception {
		return new JSONObject(post(server, paramList));
	}

	public static JSONObject postJsonObject(String server) throws Exception {
		return new JSONObject(post(server, null));
	}

	public static JSONArray postJsonArray(String server, List<NameValuePair> paramList) throws Exception {
		return new JSONArray(post(server, paramList));
	}

	public static JSONArray postJsonArray(String server) throws Exception {
		return new JSONArray(post(server, null));
	}

	public static byte[] getBytes(URL url) throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		HttpGet httpGet = new HttpGet(url.toURI());
		HttpResponse response = httpclient.execute(httpGet);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception("HTTP请求异常");
		}
		return EntityUtils.toByteArray(response.getEntity());
	}

	public static byte[] getBytes(String url) throws Exception {
		return getBytes(new URL(url));
	}

	public static File downloadCache(String url, String md5) throws Exception {
		System.out.println(url);
		File f = UiTool.createFilePath(md5);
		if (f.exists()) {
			return f;
		} else {
			byte[] bs = getBytes(new URL(url));
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(f);
				out.write(bs);
				out.flush();
			} finally {
				if (out != null)
					out.close();
			}
			return f;
		}
	}

	public static byte[] download(String url, String md5) throws Exception {
		File f = UiTool.createFilePath(md5);
		if (!f.exists()) {
			byte[] bs = getBytes(new URL(url));
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(f);
				out.write(bs);
				out.flush();
			} finally {
				if (out != null)
					out.close();
			}
			return bs;
		}
		return null;
	}

	public static byte[] getCacheBytes(String url) throws Exception {
		return getCacheBytes(url, null);
	}

	public static byte[] getCacheBytes(String url, String md5) throws Exception {
		if (md5 == null) {
			md5 = MD5.md5(url);
		}
		File f = UiTool.createFilePath(md5);
		if (f.exists()) {
			byte[] bs = new byte[(int) f.length()];
			FileInputStream in = null;
			try {
				in = new FileInputStream(f);
				in.read(bs);
			} finally {
				if (in != null)
					in.close();
			}
			return bs;
		} else {
			return download(url, md5);
		}
	}

	public static void getBitmap(final String url, final BitmapHandler handler) {
		new Thread() {
			public void run() {
				try {
					byte[] bs = HttpUtil.getCacheBytes(url);
					Bundle bundle = new Bundle();
					bundle.putByteArray("_BITMAP", bs);
					Message message = new Message();
					message.setData(bundle);
					handler.sendMessage(message);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public static void getBitmap(final String[] urls, final BitmapArrayHandler handler) {
		new Thread() {
			public void run() {
				try {
					for (String url : urls) {
						byte[] bs = HttpUtil.getCacheBytes(url);
						handler.add(bs);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
					handler.sendMessage(new Message());
				}
			}
		}.start();
	}

	public static String saveBitmap(Bitmap bitmap, String bitmapName) throws Exception {
		File f = UiTool.createFilePath(bitmapName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			out.flush();
		} finally {
			out.close();
		}
		return f.getPath();
	}

}
