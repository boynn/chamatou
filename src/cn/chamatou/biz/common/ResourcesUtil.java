package cn.chamatou.biz.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;





import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.R;
import cn.chamatou.biz.bean.URLs;

import android.graphics.drawable.Drawable;

/**
 * @ClassName: ResourcesUtil
 * @Description: 资源获取工具类
 * @author 李星江
 * @date 2014-9-18 下午1:30:14
 * @version V1.0
 */
public class ResourcesUtil
{

    // 获得res文件下字符串资源
    public static String getStringResourceAsId(int id)
    {
        return AppContext.context.getResources().getString(id);
    }

    // 获得res文件下集合资源
    public static String[] getStringsResourceAsId(int id)
    {
        return AppContext.context.getResources().getStringArray(id);
    }

    // 获得res文件下颜色资源
    public static Integer getColorResourceAsId(int id)
    {
        return AppContext.context.getResources().getColor(id);
    }

    // 获得res文件下图片资源
    public static Drawable getDrawableResourceAsId(int id)
    {
        return AppContext.context.getResources().getDrawable(id);
    }

    // 获得assert文件下的资源（不编译）
    public static InputStream getAssertStreamAsFileName(String filename)
            throws IOException
    {
        return AppContext.context.getAssets().open(filename);
    }

    // 获得res/raw文件下的资源
    public static InputStream getRawResourceAsId(int id)
    {
        return AppContext.context.getResources().openRawResource(id);
    }

    /**
     * 获取请求URL地址
     */
    public static String getUrlAskey(int key)
    {
        return getUri(R.string.server_uri) + getUri(key);
    }

    private static String getUri(int key)
    {
        if (key == 0)
        {
            return "";
        }
        return AppContext.context.getResources().getString(key);
    }

    /**
     * 获取图片请求路径
     */
    public static String getPicture(String imageName)
    {
        return getUri(R.string.server_uri) + File.separator + imageName;
    }

}
