package cn.thinker.wechatmomentsdemo;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import cn.thinker.wechatmomentsdemo.common.Constants;
import cn.thinker.wechatmomentsdemo.common.LogLevel;

public class StaticApplication extends Application {

    private static StaticApplication INSTANCE;
    private static final String TAG = "StaticApplication";

    private String mBaseCacheDir;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            LogLevel.setLogLevel(LogLevel.DEBUG);
        } else {
            LogLevel.setLogLevel(LogLevel.FINEST);
        }
        INSTANCE = this;
        initCacheDir();
    }

    public static StaticApplication peekInstance() {
        return INSTANCE;
    }

    private void initCacheDir() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = getExternalCacheDir();
            if (dir != null) {
                mBaseCacheDir = dir.toString();
            }

        } else {
            mBaseCacheDir = getCacheDir().toString();
        }
        File cacheDir = new File(mBaseCacheDir + File.separator + Constants.CACHE_IMAGES_DIR);
        if (cacheDir.mkdirs()) {
            Log.e(TAG, "create cache directory failed!");
        }
    }

    public String getCacheDir(int type) {
        if (type == Constants.CACHE_IMAGES_TYPE) {
            return mBaseCacheDir + File.separator + Constants.CACHE_IMAGES_DIR;
        } else {
            return null;
        }
    }
}
