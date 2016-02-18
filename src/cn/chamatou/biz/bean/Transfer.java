package cn.chamatou.biz.bean;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import cn.chamatou.biz.AppException;
import cn.chamatou.biz.common.StringUtils;

/**
 * 转款实体类
 * @ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class Transfer extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String eaccount;
	private String inaccount;
	private String acname;
	private String amount;
	private String password;
	private String transtime;
	private String descr;
	protected int id;

	public int getId() {
		return id;
	}

	public String getEaccount() {
		return eaccount;
	}

	public void setEaccount(String eaccount) {
		this.eaccount = eaccount;
	}

	public String getInaccount() {
		return inaccount;
	}

	public void setInaccount(String inaccount) {
		this.inaccount = inaccount;
	}

	public String getAcname() {
		return acname;
	}

	public void setAcname(String acname) {
		this.acname = acname;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTranstime() {
		return transtime;
	}

	public void setTranstime(String transtime) {
		this.transtime = transtime;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public static Transfer parse(String json) throws IOException, AppException {
		Transfer dept = null;
        //获得XmlPullParser解析器
        XmlPullParser xmlParser = Xml.newPullParser();
        try {        	
           // xmlParser.setInput(json, UTF8);
            //获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
            int evtType=xmlParser.getEventType();
			//一直循环，直到文档结束    
			while(evtType!=XmlPullParser.END_DOCUMENT){ 
	    		String tag = xmlParser.getName(); 
			    switch(evtType){ 
			    	case XmlPullParser.START_TAG:			    		
			    		break;
			    	case XmlPullParser.END_TAG:		    		
				       	break; 
			    }
			    //如果xml没有结束，则导航到下一个节点
			    evtType=xmlParser.next();
			}		
        } catch (XmlPullParserException e) {
			throw AppException.xml(e);
        }      
        return dept;       
	}
}
