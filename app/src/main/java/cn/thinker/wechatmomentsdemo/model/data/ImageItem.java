package cn.thinker.wechatmomentsdemo.model.data;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;

public class ImageItem extends BaseInfo {

    public int width;
    public int height;
    public String url;
    public String id;

    public ImageItem(int width, int height, String url) {
        this.width = width;
        this.height = height;
        this.url = url;
        this.id = this.url + "_" + this.width + "_" + this.height;
    }

    //only for Parcelable usage.
    private ImageItem() {
    }

    @Override
    protected void readFromParcel(Parcel source) {
        this.width = source.readInt();
        this.height = source.readInt();
        this.url = source.readString();
        this.id = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(url);
        dest.writeString(id);
    }

    @Override
    public void fromJSONData(JSONObject obj) throws JSONException {
    }

    @Override
    public ByteBuffer toByteBuffer(ByteBuffer buffer) {
        buffer = putInt(buffer, width);
        buffer = putInt(buffer, height);
        buffer = putString(buffer, url);
        buffer = putString(buffer, id);
        return buffer;
    }

    @Override
    public void fromByteBuffer(ByteBuffer buffer) {
        width = getInt(buffer);
        height = getInt(buffer);
        url = getString(buffer);
        id = getString(buffer);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ImageItem))
            return false;

        ImageItem target = (ImageItem) o;
        return this.id != null && this.id.equals(target.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {

        @Override
        public ImageItem createFromParcel(Parcel source) {
            ImageItem item = new ImageItem();
            item.readFromParcel(source);
            return item;
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };
}
