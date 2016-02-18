package cn.chamatou.biz.bean;

import java.io.Serializable;

public class MsgListItemBean implements Serializable {
	private int unReadCount;
	private int type;
	private int icon;
	private MsgBean lastestMsg;//最后一条新闻

	public MsgListItemBean(int unReadCount, int type, int icon,
			MsgBean lastestMsg) {
		super();
		this.unReadCount = unReadCount;
		this.type = type;
		this.icon = icon;
		this.lastestMsg = lastestMsg;
	}

	public int getUnReadCount() {
		return unReadCount;
	}

	public void setUnReadCount(int unReadCount) {
		this.unReadCount = unReadCount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public MsgBean getLastestMsg() {
		return lastestMsg;
	}

	public void setLastestMsg(MsgBean lastestMsg) {
		this.lastestMsg = lastestMsg;
	}

}
