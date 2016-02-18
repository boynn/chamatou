package cn.chamatou.biz.bean;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cn.chamatou.biz.AppException;

/**
 * 购物车商品列表实体类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class GoodsList extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int CATALOG_LASTEST = 0;
	public final static int CATALOG_HOT = -1;

	private int pageSize;
	private int itemCount;
	private int qtyCount;
	private double sum;
	private List<Goods> goodslist = new ArrayList<Goods>();
	public int getPageSize() {
		return pageSize;
	}
	public int getItemCount() {
		return itemCount;
	}
	public int getQtyCount() {
		return qtyCount;
	}
	public double getSum() {
			return sum;
	}
	public List<Goods> getGoodslist() {
		return goodslist;
	}
	public void setGoodslist(List<Goods> gl) {
		goodslist=gl;
	}
	public static GoodsList parse(String json) throws IOException, AppException {
		GoodsList goodslist = new GoodsList();
		System.out.println(json);//Goods goods = null;
		Type type = new TypeToken<List<Goods>>() {
		}.getType();
		List<Goods> glist= createGson().fromJson(json, type);
		goodslist.goodslist=glist;
		goodslist.pageSize=glist.size();
		
		return goodslist;       
	}
	
	private static Gson createGson() {
		// return new GsonBuilder().registerTypeHierarchyAdapter(String.class,
		// new DateAdapter()).create();
		GsonBuilder b = new GsonBuilder();
		b.setDateFormat("yyyy-MM-dd HH:mm:ss");
		return b.create();
	}
}
