package cn.chamatou.biz.bean;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.chamatou.biz.AppException;

/**
 * 通知公告实体类
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class Notice extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String NODE_ID = "id";
	public final static String NODE_SUBJECT = "subject";
	public final static String NODE_DETAIL = "detail";
	public final static String NODE_ENAME = "ename";
	public final static String NODE_START = "bulletin";
	
	private String subject;
	private String detail;
	private String cdate;
	private String ename;
	protected int id;

	public int getId() {
		return id;
	}

	
	public static Notice parse(String json) throws IOException, AppException {
		Notice notice = null;
		JSONObject ojson;
		try {
			ojson = new JSONObject(json);
			JSONArray noticeArray = ojson.getJSONArray("bulletin");
			notice = new Notice();
			JSONObject noticeoj = (JSONObject) noticeArray.opt(0);
			notice.id=noticeoj.getInt("ID");
			notice.setSubject(noticeoj.getString("subject"));			 
			notice.setDetail(noticeoj.getString("detail"));
			notice.setCdate(noticeoj.getString("createdate"));
			notice.setEname(noticeoj.getString("ename"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	      
       return notice;          
	}


	public String getDetail() {
		return detail;
	}


	public void setDetail(String detail) {
		this.detail = detail;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getEname() {
		return ename;
	}


	public void setEname(String ename) {
		this.ename = ename;
	}


	public String getCdate() {
		return cdate;
	}


	public void setCdate(String cdate) {
		this.cdate = cdate;
	}


}
