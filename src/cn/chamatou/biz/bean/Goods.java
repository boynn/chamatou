package cn.chamatou.biz.bean;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

/**
 * 商品实体类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class Goods implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String goods_name;
	@SerializedName("id")
	private int goodsid;
	@SerializedName("is_hot")
	private int state;
	@SerializedName("is_discount")
	private int discount;
	private int total;
	private String description;
	private String unit;
	private double price;
	private String default_image;
	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}


	public void setState(int state) {
		this.state = state;
	}
	public int getState() {
		return state;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public int getDiscount() {
		return discount;
	}
public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}


	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDefault_image() {
		return default_image;
	}
	public void setDefault_image(String default_image) {
		this.default_image = default_image;
	}
		
}
