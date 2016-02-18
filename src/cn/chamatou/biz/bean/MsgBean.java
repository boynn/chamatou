package cn.chamatou.biz.bean;

import java.io.Serializable;

public class MsgBean implements Serializable {
	private String title;
	private String content;
	private String time;
	private int type;
	private String imgUrl;

	public MsgBean(String title, String content, String time, int type,
			String imgUrl) {
		super();
		this.title = title;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
