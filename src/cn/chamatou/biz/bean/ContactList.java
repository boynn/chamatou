package cn.chamatou.biz.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.chamatou.biz.AppException;
import cn.chamatou.biz.common.StringUtils;

/**
 * 客户联系人列表实体类
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class ContactList extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int CATALOG_LASTEST = 0;
	public final static int CATALOG_HOT = -1;

	private int pageSize;
	private int itemCount;
	private List<Contact> contactlist = new ArrayList<Contact>();
	public int getPageSize() {
		return pageSize;
	}
	public int getItemCount() {
		return itemCount;
	}
	public List<Contact> getContactList() {
		return contactlist;
	}

	public static ContactList parse(String json) throws IOException, AppException {
		ContactList ctlist = new ContactList();
		Contact ct = null;
		JSONObject ojson;
		try {
			//System.out.print(json);
			ojson = new JSONObject(json);
			ctlist.pageSize =ojson.getInt("count");
			ctlist.itemCount =ojson.getInt("count");
			JSONArray contactArray = ojson.getJSONArray("contact");
			for(int i = 0;i < contactArray.length();i ++){
				ct = new Contact();
				JSONObject itemoj = (JSONObject) contactArray.opt(i);
				ct.id=itemoj.getInt("ID");
				ct.setClientId(itemoj.getInt("clientID"));
				ct.setCtname(itemoj.getString("ctname"));
				ct.setCname(itemoj.getString("cname"));		            	
				ct.setCphone(itemoj.getString("cphone").replaceAll("^(01+)", "1"));		            	
				ct.setPosition(itemoj.getString("position"));
				ct.setSpec(itemoj.getString("memo"));
				ct.setCsex(itemoj.getString("csex"));		            	
				ctlist.getContactList().add(ct); 
            	ct = null;
			}
    	
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	      
       return ctlist;       
	}
}
