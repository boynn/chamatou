package cn.chamatou.biz.bean;

import java.io.Serializable;

public class GridFuncBean implements Serializable {
	private static final long serialVersionUID = 20140829L;
	private int index;
	private String title;
	private int background;
	private int img;
	private boolean isVisible = true;

	public GridFuncBean(int index, String title, int background, int img) {
		super();
		this.index = index;
		this.title = title;
		this.background = background;
		this.img = img;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

}
