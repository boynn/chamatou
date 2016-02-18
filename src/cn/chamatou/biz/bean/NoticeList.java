package cn.chamatou.biz.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.chamatou.biz.AppException;

/**
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class NoticeList extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int pageSize;
	//private int noticeCount;
	private List<Notice> noticelist = new ArrayList<Notice>();
	public int getPageSize() {
		return pageSize;
	}
	public List<Notice> getNoticeList() {
		return noticelist;
	}

	public static NoticeList parse(String json) throws IOException, AppException {
		NoticeList noticelist = new NoticeList();
		Notice notice = null;
		JSONObject ojson;
		try {
			System.out.println(json);
			ojson = new JSONObject(json);
			noticelist.pageSize =ojson.getInt("count");
			//noticelist.noticeCount =ojson.getInt("count");
			JSONArray noticeArray = ojson.getJSONArray("bulletin");
			for(int i = 0;i < noticeArray.length();i++){
				notice = new Notice();
				JSONObject noticeoj = (JSONObject) noticeArray.opt(i);
				notice.id=noticeoj.getInt("ID");
				notice.setSubject(noticeoj.getString("subject"));
				notice.setDetail(noticeoj.getString("detail"));
				notice.setEname(noticeoj.getString("ename"));		            	
            	noticelist.getNoticeList().add(notice); 
            	notice = null;
			}
    	
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	      
       return noticelist;       
	}
}
