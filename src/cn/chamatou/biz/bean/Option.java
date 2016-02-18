package cn.chamatou.biz.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Option implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;// ID
	@SerializedName("t_name")
	public String t_name;// 名称

	public String GetId() {
		return id;
	}

	public void SetId(String id) {
		this.id = id;
	}

	public String GetTname() {
		return t_name;
	}

	public void SetTname(String t_name) {
		this.t_name = t_name;
	}

	@Override
	public String toString() {
		return t_name;
	}

}
