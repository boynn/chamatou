package cn.chamatou.biz.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import cn.chamatou.biz.AppException;

/**
 * 订单实体类
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	@SerializedName("order_number")
	private String order_no;
	@SerializedName("mobile")
	private String mobile;
	@SerializedName("resident_name")
	private String resident_name;
	@SerializedName("addr")
	private String addr;
	private String srvtime;
	private String notes;
	private String create_time;
	private List<String> pic;
	private double total;
	private double paid;
	private int payment;
	private int state;
	private int shipping;
	private int cancel;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getResident_name() {
		return resident_name;
	}

	public void setResident_name(String resident_name) {
		this.resident_name = resident_name;
	}

	public String getOrderno() {
		return order_no;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getAddr() {
		return addr;
	}

	public void setOrderno(String order_no) {
		this.order_no = order_no;
	}

	public List<String> getPic() {
		return pic;
	}

	public void setPic(List<String> pic) {
		this.pic = pic;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getPaid() {
		return paid;
	}

	public void setPaid(double paid) {
		this.paid = paid;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getPayment() {
		return payment;
	}

	public void setPayment(int payment) {
		this.payment = payment;
	}
	public int getCancel() {
		return cancel;
	}
	public void setCancel(int cancel) {
		this.cancel = cancel;
	}

	public int getShipping() {
		return shipping;
	}

	public void setShipping(int shipping) {
		this.shipping = shipping;
	}

	public String getSrvtime() {
		return srvtime;
	}

	public void setSrvtime(String srvtime) {
		this.srvtime = srvtime;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
