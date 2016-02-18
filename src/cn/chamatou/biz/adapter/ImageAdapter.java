package cn.chamatou.biz.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

import cn.chamatou.biz.widget.PhotosGallery;

public class ImageAdapter extends BaseAdapter {   
	private Integer[] mps;
	private Context mContext;   
        public ImageAdapter(Context context) {   
        mContext = context;   
    }   
  
    public int getCount() {    
        return mps.length;   
   }   
  
    public Object getItem(int position) {   
        return position;   
    }   

   public long getItemId(int position) {   
       return position;   
    }   
    public View getView(int position, View convertView, ViewGroup parent) {   
        ImageView image = new ImageView(mContext);   
        image.setImageResource(mps[position]);   
        image.setAdjustViewBounds(true);   
        image.setLayoutParams(new PhotosGallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));   
        return image;   
    }   
}  