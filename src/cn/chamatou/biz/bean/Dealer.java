package cn.chamatou.biz.bean;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import cn.chamatou.biz.AppException;
import cn.chamatou.biz.common.StringUtils;

/**
 * 会员实体类
 * @gnumryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class Dealer extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dlname;
	private String dlnum;
	private String gender;
	private String comp;
	private String idnum;
	private String contact;
	private String cphone;
	private String qq;
	private String email;
	private String web;
	private String zone;
	private String eaccount;
	private String bank;
	private String bankac;
	private String bankaddr;
	private String address;
	private String status;
	private String dlevel;
	private String pdealer;
	private String udealer;
	private String regDate;
	private String appDate;
	protected int id;

	public int getId() {
		return id;
	}

	public String getDlname() {
		return dlname;
	}
	public void setDlname(String dlname) {
		this.dlname = dlname;
	}
	public String getDlnum() {
		return dlnum;
	}

	public void setDlnum(String dlnum) {
		this.dlnum = dlnum;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getComp() {
		return comp;
	}

	public void setComp(String comp) {
		this.comp = comp;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCphone() {
		return cphone;
	}
	public void setCphone(String cphone) {
		this.cphone = cphone;
	}
	
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getEaccount() {
		return eaccount;
	}

	public void setEaccount(String eaccount) {
		this.eaccount = eaccount;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankac() {
		return bankac;
	}

	public void setBankac(String bankac) {
		this.bankac = bankac;
	}

	public String getBankaddr() {
		return bankaddr;
	}

	public void setBankaddr(String bankaddr) {
		this.bankaddr = bankaddr;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDlevel() {
		return dlevel;
	}

	public void setDlevel(String dlevel) {
		this.dlevel = dlevel;
	}

	public String getPdealer() {
		return pdealer;
	}

	public void setPdealer(String pdealer) {
		this.pdealer = pdealer;
	}

	public String getUdealer() {
		return udealer;
	}

	public void setUdealer(String udealer) {
		this.udealer = udealer;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getAppDate() {
		return appDate;
	}

	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}

	public static Dealer parse(String json) throws IOException, AppException {
		Dealer dealer = null;
		JSONObject ojson;
		try {
			ojson = new JSONObject(json);
			JSONArray dealerArray = ojson.getJSONArray("dealer");
			dealer = new Dealer();
			JSONObject dealeroj = (JSONObject) dealerArray.opt(0);
			dealer.id=dealeroj.getInt("ID");
			dealer.setDlname(dealeroj.getString("dlname"));
			dealer.setDlnum(dealeroj.getString("dlnum"));
			dealer.setComp(dealeroj.getString("comp"));
			dealer.setGender(dealeroj.getString("gender"));
			dealer.setContact(dealeroj.getString("contact"));
			dealer.setCphone(dealeroj.getString("cphone"));
			dealer.setQq(dealeroj.getString("qq"));
			dealer.setEmail(dealeroj.getString("email"));
			dealer.setWeb(dealeroj.getString("web"));
			dealer.setIdnum(dealeroj.getString("idnum"));
			dealer.setEaccount(dealeroj.getString("eaccount"));
			dealer.setAddress(dealeroj.getString("address"));
			dealer.setBank(dealeroj.getString("bkname"));
			dealer.setBankac(dealeroj.getString("bankac"));
			dealer.setBankaddr(dealeroj.getString("bankaddr"));
			dealer.setStatus(dealeroj.getString("dstatus"));		
			dealer.setDlevel(dealeroj.getString("dlvl"));
			dealer.setUdealer(dealeroj.getString("udlname"));		
			dealer.setPdealer(dealeroj.getString("pdlname"));		
			dealer.setAppDate(dealeroj.getString("appdate"));		
			dealer.setRegDate(dealeroj.getString("regdate"));		
			
					            	
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	      
       return dealer;        
	}

	
}
