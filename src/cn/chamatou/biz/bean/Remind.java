package cn.chamatou.biz.bean;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.chamatou.biz.AppException;

/**
 * 提醒事务实体类
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class Remind extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String NODE_ID = "id";
	public final static String NODE_SUBJECT = "subject";
	public final static String NODE_DESCR = "descr";
	public final static String NODE_START = "remindevent";
	
	private String subject;
	private String descr;
	private String rmdtype;
	private String rmdate;
	private String rtype="1";
	protected int id;

	public int getId() {
		return id;
	}

	
	public static Remind parse(String json) throws IOException, AppException {
		Remind remind = null;
		JSONObject ojson;
		try {
			ojson = new JSONObject(json);
			JSONArray remindArray = ojson.getJSONArray("remindevent");
			remind = new Remind();
			JSONObject remindoj = (JSONObject) remindArray.opt(0);
			remind.id=remindoj.getInt("ID");
			remind.setSubject(remindoj.getString("subject"));			 
			remind.setDescr(remindoj.getString("descr"));
			remind.setRmdtype(remindoj.getString("rmdtype"));
			remind.setRmdate(remindoj.getString("rmdate"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	      
       return remind;          
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getDescr() {
		return descr;
	}


	public void setDescr(String descr) {
		this.descr = descr;
	}


	public String getRmdtype() {
		return rmdtype;
	}


	public void setRmdtype(String rmdtype) {
		this.rmdtype = rmdtype;
	}

	public String getRmdate() {
		return rmdate;
	}


	public void setRmdate(String rmdate) {
		this.rmdate = rmdate;
	}


	public String getRtype() {
		return rtype;
	}


	public void setRtype(String rtype) {
		this.rtype = rtype;
	}


}
