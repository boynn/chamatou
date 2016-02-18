package cn.chamatou.biz.common;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * 解析JSON数据工具类
 * 
 * @ClassName : JSONHelper
 * @author : ryan
 * @date : 2013-8-22 下午4:02:46
 * 
 */
public class JSONHelper
{

    private static final Gson gson = new Gson();

    /**
     * 判断请求是否成功
     */
    public static boolean isSuccess(JsonCode jsonCode)
    {
        // jsonCode不为null且成功
        if (jsonCode != null && "0".equals(jsonCode.code))
        {
            return true;
        }
        return false;
    }

    /**
     * 判断请求是否成功
     * 
     * @throws SystemException
     */
    public static boolean isSuccess(String jsonStr)
    {
        JsonCode jsonCode = getJsonCode(jsonStr);
        // jsonCode不为null且成功
        if (jsonCode != null && "0".equals(jsonCode.code))
        {
            return true;
        }
        return false;
    }

    /**
     * 判断请求是否成功
     */
    public static boolean isFailure(JsonCode jsonCode)
    {
        // jsonCode不为null且成功
        if (jsonCode != null && "-1".equals(jsonCode.code))
        {
            return true;
        }
        return false;
    }

    /**
     * 获取jsonCode对象
     */
    public static JsonCode getJsonCode(String jsonStr)
    {
        JSONObject jsonObject;
        try
        {

            jsonObject = new JSONObject(jsonStr);

            JsonCode jsonCode = gson.fromJson(jsonObject.getString("jsonCode"),
                    JsonCode.class);
            // 判断t是否为null
            if (jsonCode != null)
            {
                return jsonCode;
            }
        }
        catch (Exception e)
        {
            
        }
        return null;
    }

    /**
     * 将JSON字符串转化为指定的Bean
     * @throws Exception 
     */
    public static <T> T getSingleBean(String jsonStr, String key, Class<T> cls) throws Exception
    {
        JSONObject jsonObject;
        try
        {
            jsonObject = new JSONObject(jsonStr);
            return gson.fromJson(jsonObject.optString(key), cls);
        }
        catch (Exception e)
        {
            throw new Exception();
            }
    }

    /**
     * 将JSON字符串转化为指定的Bean
     */
    public static <T> T getSingleBean(String jsonStr, Class<T> cls)
    {
        return gson.fromJson(jsonStr, cls);
    }

    /**
     * 将json转化为String
     * @throws Exception 
     */
    public static String getString(String jsonStr, String key) throws Exception
    {
        JSONObject jsonObject;
        try
        {
            jsonObject = new JSONObject(jsonStr);
            String json = jsonObject.optString(key);
            if (json != null)
            {
                return new String(json.getBytes(), "UTF-8");
            }
            else
            {
                return "";
            }
        }
        catch (Exception e)
        {
            throw new Exception();
         }
    }

    /**
     * 将JSON字符串转化为List
     * @throws Exception 
     */
    public static <T> List<T> getList(String jsonStr, String key, Class<T> cls,
            TypeToken<?> token) throws Exception
    {
        //JSONObject jsonObject;
        try
        {
            //jsonObject = new JSONObject(jsonStr);
        	GsonBuilder b = new GsonBuilder();
    		b.setDateFormat("yyyy-MM-dd HH:mm:ss");
    		return b.create().fromJson(jsonStr, token.getType());
    			
    		//System.out.println("json>>"+jsonStr);// 将JSON字符串转化为List
            //return gson.fromJson(jsonStr, token.getType());
        }
        catch (Exception e)
        {
            throw new Exception();
        }
    }

    /**
     * 将JSON字符串转化为List
     * @throws Exception 
     */
    public static <T> List<T> getList(String jsonStr, TypeToken<?> token) throws Exception
    {
        try
        {
            // 将JSON字符串转化为List
            return gson.fromJson(jsonStr, token.getType());
        }
        catch (Exception e)
        {
            throw new Exception();
        }
    }


    /**
     * 获取指定key对应的json字符串
     */
    public static String toJson(Object object)
    {
        if (object == null)
        {
            return null;
        }
        return gson.toJson(object);
    }
}