package cn.thinker.wechatmomentsdemo.model.image;

import java.io.File;

import cn.thinker.wechatmomentsdemo.StaticApplication;
import cn.thinker.wechatmomentsdemo.common.Constants;
import cn.thinker.wechatmomentsdemo.model.data.ImageItem;

public class ImageItemInfoHelper {
    public static String sCacheDir;
    static {
        sCacheDir = StaticApplication.peekInstance().getCacheDir(Constants.CACHE_IMAGES_TYPE);
        new File(sCacheDir).mkdirs();
    }

    public static boolean isImageExist(ImageItem item) {
        File imageFile = new File(sCacheDir, item.hashCode() + "");
        return imageFile != null && imageFile.exists();
    }

    public static String getImageSavedPath(ImageItem item) {
        return sCacheDir + File.separator + item.hashCode();
    }
}
