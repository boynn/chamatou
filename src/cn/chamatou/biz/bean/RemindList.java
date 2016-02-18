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
public class RemindList extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int pageSize;
	//private int remindCount;
	private List<Remind> remindlist = new ArrayList<Remind>();
	public int getPageSize() {
		return pageSize;
	}
	public List<Remind> getRemindList() {
		return remindlist;
	}

	public static RemindList parse(String json) throws IOException, AppException {
		RemindList remindlist = new RemindList();
		Remind remind = null;
		JSONObject ojson;
		try {
			System.out.println(json);
			ojson = new JSONObject(json);
			remindlist.pageSize =ojson.getInt("count");
			//remindlist.remindCount =ojson.getInt("count");
			JSONArray remindArray = ojson.getJSONArray("remindevent");
			for(int i = 0;i < remindArray.length();i++){
				remind = new Remind();
				JSONObject remindoj = (JSONObject) remindArray.opt(i);
				remind.id=remindoj.getInt("ID");
				remind.setSubject(remindoj.getString("subject"));
				remind.setDescr(remindoj.getString("descr"));
				remind.setRmdtype(remindoj.getString("rmdtype"));		            	
				remind.setRmdate(remindoj.getString("rmdate"));		            	
            	remindlist.getRemindList().add(remind); 
            	remind = null;
			}
    	
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	      
       return remindlist;       
	}
}
