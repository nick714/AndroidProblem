package cn.thinker.wechatmomentsdemo.model.image;

import android.graphics.Bitmap;

import cn.thinker.wechatmomentsdemo.model.data.ImageItem;

public abstract class AbstractImageItemController {

    protected boolean mIsLoaded = false;
    protected ImageItem mImageItem = null;


    public boolean isLoaded(ImageItem item) {
        return mIsLoaded && mImageItem != null && mImageItem.equals(item);
    }

    public void setImageItem(ImageItem item) {
        mImageItem = item;
        mIsLoaded = false;
        onImageItemChanged(item.width, item.height);
    }

    public void setImageItemWithoutDealChange(ImageItem item) {
        mImageItem = item;
        mIsLoaded = false;
    }

    protected abstract void onImageItemChanged(int width, int height);

    public abstract void setBitmap(Bitmap bmp, boolean isAnimationNeeded, boolean isScaleNeeded);

    public ImageItem getImageItem() {
        return mImageItem;
    }
}
