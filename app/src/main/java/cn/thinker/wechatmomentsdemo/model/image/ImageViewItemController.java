package cn.thinker.wechatmomentsdemo.model.image;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;

import cn.thinker.wechatmomentsdemo.StaticApplication;
import cn.thinker.wechatmomentsdemo.common.BitmapUtils;

public class ImageViewItemController extends AbstractImageItemController {
    private static final int FADE_IN_TIME = 400;

    protected ImageView mImageView;

    private int mDefaultResId;

    public ImageViewItemController(ImageView view, int defaultRes) {
        mDefaultResId = defaultRes;
        mImageView = view;
    }

    @Override
    protected void onImageItemChanged(int width, int height) {
        if (mImageView != null) {
            Bitmap defaultBitmap = BitmapUtils.createNewBitmapWithResource
                    (StaticApplication.peekInstance().getResources(), mDefaultResId, new int[]{width, height}, true);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setImageBitmap(defaultBitmap);
        }
    }

    @Override
    public void setBitmap(Bitmap bmp, boolean isAnimationNeeded, boolean isScaleNeeded) {
        if (mImageView != null) {
            if (bmp != null) {
                if(isScaleNeeded) {
                    mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                if (isAnimationNeeded) {
                    showTransitionDrawable(mImageView, bmp);
                } else {
                    mImageView.setImageBitmap(bmp);
                }
                mIsLoaded = true;
            }
        }
    }

    private void showTransitionDrawable(ImageView v, Bitmap bitmap) {
        final TransitionDrawable td = new TransitionDrawable(
                new Drawable[] {
                        new ColorDrawable(
                                android.R.color.transparent),
                        new BitmapDrawable(StaticApplication.peekInstance().getResources(),
                                bitmap) });
        v.setImageDrawable(td);
        td.startTransition(FADE_IN_TIME);
    }
}
