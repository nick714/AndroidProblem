package cn.thinker.wechatmomentsdemo.core.request;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.thinker.wechatmomentsdemo.common.BitmapUtils;
import cn.thinker.wechatmomentsdemo.model.IDataOperation;
import cn.thinker.wechatmomentsdemo.model.WCMDataError;
import cn.thinker.wechatmomentsdemo.model.data.ImageItem;
import cn.thinker.wechatmomentsdemo.model.image.BitmapHolder;
import cn.thinker.wechatmomentsdemo.model.image.ImageItemInfoHelper;

public class ExtendImageRequest extends Request<BitmapHolder> {
    // Socket timeout in milliseconds for image requests
    private static final int IMAGE_TIMEOUT_MS = 1000;

    // Default number of retries for image requests
    private static final int IMAGE_MAX_RETRIES = 2;

    // Default backoff multiplier for image requests
    private static final float IMAGE_BACKOFF_MULT = 2f;

    private final Response.Listener<BitmapHolder> mListener;
    private final Config mDecodeConfig;
    private final ImageItem mImageItem;
    protected BitmapHolder mHolder;

    /** Decoding lock so that we don't decode more than one image at a time (to avoid OOM's) */
    private static final Object sDecodeLock = new Object();

    private static final String TAG = "ExtendImageRequest";

    /**
     * Creates a new image request, decoding to a maximum specified width and
     * height. If both width and height are zero, the image will be decoded to
     * its natural size. If one of the two is nonzero, that dimension will be
     * clamped and the other one will be set to preserve the image's aspect
     * ratio. If both width and height are nonzero, the image will be decoded to
     * be fit in the rectangle of dimensions width x height while keeping its
     * aspect ratio.
     *
     * @param url URL of the image
     * @param listener Listener to receive the decoded bitmap
     * @param holder container the holds the result bitmap.
     * @param decodeConfig Format to decodIDataOeratione the bitmap to
     * @param errorListener Error listener, or null to ignore errors
     */
    public ExtendImageRequest(String url, Response.Listener<BitmapHolder> listener, BitmapHolder holder,
                              Config decodeConfig, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        setRetryPolicy(
                new DefaultRetryPolicy(IMAGE_TIMEOUT_MS, IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
        mListener = listener;
        mDecodeConfig = decodeConfig;
        mHolder = holder;
        mImageItem = mHolder.getParam().getData().getParcelable(IDataOperation.BUNDLE_KEY_PARAM_IMAGE_REQUEST_ITEM);
    }

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }

    /**
     * Scales one side of a rectangle to fit aspect ratio.
     *
     * @param maxPrimary Maximum size of the primary dimension (i.e. width for
     *        max width), or zero to maintain aspect ratio with secondary
     *        dimension
     * @param maxSecondary Maximum size of the secondary dimension, or zero to
     *        maintain aspect ratio with primary dimension
     * @param actualPrimary Actual size of the primary dimension
     * @param actualSecondary Actual size of the secondary dimension
     */
    @SuppressWarnings("unused")
    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary,
                                           int actualSecondary) {
        // If no dominant value at all, just return the actual.
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    @Override
    protected Response<BitmapHolder> parseNetworkResponse(NetworkResponse response) {
        saveImageToPath(response);
        // Serialize all decode on a global lock to reduce concurrent heap usage.
        synchronized (sDecodeLock) {
            try {
                return doParse(response);
            } catch (OutOfMemoryError e) {
                VolleyLog.e("Caught OOM for %d byte image, url=%s", response.data.length, getUrl());
                return Response.error(new ParseError(e));
            }
        }
    }

    private void saveImageToPath(NetworkResponse response) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(ImageItemInfoHelper.getImageSavedPath(mImageItem));
            fos.write(response.data);
            fos.flush();
        } catch (FileNotFoundException e1) {
            Log.e(TAG, "can not save target image file : " + mImageItem.url + " file not found");
            e1.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "can not save target image file : " + mImageItem.url + " write error");
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * The real guts of parseNetworkResponse. Broken out for readability.
     */
    private Response<BitmapHolder> doParse(NetworkResponse response) {
        byte[] data = response.data;
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap bitmap;
        if (mImageItem.width == 0 && mImageItem.height == 0) {
            decodeOptions.inPreferredConfig = mDecodeConfig;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
        } else {
            bitmap = BitmapUtils.createNewBitmapAndCompressByByteArray(data, new int[]{mImageItem.width, mImageItem.height}, true);
        }

        if (bitmap == null) {
            return Response.error(new WCMDataError(response, mHolder));
        } else {
            mHolder.setBitmap(bitmap);
            return Response.success(mHolder, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

    @Override
    protected void deliverResponse(BitmapHolder response) {
        mListener.onResponse(response);
    }
}
