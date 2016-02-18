package cn.chamatou.biz.bean;

import java.io.IOException;
import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.annotations.SerializedName;
import cn.chamatou.biz.AppException;

/**
 * 商铺实体类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class Store implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String store_name;
	private String store_addr;
	private String description;
	private String title;
	private String location;
	private String head;	

	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getStore_addr() {
		return store_addr;
	}
	public void setStore_addr(String store_addr) {
		this.store_addr = store_addr;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}	
}
