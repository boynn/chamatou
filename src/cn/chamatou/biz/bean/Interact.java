package cn.chamatou.biz.bean;


/**
 * 客户联系记录实体类
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class Interact extends Entity{

	private static final long serialVersionUID = 1L;

	private String cname;
	private int clientId;
	private String actDate;
	private String detail;
	protected int id;


	public int getId() {
		return id;
	}


	public String getCname() {
		return cname;
	}


	public void setCname(String cname) {
		this.cname = cname;
	}


	public String getActDate() {
		return actDate;
	}


	public void setActDate(String actdate) {
		this.actDate = actdate;
	}


	public int getClientId() {
		return clientId;
	}


	public void setClientId(int clientId) {
		this.clientId = clientId;
	}


	public String getDetail() {
		return detail;
	}


	public void setDetail(String detail) {
		this.detail = detail;
	}

}
