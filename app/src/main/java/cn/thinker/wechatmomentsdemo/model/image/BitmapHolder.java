package cn.thinker.wechatmomentsdemo.model.image;

import android.graphics.Bitmap;
import android.os.Message;

import cn.thinker.wechatmomentsdemo.common.ParcelablePoolObject;

public class BitmapHolder {
    private Bitmap mBitmap;
    private int mMessageType;

    private ParcelablePoolObject mOrigParam;

    public BitmapHolder(Message msg, Bitmap bitmap) {
        setBitmap(bitmap);
        setData(msg);
    }

    public BitmapHolder(ParcelablePoolObject param, int messageType, Bitmap bitmap) {
        setBitmap(bitmap);
        mMessageType = messageType;
        mOrigParam = param;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public ParcelablePoolObject getParam() {
        return mOrigParam;
    }

    public int getMessageType() {
        return mMessageType;
    }

    private void setData(Message mData) {
        this.mOrigParam = (ParcelablePoolObject) mData.obj;
        this.mMessageType = mData.what;
    }
}
