package com.huyingbao.hyb.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.huyingbao.hyb.R;
import com.huyingbao.hyb.utils.BitmapUtils;
import com.huyingbao.hyb.utils.DevUtils;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;

public abstract class BaseCameraAty extends BaseActivity {
    /**
     * 拍照选图
     */
    private static final int REQUEST_CODE_CAMERA = 1101;
    /**
     * 系统相册选图
     */
    private static final int REQUEST_CODE_ALBUM = 1102;
    /**
     * 裁剪图片所有参数，为null表示普通拍照选图
     */
    protected CropOption mCropOption;
    BaseDialog mBaseDialog;
    /**
     * 拍照存储路径
     */
    Uri mImageUri = null;

    /**
     * 默认选择菜单,子类调用使用
     */
    protected void showDefaultCameraMenu() {
        if (mBaseDialog == null) {
            mBaseDialog = DialogFactory.createBottomDialog(this);
            OnClickListener listener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBaseDialog.dismiss();
                    switch (v.getId()) {
                        case R.id.bt_camera:
                            startCamera();
                            break;
                        case R.id.bt_album:
                            startAlbum();
                            break;
                        default:
                            break;
                    }
                }
            };
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_camera, null);
            view.findViewById(R.id.bt_camera).setOnClickListener(listener);
            view.findViewById(R.id.bt_album).setOnClickListener(listener);
            view.findViewById(R.id.bt_cancel).setOnClickListener(listener);
            mBaseDialog.setContentView(view);
        }
        mBaseDialog.show();
    }

    /**
     * 初始化拍照图片缓存(mImageUri及对应file) 用于 拍照(截图与非截图)
     */
    private void initImageUri() {
        File photoFile = new File(DevUtils.getImageFilePath(this) + "temp.jpg");
        if (!photoFile.getParentFile().exists()) {
            photoFile.getParentFile().mkdirs();
        }
        try {
            photoFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mImageUri = Uri.fromFile(photoFile);
    }

    /**
     * 启动相机拍照
     */
    protected void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 如果有SD卡,设置拍照之后图片存储路径mImageUri
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            initImageUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        }
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 相册选图,默认从系统相册选图
     */
    protected void startAlbum() {
        Intent intent = new Intent();
        if (android.os.Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            // 系统拍照选图
            case REQUEST_CODE_CAMERA:
                if (mCropOption == null) {// 普通选图
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 有sd卡,拍照的图片已经缓存到mImageUri
                        onReceiveBitmap(mImageUri);// 操作完成后调用此方法
                    } else {// 无内存卡,拍照的图片将会自动存储到系统空间,需要从返回的data中获取存储路径
                        // 无内存卡,图片不会缓存路径mImageUri
                        Uri uri = handleUri(data);
                        onReceiveBitmap(uri);
                    }
                } else {// 裁剪图片
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 有sd卡,拍照的图片已经缓存到mImageUri
                        cropImage(mImageUri);
                    } else {// 无内存卡,拍照的图片将会自动存储到系统空间,需要从返回的data中获取存储路径
                        // 无内存卡,图片不会缓存路径mImageUri
                        Uri uri = handleUri(data);
                        cropImage(uri);
                    }
                }
                break;
            // 系统相册选图
            case REQUEST_CODE_ALBUM:
                if (mCropOption == null) {// 普通选图(只是把图片返回就可以,不需要Uri)
                    Uri uri = handleUri(data);
                    onReceiveBitmap(uri);// 操作完成后调用此方法
                } else {// 裁剪图片
                    Uri uri = handleUri(data);
                    cropImage(uri);
                }
                break;
            // 自定义裁剪图片
            case Crop.REQUEST_CROP:// 裁剪图片
                Uri uri = Crop.getOutput(data);
                onReceiveBitmap(uri);
                break;
        }
    }

    /**
     * 裁剪图片
     */
    private void cropImage(Uri uri) {
        //调用自定义截图 传递Uri到截图界面截图
        String cropName;
        if (mCropOption != null && mCropOption.cropName != null && !mCropOption.cropName.isEmpty()) {
            cropName = mCropOption.cropName;
        } else {
            cropName = "cropped.jpg";
        }
        Uri destination = Uri.fromFile(new File(getCacheDir(), cropName));
        // 创建一个intent,传入uri,跳转到截图activity,requestCode=Crop.REQUEST_CROP
        if (mCropOption == null) {
            Crop.of(uri, destination).asSquare().start(this);
        } else {
            if (mCropOption.outputX > 0 && mCropOption.outputY > 0) {
                // Crop.of(uri, destination).withMaxSize(mCropOption.outputX,mCropOption.outputY).start(this);
                Crop.of(uri, destination).withAspect(mCropOption.outputX, mCropOption.outputY).start(this);
            } else {
                Crop.of(uri, destination).withAspect(mCropOption.aspectX, mCropOption.aspectY).start(this);
            }
        }
    }

    /**
     * 处理返回的 intent(携带Uri或者Bitmap)
     *
     * @param data
     * @return 1:uri!=null则返回 ,2:uri==null有bitmap,存储bitmap到指定路径,并返回对应uri
     */
    private Uri handleUri(Intent data) {
        Uri uri = data.getData();
        if (uri != null) {
            return uri;
        }
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return uri;
        }
        String path = DevUtils.getImageFilePath(this) + "temp.jpg";
        try {
            // 保存bitmap到指定路径
            BitmapUtils.saveImageToSD(path, (Bitmap) bundle.get("data"), 100);
            return uri = Uri.fromFile(new File(path));
        } catch (IOException e) {
            return uri;
        }

    }

    /**
     * 拍照或选择图片后，回调此方法
     *
     * @param uri
     */
    protected abstract void onReceiveBitmap(Uri uri);

    public class CropOption {
        /**
         * 截图比例X
         */
        public int aspectX = 1;
        /**
         * 截图比例Y
         */
        public int aspectY = 1;
        /**
         * 截图输出宽度
         */
        public int outputX;
        /**
         * 截图输出高度
         */
        public int outputY;

        /**
         * 裁剪之后的图片命名,默认是null
         */
        public String cropName;
    }
}
