package cn.chamatou.biz.ui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 图片裁剪
 */
public abstract class GetPhotosUtils {
	private int[] types; // 1表示图片,2表示拍照,3表示图片和拍照
	private Activity activity;
	private int[] imageBtnIds;

	public void onCreate(Activity activity) {
		this.activity = activity;
		// 初始化
		init();
	}


	/**
	 * 初始化方法实现
	 */
	private void init() {
		imageBtnIds = getImageBtnId();
		types = getImageBtnType();
		getImageBtnType();
		int size = imageBtnIds.length;
		for(int i=0;i<size;i++){
			ImageView imageBtn = (ImageView) activity.findViewById(imageBtnIds[i]);
			imageBtn.setTag(types[i]);
			imageBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch ((Integer)v.getTag()) {
					case 1:
						choosePic();
						break;
					case 2:
						takePhone();
						break;
					case 3:
						ShowPickDialog() ;
						break;
					default:
						break;
					}
				}
			});
		}
	}

	public abstract int[] getImageBtnId();

	public abstract int[] getImageBtnType();
	/**
	 * 选择提示对话框
	 */
	private void ShowPickDialog() {
		new AlertDialog.Builder(activity)
		.setTitle("设置图片...")
		.setNegativeButton("相册", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				choosePic();
			}
		})
		.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				takePhone();
			}
		}).show();
	}

	private void choosePic() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		activity.startActivityForResult(intent, 1);
	}
	
	private void takePhone() {
		Intent intent = new Intent(
				MediaStore.ACTION_IMAGE_CAPTURE);
		// 下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
				.fromFile(new File(Environment
						.getExternalStorageDirectory(),
						"header.jpg")));
		activity.startActivityForResult(intent, 2);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			if (data != null) {
			startPhotoZoom(data.getData());
			}
			break;
			// 如果是调用相机拍照时
		case 2:
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/header.jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
			// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
			 * 
			 */
//			if (data != null) {
//				setPicToView(data);
//			}
			break;
		default:
			break;

		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
//			/**
//			 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
//			 */
//			photo = toRoundBitmap(photo);
//			Drawable drawable = new BitmapDrawable(photo);
			 
		}
	}

	public abstract void uploadImage(Drawable drawable);

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}
}