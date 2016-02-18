package cn.chamatou.biz.adapter;

import java.util.ArrayList;

import cn.chamatou.biz.bean.URLs;
import cn.chamatou.biz.common.BitmapManager;
import cn.chamatou.biz.common.StringUtils;
import cn.chamatou.biz.widget.PhotosGallery;

import cn.chamatou.biz.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

public class GImageAdapter extends BaseAdapter {   
    private ArrayList<String> gImgUrl = new ArrayList<String>();
    private ImageView[] mImages;
	private BitmapManager	bmpManager;
	private Context mContext;   
        public GImageAdapter(Context context) {   
        mContext = context;   
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_dface_loading));
   }   
  
    public int getCount() {  
        return gImgUrl.size();   
   }   
  
    public Object getItem(int position) {   
        return position;   
    }   

   public long getItemId(int position) {   
       return position;   
    }   
   public boolean createReflectedImages(ArrayList<String> tmpGImages) {
       gImgUrl = tmpGImages;
       mImages = new ImageView[gImgUrl.size()];
       int index = gImgUrl.size()-1;
       for (int i =0; i < gImgUrl.size(); ++i ) {
           ImageView imageView = new ImageView(mContext);
           if(gImgUrl.get(i).endsWith("portrait.gif") || StringUtils.isEmpty(gImgUrl.get(i))){
           		imageView.setImageResource(R.drawable.widget_dface);
   			}else{
   				bmpManager.loadBitmap(gImgUrl.get(i), imageView);
   			}
           imageView.setAdjustViewBounds(true);   
           imageView.setLayoutParams(new PhotosGallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));   
           mImages[index--] = imageView;
       }
       return true;
   }
   public View getView(int position, View convertView, ViewGroup parent) {   
	   return mImages[position];
    }   
}  