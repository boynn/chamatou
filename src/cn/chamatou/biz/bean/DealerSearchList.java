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
public class DealerSearchList extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	private int pageSize;
	private List<Dealer> resultlist = new ArrayList<Dealer>();
	
	public int getPageSize() {
		return pageSize;
	}
	public List<Dealer> getDealerlist() {
		return resultlist;
	}
	public void setDealerlist(List<Dealer> resultlist) {
		this.resultlist = resultlist;
	}
	
	public static DealerSearchList parse(String json) throws IOException, AppException {
		DealerSearchList dealersearchList = new DealerSearchList();
		Dealer dealer = null;
		JSONObject ojson;
		try {
			ojson = new JSONObject(json);
			dealersearchList.pageSize =ojson.getInt("count");
			//dealersearchList.goodsCount =ojson.getInt("count");
			JSONArray dealerArray = ojson.getJSONArray("dealer");
			for(int i = 0;i < dealerArray.length();i ++){
				dealer = new Dealer();
				JSONObject dealeroj = (JSONObject) dealerArray.opt(i);
				dealer.id=dealeroj.getInt("ID");
				//goods.setGimg(goodsoj.getString("photos").split("nn:nn")[0]);
				dealer.setDlname(dealeroj.getString("dlname"));		            	
            	dealer.setPdealer(dealeroj.getString("pdlname"));		            	
            	dealer.setCphone(dealeroj.getString("cphone"));			            	
            	//dealer.setTel(dealeroj.getString("telphone"));	
            	dealersearchList.getDealerlist().add(dealer); 
		        dealer = null;
			}
	    	
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}    	      
	    return dealersearchList;       
	}
}
