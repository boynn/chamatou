package cn.chamatou.biz;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;

import android.graphics.Typeface;
import cn.chamatou.biz.widget.draggrid.ChannelItem;

public class Const {
	public static long SEX_BOY = 1;
	public static long SEX_GIRL = 0;

	// 配置文件常量
	public final static Integer USER_ID = 1;
	public final static Integer USER_PASSWORD = 2;
	public final static Integer USER_MOBILE = 3;

	public final static Integer CITY_ID = 100;
	public final static Integer CITY_NAME = 101;

	public final static Integer CAR_PLOY_VER = 1000;
	public final static Integer CAR_TYPE_VER = 1001;
	public final static Integer CITY_VER = 1002;
	public final static Integer HELP_VER = 1003;

	public final static Integer SOUND_STATE = 2000; // 按键声音状态

	public final static Integer TOTAL_UNREAD_MSG_NUM = 3000; // 总的未读通知数
	public final static Integer COMMUNITY_UNREAD_MSG_NUM = 3001; // 新闻的未读通知数
	public final static Integer UNREAD_MSG_NUM = 3002; // 通知的未读通知数
	
	// //////////////////////默认频道 /////////
	/**
	 * 默认的用户选择频道列表
	 * */
	public static Map<Integer, ChannelItem> defaultUserChannelMap,defaultUserDataMap;

	/**
	 * 首页显示模块默认顺序
	 */
	public static JSONArray funcs;
	static {
		defaultUserChannelMap = new LinkedHashMap<Integer, ChannelItem>();
		defaultUserChannelMap.put(1, new ChannelItem(1, "我的文章", 1, 1,R.drawable.article));
		defaultUserChannelMap.put(2, new ChannelItem(2, "我的活动", 2, 1,R.drawable.huod));
		defaultUserChannelMap.put(3, new ChannelItem(3,"个人中心", 3, 1,R.drawable.personpg)); 
		defaultUserChannelMap.put(4, new ChannelItem(4, "茶云联供", 4, 2,R.drawable.buy));
		defaultUserChannelMap.put(5, new ChannelItem(5, "优惠活动", 5, 2,R.drawable.discount));
		defaultUserChannelMap.put(6, new ChannelItem(6, "数据分析", 6, 2,R.drawable.nums));
		defaultUserChannelMap.put(7, new ChannelItem(7, "门店管理", 7, 2,R.drawable.teahouse));
		defaultUserChannelMap.put(8, new ChannelItem(8, "店铺管理", 8, 3,R.drawable.store));
		defaultUserChannelMap.put(9, new ChannelItem(9, "我的订单", 9, 3,R.drawable.order));
		defaultUserChannelMap.put(10, new ChannelItem(10, "发布商品", 10, 3,R.drawable.goods));
		defaultUserChannelMap.put(11, new ChannelItem(11, "促销管理", 11, 3,R.drawable.income));
		funcs = new JSONArray();
		for (ChannelItem channelItem : defaultUserChannelMap.values()) {
			funcs.put(channelItem.getId());
		}
	}
	
	/**
	 * 首页显示模块默认顺序
	 */
	public static JSONArray datas;

	static {
		defaultUserDataMap = new LinkedHashMap<Integer, ChannelItem>();
		defaultUserDataMap.put(1, new ChannelItem(1, "待回答", 1, 1,0));
		defaultUserDataMap.put(2, new ChannelItem(2, "今日订单", 2, 3,0));
		defaultUserDataMap.put(3, new ChannelItem(3, "待发货", 3, 3,0));
		defaultUserDataMap.put(4, new ChannelItem(4, "退款中", 4, 3,0));
		defaultUserDataMap.put(5, new ChannelItem(5, "本周销售", 5, 3,0));
		defaultUserDataMap.put(6, new ChannelItem(6, "本月销售", 6, 3,0));
		
		datas = new JSONArray();
		for (ChannelItem channelItem : defaultUserDataMap.values()) {
			datas.put(channelItem.getId());
		}
	}
}
