package cn.chamatou.biz.bean;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cn.chamatou.biz.AppException;

/**
 * 订单列表实体类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class OrderList extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int pageSize;
	private int orderCount;
	private List<Order> orderlist = new ArrayList<Order>();
	
	public int getPageSize() {
		return pageSize;
	}
	public int getOrderCount() {
		return orderCount;
	}
	public List<Order> getOrderlist() {
		return orderlist;
	}

	public static OrderList parse(String json) throws IOException, AppException {
		OrderList orderlist = new OrderList();
		//Goods goods = null;
		Type type = new TypeToken<List<Order>>() {
		}.getType();
		System.out.println("order:" + json);
		GsonBuilder b = new GsonBuilder();
		b.setDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Order> olist= b.create().fromJson(json, type);
		orderlist.orderlist=olist;
		orderlist.pageSize=olist.size();	
		
       return orderlist;       
	}
}
