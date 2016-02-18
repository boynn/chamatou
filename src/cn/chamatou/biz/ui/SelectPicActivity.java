package cn.chamatou.biz.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import cn.chamatou.biz.R;
import cn.chamatou.biz.common.Util;

/**
 * @ClassName: SelectPicActivity
 * @Description: 上传图片路径选择页面
 * @author 舒松
 * @date 2014-10-23 下午2:49:22
 * @version V1.0
 */
public class SelectPicActivity extends Activity implements OnClickListener
{
    private final static int CROP = 200;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/iBidder/Portrait/";
    private Uri origUri;
    private Uri cropUri;
    private String tempPath;// 临时路径
    private File tempFile;// 临时文件
    private String protraitPath;// 路径
    private File protraitFile;// 文件

    private Button camera_btn;
    private Button btnchoiceLocal_btn;
    private Button btn_cancel;
    private Intent intent;
    private Bundle bundle;
    private String type = "1";// 1矩形2正方形
    private boolean isFenMian = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_pic_layout);

        camera_btn = (Button) this.findViewById(R.id.btn_take_photos);
        btnchoiceLocal_btn = (Button) this
                .findViewById(R.id.btn_choice_local_picture);
        btn_cancel = (Button) this.findViewById(R.id.btn_cancel);

        camera_btn.setOnClickListener(this);
        btnchoiceLocal_btn.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        if (getIntent().getStringExtra("type") != null)
        {
            type = getIntent().getStringExtra("type");
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_take_photos:
                startActionCamera();
                break;
            case R.id.btn_choice_local_picture:
                startImagePick();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:
                finish();
                break;
        }
    }

    /**
     * 相机拍照
     * 
     * @param output
     */
    private void startActionCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, this.getCameraTempFile());
        startActivityForResult(intent, 1);
    }

    // 拍照保存的绝对路径
    @SuppressLint("SimpleDateFormat")
    private Uri getCameraTempFile()
    {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED))
        {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists())
            {
                savedir.mkdirs();
            }
        }
        else
        {
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        // 照片命名
        String cropFileName = "iBidder_camera_" + timeStamp + ".jpg";
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        tempPath = protraitPath;
        protraitFile = new File(protraitPath);
        tempFile = protraitFile;
        cropUri = Uri.fromFile(protraitFile);
        this.origUri = this.cropUri;
        return this.cropUri;
    }

    /**
     * 选择图片裁剪
     * 
     * @param output
     */
    private void startImagePick()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "选择图片"), 2);
    }

    /**
     * 拍照后裁剪
     * 
     * @param data
     *            原始图片
     * @param output
     *            裁剪后图片
     */
    private void startActionCrop(Uri data)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
        if (type.equals("1"))
        {
            intent.putExtra("aspectX", 4);// 裁剪框比例
            intent.putExtra("aspectY", 4);
            intent.putExtra("outputX", CROP);// 输出图片大小
            intent.putExtra("outputY", CROP);
        }
        else if (type.equals("2"))
        {
            intent.putExtra("aspectX", 5);// 裁剪框比例
            intent.putExtra("aspectY", 7);
            if (isFenMian)
            {
                intent.putExtra("outputX", 295);// 输出图片大小
                intent.putExtra("outputY", 413);
            }
            else
            {
                intent.putExtra("outputX", 300);// 输出图片大小
                intent.putExtra("outputY", 500);
            }

        }
        else
        {
            intent.putExtra("aspectX", 2);// 裁剪框比例
            intent.putExtra("aspectY", 3);
            intent.putExtra("outputX", 800);// 输出图片大小
            intent.putExtra("outputY", 1200);
        }
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        try
        {

            startActivityForResult(intent, 0);
        }
        catch (Error e)
        {
            // 返回数据
            System.out.println(e.toString());
        }
        catch (Exception e)
        {
            // 返回数据,将拍照获得的图片文件路径和文件赋给最终要上传的文件路径和文件
            protraitPath = tempPath;
            protraitFile = tempFile;
            backData();
            System.out.println(e);
        }

    }

    /**
     * 
     * @Description:返回数据
     */
    private void backData()
    {
        intent = new Intent();
        bundle = new Bundle();
        bundle.putString("protraitPath", protraitPath);
        bundle.putSerializable("protraitFile", protraitFile);
        bundle.putInt("showCurrentPage", 4);
        bundle.putInt("showIndexPage", 4);
        intent.putExtras(bundle);
        setResult(2, intent);
        finish();
    }

    // 裁剪头像的绝对路径
    @SuppressLint("SimpleDateFormat")
    private Uri getUploadTempFile(Uri uri)
    {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED))
        {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists())
            {
                savedir.mkdirs();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "无法保存上传的图片，请检查SD卡是否挂载",
                    Toast.LENGTH_SHORT).show();
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String thePath = Util.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (Util.isEmpty(thePath))
        {
            thePath = Util.getAbsoluteImagePath(SelectPicActivity.this, uri);
        }
        String ext = Util.getFileFormat(thePath);
        ext = Util.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "iBidder_thumb_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);

        cropUri = Uri.fromFile(protraitFile);
        return this.cropUri;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode)
        {
            case 1:
                startActionCrop(origUri);// 拍照后裁剪
                break;
            case 2:
                startActionCrop(data.getData());// 选图后裁剪
                break;
            case 0:

                // 返回数据
                backData();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
