package cn.chamatou.biz.bean;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * 文章实体类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2016-2-21
 */
public class Article implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String title;
	private String content;
	private int type;
	private int click;
	private int praise;
	private int follow;
	private String default_image;
	private String create_time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getAtype() {
		return type;
	}

	public void setAtype(int atype) {
		this.type = atype;
	}

	public int getClick() {
		return click;
	}

	public void setClick(int click) {
		this.click = click;
	}

	public int getPraise() {
		return praise;
	}

	public void setPraise(int praise) {
		this.praise = praise;
	}

	public int getFollow() {
		return follow;
	}

	public void setFollow(int follow) {
		this.follow = follow;
	}

	public String getCreateTime() {
		return create_time;
	}

	public void setCreateTime(String createTime) {
		this.create_time = createTime;
	}

	public String getDefault_image() {
		return default_image;
	}
	public void setDefault_image(String default_image) {
		this.default_image = default_image;
	}
		
}
