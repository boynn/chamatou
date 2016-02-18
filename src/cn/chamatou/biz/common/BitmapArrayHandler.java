package cn.chamatou.biz.common;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public abstract class BitmapArrayHandler extends Handler {

	private List<byte[]> bsList=new ArrayList<byte[]>();

	public abstract void handle(List<Bitmap> bitmapList);

	@Override
	public void handleMessage(Message msg) {
		List<Bitmap> bitmapList = new ArrayList<Bitmap>();
		for (byte[] bs : bsList) {
			bitmapList.add(BitmapFactory.decodeByteArray(bs, 0, bs.length));
		}
		handle(bitmapList);
	}

	public void add(byte[] bs) {
		bsList.add(bs);
	}
}
