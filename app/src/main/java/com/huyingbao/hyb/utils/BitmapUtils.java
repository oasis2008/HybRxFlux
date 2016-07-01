package com.huyingbao.hyb.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.apache.commons.io.FileUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/6/7.
 */
public class BitmapUtils {
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/test/";

    /**
     * 压缩成webp
     *
     * @param imgName
     */
    private void compressWebp(String imgName) {
        try {
            File file = new File(path, imgName);
//            byte[] bytes = compressBitmapToBytes(file.getPath(), 600, 0, 60, Bitmap.CompressFormat.JPEG);
//            File jpg = new File(path, imgName + "compress.jpg");
//            FileUtils.writeByteArrayToFile(jpg, bytes);

            byte[] bytes1 = compressBitmapToBytes(file.getPath(), 600, 0, 60, Bitmap.CompressFormat.WEBP);//分别是图片路径，宽度高度，质量，和图片类型，重点在这里。
            File webp = new File(path, imgName + "compress.webp");
            FileUtils.writeByteArrayToFile(webp, bytes1);
        } catch (IOException e) {
            e.printStackTrace();
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
}
