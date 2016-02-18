package cn.chamatou.biz.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.chamatou.biz.AppException;

/**
 * 搜索列表实体类
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class ContactSearchList extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	private int pageSize;
	private List<Contact> resultlist = new ArrayList<Contact>();
	
	public int getPageSize() {
		return pageSize;
	}
	public List<Contact> getContactlist() {
		return resultlist;
	}
	public void setContactlist(List<Contact> resultlist) {
		this.resultlist = resultlist;
	}
	
	public static ContactSearchList parse(String json) throws IOException, AppException {
		ContactSearchList contactsearchList = new ContactSearchList();
		Contact contact = null;
		JSONObject ojson;
		try {
			ojson = new JSONObject(json);
			contactsearchList.pageSize =ojson.getInt("count");
			//contactsearchList.goodsCount =ojson.getInt("count");
			JSONArray contactArray = ojson.getJSONArray("contact");
			for(int i = 0;i < contactArray.length();i ++){
				contact = new Contact();
				JSONObject contactoj = (JSONObject) contactArray.opt(i);
				contact.id=contactoj.getInt("ID");
				//goods.setGimg(goodsoj.getString("photos").split("nn:nn")[0]);
				contact.setCtname(contactoj.getString("ctname"));		            	
            	contact.setCname(contactoj.getString("cname"));		            	
            	contact.setCphone(contactoj.getString("cphone").replaceAll("^(01+)", "1"));			            	
            	contact.setCsex(contactoj.getString("csex"));			            	
            	contact.setPosition(contactoj.getString("position"));			            	
            	contactsearchList.getContactlist().add(contact); 
		        contact = null;
			}
	    	
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}    	      
	    return contactsearchList;       
	}
}
