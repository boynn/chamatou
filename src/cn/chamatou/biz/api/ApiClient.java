package cn.chamatou.biz.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ByteArrayBody;
import com.lidroid.xutils.http.client.multipart.content.FileBody;

import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.AppException;
import cn.chamatou.biz.R;
import cn.chamatou.biz.async.ExceptionHandler;
import cn.chamatou.biz.async.Result;
import cn.chamatou.biz.bean.Article;
import cn.chamatou.biz.bean.ContactSearchList;
import cn.chamatou.biz.bean.Expert;
import cn.chamatou.biz.bean.Goods;
import cn.chamatou.biz.bean.GoodsList;
import cn.chamatou.biz.bean.Merchant;
import cn.chamatou.biz.bean.MyInfo;
import cn.chamatou.biz.bean.Notice;
import cn.chamatou.biz.bean.NoticeList;
import cn.chamatou.biz.bean.Option;
import cn.chamatou.biz.bean.Order;
import cn.chamatou.biz.bean.OrderList;
import cn.chamatou.biz.bean.Remind;
import cn.chamatou.biz.bean.RemindList;
import cn.chamatou.biz.bean.Store;
import cn.chamatou.biz.bean.URLs;
import cn.chamatou.biz.bean.Update;
import cn.chamatou.biz.bean.User;
import cn.chamatou.biz.common.StringUtils;

/**
 * API客户端接口：用于访问网络数据
 * 
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class ApiClient {

	public static final String UTF_8 = "UTF-8";
	public static final String DESC = "descend";
	public static final String ASC = "ascend";

	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;

	private static String appCookie;
	private static String appUserAgent;

	public static void cleanCookie() {
		appCookie = "";
	}

	public static String getCookie(AppContext appContext) {
		if (appCookie == null || appCookie == "") {
			appCookie = appContext.getProperty("cookie");
		}
		return appCookie;
	}

	private static String getUserAgent(AppContext appContext) {
		if (appUserAgent == null || appUserAgent == "") {
			StringBuilder ua = new StringBuilder("store.BOYNN");
			ua.append('/' + appContext.getPackageInfo().versionName + '_'
					+ appContext.getPackageInfo().versionCode);// App版本
			ua.append("/Android");// 手机系统平台
			ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
			ua.append("/" + android.os.Build.MODEL); // 手机型号
			ua.append("/" + appContext.getAppId());// 客户端唯一标识
			appUserAgent = ua.toString();
		}
		return appUserAgent;
	}

	private static HttpClient getHttpClient() {
		HttpClient httpClient = new HttpClient();
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		httpClient.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
		// 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}

	private static GetMethod getHttpGet(String url, String cookie,
			String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Host", URLs.HOST);
		httpGet.setRequestHeader("Connection", "Keep-Alive");
		httpGet.setRequestHeader("Cookie", cookie);
		httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}

	private static PostMethod getHttpPost(String url, String cookie,
			String userAgent) {
		PostMethod httpPost = new PostMethod(url);
		// 设置 请求超时时间
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Host", URLs.HOST);
		httpPost.setRequestHeader("Connection", "Keep-Alive");
		httpPost.setRequestHeader("Cookie", cookie);
		httpPost.setRequestHeader("User-Agent", userAgent);
		return httpPost;
	}

	private static String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0)
			url.append('?');

		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			// 不做URLEncoder处理
			//url.append(String.valueOf(params.get(name)));
			// 做URLEncoder处理
			try {
				url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return url.toString().replace("?&", "?");
	}

	/**
	 * get请求URL
	 * 
	 * @param url
	 * @throws AppException
	 */
	private static String http_get(AppContext appContext, String url)
			throws AppException {
		System.out.println("get_url==> " + url);
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;
		GetMethod httpGet = null;

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, cookie, userAgent);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
				responseBody = httpGet.getResponseBodyAsString();
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		
		return StringUtils.removeBOM(responseBody);
	}
	private static String post( String url,
			Map<String, Object> params, Map<String, File> files)
			throws AppException {
		
		HttpClient httpClient = null;
		PostMethod httpPost = null;
		// post表单参数处理
		int length = (params == null ? 0 : params.size())
				+ (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null)
			for (String name : params.keySet()) {
				parts[i++] = new StringPart(name, String.valueOf(params
						.get(name)), UTF_8);
			}

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, null, null);
				httpPost.addParameters(buildNameValuePair(params));
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
				InputStream inputStream = httpPost.getResponseBodyAsStream();   
		         BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));   
		         StringBuffer stringBuffer = new StringBuffer();   
		         String str= "";   
		         while((str = br.readLine()) != null){   
		             stringBuffer .append(str );   
		         }   
		         responseBody= stringBuffer.toString();
		    	break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		return StringUtils.removeBOM(responseBody);
	}
	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	private static String _post(AppContext appContext, String url,
			Map<String, Object> params, Map<String, File> files)
			throws AppException {
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;
		PostMethod httpPost = null;
		// post表单参数处理
		int length = (params == null ? 0 : params.size())
				+ (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null)
			for (String name : params.keySet()) {
				parts[i++] = new StringPart(name, String.valueOf(params
						.get(name)), UTF_8);
			}

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, cookie, userAgent);
				httpPost.addParameters(buildNameValuePair(params));
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				} else if (statusCode == HttpStatus.SC_OK) {
					Cookie[] cookies = httpClient.getState().getCookies();
					String tmpcookies = "";
					for (Cookie ck : cookies) {
						tmpcookies += ck.toString() + ";";
					}
					// 保存cookie
					if (appContext != null && tmpcookies != "") {
						appContext.setProperty("cookie", tmpcookies);
						appCookie = tmpcookies;
					}
				}
				InputStream inputStream = httpPost.getResponseBodyAsStream();   
		         BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));   
		         StringBuffer stringBuffer = new StringBuffer();   
		         String str= "";   
		         while((str = br.readLine()) != null){   
		             stringBuffer .append(str );   
		         }   
		         responseBody= stringBuffer.toString();
		    	break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		return StringUtils.removeBOM(responseBody);
	}

	private static String filePost(AppContext appContext, String url, File file)
			throws Exception {
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);
		PostMethod post = getHttpPost(url, cookie, userAgent);

		String responseBody = "";
		post.getParams().setBooleanParameter(
				HttpMethodParams.USE_EXPECT_CONTINUE, true);
		Part[] parts = { new FilePart(file.getName(), file) };
		post.setRequestEntity(new MultipartRequestEntity(parts,
				(HttpMethodParams) post.getParams()));
		HttpClient client = new HttpClient();
		int time = 0;
		do {
			try {
				int statusCode = client.executeMethod(post);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				} else if (statusCode == HttpStatus.SC_OK) {
					Cookie[] cookies = client.getState().getCookies();
					String tmpcookies = "";
					for (Cookie ck : cookies) {
						tmpcookies += ck.toString() + ";";
					}
					// 保存cookie
					if (appContext != null && tmpcookies != "") {
						appContext.setProperty("cookie", tmpcookies);
						appCookie = tmpcookies;
					}
				}
				InputStream inputStream = post.getResponseBodyAsStream();   
		         BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));   
		         StringBuffer stringBuffer = new StringBuffer();   
		         String str= "";   
		         while((str = br.readLine()) != null){   
		             stringBuffer .append(str );   
		         }   
		         responseBody= stringBuffer.toString();
		    	break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				post.releaseConnection();
				client = null;
			}
		} while (time < RETRY_TIME);

		responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		return StringUtils.removeBOM(responseBody);

	}

	@SuppressLint("UseValueOf")
	private static String httpPost(AppContext appContext, String url,
			Map<String, Object> params) throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod httpPost = new PostMethod(url);
		// Post方式我们需要配置参数
		httpPost.addParameter("Connection", "Keep-Alive");
		httpPost.addParameter("Charset", "UTF-8");
		httpPost.addParameter("Content-Type",
				"application/x-www-form-urlencoded");
		httpPost.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,
				new Integer(TIMEOUT_SOCKET));
		if (null != params && params.size() != 0) {
			// 设置需要传递的参数，NameValuePair[]
			httpPost.setRequestBody(buildNameValuePair(params));
			
		}
		String responseBody = "";
		int time = 0;
		do {
			try {
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				} else if (statusCode == HttpStatus.SC_OK) {
					Cookie[] cookies = httpClient.getState().getCookies();
					String tmpcookies = "";
					for (Cookie ck : cookies) {
						tmpcookies += ck.toString() + ";";
					}
					// 保存cookie
					if (appContext != null && tmpcookies != "") {
						appContext.setProperty("cookie", tmpcookies);
						appCookie = tmpcookies;
					}
				}
				InputStream inputStream = httpPost.getResponseBodyAsStream();   
		         BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));   
		         StringBuffer stringBuffer = new StringBuffer();   
		         String str= "";   
		         while((str = br.readLine()) != null){   
		             stringBuffer .append(str );   
		         }   
		         responseBody= stringBuffer.toString();
		    	System.out.println("JSON=>" + responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		return StringUtils.removeBOM(responseBody);
	}

	private static NameValuePair[] buildNameValuePair(Map<String, Object> params) {
		int i = 0;
		NameValuePair[] pair = new NameValuePair[params.size()];
		if (params != null)
			for (String name : params.keySet()) {
				pair[i++] = new NameValuePair(name, String.valueOf(params
						.get(name)));	
				System.out.println(name + " = " + String.valueOf(params.get(name)));
			}
		return pair;
	}

	/**
	 * 获取网络图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getNetBitmap(AppContext appContext, String url)
			throws AppException {
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);
		Bitmap bitmap = null;
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(URLs.URL_ADDR + url, cookie, userAgent);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
				InputStream inStream = httpGet.getResponseBodyAsStream();
				// System.out.println("iix===>"+httpGet.getResponseBodyAsString());
				bitmap = BitmapFactory.decodeStream(inStream);
				inStream.close();
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return bitmap;
	}

	/**
	 * 检查版本更新
	 * 
	 * @param url
	 * @return
	 */
	public static Update checkVersion(AppContext appContext)
			throws AppException {
		try {
			return Update.parse(http_get(appContext, URLs.UPDATE_VERSION));
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	/**
	 * 登录， 自动处理cookie
	 * 
	 * @param url
	 * @param username
	 * @param pwd
	 * @return
	 * @throws AppException
	 */
	public static User login(AppContext appContext, String username, String pwd)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("password", StringUtils.md5(pwd));
		params.put("keep_login", 1);

		String loginurl = URLs.LOGIN_VALIDATE_HTTP;
		
		try {
			return User.parse(httpPost(appContext, loginurl, params));
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	/**
	 * 登录， 自动处理cookie
	 * 
	 * @param url
	 * @param username
	 * @param pwd
	 * @return
	 * @throws AppException
	 * @throws IOException
	 */
	public static String uploadFile(AppContext appContext, File file)
			throws AppException {

		String uploadurl = URLs.FILE_UPLOAD;
		try {
			return filePost(appContext, uploadurl, file);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}


	/**
	 * 新商品
	 * @param communityId 
	 * 
	 * @param client
	 *            （uid、title、catalog、content、isNoticeMe）
	 * @param loginUid
	 * @return
	 * @throws AppException
	 */
	public static String storeReg(AppContext appContext, String name,String tel,String pwd,String vcode) throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name",name);		
		params.put("tel",tel);
		params.put("pwd",StringUtils.md5(pwd));
		params.put("vcode", vcode);
		//params.put("communityId",communityId);
		try {
			return _post(appContext, URLs.STORE_REG, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	
	public static String saveGoods(AppContext appContext, Goods goods,
			int loginUid) throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("merchant_id",loginUid);		
		params.put("state", goods.getState());
		params.put("discount", goods.getDiscount());
		params.put("goodsid",goods.getGoodsid());		
		params.put("gname", goods.getGoods_name());
		params.put("unit", goods.getUnit());
		params.put("price", goods.getPrice());
		params.put("pic", goods.getDefault_image());
		params.put("descr", goods.getDescription());
		try {
			return _post(appContext, URLs.NEW_GOODS, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	public static String saveStore(AppContext appContext, Store store,
			int loginUid) throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("memberId",loginUid);		
		params.put("name", store.getStore_name());
		params.put("addr", store.getStore_addr());
		params.put("title", store.getTitle());
		params.put("head", store.getHead());
		params.put("descr", store.getDescription());
		try {
			return _post(appContext, URLs.SAVE_STORE, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	public static String saveMerchant(AppContext appContext, Merchant store,
			int loginUid) throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("memberId",loginUid);		
		params.put("name", store.getMerchant_name());
		params.put("addr", store.getMerchant_addr());
		params.put("title", store.getTitle());
		params.put("head", store.getHead());
		params.put("descr", store.getDescription());
		try {
			return _post(appContext, URLs.SAVE_MERCHANT, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	public static String saveExpert(AppContext appContext, Expert expert,
			int loginUid) throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("memberId",loginUid);		
		params.put("type", expert.getType());
		params.put("name", expert.getName());
		params.put("addr", expert.getAddress());
		params.put("title", expert.getTitle());
		params.put("head", expert.getHead());
		params.put("descr", expert.getDescription());
		try {
			return _post(appContext, URLs.SAVE_EXPERT, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	
	public static String saveArticle(AppContext appContext, Article article,
			int loginUid) throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("memberId",loginUid);		
		params.put("type", article.getAtype());
		params.put("title", article.getTitle());
		params.put("content", article.getContent());
		params.put("img", article.getDefault_image());
		try {
			return _post(appContext, URLs.SAVE_ARTICLE, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	/**
	 * 获取商品列表
	 * 
	 * @param uid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws AppException
	 */
	public static GoodsList getGoodsList(AppContext appContext,
			final int uid,final int page, final int pageSize)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("merchant_id", uid);
		params.put("page", page);

		String goodsurl = URLs.GOODS_LIST;
		
		try {
			return GoodsList.parse(httpPost(appContext, goodsurl, params));
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	public static OrderList getOrderList(AppContext appContext,
			final int uid,final int minID, final int tday)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("merchant_id", uid);
		params.put("minID", minID);
		params.put("tday", tday);

		String orderurl = URLs.ORDER_LIST;
		
		try {
			return OrderList.parse(httpPost(appContext, orderurl, params));
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	/**
	 * 获取客户详情
	 * 
	 * @param dealer_id
	 * @return
	 * @throws AppException
	 */
	public static Store getStoreDetail(AppContext appContext, final int store_id)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", ""+store_id);
		try {
			Type type = new TypeToken<List<Order>>() {
			}.getType();
			
			List<Store> ms = createGson().fromJson(httpPost(appContext, URLs.STORE_DETAIL, params), type);
			return ms.get(0);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	/**
	 * 获取订单详情
	 * 
	 * @param vehicle_id
	 * @return
	 * @throws AppException
	 */
	public static Order getOrderDetail(AppContext appContext, final int order_id)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", ""+order_id);
		try {
			Type type = new TypeToken<List<Order>>() {
			}.getType();
			
			List<Order> ms = createGson().fromJson(httpPost(appContext, URLs.ORDER_DETAIL, params), type);
			return ms.get(0);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	public static String getOrderGoods(AppContext appContext, String order_id)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("order_no", ""+order_id);
		 	return httpPost(appContext, URLs.ORDER_GOODS, params);
	}
	public static String shipOrder(AppContext appContext, int order_id, final int user_id)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("order_id", ""+order_id);
		params.put("merchant_id", user_id);
		 	return httpPost(appContext, URLs.ORDER_SHIP, params);
	}
	public static List<Option> getAtype(AppContext appContext)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			Type type = new TypeToken<List<Option>>() {
			}.getType();
			
			List<Option> ls = createGson().fromJson(httpPost(appContext, URLs.QUERY_ATYPE, params), type);
			return ls;
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	public static List<Option> getMtype(AppContext appContext)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			Type type = new TypeToken<List<Option>>() {
			}.getType();
			
			List<Option> ls = createGson().fromJson(httpPost(appContext, URLs.QUERY_MTYPE, params), type);
			return ls;
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	
	public static List<Option> getStype(AppContext appContext,String mType)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mtype", mType);
		try {
			Type type = new TypeToken<List<Option>>() {
			}.getType();
			
			List<Option> ls = createGson().fromJson(httpPost(appContext, URLs.QUERY_STYPE, params), type);
			return ls;
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	/**
	 * 获取商家信息
	 * 
	 * @param myinfo_id
	 * @return
	 * @throws AppException
	 */
	public static Store getMyStore(AppContext appContext, final int user_id)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("memberId", user_id);
		try {
			Type type = new TypeToken<List<Store>>() {
			}.getType();
			
			List<Store> ms = createGson().fromJson(httpPost(appContext, URLs.MYSTORE, params), type);
			return ms.get(0);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	/**
	 * 获取商家信息
	 * 
	 * @param myinfo_id
	 * @return
	 * @throws AppException
	 */
	public static Merchant getMyMerchant(AppContext appContext, final int user_id)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("memberId", user_id);
		try {
			Type type = new TypeToken<List<Merchant>>() {
			}.getType();
			
			List<Merchant> ms = createGson().fromJson(httpPost(appContext, URLs.MYMERCHANT, params), type);
			return ms.get(0);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	/**
	 * 获取商家信息
	 * 
	 * @param myinfo_id
	 * @return
	 * @throws AppException
	 */
	public static Expert getMyExpert(AppContext appContext, final int user_id)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("memberId", user_id);
		try {
			Type type = new TypeToken<List<Expert>>() {
			}.getType();
			
			List<Expert> ms = createGson().fromJson(httpPost(appContext, URLs.MYEXPERT, params), type);
			return ms.get(0);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	/**
	 * 获取用户信息
	 * 
	 * @param myinfo_id
	 * @return
	 * @throws AppException
	 */
	public static MyInfo getMyInfo(AppContext appContext, final int user_id)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("merchant_id", user_id);
		try {
			return null;//MyInfo.parse(httpPost(appContext, URLs.MYINFO, params));
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	
	public static RemindList getRemindList(AppContext appContext,
			int uid)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("where", "datediff(day,rmdate,getdate())>=0 and state=1 and owner="+uid);
		params.put("pageIndex", 0);
		params.put("pageSize", 100);

		try {
			return RemindList.parse(_post(appContext,
					URLs.REMIND_LIST, params, null));
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	/**
	 * 获取系统序列号
	 * 
	 * @param gid
	 *            商品uid
	 * @param dealerid
	 *            代理商id
	 * @return
	 * @throws AppException
	 */
	public static String getSeqNo(AppContext appContext, String sname)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sname", sname);
		// params.put("dealerid", dealerid);
		try {
			return "";//_post(appContext, URLs.GETSEQ, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	public static NoticeList getNoticeList(AppContext appContext, final int pageIndex, final int pageSize) throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		//Date now = new Date();
		params.put("where", "validdate>getdate()");
		params.put("pageIndex", pageIndex);
		params.put("pageSize", pageSize);

		

		try {
			return NoticeList.parse(_post(appContext,
					URLs.NOTICE_LIST, params, null));
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	public static Notice getNoticeDetail(AppContext appContext, int notice_id) throws AppException {
		// TODO 自动生成的方法存根
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("where", "ID=" + notice_id);
		try {
			return Notice.parse(_post(appContext, URLs.NOTICE_LIST, params,
					null));
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	public static Remind getRemindDetail(AppContext appContext, int remind_id) throws AppException {
		// TODO 自动生成的方法存根
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("where", "ID=" + remind_id);
		try {
			return Remind.parse(_post(appContext, URLs.REMIND_LIST, params,
					null));
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	public static String delGoods(AppContext appContext, int goods_id,
			int loginUid) throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("goodsId", "" + goods_id);
		params.put("userId", ""+loginUid);
		try {
			return _post(appContext, URLs.DELGOODS, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	public static Result<JSONArray> searchCommunity(String q){
		Result<JSONArray> result = new Result<JSONArray>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", q);
		try {
				String jObj = post(URLs.SEARCH_COMM, params, null);
				result.setData(new JSONArray(jObj));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(AppContext.self.getString(R.string.unknown_error));
			result.setThrowable(e);
			ExceptionHandler.handleException(AppContext.self, result);
		}

		return result;

	}
	public static String changPwd(AppContext appContext, String opwd,String npwd,int userId) throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("opwd",StringUtils.md5(opwd));		
		params.put("npwd",StringUtils.md5(npwd));
		params.put("userId",userId);
		try {
			return _post(appContext, URLs.CHG_PWD, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	public static String upload(File file) {
		String rst="";
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(URLs.UPLOAD_URL);

			MultipartEntity m = new MultipartEntity();
			m.addPart("file", new FileBody(file));
			httpPost.setEntity(m);
			HttpResponse response = httpclient.execute(httpPost);
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode == HttpStatus.SC_OK) {
				//result.setError(Result.OK);
				rst = EntityUtils
						.toString(response.getEntity(), "UTF-8");
				//result.setData(rst);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rst;
	}

	public static String upload(byte[] data) {
		String rst="";
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(URLs.UPLOAD_URL);

			MultipartEntity m = new MultipartEntity();
			m.addPart("file", new ByteArrayBody(data, UUID.randomUUID()
					.toString().substring(0, 5)));
			httpPost.setEntity(m);

			HttpResponse response = httpclient.execute(httpPost);
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode == HttpStatus.SC_OK) {
				rst = EntityUtils
						.toString(response.getEntity(), "UTF-8");				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rst;
	}

	private static Gson createGson() {
		// return new GsonBuilder().registerTypeHierarchyAdapter(String.class,
		// new DateAdapter()).create();
		GsonBuilder b = new GsonBuilder();
		b.setDateFormat("yyyy-MM-dd HH:mm:ss");
		return b.create();
	}

}
