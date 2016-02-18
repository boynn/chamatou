package cn.chamatou.biz.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * @author shiner
 */
public class OrderGoods implements Serializable {
	private String id;
	@SerializedName("goods_id")
	private int goodsId;
	private String order_number;
	@SerializedName("goods_name")
	private String goodsname;
	@SerializedName("goods_sn")
	private String goodsnum;
	private String unit;
	private int qty;
	private String description;
	@SerializedName("pic")
	private String thumbnail;
	private double price;
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsnum() {
		return goodsnum;
	}

	public void setGoodsnum(String goodsnum) {
		this.goodsnum = goodsnum;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

}
