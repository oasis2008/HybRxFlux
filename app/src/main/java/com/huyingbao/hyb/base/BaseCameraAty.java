package com.huyingbao.hyb.base;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.huyingbao.hyb.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class BaseCameraAty extends BaseActivity {
    /***
     * 拍照选图
     ***/
    private static final int REQUEST_CODE_CAMERA = 1101;
    /***
     * 系统相册选图
     ***/
    private static final int REQUEST_CODE_ALBUM = 1102;
    /***
     * 从系统裁剪界面返回的requestcode
     ***/
    private static final int REQUEST_CODE_CROP = 1103;
    /***
     * 从superphoto选图返回的requestcode,子类复写startAlbum()跳转到superPhoto选图,主要是单选图片,截图上传
     ***/
    protected static final int REQUEST_CODE_SUPER_PHOTO_HEAD = 1104;
    BaseDialog mBaseDialog;

    Uri mImageUri = null;

    /**
     * 裁剪图片所有参数，为null表示普通拍照选图
     */
    protected CropOption mCropOption;

    /**
     * 默认选择菜单,子类调用使用
     */
    protected void showDefaultCameraMenu() {
        if (mBaseDialog == null) {
            mBaseDialog = DialogFactory.createBottomDialog(this);
            View view = LayoutInflater.from(this).inflate(R.layout.view_popup_menu, null);
            OnClickListener listener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.v_popup_btn1) {
                        startCamera();
                    } else if (v.getId() == R.id.v_popup_btn2) {
                        startAlbum();
                    }
                    mBaseDialog.dismiss();
                }
            };
            view.findViewById(R.id.v_popup_btn1).setOnClickListener(listener);
            view.findViewById(R.id.v_popup_btn2).setOnClickListener(listener);
            view.findViewById(R.id.v_popup_btnDismiss).setOnClickListener(listener);
            mBaseDialog.setContentView(view);
        }
        mBaseDialog.show();
    }

    /**
     * 初始化图片缓存(mImageUri及对应file) 用于 拍照(截图与非截图),相册(截图)
     */
    private void initImageUri() {
        File photoFile = new File(WCApplication.getImageFilePath(this) + "temp.jpg");
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
     * 相册选图,默认从系统相册选图 子类若是需要自定义选图需要重写该方法
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
        Bitmap bitmap = null;
        switch (requestCode) {
            // 系统拍照选图
            case REQUEST_CODE_CAMERA:
                if (mCropOption == null) {// 普通选图
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 有sd卡,拍照的图片已经缓存到mImageUri
                        bitmap = decodeUriAsBitmap(mImageUri);// decode bitmap
//					rotateImg(bitmap, mImageUri);
                        onReceiveBitmap(mImageUri, bitmap);// 操作完成后调用此方法
                    } else {// 无内存卡,没有图片缓存路径mImageUri
                        Uri uri = handleUri(data);
                        bitmap = decodeUriAsBitmap(uri);
                        onReceiveBitmap(uri, bitmap);
                    }
                } else {// 裁剪图片
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 有sd卡,拍照的图片已经缓存到mImageUri
                        cropImage(mImageUri);
                    } else {// 无内存卡,没有图片缓存路径mImageUri
                        Uri uri = handleUri(data);
                        cropImage(uri);
                    }
                }
                break;
            // 系统相册选图
            case REQUEST_CODE_ALBUM:
                if (mCropOption == null) {// 普通选图(只是把图片返回就可以,不需要Uri)
                    Uri uri = handleUri(data);
                    bitmap = decodeUriAsBitmap(uri);// decode bitmap
                    int rotate = getImageOrientation(data.getData());
                    if (rotate != 0) {// 将图片旋转为垂直方向
                        Matrix matrix = new Matrix();
                        matrix.setRotate(rotate);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    }
                    onReceiveBitmap(uri, bitmap);// 操作完成后调用此方法
                } else {// 裁剪图片
                    Uri uri = handleUri(data);
                    cropImage(uri);
                }

                break;
            // 从系统裁剪图片返回
            case REQUEST_CODE_CROP:
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 有sd卡,已经设置图片缓存路径mImageUri
                    bitmap = decodeUriAsBitmap(mImageUri);// decode bitmap
                    onReceiveBitmap(mImageUri, bitmap);// 操作完成后调用此方法
                } else {// 无内存卡,没有图片缓存路径mImageUri
                    Uri uri = handleUri(data);
                    bitmap = decodeUriAsBitmap(uri);
                    onReceiveBitmap(uri, bitmap);
                }
                break;
            // 自定义裁剪图片
            case Crop.REQUEST_CROP:// 裁剪图片
                Uri uri = Crop.getOutput(data);
                bitmap = decodeUriAsBitmap(uri);
                onReceiveBitmap(uri, bitmap);
                break;
        }

    }

    /**
     * 裁剪图片
     */
    private void cropImage(Uri uri) {
        // [start]调用自定义截图 传递Uri到截图界面截图
        String cropName;
        if (mCropOption != null && !mCropOption.cropName.isEmpty()) {
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
                // Crop.of(uri,
                // destination).withMaxSize(mCropOption.outputX,mCropOption.outputY).start(this);
                Crop.of(uri, destination).withAspect(mCropOption.outputX, mCropOption.outputY).start(this);
            } else {
                Crop.of(uri, destination).withAspect(mCropOption.aspectX, mCropOption.aspectY).start(this);
            }
        }
        // [end]
    }

    /**
     * 处理返回的 intent(携带Uri或者Bitmap)
     *
     * @param data
     * @return 1:uri!=null则返回 ,2:uri==null有bitmap,存储bitmap到指定路径,并返回对应uri
     */
    private Uri handleUri(Intent data) {
        // TODO Auto-generated method stub
        Uri uri = data.getData();
        if (uri != null) {
            return uri;
        }
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return uri;
        }
        String path = WCApplication.getImageFilePath(this) + "temp.jpg";
        try {
            // 保存bitmap到指定路径
            ImageHelper.saveImageToSD(path, (Bitmap) bundle.get("data"), 100);
            return uri = Uri.fromFile(new File(path));
        } catch (IOException e) {
            return uri;
        }

    }

    /**
     * 得到图片Orientation
     *
     * @param uri
     * @return
     */
    private int getImageOrientation(Uri uri) {
        String[] cols =
                {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = getContentResolver().query(uri, cols, null, null, null);
        int rotate = 0;
        if (cursor != null) {
            cursor.moveToPosition(0);
            rotate = cursor.getInt(0);
            cursor.close();
        }
        return rotate;
    }

    /**
     * Gets the content:// URI from the given corresponding path to a file
     * 从给定的相应路径文件取得content://uri
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]
                {MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ", new String[]
                {filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 将uri转成bitmap
     *
     * @param uri
     * @return
     */
    protected Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 拍照或选择图片后，回调此方法
     *
     * @param uri
     * @param bitmap
     */
    protected abstract void onReceiveBitmap(Uri uri, Bitmap bitmap);

    class CropOption {
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
