package cn.chamatou.biz.bean;

import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.os.Environment;

import cn.chamatou.biz.common.StringUtils;

/**
 * 接口URL实体类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class URLs implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String HOST = "118.123.213.252:8889";
	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";
	
	private final static String URL_SPLITTER = "/Mobile/";
	
	public final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER;
	public final static String LOGIN_VALIDATE_HTTP = URL_API_HOST + "Store/login";
	public final static String LOGIN_VALIDATE_HTTPS = HTTPS + HOST + URL_SPLITTER + "Store/login";
	public final static String URL_ADDR = HTTP + HOST;
	public final static String FILE_UPLOAD = URL_API_HOST+"UploadServlet?ftype=4";
	public final static String STORE_REG = URL_API_HOST+"Store/reg";
	public final static String CHG_PWD = URL_API_HOST+"Store/changPwd";
	public final static String NEW_GOODS = URL_API_HOST+"Store/saveGoods";
	public final static String SAVE_MERCHANT = URL_API_HOST+"Store/saveMerchant";
	public final static String SAVE_STORE = URL_API_HOST+"Store/saveStore";
	public final static String SAVE_EXPERT = URL_API_HOST+"Store/saveExpert";
	public final static String SAVE_ARTICLE = URL_API_HOST+"Store/saveArticle";
	public final static String STORE_DETAIL = URL_API_HOST+"Store/getStoreDetail";
	public final static String INTERACT_LIST = URL_API_HOST+"GetJsonList?module=interact";
	public final static String GOODS_LIST = URL_API_HOST+"Store/getGoods";
	public final static String GOODS_DETAIL = URL_API_HOST+"GetJsonList?module=client";
	public final static String ORDER_LIST = URL_API_HOST+"Store/getOrder";
	public final static String ORDER_DETAIL = URL_API_HOST+"Store/getOrderDetail";
	public final static String ORDER_GOODS = URL_API_HOST+"Store/getOrderGoods";
	public final static String ORDER_SHIP = URL_API_HOST+"Store/shipOrder";
	public final static String QUERY_ATYPE = URL_API_HOST+"Store/getAtype";
	public final static String QUERY_MTYPE = URL_API_HOST+"Store/getMtype";
	public final static String QUERY_STYPE = URL_API_HOST+"Store/getStype";
	public final static String MYINFO = URL_API_HOST+"Store/storeinfo";	
	public final static String MYSTORE = URL_API_HOST+"Store/getStore";	
	public final static String MYMERCHANT = URL_API_HOST+"Store/getMerchant";	
	public final static String MYEXPERT = URL_API_HOST+"Store/getExpert";	
	public final static String SEARCH_LIST = URL_API_HOST+"GetJsonList?module=";
	public final static String DEPTSEARCH_LIST = URL_API_HOST+"GetJsonList?module=sdept";
	public final static String CLIENTSEARCH_LIST = URL_API_HOST+"GetJsonList?module=client";
	public final static String CONTACTSEARCH_LIST = URL_API_HOST+"GetJsonList?module=contact";
	public final static String NOTICE_LIST = URL_API_HOST+"GetJsonList?module=bulletin";
	public final static String REMIND_LIST = URL_API_HOST+"GetJsonList?module=remindevent";
	public final static String UPDATE_VERSION = URL_API_HOST+"Store/getUpdateInfo";
	public final static String UPLOAD_URL = HTTP + HOST+"/upload.php";
	public final static String SEARCH_COMM = URL_API_HOST+"Community/getCommunityByName";
	public final static String DELGOODS = URL_API_HOST+"Store/delGoods";
	
	private final static String URL_HOST = "www.store.cn";
	private final static String URL_WWW_HOST = "www."+URL_HOST;
	private final static String URL_MY_HOST = "my."+URL_HOST;
	
	public final static String DIR_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/community/";

	private final static String URL_TYPE_SOFTWARE = URL_WWW_HOST + URL_SPLITTER + "p" + URL_SPLITTER;
	private final static String URL_TYPE_GOODS = URL_SPLITTER + "goods" + URL_SPLITTER;
	private final static String URL_TYPE_ZONE = URL_MY_HOST + URL_SPLITTER + "u" + URL_SPLITTER;
	
	public final static int URL_OBJ_TYPE_OTHER = 0x000;
	public final static int URL_OBJ_TYPE_NEWS = 0x001;
	public final static int URL_OBJ_TYPE_SOFTWARE = 0x002;
	public final static int URL_OBJ_TYPE_QUESTION = 0x003;
	public final static int URL_OBJ_TYPE_ZONE = 0x004;
	public final static int URL_OBJ_TYPE_BLOG = 0x005;
	public final static int URL_OBJ_TYPE_GOODS = 0x006;
	public final static int URL_OBJ_TYPE_QUESTION_TAG = 0x007;
	
	private int objId;
	private String objKey = "";
	private int objType;
	
	public int getObjId() {
		return objId;
	}
	public void setObjId(int objId) {
		this.objId = objId;
	}
	public String getObjKey() {
		return objKey;
	}
	public void setObjKey(String objKey) {
		this.objKey = objKey;
	}
	public int getObjType() {
		return objType;
	}
	public void setObjType(int objType) {
		this.objType = objType;
	}
	
	/**
	 * 转化URL为URLs实体
	 * @param path
	 * @return 不能转化的链接返回null
	 */
	public final static URLs parseURL(String path) {
		if(StringUtils.isEmpty(path))return null;
		path = formatURL(path);
		URLs urls = null;
		String objId = "";
		try {
			URL url = new URL(path);
			if(url.getHost().contains(URL_HOST)){
				urls = new URLs();
				if(path.contains(URL_WWW_HOST )){
					if(path.contains(URL_TYPE_GOODS)){
						objId = parseObjId(path, URL_TYPE_GOODS);
						urls.setObjId(StringUtils.toInt(objId));
						urls.setObjType(URL_OBJ_TYPE_NEWS);
					}
					else if(path.contains(URL_TYPE_SOFTWARE)){
						urls.setObjKey(parseObjKey(path, URL_TYPE_SOFTWARE));
						urls.setObjType(URL_OBJ_TYPE_SOFTWARE);
					}
					else{
						urls.setObjKey(path);
						urls.setObjType(URL_OBJ_TYPE_OTHER);
					}
				}
				else if(path.contains(URL_MY_HOST)){					
					if(path.contains(URL_TYPE_GOODS)){
						objId = parseObjId(path, URL_TYPE_GOODS);
						urls.setObjId(StringUtils.toInt(objId));
						urls.setObjType(URL_OBJ_TYPE_GOODS);
					}
					else if(path.contains(URL_TYPE_ZONE)){
						objId = parseObjId(path, URL_TYPE_ZONE);
						urls.setObjId(StringUtils.toInt(objId));
						urls.setObjType(URL_OBJ_TYPE_ZONE);
					}
					else{
						int p = path.indexOf(URL_MY_HOST+URL_SPLITTER) + (URL_MY_HOST+URL_SPLITTER).length();
						String str = path.substring(p);
						if(!str.contains(URL_SPLITTER)){
							urls.setObjKey(str);
							urls.setObjType(URL_OBJ_TYPE_ZONE);
						}
						else{
							urls.setObjKey(path);
							urls.setObjType(URL_OBJ_TYPE_OTHER);
						}
					}
				}
				//other
				else{
					urls.setObjKey(path);
					urls.setObjType(URL_OBJ_TYPE_OTHER);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			urls = null;
		}
		return urls;
	}

	/**
	 * 解析url获得objId
	 * @param path
	 * @param url_type
	 * @return
	 */
	private final static String parseObjId(String path, String url_type){
		String objId = "";
		int p = 0;
		String str = "";
		String[] tmp = null;
		p = path.indexOf(url_type) + url_type.length();
		str = path.substring(p);
		if(str.contains(URL_SPLITTER)){
			tmp = str.split(URL_SPLITTER);
			objId = tmp[0];
		}else{
			objId = str;
		}
		return objId;
	}
	
	/**
	 * 解析url获得objKey
	 * @param path
	 * @param url_type
	 * @return
	 */
	private final static String parseObjKey(String path, String url_type){
		path = URLDecoder.decode(path);
		String objKey = "";
		int p = 0;
		String str = "";
		String[] tmp = null;
		p = path.indexOf(url_type) + url_type.length();
		str = path.substring(p);
		if(str.contains("?")){
			tmp = str.split("?");
			objKey = tmp[0];
		}else{
			objKey = str;
		}
		return objKey;
	}
	
	/**
	 * 对URL进行格式处理
	 * @param path
	 * @return
	 */
	private final static String formatURL(String path) {
		if(path.startsWith("http://") || path.startsWith("https://"))
			return path;
		return "http://" + URLEncoder.encode(path);
	}	
}
