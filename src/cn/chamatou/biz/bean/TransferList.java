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
 * 转账列表实体类
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class TransferList extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int CATALOG_LASTEST = 0;
	public final static int CATALOG_HOT = -1;

	private int pageSize;
	private int transferCount;
	private List<Transfer> transferlist = new ArrayList<Transfer>();
	public int getPageSize() {
		return pageSize;
	}
	public int getTransferCount() {
		return transferCount;
	}
	public List<Transfer> getTransferlist() {
		return transferlist;
	}

	public static TransferList parse(String json) throws IOException, AppException {
		TransferList transferlist = new TransferList();
		Transfer transfer = null;
		JSONObject ojson;
		try {
			ojson = new JSONObject(json);
			transferlist.pageSize =ojson.getInt("count");
			transferlist.transferCount =ojson.getInt("count");
			JSONArray transferArray = ojson.getJSONArray("actrans");
			for(int i = 0;i < transferArray.length();i++){
				transfer = new Transfer();
				JSONObject transferoj = (JSONObject) transferArray.opt(i);
				transfer.id=transferoj.getInt("ID");
				//transfer.setGimg(transferoj.getString("photos").split("nn:nn")[0]);
				transfer.setEaccount(transferoj.getString("eaccount"));		            	
            	transfer.setInaccount(transferoj.getString("inaccount"));		            	
            	transfer.setAcname(transferoj.getString("acname"));			            	
            	transfer.setAmount(transferoj.getString("amount"));			            	
            	transfer.setTranstime(StringUtils.friendly_time(transferoj.getString("transtime")));	
            	transfer.setDescr(transferoj.getString("descr"));			            	
            	transferlist.getTransferlist().add(transfer); 
		        transfer = null;
			}
    	
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	      
       return transferlist;       
	}
}
