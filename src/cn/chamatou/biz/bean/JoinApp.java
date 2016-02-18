package cn.chamatou.biz.bean;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import cn.chamatou.biz.AppException;

/**
 * 加盟申请实体类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public class JoinApp extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String appno;
	private int dealer;
	private String descr;
	private String dlname;
	private int appdealer;
	private String apptime;
	private String status;


	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getAppno() {
		return appno;
	}
	public void setAppno(String appno) {
		this.appno = appno;
	}
	public int getDealer() {
		return dealer;
	}
	public void setDealer(int dealer) {
		this.dealer = dealer;
	}
	public int getAppdealer() {
		return appdealer;
	}
	public void setAppdealer(int appdealer) {
		this.appdealer = appdealer;
	}
	public String getDlname() {
		return dlname;
	}
	public void setDlname(String dlname) {
		this.dlname = dlname;
	}

	public String getApptime() {
		return apptime;
	}
	public void setApptime(String apptime) {
		this.apptime = apptime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public static JoinApp parse(String json) throws IOException, AppException {
		JoinApp sorder = null;
        //获得XmlPullParser解析器
        XmlPullParser xmlParser = Xml.newPullParser();
        try {        	
            //xmlParser.setInput(json, UTF8);
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
        return sorder;       
	}
}
