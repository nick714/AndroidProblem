package cn.thinker.wechatmomentsdemo.model;

import android.graphics.Bitmap;
import android.os.Message;

import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

import cn.thinker.wechatmomentsdemo.StaticApplication;
import cn.thinker.wechatmomentsdemo.common.OkHttpStack;
import cn.thinker.wechatmomentsdemo.common.ParcelableObjectPool;
import cn.thinker.wechatmomentsdemo.common.ParcelablePoolObject;
import cn.thinker.wechatmomentsdemo.common.ParcelablePoolObjectFactory;
import cn.thinker.wechatmomentsdemo.controller.IViewController;
import cn.thinker.wechatmomentsdemo.core.DataExecutors;
import cn.thinker.wechatmomentsdemo.model.data.ImageItem;

public class Model implements  IDataOperation{

    private static final int MAX_BUNDLE_FACTORY_SIZE = 20;

    private static Model INSTANCE;
    private ParcelableObjectPool mBundlePool;
    private ImageModelHelper mImageModelHelper;
    private DataModelHelper mDataModelHelper;

    public static Model peekInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Model(StaticApplication.peekInstance());
        }
        return INSTANCE;
    }

    private Model(StaticApplication app) {
        mBundlePool = new ParcelableObjectPool(new ParcelablePoolObjectFactory(), MAX_BUNDLE_FACTORY_SIZE);
        DataExecutors executors = new DataExecutors(Volley.newRequestQueue(app, new OkHttpStack(new OkHttpClient())));
        mImageModelHelper = new ImageModelHelper(executors);
        mDataModelHelper = new DataModelHelper(executors);
    }

    @Override
    public void requestDataOperation(IViewController target, Message msg) {
        if (msg.what == IDataOperation.REQUEST_TYPE_GET_IMAGE_BITMAP ||
                msg.what == IDataOperation.REQUEST_TYPE_DECODE_IMAGE_BITMAP) {
            mImageModelHelper.requestDataOperation(target, msg);
        } else {
            mDataModelHelper.requestDataOperation(target, msg);
        }
    }

    @Override
    public void cancelDataOperation(IViewController target, Message msg) {
        if (msg.what == IDataOperation.REQUEST_TYPE_GET_IMAGE_BITMAP ||
                msg.what == IDataOperation.REQUEST_TYPE_DECODE_IMAGE_BITMAP) {
            mImageModelHelper.cancelDataOperation(target, msg);
        } else {
            mDataModelHelper.cancelDataOperation(target, msg);
        }
    }

    public Bitmap getBitmap(ImageItem item) {
        return mImageModelHelper.getBitmap(item);
    }

    public ParcelablePoolObject peekPoolObject() {
        return mBundlePool.newObject();
    }

    public void freePoolObject(ParcelablePoolObject obj) {
        mBundlePool.freeObject(obj);
    }
}
