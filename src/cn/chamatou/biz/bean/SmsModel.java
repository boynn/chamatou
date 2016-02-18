package cn.chamatou.biz.bean;

public class SmsModel
{
    public String ID;// ID
    public String descr;// 名称
    public String cont;// 内容
    public SmsModel () {
    	   ID = "";
    	   descr = "";
    	   cont = "";
    	  }

    	  public SmsModel (String _ID, String _Name, String _Content) {
    	   ID = _ID;
    	   descr = _Name;
    	   cont = _Content;
    	  }
    	  @Override  
    	  public String toString() {  
    	          return descr;  
    	  }  
    	  public String GetId() {  
    	   return ID;  
    	  }
    	  public String GetName() {  
	          return descr;  
    	  }
    	  public String GetCont() {  
    	          return cont;  
    	  }  

}
