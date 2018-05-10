package cn.thinker.wechatmomentsdemo.common;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {
    public static Bitmap createNewBitmapWithResource(Resources res, int resID,
                                                     int wh[], boolean forceResize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = false;

        BitmapFactory.decodeResource(res, resID, options);
        int requestWidth = wh[0];
        int bmpWidth = options.outWidth;
        float inSampleSize = (float) bmpWidth / (float) requestWidth;
        if (inSampleSize > 1.0 && inSampleSize < 2.0) {
            options.inSampleSize = 2;
        } else if (inSampleSize >= 2.0) {
            options.inSampleSize = (int) inSampleSize;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(res, resID, options);

        return decodeWithFixDimension(requestWidth, wh[1], bitmap, options, forceResize);
    }

    public static Bitmap createNewBitmapAndCompressByFile(String filePath,
                                                          int wh[], boolean forceResize) {
        int offset = 100;
        File file = new File(filePath);
        long fileSize = 0;
        if (file.exists()) {
            fileSize = file.length();
            if (200 * 1024 < fileSize && fileSize <= 1024 * 1024)
                offset = 90;
            else if (1024 * 1024 < fileSize)
                offset = 85;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = false;

        BitmapFactory.decodeFile(filePath, options);
        Bitmap bitmap = decodeWithFixDimension(wh[0], wh[1], file, options,
                forceResize);
        if (bitmap != null) {
            bitmap = compressBitmapFile(offset, fileSize, bitmap);
        }
        return bitmap;
    }

    public static Bitmap createNewBitmapAndCompressByByteArray(byte[] array,
                                                               int wh[], boolean forceResize) {
        int offset = 100;
        long fileSize = array.length;
        if (200 * 1024 < fileSize && fileSize <= 1024 * 1024)
            offset = 90;
        else if (1024 * 1024 < fileSize)
            offset = 85;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = false;
        BitmapFactory.decodeByteArray(array, 0, array.length, options);
        Bitmap bitmap = decodeByteArrayWithFixDimension(wh[0], wh[1], array,
                options, forceResize);
        if (bitmap != null) {
            bitmap = compressBitmapFile(offset, fileSize, bitmap);
        }
        return bitmap;
    }

    private static Bitmap compressBitmapFile(int offset, long fileSize,
                                             Bitmap bitmap) {
        if (offset == 100)
            return bitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, offset, baos);
        byte[] buffer = baos.toByteArray();
        if (buffer.length >= fileSize)
            return bitmap;
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
    }

    private static Bitmap decodeWithFixDimension(int width, int height,
                                                 Bitmap bmp, BitmapFactory.Options options, boolean forceResize) {
        int bmpWidth = options.outWidth;
        float inSampleSize = (float) bmpWidth / (float) width;
        if (inSampleSize > 1.0 && inSampleSize < 2.0) {
            options.inSampleSize = 2;
        } else if (inSampleSize >= 2.0) {
            options.inSampleSize = (int) inSampleSize;
        } else {
            options.inSampleSize = 1;
        }
        options.inJustDecodeBounds = false;

        Bitmap bitmap = bmp;
        try {
            if (bitmap != null) {
                float scale = ((float) width / options.outWidth);
                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);
                if (scale < 1.0 || forceResize
                        || (inSampleSize > 1.0 && inSampleSize < 2.0)) {
                    int subWidth = options.outWidth > bitmap.getWidth() ? bitmap.getWidth() : options.outWidth;
                    int subHeight = options.outWidth > bitmap.getHeight() ? bitmap.getHeight() : options.outWidth;
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            subWidth, subHeight, matrix, true);
                }
                if (height != -1 && bitmap.getHeight() > height) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), height);
                }
            }
        } catch (OutOfMemoryError e) {
            System.gc();
            bitmap = null;
        }
        return bitmap;
    }

    private static Bitmap decodeWithFixDimension(int width, int height,
                                                 InputStream is, BitmapFactory.Options options, boolean forceResize) {
        int bmpWidth = options.outWidth;
        float inSampleSize = (float) bmpWidth / (float) width;
        if (inSampleSize > 1.0 && inSampleSize < 2.0) {
            options.inSampleSize = 2;// set scale factor
        } else if (inSampleSize >= 2.0) {
            options.inSampleSize = (int) inSampleSize;
        } else {
            options.inSampleSize = 1;
        }
        options.inJustDecodeBounds = false;

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(is, null, options);
            if (bitmap != null) {
                float scale = ((float) width / options.outWidth);
                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);
                // there are three situations to execute force scale；
                // 1.requested size is smaller.
                // 2.explicit force scale.
                // 3.To reduce image quality, we scale the image lower than the requested size, now we need to scale it back.
                if (scale < 1.0 || forceResize
                        || (inSampleSize > 1.0 && inSampleSize < 2.0)) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            options.outWidth, options.outHeight, matrix, true);
                }
                if (height != -1 && bitmap.getHeight() > height) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), height);
                }
            }
        } catch (OutOfMemoryError e) {
            System.gc();
            bitmap = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    private static Bitmap decodeWithFixDimension(int width, int height,
                                                 File file, BitmapFactory.Options options, boolean forceResize) {
        try {
            InputStream is = new FileInputStream(file);
            return decodeWithFixDimension(width, height, is, options,
                    forceResize);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private static Bitmap decodeByteArrayWithFixDimension(int width,
                                                          int height, byte[] array, BitmapFactory.Options options,
                                                          boolean forceResize) {
        int bmpWidth = options.outWidth;
        float inSampleSize = (float) bmpWidth / (float) width;
        if (inSampleSize > 1.0 && inSampleSize < 2.0) {
            options.inSampleSize = 2;
        } else if (inSampleSize >= 2.0) {
            options.inSampleSize = (int) inSampleSize;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeByteArray(array, 0, array.length,
                    options);
            if (bitmap != null) {
                float scale = ((float) width / options.outWidth);
                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);
                // there are three situations to execute force scale；
                // 1.requested size is smaller.
                // 2.explicit force scale.
                // 3.To reduce image quality, we scale the image lower than the requested size, now we need to scale it back.
                if (scale < 1.0 || forceResize
                        || (inSampleSize > 1.0 && inSampleSize < 2.0)) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            options.outWidth, options.outHeight, matrix, true);
                }
                if (height != -1 && bitmap.getHeight() > height) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), height);
                }
            }
        } catch (OutOfMemoryError e) {
            System.gc();
            bitmap = null;
        }
        return bitmap;
    }
}
