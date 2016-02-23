package cn.chamatou.biz.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Config;
import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.R;
import cn.chamatou.biz.async.AsyncUtil;
import cn.chamatou.biz.async.Callback;
import cn.chamatou.biz.async.ExceptionHandler;
import cn.chamatou.biz.async.Result;
import cn.chamatou.biz.bean.Article;
import cn.chamatou.biz.bean.Goods;
import cn.chamatou.biz.bean.MyInfo;
import cn.chamatou.biz.bean.Option;
import cn.chamatou.biz.bean.Store;
import cn.chamatou.biz.bean.URLs;
import cn.chamatou.biz.common.AccountTool;
import cn.chamatou.biz.common.HttpUtil;
import cn.chamatou.biz.common.UIUtils;
import cn.chamatou.biz.common.UiTool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @author shiner
 */
public class StoreData {
	private final static String TAG = "StoreData";

	private static Gson createGson() {
		// return new GsonBuilder().registerTypeHierarchyAdapter(String.class,
		// new DateAdapter()).create();
		GsonBuilder b = new GsonBuilder();
		b.setDateFormat(UIUtils.dateFormate.toPattern());
		return b.create();
	}

	public static Result<MyInfo> getMyInfo(int store_id) {
		Result<MyInfo> result = new Result<MyInfo>();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			long timestamp = Calendar.getInstance().getTimeInMillis();
			params.add(new BasicNameValuePair("timestamp", String
					.valueOf(timestamp)));
			AccountTool at = new AccountTool(AppContext.self);
			params.add(new BasicNameValuePair("merchant_id", ""+store_id));
			String jObj = HttpUtil.post(URLs.MYINFO, params);
			MyInfo ms = null;
			JSONObject ojson;
			
			ojson = new JSONObject(jObj);
			ms = new MyInfo();
			ms.setName(ojson.getString("name"));
			ms.setHead(ojson.getString("head"));
			ms.setStype(ojson.getString("stype"));
			ms.setTitle(ojson.getString("title"));
			ms.setGoodsnum(ojson.getString("goodsnum"));
			ms.setOrdernum(ojson.getString("ordernum"));
			result.setData(ms);
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(AppContext.self.getString(R.string.unknown_error));
			result.setThrowable(e);
			ExceptionHandler.handleException(AppContext.self, result);
		}

		return result;

	}
	/**
	 * 猜你喜欢的接口
	 * @return
	 */
	public static Result<List<Goods>> getGoods(String store_id,String minId) {
		Result<List<Goods>> result = new Result<List<Goods>>();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			long timestamp = Calendar.getInstance().getTimeInMillis();
			params.add(new BasicNameValuePair("minId", minId));
			params.add(new BasicNameValuePair("merchant_id", ""+store_id));
			String jObj = HttpUtil.post(URLs.GOODS_LIST, params);
			
			Type type = new TypeToken<List<Goods>>() {
			}.getType();
			System.out.println("goods by store:" + jObj);
			List<Goods> ms = createGson().fromJson(jObj, type);
			result.setData(ms);
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(AppContext.self.getString(R.string.unknown_error));
			result.setThrowable(e);
			//ExceptionHandler.handleException(AppContext.self, result);
		}

		return result;

	}
	
	public static Result<List<Goods>> getGoodsDetail(int goodsId) {
		Result<List<Goods>> result = new Result<List<Goods>>();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			long timestamp = Calendar.getInstance().getTimeInMillis();
			params.add(new BasicNameValuePair("timestamp", String
					.valueOf(timestamp)));
			params.add(new BasicNameValuePair("goodsId", goodsId+""));
			//ELog.d(TAG, "getGoodsDetail:req:" + params.toString());
			String jObj = HttpUtil.post(URLs.URL_API_HOST
					+ "Market/getGoodsDetail", params);
			//ELog.d(TAG, "getGoodsDetail:res:" + jObj);

			//SimpleCache cache = SimpleCache.getInstance(AppContext.self);
			//cache.set("Market/getGoodsDetail", jObj);

			Type type = new TypeToken<List<Goods>>() {
			}.getType();
			
			List<Goods> ms = createGson().fromJson(jObj, type);
			result.setData(ms);
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(AppContext.self.getString(R.string.unknown_error));
			result.setThrowable(e);
			ExceptionHandler.handleException(AppContext.self, result);
		}

		return result;

	}

	public static Result<List<Goods>> getMyPublish(String minId) {
		Result<List<Goods>> result = new Result<List<Goods>>();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			long timestamp = Calendar.getInstance().getTimeInMillis();
			params.add(new BasicNameValuePair("timestamp", String
					.valueOf(timestamp)));
			AccountTool at = new AccountTool(AppContext.self);
			params.add(new BasicNameValuePair("minId", minId));
			params.add(new BasicNameValuePair("resident_id", at.getId()));
			params.add(new BasicNameValuePair("sign", UiTool.sign(timestamp,
					at.getId(), minId)));
			//ELog.d(TAG, "getMyPublish:req:" + params.toString());
			String jObj = HttpUtil.post(URLs.URL_API_HOST
					+ "Market/getMyPublish", params);
			//ELog.d(TAG, "getMyPublish:res:" + jObj);

			Type type = new TypeToken<List<Goods>>() {
			}.getType();

			List<Goods> ms = createGson().fromJson(jObj, type);
			result.setData(ms);
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(AppContext.self.getString(R.string.unknown_error));
			result.setThrowable(e);
			ExceptionHandler.handleException(AppContext.self, result);
		}
		System.out.println("goodsdetail:" + result);
		
		return result;

	}

	/**
	 * 搜索商品
	 * @param q
	 * @param minId
	 * @return
	 */
	public static Result<List<Goods>> search(String q, String minId) {
		Result<List<Goods>> result = new Result<List<Goods>>();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			long timestamp = Calendar.getInstance().getTimeInMillis();
			params.add(new BasicNameValuePair("timestamp", String
					.valueOf(timestamp)));
			AccountTool at = new AccountTool(AppContext.self);
			params.add(new BasicNameValuePair("name", q));
			params.add(new BasicNameValuePair("minId", minId));
			String jObj = HttpUtil.post(URLs.URL_API_HOST
					+ "Market/searchByName", params);
			
			Type type = new TypeToken<List<Goods>>() {
			}.getType();

			List<Goods> ms = createGson().fromJson(jObj, type);
			result.setData(ms);
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(AppContext.self.getString(R.string.unknown_error));
			result.setThrowable(e);
			ExceptionHandler.handleException(AppContext.self, result);
		}

		return result;

	}

	public static Result<Void> publish(Goods goods) {
		Result<Void> result = new Result<Void>();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			long timestamp = Calendar.getInstance().getTimeInMillis();
			params.add(new BasicNameValuePair("price", goods.getPrice() + ""));
			params.add(new BasicNameValuePair("timestamp", String
					.valueOf(timestamp)));
			AccountTool at = new AccountTool(AppContext.self);
			params.add(new BasicNameValuePair("merchant_id", at.getId()));
			String jObj = HttpUtil.post(
					URLs.URL_API_HOST + "Market/submit", params);
			
			JSONObject json = new JSONObject(jObj);
			result.setError(json.optInt("error", -1));
			result.setErrorMsg(json.optString("errormsg",
					AppContext.self.getString(R.string.unknown_error)));
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(AppContext.self.getString(R.string.unknown_error));
			result.setThrowable(e);
			ExceptionHandler.handleException(AppContext.self, result);
		}

		return result;

	}
	public static void operOrder(Callback<String> callback,final int orderno,final int oper){
		AsyncUtil.goAsync(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				long timestamp = Calendar.getInstance().getTimeInMillis();
				params.add(new BasicNameValuePair("order_no", String.valueOf(orderno)));
				params.add(new BasicNameValuePair("oper", ""+oper));
				params.add(new BasicNameValuePair("sign", UiTool.sign(timestamp,
						 String.valueOf(orderno))));
				String jObj = HttpUtil.post(URLs.URL_API_HOST
						+ "Market/operOrder", params);
				System.out.println("rows:" + jObj);
				return jObj;
			}
		}, callback);
	}
	
	
	public static Result<List<Store>> getStoreDetail(int storeId) {
		Result<List<Store>> result = new Result<List<Store>>();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			long timestamp = Calendar.getInstance().getTimeInMillis();
			params.add(new BasicNameValuePair("timestamp", String
					.valueOf(timestamp)));
			params.add(new BasicNameValuePair("storeId", storeId+""));
			params.add(new BasicNameValuePair("sign", UiTool.sign(timestamp,
					storeId+"")));
			//ELog.d(TAG, "getStoreDetail:req:" + params.toString());
			String jObj = HttpUtil.post(URLs.URL_API_HOST
					+ "Market/getStoreDetail", params);

			Type type = new TypeToken<List<Store>>() {
			}.getType();
			
			List<Store> ms = createGson().fromJson(jObj, type);
			result.setData(ms);
		} catch (Throwable e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(AppContext.self.getString(R.string.unknown_error));
			result.setThrowable(e);
			ExceptionHandler.handleException(AppContext.self, result);
		}

		return result;

	}

	public static void getMyArticle(Callback<String> callback, final int memberId,final int type,final int page) {
			AsyncUtil.goAsync(new Callable<String>() {

				@Override
				public String call() throws Exception {
					// TODO Auto-generated method stub
					List<NameValuePair> paramList = new ArrayList<NameValuePair>();
					long timestamp = Calendar.getInstance().getTimeInMillis();
					paramList.add(new BasicNameValuePair("memberId", String.valueOf(memberId)));
					paramList.add(new BasicNameValuePair("type", String.valueOf(type)));
					paramList.add(new BasicNameValuePair("page", String.valueOf(page)));
					String jObj = HttpUtil.post(URLs.URL_API_HOST + "Store/getArticle", paramList);
					return jObj;
				}
			}, callback);
			
		}

	
}
