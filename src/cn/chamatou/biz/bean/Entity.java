package cn.chamatou.biz.bean;

import java.text.SimpleDateFormat;

/**
 * 实体类
 * @authorryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
public abstract class Entity extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static SimpleDateFormat SDF_IN = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat SDF_OUT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	

}
