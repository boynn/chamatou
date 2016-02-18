package cn.chamatou.biz.bean;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import cn.chamatou.biz.AppException;
import cn.chamatou.biz.async.Result;
import cn.chamatou.biz.common.StringUtils;

/**
 * 登录用户实体类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class User extends Base {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int uid;
	private String Account;
	private String pwd;
	private String name;
	private String head;
	private boolean isR1;
	private boolean isR2;
	private boolean isR3;
	private Result validate;
	private boolean isRememberMe;
	
	
	public boolean isRememberMe() {
		return isRememberMe;
	}
	public void setRememberMe(boolean isRememberMe) {
		this.isRememberMe = isRememberMe;
	}
	public String getAccount() {
		return Account;
	}
	public void setAccount(String account) {
		Account = account;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Result getValidate() {
		return validate;
	}
	public void setValidate(Result validate) {
		this.validate = validate;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	
	public boolean isR1() {
		return isR1;
	}
	public void setR1(boolean isR1) {
		this.isR1 = isR1;
	}
	public boolean isR2() {
		return isR2;
	}
	public void setR2(boolean isR2) {
		this.isR2 = isR2;
	}
	public boolean isR3() {
		return isR3;
	}
	public void setR3(boolean isR3) {
		this.isR3 = isR3;
	}
	public static User parse(String jison) throws IOException, AppException {
		User user = new User();
		Result res = null;
		JSONObject json;
		try {
			System.out.println("json=====>"+jison);
			json = new JSONObject(StringUtils.removeBOM(jison));   
			res = new Result();
			res.setError(json.getInt("error"));
			res.setErrorMsg(json.getString("errormsg"));
			if (res != null) { 
	       		user.setValidate(res);
	       		if(res.ok()){
	       			user.uid = json.getInt("id");
	    			user.setName(json.getString("name"));
	    			user.setHead(json.getString("head"));
	    			user.setR1((json.getInt("isR1")==1));
	    			user.setR2((json.getInt("isR2")==1));
	    			user.setR3((json.getInt("isR3")==1));
	    			
	    		}
	       	}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return user; 
		
	}
}
