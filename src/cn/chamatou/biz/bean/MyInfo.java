package cn.chamatou.biz.bean;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import cn.chamatou.biz.AppException;
import cn.chamatou.biz.common.StringUtils;

/**
 * 会员实体类
 * @gnumryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class MyInfo extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String head;
	//private String community;
	private String title;
	private String stype;
	private String goodsnum;
	private String ordernum;
	


	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getHead() {
		return head;
	}



	public void setHead(String head) {
		this.head = head;
	}



	public String getStype() {
		return stype;
	}



	public void setStype(String stype) {
		this.stype = stype;
	}



	public String getGoodsnum() {
		return goodsnum;
	}



	public void setGoodsnum(String goodsnum) {
		this.goodsnum = goodsnum;
	}



	public String getOrdernum() {
		return ordernum;
	}



	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}


	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}

	
}
