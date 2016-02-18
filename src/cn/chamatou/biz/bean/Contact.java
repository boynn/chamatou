package cn.chamatou.biz.bean;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import cn.chamatou.biz.AppException;
import cn.chamatou.biz.common.StringUtils;

/**
 * 客户联系人实体类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class Contact extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String NODE_ID = "id";
	public final static String NODE_CTNAME = "ctname";
	public final static String NODE_CNAME = "cname";
	public final static String NODE_PHONE = "cphone";
	public final static String NODE_SEX = "csex";
	public final static String NODE_APPCLIENT = "appclient";
	public final static String NODE_START = "contact";
	
	private String ctname;
	private String cname;
	private String cphone;
	private int clientId;
	private String position;
	private String csex;
	private String spec;
	protected int id;

	public int getId() {
		return id;
	}

	
	public static Contact parse(String json) throws IOException, AppException {
		Contact ct = null;
        
        return ct;       
	}


	public String getCtname() {
		return ctname;
	}


	public void setCtname(String ctname) {
		this.ctname = ctname;
	}


	public String getCname() {
		return cname;
	}


	public void setCname(String cname) {
		this.cname = cname;
	}


	public String getCphone() {
		return cphone;
	}


	public void setCphone(String cphone) {
		this.cphone = cphone;
	}


	public int getClientId() {
		return clientId;
	}


	public void setClientId(int clientId) {
		this.clientId = clientId;
	}


	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}


	public String getCsex() {
		return csex;
	}


	public void setCsex(String csex) {
		this.csex = csex;
	}


	public String getSpec() {
		return spec;
	}


	public void setSpec(String spec) {
		this.spec = spec;
	}
}
