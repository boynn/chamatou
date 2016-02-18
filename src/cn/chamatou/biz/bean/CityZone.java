package cn.chamatou.biz.bean;

public class CityZone
{
    public String ID;// 区县ID
    public String zname;// 区县名称
    public CityZone () {
    	   ID = "";
    	   zname = "";
    	  }

    	  public CityZone (String _ID, String _Name) {
    	   ID = _ID;
    	   zname = _Name;
    	  }
    	  @Override  
    	  public String toString() {  
    	          return zname;  
    	  }  
    	  public String GetId() {  
    	   return ID;  
    	  }
    	  public String GetName() {  
    	          return zname;  
    	  }  

}
