package cn.chamatou.biz.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public abstract class BitmapHandler extends Handler{
	
	public abstract void handle(Bitmap bitmap);

	@Override
	public void handleMessage(Message msg) {
		Bundle bundle = msg.getData();
		byte[] bs = bundle.getByteArray("_BITMAP");
		
		Bitmap bitmap=BitmapFactory.decodeByteArray(bs, 0, bs.length);
		handle(bitmap);
		super.handleMessage(msg);
	}
}
