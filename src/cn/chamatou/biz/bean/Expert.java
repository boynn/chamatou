package cn.chamatou.biz.bean;

import java.io.Serializable;

/**
 * 茶艺师实体类
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2016-1-21
 */
public class Expert implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;	
	private String name;
	private String address;
	private int sex;
	private String description;
	private String srv;
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
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
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
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	
}
