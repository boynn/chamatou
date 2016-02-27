package cn.chamatou.biz.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class MsgBean implements Serializable {
	private int id;
	@SerializedName("creator_user")
	private String sender;
	private String content;
	@SerializedName("create_time")
	private String time;
	private int type;
	@SerializedName("icon_url")
	private String imgUrl;

	public MsgBean(String sender, String content, String time, int type,
			String imgUrl) {
		super();
		this.sender = sender;
		this.content = content;
		this.time = time;
		this.type = type;
		this.imgUrl = imgUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
