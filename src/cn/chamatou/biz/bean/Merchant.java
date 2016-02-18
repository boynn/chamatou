package cn.chamatou.biz.bean;

import java.io.Serializable;

/**
 * 商铺实体类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class Merchant implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String merchant_name;
	private String merchant_addr;
	private int type;
	private int stype;
	private String description;
	private String title;
	private String head;	

	public String getMerchant_name() {
		return merchant_name;
	}
	public void setMerchant_name(String merchant_name) {
		this.merchant_name = merchant_name;
	}
	public String getMerchant_addr() {
		return merchant_addr;
	}
	public void setMerchant_addr(String merchant_addr) {
		this.merchant_addr = merchant_addr;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStype() {
		return stype;
	}
	public void setStype(int stype) {
		this.stype = stype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}

}
