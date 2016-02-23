package cn.chamatou.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.http.client.CookieStore;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import cn.chamatou.biz.api.ApiClient;
import cn.chamatou.biz.bean.Article;
import cn.chamatou.biz.bean.ContactSearchList;
import cn.chamatou.biz.bean.Expert;
import cn.chamatou.biz.bean.Goods;
import cn.chamatou.biz.bean.GoodsList;
import cn.chamatou.biz.bean.Merchant;
import cn.chamatou.biz.bean.MyInfo;
import cn.chamatou.biz.bean.Notice;
import cn.chamatou.biz.bean.NoticeList;
import cn.chamatou.biz.bean.Option;
import cn.chamatou.biz.bean.Order;
import cn.chamatou.biz.bean.OrderList;
import cn.chamatou.biz.bean.RemindList;
import cn.chamatou.biz.bean.Store;
import cn.chamatou.biz.bean.User;
import cn.chamatou.biz.common.CyptoUtils;
import cn.chamatou.biz.common.ImageUtils;
import cn.chamatou.biz.common.MethodsCompat;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.common.UIHelper;
import cn.chamatou.biz.widget.draggrid.ChannelItem;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * @author ryan (http://www.boynn.com/)
 * @version 1.0
 * @created 2013-6-21
 */
@SuppressWarnings("deprecation")
public class AppContext extends Application {
	
    public static boolean isGPS = false;// 是否已经定位
	public static Context context;
    public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;
	public static AppContext self;

	public static final int PAGE_SIZE = 20;//默认分页大小
	private static final int CACHE_TIME = 60*60000;//缓存失效时间
	public LocationClient mLocationClient;
    public GeofenceClient mGeofenceClient;
    public MyLocationListener mMyLocationListener;
    public static BitmapUtils bitmapUtils;

	private boolean login = false;	//登录状态
	private int loginUid = 0;	//登录用户的id
	private boolean isR1 = false;	//茶艺师
	private boolean isR2 = false;	//茶坊
	private boolean isR3 = false;	//茶商
	private Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();
	private CookieStore cookies;  
    public CookieStore getCookie(){   
        return cookies;
    }
    public void setCookie(CookieStore cks){
        cookies = cks;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        self = this;
		
        // 初始化
        init();
    }
    
    private void init()
    {
        LogUtils.i("-------------SystemApplication");
        // 初始化参数
        context = getApplicationContext();
        // 初始化地图定位
        initLocation();
        // 初始化BitmapUtils对象
        initBitmapUtils();       
        initImageLoader(self);
        
    }
        /**
     * 初始化地图定位
     */
    private void initLocation()
    {
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mGeofenceClient = new GeofenceClient(getApplicationContext());

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }
    /**
     * 初始化BitmapUtils对象
     */
    public void initBitmapUtils()
    {

        bitmapUtils = new BitmapUtils(context);
    }
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				//.writeDebugLogs() // Remove for release app
				.build();
			// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
    private Handler unLoginHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 1){
				UIHelper.ToastMessage(AppContext.this, getString(R.string.msg_login_error));
				UIHelper.showLoginDialog(AppContext.this);
			}
		}		
	};
	public boolean isR1() {
		return isR1;
	}
	public boolean isR2() {
		return isR2;
	}
	public boolean isR3() {
		return isR3;
	}
	
	/**
	 * 检测当前系统声音是否为正常模式
	 * @return
	 */
	public boolean isAudioNormal() {
		AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE); 
		return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
	}
	
	/**
	 * 应用程序是否发出提示音
	 * @return
	 */
	public boolean isAppSound() {
		return isAudioNormal() && isVoice();
	}
	
	/**
	 * 检测网络是否可用
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
	 */
	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}		
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if(!StringUtils.isEmpty(extraInfo)){
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
	
	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}
	
	/**
	 * 获取App安装包信息
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try { 
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
		if(info == null) info = new PackageInfo();
		return info;
	}
	
	/**
	 * 获取App唯一标识
	 * @return
	 */
	public String getAppId() {
		String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
		if(StringUtils.isEmpty(uniqueID)){
			uniqueID = UUID.randomUUID().toString();
			setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
		}
		return uniqueID;
	}
	
	/**
	 * 用户是否登录
	 * @return
	 */
	public boolean isLogin() {
		return login;
	}
	
	/**
	 * 获取登录用户id
	 * @return
	 */
	public int getLoginUid() {
		return this.loginUid;
	}
	
	public Boolean getR1() {
		return this.isR1;
	}
	public Boolean getR2() {
		return this.isR2;
	}
	public Boolean getR3() {
		return this.isR3;
	}
	/**
	 * 用户注销
	 */
	public void Logout() {
		ApiClient.cleanCookie();
		this.cleanCookie();
		this.login = false;
		this.loginUid = 0;
	}
	
	/**
	 * 未登录或修改密码后的处理
	 */
	public Handler getUnLoginHandler() {
		return this.unLoginHandler;
	}
	
	/**
	 * 初始化用户登录信息
	 */
	public void initLoginInfo() {
		User loginUser = getLoginInfo();
		if(loginUser!=null && loginUser.getUid()>0 && loginUser.isRememberMe()){
			this.loginUid = loginUser.getUid();
			this.login = true;
			this.isR1 = loginUser.isR1();
			this.isR2 = loginUser.isR2();
			this.isR3 = loginUser.isR3();
			
		}else{
			this.Logout();
		}
	}
	
	/**
	 * 用户登录验证
	 * @param account
	 * @param pwd
	 * @return
	 * @throws AppException
	 */
	public User loginVerify(String account, String pwd) throws AppException {
		return ApiClient.login(this, account, pwd);
	}
	
	public String getMydatas() throws AppException {
		String mydata="[";
		if(this.isR1){
			for (ChannelItem channelItem : Const.defaultUserDataMap.values()) {
				if (channelItem.getSelected()==1) {
					mydata+=channelItem.getId()+",";
				}
			}
			
		}
		if(this.isR2){
			for (ChannelItem channelItem : Const.defaultUserDataMap.values()) {
				if (channelItem.getSelected()==2) {
					mydata+=channelItem.getId()+",";
				}
			}
			
		}
		if(this.isR3){
			for (ChannelItem channelItem : Const.defaultUserDataMap.values()) {
				if (channelItem.getSelected()==3) {
					mydata+=channelItem.getId()+",";
				}
			}
			
		}
		return mydata+="20]";
	}
	public String getMyfuncs() throws AppException {
		String myfunc="[";
		if(this.isR1){
			for (ChannelItem channelItem : Const.defaultUserChannelMap.values()) {
				if (channelItem.getSelected()==1) {
					myfunc+=channelItem.getId()+",";
				}
			}
			
		}
		if(this.isR2){
			for (ChannelItem channelItem : Const.defaultUserChannelMap.values()) {
				if (channelItem.getSelected()==2) {
					myfunc+=channelItem.getId()+",";
				}
			}
			
		}
		if(this.isR3){
			for (ChannelItem channelItem : Const.defaultUserChannelMap.values()) {
				if (channelItem.getSelected()==3) {
					myfunc+=channelItem.getId()+",";
				}
			}
			
		}
		return myfunc+="20]";
	}
	/**
	 * 商家注册
	 * @param name 
	 * @param tel
	 * @param pwd
	 * @return
	 * @throws AppException
	 */
	public String storeReg(String name, String tel, String pwd, String vcode) throws AppException {
		return ApiClient.storeReg(this, name,tel,pwd,vcode);
	}
	/**
	 * 订单列表
	 * @param minID：
	 * @param tday
	 * @return
	 * @throws AppException
	 */
	
	public OrderList getOrderList( int minID,int tday) throws AppException {
		OrderList list = null;
		String key = "orderlist_"+minID+"_"+PAGE_SIZE;		
		if(isNetworkConnected()) {
			try{
				list = ApiClient.getOrderList(this,loginUid,minID, tday);
				if(list != null && minID == 0){
					saveObject(list, key);					
				}
			}catch(AppException e){
				list = (OrderList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (OrderList)readObject(key);
			if(list == null)
				list = new OrderList();
		}
		return list;
	}
/**
	 * 商品列表
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public GoodsList getGoodsList( int page, boolean isRefresh) throws AppException {
		GoodsList list = null;
		String key = "goodslist_"+page+"_"+PAGE_SIZE;		
		if(isNetworkConnected()) {
			try{
				list = ApiClient.getGoodsList(this,loginUid,page, PAGE_SIZE);
				if(list != null && page == 0){
					saveObject(list, key);					
				}
			}catch(AppException e){
				list = (GoodsList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (GoodsList)readObject(key);
			if(list == null)
				list = new GoodsList();
		}
		return list;
	}
	/**
	 * 新客户
	 * @param post （uid、title、catalog、content、isNoticeMe）
	 * @return
	 * @throws AppException
	 */
	public String saveGoods(Goods goods) throws AppException {
		return ApiClient.saveGoods(this,goods,this.loginUid);
	}
	public String uploadFile(File file) throws AppException {
		return ApiClient.upload(file);
	}
	public String uploadData(byte[] data) throws AppException {
		return ApiClient.upload(data);
	}
	/**
	 * 获取客户详情
	 * @param dealer_id
	 * @return
	 * @throws AppException
	 */
	public Order getOrder(int order_id, boolean isRefresh) throws AppException {
		Order order = null;
		String key = "order_"+order_id;
		if(isNetworkConnected()) {
			try{
				order = ApiClient.getOrderDetail(this, order_id);
				if(order != null){
					saveObject(order, key);					
				}
			}catch(AppException e){
				order = (Order)readObject(key);
				if(order == null)
					throw e;
			}
		} else {
			order = (Order)readObject(key);
			if(order == null)
				order = new Order();
		}
		return order;
	}
	public String getOrderGoods(String order_id, boolean isRefresh) throws Exception {
		return ApiClient.getOrderGoods(this, order_id);				
	}
	public String shipOrder(int order_id) throws Exception {
		return ApiClient.shipOrder(this, order_id,this.loginUid);				
	}


	public String getJoinAppId() throws AppException {
		return ApiClient.getSeqNo(this, "appno");
	}
	

	public NoticeList getNoticeList(boolean isRefresh) throws AppException {
		NoticeList list = null;
		String key = "Noticelist_0";
		if(isNetworkConnected()) {
			try{
				list = ApiClient.getNoticeList(this,0,20);
				if(list != null){
					saveObject(list, key);					
				}
			}catch(AppException e){
				list = (NoticeList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (NoticeList)readObject(key);
			if(list == null)
				list = new NoticeList();
		}
		return list;
	}	
	
	@SuppressWarnings("unchecked")
	public List<Option> getAtype() throws AppException {
		List<Option> tList = null;
		String key = "articletype_";
		if(isNetworkConnected()) {
			try{
				return ApiClient.getAtype(this);
				
			}catch(AppException e){
				tList = (List<Option>)readObject(key);
				if(tList == null)
					throw e;
			}
		} else {
			tList = (List<Option>)readObject(key);			;
		}
		return tList;
	}
	/**
	 * 获取服务类型
	 * @param myinfo_id
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public List<Option> getMtype() throws AppException {
		List<Option> tList = null;
		String key = "merchanttype_";
		if(isNetworkConnected()) {
			try{
				return ApiClient.getMtype(this);
				
			}catch(AppException e){
				tList = (List<Option>)readObject(key);
				if(tList == null)
					throw e;
			}
		} else {
			tList = (List<Option>)readObject(key);			;
		}
		return tList;
	}
	@SuppressWarnings("unchecked")
	public List<Option> getStype(String mType) throws AppException {
		List<Option> tList = null;
		String key = "mystoretype_";
		if(isNetworkConnected()) {
			try{
				return ApiClient.getStype(this,mType);
				
			}catch(AppException e){
				tList = (List<Option>)readObject(key);
				if(tList == null)
					throw e;
			}
		} else {
			tList = (List<Option>)readObject(key);			;
		}
		return tList;
	}
	/**
	 * 获取商家信息
	 * @param myinfo_id
	 * @return
	 * @throws AppException
	 */
	public Merchant getMyMerchant() throws AppException {
		Merchant mystore = null;
		String key = "merchant"+this.loginUid;
		if(isNetworkConnected()) {
			try{
				mystore = ApiClient.getMyMerchant(this, this.loginUid);
				if(mystore != null){
					saveObject(mystore, key);					
				}
			}catch(AppException e){
				mystore = (Merchant)readObject(key);
				if(mystore == null)
					throw e;
			}
		} else {
			mystore = (Merchant)readObject(key);
			if(mystore == null)
				mystore = new Merchant();
		}
		return mystore;
	}
	/**
	 * 获取门店信息
	 * @param myinfo_id
	 * @return
	 * @throws AppException
	 */
	public Store getMyStore() throws AppException {
		Store mystore = null;
		String key = "mystore_"+this.loginUid;
		if(isNetworkConnected()) {
			try{
				mystore = ApiClient.getMyStore(this, this.loginUid);
				if(mystore != null){
					saveObject(mystore, key);					
				}
			}catch(AppException e){
				mystore = (Store)readObject(key);
				if(mystore == null)
					throw e;
			}
		} else {
			mystore = (Store)readObject(key);
			if(mystore == null)
				mystore = new Store();
		}
		return mystore;
	}
	/**
	 * 获取茶艺师信息
	 * @param myinfo_id
	 * @return
	 * @throws AppException
	 */
	public Expert getMyExpert() throws AppException {
		Expert myexpert = null;
		String key = "myexpert_"+this.loginUid;
		if(isNetworkConnected()) {
			try{
				myexpert = ApiClient.getMyExpert(this, this.loginUid);
				if(myexpert != null){
					saveObject(myexpert, key);					
				}
			}catch(AppException e){
				myexpert = (Expert)readObject(key);
				if(myexpert == null)
					throw e;
			}
		} else {
			myexpert = (Expert)readObject(key);
			if(myexpert == null)
				myexpert = new Expert();
		}
		return myexpert;
	}
	public MyInfo getMyinfo( boolean isRefresh) throws AppException {
		MyInfo myinfo = null;
		String key = "myinfo_"+this.loginUid;
		if(isNetworkConnected()) {
			try{
				myinfo = ApiClient.getMyInfo(this, this.loginUid);
				if(myinfo != null){
					saveObject(myinfo, key);					
				}
			}catch(AppException e){
				myinfo = (MyInfo)readObject(key);
				if(myinfo == null)
					throw e;
			}
		} else {
			myinfo = (MyInfo)readObject(key);
			if(myinfo == null)
				myinfo = new MyInfo();
		}
		return myinfo;
	}
	public String getMyReminds( boolean isRefresh) throws AppException {
		RemindList myrmds=getRemindlist(isRefresh);
		if(myrmds!=null&&myrmds.getPageSize()>0)
			return "您有"+myrmds.getPageSize()+"个未处理事务";
		else return "";
	}
	public RemindList getRemindlist( boolean isRefresh) throws AppException {
		RemindList myrmd = null;
		String key = "myReminds_"+this.loginUid;
		if(isNetworkConnected()) {
			try{
				myrmd = ApiClient.getRemindList(this, this.loginUid);
				if(myrmd != null){
					saveObject(myrmd, key);					
				}
			}catch(AppException e){
				myrmd = (RemindList)readObject(key);
				if(myrmd == null)
					throw e;
			}
		} else {
			myrmd = (RemindList)readObject(key);
			if(myrmd == null)
				myrmd = new RemindList();
		}
		
		return myrmd;
	}
	public String delGoods(int goods_id) throws AppException {
		return ApiClient.delGoods(this, goods_id,loginUid);
	}
	/**
	 * 保存登录信息
	 * @param username
	 * @param pwd
	 */
	public void saveLoginInfo(final User user) {
		this.loginUid = user.getUid();
		this.isR1 = user.isR1();
		this.isR2 = user.isR2();
		this.isR3 = user.isR3();
		this.login = true;
		setProperties(new Properties(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			setProperty("user.uid", String.valueOf(user.getUid()));
			setProperty("user.name", user.getName());
			setProperty("user.account", user.getAccount());
			setProperty("user.isR1",  String.valueOf(user.isR1()));
			setProperty("user.isR2",  String.valueOf(user.isR2()));
			setProperty("user.isR3",  String.valueOf(user.isR3()));
			setProperty("user.pwd", CyptoUtils.encode("oschinaApp",user.getPwd()));
			setProperty("user.isRememberMe", String.valueOf(user.isRememberMe()));//是否记住我的信息
		}});		
	}
	
	/**
	 * 清除登录信息
	 */
	public void cleanLoginInfo() {
		this.loginUid = 0;
		this.login = false;
		removeProperty("user.uid","user.name","user.head","user.account","user.pwd","user.isR1","user.isR2","user.isR3","user.isRememberMe");
	}
	
	/**
	 * 获取登录信息
	 * @return
	 */
	public User getLoginInfo() {		
		User lu = new User();		
		lu.setUid(StringUtils.toInt(getProperty("user.uid"), 0));
		lu.setName(getProperty("user.name"));
		lu.setHead(getProperty("user.head"));
		lu.setAccount(getProperty("user.account"));
		lu.setPwd(CyptoUtils.decode("oschinaApp",getProperty("user.pwd")));
		lu.setR1(StringUtils.toBool(getProperty("user.isR1")));
		lu.setR2(StringUtils.toBool(getProperty("user.isR2")));
		lu.setR3(StringUtils.toBool(getProperty("user.isR3")));
		lu.setRememberMe(StringUtils.toBool(getProperty("user.isRememberMe")));
		return lu;
	}
	
	/**
	 * 保存用户头像
	 * @param fileName
	 * @param bitmap
	 */
	public void saveUserHead(String fileName,Bitmap bitmap) {
		try {
			ImageUtils.saveImage(this, fileName, bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取用户头像
	 * @param key
	 * @return
	 * @throws AppException
	 */
	public Bitmap getUserHead(String key) throws AppException {
		FileInputStream fis = null;
		try{
			fis = openFileInput(key);
			return BitmapFactory.decodeStream(fis);
		}catch(Exception e){
			throw AppException.run(e);
		}finally{
			try {
				fis.close();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * 是否加载显示文章图片
	 * @return
	 */
	public boolean isLoadImage()
	{
		String perf_loadimage = getProperty(AppConfig.CONF_LOAD_IMAGE);
		//默认是加载的
		if(StringUtils.isEmpty(perf_loadimage))
			return true;
		else
			return StringUtils.toBool(perf_loadimage);
	}
	
	/**
	 * 设置是否加载文章图片
	 * @param b
	 */
	public void setConfigLoadimage(boolean b)
	{
		setProperty(AppConfig.CONF_LOAD_IMAGE, String.valueOf(b));
	}
	
	/**
	 * 是否发出提示音
	 * @return
	 */
	public boolean isVoice()
	{
		String perf_voice = getProperty(AppConfig.CONF_VOICE);
		//默认是开启提示声音
		if(StringUtils.isEmpty(perf_voice))
			return true;
		else
			return StringUtils.toBool(perf_voice);
	}
	
	/**
	 * 设置是否发出提示音
	 * @param b
	 */
	public void setConfigVoice(boolean b)
	{
		setProperty(AppConfig.CONF_VOICE, String.valueOf(b));
	}
	
	/**
	 * 是否左右滑动
	 * @return
	 */
	public boolean isScroll()
	{
		String perf_scroll = getProperty(AppConfig.CONF_SCROLL);
		//默认是关闭左右滑动
		if(StringUtils.isEmpty(perf_scroll))
			return false;
		else
			return StringUtils.toBool(perf_scroll);
	}
	
	/**
	 * 设置是否左右滑动
	 * @param b
	 */
	public void setConfigScroll(boolean b)
	{
		setProperty(AppConfig.CONF_SCROLL, String.valueOf(b));
	}
	
	/**
	 * 是否Https登录
	 * @return
	 */
	public boolean isHttpsLogin()
	{
		String perf_httpslogin = getProperty(AppConfig.CONF_HTTPS_LOGIN);
		//默认是http
		if(StringUtils.isEmpty(perf_httpslogin))
			return false;
		else
			return StringUtils.toBool(perf_httpslogin);
	}
	
	/**
	 * 设置是是否Https登录
	 * @param b
	 */
	public void setConfigHttpsLogin(boolean b)
	{
		setProperty(AppConfig.CONF_HTTPS_LOGIN, String.valueOf(b));
	}
	
	/**
	 * 是否是信息密码
	 * @return
	 */
	public boolean isInfoPwd(String pwd)
	{
		String info_password = getProperty(AppConfig.CONF_INFO_PWD);
		//默认是http
		if(StringUtils.isEmpty(info_password))
		{	
			if(pwd.equals("bbmb"))
				return true;
			else
				return false;		
		}
		else
			return pwd.equals(info_password);
	}
	/**
	 * 设置内部信息密码
	 * @param pwd
	 */
	public void setConfigInfoPwd(String pwd)
	{
		setProperty(AppConfig.CONF_INFO_PWD, pwd);
	}
	/**
	 * 清除保存的缓存
	 */
	public void cleanCookie()
	{
		removeProperty(AppConfig.CONF_COOKIE);
	}
	
	/**
	 * 判断缓存是否存在
	 * @param cachefile
	 * @return
	 */
	private boolean isExistDataCache(String cachefile)
	{
		boolean exist = false;
		File data = getFileStreamPath(cachefile);
		if(data.exists())
			exist = true;
		return exist;
	}
	
	/**
	 * 判断缓存是否失效
	 * @param cachefile
	 * @return
	 */
	public boolean isCacheDataFailure(String cachefile)
	{
		boolean failure = false;
		File data = getFileStreamPath(cachefile);
		if(data.exists() && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
			failure = true;
		else if(!data.exists())
			failure = true;
		return failure;
	}
	
	/**
	 * 清除app缓存
	 */
	public void clearAppCache()
	{
		/*清除webview缓存
		File file = CacheManager.getCacheFileBaseDir();  
		if (file != null && file.exists() && file.isDirectory()) {  
		    for (File item : file.listFiles()) {  
		    	item.delete();  
		    }  
		    file.delete();  
		}  	*/	  
		deleteDatabase("webview.db");  
		deleteDatabase("webview.db-shm");  
		deleteDatabase("webview.db-wal");  
		deleteDatabase("webviewCache.db");  
		deleteDatabase("webviewCache.db-shm");  
		deleteDatabase("webviewCache.db-wal");  
		//清除数据缓存
		clearCacheFolder(getFilesDir(),System.currentTimeMillis());
		clearCacheFolder(getCacheDir(),System.currentTimeMillis());
		//2.2版本才有将应用缓存转移到sd卡的功能
		if(isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)){
			clearCacheFolder(MethodsCompat.getExternalCacheDir(this),System.currentTimeMillis());
		}
		//清除编辑器保存的临时内容
		Properties props = getProperties();
		for(Object key : props.keySet()) {
			String _key = key.toString();
			if(_key.startsWith("temp"))
				removeProperty(_key);
		}
	}	
	
	/**
	 * 清除缓存目录
	 * @param dir 目录
	 * @param numDays 当前系统时间
	 * @return
	 */
	private int clearCacheFolder(File dir, long curTime) {          
	    int deletedFiles = 0;         
	    if (dir!= null && dir.isDirectory()) {             
	        try {                
	            for (File child:dir.listFiles()) {    
	                if (child.isDirectory()) {              
	                    deletedFiles += clearCacheFolder(child, curTime);          
	                }  
	                if (child.lastModified() < curTime) {     
	                    if (child.delete()) {                   
	                        deletedFiles++;           
	                    }    
	                }    
	            }             
	        } catch(Exception e) {       
	            e.printStackTrace();    
	        }     
	    }       
	    return deletedFiles;     
	}
	
	/**
	 * 将对象保存到内存缓存中
	 * @param key
	 * @param value
	 */
	public void setMemCache(String key, Object value) {
		memCacheRegion.put(key, value);
	}
	
	/**
	 * 从内存缓存中获取对象
	 * @param key
	 * @return
	 */
	public Object getMemCache(String key){
		return memCacheRegion.get(key);
	}
	
	/**
	 * 保存磁盘缓存
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void setDiskCache(String key, String value) throws IOException {
		FileOutputStream fos = null;
		try{
			fos = openFileOutput("cache_"+key+".data", Context.MODE_PRIVATE);
			fos.write(value.getBytes());
			fos.flush();
		}finally{
			try {
				fos.close();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * 获取磁盘缓存数据
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public String getDiskCache(String key) throws IOException {
		FileInputStream fis = null;
		try{
			fis = openFileInput("cache_"+key+".data");
			byte[] datas = new byte[fis.available()];
			fis.read(datas);
			return new String(datas);
		}finally{
			try {
				fis.close();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * 保存对象
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public boolean saveObject(Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try{
			fos = openFileOutput(file, MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				oos.close();
			} catch (Exception e) {}
			try {
				fos.close();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * 读取对象
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Serializable readObject(String file){
		if(!isExistDataCache(file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try{
			fis = openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable)ois.readObject();
		}catch(FileNotFoundException e){
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				ois.close();
			} catch (Exception e) {}
			try {
				fis.close();
			} catch (Exception e) {}
		}
		return null;
	}

	public boolean containsProperty(String key){
		Properties props = getProperties();
		 return props.containsKey(key);
	}
	
	public void setProperties(Properties ps){
		AppConfig.getAppConfig(this).set(ps);
	}

	public Properties getProperties(){
		return AppConfig.getAppConfig(this).get();
	}
	
	public void setProperty(String key,String value){
		AppConfig.getAppConfig(this).set(key, value);
	}
	
	public String getProperty(String key){
		return AppConfig.getAppConfig(this).get(key);
	}
	public void removeProperty(String...key){
		AppConfig.getAppConfig(this).remove(key);
	}
	public void getDate(View v) {
		  final EditText edit = (EditText) v;
		  Calendar cd = Calendar.getInstance();
		  Date date = new Date();
		  cd.setTime(date);
		  new DatePickerDialog(v.getContext(), new OnDateSetListener() {
		   public void onDateSet(DatePicker view, int year, int monthOfYear,
		     int dayOfMonth) {
		    monthOfYear++;
		    edit.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
		   }
		  }, cd.get(Calendar.YEAR), cd.get(Calendar.MONTH),
		    cd.get(Calendar.DAY_OF_MONTH)).show();
		 }
	 /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            String city = location.getCity();
            if (!StringUtils.isEmpty(city))
            {
                if (!isGPS)
                {
                    isGPS = true;
                    mGeofenceClient.stop();                    
                }
            }
        }
    }
	public Notice getNotice(int notice_id, boolean isRefresh){
		Notice notice = null;
		String key = "notice_"+notice_id;
		if(isNetworkConnected()) {
			try {
				notice = ApiClient.getNoticeDetail(this, notice_id);
			} catch (AppException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			if(notice != null){
				saveObject(notice, key);					
			}
		} else {
			notice = (Notice)readObject(key);
			if(notice == null)
				notice = new Notice();
		}
		return notice;
	}
	public String changPwd(String Opwd,String Npwd) throws AppException{
		return ApiClient.changPwd(this,Opwd,Npwd,this.loginUid);
	}
	public String saveStore(Store store) throws AppException {
			return ApiClient.saveStore(this,store,this.loginUid);
		}

	public String saveExpert(Expert expert) throws AppException {
		return ApiClient.saveExpert(this,expert,this.loginUid);
	}
	public String saveMerchant(Merchant merchant) throws AppException {
		return ApiClient.saveMerchant(this,merchant,this.loginUid);
	}
		
	public String saveArticle(Article article) throws AppException {
		return ApiClient.saveArticle(this,article,this.loginUid);
	}
		
}
