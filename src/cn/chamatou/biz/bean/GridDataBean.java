package cn.chamatou.biz.bean;

import java.io.Serializable;

public class GridDataBean implements Serializable {
	private static final long serialVersionUID = 20140829L;
	private int index;
	private String title;
	private String val;
	private int background;
	private boolean isVisible = true;

	public GridDataBean(int index, String title, int background, String val) {
		super();
		this.index = index;
		this.title = title;
		this.background = background;
		this.setVal(val);
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

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	

}
