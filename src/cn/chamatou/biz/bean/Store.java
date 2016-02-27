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
	
	private int id;
	private String name;
	private String address;
	private String owner;
	private String mgr;
	private String description;
	private String srv;
	private String ttype;
	private String head;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getMgr() {
		return mgr;
	}
	public void setMgr(String mgr) {
		this.mgr = mgr;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSrv() {
		return srv;
	}
	public void setSrv(String srv) {
		this.srv = srv;
	}
	public String getTtype() {
		return ttype;
	}
	public void setTtype(String ttype) {
		this.ttype = ttype;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}	

}
