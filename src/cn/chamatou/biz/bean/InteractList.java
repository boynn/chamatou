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
 * 客户联系记录列表实体类
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class InteractList extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int pageSize;
	private int itemCount;
	private List<Interact> interactlist = new ArrayList<Interact>();
	public int getPageSize() {
		return pageSize;
	}
	public int getItemCount() {
		return itemCount;
	}
	public List<Interact> getInteractList() {
		return interactlist;
	}

	public static InteractList parse(String json) throws IOException, AppException {
		InteractList itlist = new InteractList();
		Interact it = null;
		JSONObject ojson;
		try {
			ojson = new JSONObject(json);
			itlist.pageSize =ojson.getInt("count");
			itlist.itemCount =ojson.getInt("count");
			JSONArray interactArray = ojson.getJSONArray("interact");
			for(int i = 0;i < interactArray.length();i ++){
				it = new Interact();
				JSONObject porderitemoj = (JSONObject) interactArray.opt(i);
				it.id=porderitemoj.getInt("ID");
				it.setCname(porderitemoj.getString("cname"));		            	
				it.setActDate(porderitemoj.getString("actDate"));		            	
				it.setDetail(porderitemoj.getString("detail"));
				itlist.getInteractList().add(it); 
            	it = null;
			}
    	
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	      
       return itlist;       
	}
}
