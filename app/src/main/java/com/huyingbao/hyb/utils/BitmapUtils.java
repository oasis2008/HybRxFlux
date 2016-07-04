package com.huyingbao.hyb.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import org.apache.commons.io.FileUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/6/7.
 */
public class BitmapUtils {

    /**
     * 压缩成webp
     *
     * @param uriPath
     */
    public static String compressWebp(String uriPath) {
        try {
            File file = new File(uriPath);
            //byte[] bytes = compressBitmapToBytes(file.getPath(), 600, 0, 60, Bitmap.CompressFormat.JPEG);
            //分别是图片路径，宽度高度，质量，和图片类型，重点在这里。
            byte[] bytes1 = compressBitmapToBytes(file.getPath(), 600, 0, 60, Bitmap.CompressFormat.WEBP);
            File webp = new File(file.getParentFile(), file.getName().replaceAll("[.][^.]+$", "") + ".webp");
            FileUtils.writeByteArrayToFile(webp, bytes1);
            return webp.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * bitmap转成对应格式的byte[]
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @param quality
     * @param format
     * @return
     */
    public static byte[] compressBitmapToBytes(String filePath, int reqWidth, int reqHeight, int quality, Bitmap.CompressFormat format) {
        Bitmap bitmap = getSmallBitmap(filePath, reqWidth, reqHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, quality, baos);
        byte[] bytes = baos.toByteArray();
        bitmap.recycle();
        return bytes;
    }

    /**
     * 返回编码之后的bitmap
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        //计算压缩比例
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        //options.inPreferQualityOverSpeed = true;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算压缩比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int h = options.outHeight;
        int w = options.outWidth;
        int inSampleSize = 0;
        if (h > reqHeight || w > reqWidth) {
            float ratioW = (float) w / reqWidth;
            float ratioH = (float) h / reqHeight;
            inSampleSize = (int) Math.min(ratioH, ratioW);
        }
        inSampleSize = Math.max(1, inSampleSize);
        return inSampleSize;
    }

    /**
     * 写图片文件到SD卡
     *
     * @throws IOException
     */
    public static void saveImageToSD(String filePath, Bitmap bitmap, int quality) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
        }
    }

    /**
     * 将uri转成bitmap
     *
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    //                    bitmap = decodeUriAsBitmap(uri);// decode bitmap
    //                    int rotate = getImageOrientation(data.getData());
    //                    if (rotate != 0) {// 将图片旋转为垂直方向
    //                        Matrix matrix = new Matrix();
    //                        matrix.setRotate(rotate);
    //                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    //                    }

    /**
     * 得到图片方向Orientation
     *
     * @param context
     * @param uri
     * @return
     */
    public static int getImageOrientation(Context context, Uri uri) {
        String[] cols =
                {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(uri, cols, null, null, null);
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
}
