package cn.chamatou.biz.common;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import cn.chamatou.biz.R;

/**
 * @author shiner
 */
public class ImageLoaderUtil {

	public static DisplayImageOptions createDetailOption() {
		return new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.NONE).build();
	}

	public static DisplayImageOptions createNormalOption() {
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.widget_dface)
				.showImageForEmptyUri(R.drawable.widget_dface)
				.showImageOnFail(R.drawable.widget_dface).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	public static DisplayImageOptions createNormalOption(int onLoadingImg,int imageForEmptyUri,int imageOnFail) {
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(onLoadingImg)
				.showImageForEmptyUri(imageForEmptyUri)
				.showImageOnFail(imageOnFail).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

}
